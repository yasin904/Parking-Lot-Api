package com.example.parkingLot.parkingLot.dto

import VehicleType

data class parkVehicleResponse(
    val message : String,
    val slotNumber : Int,
    val vehicleType:VehicleType,
    val vehicleNumber : String,

)