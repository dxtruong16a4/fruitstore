import httpClient from './axiosClient';
import type { ApiResponse, PaginationParams } from '../types';

// Order interfaces
export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productPrice: number;
  quantity: number;
  subtotal: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  userId: number;
  customerName: string;
  customerEmail: string;
  customerPhone?: string;
  shippingAddress: string;
  totalAmount: number;
  status: 'PENDING' | 'CONFIRMED' | 'DELIVERED' | 'CANCELLED';
  items: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateOrderRequest {
  customerName: string;
  customerEmail: string;
  customerPhone?: string;
  shippingAddress: string;
  discountCode?: string;
}

export interface UpdateOrderStatusRequest {
  status: 'PENDING' | 'CONFIRMED' | 'DELIVERED' | 'CANCELLED';
  notes?: string;
}

export interface OrderStatistics {
  totalOrders: number;
  totalAmount: number;
  pendingOrders: number;
  confirmedOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
  averageOrderValue: number;
}

// Order API endpoints (Customer)
export const orderApi = {
  // Create order from cart
  createOrder: (orderData: CreateOrderRequest): Promise<ApiResponse<Order>> => {
    return httpClient.post('/api/orders', orderData);
  },

  // Get user orders with pagination
  getOrdersByUser: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/orders', { params });
  },

  // Get order by ID
  getOrderById: (id: number): Promise<ApiResponse<Order>> => {
    return httpClient.get(`/api/orders/${id}`);
  },

  // Get order by order number
  getOrderByOrderNumber: (orderNumber: string): Promise<ApiResponse<Order>> => {
    return httpClient.get(`/api/orders/number/${orderNumber}`);
  },

  // Cancel order
  cancelOrder: (id: number): Promise<ApiResponse<Order>> => {
    return httpClient.put(`/api/orders/${id}/cancel`);
  },

  // Get orders by status
  getOrdersByStatus: (status: string, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get(`/api/orders/status/${status}`, { params });
  },

  // Get recent orders
  getRecentOrders: (days: number = 30, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/orders/recent', {
      params: { days, ...params }
    });
  },

  // Get cancellable orders
  getCancellableOrders: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/orders/cancellable', { params });
  },

  // Check if order can be cancelled
  canCancelOrder: (id: number): Promise<ApiResponse<boolean>> => {
    return httpClient.get(`/api/orders/${id}/can-cancel`);
  },

  // Get user order statistics
  getOrderStatisticsByUser: (): Promise<ApiResponse<OrderStatistics>> => {
    return httpClient.get('/api/orders/statistics');
  },
};

// Admin Order API endpoints
export const adminOrderApi = {
  // Get all orders with pagination (Admin)
  getAllOrders: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/admin/orders', { params });
  },

  // Get order by ID (Admin)
  getOrderById: (id: number): Promise<ApiResponse<Order>> => {
    return httpClient.get(`/api/admin/orders/${id}`);
  },

  // Get order by order number (Admin)
  getOrderByOrderNumber: (orderNumber: string): Promise<ApiResponse<Order>> => {
    return httpClient.get(`/api/admin/orders/number/${orderNumber}`);
  },

  // Update order status (Admin)
  updateOrderStatus: (id: number, statusData: UpdateOrderStatusRequest): Promise<ApiResponse<Order>> => {
    return httpClient.put(`/api/admin/orders/${id}/status`, statusData);
  },

  // Cancel order (Admin)
  cancelOrder: (id: number): Promise<ApiResponse<Order>> => {
    return httpClient.put(`/api/admin/orders/${id}/cancel`);
  },

  // Get orders with filters (Admin)
  getOrdersWithFilters: (filters: {
    userId?: number;
    status?: string;
    minAmount?: number;
    maxAmount?: number;
    customerName?: string;
    customerEmail?: string;
    startDate?: string;
    endDate?: string;
    page?: number;
    size?: number;
  }): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/admin/orders/filter', { params: filters });
  },

  // Get orders by status (Admin)
  getOrdersByStatus: (status: string, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get(`/api/admin/orders/status/${status}`, { params });
  },

  // Get recent orders (Admin)
  getRecentOrders: (days: number = 7, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/admin/orders/recent', {
      params: { days, ...params }
    });
  },

  // Get cancellable orders (Admin)
  getCancellableOrders: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/admin/orders/cancellable', { params });
  },

  // Get order statistics (Admin)
  getOrderStatistics: (): Promise<ApiResponse<OrderStatistics>> => {
    return httpClient.get('/api/admin/orders/statistics');
  },

  // Get order count by status (Admin)
  getOrderCountByStatus: (status: string): Promise<ApiResponse<number>> => {
    return httpClient.get(`/api/admin/orders/count/status/${status}`);
  },

  // Get order count by user (Admin)
  getOrderCountByUser: (userId: number): Promise<ApiResponse<number>> => {
    return httpClient.get(`/api/admin/orders/count/user/${userId}`);
  },

  // Get order count by user and status (Admin)
  getOrderCountByUserAndStatus: (userId: number, status: string): Promise<ApiResponse<number>> => {
    return httpClient.get(`/api/admin/orders/count/user/${userId}/status/${status}`);
  },

  // Check if order can be cancelled (Admin)
  canCancelOrder: (id: number): Promise<ApiResponse<boolean>> => {
    return httpClient.get(`/api/admin/orders/${id}/can-cancel`);
  },
};
