package com.example.parkingLot.parkingLot.dao.Impl

import VehicleType
import com.example.parkingLot.parkingLot.dao.ParkingSlotDao
import com.example.parkingLot.parkingLot.jooq.public_.Tables.PARKING_SLOT
import com.example.parkingLot.parkingLot.jooq.public_.Tables.VEHICLE
import com.example.parkingLot.parkingLot.jooq.public_.tables.ParkingSlot
import com.example.parkingLot.parkingLot.jooq.public_.tables.records.ParkingSlotRecord
import com.example.parkingLot.parkingLot.mapper.Mapper
import com.example.parkingLot.parkingLot.model.parkingSlot
import com.example.parkingLot.parkingLot.mapper.Mapper.toDomain
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.TableField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ParkingSlotDaoImpl(@Autowired private val dsl:DSLContext) : ParkingSlotDao {
    private val parking_slot = PARKING_SLOT
    private val vehicle = VEHICLE
    override fun createSlot(count: Int, vehicleType: VehicleType): Int {
       return dsl.insertInto(parking_slot)
                .set(parking_slot.VEHICLE_TYPE, vehicleType.name)
                .set(parking_slot.IS_OCCUPIED, false)
                .execute()
    }

    override fun getAll(): List<ParkingSlotRecord> {
        return dsl.selectFrom(parking_slot).fetch()
    }

    override fun getAllSlots(): Result<Record> {
        return dsl.select()
            .from(parking_slot)
            .leftJoin(vehicle).on(parking_slot.ID.eq(vehicle.PARKING_SLOT_ID))
            .orderBy(parking_slot.SLOT_NUMBER)
            .fetch()

    }

    override fun getBySlotNumber(slotNumber: Int): ParkingSlotRecord? {
       return dsl.selectFrom(parking_slot)
           .where(parking_slot.SLOT_NUMBER.eq(slotNumber))
           .fetchOne()
    }

    override fun findFirstFreeSlot(type: VehicleType): ParkingSlotRecord? {
        return dsl.selectFrom(parking_slot)
            .where(
                parking_slot.VEHICLE_TYPE.eq(type.name)
                    .and(parking_slot.IS_OCCUPIED.isFalse)
            )
            .orderBy(parking_slot.SLOT_NUMBER.asc())
            .limit(1)
            .fetchOne()
    }

    override fun markOccupied(id: Int): Int {
        return dsl.update(parking_slot)
            .set(parking_slot.IS_OCCUPIED,true)
            .where(parking_slot.ID.eq(id))
            .execute()

    }

    override fun markFree(id: Int): Int {
        return dsl.update(parking_slot)
            .set(parking_slot.IS_OCCUPIED,false)
            .where(parking_slot.ID.eq(id))
            .execute()
    }

    override fun deleteByType(type: VehicleType): Int {
        return dsl.deleteFrom(parking_slot)
            .where(parking_slot.VEHICLE_TYPE.eq(type.name))
            .execute()
    }

    override fun getSlotsByType(type: VehicleType): List<ParkingSlotRecord> {
        return dsl.selectFrom(parking_slot)
            .where(parking_slot.VEHICLE_TYPE.eq(type.name))
            .fetch()
    }

    override fun getUnOccupiedBySlotsByType(type: VehicleType): List<Int> {
        return dsl.select(parking_slot.SLOT_NUMBER)
            .from(parking_slot)
            .where(parking_slot.VEHICLE_TYPE.eq(type.name))
            .and(parking_slot.IS_OCCUPIED.isFalse)
            .fetch(parking_slot.SLOT_NUMBER)



    }

    override fun deleteBySlotNumber(slotNumber: List<Int>) {
        dsl.deleteFrom(parking_slot)
            .where(parking_slot.SLOT_NUMBER.`in`(slotNumber))
            .execute()
    }



}