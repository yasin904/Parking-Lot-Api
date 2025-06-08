package com.example.parkingLot.parkingLot.dao.Impl

import VehicleType
import com.example.parkingLot.parkingLot.dao.VehicleDao
import com.example.parkingLot.parkingLot.jooq.public_.tables.records.VehicleRecord
import com.example.parkingLot.parkingLot.jooq.public_.Tables.VEHICLE
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class VehicleDaoImpl(@Autowired private val dsl: DSLContext): VehicleDao {
    private val vehicle = VEHICLE
    override fun parkVehicle(registrationNumber: String, type: VehicleType, parkingSlotId: Int):Int {
        return dsl.insertInto(vehicle)
            .set(vehicle.REGISTRATION_NUMBER,registrationNumber)
            .set(vehicle.VEHICLE_TYPE,type.name)
            .set(vehicle.PARKING_SLOT_ID,parkingSlotId)
            .execute()
    }

    override fun unparkVehicle(slotId: Int): Int {
        return dsl.deleteFrom(vehicle)
            .where(vehicle.PARKING_SLOT_ID.eq(slotId))
            .execute()
    }

    override fun findByRegistrationNumber(registrationNumber: String): VehicleRecord? {
        return dsl.selectFrom(vehicle)
            .where(vehicle.REGISTRATION_NUMBER.eq(registrationNumber))
            .fetchOne()
    }

    override fun findBySlotId(slotId: Int): VehicleRecord? {
        return dsl.selectFrom(vehicle)
            .where(vehicle.PARKING_SLOT_ID.eq(slotId))
            .fetchOne()
    }

    override fun removeBySlotId(slotId: Int): Int {
        return dsl.deleteFrom(vehicle)
            .where(vehicle.PARKING_SLOT_ID.eq(slotId))
            .execute()
    }

}