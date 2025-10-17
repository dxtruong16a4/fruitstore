import { httpClient } from './axiosClient';
import type { 
  LoginRequest, 
  RegisterRequest, 
  LoginResponse, 
  User,
  ApiResponse
} from '../types';

// Auth API endpoints
export const authApi = {
  // Login user
  login: (credentials: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    return httpClient.post('/api/auth/login', credentials);
  },

  // Register user
  register: (userData: RegisterRequest): Promise<ApiResponse<LoginResponse>> => {
    return httpClient.post('/api/auth/register', userData);
  },

  // Get user profile
  getProfile: (): Promise<ApiResponse<User>> => {
    return httpClient.get('/api/auth/profile');
  },

  // Update user profile
  updateProfile: (userData: Partial<User>): Promise<ApiResponse<User>> => {
    return httpClient.put('/api/auth/profile', userData);
  },

  // Change password
  changePassword: (passwordData: { currentPassword: string; newPassword: string }): Promise<ApiResponse<void>> => {
    return httpClient.post('/api/auth/change-password', passwordData);
  },

  // Get current user
  getCurrentUser: (): Promise<ApiResponse<User>> => {
    return httpClient.get('/api/auth/me');
  },
};
