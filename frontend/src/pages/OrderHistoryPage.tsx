import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Chip,
  IconButton,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Pagination,
  Alert,
  CircularProgress,
  Snackbar,
  Grid,
} from '@mui/material';
import {
  Visibility,
  Search,
  FilterList,
  Refresh,
  Cancel,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useOrderStore } from '../stores/orderStore';
import { useAuthStore } from '../stores/authStore';

const OrderHistoryPage: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const {
    orders,
    filters,
    pagination,
    isLoading,
    error,
    fetchOrders,
    setFilters,
    clearFilters,
    setPage,
    setPageSize,
    cancelOrder,
  } = useOrderStore();

  const [searchTerm, setSearchTerm] = useState('');
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  useEffect(() => {
    if (isAuthenticated) {
      fetchOrders();
    }
  }, [isAuthenticated, fetchOrders]);

  const handleSearch = () => {
    setFilters({ search: searchTerm, page: 0 });
  };

  const handleStatusFilter = (status: string) => {
    setFilters({ status: status || undefined, page: 0 });
  };

  const handlePageChange = (_event: React.ChangeEvent<unknown>, page: number) => {
    setPage(page - 1);
  };

  const handlePageSizeChange = (event: any) => {
    setPageSize(parseInt(event.target.value));
  };

  const handleViewOrder = (orderId: number) => {
    navigate(`/orders/${orderId}`);
  };

  const handleCancelOrder = async (orderId: number) => {
    try {
      await cancelOrder(orderId);
      setSnackbarMessage('Đã hủy đơn hàng thành công');
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể hủy đơn hàng');
      setSnackbarOpen(true);
    }
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
      month: '2-digit',
      day: '2-digit',
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
            Bạn cần đăng nhập để xem lịch sử đơn hàng
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

  if (isLoading && orders.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <Box textAlign="center">
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Đang tải lịch sử đơn hàng...
            </Typography>
          </Box>
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button onClick={() => fetchOrders()} variant="outlined">
          Thử lại
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={4}>
        <Typography variant="h4" component="h1" fontWeight="bold">
          Lịch sử đơn hàng
        </Typography>
        <Button
          variant="outlined"
          startIcon={<Refresh />}
          onClick={() => fetchOrders()}
          disabled={isLoading}
        >
          Làm mới
        </Button>
      </Box>

      {/* Filters */}
      <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
        <Box display="flex" alignItems="center" mb={2}>
          <FilterList sx={{ mr: 1 }} />
          <Typography variant="h6" fontWeight="bold">
            Bộ lọc
          </Typography>
        </Box>
        
        <Grid container spacing={2} alignItems="center">
          <Grid size={{ xs: 12, md: 4 }}>
            <TextField
              fullWidth
              label="Tìm kiếm đơn hàng"
              placeholder="Nhập mã đơn hàng..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              InputProps={{
                endAdornment: (
                  <IconButton onClick={handleSearch} edge="end">
                    <Search />
                  </IconButton>
                ),
              }}
            />
          </Grid>
          
          <Grid size={{ xs: 12, md: 3 }}>
            <FormControl fullWidth>
              <InputLabel>Trạng thái</InputLabel>
              <Select
                value={filters.status || ''}
                onChange={(e) => handleStatusFilter(e.target.value)}
                label="Trạng thái"
              >
                <MenuItem value="">Tất cả trạng thái</MenuItem>
                <MenuItem value="PENDING">Chờ xử lý</MenuItem>
                <MenuItem value="CONFIRMED">Đã xác nhận</MenuItem>
                <MenuItem value="SHIPPED">Đang giao</MenuItem>
                <MenuItem value="DELIVERED">Đã giao</MenuItem>
                <MenuItem value="CANCELLED">Đã hủy</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          
          <Grid size={{ xs: 12, md: 2 }}>
            <FormControl fullWidth>
              <InputLabel>Số đơn/trang</InputLabel>
              <Select
                value={pagination.size}
                onChange={handlePageSizeChange}
                label="Số đơn/trang"
              >
                <MenuItem value={5}>5 đơn hàng</MenuItem>
                <MenuItem value={10}>10 đơn hàng</MenuItem>
                <MenuItem value={20}>20 đơn hàng</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          
          <Grid size={{ xs: 12, md: 3 }}>
            <Button
              variant="outlined"
              onClick={clearFilters}
              fullWidth
            >
              Xóa bộ lọc
            </Button>
          </Grid>
        </Grid>
      </Paper>

      {/* Orders Table */}
      {orders.length === 0 ? (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary" gutterBottom>
            Chưa có đơn hàng nào
          </Typography>
          <Typography variant="body2" color="text.secondary" paragraph>
            Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm!
          </Typography>
          <Button
            variant="contained"
            onClick={() => navigate('/products')}
          >
            Mua sắm ngay
          </Button>
        </Box>
      ) : (
        <>
          <TableContainer component={Paper} elevation={2}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Mã đơn hàng</TableCell>
                  <TableCell>Ngày đặt</TableCell>
                  <TableCell>Sản phẩm</TableCell>
                  <TableCell align="right">Tổng tiền</TableCell>
                  <TableCell align="center">Trạng thái</TableCell>
                  <TableCell align="center">Thao tác</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orders.map((order) => (
                  <TableRow key={order.id} hover>
                    <TableCell>
                      <Typography variant="body2" fontWeight="bold">
                        #{order.id}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2">
                        {formatDate(order.createdAt)}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2">
                        {order.items.length} sản phẩm
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {order.items[0]?.product.name}
                        {order.items.length > 1 && ` và ${order.items.length - 1} sản phẩm khác`}
                      </Typography>
                    </TableCell>
                    <TableCell align="right">
                      <Typography variant="body1" fontWeight="bold">
                        {formatPrice(order.totalAmount)}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Chip
                        label={getStatusLabel(order.status)}
                        color={getStatusColor(order.status) as any}
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Box display="flex" gap={1} justifyContent="center">
                        <IconButton
                          size="small"
                          onClick={() => handleViewOrder(order.id)}
                          color="primary"
                        >
                          <Visibility />
                        </IconButton>
                        {canCancelOrder(order.status) && (
                          <IconButton
                            size="small"
                            onClick={() => handleCancelOrder(order.id)}
                            color="error"
                          >
                            <Cancel />
                          </IconButton>
                        )}
                      </Box>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          {/* Pagination */}
          {pagination.totalPages > 1 && (
            <Box display="flex" justifyContent="center" mt={4}>
              <Pagination
                count={pagination.totalPages}
                page={pagination.currentPage + 1}
                onChange={handlePageChange}
                color="primary"
                size="large"
                disabled={isLoading}
                showFirstButton
                showLastButton
              />
            </Box>
          )}
        </>
      )}

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

export default OrderHistoryPage;
