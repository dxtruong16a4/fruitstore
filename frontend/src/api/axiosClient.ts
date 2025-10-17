import axios, { type AxiosInstance, type AxiosRequestConfig } from 'axios';
import type { ApiResponse, ApiError } from '../types';

// Base configuration
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Create axios instance
const axiosClient: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - Add JWT token to requests
axiosClient.interceptors.request.use(
  (config) => {
    // Get token from localStorage
    const token = localStorage.getItem('token');
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - Handle responses and errors
axiosClient.interceptors.response.use(
  (response) => {
    // Return the data directly for successful responses
    return response.data;
  },
  (error) => {
    // Handle different error scenarios
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;
      
      // Handle 401 Unauthorized - Token expired or invalid
      if (status === 401) {
        // Clear token and redirect to login
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        
        // Only redirect if not already on login page
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
      }
      
      // Handle 403 Forbidden - Insufficient permissions
      if (status === 403) {
        console.warn('Access denied: Insufficient permissions');
      }
      
      // Handle 500 Internal Server Error
      if (status >= 500) {
        console.error('Server error:', data);
      }
      
      // Create standardized error object
      const apiError: ApiError = {
        message: data?.message || error.message || 'An error occurred',
        status,
        timestamp: new Date().toISOString(),
        path: error.config?.url || '',
      };
      
      return Promise.reject(apiError);
    } else if (error.request) {
      // Network error - No response received
      const networkError: ApiError = {
        message: 'Network error - Please check your connection',
        status: 0,
        timestamp: new Date().toISOString(),
        path: error.config?.url || '',
      };
      
      return Promise.reject(networkError);
    } else {
      // Request setup error
      const setupError: ApiError = {
        message: error.message || 'Request setup error',
        status: 0,
        timestamp: new Date().toISOString(),
        path: error.config?.url || '',
      };
      
      return Promise.reject(setupError);
    }
  }
);

// Generic HTTP methods
export const httpClient = {
  // GET request
  get: <T>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.get(url, config) as Promise<ApiResponse<T>>;
  },

  // POST request
  post: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.post(url, data, config) as Promise<ApiResponse<T>>;
  },

  // PUT request
  put: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.put(url, data, config) as Promise<ApiResponse<T>>;
  },

  // PATCH request
  patch: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.patch(url, data, config) as Promise<ApiResponse<T>>;
  },

  // DELETE request
  delete: <T>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.delete(url, config) as Promise<ApiResponse<T>>;
  },

  // Upload file
  upload: <T>(url: string, formData: FormData, config?: AxiosRequestConfig): Promise<ApiResponse<T>> => {
    return axiosClient.post(url, formData, {
      ...config,
      headers: {
        ...config?.headers,
        'Content-Type': 'multipart/form-data',
      },
    });
  },

  // Download file
  download: (url: string, filename?: string, config?: AxiosRequestConfig): Promise<void> => {
    return axiosClient.get(url, {
      ...config,
      responseType: 'blob',
    }).then((response) => {
      const blob = new Blob([response.data]);
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = filename || 'download';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(downloadUrl);
    });
  },
};

// Export the axios instance for advanced usage
export { axiosClient };
export default httpClient;
