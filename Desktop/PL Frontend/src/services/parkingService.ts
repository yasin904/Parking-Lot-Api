import axios from 'axios';
import type { 
  ParkingSlot, 
  CreateSlotsRequest, 
  RemoveSlotsRequest, 
  GetSlotsRequest,
  AssignParkingRequest,
  UnparkVehicleRequest
} from '../types/parking';

// Update this URL to match your backend server
const API_BASE_URL = 'http://localhost:8080/api/parking';

export const parkingService = {
  createSlots: async (request: CreateSlotsRequest): Promise<ParkingSlot[]> => {
    console.log('Creating slots with request:', request);
    const response = await axios.post(`${API_BASE_URL}/create`, null, {
      params: {
        type: request.vehicleType,
        count: request.count
      }
    });
    console.log('Create slots response:', response.data);
    return response.data;
  },

  removeAllSlots: async (): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/slots/remove-all`);
  },

  removePartialSlots: async (request: RemoveSlotsRequest): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/slots/remove-partial`, { data: request });
  },

  getAllSlots: async (): Promise<ParkingSlot[]> => {
    console.log('Fetching all slots');
    const response = await axios.get(`${API_BASE_URL}/slots`);
    console.log('Raw response from backend:', response.data);
    
    // Log each slot's data
    response.data.forEach((slot: any, index: number) => {
      console.log(`Slot ${index} data:`, {
        slotNumber: slot.slotNumber,
        occupied: slot.occupied,
        vehicleType: slot.vehicleType,
        vehicleNumber: slot.vehicleNumber,
        rawSlot: slot
      });
    });
    
    // Map the response data to ensure it matches our ParkingSlot interface
    const slots = response.data.map((slot: any) => {
      const mappedSlot = {
        slotNumber: slot.slotNumber,
        occupied: slot.occupied,
        vehicleType: slot.vehicleType || null,
        vehicleNumber: slot.vehicleNumber || null
      };
      console.log('Mapped slot:', mappedSlot);
      return mappedSlot;
    });
    
    console.log('Final mapped slots:', slots);
    return slots;
  },

  getSlotsByType: async (request: GetSlotsRequest): Promise<ParkingSlot[]> => {
    const response = await axios.get(`${API_BASE_URL}/slots/filter`, { params: request });
    return response.data;
  },

  getStatus: async (): Promise<ParkingSlot[]> => {
    const response = await axios.get(`${API_BASE_URL}/status`);
    return response.data;
  },

  assignParking: async (request: AssignParkingRequest): Promise<ParkingSlot> => {
    console.log('Assigning parking with request:', request);
    const response = await axios.post(`${API_BASE_URL}/park`, {
      number: request.vehicleNumber,
      type: request.vehicleType
    });
    console.log('Assign parking response:', response.data);
    
    // After parking, fetch the updated slot data
    const allSlots = await axios.get(`${API_BASE_URL}/slots`);
    console.log('Slots after parking:', allSlots.data);
    
    const updatedSlot = allSlots.data.find((slot: ParkingSlot) => slot.slotNumber === response.data.slotNumber);
    console.log('Found updated slot:', updatedSlot);
    
    if (!updatedSlot) {
      throw new Error('Failed to fetch updated slot data');
    }
    
    return updatedSlot;
  },

  unparkVehicle: async (slotId: string): Promise<ParkingSlot> => {
    console.log('Unparking vehicle from slot:', slotId);
    const response = await axios.post(`${API_BASE_URL}/unpark/${slotId}`);
    console.log('Unpark response:', response.data);
    return response.data;
  }
}; 