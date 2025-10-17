import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Typography,
  Divider,
  Alert,
  CircularProgress
} from '@mui/material';
import { useAppSelector } from '../redux';
import type { CreateOrderRequest } from '../api/orderApi';

interface CheckoutFormProps {
  open: boolean;
  onClose: () => void;
  onConfirm: (orderData: CreateOrderRequest) => void;
  loading?: boolean;
}

const CheckoutForm: React.FC<CheckoutFormProps> = ({
  open,
  onClose,
  onConfirm,
  loading = false
}) => {
  const { items, totalAmount } = useAppSelector((state) => state.cart);
  const { user } = useAppSelector((state) => state.auth);

  const [formData, setFormData] = useState({
    shippingAddress: user?.address || '',
    customerName: user?.fullName || '',
    customerEmail: user?.email || '',
    phoneNumber: user?.phone || '',
    notes: '',
    discountCode: ''
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleInputChange = (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value
    }));
    
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }));
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.shippingAddress.trim()) {
      newErrors.shippingAddress = 'Địa chỉ giao hàng là bắt buộc';
    }

    if (!formData.customerName.trim()) {
      newErrors.customerName = 'Tên khách hàng là bắt buộc';
    }

    if (!formData.customerEmail.trim()) {
      newErrors.customerEmail = 'Email khách hàng là bắt buộc';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.customerEmail)) {
      newErrors.customerEmail = 'Vui lòng nhập địa chỉ email hợp lệ';
    }

    if (formData.phoneNumber && !/^[0-9+\-\s()]+$/.test(formData.phoneNumber)) {
      newErrors.phoneNumber = 'Vui lòng nhập số điện thoại hợp lệ';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    const orderData: CreateOrderRequest = {
      shippingAddress: formData.shippingAddress.trim(),
      customerName: formData.customerName.trim(),
      customerEmail: formData.customerEmail.trim(),
      phoneNumber: formData.phoneNumber.trim() || undefined,
      notes: formData.notes.trim() || undefined,
      discountCode: formData.discountCode.trim() || undefined,
    };

    onConfirm(orderData);
  };

  const handleClose = () => {
    if (!loading) {
      onClose();
    }
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        sx: { minHeight: '600px' }
      }}
    >
      <DialogTitle>
        <Typography variant="h5" component="div">
          🛒 Thanh toán
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Xem đơn hàng và cung cấp thông tin giao hàng
        </Typography>
      </DialogTitle>

      <DialogContent dividers>
        <Box component="form" onSubmit={handleSubmit} noValidate>
          {/* Order Summary */}
          <Box sx={{ mb: 3 }}>
            <Typography variant="h6" gutterBottom>
              Tóm tắt đơn hàng
            </Typography>
            <Box sx={{ bgcolor: 'grey.50', p: 2, borderRadius: 1 }}>
              {items.map((item) => (
                <Box key={item.id} sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography variant="body2">
                    {item.productName} x {item.quantity}
                  </Typography>
                  <Typography variant="body2" fontWeight="medium">
                    ${item.subtotal.toFixed(2)}
                  </Typography>
                </Box>
              ))}
              <Divider sx={{ my: 1 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="h6">Total:</Typography>
                <Typography variant="h6" color="primary">
                  ${totalAmount.toFixed(2)}
                </Typography>
              </Box>
            </Box>
          </Box>

          {/* Shipping Information */}
          <Typography variant="h6" gutterBottom sx={{ mt: 3 }}>
            Thông tin giao hàng
          </Typography>

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              fullWidth
              label="Tên khách hàng *"
              value={formData.customerName}
              onChange={handleInputChange('customerName')}
              error={!!errors.customerName}
              helperText={errors.customerName}
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Email *"
              type="email"
              value={formData.customerEmail}
              onChange={handleInputChange('customerEmail')}
              error={!!errors.customerEmail}
              helperText={errors.customerEmail}
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Số điện thoại"
              value={formData.phoneNumber}
              onChange={handleInputChange('phoneNumber')}
              error={!!errors.phoneNumber}
              helperText={errors.phoneNumber || 'Optional'}
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Địa chỉ giao hàng *"
              multiline
              rows={3}
              value={formData.shippingAddress}
              onChange={handleInputChange('shippingAddress')}
              error={!!errors.shippingAddress}
              helperText={errors.shippingAddress}
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Ghi chú đơn hàng"
              multiline
              rows={2}
              value={formData.notes}
              onChange={handleInputChange('notes')}
              helperText="Bất kỳ hướng dẫn đặc biệt nào cho đơn hàng của bạn"
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Mã giảm giá"
              value={formData.discountCode}
              onChange={handleInputChange('discountCode')}
              helperText="Nhập mã giảm giá nếu bạn có"
              disabled={loading}
            />
          </Box>
        </Box>
      </DialogContent>

      <DialogActions sx={{ p: 3 }}>
        <Button 
          onClick={handleClose} 
          disabled={loading}
          size="large"
        >
          Hủy
        </Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading || items.length === 0}
          size="large"
          startIcon={loading ? <CircularProgress size={20} /> : null}
        >
          {loading ? 'Đang xử lý...' : 'Đặt đơn hàng'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CheckoutForm;
