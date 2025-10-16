import type { ApiError } from '../types';

// Error handling utilities
export const handleApiError = (error: ApiError): string => {
  // Handle different error scenarios
  if (error.status === 401) {
    return 'Authentication required. Please log in again.';
  }
  
  if (error.status === 403) {
    return 'Access denied. You do not have permission to perform this action.';
  }
  
  if (error.status === 404) {
    return 'Resource not found.';
  }
  
  if (error.status === 422) {
    return 'Validation error. Please check your input.';
  }
  
  if (error.status >= 500) {
    return 'Server error. Please try again later.';
  }
  
  if (error.status === 0) {
    return 'Network error. Please check your internet connection.';
  }
  
  return error.message || 'An unexpected error occurred.';
};

// Request retry utility
export const retryRequest = async <T>(
  requestFn: () => Promise<T>,
  maxAttempts: number = 3,
  delay: number = 1000
): Promise<T> => {
  let lastError: Error;
  
  for (let attempt = 1; attempt <= maxAttempts; attempt++) {
    try {
      return await requestFn();
    } catch (error) {
      lastError = error as Error;
      
      // Don't retry on client errors (4xx)
      if (error && typeof error === 'object' && 'status' in error) {
        const apiError = error as ApiError;
        if (apiError.status >= 400 && apiError.status < 500) {
          throw error;
        }
      }
      
      // Don't retry on last attempt
      if (attempt === maxAttempts) {
        throw error;
      }
      
      // Wait before retrying
      await new Promise(resolve => setTimeout(resolve, delay * attempt));
    }
  }
  
  throw lastError!;
};

// Debounce utility for search
export const debounce = <T extends (...args: unknown[]) => unknown>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: number;
  
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
};

// Format currency
export const formatCurrency = (amount: number, currency: string = 'VND'): string => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: currency,
  }).format(amount);
};

// Format date
export const formatDate = (date: string | Date, options?: Intl.DateTimeFormatOptions): string => {
  const defaultOptions: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  };
  
  return new Intl.DateTimeFormat('vi-VN', { ...defaultOptions, ...options }).format(
    typeof date === 'string' ? new Date(date) : date
  );
};

// Format date and time
export const formatDateTime = (date: string | Date): string => {
  return formatDate(date, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

// Generate random string
export const generateRandomString = (length: number = 8): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  
  return result;
};

// Validate email
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validate phone number (Vietnamese format)
export const isValidPhone = (phone: string): boolean => {
  const phoneRegex = /^(\+84|84|0)[1-9][0-9]{8,9}$/;
  return phoneRegex.test(phone.replace(/\s/g, ''));
};

// Truncate text
export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

// Calculate pagination info
export const calculatePagination = (
  currentPage: number,
  pageSize: number,
  totalElements: number
) => {
  const totalPages = Math.ceil(totalElements / pageSize);
  const startIndex = currentPage * pageSize;
  const endIndex = Math.min(startIndex + pageSize - 1, totalElements - 1);
  
  return {
    totalPages,
    startIndex,
    endIndex,
    hasNext: currentPage < totalPages - 1,
    hasPrevious: currentPage > 0,
    isFirst: currentPage === 0,
    isLast: currentPage === totalPages - 1,
  };
};
