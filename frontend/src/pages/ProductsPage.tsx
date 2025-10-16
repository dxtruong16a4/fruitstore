import React from 'react';
import { Container, Typography, Box, Card, CardContent, Button, Grid } from '@mui/material';

const ProductsPage: React.FC = () => {
  // Mock products data
  const products = [
    { id: 1, name: 'Fresh Apples', price: 2.99, category: 'Fruits' },
    { id: 2, name: 'Sweet Bananas', price: 1.99, category: 'Fruits' },
    { id: 3, name: 'Juicy Oranges', price: 3.49, category: 'Fruits' },
    { id: 4, name: 'Ripe Strawberries', price: 4.99, category: 'Berries' },
    { id: 5, name: 'Fresh Grapes', price: 3.99, category: 'Fruits' },
    { id: 6, name: 'Sweet Pineapple', price: 5.99, category: 'Tropical' },
  ];

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        🍎 Our Products
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        Fresh fruits carefully selected for quality and taste
      </Typography>

      <Grid container spacing={3}>
        {products.map((product) => (
          <Grid item xs={12} sm={6} md={4} key={product.id}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>
                  {product.name}
                </Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {product.category}
                </Typography>
                <Typography variant="h5" color="primary" gutterBottom>
                  ${product.price}
                </Typography>
                <Button 
                  variant="contained" 
                  fullWidth
                  sx={{ mt: 2 }}
                >
                  Add to Cart
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default ProductsPage;
