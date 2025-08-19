import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Chip,
  Container,
} from '@mui/material';
import { 
  CheckCircle as TaskIcon,
  Speed as QuarkusIcon,
} from '@mui/icons-material';

export const AppHeader: React.FC = () => {
  return (
    <AppBar 
      position="static" 
      elevation={0}
      sx={{ 
        background: 'linear-gradient(135deg, #2e7d4f 0%, #4caf50 100%)',
        borderBottom: '3px solid #ff8f00',
      }}
    >
      <Container>
        <Toolbar sx={{ py: 2, px: 0, flex: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', flex: 1 }}>
            <Typography 
              variant="h3" 
              component="h1" 
              sx={{ 
                display: 'flex', 
                alignItems: 'center',
                gap: 2,
                color: 'white',
                fontWeight: 800,
              }}
            >
              🦖 Quarkusaurus
            </Typography>
            <Box sx={{ ml: 3, display: 'flex', gap: 1 }}>
              <Chip
                icon={<TaskIcon />}
                label="Task Manager"
                variant="outlined"
                sx={{
                  color: 'white',
                  borderColor: 'rgba(255, 255, 255, 0.5)',
                  '& .MuiChip-icon': { color: 'white' }
                }}
              />
              <Chip
                icon={<QuarkusIcon />}
                label="Quarkus + React"
                variant="outlined"
                sx={{
                  color: 'white',
                  borderColor: 'rgba(255, 255, 255, 0.5)',
                  '& .MuiChip-icon': { color: 'white' }
                }}
              />
            </Box>
          </Box>
          <Typography 
            variant="h6" 
            sx={{ 
              color: 'rgba(255, 255, 255, 0.9)', 
              fontStyle: 'italic',
              display: { xs: 'none', md: 'block' }
            }}
          >
            Roar into productivity! 🦕
          </Typography>
        </Toolbar>
      </Container>
    </AppBar>
  );
};