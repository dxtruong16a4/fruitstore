import { httpClient } from './axiosClient';

// Order Types
export interface OrderItem {
  orderItemId: number;
  product: {
    productId: number;
    name: string;
    price: number;
    imageUrl?: string;
  };
  quantity: number;
  unitPrice: number;
  subtotal: number;
  createdAt: string;
}

export interface OrderResponse {
  orderId: number;
  orderNumber: string;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  totalAmount: number;
  createdAt: string;
  updatedAt: string;
  totalItems: number;
  customerName?: string;
  customerEmail?: string;
  shippingAddress?: string;
  notes?: string;
}

export interface OrderDetailResponse extends OrderResponse {
  orderItems: OrderItem[];
  shippingAddress: string;
  customerName: string;
  customerEmail: string;
  phoneNumber?: string;
  notes?: string;
  shippedAt?: string;
  deliveredAt?: string;
  cancelledAt?: string;
}

export interface OrderListResponse {
  data: OrderResponse[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
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
  shippingAddress: string;
  customerName: string;
  customerEmail: string;
  phoneNumber?: string;
  notes?: string;
  discountCode?: string;
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
      const response = await httpClient.get<OrderListResponse>('/api/orders', { params });
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching orders:', error);
      return {
        success: false,
        data: {
          data: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          pageSize: 10,
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
      const response = await httpClient.get<OrderResponse>(`/api/orders/${id}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching order:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order'
      };
    }
  },

  // Get order details by ID (with items)
  getOrderDetails: async (id: number): Promise<{ success: boolean; data?: OrderDetailResponse; message?: string }> => {
    try {
      const response = await httpClient.get<OrderDetailResponse>(`/api/orders/${id}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching order details:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order details'
      };
    }
  },

  // Get order by order number
  getOrderByOrderNumber: async (orderNumber: string): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await httpClient.get<OrderResponse>(`/api/orders/number/${orderNumber}`);
      return {
        success: true,
        data: response.data
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
      const response = await httpClient.put<OrderResponse>(`/api/orders/${id}/cancel`);
      return {
        success: true,
        data: response.data,
        message: response.message
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
      const response = await httpClient.get<OrderListResponse>(`/api/orders/status/${status}`, { params });
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching orders by status:', error);
      return {
        success: false,
        data: {
          data: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          pageSize: 10,
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
      const response = await httpClient.get<OrderListResponse>('/api/orders/recent', { params });
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching recent orders:', error);
      return {
        success: false,
        data: {
          data: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          pageSize: 10,
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
      const response = await httpClient.get<OrderListResponse>('/api/orders/cancellable', { params });
      return {
        success: true,
        data: response.data
      };
    } catch (error: any) {
      console.error('Error fetching cancellable orders:', error);
      return {
        success: false,
        data: {
          data: [],
          totalElements: 0,
          totalPages: 0,
          currentPage: 0,
          pageSize: 10,
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
      const response = await httpClient.get<boolean>(`/api/orders/${id}/can-cancel`);
      return {
        success: true,
        data: response.data
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
      const response = await httpClient.get<OrderStatistics>('/api/orders/statistics');
      return {
        success: true,
        data: response.data
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
      const response = await httpClient.post<OrderResponse>('/api/orders', request);
      return {
        success: true,
        data: response.data,
        message: response.message
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