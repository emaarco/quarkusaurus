import React from 'react';
import {
  ListItem,
  Typography,
  Box,
} from '@mui/material';
import type { Task } from '../types/Task';

interface TaskItemProps {
  task: Task;
}

export const TaskItem: React.FC<TaskItemProps> = ({ task }) => {
  const formatDate = (dateString: string | null | undefined): string => {
    if (!dateString) return 'No date';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <ListItem 
      sx={{ 
        p: 3,
        '&:hover': {
          backgroundColor: 'rgba(76, 175, 80, 0.04)',
          transition: 'background-color 0.2s ease-in-out',
        }
      }}
    >
      <Box display="flex" alignItems="flex-start" gap={3} width="100%">
        <Box
          sx={{
            backgroundColor: 'primary.secondary',
            borderRadius: '50%',
            width: 48,
            height: 48,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexShrink: 0,
            fontSize: '1.5rem',
          }}
        >
          🦕
        </Box>
        <Box flex={1}>
          <Typography 
            variant="h6" 
            component="h3" 
            gutterBottom
            sx={{ 
              color: 'primary.main',
              fontWeight: 600,
              fontSize: '1.25rem',
            }}
          >
            {task.title}
          </Typography>
          <Typography
            variant="body1"
            color="text.secondary"
            sx={{ mb: 2, lineHeight: 1.6 }}
          >
            {task.description}
          </Typography>
          <Box 
            sx={{ 
              display: 'flex', 
              alignItems: 'center', 
              gap: 1,
              px: 2,
              py: 1,
              backgroundColor: 'rgba(46, 125, 79, 0.05)',
              borderRadius: 2,
              width: 'fit-content',
            }}
          >
            <Typography 
              variant="caption" 
              sx={{ 
                color: 'primary.dark',
                fontWeight: 500,
                fontSize: '0.75rem',
              }}
            >
              🗓️ Created: {formatDate(task.createdAt)}
            </Typography>
          </Box>
        </Box>
      </Box>
    </ListItem>
  );
};