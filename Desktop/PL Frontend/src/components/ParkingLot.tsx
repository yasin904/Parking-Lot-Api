import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Container,
  Paper,
  TextField,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Card,
  CardContent,
  Snackbar,
  Alert,
  useTheme,
  alpha,
} from '@mui/material';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import TwoWheelerIcon from '@mui/icons-material/TwoWheeler';
import LocalParkingIcon from '@mui/icons-material/LocalParking';
import { parkingService } from '../services/parkingService';
import type { ParkingSlot, VehicleType } from '../types/parking';

const ParkingLot: React.FC = () => {
  const theme = useTheme();
  const [slots, setSlots] = useState<ParkingSlot[]>([]);
  const [createSlotType, setCreateSlotType] = useState<VehicleType>('CAR');
  const [parkVehicleType, setParkVehicleType] = useState<VehicleType>('CAR');
  const [slotCount, setSlotCount] = useState<number>(1);
  const [vehicleNumber, setVehicleNumber] = useState<string>('');
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'error' } | null>(null);

  useEffect(() => {
    fetchAllSlots();
  }, []);

  const fetchAllSlots = async () => {
    try {
      const allSlots = await parkingService.getAllSlots();
      console.log('Fetched slots in component:', allSlots);
      setSlots(allSlots);
    } catch (error) {
      console.error('Error fetching slots:', error);
      showMessage('Error fetching slots', 'error');
    }
  };

  const showMessage = (text: string, type: 'success' | 'error') => {
    setMessage({ text, type });
    setTimeout(() => setMessage(null), 3000);
  };

  const handleCreateSlots = async () => {
    try {
      await parkingService.createSlots({ vehicleType: createSlotType, count: slotCount });
      showMessage('Slots created successfully', 'success');
      fetchAllSlots();
    } catch (error) {
      console.error('Error creating slots:', error);
      showMessage('Error creating slots', 'error');
    }
  };

  const handleAssignParking = async () => {
    try {
      console.log('Parking vehicle:', { type: parkVehicleType, number: vehicleNumber });
      const response = await parkingService.assignParking({
        vehicleType: parkVehicleType,
        vehicleNumber,
      });
      console.log('Parking response:', response);
      
      // Show success message with slot number
      showMessage(`Vehicle parked successfully at slot ${response.slotNumber}`, 'success');
      
      // Update the slots list
      await fetchAllSlots();
      
      // Clear the form
      setVehicleNumber('');
    } catch (error) {
      console.error('Error assigning parking:', error);
      showMessage('Error assigning parking', 'error');
    }
  };

  const handleUnparkVehicle = async (slotId: string) => {
    try {
      console.log('Unparking vehicle from slot:', slotId);
      await parkingService.unparkVehicle(slotId);
      showMessage('Vehicle unparked successfully', 'success');
      fetchAllSlots();
    } catch (error) {
      console.error('Error unparking vehicle:', error);
      showMessage('Error unparking vehicle', 'error');
    }
  };

  const getVehicleIcon = (type: VehicleType | null) => {
    switch (type) {
      case 'CAR':
        return <DirectionsCarIcon sx={{ fontSize: 40, color: theme.palette.primary.main }} />;
      case 'BIKE':
        return <TwoWheelerIcon sx={{ fontSize: 40, color: theme.palette.primary.main }} />;
      default:
        return <LocalParkingIcon sx={{ fontSize: 40, color: theme.palette.text.secondary }} />;
    }
  };

  return (
    <Box sx={{ 
      minHeight: '100vh',
      width: '100vw',
      background: `linear-gradient(135deg, ${alpha(theme.palette.primary.main, 0.1)} 0%, ${alpha(theme.palette.secondary.main, 0.1)} 100%)`,
      py: 4,
      px: 0,
      m: 0,
      overflowX: 'hidden'
    }}>
      <Container maxWidth="lg" sx={{ px: { xs: 2, sm: 3, md: 4 } }}>
        <Box sx={{ 
          display: 'flex', 
          alignItems: 'center', 
          gap: 2, 
          mb: 4 
        }}>
          <LocalParkingIcon sx={{ fontSize: 40, color: theme.palette.primary.main }} />
          <Typography variant="h4" component="h1" sx={{ 
            fontWeight: 'bold',
            background: `linear-gradient(45deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
            backgroundClip: 'text',
            textFillColor: 'transparent',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'
          }}>
            Parking Lot Management
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
          {/* Create Slots Section */}
          <Box sx={{ flex: '1 1 45%', minWidth: '300px' }}>
            <Paper sx={{ 
              p: 3,
              borderRadius: 2,
              boxShadow: theme.shadows[3],
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'translateY(-4px)',
                boxShadow: theme.shadows[6]
              }
            }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                <LocalParkingIcon color="primary" />
                <Typography variant="h6">
                  Create Slots
                </Typography>
              </Box>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel id="create-slot-type-label">Vehicle Type</InputLabel>
                <Select
                  labelId="create-slot-type-label"
                  value={createSlotType}
                  label="Vehicle Type"
                  onChange={(e) => setCreateSlotType(e.target.value as VehicleType)}
                >
                  <MenuItem value="CAR">Car</MenuItem>
                  <MenuItem value="BIKE">Bike</MenuItem>
                </Select>
              </FormControl>
              <TextField
                fullWidth
                type="number"
                label="Number of Slots"
                value={slotCount}
                onChange={(e) => setSlotCount(Number(e.target.value))}
                sx={{ mb: 2 }}
              />
              <Button 
                variant="contained" 
                onClick={handleCreateSlots}
                fullWidth
                sx={{ 
                  py: 1.5,
                  background: `linear-gradient(45deg, ${theme.palette.primary.main}, ${theme.palette.primary.dark})`,
                  '&:hover': {
                    background: `linear-gradient(45deg, ${theme.palette.primary.dark}, ${theme.palette.primary.main})`,
                  }
                }}
              >
                Create Slots
              </Button>
            </Paper>
          </Box>

          {/* Park Vehicle Section */}
          <Box sx={{ flex: '1 1 45%', minWidth: '300px' }}>
            <Paper sx={{ 
              p: 3,
              borderRadius: 2,
              boxShadow: theme.shadows[3],
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'translateY(-4px)',
                boxShadow: theme.shadows[6]
              }
            }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                <DirectionsCarIcon color="primary" />
                <Typography variant="h6">
                  Park Vehicle
                </Typography>
              </Box>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel id="park-vehicle-type-label">Vehicle Type</InputLabel>
                <Select
                  labelId="park-vehicle-type-label"
                  value={parkVehicleType}
                  label="Vehicle Type"
                  onChange={(e) => setParkVehicleType(e.target.value as VehicleType)}
                >
                  <MenuItem value="CAR">Car</MenuItem>
                  <MenuItem value="BIKE">Bike</MenuItem>
                </Select>
              </FormControl>
              <TextField
                fullWidth
                label="Vehicle Number"
                value={vehicleNumber}
                onChange={(e) => setVehicleNumber(e.target.value)}
                sx={{ mb: 2 }}
              />
              <Button 
                variant="contained" 
                color="primary" 
                onClick={handleAssignParking}
                fullWidth
                sx={{ 
                  py: 1.5,
                  background: `linear-gradient(45deg, ${theme.palette.secondary.main}, ${theme.palette.secondary.dark})`,
                  '&:hover': {
                    background: `linear-gradient(45deg, ${theme.palette.secondary.dark}, ${theme.palette.secondary.main})`,
                  }
                }}
              >
                Park Vehicle
              </Button>
            </Paper>
          </Box>

          {/* Slots Display Section */}
          <Box sx={{ flex: '1 1 100%' }}>
            <Paper sx={{ 
              p: 3,
              borderRadius: 2,
              boxShadow: theme.shadows[3]
            }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
                <LocalParkingIcon color="primary" />
                <Typography variant="h6">
                  Parking Slots
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                {/* Occupied Slots */}
                <Box>
                  <Typography variant="subtitle1" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>
                    Occupied Slots ({slots.filter(slot => slot.occupied).length})
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
                    {slots
                      .filter(slot => {
                        console.log('Filtering slot:', {
                          slotNumber: slot.slotNumber,
                          occupied: slot.occupied,
                          vehicleType: slot.vehicleType,
                          vehicleNumber: slot.vehicleNumber
                        });
                        return slot.occupied === true;
                      })
                      .map((slot) => {
                        console.log('Rendering occupied slot:', {
                          slotNumber: slot.slotNumber,
                          occupied: slot.occupied,
                          vehicleType: slot.vehicleType,
                          vehicleNumber: slot.vehicleNumber
                        });
                        return (
                          <Box key={slot.slotNumber} sx={{ flex: '1 1 300px', maxWidth: '400px' }}>
                            <Card sx={{ 
                              borderRadius: 2,
                              boxShadow: theme.shadows[2],
                              transition: 'transform 0.2s',
                              '&:hover': {
                                transform: 'translateY(-4px)',
                                boxShadow: theme.shadows[4]
                              }
                            }}>
                              <CardContent>
                                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                                  {getVehicleIcon(slot.vehicleType)}
                                  <Typography variant="h6" color="primary">
                                    Slot {slot.slotNumber}
                                  </Typography>
                                </Box>
                                <Typography color="textSecondary" gutterBottom>
                                  Type: {slot.vehicleType || 'Not specified'}
                                </Typography>
                                <Typography color="textSecondary" gutterBottom>
                                  Vehicle: {slot.vehicleNumber || 'Not specified'}
                                </Typography>
                                <Button
                                  variant="contained"
                                  color="secondary"
                                  onClick={() => handleUnparkVehicle(slot.slotNumber.toString())}
                                  fullWidth
                                  sx={{ mt: 2 }}
                                >
                                  Unpark
                                </Button>
                              </CardContent>
                            </Card>
                          </Box>
                        );
                      })}
                  </Box>
                </Box>

                {/* Free Slots */}
                <Box>
                  <Typography variant="subtitle1" color="textSecondary" gutterBottom sx={{ fontWeight: 'bold' }}>
                    Free Slots ({slots.filter(slot => !slot.occupied).length})
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
                    {slots
                      .filter(slot => !slot.occupied)
                      .map((slot) => (
                        <Box key={slot.slotNumber} sx={{ flex: '1 1 300px', maxWidth: '400px' }}>
                          <Card sx={{ 
                            borderRadius: 2,
                            boxShadow: theme.shadows[1],
                            transition: 'transform 0.2s',
                            '&:hover': {
                              transform: 'translateY(-4px)',
                              boxShadow: theme.shadows[3]
                            }
                          }}>
                            <CardContent>
                              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                                {getVehicleIcon(slot.vehicleType)}
                                <Typography variant="h6">
                                  Slot {slot.slotNumber}
                                </Typography>
                              </Box>
                              <Typography color="textSecondary" gutterBottom>
                                Type: {slot.vehicleType || 'Not specified'}
                              </Typography>
                              <Typography color="textSecondary">
                                Status: Free
                              </Typography>
                            </CardContent>
                          </Card>
                        </Box>
                      ))}
                  </Box>
                </Box>
              </Box>
            </Paper>
          </Box>
        </Box>

        <Snackbar
          open={!!message}
          autoHideDuration={3000}
          onClose={() => setMessage(null)}
          anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        >
          <Alert 
            severity={message?.type} 
            onClose={() => setMessage(null)}
            sx={{ 
              boxShadow: theme.shadows[3],
              borderRadius: 2
            }}
          >
            {message?.text}
          </Alert>
        </Snackbar>
      </Container>
    </Box>
  );
};

export default ParkingLot; 