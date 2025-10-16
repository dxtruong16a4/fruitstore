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
  IconButton,
  Button,
  TextField,
  Divider,
  Alert,
  CircularProgress,
  Snackbar,
  Grid,
  Card,
  CardContent,
  CardActions,
} from '@mui/material';
import {
  Delete,
  Add,
  Remove,
  ShoppingCart,
  ShoppingBag,
  ArrowBack,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useCartStore } from '../stores/cartStore';
import { useAuthStore } from '../stores/authStore';

const CartPage: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuthStore();
  const {
    cart,
    isLoading,
    error,
    fetchCart,
    updateCartItem,
    removeFromCart,
    clearCart,
    clearError,
  } = useCartStore();

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
    }
  }, [isAuthenticated, fetchCart]);

  const handleQuantityChange = async (itemId: number, newQuantity: number) => {
    if (newQuantity < 1) return;

    try {
      await updateCartItem(itemId, newQuantity);
      setSnackbarMessage('Đã cập nhật số lượng sản phẩm');
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể cập nhật số lượng');
      setSnackbarOpen(true);
    }
  };

  const handleRemoveItem = async (itemId: number) => {
    try {
      await removeFromCart(itemId);
      setSnackbarMessage('Đã xóa sản phẩm khỏi giỏ hàng');
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể xóa sản phẩm');
      setSnackbarOpen(true);
    }
  };

  const handleClearCart = async () => {
    try {
      await clearCart();
      setSnackbarMessage('Đã xóa tất cả sản phẩm khỏi giỏ hàng');
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể xóa giỏ hàng');
      setSnackbarOpen(true);
    }
  };

  const handleCheckout = () => {
    navigate('/checkout');
  };

  const handleContinueShopping = () => {
    navigate('/products');
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  if (!isAuthenticated) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box textAlign="center">
          <Alert severity="warning" sx={{ mb: 3 }}>
            Bạn cần đăng nhập để xem giỏ hàng
          </Alert>
          <Button
            variant="contained"
            onClick={() => navigate('/login')}
            startIcon={<ShoppingCart />}
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
              Đang tải giỏ hàng...
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
        <Button onClick={fetchCart} variant="outlined">
          Thử lại
        </Button>
      </Container>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box textAlign="center">
          <ShoppingCart sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h5" gutterBottom>
            Giỏ hàng trống
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            Bạn chưa có sản phẩm nào trong giỏ hàng
          </Typography>
          <Button
            variant="contained"
            size="large"
            startIcon={<ShoppingBag />}
            onClick={handleContinueShopping}
            sx={{ mt: 2 }}
          >
            Tiếp tục mua sắm
          </Button>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={4}>
        <Box display="flex" alignItems="center">
          <Button
            startIcon={<ArrowBack />}
            onClick={() => navigate('/products')}
            sx={{ mr: 2 }}
          >
            Quay lại
          </Button>
          <Typography variant="h4" component="h1" fontWeight="bold">
            Giỏ hàng
          </Typography>
        </Box>
        <Button
          variant="outlined"
          color="error"
          startIcon={<Delete />}
          onClick={handleClearCart}
        >
          Xóa tất cả
        </Button>
      </Box>

      <Grid container spacing={3}>
        {/* Cart Items */}
        <Grid size={{ xs: 12, md: 8 }}>
          <Paper elevation={2}>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Sản phẩm</TableCell>
                    <TableCell align="center">Số lượng</TableCell>
                    <TableCell align="right">Giá</TableCell>
                    <TableCell align="right">Thành tiền</TableCell>
                    <TableCell align="center">Thao tác</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {cart.items.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell>
                        <Box display="flex" alignItems="center">
                          <Box
                            component="img"
                            src={item.product.imageUrl || '/api/placeholder/80/80'}
                            alt={item.product.name}
                            sx={{
                              width: 80,
                              height: 80,
                              objectFit: 'cover',
                              borderRadius: 1,
                              mr: 2,
                              backgroundColor: '#f5f5f5',
                            }}
                          />
                          <Box>
                            <Typography variant="h6" fontWeight="bold">
                              {item.product.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {formatPrice(item.product.price)} / sản phẩm
                            </Typography>
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell align="center">
                        <Box display="flex" alignItems="center" justifyContent="center">
                          <IconButton
                            size="small"
                            onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                            disabled={item.quantity <= 1}
                          >
                            <Remove />
                          </IconButton>
                          <TextField
                            value={item.quantity}
                            onChange={(e) => {
                              const newQuantity = parseInt(e.target.value) || 1;
                              handleQuantityChange(item.id, newQuantity);
                            }}
                            inputProps={{
                              min: 1,
                              style: { textAlign: 'center', width: '60px' },
                            }}
                            size="small"
                            type="number"
                          />
                          <IconButton
                            size="small"
                            onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                          >
                            <Add />
                          </IconButton>
                        </Box>
                      </TableCell>
                      <TableCell align="right">
                        <Typography variant="body1" fontWeight="bold">
                          {formatPrice(item.product.price)}
                        </Typography>
                      </TableCell>
                      <TableCell align="right">
                        <Typography variant="h6" color="primary" fontWeight="bold">
                          {formatPrice(item.subtotal)}
                        </Typography>
                      </TableCell>
                      <TableCell align="center">
                        <IconButton
                          color="error"
                          onClick={() => handleRemoveItem(item.id)}
                        >
                          <Delete />
                        </IconButton>
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
                <Typography variant="body2">Số sản phẩm:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {cart.totalItems}
                </Typography>
              </Box>
              
              <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="body2">Tạm tính:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {formatPrice(cart.totalAmount)}
                </Typography>
              </Box>
              
              <Divider sx={{ my: 2 }} />
              
              <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="h6">Tổng cộng:</Typography>
                <Typography variant="h6" color="primary" fontWeight="bold">
                  {formatPrice(cart.totalAmount)}
                </Typography>
              </Box>
            </CardContent>
            
            <CardActions sx={{ p: 2, pt: 0 }}>
              <Button
                variant="contained"
                fullWidth
                size="large"
                onClick={handleCheckout}
                sx={{ py: 1.5, fontWeight: 'bold' }}
              >
                Thanh toán
              </Button>
            </CardActions>
          </Card>
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

export default CartPage;
