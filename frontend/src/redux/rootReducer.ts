import { combineReducers } from '@reduxjs/toolkit';
import { apiSlice } from './api/apiSlice';
import authSlice from './slices/authSlice';
import cartSlice from './slices/cartSlice';
import uiSlice from './slices/uiSlice';

// Combine all reducers
const rootReducer = combineReducers({
  // API slice for RTK Query
  [apiSlice.reducerPath]: apiSlice.reducer,
  
  // Feature slices
  auth: authSlice,
  cart: cartSlice,
  ui: uiSlice,
});

export default rootReducer;
