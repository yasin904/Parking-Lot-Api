package com.example.parkingLot.parkingLot.service

import Vehicle
import VehicleType
import com.example.parkingLot.parkingLot.dto.*

interface parkingService {
    fun createSlots(count:Int,type: VehicleType):String

    fun parkVehicle(request : parkVehicleRequest):parkVehicleResponse

    fun unParkVehicle(slotNumber: Int):String

    fun getStatus():List<parkingSlotResponse>

    fun getFilteredSlots(type : VehicleType?,available : Boolean?): List<parkingSlotResponse>

    fun removeSlots(type: VehicleType):String
}