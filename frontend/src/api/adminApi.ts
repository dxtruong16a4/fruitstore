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

      const orderStats = (orders.data as any)?.data || {};
      const lowStockProductsData = (lowStockProducts.data as any)?.data || [];
      const allProductsData = (allProducts.data as any)?.data || [];
      const categoriesData = (categories.data as any)?.data || [];
      const discountsData = (discounts.data as any)?.data || [];

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
      return {
        success: true,
        data: (response.data as any)?.data
      };
    } catch (error: any) {
      console.error('Error fetching order statistics:', error);
      return {
        success: false,
        message: error.response?.data?.message || 'Failed to fetch order statistics'
      };
    }
  },

  // Get recent orders
  getRecentOrders: async (days: number = 7, limit: number = 10): Promise<{ success: boolean; data?: RecentOrder[]; message?: string }> => {
    try {
      const response = await httpClient.get('/api/admin/orders/recent', {
        params: { days, page: 0, size: limit }
      });
      return {
        success: true,
        data: (response.data as any)?.data?.data || []
      };
    } catch (error: any) {
      console.error('Error fetching recent orders:', error);
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Failed to fetch recent orders'
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
  }
};

