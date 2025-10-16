import React from 'react';
import { Box, Typography, Alert, CircularProgress, Grid } from '@mui/material';
import { type Product } from '../../services/productService';
import ProductCard from './ProductCard';

interface ProductGridProps {
  products: Product[];
  isLoading?: boolean;
  error?: string | null;
  onAddToCart?: (productId: number) => void;
  emptyMessage?: string;
}

const ProductGrid: React.FC<ProductGridProps> = ({
  products,
  isLoading = false,
  error = null,
  onAddToCart,
  emptyMessage = 'Không tìm thấy sản phẩm nào',
}) => {
  if (isLoading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="300px"
      >
        <Box textAlign="center">
          <CircularProgress size={60} />
          <Typography variant="body1" sx={{ mt: 2 }}>
            Đang tải sản phẩm...
          </Typography>
        </Box>
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" my={4}>
        <Alert severity="error" sx={{ maxWidth: 600 }}>
          {error}
        </Alert>
      </Box>
    );
  }

  if (products.length === 0) {
    return (
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="300px"
        textAlign="center"
      >
        <Typography variant="h6" color="text.secondary" gutterBottom>
          {emptyMessage}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Hãy thử thay đổi bộ lọc hoặc tìm kiếm với từ khóa khác
        </Typography>
      </Box>
    );
  }

  return (
    <Grid container spacing={3}>
      {products.map((product) => (
        <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }} key={product.id}>
          <ProductCard
            product={product}
            onAddToCart={onAddToCart}
          />
        </Grid>
      ))}
    </Grid>
  );
};

export default ProductGrid;
