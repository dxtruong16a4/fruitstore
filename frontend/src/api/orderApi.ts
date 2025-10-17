import { axiosClient } from './axiosClient';

// Order Types
export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productPrice: number;
  quantity: number;
  subtotal: number;
}

export interface OrderResponse {
  id: number;
  orderNumber: string;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  totalAmount: number;
  createdAt: string;
  updatedAt: string;
  items: OrderItem[];
  shippingAddress?: string;
  notes?: string;
}

export interface OrderListResponse {
  orders: OrderResponse[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  size: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface OrderStatistics {
  totalOrders: number;
  totalAmount: number;
  averageOrderValue: number;
  pendingOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
}

export interface CreateOrderRequest {
  items: Array<{
    productId: number;
    quantity: number;
  }>;
  shippingAddress?: string;
  notes?: string;
}

// API Functions
export const orderApi = {
  // Get orders for authenticated user with pagination
  getOrders: async (params: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: 'asc' | 'desc';
  } = {}): Promise<{ success: boolean; data: OrderListResponse; message?: string }> => {
    try {
      const response = await axiosClient.get('/api/orders', { params });
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching orders:', error);
      return {
        success: false,
        data: {
          orders: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          size: 10,
          hasNext: false,
          hasPrevious: false
        },
        message: error.response?.data?.message || 'Failed to fetch orders'
      };
    }
  },

  // Get order by ID
  getOrderById: async (id: number): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await axiosClient.get(`/api/orders/${id}`);
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching order:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order'
      };
    }
  },

  // Get order by order number
  getOrderByOrderNumber: async (orderNumber: string): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await axiosClient.get(`/api/orders/number/${orderNumber}`);
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching order by number:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order'
      };
    }
  },

  // Cancel order
  cancelOrder: async (id: number): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await axiosClient.put(`/api/orders/${id}/cancel`);
      return {
        success: true,
        data: response.data.data,
        message: response.data.message
      };
    } catch (error: any) {
      console.error('Error cancelling order:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to cancel order'
      };
    }
  },

  // Get orders by status
  getOrdersByStatus: async (status: string, params: {
    page?: number;
    size?: number;
  } = {}): Promise<{ success: boolean; data: OrderListResponse; message?: string }> => {
    try {
      const response = await axiosClient.get(`/api/orders/status/${status}`, { params });
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching orders by status:', error);
      return {
        success: false,
        data: {
          orders: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          size: 10,
          hasNext: false,
          hasPrevious: false
        },
        message: error.response?.data?.message || 'Failed to fetch orders'
      };
    }
  },

  // Get recent orders
  getRecentOrders: async (params: {
    days?: number;
    page?: number;
    size?: number;
  } = {}): Promise<{ success: boolean; data: OrderListResponse; message?: string }> => {
    try {
      const response = await axiosClient.get('/api/orders/recent', { params });
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching recent orders:', error);
      return {
        success: false,
        data: {
          orders: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          size: 10,
          hasNext: false,
          hasPrevious: false
        },
        message: error.response?.data?.message || 'Failed to fetch recent orders'
      };
    }
  },

  // Get cancellable orders
  getCancellableOrders: async (params: {
    page?: number;
    size?: number;
  } = {}): Promise<{ success: boolean; data: OrderListResponse; message?: string }> => {
    try {
      const response = await axiosClient.get('/api/orders/cancellable', { params });
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching cancellable orders:', error);
      return {
        success: false,
        data: {
          orders: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          size: 10,
          hasNext: false,
          hasPrevious: false
        },
        message: error.response?.data?.message || 'Failed to fetch cancellable orders'
      };
    }
  },

  // Check if order can be cancelled
  canCancelOrder: async (id: number): Promise<{ success: boolean; data?: boolean; message?: string }> => {
    try {
      const response = await axiosClient.get(`/api/orders/${id}/can-cancel`);
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error checking if order can be cancelled:', error);
      return {
        success: false,
        data: false,
        message: error.response?.data?.message || 'Failed to check order cancellation status'
      };
    }
  },

  // Get order statistics
  getOrderStatistics: async (): Promise<{ success: boolean; data?: OrderStatistics; message?: string }> => {
    try {
      const response = await axiosClient.get('/api/orders/statistics');
      return {
        success: true,
        data: response.data.data
      };
    } catch (error: any) {
      console.error('Error fetching order statistics:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order statistics'
      };
    }
  },

  // Create order from cart
  createOrder: async (request: CreateOrderRequest): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await axiosClient.post('/api/orders', request);
      return {
        success: true,
        data: response.data.data,
        message: response.data.message
      };
    } catch (error: any) {
      console.error('Error creating order:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to create order'
      };
    }
  }
};