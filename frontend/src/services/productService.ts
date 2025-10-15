import api, { API_ENDPOINTS } from './api';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  imageUrl?: string;
  categoryId: number;
  category: {
    id: number;
    name: string;
  };
  createdAt: string;
  updatedAt: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductFilters {
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  search?: string;
  page?: number;
  size?: number;
  sortBy?: 'name' | 'price' | 'createdAt';
  sortDirection?: 'asc' | 'desc';
}

export interface ProductResponse {
  content: Product[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

class ProductService {
  async getProducts(filters?: ProductFilters): Promise<ProductResponse> {
    try {
      const params = new URLSearchParams();
      
      if (filters?.categoryId) params.append('categoryId', filters.categoryId.toString());
      if (filters?.minPrice) params.append('minPrice', filters.minPrice.toString());
      if (filters?.maxPrice) params.append('maxPrice', filters.maxPrice.toString());
      if (filters?.search) params.append('search', filters.search);
      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.size) params.append('size', filters.size.toString());
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortDirection) params.append('sortDirection', filters.sortDirection);
      
      const response = await api.get(`${API_ENDPOINTS.PRODUCTS}?${params}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getProductById(id: number): Promise<Product> {
    try {
      const response = await api.get(API_ENDPOINTS.PRODUCT_BY_ID(id.toString()));
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async getCategories(): Promise<Category[]> {
    try {
      const response = await api.get(API_ENDPOINTS.CATEGORIES);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async createProduct(productData: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>): Promise<Product> {
    try {
      const response = await api.post(`${API_ENDPOINTS.ADMIN_PRODUCTS}`, productData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateProduct(id: number, productData: Partial<Product>): Promise<Product> {
    try {
      const response = await api.put(`${API_ENDPOINTS.ADMIN_PRODUCTS}/${id}`, productData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async deleteProduct(id: number): Promise<void> {
    try {
      await api.delete(`${API_ENDPOINTS.ADMIN_PRODUCTS}/${id}`);
    } catch (error) {
      throw error;
    }
  }

  async createCategory(categoryData: Omit<Category, 'id' | 'createdAt' | 'updatedAt'>): Promise<Category> {
    try {
      const response = await api.post(`${API_ENDPOINTS.ADMIN_CATEGORIES}`, categoryData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateCategory(id: number, categoryData: Partial<Category>): Promise<Category> {
    try {
      const response = await api.put(`${API_ENDPOINTS.ADMIN_CATEGORIES}/${id}`, categoryData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async deleteCategory(id: number): Promise<void> {
    try {
      await api.delete(`${API_ENDPOINTS.ADMIN_CATEGORIES}/${id}`);
    } catch (error) {
      throw error;
    }
  }
}

export const productService = new ProductService();
export default productService;
