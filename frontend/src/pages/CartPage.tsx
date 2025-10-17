import React, { useState } from 'react';
import { Container, Typography, Box, Card, CardContent, Button, List, ListItem, ListItemText, Divider } from '@mui/material';
import { useAppSelector, useAppDispatch } from '../redux';
import { clearCart, removeFromCart, updateQuantity } from '../redux/slices/cartSlice';
import { syncCartWithBackend } from '../redux/thunks/cartThunks';
import type { CartItem } from '../redux/slices/cartSlice';
import ToastContainer from '../components/ToastContainer';
import { useToast } from '../hooks/useToast';
import CheckoutForm from '../components/CheckoutForm';
import { orderApi, type CreateOrderRequest } from '../api/orderApi';
import { PageLayout } from '../components/layout';

const CartPage: React.FC = () => {
  const dispatch = useAppDispatch();
  const { items, totalItems, totalAmount } = useAppSelector((state) => state.cart);
  const { toasts, removeToast, showSuccess, showWarning, showError } = useToast();
  
  const [checkoutOpen, setCheckoutOpen] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  const handleRemoveItem = (itemId: number) => {
    const item = items.find(item => item.id === itemId);
    dispatch(removeFromCart(itemId));
    if (item) {
      showWarning('Đã xóa sản phẩm', `${item.productName} đã được xóa khỏi giỏ hàng của bạn`);
    }
  };

  const handleUpdateQuantity = (itemId: number, newQuantity: number) => {
    if (newQuantity <= 0) {
      handleRemoveItem(itemId);
    } else {
      dispatch(updateQuantity({ id: itemId, quantity: newQuantity }));
    }
  };

  const handleClearCart = () => {
    dispatch(clearCart());
    showSuccess('Đã xóa giỏ hàng', 'Tất cả sản phẩm đã được xóa khỏi giỏ hàng của bạn');
  };

  const handleCheckout = () => {
    if (items.length === 0) {
      showWarning('Giỏ hàng trống', 'Vui lòng thêm sản phẩm vào giỏ hàng trước khi thanh toán');
      return;
    }
    setCheckoutOpen(true);
  };

  const handleCheckoutConfirm = async (orderData: CreateOrderRequest) => {
    setIsProcessing(true);
    
    try {
      // First, sync the local cart with the backend
      if (items.length > 0) {
        await dispatch(syncCartWithBackend(items)).unwrap();
      }
      
      // Then create the order
      const response = await orderApi.createOrder(orderData);
      
      if (response.success && response.data) {
        // Clear the cart after successful order creation
        dispatch(clearCart());
        
        // Show success message
        showSuccess(
          'Đặt hàng thành công!', 
          `Đơn hàng #${response.data.orderNumber} của bạn đã được đặt. Tổng cộng: $${response.data.totalAmount.toFixed(2)}`
        );
        
        // Close checkout dialog
        setCheckoutOpen(false);
      } else {
        showError('Đặt hàng thất bại', response.message || 'Không thể tạo đơn hàng. Vui lòng thử lại.');
      }
    } catch (error) {
      console.error('Checkout error:', error);
      showError('Đặt hàng thất bại', 'Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.');
    } finally {
      setIsProcessing(false);
    }
  };

  const handleCheckoutClose = () => {
    if (!isProcessing) {
      setCheckoutOpen(false);
    }
  };

  return (
    <PageLayout>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        🛒 Giỏ hàng
      </Typography>

      {items.length === 0 ? (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="h6" gutterBottom>
              Giỏ hàng của bạn trống
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Thêm một số sản phẩm để bắt đầu
            </Typography>
          </CardContent>
        </Card>
      ) : (
        <Box>
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <List>
                {items.map((item: CartItem) => (
                  <React.Fragment key={item.id}>
                    <ListItem>
                      <ListItemText
                        primary={item.productName}
                        secondary={`$${item.productPrice} mỗi cái`}
                      />
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Button
                          size="small"
                          onClick={() => handleUpdateQuantity(item.id, item.quantity - 1)}
                        >
                          -
                        </Button>
                        <Typography sx={{ minWidth: 40, textAlign: 'center' }}>
                          {item.quantity}
                        </Typography>
                        <Button
                          size="small"
                          onClick={() => handleUpdateQuantity(item.id, item.quantity + 1)}
                        >
                          +
                        </Button>
                        <Typography sx={{ minWidth: 80, textAlign: 'right' }}>
                          ${item.subtotal.toFixed(2)}
                        </Typography>
                        <Button
                          color="error"
                          size="small"
                          onClick={() => handleRemoveItem(item.id)}
                        >
                          Xóa
                        </Button>
                      </Box>
                    </ListItem>
                    <Divider />
                  </React.Fragment>
                ))}
              </List>
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6">
                  Tổng sản phẩm: {totalItems}
                </Typography>
                <Typography variant="h5" color="primary">
                  Tổng cộng: ${totalAmount.toFixed(2)}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button
                  variant="outlined"
                  onClick={handleClearCart}
                >
                  Xóa giỏ hàng
                </Button>
                <Button
                  variant="contained"
                  onClick={handleCheckout}
                  sx={{ flexGrow: 1 }}
                >
                  Tiến hành thanh toán
                </Button>
              </Box>
            </CardContent>
          </Card>
        </Box>
      )}

      {/* Toast Notifications */}
      <ToastContainer
        toasts={toasts}
        onRemoveToast={removeToast}
      />

      {/* Checkout Dialog */}
      <CheckoutForm
        open={checkoutOpen}
        onClose={handleCheckoutClose}
        onConfirm={handleCheckoutConfirm}
        loading={isProcessing}
      />
      </Container>
    </PageLayout>
  );
};

export default CartPage;
