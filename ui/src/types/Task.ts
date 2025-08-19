export interface Task {
  id: string;
  title: string;
  description: string;
  createdAt?: string | null;
  updatedAt?: string | null;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
}

export interface ApiError {
  message: string;
  status: number;
}