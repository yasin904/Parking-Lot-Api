import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import ParkingLot from './components/ParkingLot';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <ParkingLot />
    </ThemeProvider>
  );
}

export default App;
