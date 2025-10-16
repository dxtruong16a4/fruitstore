import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Pagination,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
  Alert,
  Snackbar,
} from '@mui/material';
import { useProductStore } from '../stores/productStore';
import ProductGrid from '../components/product/ProductGrid';
import ProductFilters from '../components/product/ProductFilters';

const ProductListPage: React.FC = () => {
  const {
    products,
    categories,
    filters,
    pagination,
    isLoading,
    error,
    fetchProducts,
    fetchCategories,
    setFilters,
    clearFilters,
    setPage,
    setPageSize,
    clearError,
  } = useProductStore();

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, [fetchProducts, fetchCategories]);

  const handleFiltersChange = (newFilters: any) => {
    setFilters(newFilters);
  };

  const handleClearFilters = () => {
    clearFilters();
  };

  const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
    setPage(page - 1); // MUI Pagination is 1-based, our API is 0-based
  };

  const handlePageSizeChange = (event: any) => {
    setPageSize(parseInt(event.target.value));
  };

  const handleAddToCart = (productId: number) => {
    setSnackbarMessage('Đã thêm sản phẩm vào giỏ hàng!');
    setSnackbarOpen(true);
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const getPageSizeOptions = () => {
    return [
      { value: 12, label: '12 sản phẩm/trang' },
      { value: 24, label: '24 sản phẩm/trang' },
      { value: 36, label: '36 sản phẩm/trang' },
      { value: 48, label: '48 sản phẩm/trang' },
    ];
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      <Box mb={4}>
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          Sản phẩm
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Khám phá các loại trái cây tươi ngon và chất lượng cao
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Filters Sidebar */}
        <Grid size={{ xs: 12, md: 3 }}>
          <ProductFilters
            filters={filters}
            categories={categories}
            onFiltersChange={handleFiltersChange}
            onClearFilters={handleClearFilters}
            isLoading={isLoading}
          />
        </Grid>

        {/* Products Grid */}
        <Grid size={{ xs: 12, md: 9 }}>
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
            <Typography variant="h6" color="text.secondary">
              {pagination.totalElements > 0 ? (
                <>
                  Hiển thị {products.length} trong tổng số {pagination.totalElements} sản phẩm
                </>
              ) : (
                'Không có sản phẩm nào'
              )}
            </Typography>

            <FormControl size="small" sx={{ minWidth: 180 }}>
              <InputLabel>Số sản phẩm/trang</InputLabel>
              <Select
                value={pagination.size}
                onChange={handlePageSizeChange}
                label="Số sản phẩm/trang"
                disabled={isLoading}
              >
                {getPageSizeOptions().map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>

          <ProductGrid
            products={products}
            isLoading={isLoading}
            error={error}
            onAddToCart={handleAddToCart}
            emptyMessage="Không tìm thấy sản phẩm nào phù hợp với bộ lọc của bạn"
          />

          {/* Pagination */}
          {pagination.totalPages > 1 && (
            <Box display="flex" justifyContent="center" mt={4}>
              <Pagination
                count={pagination.totalPages}
                page={pagination.currentPage + 1} // Convert to 1-based for MUI
                onChange={handlePageChange}
                color="primary"
                size="large"
                disabled={isLoading}
                showFirstButton
                showLastButton
              />
            </Box>
          )}
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

      {/* Error Snackbar */}
      <Snackbar
        open={!!error}
        autoHideDuration={6000}
        onClose={clearError}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert onClose={clearError} severity="error" sx={{ width: '100%' }}>
          {error}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default ProductListPage;
