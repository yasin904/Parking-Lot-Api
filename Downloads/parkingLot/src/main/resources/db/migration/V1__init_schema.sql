-- Parking Slot
CREATE TABLE parking_slot(
id SERIAL PRIMARY KEY,
slot_number INT NOT NULL,
vehicle_type VARCHAR(20) NOT NULL,
is_occupied BOOLEAN DEFAULT FALSE,
UNIQUE(slot_number,vehicle_type)
);

--vehicle
CREATE TABLE vehicle(
id SERIAL PRIMARY KEY,
registration_number VARCHAR(20) UNIQUE NOT NULL,
vehicle_type VARCHAR(20) NOT NULL,
parking_slot_id INT UNIQUE REFERENCES parking_slot(id) ON DELETE SET NULL
);

