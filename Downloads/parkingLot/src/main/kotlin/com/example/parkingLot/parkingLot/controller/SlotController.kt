package com.example.parkingLot.parkingLot.controller

import VehicleType
import com.example.parkingLot.parkingLot.dto.parkingSlotResponse
import com.example.parkingLot.parkingLot.service.parkingService
import org.intellij.lang.annotations.Pattern
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@Validated
@RestController
@RequestMapping("api/parking")
class SlotController(private val parkingService: parkingService) {
    @GetMapping("/slots")
    fun getFilteredSlots(@RequestParam(required = false) type: VehicleType?,
                         @RequestParam(required = false) available: Boolean?):List<parkingSlotResponse>{
        return parkingService.getFilteredSlots(type,available)
    }
}