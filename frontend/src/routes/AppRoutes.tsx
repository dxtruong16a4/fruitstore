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
import SignUpPage from '../pages/SignUpPage';
import AdminPage from '../pages/AdminPage';
import NotFoundPage from '../pages/NotFoundPage';
import AboutPage from '../pages/AboutPage';
import ContactPage from '../pages/ContactPage';
import HelpCenterPage from '../pages/HelpCenterPage';
import TrackOrderPage from '../pages/TrackOrderPage';
import ReturnsPage from '../pages/ReturnsPage';
import ShippingInfoPage from '../pages/ShippingInfoPage';

// Component imports
import ProtectedRoute from './ProtectedRoute';

const AppRoutes: React.FC = () => {
  const { isAuthenticated } = useAppSelector((state) => state.auth);

  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<HomePage />} />
      <Route path="/products" element={<ProductsPage />} />
      <Route path="/login" element={
        isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />
      } />
      <Route path="/register" element={
        isAuthenticated ? <Navigate to="/" replace /> : <SignUpPage />
      } />
      
      {/* Additional Public Pages */}
      <Route path="/about" element={<AboutPage />} />
      <Route path="/contact" element={<ContactPage />} />
      <Route path="/help" element={<HelpCenterPage />} />
      <Route path="/track-order" element={<TrackOrderPage />} />
      <Route path="/returns" element={<ReturnsPage />} />
      <Route path="/shipping-info" element={<ShippingInfoPage />} />
      
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
