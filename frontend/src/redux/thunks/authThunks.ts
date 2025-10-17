import { createAsyncThunk } from '@reduxjs/toolkit';
import { authApi } from '../../api/authApi';
import { loginSuccess, loginFailure, logout, updateUser } from '../slices/authSlice';
import { loadCartFromBackend } from './cartThunks';
import type { User, LoginRequest } from '../../types';

// Thunk to restore authentication state from token
export const restoreAuth = createAsyncThunk(
  'auth/restoreAuth',
  async (_, { dispatch, rejectWithValue }) => {
    try {
      const token = localStorage.getItem('token');
      
      if (!token) {
        return rejectWithValue('No token found');
      }

      // Verify token by calling a protected endpoint
      const response = await authApi.getCurrentUser();
      
      if (response.success && response.data) {
        dispatch(loginSuccess({ 
          user: response.data, 
          token 
        }));
        // Load cart from backend after successful authentication
        dispatch(loadCartFromBackend());
        return response.data;
      } else {
        // Token is invalid, clear it
        dispatch(logout());
        return rejectWithValue('Invalid token');
      }
    } catch (error: any) {
      // Token is invalid or expired, clear it
      dispatch(logout());
      return rejectWithValue(error.message || 'Authentication failed');
    }
  }
);

// Thunk to login
export const loginUser = createAsyncThunk(
  'auth/loginUser',
  async (credentials: LoginRequest, { dispatch, rejectWithValue }) => {
    try {
      const response = await authApi.login(credentials);
      
      if (response.success && response.data && response.data.user) {
        dispatch(loginSuccess({
          user: response.data.user,
          token: response.data.token
        }));
        // Load cart from backend after successful login
        dispatch(loadCartFromBackend());
        return response.data;
      } else {
        return rejectWithValue(response.message || 'Login failed');
      }
    } catch (error: any) {
      dispatch(loginFailure(error.message || 'Login failed'));
      return rejectWithValue(error.message || 'Login failed');
    }
  }
);

// Thunk to logout
export const logoutUser = createAsyncThunk(
  'auth/logoutUser',
  async (_, { dispatch }) => {
    try {
      // No logout API call needed - just clear local state
      // await authApi.logout();
    } catch (error) {
      // Ignore logout errors
      console.warn('Logout failed:', error);
    } finally {
      dispatch(logout());
    }
  }
);

// Thunk to get user profile
export const getUserProfile = createAsyncThunk(
  'auth/getUserProfile',
  async (_, { rejectWithValue }) => {
    try {
      const response = await authApi.getProfile();
      
      if (response.success && response.data) {
        return response.data;
      } else {
        return rejectWithValue(response.message || 'Failed to get profile');
      }
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to get profile');
    }
  }
);

// Thunk to update user profile
export const updateUserProfile = createAsyncThunk(
  'auth/updateUserProfile',
  async (profileData: Partial<User>, { dispatch, rejectWithValue }) => {
    try {
      const response = await authApi.updateProfile(profileData);
      
      if (response.success && response.data) {
        // Update user in Redux store
        dispatch(updateUser(response.data));
        return response.data;
      } else {
        return rejectWithValue(response.message || 'Failed to update profile');
      }
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to update profile');
    }
  }
);

// Thunk to change password
export const changeUserPassword = createAsyncThunk(
  'auth/changeUserPassword',
  async (passwordData: { oldPassword: string; newPassword: string }, { rejectWithValue }) => {
    try {
      const response = await authApi.changePassword(passwordData);
      
      if (response.success) {
        return response.data;
      } else {
        return rejectWithValue(response.message || 'Failed to change password');
      }
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to change password');
    }
  }
);
