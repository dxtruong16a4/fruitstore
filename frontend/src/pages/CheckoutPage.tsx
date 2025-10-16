import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  Paper,
  TextField,
  Button,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Divider,
  Alert,
  CircularProgress,
  Card,
  CardContent,
  Snackbar,
  Stepper,
  Step,
  StepLabel,
  StepContent,
} from '@mui/material';
import {
  Payment,
  LocalShipping,
  CheckCircle,
  ArrowBack,
} from '@mui/icons-material';
import { useCartStore } from '../stores/cartStore';
import { useAuthStore } from '../stores/authStore';
import { useOrderStore } from '../stores/orderStore';
import { useDiscountStore } from '../stores/discountStore';

const schema = yup.object({
  shippingAddress: yup.object({
    street: yup.string().required('Địa chỉ là bắt buộc'),
    city: yup.string().required('Thành phố là bắt buộc'),
    state: yup.string().required('Tỉnh/Thành phố là bắt buộc'),
    zipCode: yup.string().required('Mã bưu điện là bắt buộc'),
    country: yup.string().required('Quốc gia là bắt buộc'),
  }),
  paymentMethod: yup.string().required('Phương thức thanh toán là bắt buộc'),
  discountCode: yup.string().optional().default(''),
});

type CheckoutFormData = yup.InferType<typeof schema>;

const CheckoutPage: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const { cart, fetchCart } = useCartStore();
  const { createOrder, isLoading: orderLoading } = useOrderStore();
  const { validateDiscount, appliedDiscount, clearDiscount } = useDiscountStore();
  
  const [activeStep, setActiveStep] = useState(0);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [isValidatingDiscount, setIsValidatingDiscount] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    setValue,
  } = useForm<CheckoutFormData>({
    resolver: yupResolver(schema),
    defaultValues: {
      shippingAddress: {
        street: '',
        city: '',
        state: '',
        zipCode: '',
        country: 'Việt Nam',
      },
      paymentMethod: 'cod',
      discountCode: '',
    },
  });

  const discountCode = watch('discountCode');

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (!cart || cart.items.length === 0) {
      navigate('/cart');
      return;
    }
    fetchCart();
  }, [isAuthenticated, cart, navigate, fetchCart]);

  const handleValidateDiscount = async () => {
    if (!discountCode || !cart) return;

    setIsValidatingDiscount(true);
    try {
      await validateDiscount(discountCode, cart.totalAmount);
      setSnackbarMessage('Mã giảm giá hợp lệ!');
      setSnackbarOpen(true);
    } catch (error: any) {
      setSnackbarMessage(error.response?.data?.message || 'Mã giảm giá không hợp lệ');
      setSnackbarOpen(true);
    } finally {
      setIsValidatingDiscount(false);
    }
  };

  const handleRemoveDiscount = () => {
    clearDiscount();
    setValue('discountCode', '');
  };

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const onSubmit = async (data: CheckoutFormData) => {
    if (!cart) return;

    try {
      const orderData = {
        items: cart.items.map(item => ({
          productId: item.productId,
          quantity: item.quantity,
        })),
        shippingAddress: data.shippingAddress,
        paymentMethod: data.paymentMethod,
        discountCode: appliedDiscount?.discount?.code,
      };

      await createOrder(orderData);
      setSnackbarMessage('Đặt hàng thành công!');
      setSnackbarOpen(true);
      navigate('/orders');
    } catch (error: any) {
      setSnackbarMessage(error.response?.data?.message || 'Đặt hàng thất bại');
      setSnackbarOpen(true);
    }
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

  if (!isAuthenticated || !cart || cart.items.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box textAlign="center">
          <CircularProgress />
        </Box>
      </Container>
    );
  }


  const totalAmount = cart.totalAmount;
  const discountAmount = appliedDiscount?.discountAmount || 0;
  const finalAmount = totalAmount - discountAmount;

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" mb={4}>
        <Button
          startIcon={<ArrowBack />}
          onClick={() => navigate('/cart')}
          sx={{ mr: 2 }}
        >
          Quay lại
        </Button>
        <Typography variant="h4" component="h1" fontWeight="bold">
          Thanh toán
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Checkout Form */}
        <Grid size={{ xs: 12, md: 8 }}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <Stepper activeStep={activeStep} orientation="vertical">
              {/* Step 1: Shipping Information */}
              <Step>
                <StepLabel icon={<LocalShipping />}>
                  Thông tin giao hàng
                </StepLabel>
                <StepContent>
                  <Box component="form" sx={{ mt: 2 }}>
                    <Grid container spacing={2}>
                      <Grid size={{ xs: 12 }}>
                        <TextField
                          {...register('shippingAddress.street')}
                          fullWidth
                          label="Địa chỉ"
                          error={!!errors.shippingAddress?.street}
                          helperText={errors.shippingAddress?.street?.message}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField
                          {...register('shippingAddress.city')}
                          fullWidth
                          label="Thành phố"
                          error={!!errors.shippingAddress?.city}
                          helperText={errors.shippingAddress?.city?.message}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField
                          {...register('shippingAddress.state')}
                          fullWidth
                          label="Tỉnh/Thành phố"
                          error={!!errors.shippingAddress?.state}
                          helperText={errors.shippingAddress?.state?.message}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField
                          {...register('shippingAddress.zipCode')}
                          fullWidth
                          label="Mã bưu điện"
                          error={!!errors.shippingAddress?.zipCode}
                          helperText={errors.shippingAddress?.zipCode?.message}
                        />
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField
                          {...register('shippingAddress.country')}
                          fullWidth
                          label="Quốc gia"
                          error={!!errors.shippingAddress?.country}
                          helperText={errors.shippingAddress?.country?.message}
                        />
                      </Grid>
                    </Grid>
                    <Box sx={{ mt: 2 }}>
                      <Button variant="contained" onClick={handleNext}>
                        Tiếp tục
                      </Button>
                    </Box>
                  </Box>
                </StepContent>
              </Step>

              {/* Step 2: Payment Method */}
              <Step>
                <StepLabel icon={<Payment />}>
                  Phương thức thanh toán
                </StepLabel>
                <StepContent>
                  <Box sx={{ mt: 2 }}>
                    <FormControl fullWidth error={!!errors.paymentMethod}>
                      <InputLabel>Phương thức thanh toán</InputLabel>
                      <Select
                        {...register('paymentMethod')}
                        label="Phương thức thanh toán"
                      >
                        <MenuItem value="cod">Thanh toán khi nhận hàng (COD)</MenuItem>
                        <MenuItem value="bank_transfer">Chuyển khoản ngân hàng</MenuItem>
                        <MenuItem value="credit_card">Thẻ tín dụng</MenuItem>
                      </Select>
                    </FormControl>
                    <Box sx={{ mt: 2 }}>
                      <Button onClick={handleBack} sx={{ mr: 1 }}>
                        Quay lại
                      </Button>
                      <Button variant="contained" onClick={handleNext}>
                        Tiếp tục
                      </Button>
                    </Box>
                  </Box>
                </StepContent>
              </Step>

              {/* Step 3: Order Confirmation */}
              <Step>
                <StepLabel icon={<CheckCircle />}>
                  Xác nhận đơn hàng
                </StepLabel>
                <StepContent>
                  <Box sx={{ mt: 2 }}>
                    <Typography variant="h6" gutterBottom>
                      Xác nhận thông tin đơn hàng
                    </Typography>
                    <Typography variant="body2" color="text.secondary" paragraph>
                      Vui lòng kiểm tra lại thông tin trước khi đặt hàng
                    </Typography>
                    <Box sx={{ mt: 2 }}>
                      <Button onClick={handleBack} sx={{ mr: 1 }}>
                        Quay lại
                      </Button>
                      <Button
                        variant="contained"
                        onClick={handleSubmit(onSubmit as any)}
                        disabled={orderLoading}
                        startIcon={orderLoading ? <CircularProgress size={20} /> : <CheckCircle />}
                      >
                        {orderLoading ? 'Đang xử lý...' : 'Đặt hàng'}
                      </Button>
                    </Box>
                  </Box>
                </StepContent>
              </Step>
            </Stepper>
          </Paper>
        </Grid>

        {/* Order Summary */}
        <Grid size={{ xs: 12, md: 4 }}>
          <Card elevation={2}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Tóm tắt đơn hàng
              </Typography>
              
              {/* Cart Items */}
              <Box mb={2}>
                {cart.items.map((item) => (
                  <Box key={item.id} display="flex" justifyContent="space-between" mb={1}>
                    <Typography variant="body2">
                      {item.product.name} x {item.quantity}
                    </Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {formatPrice(item.subtotal)}
                    </Typography>
                  </Box>
                ))}
              </Box>
              
              <Divider sx={{ my: 2 }} />
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography variant="body2">Tạm tính:</Typography>
                <Typography variant="body2" fontWeight="bold">
                  {formatPrice(totalAmount)}
                </Typography>
              </Box>
              
              {/* Discount Code */}
              <Box mb={2}>
                <TextField
                  {...register('discountCode')}
                  fullWidth
                  size="small"
                  label="Mã giảm giá"
                  disabled={!!appliedDiscount}
                  InputProps={{
                    endAdornment: appliedDiscount ? (
                      <Button size="small" onClick={handleRemoveDiscount}>
                        Xóa
                      </Button>
                    ) : (
                      <Button
                        size="small"
                        onClick={handleValidateDiscount}
                        disabled={!discountCode || isValidatingDiscount}
                      >
                        {isValidatingDiscount ? 'Đang kiểm tra...' : 'Áp dụng'}
                      </Button>
                    ),
                  }}
                />
                {appliedDiscount && (
                  <Alert severity="success" sx={{ mt: 1 }}>
                    Đã áp dụng mã giảm giá: {appliedDiscount.discount?.code}
                  </Alert>
                )}
              </Box>
              
              {discountAmount > 0 && (
                <Box display="flex" justifyContent="space-between" mb={1}>
                  <Typography variant="body2" color="success.main">
                    Giảm giá:
                  </Typography>
                  <Typography variant="body2" color="success.main" fontWeight="bold">
                    -{formatPrice(discountAmount)}
                  </Typography>
                </Box>
              )}
              
              <Divider sx={{ my: 2 }} />
              
              <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="h6">Tổng cộng:</Typography>
                <Typography variant="h6" color="primary" fontWeight="bold">
                  {formatPrice(finalAmount)}
                </Typography>
              </Box>
            </CardContent>
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

export default CheckoutPage;
