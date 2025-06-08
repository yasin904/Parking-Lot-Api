package com.example.parkingLot.parkingLot.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(SlotNotAvailableException::class)
    fun handleSlotNotAvailable(ex:SlotNotAvailableException):ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)

    @ExceptionHandler(InvalidSlotException::class)
    fun handleInvalidSlot(ex:InvalidSlotException):ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)

    @ExceptionHandler(VehicleAlreadyParkedException::class)
    fun handleVehicleAlreadyParked(ex:VehicleAlreadyParkedException):ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred: ${ex.message}")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex : MethodArgumentNotValidException):ResponseEntity<Map<String,String>>{
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage?:"Invalid value") }
        return ResponseEntity.badRequest().body(errors)
    }
}