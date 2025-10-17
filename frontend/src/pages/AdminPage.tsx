import React from 'react';
import { Container, Typography, Box, Card, CardContent, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const AdminPage: React.FC = () => {
  const navigate = useNavigate();

  const adminFeatures = [
    {
      title: 'Manage Products',
      description: 'Add, edit, and manage product catalog',
      icon: '📦',
      path: '/admin/products'
    },
    {
      title: 'Manage Orders',
      description: 'View and process customer orders',
      icon: '📋',
      path: '/admin/orders'
    },
    {
      title: 'Manage Users',
      description: 'View and manage user accounts',
      icon: '👥',
      path: '/admin/users'
    },
    {
      title: 'Manage Categories',
      description: 'Organize products by categories',
      icon: '🏷️',
      path: '/admin/categories'
    },
    {
      title: 'Manage Discounts',
      description: 'Create and manage discount codes',
      icon: '💰',
      path: '/admin/discounts'
    },
    {
      title: 'Analytics',
      description: 'View sales and performance analytics',
      icon: '📊',
      path: '/admin/analytics'
    }
  ];

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        🔧 Admin Dashboard
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        Manage your FruitStore from the admin panel
      </Typography>

      <Box 
        sx={{ 
          display: 'grid', 
          gridTemplateColumns: { 
            xs: '1fr', 
            sm: 'repeat(2, 1fr)', 
            md: 'repeat(3, 1fr)' 
          }, 
          gap: 3 
        }}
      >
        {adminFeatures.map((feature, index) => (
          <Card key={index} sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardContent sx={{ flexGrow: 1 }}>
              <Box sx={{ textAlign: 'center', mb: 2 }}>
                <Typography variant="h2" component="div">
                  {feature.icon}
                </Typography>
              </Box>
              <Typography variant="h6" gutterBottom>
                {feature.title}
              </Typography>
              <Typography variant="body2" color="text.secondary" paragraph>
                {feature.description}
              </Typography>
              <Button
                variant="contained"
                fullWidth
                onClick={() => navigate(feature.path)}
              >
                Manage
              </Button>
            </CardContent>
          </Card>
        ))}
      </Box>

      <Card sx={{ mt: 4 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            🎯 Phase 1.5 Admin Features
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Admin dashboard with protected routes and role-based access control.
            All admin features are accessible only to authenticated admin users.
          </Typography>
        </CardContent>
      </Card>
    </Container>
  );
};

export default AdminPage;
