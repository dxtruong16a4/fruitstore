import { create } from 'zustand';
import productService, { type Product, type Category, type ProductFilters } from '../services/productService';

interface ProductState {
  products: Product[];
  categories: Category[];
  currentProduct: Product | null;
  filters: ProductFilters;
  pagination: {
    totalElements: number;
    totalPages: number;
    currentPage: number;
    size: number;
  };
  isLoading: boolean;
  error: string | null;
  
  // Actions
  fetchProducts: (filters?: ProductFilters) => Promise<void>;
  fetchProductById: (id: number) => Promise<void>;
  fetchCategories: () => Promise<void>;
  createProduct: (productData: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>) => Promise<void>;
  updateProduct: (id: number, productData: Partial<Product>) => Promise<void>;
  deleteProduct: (id: number) => Promise<void>;
  setFilters: (filters: Partial<ProductFilters>) => void;
  clearFilters: () => void;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  clearError: () => void;
}

const defaultFilters: ProductFilters = {
  page: 0,
  size: 12,
  sortBy: 'createdAt',
  sortDirection: 'desc',
};

export const useProductStore = create<ProductState>((set, get) => ({
  products: [],
  categories: [],
  currentProduct: null,
  filters: defaultFilters,
  pagination: {
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
    size: 12,
  },
  isLoading: false,
  error: null,

  fetchProducts: async (filters?: ProductFilters) => {
    set({ isLoading: true, error: null });
    try {
      const currentFilters = { ...get().filters, ...filters };
      const response = await productService.getProducts(currentFilters);
      set({ 
        products: response.content,
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
        error: error.response?.data?.message || 'Không thể tải danh sách sản phẩm',
        isLoading: false 
      });
    }
  },

  fetchProductById: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      const product = await productService.getProductById(id);
      set({ currentProduct: product, isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tải thông tin sản phẩm',
        isLoading: false 
      });
    }
  },

  fetchCategories: async () => {
    set({ isLoading: true, error: null });
    try {
      const categories = await productService.getCategories();
      set({ categories, isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tải danh mục',
        isLoading: false 
      });
    }
  },

  createProduct: async (productData: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>) => {
    set({ isLoading: true, error: null });
    try {
      await productService.createProduct(productData);
      // Refresh products after creation
      get().fetchProducts();
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể tạo sản phẩm',
        isLoading: false 
      });
      throw error;
    }
  },

  updateProduct: async (id: number, productData: Partial<Product>) => {
    set({ isLoading: true, error: null });
    try {
      await productService.updateProduct(id, productData);
      // Refresh products after update
      get().fetchProducts();
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể cập nhật sản phẩm',
        isLoading: false 
      });
      throw error;
    }
  },

  deleteProduct: async (id: number) => {
    set({ isLoading: true, error: null });
    try {
      await productService.deleteProduct(id);
      // Refresh products after deletion
      get().fetchProducts();
      set({ isLoading: false });
    } catch (error: any) {
      set({ 
        error: error.response?.data?.message || 'Không thể xóa sản phẩm',
        isLoading: false 
      });
      throw error;
    }
  },

  setFilters: (newFilters: Partial<ProductFilters>) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, ...newFilters, page: 0 }; // Reset to first page when filters change
    set({ filters: updatedFilters });
    get().fetchProducts(updatedFilters);
  },

  clearFilters: () => {
    set({ filters: defaultFilters });
    get().fetchProducts(defaultFilters);
  },

  setPage: (page: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, page };
    set({ filters: updatedFilters });
    get().fetchProducts(updatedFilters);
  },

  setPageSize: (size: number) => {
    const currentFilters = get().filters;
    const updatedFilters = { ...currentFilters, size, page: 0 };
    set({ filters: updatedFilters });
    get().fetchProducts(updatedFilters);
  },

  clearError: () => {
    set({ error: null });
  },
}));
