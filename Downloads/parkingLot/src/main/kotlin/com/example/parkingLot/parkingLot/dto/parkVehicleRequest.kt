package com.example.parkingLot.parkingLot.dto

import VehicleType
import jakarta.validation.constraints.NotBlank

data class parkVehicleRequest(
    @field:NotBlank(message = "Vehicle number is required")
    val number : String,
    @field:NotBlank(message = "Vehicle type is required")
    val type: VehicleType
)
