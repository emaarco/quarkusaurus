import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Alert,
} from '@mui/material';
import type { CreateTaskRequest, ApiError } from '../types/Task';

interface AddTaskFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (task: CreateTaskRequest) => Promise<void>;
}

export const AddTaskForm: React.FC<AddTaskFormProps> = ({
  open,
  onClose,
  onSubmit,
}) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();
    
    if (!title.trim() || !description.trim()) {
      setError('Both title and description are required');
      return;
    }

    setIsSubmitting(true);
    setError(null);

    try {
      await onSubmit({
        title: title.trim(),
        description: description.trim(),
      });
      
      // Reset form
      setTitle('');
      setDescription('');
      onClose();
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.message || 'Failed to create task');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = (): void => {
    if (!isSubmitting) {
      setTitle('');
      setDescription('');
      setError(null);
      onClose();
    }
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose} 
      maxWidth="sm" 
      fullWidth
      PaperProps={{
        sx: {
          borderRadius: 3,
          background: 'linear-gradient(135deg, #ffffff 0%, #f1f8e9 100%)',
          border: '2px solid #e8f5e8',
        }
      }}
    >
      <form onSubmit={handleSubmit}>
        <DialogTitle 
          sx={{ 
            background: 'linear-gradient(135deg, #2e7d4f 0%, #4caf50 100%)',
            color: 'white',
            display: 'flex',
            alignItems: 'center',
            gap: 2,
            fontSize: '1.5rem',
            fontWeight: 600,
          }}
        >
          🦖 Add New Fossil Task
        </DialogTitle>
        <DialogContent sx={{ pt: 3 }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
            {error && (
              <Alert 
                severity="error" 
                onClose={() => setError(null)}
                sx={{ borderRadius: 2 }}
              >
                {error}
              </Alert>
            )}
            <TextField
              label="🏷️ Task Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              fullWidth
              required
              disabled={isSubmitting}
              placeholder="What prehistoric task needs to be done?"
              sx={{
                  marginTop: 4,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                  '&:hover .MuiOutlinedInput-notchedOutline': {
                    borderColor: 'primary.main',
                  },
                },
              }}
            />
            <TextField
              label="📝 Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              fullWidth
              required
              multiline
              rows={4}
              disabled={isSubmitting}
              placeholder="Roar into the details of your task..."
              sx={{
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                  '&:hover .MuiOutlinedInput-notchedOutline': {
                    borderColor: 'primary.main',
                  },
                },
              }}
            />
          </Box>
        </DialogContent>
        <DialogActions sx={{ p: 3, gap: 2 }}>
          <Button 
            onClick={handleClose} 
            disabled={isSubmitting}
            variant="outlined"
            sx={{ 
              borderRadius: 2,
              px: 3,
              borderColor: 'primary.main',
              color: 'primary.main',
              '&:hover': {
                borderColor: 'primary.dark',
                backgroundColor: 'rgba(46, 125, 79, 0.04)',
              }
            }}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            disabled={isSubmitting || !title.trim() || !description.trim()}
            sx={{
              borderRadius: 2,
              px: 4,
              background: 'linear-gradient(135deg, #2e7d4f 0%, #4caf50 100%)',
              '&:hover': {
                background: 'linear-gradient(135deg, #1b5e20 0%, #2e7d4f 100%)',
                transform: 'translateY(-2px)',
                boxShadow: '0 8px 16px rgba(46, 125, 79, 0.3)',
              },
              '&:disabled': {
                background: 'rgba(0, 0, 0, 0.12)',
              },
              transition: 'all 0.2s ease-in-out',
            }}
          >
            {isSubmitting ? '🦕 Creating...' : '🦖 Create Task'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};