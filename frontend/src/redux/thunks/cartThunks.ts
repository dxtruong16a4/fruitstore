import { createAsyncThunk } from '@reduxjs/toolkit';
import { cartApi, type Cart, type AddToCartRequest } from '../../api/cartApi';
import { loadCartItems, setCartLoading, setCartError } from '../slices/cartSlice';

// Load cart from backend
export const loadCartFromBackend = createAsyncThunk(
  'cart/loadFromBackend',
  async (_, { dispatch, rejectWithValue }) => {
    try {
      dispatch(setCartLoading(true));
      const response = await cartApi.getCart();
      
      if (response.success) {
        const cart = response.data;
        dispatch(loadCartItems(cart.items));
        return cart;
      } else {
        throw new Error(response.message || 'Failed to load cart');
      }
    } catch (error: any) {
      dispatch(setCartError(error.message || 'Failed to load cart'));
      return rejectWithValue(error.message || 'Failed to load cart');
    } finally {
      dispatch(setCartLoading(false));
    }
  }
);

// Add item to backend cart
export const addItemToBackendCart = createAsyncThunk(
  'cart/addToBackend',
  async (itemData: AddToCartRequest, { dispatch, rejectWithValue }) => {
    try {
      dispatch(setCartLoading(true));
      const response = await cartApi.addItemToCart(itemData);
      
      if (response.success) {
        const cart = response.data;
        dispatch(loadCartItems(cart.items));
        return cart;
      } else {
        throw new Error(response.message || 'Failed to add item to cart');
      }
    } catch (error: any) {
      dispatch(setCartError(error.message || 'Failed to add item to cart'));
      return rejectWithValue(error.message || 'Failed to add item to cart');
    } finally {
      dispatch(setCartLoading(false));
    }
  }
);

// Sync local cart with backend
export const syncCartWithBackend = createAsyncThunk(
  'cart/syncWithBackend',
  async (localItems: any[], { dispatch, rejectWithValue }) => {
    try {
      dispatch(setCartLoading(true));
      
      // First, clear the backend cart
      await cartApi.clearCart();
      
      // Then add all local items to backend cart
      for (const item of localItems) {
        if (item.productId === 0) {
          throw new Error(`Invalid productId 0 for product: ${item.productName}`);
        }
        await cartApi.addItemToCart({
          productId: item.productId,
          quantity: item.quantity
        });
      }
      
      // Finally, load the updated cart from backend
      const response = await cartApi.getCart();
      
      if (response.success) {
        const cart = response.data;
        dispatch(loadCartItems(cart.items));
        return cart;
      } else {
        throw new Error(response.message || 'Failed to sync cart');
      }
    } catch (error: any) {
      dispatch(setCartError(error.message || 'Failed to sync cart'));
      return rejectWithValue(error.message || 'Failed to sync cart');
    } finally {
      dispatch(setCartLoading(false));
    }
  }
);
