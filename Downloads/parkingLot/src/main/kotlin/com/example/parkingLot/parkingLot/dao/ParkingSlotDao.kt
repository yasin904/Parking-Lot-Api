package com.example.parkingLot.parkingLot.dao

import VehicleType
import com.example.parkingLot.parkingLot.jooq.public_.tables.records.ParkingSlotRecord
import org.jooq.Record
import org.jooq.Result

interface ParkingSlotDao {
    fun createSlot(slotNumber : Int,vehicleType : VehicleType):Int
    fun getAll():List<ParkingSlotRecord>
    fun getAllSlots(): Result<Record>
    fun getBySlotNumber(slotNumber: Int): ParkingSlotRecord?
    fun findFirstFreeSlot(type : VehicleType): ParkingSlotRecord?
    fun markOccupied(id:Int):Int
    fun markFree(id:Int):Int
    fun deleteByType(type:VehicleType):Int
    fun getSlotsByType(type:VehicleType):List<ParkingSlotRecord>
}