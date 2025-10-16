import { create } from 'zustand';
import discountService from '../services/discountService';
import type { Discount, DiscountFilters, DiscountValidationResponse } from '../services/discountService';

interface DiscountState {
  discounts: Discount[];
  appliedDiscount: DiscountValidationResponse | null;
  filters: DiscountFilters;
  pagination: {
    totalElements: number;
    totalPages: number;
    currentPage: number;
    size: number;
  };
  isLoading: boolean;
  error: string | null;
  
  // Actions
  fetchDiscounts: (filters?: DiscountFilters) => Promise<void>;
  validateDiscount: (code: string, orderAmount: number) => Promise<void>;
  createDiscount: (discountData: any) => Promise<Discount>;
  updateDiscount: (id: number, discountData: any) => Promise<Discount>;
  deleteDiscount: (id: number) => Promise<void>;
  toggleDiscountStatus: (id: number) => Promise<void>;
  setFilters: (filters: Partial<DiscountFilters>) => void;
  clearFilters: () => void;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  clearDiscount: () => void;
  clearError: () => void;
}

const defaultFilters: DiscountFilters = {
  page: 0,
  size: 10,
  sortBy: 'createdAt',
  sortDirection: 'desc',
};

export const useDiscountStore = create<DiscountState>((set, get) => ({
  discounts: [],
  appliedDiscount: null,
  filters: defaultFilters,
  pagination: {
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
    size: 10,
  },
  isLoading: false,
  error: null,

  fetchDiscounts: async (filters?: DiscountFilters) => {
    set({ isLoading: true, error: null });
    try {
      const currentFilters = { ...get().filters, ...filters };
      const response = await discountService.getDiscounts(currentFilters);
      set({ 
        discounts: response.content,
        pagination: {
          totalElements: response.totalElements,
          totalPages: response.totalPages,
          currentPage: response.number,
          size: response.size,
        },
        filters: currentFilters,
        isLoading: false 
      });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tải danh sách mã giảm giá',
        isLoading: false 
      });
    }
  },

  validateDiscount: async (code: string, orderAmount: number) => {
    set({ isLoading: true, error: null });
    try {
      const response = await discountService.validateDiscount({ code, orderAmount });
      set({ 
        appliedDiscount: response,
        isLoading: false 
      });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Mã giảm giá không hợp lệ',
        isLoading: false 
      });
      throw error;
    }
  },

  createDiscount: async (discountData: any) => {
    set({ isLoading: true, error: null });
    try {
      const discount = await discountService.createDiscount(discountData);
      set({ isLoading: false });
      return discount;
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tạo mã giảm giá',
        isLoading: false 
      });
      throw error;
    }
  },

  updateDiscount: async (id: number, discountData: any) => {
    set({ isLoading: true, error: null });
    try {
      const discount = await discountService.updateDiscount(id, discountData);
      set({ isLoading: false });
      return discount;
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể cập nhật mã giảm giá',
        isLoading: false 
      });
      throw error;
    }
  },

  deleteDiscount: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await discountService.deleteDiscount(id);
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể xóa mã giảm giá',
        isLoading: false 
      });
      throw error;
    }
  },

  toggleDiscountStatus: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await discountService.toggleDiscountStatus(id);
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể cập nhật trạng thái',
        isLoading: false 
      });
      throw error;
    }
  },

  setFilters: (newFilters: Partial<DiscountFilters>) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, ...newFilters, page: 0 };
    set({ filters: updatedFilters });
    get().fetchDiscounts(updatedFilters);
  },

  clearFilters: () => {
    set({ filters: defaultFilters });
    get().fetchDiscounts(defaultFilters);
  },

  setPage: (page: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, page };
    set({ filters: updatedFilters });
    get().fetchDiscounts(updatedFilters);
  },

  setPageSize: (size: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, size, page: 0 };
    set({ filters: updatedFilters });
    get().fetchDiscounts(updatedFilters);
  },

  clearDiscount: () => {
    set({ appliedDiscount: null, error: null });
  },

  clearError: () => {
    set({ error: null });
  },
}));