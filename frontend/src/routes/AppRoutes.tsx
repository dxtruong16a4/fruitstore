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
import DashboardPage from '../pages/admin/DashboardPage';
import UserManagementPage from '../pages/admin/UserManagementPage';
import CategoryManagementPage from '../pages/admin/CategoryManagementPage';
import ProductManagementPage from '../pages/admin/ProductManagementPage';
import OrderManagementPage from '../pages/admin/OrderManagementPage';
import DiscountManagementPage from '../pages/admin/DiscountManagementPage';

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
              <DashboardPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/dashboard"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <DashboardPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/users"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <UserManagementPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/categories"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <CategoryManagementPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/products"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <ProductManagementPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/orders"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <OrderManagementPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/discounts"
        element={
          <AdminLayout>
            <ProtectedRoute requireAdmin>
              <DiscountManagementPage />
            </ProtectedRoute>
          </AdminLayout>
        }
      />
      <Route
        path="/admin/overview"
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
