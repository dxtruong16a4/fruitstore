import api, { API_ENDPOINTS } from './api';

export interface CartItem {
  id: number;
  productId: number;
  product: {
    id: number;
    name: string;
    price: number;
    imageUrl?: string;
  };
  quantity: number;
  subtotal: number;
}

export interface Cart {
  id: number;
  userId: number;
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
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

class CartService {
  async getCart(): Promise<Cart> {
    try {
      const response = await api.get(API_ENDPOINTS.CART);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async addToCart(item: AddToCartRequest): Promise<Cart> {
    try {
      const response = await api.post(API_ENDPOINTS.CART_ITEMS, item);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async updateCartItem(itemId: number, item: UpdateCartItemRequest): Promise<Cart> {
    try {
      const response = await api.put(API_ENDPOINTS.CART_ITEM(itemId.toString()), item);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async removeFromCart(itemId: number): Promise<Cart> {
    try {
      const response = await api.delete(API_ENDPOINTS.CART_ITEM(itemId.toString()));
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async clearCart(): Promise<void> {
    try {
      await api.delete(API_ENDPOINTS.CART);
    } catch (error) {
      throw error;
    }
  }

  async getCartItemCount(): Promise<number> {
    try {
      const cart = await this.getCart();
      return cart.totalItems;
    } catch (error) {
      return 0;
    }
  }
}

export const cartService = new CartService();
export default cartService;
