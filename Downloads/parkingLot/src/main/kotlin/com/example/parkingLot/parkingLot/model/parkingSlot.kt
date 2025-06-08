package com.example.parkingLot.parkingLot.model

import Vehicle
import VehicleType

data class parkingSlot(
    val slotNumber : Int,
    var isOccupied : Boolean = false,
    var vehicle : Vehicle? = null,
    val type : VehicleType

)