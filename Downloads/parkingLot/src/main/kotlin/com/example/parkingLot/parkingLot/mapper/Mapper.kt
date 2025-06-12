package com.example.parkingLot.parkingLot.mapper

import com.example.parkingLot.parkingLot.dao.ParkingSlotDao
import com.example.parkingLot.parkingLot.model.parkingSlot
import com.example.parkingLot.parkingLot.jooq.public_.tables.records.ParkingSlotRecord

object Mapper {
    fun toDomain(record:ParkingSlotRecord):parkingSlot{
        return parkingSlot(
            slotNumber = record.slotNumber,
            isOccupied = record.isOccupied,
            vehicle = null,
            type = VehicleType.valueOf(record.vehicleType)
        )
    }

    fun toRecord(slot:parkingSlot):ParkingSlotRecord{
        val record = ParkingSlotRecord()
        record.slotNumber = slot.slotNumber
        record.isOccupied = slot.isOccupied
        record.vehicleType = slot.type.name
        return record


    }
}