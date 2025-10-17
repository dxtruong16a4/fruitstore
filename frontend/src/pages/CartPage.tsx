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
      showWarning('Item Removed', `${item.productName} has been removed from your cart`);
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
    showSuccess('Cart Cleared', 'All items have been removed from your cart');
  };

  const handleCheckout = () => {
    if (items.length === 0) {
      showWarning('Empty Cart', 'Please add items to your cart before checkout');
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
          'Order Placed Successfully!', 
          `Your order #${response.data.orderNumber} has been placed. Total: $${response.data.totalAmount.toFixed(2)}`
        );
        
        // Close checkout dialog
        setCheckoutOpen(false);
      } else {
        showError('Order Failed', response.message || 'Failed to create order. Please try again.');
      }
    } catch (error) {
      console.error('Checkout error:', error);
      showError('Order Failed', 'An unexpected error occurred. Please try again.');
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
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        🛒 Shopping Cart
      </Typography>

      {items.length === 0 ? (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="h6" gutterBottom>
              Your cart is empty
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Add some products to get started
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
                        secondary={`$${item.productPrice} each`}
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
                          Remove
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
                  Total Items: {totalItems}
                </Typography>
                <Typography variant="h5" color="primary">
                  Total: ${totalAmount.toFixed(2)}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button
                  variant="outlined"
                  onClick={handleClearCart}
                >
                  Clear Cart
                </Button>
                <Button
                  variant="contained"
                  onClick={handleCheckout}
                  sx={{ flexGrow: 1 }}
                >
                  Proceed to Checkout
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
  );
};

export default CartPage;
