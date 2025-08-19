import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline, Box } from '@mui/material';
import { TaskList } from './components/TaskList';
import { AppHeader } from './components/AppHeader';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#2e7d4f', // Forest green for dinosaur theme
      light: '#4caf50',
      dark: '#1b5e20',
    },
    secondary: {
      main: '#ff8f00', // Amber for dinosaur theme
      light: '#ffc947',
      dark: '#c56000',
    },
    background: {
      default: '#f1f8e9', // Very light green background
      paper: '#ffffff',
    },
    success: {
      main: '#4caf50',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h3: {
      fontWeight: 700,
      fontSize: '2.5rem',
    },
    h4: {
      fontWeight: 600,
      fontSize: '2rem',
    },
    h5: {
      fontWeight: 600,
      fontSize: '1.5rem',
    },
    h6: {
      fontWeight: 500,
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiPaper: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 8px rgba(46, 125, 79, 0.1)',
        },
      },
    },
    MuiFab: {
      styleOverrides: {
        root: {
          boxShadow: '0 4px 16px rgba(46, 125, 79, 0.3)',
        },
      },
    },
  },
});

const App: React.FC = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ 
        minHeight: '100vh',
        minWidth: '100vw',
        backgroundColor: 'background.default',
        display: 'flex',
        flexDirection: 'column',
      }}>
        <AppHeader />
        <Box sx={{ flex: 1 }}>
          <TaskList />
        </Box>
      </Box>
    </ThemeProvider>
  );
};

export default App;
