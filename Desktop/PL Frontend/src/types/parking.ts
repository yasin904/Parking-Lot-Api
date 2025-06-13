export type VehicleType = 'CAR' | 'BIKE';

export interface ParkingSlot {
  slotNumber: number;
  occupied: boolean;
  vehicleType: VehicleType | null;
  vehicleNumber: string | null;
}

export interface CreateSlotsRequest {
  vehicleType: VehicleType;
  count: number;
}

export interface RemoveSlotsRequest {
  count: number;
}

export interface GetSlotsRequest {
  vehicleType: VehicleType;
}

export interface AssignParkingRequest {
  vehicleType: VehicleType;
  vehicleNumber: string;
}

export interface UnparkVehicleRequest {
  vehicleNumber: string;
} 