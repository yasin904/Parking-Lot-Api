package com.example.parkingLot.parkingLot.dao

import VehicleType
import com.example.parkingLot.parkingLot.jooq.public_.tables.records.VehicleRecord

interface VehicleDao {
    fun parkVehicle(registrationNumber :String,type:VehicleType,parkingSlotId:Int):Int
    fun unparkVehicle(slotId:Int):Int
    fun findByRegistrationNumber(registrationNumber : String): VehicleRecord?
    fun findBySlotId(slotId:Int):VehicleRecord?
    fun removeBySlotId(slotId:Int):Int

}