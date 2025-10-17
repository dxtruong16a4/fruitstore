import httpClient from './axiosClient';
import type { ApiResponse } from '../types';

// Category interface
export interface Category {
  categoryId: number; // API returns categoryId
  id?: number; // Keep for compatibility
  name: string;
  description?: string;
  imageUrl?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  productCount?: number;
}

// Category API endpoints
export const categoryApi = {
  // Get all categories
  getAllCategories: (): Promise<ApiResponse<Category[]>> => {
    return httpClient.get('/api/categories');
  },

  // Get active categories only
  getActiveCategories: (): Promise<ApiResponse<Category[]>> => {
    return httpClient.get('/api/categories/active');
  },

  // Get category by ID
  getCategoryById: (id: number): Promise<ApiResponse<Category>> => {
    return httpClient.get(`/api/categories/${id}`);
  },

  // Get active category by ID
  getActiveCategoryById: (id: number): Promise<ApiResponse<Category>> => {
    return httpClient.get(`/api/categories/${id}/active`);
  },

  // Search categories by name
  searchCategories: (name: string): Promise<ApiResponse<Category[]>> => {
    return httpClient.get('/api/categories/search', {
      params: { name }
    });
  },

  // Search active categories by name
  searchActiveCategories: (name: string): Promise<ApiResponse<Category[]>> => {
    return httpClient.get('/api/categories/search/active', {
      params: { name }
    });
  },

  // Get categories with product counts
  getCategoriesWithCounts: (): Promise<ApiResponse<Category[]>> => {
    return httpClient.get('/api/categories/with-counts');
  },

  // Admin endpoints
  // Create category (Admin only)
  createCategory: (categoryData: Omit<Category, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Category>> => {
    return httpClient.post('/api/categories', categoryData);
  },

  // Update category (Admin only)
  updateCategory: (id: number, categoryData: Partial<Category>): Promise<ApiResponse<Category>> => {
    return httpClient.put(`/api/categories/${id}`, categoryData);
  },

  // Delete category (Admin only - soft delete)
  deleteCategory: (id: number): Promise<ApiResponse<void>> => {
    return httpClient.delete(`/api/categories/${id}`);
  },

  // Permanently delete category (Admin only)
  permanentlyDeleteCategory: (id: number): Promise<ApiResponse<void>> => {
    return httpClient.delete(`/api/categories/${id}/permanent`);
  },

  // Activate category (Admin only)
  activateCategory: (id: number): Promise<ApiResponse<Category>> => {
    return httpClient.put(`/api/categories/${id}/activate`);
  },

  // Deactivate category (Admin only)
  deactivateCategory: (id: number): Promise<ApiResponse<Category>> => {
    return httpClient.put(`/api/categories/${id}/deactivate`);
  },
};
