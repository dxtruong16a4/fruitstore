import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  CircularProgress,
  Alert,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
} from '@mui/material';
import {
  ShoppingCart,
  Inventory,
  AttachMoney,
  TrendingUp,
  Refresh,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useProductStore } from '../stores/productStore';
import { useOrderStore } from '../stores/orderStore';

const AdminDashboard: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const { products, fetchProducts } = useProductStore();
  const { orders, fetchOrders } = useOrderStore();

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/');
      return;
    }

    const loadDashboardData = async () => {
      try {
        setIsLoading(true);
        await Promise.all([
          fetchProducts({ page: 0, size: 5 }),
          fetchOrders({ page: 0, size: 5 }),
        ]);
      } catch (error: any) {
        setError('Không thể tải dữ liệu dashboard');
      } finally {
        setIsLoading(false);
      }
    };

    loadDashboardData();
  }, [isAuthenticated, navigate, fetchProducts, fetchOrders]);

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN');
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

  if (!isAuthenticated) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error">
          Bạn không có quyền truy cập trang quản trị
        </Alert>
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
              Đang tải dữ liệu dashboard...
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
        <Button onClick={() => window.location.reload()} variant="outlined">
          Thử lại
        </Button>
      </Container>
    );
  }

  // Calculate statistics
  const totalProducts = products.length;
  const totalOrders = orders.length;
  const totalRevenue = orders.reduce((sum, order) => sum + order.totalAmount, 0);
  const pendingOrders = orders.filter(order => order.status === 'PENDING').length;

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={4}>
        <Typography variant="h4" component="h1" fontWeight="bold">
          Dashboard Quản trị
        </Typography>
        <Button
          variant="outlined"
          startIcon={<Refresh />}
          onClick={() => window.location.reload()}
        >
          Làm mới
        </Button>
      </Box>

      {/* Statistics Cards */}
      <Grid container spacing={3} mb={4}>
        <Grid size={{ xs: 12, sm: 6, md: 3 }}>
          <Card elevation={2}>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Tổng sản phẩm
                  </Typography>
                  <Typography variant="h4" fontWeight="bold">
                    {totalProducts}
                  </Typography>
                </Box>
                <Inventory color="primary" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
            <CardActions>
              <Button size="small" onClick={() => navigate('/admin/products')}>
                Xem chi tiết
              </Button>
            </CardActions>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 3 }}>
          <Card elevation={2}>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Tổng đơn hàng
                  </Typography>
                  <Typography variant="h4" fontWeight="bold">
                    {totalOrders}
                  </Typography>
                </Box>
                <ShoppingCart color="primary" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
            <CardActions>
              <Button size="small" onClick={() => navigate('/admin/orders')}>
                Xem chi tiết
              </Button>
            </CardActions>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 3 }}>
          <Card elevation={2}>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Tổng doanh thu
                  </Typography>
                  <Typography variant="h4" fontWeight="bold">
                    {formatPrice(totalRevenue)}
                  </Typography>
                </Box>
                <AttachMoney color="primary" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 3 }}>
          <Card elevation={2}>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Đơn chờ xử lý
                  </Typography>
                  <Typography variant="h4" fontWeight="bold" color="warning.main">
                    {pendingOrders}
                  </Typography>
                </Box>
                <TrendingUp color="warning" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
            <CardActions>
              <Button size="small" onClick={() => navigate('/admin/orders?status=PENDING')}>
                Xem chi tiết
              </Button>
            </CardActions>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        {/* Recent Orders */}
        <Grid size={{ xs: 12, md: 8 }}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
              <Typography variant="h6" fontWeight="bold">
                Đơn hàng gần đây
              </Typography>
              <Button
                size="small"
                onClick={() => navigate('/admin/orders')}
              >
                Xem tất cả
              </Button>
            </Box>
            
            {orders.length === 0 ? (
              <Box textAlign="center" py={4}>
                <Typography color="text.secondary">
                  Chưa có đơn hàng nào
                </Typography>
              </Box>
            ) : (
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Mã đơn</TableCell>
                      <TableCell>Ngày đặt</TableCell>
                      <TableCell>Khách hàng</TableCell>
                      <TableCell align="right">Tổng tiền</TableCell>
                      <TableCell align="center">Trạng thái</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {orders.slice(0, 5).map((order) => (
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
                            User #{order.userId}
                          </Typography>
                        </TableCell>
                        <TableCell align="right">
                          <Typography variant="body2" fontWeight="bold">
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
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </Paper>
        </Grid>

        {/* Recent Products */}
        <Grid size={{ xs: 12, md: 4 }}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
              <Typography variant="h6" fontWeight="bold">
                Sản phẩm mới
              </Typography>
              <Button
                size="small"
                onClick={() => navigate('/admin/products')}
              >
                Xem tất cả
              </Button>
            </Box>
            
            {products.length === 0 ? (
              <Box textAlign="center" py={4}>
                <Typography color="text.secondary">
                  Chưa có sản phẩm nào
                </Typography>
              </Box>
            ) : (
              <Box>
                {products.slice(0, 5).map((product) => (
                  <Box key={product.id} display="flex" alignItems="center" mb={2}>
                    <Box
                      component="img"
                      src={product.imageUrl || '/api/placeholder/50/50'}
                      alt={product.name}
                      sx={{
                        width: 50,
                        height: 50,
                        objectFit: 'cover',
                        borderRadius: 1,
                        mr: 2,
                        backgroundColor: '#f5f5f5',
                      }}
                    />
                    <Box flexGrow={1}>
                      <Typography variant="body2" fontWeight="bold" noWrap>
                        {product.name}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {formatPrice(product.price)}
                      </Typography>
                    </Box>
                  </Box>
                ))}
              </Box>
            )}
          </Paper>
        </Grid>
      </Grid>

      {/* Quick Actions */}
      <Paper elevation={2} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          Thao tác nhanh
        </Typography>
        <Box display="flex" gap={2} flexWrap="wrap">
          <Button
            variant="contained"
            onClick={() => navigate('/admin/products')}
            startIcon={<Inventory />}
          >
            Quản lý sản phẩm
          </Button>
          <Button
            variant="contained"
            onClick={() => navigate('/admin/orders')}
            startIcon={<ShoppingCart />}
          >
            Quản lý đơn hàng
          </Button>
          <Button
            variant="outlined"
            onClick={() => navigate('/admin/categories')}
          >
            Quản lý danh mục
          </Button>
          <Button
            variant="outlined"
            onClick={() => navigate('/admin/discounts')}
          >
            Quản lý giảm giá
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default AdminDashboard;
