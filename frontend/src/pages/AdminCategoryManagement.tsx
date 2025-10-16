import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  Container,
  Typography,
  Box,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  IconButton,
  Alert,
  CircularProgress,
  Snackbar,
  Grid,
  Card,
  CardContent,
  CardMedia,
} from '@mui/material';
import {
  Add,
  Edit,
  Delete,
  Search,
  Refresh,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useProductStore } from '../stores/productStore';

const schema = yup.object({
  name: yup.string().required('Tên danh mục là bắt buộc'),
  description: yup.string().optional(),
  imageUrl: yup.string().url('URL hình ảnh không hợp lệ').optional(),
});

type CategoryFormData = yup.InferType<typeof schema>;

const AdminCategoryManagement: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const {
    categories,
    isLoading,
    error,
    fetchCategories,
  } = useProductStore();

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState<any>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    setValue,
  } = useForm({
    resolver: yupResolver(schema),
  });

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/');
      return;
    }

    fetchCategories();
  }, [isAuthenticated, navigate, fetchCategories]);

  const handleOpenDialog = (category?: any) => {
    if (category) {
      setEditingCategory(category);
      setValue('name', category.name);
      setValue('description', category.description || '');
      setValue('imageUrl', category.imageUrl || '');
    } else {
      setEditingCategory(null);
      reset();
    }
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setEditingCategory(null);
    reset();
  };

  const onSubmit = async (data: CategoryFormData) => {
    try {
      // TODO: Implement category creation/update when backend is ready
      if (editingCategory) {
        // await updateCategory(editingCategory.id, data);
        console.log('Update category:', data); // eslint-disable-line no-console
        setSnackbarMessage('Cập nhật danh mục thành công');
      } else {
        // await createCategory(data);
        console.log('Create category:', data); // eslint-disable-line no-console
        setSnackbarMessage('Tạo danh mục thành công');
      }
      setSnackbarOpen(true);
      handleCloseDialog();
      fetchCategories();
    } catch (error: any) {
      setSnackbarMessage(error.response?.data?.message || 'Có lỗi xảy ra');
      setSnackbarOpen(true);
    }
  };

  const handleDeleteCategory = async (categoryId: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa danh mục này?')) {
      try {
        // TODO: Implement category deletion when backend is ready
        // await deleteCategory(categoryId);
        console.log('Delete category:', categoryId); // eslint-disable-line no-console
        setSnackbarMessage('Xóa danh mục thành công');
        setSnackbarOpen(true);
        fetchCategories();
      } catch (error: any) {
        setSnackbarMessage(error.response?.data?.message || 'Không thể xóa danh mục');
        setSnackbarOpen(true);
      }
    }
  };

  const filteredCategories = categories.filter(category =>
    category.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  if (!isAuthenticated) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error">
          Bạn không có quyền truy cập trang quản trị
        </Alert>
      </Container>
    );
  }

  if (isLoading && categories.length === 0) {
    return (
      <Container maxWidth="xl" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <Box textAlign="center">
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Đang tải danh sách danh mục...
            </Typography>
          </Box>
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="xl" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button onClick={fetchCategories} variant="outlined">
          Thử lại
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={4}>
        <Typography variant="h4" component="h1" fontWeight="bold">
          Quản lý danh mục
        </Typography>
        <Box display="flex" gap={2}>
          <Button
            variant="outlined"
            startIcon={<Refresh />}
            onClick={fetchCategories}
            disabled={isLoading}
          >
            Làm mới
          </Button>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => handleOpenDialog()}
          >
            Thêm danh mục
          </Button>
        </Box>
      </Box>

      {/* Search */}
      <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
        <TextField
          fullWidth
          label="Tìm kiếm danh mục"
          placeholder="Nhập tên danh mục..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />,
          }}
        />
      </Paper>

      {/* Categories Grid */}
      {filteredCategories.length === 0 ? (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary" gutterBottom>
            {searchTerm ? 'Không tìm thấy danh mục nào' : 'Chưa có danh mục nào'}
          </Typography>
          <Typography variant="body2" color="text.secondary" paragraph>
            {searchTerm ? 'Hãy thử tìm kiếm với từ khóa khác' : 'Hãy tạo danh mục đầu tiên'}
          </Typography>
          {!searchTerm && (
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => handleOpenDialog()}
            >
              Thêm danh mục
            </Button>
          )}
        </Box>
      ) : (
        <Grid container spacing={3}>
          {filteredCategories.map((category) => (
            <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3 }} key={category.id}>
              <Card elevation={2} sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                <CardMedia
                  component="img"
                  height="200"
                  image={category.imageUrl || '/api/placeholder/300/200'}
                  alt={category.name}
                  sx={{
                    objectFit: 'cover',
                    backgroundColor: '#f5f5f5',
                  }}
                />
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography variant="h6" component="h3" gutterBottom fontWeight="bold">
                    {category.name}
                  </Typography>
                  {category.description && (
                    <Typography
                      variant="body2"
                      color="text.secondary"
                      sx={{
                        display: '-webkit-box',
                        WebkitLineClamp: 2,
                        WebkitBoxOrient: 'vertical',
                        overflow: 'hidden',
                      }}
                    >
                      {category.description}
                    </Typography>
                  )}
                  <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 1 }}>
                    Tạo ngày: {new Date(category.createdAt).toLocaleDateString('vi-VN')}
                  </Typography>
                </CardContent>
                <Box sx={{ p: 2, pt: 0 }}>
                  <Box display="flex" gap={1} justifyContent="flex-end">
                    <IconButton
                      size="small"
                      onClick={() => handleOpenDialog(category)}
                      color="primary"
                    >
                      <Edit />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDeleteCategory(category.id)}
                      color="error"
                    >
                      <Delete />
                    </IconButton>
                  </Box>
                </Box>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Category Form Dialog */}
      <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {editingCategory ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới'}
        </DialogTitle>
        <form onSubmit={handleSubmit(onSubmit)}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid size={{ xs: 12 }}>
                <TextField
                  {...register('name')}
                  fullWidth
                  label="Tên danh mục"
                  error={!!errors.name}
                  helperText={errors.name?.message}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <TextField
                  {...register('description')}
                  fullWidth
                  label="Mô tả"
                  multiline
                  rows={3}
                  error={!!errors.description}
                  helperText={errors.description?.message}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <TextField
                  {...register('imageUrl')}
                  fullWidth
                  label="URL hình ảnh"
                  error={!!errors.imageUrl}
                  helperText={errors.imageUrl?.message}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>
              Hủy
            </Button>
            <Button type="submit" variant="contained">
              {editingCategory ? 'Cập nhật' : 'Tạo mới'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>

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
    </Container>
  );
};

export default AdminCategoryManagement;
