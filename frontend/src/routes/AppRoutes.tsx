import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAppSelector } from '../redux';

// Layout imports
import { AdminLayout } from '../layouts';

// Page imports
import HomePage from '../pages/HomePage';
import ProductsPage from '../pages/ProductsPage';
import CartPage from '../pages/CartPage';
import OrdersPage from '../pages/OrdersPage';
import LoginPage from '../pages/LoginPage';
import SignUpPage from '../pages/SignUpPage';
import AdminPage from '../pages/AdminPage';
import NotFoundPage from '../pages/NotFoundPage';
import ProfilePage from '../pages/ProfilePage';
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
      
      <Route 
        path="/cart" 
        element={
            <ProtectedRoute>
              <CartPage />
            </ProtectedRoute>
        } 
      />
      <Route 
        path="/orders" 
        element={
            <ProtectedRoute>
              <OrdersPage />
            </ProtectedRoute>
        } 
      />
      <Route 
        path="/profile" 
        element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
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
          <NotFoundPage />
      } />
    </Routes>
  );
};

export default AppRoutes;
