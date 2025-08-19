import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  List,
  Fab,
  Box,
  Alert,
  CircularProgress,
  Paper,
} from '@mui/material';
import { Add as AddIcon, Refresh as RefreshIcon } from '@mui/icons-material';
import type { Task, CreateTaskRequest, ApiError } from '../types/Task';
import { taskService } from '../services/taskService';
import { TaskItem } from './TaskItem';
import { AddTaskForm } from './AddTaskForm';

export const TaskList: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isAddFormOpen, setIsAddFormOpen] = useState(false);

  const fetchTasks = async (): Promise<void> => {
    try {
      setLoading(true);
      setError(null);
      const fetchedTasks = await taskService.getTasks();
      setTasks(fetchedTasks);
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.message || 'Failed to fetch tasks');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (newTask: CreateTaskRequest): Promise<void> => {
    const createdTask = await taskService.createTask(newTask);
    setTasks(prev => [...prev, createdTask]);
  };

  const handleRefresh = (): void => {
    fetchTasks();
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  if (loading) {
    return (
      <Box sx={{ 
        mt: 6, 
        display: 'flex', 
        flexDirection: 'column',
        alignItems: 'center',
        minHeight: '400px',
        justifyContent: 'center',
        width: '100%',
        px: { xs: 3, sm: 4, md: 6 }
      }}>
        <Box sx={{ textAlign: 'center' }}>
          <CircularProgress size={80} sx={{ color: 'primary.main', mb: 3 }} />
          <Typography variant="h6" color="text.secondary">
            🦕 Dinosaurs are fetching your tasks...
          </Typography>
        </Box>
      </Box>
    );
  }

  return (
    <Box sx={{ mt: 4, pb: 10, px: { xs: 3, sm: 4, md: 6 }, width: '100%' }}>
      <Box 
        display="flex" 
        justifyContent="space-between" 
        alignItems="center" 
        mb={4}
        sx={{
          background: 'linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)',
          p: 3,
          borderRadius: 3,
          boxShadow: '0 4px 20px rgba(46, 125, 79, 0.1)',
          border: '2px solid #e8f5e8',
        }}
      >
        <Box>
          <Typography 
            variant="h4" 
            component="h2" 
            sx={{ 
              color: 'primary.main',
              display: 'flex',
              alignItems: 'center',
              gap: 2,
              mb: 1,
            }}
          >
            📋 Your Tasks
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Manage your prehistoric productivity
          </Typography>
        </Box>
        <Fab
          color="secondary"
          size="medium"
          onClick={handleRefresh}
          disabled={loading}
          sx={{ 
            '&:hover': {
              transform: 'scale(1.1)',
              transition: 'transform 0.2s ease-in-out',
            }
          }}
        >
          <RefreshIcon />
        </Fab>
      </Box>

      {error && (
        <Alert 
          severity="error" 
          sx={{ mb: 3 }}
          onClose={() => setError(null)}
        >
          {error}
        </Alert>
      )}

      {tasks.length === 0 ? (
        <Paper 
          elevation={3} 
          sx={{ 
            p: 6, 
            textAlign: 'center',
            background: 'linear-gradient(135deg, #ffffff 0%, #f1f8e9 100%)',
            border: '2px dashed #4caf50',
            borderRadius: 4,
          }}
        >
          <Typography 
            variant="h3" 
            sx={{ fontSize: '4rem', mb: 2 }}
          >
            🦴
          </Typography>
          <Typography variant="h5" color="primary.main" gutterBottom fontWeight={600}>
            No fossils found yet!
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            Start your prehistoric productivity journey by adding your first task.
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Click the mighty 🦖 button below to get started!
          </Typography>
        </Paper>
      ) : (
        <Paper 
          elevation={2} 
          sx={{ 
            borderRadius: 3,
            overflow: 'hidden',
            border: '1px solid #e8f5e8',
          }}
        >
          <List sx={{ width: '100%', p: 0 }}>
            {tasks.map((task, index) => (
              <Box key={task.id}>
                <TaskItem task={task} />
                {index < tasks.length - 1 && (
                  <Box sx={{ 
                    height: '1px', 
                    background: 'linear-gradient(90deg, transparent 0%, #e8f5e8 50%, transparent 100%)',
                    mx: 3,
                  }} />
                )}
              </Box>
            ))}
          </List>
        </Paper>
      )}

      <Fab
        color="primary"
        onClick={() => setIsAddFormOpen(true)}
        size="large"
        sx={{
          position: 'fixed',
          bottom: 32,
          right: 32,
          background: 'linear-gradient(135deg, #2e7d4f 0%, #4caf50 100%)',
          '&:hover': {
            background: 'linear-gradient(135deg, #1b5e20 0%, #2e7d4f 100%)',
            transform: 'scale(1.1)',
            transition: 'all 0.3s ease-in-out',
          },
          boxShadow: '0 8px 24px rgba(46, 125, 79, 0.4)',
          fontSize: '1.5rem',
        }}
      >
        🦖
      </Fab>

      <AddTaskForm
        open={isAddFormOpen}
        onClose={() => setIsAddFormOpen(false)}
        onSubmit={handleCreateTask}
      />
    </Box>
  );
};