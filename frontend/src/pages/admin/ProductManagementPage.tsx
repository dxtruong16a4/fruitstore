import React, { useEffect, useState } from 'react';
import {
  Container,
  Card,
  CardContent,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  TablePagination,
  IconButton,
  Chip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { Inventory, Edit, Delete, Add, Warning } from '@mui/icons-material';
import { adminApi, type ProductResponse, type CategoryResponse } from '../../api/adminApi';

const ProductManagementPage: React.FC = () => {
  const [products, setProducts] = useState<ProductResponse[]>([]);
  const [categories, setCategories] = useState<CategoryResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingProduct, setEditingProduct] = useState<ProductResponse | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: 0,
    stockQuantity: 0,
    imageUrl: '',
    categoryId: 0,
  });
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<number | null>(null);

  useEffect(() => {
    fetchData();
  }, [page, rowsPerPage]);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching products and categories...');

      const [productsResult, categoriesResult] = await Promise.all([
        adminApi.getAllProducts(page, rowsPerPage),
        adminApi.getAllCategories(),
      ]);

      console.log('Products API response:', productsResult);
      console.log('Categories API response:', categoriesResult);

      if (productsResult.success && productsResult.data) {
        console.log('Products data:', productsResult.data);
        setProducts(productsResult.data.content || []);
        setTotalElements(productsResult.data.totalElements || 0);
      } else {
        console.error('Products API error:', productsResult.message);
      }

      if (categoriesResult.success && categoriesResult.data) {
        console.log('Categories data:', categoriesResult.data);
        setCategories(categoriesResult.data);
      } else {
        console.error('Categories API error:', categoriesResult.message);
      }
    } catch (err) {
      const errorMsg = 'An error occurred while loading data';
      console.error(errorMsg, err);
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleChangePage = (_event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleOpenDialog = (product?: ProductResponse) => {
    if (product) {
      setEditingProduct(product);
      setFormData({
        name: product.name,
        description: product.description || '',
        price: product.price,
        stockQuantity: product.stockQuantity,
        imageUrl: product.imageUrl || '',
        categoryId: product.category?.categoryId || 0,
      });
    } else {
      setEditingProduct(null);
      setFormData({
        name: '',
        description: '',
        price: 0,
        stockQuantity: 0,
        imageUrl: '',
        categoryId: 0,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingProduct(null);
  };

  const handleSave = async () => {
    if (!formData.name.trim() || formData.price <= 0 || formData.categoryId === 0) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      setSaving(true);
      let result;

      if (editingProduct) {
        result = await adminApi.updateProduct(editingProduct.productId, formData);
      } else {
        result = await adminApi.createProduct(formData);
      }

      if (result.success) {
        await fetchData();
        handleCloseDialog();
      } else {
        setError(result.message || 'Failed to save product');
      }
    } catch (err) {
      setError('An error occurred while saving product');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (productId: number) => {
    try {
      setSaving(true);
      const result = await adminApi.deleteProduct(productId);

      if (result.success) {
        await fetchData();
        setDeleteConfirm(null);
      } else {
        setError(result.message || 'Failed to delete product');
      }
    } catch (err) {
      setError('An error occurred while deleting product');
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  if (loading && products.length === 0) {
    return (
      <Container maxWidth="lg" sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1, display: 'flex', alignItems: 'center', gap: 1 }}>
            <Inventory sx={{ fontSize: 32 }} />
            Product Management
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Manage product catalog
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => handleOpenDialog()}
        >
          Add Product
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
          {error}
        </Alert>
      )}

      <Card>
        <CardContent>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                  <TableCell sx={{ fontWeight: 'bold' }}>Name</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Category</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="right">Price</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="right">Stock</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }} align="center">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.map((product) => (
                  <TableRow key={product.productId} hover>
                    <TableCell sx={{ fontWeight: 500 }}>{product.name}</TableCell>
                    <TableCell>{product.category?.name || '-'}</TableCell>
                    <TableCell align="right">₫{product.price.toLocaleString()}</TableCell>
                    <TableCell align="right">
                      {product.stockQuantity < 10 ? (
                        <Chip
                          icon={<Warning />}
                          label={product.stockQuantity}
                          color="warning"
                          size="small"
                        />
                      ) : (
                        product.stockQuantity
                      )}
                    </TableCell>
                    <TableCell align="center">
                      <IconButton
                        size="small"
                        onClick={() => handleOpenDialog(product)}
                        title="Edit"
                      >
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => setDeleteConfirm(product.productId)}
                        disabled={saving}
                        title="Delete"
                      >
                        <Delete fontSize="small" color="error" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25, 50]}
            component="div"
            count={totalElements}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </CardContent>
      </Card>

      {/* Create/Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingProduct ? 'Edit Product' : 'Add New Product'}
        </DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Product Name"
              fullWidth
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="Enter product name"
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={2}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Enter product description"
            />
            <TextField
              label="Price"
              fullWidth
              type="number"
              value={formData.price}
              onChange={(e) => setFormData({ ...formData, price: parseFloat(e.target.value) })}
              placeholder="Enter price"
            />
            <TextField
              label="Stock Quantity"
              fullWidth
              type="number"
              value={formData.stockQuantity}
              onChange={(e) => setFormData({ ...formData, stockQuantity: parseInt(e.target.value) })}
              placeholder="Enter stock quantity"
            />
            <TextField
              label="Image URL"
              fullWidth
              value={formData.imageUrl}
              onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
              placeholder="Enter image URL"
            />
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={formData.categoryId}
                label="Category"
                onChange={(e) => setFormData({ ...formData, categoryId: e.target.value as number })}
              >
                <MenuItem value={0}>Select a category</MenuItem>
                {categories.map((cat) => (
                  <MenuItem key={cat.categoryId} value={cat.categoryId}>
                    {cat.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button 
            onClick={handleSave} 
            variant="contained" 
            disabled={saving || !formData.name.trim() || formData.price <= 0 || formData.categoryId === 0}
          >
            {saving ? 'Saving...' : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog open={deleteConfirm !== null} onClose={() => setDeleteConfirm(null)}>
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this product? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirm(null)}>Cancel</Button>
          <Button 
            onClick={() => deleteConfirm !== null && handleDelete(deleteConfirm)}
            variant="contained"
            color="error"
            disabled={saving}
          >
            {saving ? 'Deleting...' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default ProductManagementPage;

