import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress,
  InputAdornment,
  IconButton,
  Divider,
  Chip,
} from '@mui/material';
import {
  Visibility,
  VisibilityOff,
  Person,
  Email,
  Badge,
  Edit,
  Save,
  Cancel,
} from '@mui/icons-material';
import { useAuthStore } from '../stores/authStore';

const schema = yup.object({
  fullName: yup
    .string()
    .required('Họ tên là bắt buộc')
    .min(2, 'Họ tên phải có ít nhất 2 ký tự')
    .max(50, 'Họ tên không được quá 50 ký tự'),
  email: yup
    .string()
    .required('Email là bắt buộc')
    .email('Email không hợp lệ'),
  currentPassword: yup.string().optional(),
  newPassword: yup
    .string()
    .optional()
    .test('password-strength', 'Mật khẩu phải chứa ít nhất 1 chữ thường, 1 chữ hoa và 1 số', function(value) {
      if (!value || value.length === 0) return true; // Optional field
      return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(value);
    })
    .test('password-length', 'Mật khẩu phải có ít nhất 6 ký tự', function(value) {
      if (!value || value.length === 0) return true; // Optional field
      return value.length >= 6;
    }),
  confirmPassword: yup
    .string()
    .optional()
    .test('passwords-match', 'Mật khẩu xác nhận không khớp', function(value) {
      const newPassword = this.parent.newPassword;
      if (!value || !newPassword) return true; // Both optional
      return value === newPassword;
    }),
});

type ProfileFormData = yup.InferType<typeof schema>;

const ProfilePage: React.FC = () => {
  const { user, logout } = useAuthStore();
  const [isEditing, setIsEditing] = useState(false);
  const [showPasswords, setShowPasswords] = useState({
    current: false,
    new: false,
    confirm: false,
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ProfileFormData>({
    resolver: yupResolver(schema) as any,
    defaultValues: {
      fullName: user?.fullName || '',
      email: user?.email || '',
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
  });


  const togglePasswordVisibility = (field: 'current' | 'new' | 'confirm') => {
    setShowPasswords(prev => ({
      ...prev,
      [field]: !prev[field],
    }));
  };

  const handleEdit = () => {
    setIsEditing(true);
    setError(null);
    setSuccess(null);
  };

  const handleCancel = () => {
    setIsEditing(false);
    reset();
    setError(null);
    setSuccess(null);
  };

  const onSubmit = async (data: ProfileFormData) => {
    setIsLoading(true);
    setError(null);
    setSuccess(null);

    try {
      // Validate password requirements
      if (data.newPassword && !data.currentPassword) {
        setError('Vui lòng nhập mật khẩu hiện tại để thay đổi mật khẩu');
        setIsLoading(false);
        return;
      }

      if (data.newPassword && data.newPassword !== data.confirmPassword) {
        setError('Mật khẩu xác nhận không khớp');
        setIsLoading(false);
        return;
      }

      // Simulate API call - replace with actual update profile service
      console.log('Profile update data:', data);
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccess('Cập nhật thông tin thành công!');
      setIsEditing(false);
    } catch (error: any) {
      setError(error.message || 'Có lỗi xảy ra khi cập nhật thông tin');
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
  };

  if (!user) {
    return (
      <Container maxWidth="sm">
        <Box textAlign="center" py={4}>
          <Typography variant="h6" color="error">
            Không tìm thấy thông tin người dùng
          </Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box py={4}>
        <Typography variant="h4" gutterBottom>
          Hồ sơ cá nhân
        </Typography>

        <Paper elevation={3} sx={{ p: 4 }}>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" sx={{ mb: 2 }}>
              {success}
            </Alert>
          )}

          <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
            <Typography variant="h6">
              Thông tin tài khoản
            </Typography>
            {!isEditing && (
              <Button
                variant="outlined"
                startIcon={<Edit />}
                onClick={handleEdit}
              >
                Chỉnh sửa
              </Button>
            )}
          </Box>

          <Box component="form" onSubmit={handleSubmit(onSubmit as any)}>
            <Box display="flex" alignItems="center" mb={3}>
              <Typography variant="body1" sx={{ minWidth: 120 }}>
                Tên đăng nhập:
              </Typography>
              <Typography variant="body1" fontWeight="bold">
                {user.username}
              </Typography>
            </Box>

            <Box display="flex" alignItems="center" mb={3}>
              <Typography variant="body1" sx={{ minWidth: 120 }}>
                Vai trò:
              </Typography>
              <Chip
                label={user.role === 'ADMIN' ? 'Quản trị viên' : 'Khách hàng'}
                color={user.role === 'ADMIN' ? 'primary' : 'default'}
                size="small"
              />
            </Box>

            <Divider sx={{ my: 3 }} />

            <TextField
              {...register('fullName')}
              fullWidth
              label="Họ và tên"
              margin="normal"
              disabled={!isEditing}
              error={!!errors.fullName}
              helperText={errors.fullName?.message}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Badge color="action" />
                  </InputAdornment>
                ),
              }}
            />

            <TextField
              {...register('email')}
              fullWidth
              label="Email"
              margin="normal"
              disabled={!isEditing}
              error={!!errors.email}
              helperText={errors.email?.message}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Email color="action" />
                  </InputAdornment>
                ),
              }}
            />

            {isEditing && (
              <>
                <Divider sx={{ my: 3 }} />
                <Typography variant="h6" gutterBottom>
                  Thay đổi mật khẩu (tùy chọn)
                </Typography>

                <TextField
                  {...register('currentPassword')}
                  fullWidth
                  label="Mật khẩu hiện tại"
                  type={showPasswords.current ? 'text' : 'password'}
                  margin="normal"
                  error={!!errors.currentPassword}
                  helperText={errors.currentPassword?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Person color="action" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => togglePasswordVisibility('current')}
                          edge="end"
                        >
                          {showPasswords.current ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />

                <TextField
                  {...register('newPassword')}
                  fullWidth
                  label="Mật khẩu mới"
                  type={showPasswords.new ? 'text' : 'password'}
                  margin="normal"
                  error={!!errors.newPassword}
                  helperText={errors.newPassword?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Person color="action" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => togglePasswordVisibility('new')}
                          edge="end"
                        >
                          {showPasswords.new ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />

                <TextField
                  {...register('confirmPassword')}
                  fullWidth
                  label="Xác nhận mật khẩu mới"
                  type={showPasswords.confirm ? 'text' : 'password'}
                  margin="normal"
                  error={!!errors.confirmPassword}
                  helperText={errors.confirmPassword?.message}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Person color="action" />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => togglePasswordVisibility('confirm')}
                          edge="end"
                        >
                          {showPasswords.confirm ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />
              </>
            )}

            {isEditing && (
              <Box display="flex" gap={2} mt={3}>
                <Button
                  type="submit"
                  variant="contained"
                  startIcon={isLoading ? <CircularProgress size={20} /> : <Save />}
                  disabled={isLoading}
                >
                  {isLoading ? 'Đang lưu...' : 'Lưu thay đổi'}
                </Button>
                <Button
                  variant="outlined"
                  startIcon={<Cancel />}
                  onClick={handleCancel}
                  disabled={isLoading}
                >
                  Hủy
                </Button>
              </Box>
            )}
          </Box>

          <Divider sx={{ my: 3 }} />

          <Box textAlign="center">
            <Button
              variant="outlined"
              color="error"
              onClick={handleLogout}
            >
              Đăng xuất
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default ProfilePage;
