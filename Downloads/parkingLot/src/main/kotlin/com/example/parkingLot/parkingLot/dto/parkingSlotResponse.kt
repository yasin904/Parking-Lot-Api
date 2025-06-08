package com.example.parkingLot.parkingLot.dto

data class parkingSlotResponse(
    val slotNumber : Int,
    val occupied : Boolean,
    val VehicleNumber : String? = null,
    val vehicleType : String? = null
)