import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Grid,
  Typography,
  Box,
  Button,
  Paper,
  Chip,
  Divider,
  Alert,
  CircularProgress,
  IconButton,
  Snackbar,
  Breadcrumbs,
  Link,
} from '@mui/material';
import {
  ShoppingCart,
  ArrowBack,
  Home,
  NavigateNext,
  Add,
  Remove,
} from '@mui/icons-material';
import { useProductStore } from '../stores/productStore';
import { useCartStore } from '../stores/cartStore';

const ProductDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentProduct, isLoading, error, fetchProductById } = useProductStore();
  const { addToCart } = useCartStore();
  
  const [quantity, setQuantity] = useState(1);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  useEffect(() => {
    if (id) {
      fetchProductById(parseInt(id));
    }
  }, [id, fetchProductById]);

  const handleAddToCart = async () => {
    if (!currentProduct) return;

    try {
      await addToCart(currentProduct.id, quantity);
      setSnackbarMessage(`Đã thêm ${quantity} sản phẩm vào giỏ hàng!`);
      setSnackbarOpen(true);
    } catch (error) {
      setSnackbarMessage('Không thể thêm sản phẩm vào giỏ hàng');
      setSnackbarOpen(true);
    }
  };

  const handleQuantityChange = (delta: number) => {
    if (!currentProduct) return;
    
    const newQuantity = quantity + delta;
    if (newQuantity >= 1 && newQuantity <= currentProduct.stock) {
      setQuantity(newQuantity);
    }
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const isInStock = currentProduct ? currentProduct.stock > 0 : false;

  if (isLoading) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <Box textAlign="center">
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Đang tải thông tin sản phẩm...
            </Typography>
          </Box>
        </Box>
      </Container>
    );
  }

  if (error || !currentProduct) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box textAlign="center">
          <Alert severity="error" sx={{ mb: 2 }}>
            {error || 'Không tìm thấy sản phẩm'}
          </Alert>
          <Button
            variant="contained"
            startIcon={<ArrowBack />}
            onClick={() => navigate('/products')}
          >
            Quay lại danh sách sản phẩm
          </Button>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Breadcrumbs */}
      <Breadcrumbs separator={<NavigateNext fontSize="small" />} sx={{ mb: 3 }}>
        <Link
          component="button"
          variant="body2"
          onClick={() => navigate('/')}
          sx={{ display: 'flex', alignItems: 'center' }}
        >
          <Home sx={{ mr: 0.5, fontSize: 16 }} />
          Trang chủ
        </Link>
        <Link
          component="button"
          variant="body2"
          onClick={() => navigate('/products')}
        >
          Sản phẩm
        </Link>
        <Typography variant="body2" color="text.primary">
          {currentProduct.name}
        </Typography>
      </Breadcrumbs>

      {/* Back Button */}
      <Button
        startIcon={<ArrowBack />}
        onClick={() => navigate('/products')}
        sx={{ mb: 3 }}
      >
        Quay lại
      </Button>

      <Grid container spacing={4}>
        {/* Product Image */}
        <Grid size={{ xs: 12, md: 6 }}>
          <Paper
            elevation={2}
            sx={{
              p: 2,
              textAlign: 'center',
              backgroundColor: '#fafafa',
            }}
          >
            <Box
              component="img"
              src={currentProduct.imageUrl || '/api/placeholder/500/400'}
              alt={currentProduct.name}
              sx={{
                width: '100%',
                height: '400px',
                objectFit: 'cover',
                borderRadius: 1,
                backgroundColor: '#f5f5f5',
              }}
            />
          </Paper>
        </Grid>

        {/* Product Info */}
        <Grid size={{ xs: 12, md: 6 }}>
          <Box>
            <Box display="flex" alignItems="center" mb={2}>
              <Chip
                label={currentProduct.category.name}
                color="primary"
                variant="outlined"
                sx={{ mr: 2 }}
              />
              {!isInStock && (
                <Chip
                  label="Hết hàng"
                  color="error"
                  variant="filled"
                />
              )}
              {isInStock && currentProduct.stock <= 10 && (
                <Chip
                  label="Sắp hết hàng"
                  color="warning"
                  variant="filled"
                />
              )}
            </Box>

            <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
              {currentProduct.name}
            </Typography>

            <Typography
              variant="h5"
              color="primary"
              gutterBottom
              fontWeight="bold"
              sx={{ mb: 3 }}
            >
              {formatPrice(currentProduct.price)}
            </Typography>

            <Typography variant="body1" paragraph sx={{ mb: 3, lineHeight: 1.6 }}>
              {currentProduct.description}
            </Typography>

            <Box mb={3}>
              <Typography variant="h6" gutterBottom>
                Thông tin sản phẩm
              </Typography>
              <Box display="flex" flexDirection="column" gap={1}>
                <Box display="flex" justifyContent="space-between">
                  <Typography variant="body2" color="text.secondary">
                    Tồn kho:
                  </Typography>
                  <Typography
                    variant="body2"
                    color={currentProduct.stock > 10 ? 'success.main' : currentProduct.stock > 0 ? 'warning.main' : 'error.main'}
                    fontWeight="bold"
                  >
                    {currentProduct.stock} sản phẩm
                  </Typography>
                </Box>
                <Box display="flex" justifyContent="space-between">
                  <Typography variant="body2" color="text.secondary">
                    Danh mục:
                  </Typography>
                  <Typography variant="body2" fontWeight="bold">
                    {currentProduct.category.name}
                  </Typography>
                </Box>
                <Box display="flex" justifyContent="space-between">
                  <Typography variant="body2" color="text.secondary">
                    Ngày tạo:
                  </Typography>
                  <Typography variant="body2" fontWeight="bold">
                    {new Date(currentProduct.createdAt).toLocaleDateString('vi-VN')}
                  </Typography>
                </Box>
              </Box>
            </Box>

            <Divider sx={{ my: 3 }} />

            {/* Quantity and Add to Cart */}
            {isInStock && (
              <Box>
                <Typography variant="h6" gutterBottom>
                  Số lượng
                </Typography>
                <Box display="flex" alignItems="center" gap={2} mb={3}>
                  <Box display="flex" alignItems="center" border={1} borderColor="divider" borderRadius={1}>
                    <IconButton
                      onClick={() => handleQuantityChange(-1)}
                      disabled={quantity <= 1}
                      size="small"
                    >
                      <Remove />
                    </IconButton>
                    <Typography
                      variant="h6"
                      sx={{
                        minWidth: 40,
                        textAlign: 'center',
                        px: 1,
                      }}
                    >
                      {quantity}
                    </Typography>
                    <IconButton
                      onClick={() => handleQuantityChange(1)}
                      disabled={quantity >= currentProduct.stock}
                      size="small"
                    >
                      <Add />
                    </IconButton>
                  </Box>
                  <Typography variant="body2" color="text.secondary">
                    (Tối đa: {currentProduct.stock})
                  </Typography>
                </Box>

                <Button
                  variant="contained"
                  size="large"
                  startIcon={<ShoppingCart />}
                  onClick={handleAddToCart}
                  sx={{
                    py: 1.5,
                    px: 4,
                    fontSize: '1.1rem',
                    fontWeight: 'bold',
                  }}
                >
                  Thêm vào giỏ hàng
                </Button>
              </Box>
            )}

            {!isInStock && (
              <Alert severity="warning" sx={{ mt: 2 }}>
                Sản phẩm này hiện đang hết hàng. Vui lòng quay lại sau.
              </Alert>
            )}
          </Box>
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

export default ProductDetailPage;
