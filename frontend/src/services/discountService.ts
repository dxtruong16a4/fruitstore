import api, { API_ENDPOINTS } from './api';

export interface Discount {
  id: number;
  code: string;
  description: string;
  type: 'PERCENTAGE' | 'FIXED_AMOUNT';
  value: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  usedCount: number;
  isActive: boolean;
  validFrom: string;
  validUntil: string;
  createdAt: string;
  updatedAt: string;
}

export interface DiscountValidationRequest {
  code: string;
  orderAmount: number;
}

export interface DiscountValidationResponse {
  isValid: boolean;
  discount?: Discount;
  discountAmount: number;
  message?: string;
}

export interface CreateDiscountRequest {
  code: string;
  description: string;
  type: 'PERCENTAGE' | 'FIXED_AMOUNT';
  value: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  validFrom: string;
  validUntil: string;
}

export interface UpdateDiscountRequest {
  code?: string;
  description?: string;
  type?: 'PERCENTAGE' | 'FIXED_AMOUNT';
  value?: number;
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  usageLimit?: number;
  isActive?: boolean;
  validFrom?: string;
  validUntil?: string;
}

export interface DiscountFilters {
  isActive?: boolean;
  code?: string;
  page?: number;
  size?: number;
  sortBy?: 'code' | 'createdAt' | 'validUntil';
  sortDirection?: 'asc' | 'desc';
}

export interface DiscountResponse {
  content: Discount[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

class DiscountService {
  async validateDiscount(request: DiscountValidationRequest): Promise<DiscountValidationResponse> {
    try {
      const response = await api.post(API_ENDPOINTS.DISCOUNT_VALIDATE, request);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Admin methods
  async getDiscounts(filters?: DiscountFilters): Promise<DiscountResponse> {
    try {
      const params = new URLSearchParams();
      
      if (filters?.isActive !== undefined) params.append('isActive', filters.isActive.toString());
      if (filters?.code) params.append('code', filters.code);
      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.size) params.append('size', filters.size.toString());
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortDirection) params.append('sortDirection', filters.sortDirection);
      
      const response = await api.get(`/admin/discounts?${params}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getDiscountById(id: number): Promise<Discount> {
    try {
      const response = await api.get(`/admin/discounts/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async createDiscount(discountData: CreateDiscountRequest): Promise<Discount> {
    try {
      const response = await api.post('/admin/discounts', discountData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateDiscount(id: number, discountData: UpdateDiscountRequest): Promise<Discount> {
    try {
      const response = await api.put(`/admin/discounts/${id}`, discountData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async deleteDiscount(id: number): Promise<void> {
    try {
      await api.delete(`/admin/discounts/${id}`);
    } catch (error) {
      throw error;
    }
  }

  async toggleDiscountStatus(id: number): Promise<Discount> {
    try {
      const response = await api.put(`/admin/discounts/${id}/toggle-status`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getDiscountStats(): Promise<{
    totalDiscounts: number;
    activeDiscounts: number;
    expiredDiscounts: number;
    totalUsage: number;
    totalSavings: number;
  }> {
    try {
      const response = await api.get('/admin/discounts/stats');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getDiscountUsageHistory(discountId: number): Promise<{
    discount: Discount;
    usageHistory: {
      id: number;
      orderId: number;
      userId: number;
      discountAmount: number;
      orderAmount: number;
      usedAt: string;
    }[];
  }> {
    try {
      const response = await api.get(`/admin/discounts/${discountId}/usage-history`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export const discountService = new DiscountService();
export default discountService;
