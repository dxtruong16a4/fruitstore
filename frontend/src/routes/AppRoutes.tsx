import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAppSelector } from '../redux';

// Layout imports
import { UserLayout, AdminLayout } from '../layouts';

// Page imports
import HomePage from '../pages/HomePage';
import ProductsPage from '../pages/ProductsPage';
import CartPage from '../pages/CartPage';
import OrdersPage from '../pages/OrdersPage';
import LoginPage from '../pages/LoginPage';
import AdminPage from '../pages/AdminPage';
import NotFoundPage from '../pages/NotFoundPage';

// Component imports
import ProtectedRoute from './ProtectedRoute';

const AppRoutes: React.FC = () => {
  const { isAuthenticated, user } = useAppSelector((state) => state.auth);

  return (
    <Routes>
      {/* Public Routes with UserLayout */}
      <Route path="/" element={
        <UserLayout>
          <HomePage />
        </UserLayout>
      } />
      <Route path="/products" element={
        <UserLayout>
          <ProductsPage />
        </UserLayout>
      } />
      <Route path="/login" element={
        isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />
      } />
      
      {/* Protected Routes with UserLayout */}
      <Route 
        path="/cart" 
        element={
          <UserLayout>
            <ProtectedRoute>
              <CartPage />
            </ProtectedRoute>
          </UserLayout>
        } 
      />
      <Route 
        path="/orders" 
        element={
          <UserLayout>
            <ProtectedRoute>
              <OrdersPage />
            </ProtectedRoute>
          </UserLayout>
        } 
      />
      
      {/* Admin Routes with AdminLayout */}
      <Route 
        path="/admin" 
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <AdminPage />
            </ProtectedRoute>
          </AdminLayout>
        } 
      />
      <Route 
        path="/admin/*" 
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <AdminPage />
            </ProtectedRoute>
          </AdminLayout>
        } 
      />
      
      {/* 404 Route */}
      <Route path="*" element={
        <UserLayout>
          <NotFoundPage />
        </UserLayout>
      } />
    </Routes>
  );
};

export default AppRoutes;
