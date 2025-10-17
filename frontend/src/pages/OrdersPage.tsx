import React, { useState, useEffect } from 'react';
import { Container, Typography, Box, Card, CardContent, Chip, Button } from '@mui/material';
import { Loader2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { orderApi, type OrderResponse } from '../api/orderApi';
import { useAppSelector } from '../redux';
import ToastContainer from '../components/ToastContainer';
import { useToast } from '../hooks/useToast';

const OrdersPage: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const { toasts, removeToast, showSuccess, showError } = useToast();
  
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [sortBy, setSortBy] = useState<'createdAt' | 'totalAmount' | 'status'>('createdAt');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');

  // Fetch orders from API
  const fetchOrders = async () => {
    if (!isAuthenticated) {
      showError('Authentication Required', 'Please sign in to view your orders');
      navigate('/login');
      return;
    }

    try {
      setLoading(true);
      const response = await orderApi.getOrders({
        page: currentPage,
        size: 10,
        sortBy,
        sortDirection
      });

      if (response.success && response.data) {
        setOrders(response.data.data || []);
        setTotalPages(response.data.totalPages || 0);
      } else {
        showError('Failed to Load Orders', response.message || 'Unable to fetch your orders');
        // Set empty orders array on error
        setOrders([]);
      }
    } catch (error: any) {
      console.error('Error fetching orders:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        showError('Authentication Required', 'Please sign in to view your orders');
        navigate('/login');
      } else {
        showError('Failed to Load Orders', error.message || 'An unexpected error occurred while loading your orders');
      }
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [currentPage, sortBy, sortDirection]);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'DELIVERED': return 'success';
      case 'CONFIRMED': return 'info';
      case 'SHIPPED': return 'info';
      case 'PENDING': return 'warning';
      case 'CANCELLED': return 'error';
      default: return 'default';
    }
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const handleCancelOrder = async (orderId: number) => {
    if (!isAuthenticated) {
      showError('Authentication Required', 'Please sign in to cancel orders');
      navigate('/login');
      return;
    }

    try {
      const response = await orderApi.cancelOrder(orderId);
      if (response.success) {
        showSuccess('Order Cancelled', 'Your order has been successfully cancelled');
        // Refresh orders list
        fetchOrders();
      } else {
        showError('Failed to Cancel Order', response.message || 'Unable to cancel the order');
      }
    } catch (error: any) {
      console.error('Error cancelling order:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        showError('Authentication Required', 'Please sign in to cancel orders');
        navigate('/login');
      } else {
        showError('Failed to Cancel Order', error.message || 'An unexpected error occurred while cancelling the order');
      }
    }
  };

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <div className="flex items-center justify-center py-12">
          <div className="text-center">
            <Loader2 className="w-12 h-12 text-green-600 animate-spin mx-auto mb-4" />
            <p className="text-gray-600">Loading your orders...</p>
          </div>
        </div>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            📦 My Orders
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Track your orders and view order history
          </Typography>
        </Box>
        
        {/* Sort Controls */}
        <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
          <Typography variant="body2" color="text.secondary">Sort by:</Typography>
          <select
            value={`${sortBy}-${sortDirection}`}
            onChange={(e) => {
              const [field, direction] = e.target.value.split('-');
              setSortBy(field as 'createdAt' | 'totalAmount' | 'status');
              setSortDirection(direction as 'asc' | 'desc');
              setCurrentPage(0);
            }}
            className="px-3 py-1 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent"
          >
            <option value="createdAt-desc">Newest First</option>
            <option value="createdAt-asc">Oldest First</option>
            <option value="totalAmount-desc">Highest Amount</option>
            <option value="totalAmount-asc">Lowest Amount</option>
            <option value="status-asc">Status A-Z</option>
          </select>
        </Box>
      </Box>

      {!orders || orders.length === 0 ? (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="h6" gutterBottom>
              No orders found
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Start shopping to see your orders here
            </Typography>
          </CardContent>
        </Card>
      ) : (
        <>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            {orders && orders.map((order) => (
              <Card key={order.orderId}>
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                    <Box>
                      <Typography variant="h6" gutterBottom>
                        Order #{order.orderNumber}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Date: {formatDate(order.createdAt)}
                      </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 1 }}>
                      <Chip 
                        label={order.status} 
                        color={getStatusColor(order.status) as any}
                        size="small"
                      />
                      <Typography variant="h6" color="primary">
                        {formatPrice(order.totalAmount)}
                      </Typography>
                    </Box>
                  </Box>
                  
                  <Typography variant="body2" gutterBottom>
                    Items: {order.totalItems} items
                  </Typography>
                  
                  <Box sx={{ display: 'flex', gap: 1, mt: 2 }}>
                    <Button size="small" variant="outlined">
                      View Details
                    </Button>
                    {(order.status === 'PENDING' || order.status === 'CONFIRMED') && (
                      <Button 
                        size="small" 
                        color="error" 
                        variant="outlined"
                        onClick={() => handleCancelOrder(order.orderId)}
                      >
                        Cancel Order
                      </Button>
                    )}
                  </Box>
                </CardContent>
              </Card>
            ))}
          </Box>

          {/* Pagination */}
          {totalPages > 1 && (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Button
                  onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
                  disabled={currentPage === 0}
                  variant="outlined"
                  size="small"
                >
                  Previous
                </Button>
                
                {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                  const pageNumber = Math.max(0, Math.min(totalPages - 1, currentPage - 2 + i));
                  return (
                    <Button
                      key={pageNumber}
                      onClick={() => setCurrentPage(pageNumber)}
                      variant={currentPage === pageNumber ? "contained" : "outlined"}
                      size="small"
                      sx={{ minWidth: 40 }}
                    >
                      {pageNumber + 1}
                    </Button>
                  );
                })}
                
                <Button
                  onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
                  disabled={currentPage === totalPages - 1}
                  variant="outlined"
                  size="small"
                >
                  Next
                </Button>
              </Box>
            </Box>
          )}
        </>
      )}

      {/* Toast Notifications */}
      <ToastContainer
        toasts={toasts}
        onRemoveToast={removeToast}
      />
    </Container>
  );
};

export default OrdersPage;
