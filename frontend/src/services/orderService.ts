import api, { API_ENDPOINTS } from './api';

export interface OrderItem {
  id: number;
  productId: number;
  product: {
    id: number;
    name: string;
    price: number;
    imageUrl?: string;
  };
  quantity: number;
  price: number;
  subtotal: number;
}

export interface Order {
  id: number;
  userId: number;
  items: OrderItem[];
  totalAmount: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  shippingAddress: {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
  };
  paymentMethod: string;
  discountCode?: string;
  discountAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateOrderRequest {
  items: {
    productId: number;
    quantity: number;
  }[];
  shippingAddress: {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
  };
  paymentMethod: string;
  discountCode?: string;
}

export interface OrderFilters {
  search?: string;
  status?: string;
  page?: number;
  size?: number;
  sortBy?: 'createdAt' | 'totalAmount' | 'status';
  sortDirection?: 'asc' | 'desc';
}

export interface OrderResponse {
  content: Order[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

class OrderService {
  async createOrder(orderData: CreateOrderRequest): Promise<Order> {
    try {
      const response = await api.post(API_ENDPOINTS.ORDERS, orderData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getOrders(filters?: OrderFilters): Promise<OrderResponse> {
    try {
      const params = new URLSearchParams();
      
      if (filters?.search) params.append('search', filters.search);
      if (filters?.status) params.append('status', filters.status);
      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.size) params.append('size', filters.size.toString());
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortDirection) params.append('sortDirection', filters.sortDirection);
      
      const response = await api.get(`${API_ENDPOINTS.ORDERS}?${params}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getOrderById(id: number): Promise<Order> {
    try {
      const response = await api.get(API_ENDPOINTS.ORDER_BY_ID(id.toString()));
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async cancelOrder(id: number): Promise<Order> {
    try {
      const response = await api.put(`${API_ENDPOINTS.ORDERS}/${id}/cancel`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateOrderStatus(id: number, status: string): Promise<Order> {
    try {
      const response = await api.put(`${API_ENDPOINTS.ORDERS}/${id}/status`, { status });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Admin methods
  async getAllOrders(filters?: OrderFilters): Promise<OrderResponse> {
    try {
      const params = new URLSearchParams();
      
      if (filters?.search) params.append('search', filters.search);
      if (filters?.status) params.append('status', filters.status);
      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.size) params.append('size', filters.size.toString());
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortDirection) params.append('sortDirection', filters.sortDirection);
      
      const response = await api.get(`${API_ENDPOINTS.ADMIN_ORDERS}?${params}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getOrderByIdAdmin(id: number): Promise<Order> {
    try {
      const response = await api.get(`${API_ENDPOINTS.ADMIN_ORDERS}/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateOrderStatusAdmin(id: number, status: string): Promise<Order> {
    try {
      const response = await api.put(`${API_ENDPOINTS.ADMIN_ORDERS}/${id}/status`, { status });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getOrderStats(): Promise<{
    totalOrders: number;
    pendingOrders: number;
    confirmedOrders: number;
    shippedOrders: number;
    deliveredOrders: number;
    cancelledOrders: number;
    totalRevenue: number;
  }> {
    try {
      const response = await api.get(`${API_ENDPOINTS.ADMIN_ORDERS}/stats`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export const orderService = new OrderService();
export default orderService;
