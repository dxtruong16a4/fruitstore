import { create } from 'zustand';
import orderService, { type Order, type CreateOrderRequest, type OrderFilters } from '../services/orderService';

interface OrderState {
  orders: Order[];
  currentOrder: Order | null;
  filters: OrderFilters;
  pagination: {
    totalElements: number;
    totalPages: number;
    currentPage: number;
    size: number;
  };
  isLoading: boolean;
  error: string | null;
  
  // Actions
  fetchOrders: (filters?: OrderFilters) => Promise<void>;
  fetchOrderById: (id: number) => Promise<void>;
  createOrder: (orderData: CreateOrderRequest) => Promise<Order>;
  cancelOrder: (id: number) => Promise<void>;
  updateOrderStatus: (id: number, status: string) => Promise<void>;
  setFilters: (filters: Partial<OrderFilters>) => void;
  clearFilters: () => void;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  clearError: () => void;
}

const defaultFilters: OrderFilters = {
  page: 0,
  size: 10,
  sortBy: 'createdAt',
  sortDirection: 'desc',
};

export const useOrderStore = create<OrderState>((set, get) => ({
  orders: [],
  currentOrder: null,
  filters: defaultFilters,
  pagination: {
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
    size: 10,
  },
  isLoading: false,
  error: null,

  fetchOrders: async (filters?: OrderFilters) => {
    set({ isLoading: true, error: null });
    try {
      const currentFilters = { ...get().filters, ...filters };
      const response = await orderService.getOrders(currentFilters);
      set({ 
        orders: response.content,
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
        error: error.response?.data?.message || 'Không thể tải danh sách đơn hàng',
        isLoading: false 
      });
    }
  },

  fetchOrderById: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      const order = await orderService.getOrderById(id);
      set({ currentOrder: order, isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tải thông tin đơn hàng',
        isLoading: false 
      });
    }
  },

  createOrder: async (orderData: CreateOrderRequest) => {
    set({ isLoading: true, error: null });
    try {
      const order = await orderService.createOrder(orderData);
      set({ isLoading: false });
      return order;
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tạo đơn hàng',
        isLoading: false 
      });
      throw error;
    }
  },

  cancelOrder: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await orderService.cancelOrder(id);
      // Refresh orders after cancellation
      get().fetchOrders();
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể hủy đơn hàng',
        isLoading: false 
      });
    }
  },

  updateOrderStatus: async (id: number, status: string) => {
    set({ isLoading: true, error: null });
    try {
      await orderService.updateOrderStatusAdmin(id, status);
      // Refresh orders after status update
      get().fetchOrders();
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể cập nhật trạng thái đơn hàng',
        isLoading: false 
      });
      throw error;
    }
  },

  setFilters: (newFilters: Partial<OrderFilters>) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, ...newFilters, page: 0 };
    set({ filters: updatedFilters });
    get().fetchOrders(updatedFilters);
  },

  clearFilters: () => {
    set({ filters: defaultFilters });
    get().fetchOrders(defaultFilters);
  },

  setPage: (page: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, page };
    set({ filters: updatedFilters });
    get().fetchOrders(updatedFilters);
  },

  setPageSize: (size: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, size, page: 0 };
    set({ filters: updatedFilters });
    get().fetchOrders(updatedFilters);
  },

  clearError: () => {
    set({ error: null });
  },
}));
