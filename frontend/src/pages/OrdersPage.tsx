import React, { useState, useEffect } from 'react';
import { Container, Typography, Box, Card, CardContent, Chip, Button } from '@mui/material';
import { Loader2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { orderApi, type OrderResponse, type OrderDetailResponse } from '../api/orderApi';
import { useAppSelector } from '../redux';
import ToastContainer from '../components/ToastContainer';
import OrderDetailModal from '../components/OrderDetailModal';
import { useToast } from '../hooks/useToast';
import { PageLayout } from '../components/layout';

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
  
  // Order detail modal state
  const [selectedOrder, setSelectedOrder] = useState<OrderDetailResponse | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [loadingOrderDetail, setLoadingOrderDetail] = useState(false);

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
        showError('Lỗi khi tải đơn hàng', response.message || 'Không thể tải đơn hàng của bạn');
        // Set empty orders array on error
        setOrders([]);
      }
    } catch (error: any) {
      console.error('Error fetching orders:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để xem đơn hàng của bạn');
        navigate('/login');
      } else {
        showError('Lỗi khi tải đơn hàng', error.message || 'Đã xảy ra lỗi không mong muốn khi tải đơn hàng của bạn');
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
      showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để hủy đơn hàng');
      navigate('/login');
      return;
    }

    try {
      const response = await orderApi.cancelOrder(orderId);
      if (response.success) {
        showSuccess('Đơn hàng đã được hủy', 'Đơn hàng của bạn đã được hủy thành công');
        // Refresh orders list
        fetchOrders();
        // Close detail modal if open
        setIsDetailModalOpen(false);
        setSelectedOrder(null);
      } else {
        showError('Lỗi khi hủy đơn hàng', response.message || 'Không thể hủy đơn hàng');
      }
    } catch (error: any) {
      console.error('Error cancelling order:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để hủy đơn hàng');
        navigate('/login');
      } else {
        showError('Lỗi khi hủy đơn hàng', error.message || 'Đã xảy ra lỗi không mong muốn khi hủy đơn hàng');
      }
    }
  };

  const handleViewOrderDetails = async (orderId: number) => {
    if (!isAuthenticated) {
      showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để xem chi tiết đơn hàng');
      navigate('/login');
      return;
    }

    try {
      setLoadingOrderDetail(true);
      const response = await orderApi.getOrderDetails(orderId);
      
      if (response.success && response.data) {
        setSelectedOrder(response.data);
        setIsDetailModalOpen(true);
      } else {
        showError('Lỗi khi tải chi tiết đơn hàng', response.message || 'Không thể tải chi tiết đơn hàng');
      }
    } catch (error: any) {
      console.error('Error fetching order details:', error);
      
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để xem chi tiết đơn hàng');
        navigate('/login');
      } else {
        showError('Lỗi khi tải chi tiết đơn hàng', error.message || 'Đã xảy ra lỗi không mong muốn khi tải chi tiết đơn hàng');
      }
    } finally {
      setLoadingOrderDetail(false);
    }
  };

  const handleCloseDetailModal = () => {
    setIsDetailModalOpen(false);
    setSelectedOrder(null);
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
    <PageLayout>
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
            className="px-3 py-1 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent bg-white text-black"
          >
            <option value="createdAt-desc">Mới nhất</option>
            <option value="createdAt-asc">Cũ nhất</option>
            <option value="totalAmount-desc">Cao nhất</option>
            <option value="totalAmount-asc">Thấp nhất</option>
            <option value="status-asc">Trạng thái A-Z</option>
          </select>
        </Box>
      </Box>

      {!orders || orders.length === 0 ? (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="h6" gutterBottom>
              Không tìm thấy đơn hàng
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Bắt đầu mua sắm để xem đơn hàng của bạn ở đây
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
                    <Button 
                      size="small" 
                      variant="outlined"
                      onClick={() => handleViewOrderDetails(order.orderId)}
                      disabled={loadingOrderDetail}
                    >
                      {loadingOrderDetail ? 'Đang tải...' : 'Xem chi tiết'}
                    </Button>
                    {(order.status === 'PENDING' || order.status === 'CONFIRMED') && (
                      <Button 
                        size="small" 
                        color="error" 
                        variant="outlined"
                        onClick={() => handleCancelOrder(order.orderId)}
                      >
                        Hủy đơn hàng
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
                  Trước
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
                  Tiếp
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

      {/* Order Detail Modal */}
      <OrderDetailModal
        order={selectedOrder}
        isOpen={isDetailModalOpen}
        onClose={handleCloseDetailModal}
        onCancelOrder={handleCancelOrder}
      />
    </Container>
    </PageLayout>
  );
};

export default OrdersPage;
