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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Chip,
  Alert,
  CircularProgress,
  Snackbar,
  Grid,
} from '@mui/material';
import {
  Add,
  Edit,
  Delete,
  Search,
  Refresh,
  ToggleOn,
  ToggleOff,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useDiscountStore } from '../stores/discountStore';

const schema = yup.object({
  code: yup.string().required('Mã giảm giá là bắt buộc'),
  description: yup.string().required('Mô tả là bắt buộc'),
  type: yup.string().oneOf(['PERCENTAGE', 'FIXED_AMOUNT']).required('Loại giảm giá là bắt buộc'),
  value: yup.number().required('Giá trị giảm giá là bắt buộc').min(0, 'Giá trị phải lớn hơn 0'),
  minOrderAmount: yup.number().min(0, 'Giá trị tối thiểu phải lớn hơn hoặc bằng 0').optional(),
  maxDiscountAmount: yup.number().min(0, 'Giá trị tối đa phải lớn hơn hoặc bằng 0').optional(),
  usageLimit: yup.number().min(1, 'Giới hạn sử dụng phải lớn hơn 0').optional(),
  validFrom: yup.string().required('Ngày bắt đầu là bắt buộc'),
  validUntil: yup.string().required('Ngày kết thúc là bắt buộc'),
});

type DiscountFormData = yup.InferType<typeof schema>;

const AdminDiscountManagement: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const {
    discounts,
    isLoading,
    error,
    fetchDiscounts,
    createDiscount,
    updateDiscount,
    deleteDiscount,
    toggleDiscountStatus,
  } = useDiscountStore();

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingDiscount, setEditingDiscount] = useState<any>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    setValue,
    watch,
  } = useForm({
    resolver: yupResolver(schema),
  });

  const discountType = watch('type');

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/');
      return;
    }

    fetchDiscounts();
  }, [isAuthenticated, navigate, fetchDiscounts]);

  const handleOpenDialog = (discount?: any) => {
    if (discount) {
      setEditingDiscount(discount);
      setValue('code', discount.code);
      setValue('description', discount.description);
      setValue('type', discount.type);
      setValue('value', discount.value);
      setValue('minOrderAmount', discount.minOrderAmount || 0);
      setValue('maxDiscountAmount', discount.maxDiscountAmount || 0);
      setValue('usageLimit', discount.usageLimit || 0);
      setValue('validFrom', discount.validFrom.split('T')[0]);
      setValue('validUntil', discount.validUntil.split('T')[0]);
    } else {
      setEditingDiscount(null);
      reset();
    }
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setEditingDiscount(null);
    reset();
  };

  const onSubmit = async (data: DiscountFormData) => {
    try {
      const discountData = {
        ...data,
        minOrderAmount: data.minOrderAmount || undefined,
        maxDiscountAmount: data.maxDiscountAmount || undefined,
        usageLimit: data.usageLimit || undefined,
      };

      if (editingDiscount) {
        await updateDiscount(editingDiscount.id, discountData);
        setSnackbarMessage('Cập nhật mã giảm giá thành công');
      } else {
        await createDiscount(discountData);
        setSnackbarMessage('Tạo mã giảm giá thành công');
      }
      setSnackbarOpen(true);
      handleCloseDialog();
      fetchDiscounts();
    } catch (error: any) {
      setSnackbarMessage(error.response?.data?.message || 'Có lỗi xảy ra');
      setSnackbarOpen(true);
    }
  };

  const handleDeleteDiscount = async (discountId: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa mã giảm giá này?')) {
      try {
        await deleteDiscount(discountId);
        setSnackbarMessage('Xóa mã giảm giá thành công');
        setSnackbarOpen(true);
        fetchDiscounts();
      } catch (error: any) {
        setSnackbarMessage(error.response?.data?.message || 'Không thể xóa mã giảm giá');
        setSnackbarOpen(true);
      }
    }
  };

  const handleToggleStatus = async (discountId: number) => {
    try {
      await toggleDiscountStatus(discountId);
      setSnackbarMessage('Cập nhật trạng thái thành công');
      setSnackbarOpen(true);
      fetchDiscounts();
    } catch (error: any) {
      setSnackbarMessage(error.response?.data?.message || 'Không thể cập nhật trạng thái');
      setSnackbarOpen(true);
    }
  };

  const handleSearch = () => {
    fetchDiscounts({ code: searchTerm, page: 0 });
  };

  const handleStatusFilter = (status: string) => {
    setStatusFilter(status);
    fetchDiscounts({ isActive: status === 'active' ? true : status === 'inactive' ? false : undefined, page: 0 });
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN');
  };

  const getTypeLabel = (type: string) => {
    return type === 'PERCENTAGE' ? 'Phần trăm' : 'Số tiền cố định';
  };

  const getValueDisplay = (type: string, value: number) => {
    return type === 'PERCENTAGE' ? `${value}%` : formatPrice(value);
  };

  const isExpired = (validUntil: string) => {
    return new Date(validUntil) < new Date();
  };

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

  if (isLoading && discounts.length === 0) {
    return (
      <Container maxWidth="xl" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <Box textAlign="center">
            <CircularProgress size={60} />
            <Typography variant="h6" sx={{ mt: 2 }}>
              Đang tải danh sách mã giảm giá...
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
        <Button onClick={() => fetchDiscounts()} variant="outlined">
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
          Quản lý mã giảm giá
        </Typography>
        <Box display="flex" gap={2}>
          <Button
            variant="outlined"
            startIcon={<Refresh />}
            onClick={() => fetchDiscounts()}
            disabled={isLoading}
          >
            Làm mới
          </Button>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => handleOpenDialog()}
          >
            Thêm mã giảm giá
          </Button>
        </Box>
      </Box>

      {/* Filters */}
      <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid size={{ xs: 12, md: 4 }}>
            <TextField
              fullWidth
              label="Tìm kiếm mã giảm giá"
              placeholder="Nhập mã giảm giá..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              InputProps={{
                endAdornment: (
                  <IconButton onClick={handleSearch} edge="end">
                    <Search />
                  </IconButton>
                ),
              }}
            />
          </Grid>
          
          <Grid size={{ xs: 12, md: 3 }}>
            <FormControl fullWidth>
              <InputLabel>Trạng thái</InputLabel>
              <Select
                value={statusFilter}
                onChange={(e) => handleStatusFilter(e.target.value)}
                label="Trạng thái"
              >
                <MenuItem value="">Tất cả trạng thái</MenuItem>
                <MenuItem value="active">Đang hoạt động</MenuItem>
                <MenuItem value="inactive">Không hoạt động</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
      </Paper>

      {/* Discounts Table */}
      {discounts.length === 0 ? (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary" gutterBottom>
            Chưa có mã giảm giá nào
          </Typography>
          <Typography variant="body2" color="text.secondary" paragraph>
            Hãy tạo mã giảm giá đầu tiên
          </Typography>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => handleOpenDialog()}
          >
            Thêm mã giảm giá
          </Button>
        </Box>
      ) : (
        <TableContainer component={Paper} elevation={2}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Mã giảm giá</TableCell>
                <TableCell>Mô tả</TableCell>
                <TableCell>Loại</TableCell>
                <TableCell>Giá trị</TableCell>
                <TableCell>Đơn tối thiểu</TableCell>
                <TableCell>Giới hạn sử dụng</TableCell>
                <TableCell>Ngày hết hạn</TableCell>
                <TableCell align="center">Trạng thái</TableCell>
                <TableCell align="center">Thao tác</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {discounts.map((discount) => (
                <TableRow key={discount.id} hover>
                  <TableCell>
                    <Typography variant="body1" fontWeight="bold">
                      {discount.code}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2" noWrap>
                      {discount.description}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={getTypeLabel(discount.type)}
                      size="small"
                      color={discount.type === 'PERCENTAGE' ? 'primary' : 'secondary'}
                    />
                  </TableCell>
                  <TableCell>
                    <Typography variant="body1" fontWeight="bold">
                      {getValueDisplay(discount.type, discount.value)}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">
                      {discount.minOrderAmount ? formatPrice(discount.minOrderAmount) : 'Không giới hạn'}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">
                      {discount.usageLimit ? `${discount.usedCount}/${discount.usageLimit}` : 'Không giới hạn'}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography
                      variant="body2"
                      color={isExpired(discount.validUntil) ? 'error' : 'text.primary'}
                    >
                      {formatDate(discount.validUntil)}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Chip
                      label={discount.isActive ? 'Hoạt động' : 'Không hoạt động'}
                      color={discount.isActive ? 'success' : 'error'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <Box display="flex" gap={1} justifyContent="center">
                      <IconButton
                        size="small"
                        onClick={() => handleToggleStatus(discount.id)}
                        color={discount.isActive ? 'error' : 'success'}
                      >
                        {discount.isActive ? <ToggleOff /> : <ToggleOn />}
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleOpenDialog(discount)}
                        color="primary"
                      >
                        <Edit />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleDeleteDiscount(discount.id)}
                        color="error"
                      >
                        <Delete />
                      </IconButton>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* Discount Form Dialog */}
      <Dialog open={dialogOpen} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {editingDiscount ? 'Chỉnh sửa mã giảm giá' : 'Thêm mã giảm giá mới'}
        </DialogTitle>
        <form onSubmit={handleSubmit(onSubmit)}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('code')}
                  fullWidth
                  label="Mã giảm giá"
                  error={!!errors.code}
                  helperText={errors.code?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <FormControl fullWidth error={!!errors.type}>
                  <InputLabel>Loại giảm giá</InputLabel>
                  <Select
                    {...register('type')}
                    label="Loại giảm giá"
                  >
                    <MenuItem value="PERCENTAGE">Phần trăm</MenuItem>
                    <MenuItem value="FIXED_AMOUNT">Số tiền cố định</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid size={{ xs: 12 }}>
                <TextField
                  {...register('description')}
                  fullWidth
                  label="Mô tả"
                  multiline
                  rows={2}
                  error={!!errors.description}
                  helperText={errors.description?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('value')}
                  fullWidth
                  label={discountType === 'PERCENTAGE' ? 'Phần trăm (%)' : 'Số tiền (VNĐ)'}
                  type="number"
                  error={!!errors.value}
                  helperText={errors.value?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('minOrderAmount')}
                  fullWidth
                  label="Đơn hàng tối thiểu (VNĐ)"
                  type="number"
                  error={!!errors.minOrderAmount}
                  helperText={errors.minOrderAmount?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('maxDiscountAmount')}
                  fullWidth
                  label="Giảm giá tối đa (VNĐ)"
                  type="number"
                  error={!!errors.maxDiscountAmount}
                  helperText={errors.maxDiscountAmount?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('usageLimit')}
                  fullWidth
                  label="Giới hạn sử dụng"
                  type="number"
                  error={!!errors.usageLimit}
                  helperText={errors.usageLimit?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('validFrom')}
                  fullWidth
                  label="Ngày bắt đầu"
                  type="date"
                  InputLabelProps={{ shrink: true }}
                  error={!!errors.validFrom}
                  helperText={errors.validFrom?.message}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  {...register('validUntil')}
                  fullWidth
                  label="Ngày kết thúc"
                  type="date"
                  InputLabelProps={{ shrink: true }}
                  error={!!errors.validUntil}
                  helperText={errors.validUntil?.message}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>
              Hủy
            </Button>
            <Button type="submit" variant="contained">
              {editingDiscount ? 'Cập nhật' : 'Tạo mới'}
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

export default AdminDiscountManagement;
