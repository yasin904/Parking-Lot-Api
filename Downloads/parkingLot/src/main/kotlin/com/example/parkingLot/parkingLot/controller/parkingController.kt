package com.example.parkingLot.parkingLot.controller

import Vehicle
import VehicleType
import com.example.parkingLot.parkingLot.service.Impl.parkingServiceImpl
import com.example.parkingLot.parkingLot.dto.parkVehicleRequest
import com.example.parkingLot.parkingLot.dto.parkVehicleResponse
import com.example.parkingLot.parkingLot.dto.parkingSlotResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/parking")
class parkingController(
    private val parkingService : parkingServiceImpl
) {
    @PostMapping("/create")
    fun create( @RequestParam count : Int,@RequestParam type:VehicleType):ResponseEntity<String>{
        return ResponseEntity.ok(parkingService.createSlots(count,type))
    }
    @PostMapping("/park")
    fun park(@RequestBody@Valid request: parkVehicleRequest):ResponseEntity<parkVehicleResponse>{
        val response = parkingService.parkVehicle(request)
        val status = if(response.slotNumber == -1) HttpStatus.BAD_REQUEST else HttpStatus.OK
        return ResponseEntity(response,status)
    }
    @PostMapping("/unpark/{parkingSlot}")
    fun unPark(@PathVariable("parkingSlot") slot:Int):ResponseEntity<String>{
        val message = parkingService.unParkVehicle(slot)
        return ResponseEntity.ok(message)
    }
    @GetMapping("/status")
    fun getStatus():ResponseEntity<List<parkingSlotResponse>>{
        val status = parkingService.getStatus()
        return ResponseEntity.ok(status)
    }
    @PostMapping("/remove-slot")
    fun removeAllSlots(@RequestParam type:VehicleType):ResponseEntity<String>{
        return ResponseEntity.ok(parkingService.removeSlots(type))
    }

}