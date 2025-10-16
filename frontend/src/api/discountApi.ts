import httpClient from './axiosClient';
import type { ApiResponse, PaginationParams } from '../types';

// Discount interfaces
export interface Discount {
  id: number;
  code: string;
  description: string;
  discountType: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  usedCount: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface DiscountValidationResponse {
  isValid: boolean;
  message: string;
  discountAmount?: number;
  discount?: Discount;
}

export interface DiscountUsage {
  id: number;
  discountId: number;
  userId: number;
  orderId?: number;
  discountAmount: number;
  usedAt: string;
  discount: Discount;
  user: {
    id: number;
    username: string;
    email: string;
  };
}

export interface DiscountUsageStats {
  totalUsages: number;
  totalDiscountAmount: number;
  uniqueUsers: number;
  averageDiscountAmount: number;
}

export interface CreateDiscountRequest {
  code: string;
  description: string;
  discountType: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  startDate: string;
  endDate: string;
}

export interface UpdateDiscountRequest {
  code?: string;
  description?: string;
  discountType?: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue?: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  startDate?: string;
  endDate?: string;
  isActive?: boolean;
}

export interface ValidateDiscountRequest {
  code: string;
  orderAmount: number;
}

// Discount API endpoints
export const discountApi = {
  // Public endpoints
  // Get active discounts
  getActiveDiscounts: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/discounts/active', { params });
  },

  // Validate discount code
  validateDiscount: (request: ValidateDiscountRequest): Promise<ApiResponse<DiscountValidationResponse>> => {
    return httpClient.post('/api/discounts/validate', request);
  },

  // Get discount by code
  getDiscountByCode: (code: string): Promise<ApiResponse<Discount>> => {
    return httpClient.get(`/api/discounts/code/${code}`);
  },

  // Get available discounts for user
  getAvailableDiscountsForUser: (userId: number, orderAmount: number): Promise<ApiResponse<Discount[]>> => {
    return httpClient.get('/api/discounts/available', {
      params: { userId, orderAmount }
    });
  },

  // Apply discount to order amount
  applyDiscount: (code: string, orderAmount: number): Promise<ApiResponse<number>> => {
    return httpClient.post('/api/discounts/apply', null, {
      params: { code, orderAmount }
    });
  },

  // Admin endpoints
  // Get all discounts (Admin)
  getAllDiscounts: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/discounts', { params });
  },

  // Get discount by ID (Admin)
  getDiscountById: (id: number): Promise<ApiResponse<Discount>> => {
    return httpClient.get(`/api/discounts/${id}`);
  },

  // Create discount (Admin)
  createDiscount: (discountData: CreateDiscountRequest): Promise<ApiResponse<Discount>> => {
    return httpClient.post('/api/discounts', discountData);
  },

  // Update discount (Admin)
  updateDiscount: (id: number, discountData: UpdateDiscountRequest): Promise<ApiResponse<Discount>> => {
    return httpClient.put(`/api/discounts/${id}`, discountData);
  },

  // Delete discount (Admin)
  deleteDiscount: (id: number): Promise<ApiResponse<string>> => {
    return httpClient.delete(`/api/discounts/${id}`);
  },

  // Get discount usage statistics (Admin)
  getDiscountUsageStats: (id: number): Promise<ApiResponse<DiscountUsageStats>> => {
    return httpClient.get(`/api/discounts/${id}/stats`);
  },

  // Get discount usages with pagination (Admin)
  getDiscountUsages: (id: number, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get(`/api/discounts/${id}/usages`, { params });
  },

  // Get user's discount usages (Admin)
  getUserDiscountUsages: (userId: number, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get(`/api/discounts/user/${userId}/usages`, { params });
  },

  // Check if user has used a discount (Admin)
  hasUserUsedDiscount: (userId: number, discountId: number): Promise<ApiResponse<boolean>> => {
    return httpClient.get(`/api/discounts/user/${userId}/discount/${discountId}/used`);
  },

  // Record discount usage (Admin)
  recordDiscountUsage: (data: {
    discountId: number;
    userId: number;
    orderId?: number;
    discountAmount: number;
  }): Promise<ApiResponse<string>> => {
    return httpClient.post('/api/discounts/usage', null, {
      params: data
    });
  },
};
