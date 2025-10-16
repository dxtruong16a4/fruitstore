import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/query';
import rootReducer from './rootReducer';
import { apiSlice } from './api/apiSlice';

// Configure Redux store
export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'],
      },
    })
      .concat(apiSlice.middleware)
      .concat(
        // Custom middleware can be added here
        (store: any) => (next: any) => (action: any) => {
          // Logger middleware for development
          if (import.meta.env.DEV) {
            console.group(action.type);
            console.info('dispatching', action);
            const result = next(action);
            console.log('next state', store.getState());
            console.groupEnd();
            return result;
          }
          return next(action);
        }
      ),
  devTools: import.meta.env.DEV,
});

// Setup listeners for RTK Query
setupListeners(store.dispatch);

// Export types for TypeScript
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

// Export store instance
export default store;
