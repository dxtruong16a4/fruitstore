import httpClient from './axiosClient';
import type { ApiResponse, PaginationParams } from '../types';

// Product interface
export interface Product {
  productId: number; // API returns productId
  id?: number; // Keep for compatibility
  name: string;
  description: string;
  price: number;
  imageUrl?: string;
  stockQuantity: number;
  categoryId: number;
  categoryName?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

// Product filter interface
export interface ProductFilter {
  keyword?: string;
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  isActive?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}

// Product API endpoints
export const productApi = {
  // Get all products with pagination
  getAllProducts: (params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/products', { params });
  },

  // Get product by ID
  getProductById: (id: number): Promise<ApiResponse<Product>> => {
    return httpClient.get(`/api/products/${id}`);
  },

  // Get active product by ID
  getActiveProductById: (id: number): Promise<ApiResponse<Product>> => {
    return httpClient.get(`/api/products/${id}/active`);
  },

  // Get products by category
  getProductsByCategory: (categoryId: number, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get(`/api/products/category/${categoryId}`, { params });
  },

  // Get products by multiple categories
  getProductsByCategories: (categoryIds: number[], params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/products/categories', {
      params: {
        categoryIds: categoryIds.join(','),
        ...params
      }
    });
  },

  // Search products with filters
  searchProducts: (filters: ProductFilter): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/products/search', { params: filters });
  },

  // Search products by name
  searchProductsByName: (name: string, params?: PaginationParams): Promise<ApiResponse<any>> => {
    return httpClient.get('/api/products/search/name', {
      params: { name, ...params }
    });
  },

  // Get products by price range
  getProductsByPriceRange: (minPrice: number, maxPrice: number): Promise<ApiResponse<Product[]>> => {
    return httpClient.get('/api/products/price-range', {
      params: { minPrice, maxPrice }
    });
  },

  // Check stock availability
  checkStockAvailability: (id: number, quantity: number): Promise<ApiResponse<boolean>> => {
    return httpClient.get(`/api/products/${id}/stock`, {
      params: { quantity }
    });
  },

  // Admin endpoints
  // Create product (Admin only)
  createProduct: (productData: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Product>> => {
    return httpClient.post('/api/products', productData);
  },

  // Update product (Admin only)
  updateProduct: (id: number, productData: Partial<Product>): Promise<ApiResponse<Product>> => {
    return httpClient.put(`/api/products/${id}`, productData);
  },

  // Delete product (Admin only - soft delete)
  deleteProduct: (id: number): Promise<ApiResponse<void>> => {
    return httpClient.delete(`/api/products/${id}`);
  },

  // Permanently delete product (Admin only)
  permanentlyDeleteProduct: (id: number): Promise<ApiResponse<void>> => {
    return httpClient.delete(`/api/products/${id}/permanent`);
  },

  // Reduce stock (Admin only)
  reduceStock: (id: number, quantity: number): Promise<ApiResponse<void>> => {
    return httpClient.put(`/api/products/${id}/stock/reduce`, null, {
      params: { quantity }
    });
  },

  // Add stock (Admin only)
  addStock: (id: number, quantity: number): Promise<ApiResponse<void>> => {
    return httpClient.put(`/api/products/${id}/stock/add`, null, {
      params: { quantity }
    });
  },

  // Get low stock products (Admin only)
  getLowStockProducts: (threshold: number = 10): Promise<ApiResponse<Product[]>> => {
    return httpClient.get('/api/products/admin/low-stock', {
      params: { threshold }
    });
  },

  // Get top products by stock (Admin only)
  getTopProductsByStock: (limit: number = 10): Promise<ApiResponse<Product[]>> => {
    return httpClient.get('/api/products/admin/top-stock', {
      params: { limit }
    });
  },
};
