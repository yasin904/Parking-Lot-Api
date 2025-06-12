package com.example.parkingLot.parkingLot.service.Impl

import VehicleType
import com.example.parkingLot.parkingLot.Manager.SlotManager
import com.example.parkingLot.parkingLot.dto.*
import com.example.parkingLot.parkingLot.service.parkingService
import org.springframework.stereotype.Service

@Service
class parkingServiceImpl(
    private val slotManager : SlotManager
) : parkingService {
    override fun createSlots(count: Int,type: VehicleType): String {
        return slotManager.createSlots(count,type)
    }

    override fun parkVehicle(request: parkVehicleRequest): parkVehicleResponse {
        return slotManager.assignSlot(request)
    }

    override fun unParkVehicle(slotNumber: Int): String {
        return slotManager.releaseSlot(slotNumber)
    }

    override fun getStatus(): List<parkingSlotResponse> {
        return slotManager.getStatus()
    }

    override fun getFilteredSlots(type: VehicleType?, available: Boolean?): List<parkingSlotResponse> {
        return slotManager.getAllSlots()
            .filter{(type == null || it.type == type) && (available == null || !it.isOccupied == available)}
            .map { slot -> parkingSlotResponse(
                slotNumber = slot.slotNumber,
                occupied = slot.isOccupied,
                vehicleType = slot.vehicle?.vehicleType?.toString(),
                VehicleNumber = slot.vehicle?.vehicleNumber
            ) }
    }

    override fun removeSlots( type: VehicleType): String {
       return slotManager.removeSlotsComplete(type)
    }

    override fun removePartialSlots(type: VehicleType, count: Int): String {
        return slotManager.removePartialSlotsByType(type,count)
    }

}