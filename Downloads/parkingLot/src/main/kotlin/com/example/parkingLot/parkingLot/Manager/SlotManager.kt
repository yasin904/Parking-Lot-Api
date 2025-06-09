package com.example.parkingLot.parkingLot.Manager

import Vehicle
import VehicleType
import com.example.parkingLot.parkingLot.dao.ParkingSlotDao
import com.example.parkingLot.parkingLot.dao.VehicleDao
import com.example.parkingLot.parkingLot.dto.*
import com.example.parkingLot.parkingLot.exception.InvalidSlotException
import com.example.parkingLot.parkingLot.exception.SlotNotAvailableException
import com.example.parkingLot.parkingLot.exception.VehicleAlreadyParkedException
import com.example.parkingLot.parkingLot.jooq.public_.Tables.PARKING_SLOT
import com.example.parkingLot.parkingLot.jooq.public_.Tables.VEHICLE
import com.example.parkingLot.parkingLot.model.parkingSlot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Component
class SlotManager() {
    @Autowired
    lateinit var parkingSlotDao : ParkingSlotDao
    @Autowired
    lateinit var vehicleDao : VehicleDao
    private val lock = ReentrantLock()

     fun createSlots(count : Int,type : VehicleType): String = lock.withLock {
        val existingSlots = parkingSlotDao.getSlotsByType(type)
         val currentCount = existingSlots.size
        for(i in 1..count){
            parkingSlotDao.createSlot(currentCount+i,type)
        }
        return "Created $count slots for $type"
    }


     fun assignSlot(request: parkVehicleRequest): parkVehicleResponse = lock.withLock {
        if(vehicleDao.findByRegistrationNumber(request.number)!=null){
            throw VehicleAlreadyParkedException("Vehicle already parked.")
        }
        val freeSlot = parkingSlotDao.findFirstFreeSlot(request.type)?:
        throw SlotNotAvailableException("slot not available for ${request.type}")
         parkingSlotDao.markOccupied(freeSlot.id)
         vehicleDao.parkVehicle(request.number,request.type,freeSlot.id)
         return parkVehicleResponse(
             "Vehicle parked at slot ${freeSlot.slotNumber}",
             freeSlot.slotNumber
         )
    }


     fun releaseSlot(slotNumber: Int): String = lock.withLock {
         val slot = parkingSlotDao.getBySlotNumber(slotNumber)?:
        throw InvalidSlotException("parking lot is full")
        if(!slot.isOccupied)
            throw SlotNotAvailableException("Slot is already empty")

        vehicleDao.removeBySlotId(slot.id)
         parkingSlotDao.markFree(slot.id)
        return "Slot $slotNumber is now free"
    }

     fun getStatus(): List<parkingSlotResponse> = lock.withLock {
         val slots = parkingSlotDao.getAll()
        return slots.map {
            val vehicle = vehicleDao.findBySlotId(it.id)
           parkingSlotResponse(

               slotNumber = it.slotNumber,
               occupied = it.isOccupied,
               VehicleNumber = vehicle?.registrationNumber,
               vehicleType = vehicle?.vehicleType
           )
        }
    }
    fun getAllSlots():List<parkingSlot> = lock.withLock{
        return parkingSlotDao.getAllSlots().map{record ->
            val slotRecord = record.into(PARKING_SLOT)
            val vehicleRecord = record.into(VEHICLE)
            parkingSlot(
                slotNumber = slotRecord.slotNumber,
                isOccupied = slotRecord.isOccupied,
                type = VehicleType.valueOf(slotRecord.vehicleType),
                vehicle = if(slotRecord.isOccupied&&vehicleRecord.registrationNumber!=null){
                    Vehicle(
                        vehicleNumber = vehicleRecord.registrationNumber,
                        vehicleType = vehicleRecord.vehicleType
                    )
                } else null


            )
        }
    }

    fun removeSlotsComplete(type: VehicleType):String = lock.withLock {
        val slotsToRemove = parkingSlotDao.getSlotsByType(type)
        val occupiedSlots = slotsToRemove.filter{it.isOccupied}

        if (slotsToRemove.isEmpty()){
            throw IllegalStateException("No slots found for vehicle type: $type")
        }

        if(occupiedSlots.isNotEmpty()){
            val occupiedSlotNumber = occupiedSlots.joinToString(","){it.slotNumber.toString()}
           return@withLock "Cannot remove slots for $type. The following slots are still occupied: $occupiedSlotNumber. Please unpark them first."
        }
        else {
            parkingSlotDao.deleteByType(type)
            return@withLock "Removed ${slotsToRemove.size} slots for $type"
        }
    }
    fun removePartialSlotsByType(type:VehicleType,count:Int):String = lock.withLock {
        val unOccupiedSlots = parkingSlotDao.getUnOccupiedBySlotsByType(type)
        if(unOccupiedSlots.size<count) {
            throw IllegalStateException("Only ${unOccupiedSlots.size} unoccupied slots available, but request to remove $count.")
        }
            val slotsToRemove = unOccupiedSlots.take(count)
            parkingSlotDao.deleteBySlotNumber(slotsToRemove)
            return "Removed ${slotsToRemove.size} unoccupied slots for $type"

    }

}