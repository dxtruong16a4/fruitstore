import httpClient from './axiosClient';
import type { ApiResponse } from '../types';

// Cart interfaces
export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  productPrice: number;
  productImageUrl?: string;
  quantity: number;
  subtotal: number;
}

export interface Cart {
  cartId: number;
  userId: number;
  items: CartItem[];
  itemCount: number;
  totalItems: number;
  subtotal: number;
  isEmpty: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface AddToCartRequest {
  productId: number;
  quantity: number;
}

export interface UpdateCartItemRequest {
  quantity: number;
}

export interface CartStats {
  itemCount: number;
  totalItems: number;
  subtotal: number;
  totalAmount: number;
  isEmpty: boolean;
}

// Cart API endpoints
export const cartApi = {
  // Get full cart
  getCart: (): Promise<ApiResponse<Cart>> => {
    return httpClient.get('/api/cart');
  },

  // Get cart summary
  getCartSummary: (): Promise<ApiResponse<Cart>> => {
    return httpClient.get('/api/cart/summary');
  },

  // Add item to cart
  addItemToCart: (itemData: AddToCartRequest): Promise<ApiResponse<Cart>> => {
    return httpClient.post('/api/cart/items', itemData);
  },

  // Update cart item quantity
  updateCartItem: (cartItemId: number, itemData: UpdateCartItemRequest): Promise<ApiResponse<Cart>> => {
    return httpClient.put(`/api/cart/items/${cartItemId}`, itemData);
  },

  // Remove item from cart
  removeCartItem: (cartItemId: number): Promise<ApiResponse<Cart>> => {
    return httpClient.delete(`/api/cart/items/${cartItemId}`);
  },

  // Clear all items from cart
  clearCart: (): Promise<ApiResponse<Cart>> => {
    return httpClient.delete('/api/cart/clear');
  },

  // Get cart total amount
  getCartTotal: (): Promise<ApiResponse<number>> => {
    return httpClient.get('/api/cart/total');
  },

  // Check if cart has items
  hasItems: (): Promise<ApiResponse<boolean>> => {
    return httpClient.get('/api/cart/has-items');
  },

  // Get cart item count (number of different products)
  getItemCount: (): Promise<ApiResponse<number>> => {
    return httpClient.get('/api/cart/item-count');
  },

  // Get total quantity of items in cart
  getTotalItems: (): Promise<ApiResponse<number>> => {
    return httpClient.get('/api/cart/total-items');
  },

  // Get cart statistics
  getCartStats: (): Promise<ApiResponse<CartStats>> => {
    return httpClient.get('/api/cart/stats');
  },
};
