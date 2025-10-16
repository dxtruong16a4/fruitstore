import React from 'react';
import { Container, Typography, Box, Card, CardContent, Chip, Button } from '@mui/material';

const OrdersPage: React.FC = () => {
  // Mock orders data
  const orders = [
    {
      id: 1,
      orderNumber: 'ORD-001',
      date: '2024-01-15',
      status: 'DELIVERED',
      total: 45.99,
      items: ['Fresh Apples', 'Sweet Bananas', 'Juicy Oranges']
    },
    {
      id: 2,
      orderNumber: 'ORD-002',
      date: '2024-01-20',
      status: 'CONFIRMED',
      total: 32.50,
      items: ['Ripe Strawberries', 'Fresh Grapes']
    },
    {
      id: 3,
      orderNumber: 'ORD-003',
      date: '2024-01-25',
      status: 'PENDING',
      total: 28.75,
      items: ['Sweet Pineapple', 'Fresh Apples']
    }
  ];

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'DELIVERED': return 'success';
      case 'CONFIRMED': return 'info';
      case 'PENDING': return 'warning';
      case 'CANCELLED': return 'error';
      default: return 'default';
    }
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        📦 My Orders
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        Track your orders and view order history
      </Typography>

      {orders.length === 0 ? (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="h6" gutterBottom>
              No orders found
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              Start shopping to see your orders here
            </Typography>
          </CardContent>
        </Card>
      ) : (
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {orders.map((order) => (
            <Card key={order.id}>
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                  <Box>
                    <Typography variant="h6" gutterBottom>
                      Order #{order.orderNumber}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Date: {order.date}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 1 }}>
                    <Chip 
                      label={order.status} 
                      color={getStatusColor(order.status) as any}
                      size="small"
                    />
                    <Typography variant="h6" color="primary">
                      ${order.total.toFixed(2)}
                    </Typography>
                  </Box>
                </Box>
                
                <Typography variant="body2" gutterBottom>
                  Items: {order.items.join(', ')}
                </Typography>
                
                <Box sx={{ display: 'flex', gap: 1, mt: 2 }}>
                  <Button size="small" variant="outlined">
                    View Details
                  </Button>
                  {order.status === 'PENDING' && (
                    <Button size="small" color="error" variant="outlined">
                      Cancel Order
                    </Button>
                  )}
                </Box>
              </CardContent>
            </Card>
          ))}
        </Box>
      )}
    </Container>
  );
};

export default OrdersPage;
