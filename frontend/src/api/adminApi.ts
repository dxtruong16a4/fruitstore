import { httpClient } from './axiosClient';

// Admin Dashboard Statistics Types
export interface DashboardStatistics {
  totalUsers: number;
  adminUsers: number;
  customerUsers: number;
  totalCategories: number;
  activeCategories: number;
  inactiveCategories: number;
  totalProducts: number;
  lowStockProducts: number;
  totalOrders: number;
  pendingOrders: number;
  confirmedOrders: number;
  shippedOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
  totalRevenue: number;
  averageOrderValue: number;
  totalDiscounts: number;
  activeDiscounts: number;
  usedDiscounts: number;
}

export interface OrderStatistics {
  totalOrders: number;
  pendingOrders: number;
  confirmedOrders: number;
  shippedOrders: number;
  deliveredOrders: number;
  cancelledOrders: number;
  totalRevenue: number;
  averageOrderValue: number;
}

export interface RecentOrder {
  orderId: number;
  orderNumber: string;
  customerName: string;
  totalAmount: number;
  status: string;
  createdAt: string;
}

export interface RecentActivity {
  orders: RecentOrder[];
  discountUsages: any[];
}

export interface UserStats {
  totalUsers: number;
  adminUsers: number;
  customerUsers: number;
}

export interface CategoryStats {
  totalCategories: number;
  activeCategories: number;
  inactiveCategories: number;
}

export interface ProductStats {
  totalProducts: number;
  lowStockProducts: number;
}

export interface DiscountStats {
  totalDiscounts: number;
  activeDiscounts: number;
  usedDiscounts: number;
}

export interface UserResponse {
  userId: number;
  username: string;
  email: string;
  fullName: string;
  phone?: string;
  address?: string;
  role: 'ADMIN' | 'CUSTOMER';
  createdAt: string;
}

export interface UserStatistics {
  totalUsers: number;
  adminUsers: number;
  customerUsers: number;
}

export interface CategoryResponse {
  categoryId: number;
  name: string;
  description?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt?: string;
  productCount?: number;
}

export interface CreateCategoryRequest {
  name: string;
  description?: string;
}

export interface UpdateCategoryRequest {
  name?: string;
  description?: string;
}

export interface ProductResponse {
  productId: number;
  name: string;
  description?: string;
  price: number;
  stockQuantity: number;
  imageUrl?: string;
  category?: { categoryId: number; name: string };
  isActive: boolean;
  createdAt: string;
  updatedAt?: string;
}

export interface CreateProductRequest {
  name: string;
  description?: string;
  price: number;
  stockQuantity: number;
  imageUrl?: string;
  categoryId: number;
}

export interface UpdateProductRequest {
  name?: string;
  description?: string;
  price?: number;
  stockQuantity?: number;
  imageUrl?: string;
  categoryId?: number;
}

export interface OrderItemResponse {
  orderItemId: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
  subtotal: number;
}

export interface OrderResponse {
  orderId: number;
  orderNumber: string;
  user: {
    userId: number;
    username: string;
    fullName?: string;
  };
  username?: string;
  totalAmount: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  shippingAddress?: string;
  phoneNumber?: string;
  customerName?: string;
  customerEmail?: string;
  notes?: string;
  orderItems?: OrderItemResponse[];
  createdAt: string;
  updatedAt?: string;
}

export interface DiscountResponse {
  discountId: number;
  code: string;
  description?: string;
  discountType: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  maxUsageCount?: number;
  usageCount: number;
  expiryDate?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt?: string;
}

export interface CreateDiscountRequest {
  code: string;
  description?: string;
  discountType: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue: number;
  maxUsageCount?: number;
  expiryDate?: string;
}

export interface UpdateDiscountRequest {
  code?: string;
  description?: string;
  discountType?: 'PERCENTAGE' | 'FIXED_AMOUNT';
  discountValue?: number;
  maxUsageCount?: number;
  expiryDate?: string;
  isActive?: boolean;
}

// Admin API Functions
export const adminApi = {
  // Get dashboard statistics
  getDashboardStatistics: async (): Promise<{ success: boolean; data?: DashboardStatistics; message?: string }> => {
    try {
      // Fetch all statistics in parallel
      const [orders, lowStockProducts, allProducts, categories, discounts] = await Promise.all([
        httpClient.get('/api/admin/orders/statistics'),
        httpClient.get('/api/products/admin/low-stock?threshold=10'),
        httpClient.get('/api/products'),
        httpClient.get('/api/categories'),
        httpClient.get('/api/discounts?page=0&size=100')
      ]);

      console.log('Dashboard API responses:', { orders, lowStockProducts, allProducts, categories, discounts });

      const orderStats = (orders as any)?.data || {};
      const lowStockProductsData = (lowStockProducts as any)?.data || [];

      // Handle paginated response from /api/products
      const allProductsResponse = (allProducts as any)?.data;
      const allProductsData = Array.isArray(allProductsResponse)
        ? allProductsResponse
        : (allProductsResponse?.content || []);

      const categoriesData = (categories as any)?.data || [];

      // Handle paginated response from /api/discounts
      const discountsResponse = (discounts as any)?.data;
      const discountsData = Array.isArray(discountsResponse)
        ? discountsResponse
        : (discountsResponse?.content || []);

      const stats: DashboardStatistics = {
        totalUsers: 0,
        adminUsers: 0,
        customerUsers: 0,
        totalCategories: categoriesData.length,
        activeCategories: categoriesData.filter((c: any) => c.isActive).length,
        inactiveCategories: categoriesData.filter((c: any) => !c.isActive).length,
        totalProducts: allProductsData.length,
        lowStockProducts: lowStockProductsData.length,
        totalOrders: orderStats.totalOrders || 0,
        pendingOrders: orderStats.pendingOrders || 0,
        confirmedOrders: orderStats.confirmedOrders || 0,
        shippedOrders: orderStats.shippedOrders || 0,
        deliveredOrders: orderStats.deliveredOrders || 0,
        cancelledOrders: orderStats.cancelledOrders || 0,
        totalRevenue: orderStats.totalRevenue || 0,
        averageOrderValue: orderStats.averageOrderValue || 0,
        totalDiscounts: discountsData.length,
        activeDiscounts: discountsData.filter((d: any) => d.isActive).length,
        usedDiscounts: discountsData.filter((d: any) => d.usedCount > 0).length,
      };

      return {
        success: true,
        data: stats
      };
    } catch (error: any) {
      console.error('Error fetching dashboard statistics:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch dashboard statistics'
      };
    }
  },

  // Get order statistics
  getOrderStatistics: async (): Promise<{ success: boolean; data?: OrderStatistics; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/orders/statistics');
      console.log('getOrderStatistics raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching order statistics:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch order statistics'
      };
    }
  },

  // Get recent orders
  getRecentOrders: async (days: number = 7, limit: number = 10): Promise<{ success: boolean; data?: RecentOrder[]; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/orders/recent', {
        params: { days, page: 0, size: limit }
      });
      console.log('getRecentOrders raw response:', response);
      return {
        success: true,
        data: (response as any)?.data?.data || []
      };
    } catch (error: any) {
      console.error('Error fetching recent orders:', error);
      return {
        success: false,
        data: [],
        message: error.message || 'Failed to fetch recent orders'
      };
    }
  },

  // Get all orders with pagination
  getAllOrders: async (page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/orders', {
        params: { page, size, sortBy: 'createdAt', sortDirection: 'desc' }
      });
      return {
        success: true,
        data: (response.data as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching all orders:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch orders'
      };
    }
  },

  // Get orders by status
  getOrdersByStatus: async (status: string, page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get(`/api/admin/orders/status/${status}`, {
        params: { page, size }
      });
      return {
        success: true,
        data: (response.data as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching orders by status:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch orders'
      };
    }
  },

  // Update order status
  updateOrderStatus: async (orderId: number, status: string, notes?: string): Promise<{ success: boolean; message?: string }> => {
    try {
      await httpClient.put(`/api/admin/orders/${orderId}/status`, { status, notes });
      return {
        success: true,
        message: 'Order status updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating order status:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to update order status'
      };
    }
  },

  // Get all users with pagination
  getAllUsers: async (page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/users', {
        params: { page, size, sortBy: 'createdAt', sortDirection: 'desc' }
      });
      console.log('getAllUsers raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching users:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch users'
      };
    }
  },

  // Get user by ID
  getUserById: async (userId: number): Promise<{ success: boolean; data?: UserResponse; message?: string }> => {
    try {
      const response = await httpClient.get(`/api/admin/users/${userId}`);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching user:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch user'
      };
    }
  },

  // Update user role
  updateUserRole: async (userId: number, role: string): Promise<{ success: boolean; data?: UserResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/admin/users/${userId}/role`, null, {
        params: { role }
      });
      return {
        success: true,
        data: (response as any)?.data,
        message: 'User role updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating user role:', error);
      return {
        success: false,
        message: error.message || 'Failed to update user role'
      };
    }
  },

  // Get user statistics
  getUserStatistics: async (): Promise<{ success: boolean; data?: UserStatistics; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/users/statistics');
      console.log('getUserStatistics raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching user statistics:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch user statistics'
      };
    }
  },

  // Get all categories
  getAllCategories: async (): Promise<{ success: boolean; data?: CategoryResponse[]; message?: string }> => {
    try {
      const response = await httpClient.get('/api/categories');
      console.log('getAllCategories raw response:', response);
      return {
        success: true,
        data: (response as any)?.data || []
      };
    } catch (error: any) {
      console.error('Error fetching categories:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch categories'
      };
    }
  },

  // Create category
  createCategory: async (request: CreateCategoryRequest): Promise<{ success: boolean; data?: CategoryResponse; message?: string }> => {
    try {
      const response = await httpClient.post('/api/categories', request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Category created successfully'
      };
    } catch (error: any) {
      console.error('Error creating category:', error);
      return {
        success: false,
        message: error.message || 'Failed to create category'
      };
    }
  },

  // Update category
  updateCategory: async (categoryId: number, request: UpdateCategoryRequest): Promise<{ success: boolean; data?: CategoryResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/categories/${categoryId}`, request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Category updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating category:', error);
      return {
        success: false,
        message: error.message || 'Failed to update category'
      };
    }
  },

  // Activate category
  activateCategory: async (categoryId: number): Promise<{ success: boolean; data?: CategoryResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/categories/${categoryId}/activate`);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Category activated successfully'
      };
    } catch (error: any) {
      console.error('Error activating category:', error);
      return {
        success: false,
        message: error.message || 'Failed to activate category'
      };
    }
  },

  // Deactivate category
  deactivateCategory: async (categoryId: number): Promise<{ success: boolean; data?: CategoryResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/categories/${categoryId}/deactivate`);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Category deactivated successfully'
      };
    } catch (error: any) {
      console.error('Error deactivating category:', error);
      return {
        success: false,
        message: error.message || 'Failed to deactivate category'
      };
    }
  },

  // Delete category
  deleteCategory: async (categoryId: number): Promise<{ success: boolean; message?: string }> => {
    try {
      await httpClient.delete(`/api/categories/${categoryId}`);
      return {
        success: true,
        message: 'Category deleted successfully'
      };
    } catch (error: any) {
      console.error('Error deleting category:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to delete category'
      };
    }
  },

  // Get all products with pagination
  getAllProducts: async (page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get('/api/products', {
        params: { page, size, sortBy: 'createdAt', sortDirection: 'desc' }
      });
      console.log('getAllProducts raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching products:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch products'
      };
    }
  },

  // Create product
  createProduct: async (request: CreateProductRequest): Promise<{ success: boolean; data?: ProductResponse; message?: string }> => {
    try {
      const response = await httpClient.post('/api/products', request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Product created successfully'
      };
    } catch (error: any) {
      console.error('Error creating product:', error);
      return {
        success: false,
        message: error.message || 'Failed to create product'
      };
    }
  },

  // Update product
  updateProduct: async (productId: number, request: UpdateProductRequest): Promise<{ success: boolean; data?: ProductResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/products/${productId}`, request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Product updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating product:', error);
      return {
        success: false,
        message: error.message || 'Failed to update product'
      };
    }
  },

  // Delete product
  deleteProduct: async (productId: number): Promise<{ success: boolean; message?: string }> => {
    try {
      await httpClient.delete(`/api/products/${productId}`);
      return {
        success: true,
        message: 'Product deleted successfully'
      };
    } catch (error: any) {
      console.error('Error deleting product:', error);
      return {
        success: false,
        message: error.message || 'Failed to delete product'
      };
    }
  },

  // Get all orders with pagination
  getAllOrdersAdmin: async (page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/orders', {
        params: { page, size, sortBy: 'createdAt', sortDirection: 'desc' }
      });
      console.log('getAllOrdersAdmin raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching orders:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch orders'
      };
    }
  },

  // Get order by ID
  getOrderById: async (orderId: number): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await httpClient.get(`/api/admin/orders/${orderId}`);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching order:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch order'
      };
    }
  },

  // Update order status
  updateOrderStatusAdmin: async (orderId: number, status: string, notes?: string): Promise<{ success: boolean; data?: OrderResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/admin/orders/${orderId}/status`, { status, notes });
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Order status updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating order status:', error);
      return {
        success: false,
        message: error.message || 'Failed to update order status'
      };
    }
  },

  // Get all discounts with pagination
  getAllDiscounts: async (page: number = 0, size: number = 20): Promise<{ success: boolean; data?: any; message?: string }> => {
    try {
      const response = await httpClient.get('/api/discounts', {
        params: { page, size, sortBy: 'createdAt', sortDirection: 'desc' }
      });
      console.log('getAllDiscounts raw response:', response);
      return {
        success: true,
        data: (response as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching discounts:', error);
      return {
        success: false,
        message: error.message || 'Failed to fetch discounts'
      };
    }
  },

  // Create discount
  createDiscount: async (request: CreateDiscountRequest): Promise<{ success: boolean; data?: DiscountResponse; message?: string }> => {
    try {
      const response = await httpClient.post('/api/discounts', request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Discount created successfully'
      };
    } catch (error: any) {
      console.error('Error creating discount:', error);
      return {
        success: false,
        message: error.message || 'Failed to create discount'
      };
    }
  },

  // Update discount
  updateDiscount: async (discountId: number, request: UpdateDiscountRequest): Promise<{ success: boolean; data?: DiscountResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/discounts/${discountId}`, request);
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Discount updated successfully'
      };
    } catch (error: any) {
      console.error('Error updating discount:', error);
      return {
        success: false,
        message: error.message || 'Failed to update discount'
      };
    }
  },

  // Delete discount
  deleteDiscount: async (discountId: number): Promise<{ success: boolean; message?: string }> => {
    try {
      await httpClient.delete(`/api/discounts/${discountId}`);
      return {
        success: true,
        message: 'Discount deleted successfully'
      };
    } catch (error: any) {
      console.error('Error deleting discount:', error);
      return {
        success: false,
        message: error.message || 'Failed to delete discount'
      };
    }
  },

  // Activate discount
  activateDiscount: async (discountId: number): Promise<{ success: boolean; data?: DiscountResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/discounts/${discountId}`, { isActive: true });
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Discount activated successfully'
      };
    } catch (error: any) {
      console.error('Error activating discount:', error);
      return {
        success: false,
        message: error.message || 'Failed to activate discount'
      };
    }
  },

  // Deactivate discount
  deactivateDiscount: async (discountId: number): Promise<{ success: boolean; data?: DiscountResponse; message?: string }> => {
    try {
      const response = await httpClient.put(`/api/discounts/${discountId}`, { isActive: false });
      return {
        success: true,
        data: (response as any)?.data,
        message: 'Discount deactivated successfully'
      };
    } catch (error: any) {
      console.error('Error deactivating discount:', error);
      return {
        success: false,
        message: error.message || 'Failed to deactivate discount'
      };
    }
  }

};

