import React, { useState } from 'react';
import { Container, Typography, Box, Card, CardContent, TextField, Button, Alert } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../redux';
import { loginStart, loginSuccess, loginFailure } from '../redux/slices/authSlice';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const { isLoading, error } = useAppSelector((state) => state.auth);

  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const from = (location.state as any)?.from?.pathname || '/';

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.username || !formData.password) {
      dispatch(loginFailure('Please fill in all fields'));
      return;
    }

    dispatch(loginStart());
    
    // Simulate API call
    setTimeout(() => {
      if (formData.username === 'admin' && formData.password === 'admin') {
        dispatch(loginSuccess({
          user: {
            id: 1,
            username: 'admin',
            email: 'admin@fruitstore.com',
            role: 'ADMIN',
            firstName: 'Admin',
            lastName: 'User',
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
          },
          token: 'admin_token_123',
        }));
        navigate(from, { replace: true });
      } else if (formData.username === 'customer' && formData.password === 'customer') {
        dispatch(loginSuccess({
          user: {
            id: 2,
            username: 'customer',
            email: 'customer@fruitstore.com',
            role: 'CUSTOMER',
            firstName: 'Customer',
            lastName: 'User',
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
          },
          token: 'customer_token_123',
        }));
        navigate(from, { replace: true });
      } else {
        dispatch(loginFailure('Invalid username or password'));
      }
    }, 1000);
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 8, mb: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ textAlign: 'center', mb: 3 }}>
            <Typography variant="h4" component="h1" gutterBottom>
              🍎 Login
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Sign in to your FruitStore account
            </Typography>
          </Box>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              margin="normal"
              required
              disabled={isLoading}
            />
            <TextField
              fullWidth
              label="Password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              margin="normal"
              required
              disabled={isLoading}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={isLoading}
            >
              {isLoading ? 'Signing in...' : 'Sign In'}
            </Button>
          </Box>

          <Box sx={{ textAlign: 'center', mt: 2 }}>
            <Typography variant="body2" color="text.secondary">
              Demo accounts:
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Admin: admin / admin
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Customer: customer / customer
            </Typography>
          </Box>
        </CardContent>
      </Card>
    </Container>
  );
};

export default LoginPage;
