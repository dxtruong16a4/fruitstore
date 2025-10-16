import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  Paper,
  Grid,
  Button,
  Chip,
  Divider,
  Alert,
  CircularProgress,
  Card,
  CardContent,
  CardActions,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Stepper,
  Step,
  StepLabel,
  Snackbar,
} from '@mui/material';
import {
  ArrowBack,
  Cancel,
  Refresh,
  Print,
} from '@mui/icons-material';
import { useOrderStore } from '../stores/orderStore';
import { useAuthStore } from '../stores/authStore';

const OrderDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const {
    currentOrder,
    isLoading,
    error,
    fetchOrderById,
    cancelOrder,
  } = useOrderStore();

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  useEffect(() => {
    if (id && isAuthenticated) {
      fetchOrderById(parseInt(id));
    }
  }, [id, isAuthenticated, fetchOrderById]);

  const handleCancelOrder = async () => {
    if (!currentOrder) return;

    try {
      await cancelOrder(currentOrder.id);
      setSnackbarMessage('Đã hủy đơn hàng thành công');
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể hủy đơn hàng');
      setSnackbarOpen(true);
    }
  };

  const handlePrintOrder = () => {
    window.print();
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'warning';
      case 'CONFIRMED':
        return 'info';
      case 'SHIPPED':
        return 'primary';
      case 'DELIVERED':
        return 'success';
      case 'CANCELLED':
        return 'error';
      default:
        return 'default';
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'Chờ xử lý';
      case 'CONFIRMED':
        return 'Đã xác nhận';
      case 'SHIPPED':
        return 'Đang giao';
      case 'DELIVERED':
        return 'Đã giao';
      case 'CANCELLED':
        return 'Đã hủy';
      default:
        return status;
    }
  };

  const getStatusSteps = (status: string) => {
    const steps = [
      { label: 'Đặt hàng', completed: true, active: false },
      { label: 'Xác nhận', completed: status !== 'PENDING', active: status === 'PENDING' },
      { label: 'Đang giao', completed: ['SHIPPED', 'DELIVERED'].includes(status), active: status === 'CONFIRMED' },
      { label: 'Hoàn thành', completed: status === 'DELIVERED', active: status === 'SHIPPED' },
    ];

    if (status === 'CANCELLED') {
      return steps.map(step => ({ ...step, completed: false, active: false }));
    }

    return steps;
  };

  const canCancelOrder = (status: string) => {
    return status === 'PENDING' || status === 'CONFIRMED';
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  if (!isAuthenticated) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box textAlign="center">
          <Alert severity="warning" sx={{ mb: 3 }}>
            Bạn cần đăng nhập để xem chi tiết đơn hàng
          </Alert>
          <Button
            variant="contained"
            onClick={() => navigate('/login')}
          >
            Đăng nhập
          </Button>
        </Box>
      </Container>
    );
  }

  if (isLoading) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <Box textAlign="center">
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Đang tải chi tiết đơn hàng...
            </Typography>
          </Box>
        </Box>
      </Container>
    );
  }

  if (error || !currentOrder) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error || 'Không tìm thấy đơn hàng'}
        </Alert>
        <Button onClick={() => navigate('/orders')} variant="outlined">
          Quay lại danh sách đơn hàng
        </Button>
      </Container>
    );
  }

  const statusSteps = getStatusSteps(currentOrder.status);

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={4}>
        <Box display="flex" alignItems="center">
          <Button
            startIcon={<ArrowBack />}
            onClick={() => navigate('/orders')}
            sx={{ mr: 2 }}
          >
            Quay lại
          </Button>
          <Typography variant="h4" component="h1" fontWeight="bold">
            Chi tiết đơn hàng #{currentOrder.id}
          </Typography>
        </Box>
        <Box display="flex" gap={1}>
          <Button
            variant="outlined"
            startIcon={<Refresh />}
            onClick={() => fetchOrderById(currentOrder.id)}
          >
            Làm mới
          </Button>
          <Button
            variant="outlined"
            startIcon={<Print />}
            onClick={handlePrintOrder}
          >
            In hóa đơn
          </Button>
        </Box>
      </Box>

      <Grid container spacing={3}>
        {/* Order Status Timeline */}
        <Grid size={{ xs: 12 }}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Trạng thái đơn hàng
            </Typography>
            <Stepper orientation="horizontal" activeStep={statusSteps.findIndex(step => step.active)}>
              {statusSteps.map((step, index) => (
                <Step key={index} completed={step.completed}>
                  <StepLabel
                    color={step.completed ? 'primary' : step.active ? 'primary' : 'disabled'}
                  >
                    {step.label}
                  </StepLabel>
                </Step>
              ))}
            </Stepper>
            <Box mt={2} display="flex" alignItems="center" gap={2}>
              <Chip
                label={getStatusLabel(currentOrder.status)}
                color={getStatusColor(currentOrder.status) as any}
                size="medium"
              />
              <Typography variant="body2" color="text.secondary">
                Cập nhật lần cuối: {formatDate(currentOrder.updatedAt)}
              </Typography>
            </Box>
          </Paper>
        </Grid>

        {/* Order Items */}
        <Grid size={{ xs: 12, md: 8 }}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Sản phẩm đã đặt
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Sản phẩm</TableCell>
                    <TableCell align="center">Số lượng</TableCell>
                    <TableCell align="right">Giá</TableCell>
                    <TableCell align="right">Thành tiền</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {currentOrder.items.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell>
                        <Box display="flex" alignItems="center">
                          <Box
                            component="img"
                            src={item.product.imageUrl || '/api/placeholder/60/60'}
                            alt={item.product.name}
                            sx={{
                              width: 60,
                              height: 60,
                              objectFit: 'cover',
                              borderRadius: 1,
                              mr: 2,
                              backgroundColor: '#f5f5f5',
                            }}
                          />
                          <Box>
                            <Typography variant="body1" fontWeight="bold">
                              {item.product.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              SKU: {item.product.id}
                            </Typography>
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell align="center">
                        <Typography variant="body1" fontWeight="bold">
                          {item.quantity}
                        </Typography>
                      </TableCell>
                      <TableCell align="right">
                        <Typography variant="body1">
                          {formatPrice(item.price)}
                        </Typography>
                      </TableCell>
                      <TableCell align="right">
                        <Typography variant="body1" fontWeight="bold">
                          {formatPrice(item.subtotal)}
                        </Typography>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>

        {/* Order Summary */}
        <Grid size={{ xs: 12, md: 4 }}>
          <Card elevation={2}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Tóm tắt đơn hàng
              </Typography>
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography variant="body2">Mã đơn hàng:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  #{currentOrder.id}
                </Typography>
              </Box>
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography variant="body2">Ngày đặt:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {formatDate(currentOrder.createdAt)}
                </Typography>
              </Box>
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography variant="body2">Số sản phẩm:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {currentOrder.items.length}
                </Typography>
              </Box>
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography variant="body2">Tạm tính:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {formatPrice(currentOrder.totalAmount)}
                </Typography>
              </Box>
              
              {currentOrder.discountAmount && currentOrder.discountAmount > 0 && (
                <Box display="flex" justifyContent="space-between" mb={1}>
                  <Typography variant="body2" color="success.main">
                    Giảm giá:
                  </Typography>
                  <Typography variant="body2" color="success.main" fontWeight="bold">
                    -{formatPrice(currentOrder.discountAmount)}
                  </Typography>
                </Box>
              )}
              
              <Divider sx={{ my: 2 }} />
              
              <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="h6">Tổng cộng:</Typography>
                <Typography variant="h6" color="primary" fontWeight="bold">
                  {formatPrice(currentOrder.totalAmount - (currentOrder.discountAmount || 0))}
                </Typography>
              </Box>
            </CardContent>
          </Card>

          {/* Shipping Information */}
          <Card elevation={2} sx={{ mt: 2 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Thông tin giao hàng
              </Typography>
              
              <Box mb={1}>
                <Typography variant="body2" color="text.secondary">
                  Địa chỉ:
                </Typography>
                <Typography variant="body2">
                  {currentOrder.shippingAddress.street}
                </Typography>
                <Typography variant="body2">
                  {currentOrder.shippingAddress.city}, {currentOrder.shippingAddress.state}
                </Typography>
                <Typography variant="body2">
                  {currentOrder.shippingAddress.zipCode}, {currentOrder.shippingAddress.country}
                </Typography>
              </Box>
              
              <Box mb={1}>
                <Typography variant="body2" color="text.secondary">
                  Phương thức thanh toán:
                </Typography>
                <Typography variant="body2" fontWeight="bold">
                  {currentOrder.paymentMethod === 'cod' ? 'Thanh toán khi nhận hàng (COD)' : 
                   currentOrder.paymentMethod === 'bank_transfer' ? 'Chuyển khoản ngân hàng' :
                   currentOrder.paymentMethod === 'credit_card' ? 'Thẻ tín dụng' : currentOrder.paymentMethod}
                </Typography>
              </Box>
            </CardContent>
          </Card>

          {/* Actions */}
          {canCancelOrder(currentOrder.status) && (
            <Card elevation={2} sx={{ mt: 2 }}>
              <CardActions>
                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<Cancel />}
                  onClick={handleCancelOrder}
                  fullWidth
                >
                  Hủy đơn hàng
                </Button>
              </CardActions>
            </Card>
          )}
        </Grid>
      </Grid>

      {/* Success Snackbar */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={handleSnackbarClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert onClose={handleSnackbarClose} severity="success" sx={{ width: '100%' }}>
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default OrderDetailPage;
