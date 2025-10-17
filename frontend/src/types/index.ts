// Common types for the application

export interface User {
  userId: number;
  id?: number; // Keep for compatibility
  username: string;
  email: string;
  fullName: string;
  firstName?: string; // Keep for compatibility
  lastName?: string; // Keep for compatibility
  role: 'customer' | 'admin';
  isActive?: boolean; // Keep for compatibility
  createdAt: string;
  updatedAt?: string; // Keep for compatibility
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  phone?: string;
  address?: string;
}

export interface LoginResponse {
  token: string;
  tokenType?: string;
  userId: number;
  username: string;
  email: string;
  fullName: string;
  role: 'customer' | 'admin';
  user?: User; // Keep for compatibility
}

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp: string;
}

export interface PaginationParams {
  page: number;
  size: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

// Error types
export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
  path: string;
}
