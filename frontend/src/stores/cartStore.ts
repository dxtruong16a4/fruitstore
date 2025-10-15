import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import cartService, { Cart, CartItem } from '../services/cartService';

interface CartState {
  cart: Cart | null;
  isLoading: boolean;
  error: string | null;
  
  // Actions
  fetchCart: () => Promise<void>;
  addToCart: (productId: number, quantity: number) => Promise<void>;
  updateCartItem: (itemId: number, quantity: number) => Promise<void>;
  removeFromCart: (itemId: number) => Promise<void>;
  clearCart: () => Promise<void>;
  clearError: () => void;
}

export const useCartStore = create<CartState>()(
  persist(
    (set, get) => ({
      cart: null,
      isLoading: false,
      error: null,

      fetchCart: async () => {
        set({ isLoading: true, error: null });
        try {
          const cart = await cartService.getCart();
          set({ cart, isLoading: false });
        } catch (error: any) {
          set({ 
            error: error.response?.data?.message || 'Không thể tải giỏ hàng',
            isLoading: false 
          });
        }
      },

      addToCart: async (productId: number, quantity: number) => {
        set({ isLoading: true, error: null });
        try {
          const cart = await cartService.addToCart({ productId, quantity });
          set({ cart, isLoading: false });
        } catch (error: any) {
          set({ 
            error: error.response?.data?.message || 'Không thể thêm sản phẩm vào giỏ hàng',
            isLoading: false 
          });
          throw error;
        }
      },

      updateCartItem: async (itemId: number, quantity: number) => {
        set({ isLoading: true, error: null });
        try {
          const cart = await cartService.updateCartItem(itemId, { quantity });
          set({ cart, isLoading: false });
        } catch (error: any) {
          set({ 
            error: error.response?.data?.message || 'Không thể cập nhật giỏ hàng',
            isLoading: false 
          });
        }
      },

      removeFromCart: async (itemId: number) => {
        set({ isLoading: true, error: null });
        try {
          const cart = await cartService.removeFromCart(itemId);
          set({ cart, isLoading: false });
        } catch (error: any) {
          set({ 
            error: error.response?.data?.message || 'Không thể xóa sản phẩm khỏi giỏ hàng',
            isLoading: false 
          });
        }
      },

      clearCart: async () => {
        set({ isLoading: true, error: null });
        try {
          await cartService.clearCart();
          set({ cart: null, isLoading: false });
        } catch (error: any) {
          set({ 
            error: error.response?.data?.message || 'Không thể xóa giỏ hàng',
            isLoading: false 
          });
        }
      },

      clearError: () => {
        set({ error: null });
      },
    }),
    {
      name: 'cart-storage',
      partialize: (state) => ({ cart: state.cart }),
    }
  )
);
