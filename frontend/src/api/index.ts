// Export all API modules
export { authApi } from './authApi';
export { categoryApi } from './categoryApi';
export { productApi } from './productApi';
export { cartApi } from './cartApi';
export { orderApi } from './orderApi';
export { discountApi } from './discountApi';
export { adminApi } from './adminApi';

// Export HTTP client
export { default as httpClient, axiosClient } from './axiosClient';

// Export types
export type { Category } from './categoryApi';
export type { Product, ProductFilter } from './productApi';
export type { Cart, CartItem, AddToCartRequest, UpdateCartItemRequest, CartStats } from './cartApi';
export type { OrderItem, OrderResponse, OrderStatistics } from './orderApi';
export type {
  Discount,
  DiscountValidationResponse,
  DiscountUsage,
  DiscountUsageStats,
  CreateDiscountRequest,
  UpdateDiscountRequest,
  ValidateDiscountRequest
} from './discountApi';
