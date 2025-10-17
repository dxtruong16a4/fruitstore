import { createAsyncThunk } from '@reduxjs/toolkit';
import { authApi } from '../../api/authApi';
import { loginSuccess, loginFailure, logout } from '../slices/authSlice';
import { loadCartFromBackend } from './cartThunks';

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
  async (credentials: { username: string; password: string }, { dispatch, rejectWithValue }) => {
    try {
      const response = await authApi.login(credentials);
      
      if (response.success && response.data) {
        dispatch(loginSuccess(response.data));
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
      await authApi.logout();
    } catch (error) {
      // Ignore logout errors
      console.warn('Logout API call failed:', error);
    } finally {
      dispatch(logout());
    }
  }
);
