import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Eye, EyeOff, Lock, User, Mail, UserCheck, Loader2, AlertCircle, Home } from 'lucide-react';
import { useAppDispatch, useAppSelector } from '../redux';
import { loginStart, loginSuccess, loginFailure } from '../redux/slices/authSlice';
import { authApi } from '../api/authApi';

const SignUpPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { isLoading, error } = useAppSelector((state) => state.auth);

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    fullName: '',
    phone: '',
    address: '',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const validateForm = () => {
    if (!formData.username || !formData.email || !formData.password || !formData.confirmPassword || !formData.fullName) {
      dispatch(loginFailure('Vui lòng điền đầy đủ thông tin bắt buộc'));
      return false;
    }

    if (formData.password !== formData.confirmPassword) {
      dispatch(loginFailure('Mật khẩu xác nhận không khớp'));
      return false;
    }

    if (formData.password.length < 8) {
      dispatch(loginFailure('Mật khẩu phải có ít nhất 8 ký tự'));
      return false;
    }

    if (formData.username.length < 3) {
      dispatch(loginFailure('Tên đăng nhập phải có ít nhất 3 ký tự'));
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      dispatch(loginFailure('Email không hợp lệ'));
      return false;
    }

    const usernameRegex = /^[a-zA-Z0-9_]+$/;
    if (!usernameRegex.test(formData.username)) {
      dispatch(loginFailure('Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới'));
      return false;
    }

    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    dispatch(loginStart());
    
    try {
      const response = await authApi.register({
        username: formData.username,
        email: formData.email,
        password: formData.password,
        fullName: formData.fullName,
        phone: formData.phone || undefined,
        address: formData.address || undefined
      });

      if (response.success) {
        // Create user object from response data
        const userData = {
          userId: response.data.userId,
          id: response.data.userId, // Keep for compatibility
          username: response.data.username,
          email: response.data.email,
          fullName: response.data.fullName,
          firstName: response.data.fullName.split(' ')[0], // Extract first name
          lastName: response.data.fullName.split(' ').slice(1).join(' '), // Extract last name
          role: response.data.role,
          isActive: true, // Default value
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };

        // Store token and user data
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(userData));
        
        dispatch(loginSuccess({
          token: response.data.token,
          user: userData
        }));
        navigate('/', { replace: true });
      } else {
        dispatch(loginFailure(response.message || 'Đăng ký thất bại'));
      }
    } catch (error: any) {
      console.error('Signup error:', error);
      dispatch(loginFailure(error.message || 'Lỗi kết nối. Vui lòng thử lại sau.'));
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-white to-green-100 flex items-center justify-center px-4 py-12">
      <div className="max-w-md w-full">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-green-600 rounded-2xl mb-4">
            <span className="text-3xl">🍎</span>
          </div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Tạo tài khoản mới</h1>
          <p className="text-gray-600">Đăng ký để trải nghiệm FruitStore</p>
          
          {/* Back to Home Button */}
          <div className="mt-4">
            <Link
              to="/"
              className="inline-flex items-center space-x-2 text-green-600 hover:text-green-700 font-medium transition"
            >
              <Home className="w-4 h-4" />
              <span>Trở về trang chủ</span>
            </Link>
          </div>
        </div>

        {/* Signup Form */}
        <div className="bg-white rounded-3xl shadow-xl p-8">
          {error && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-2xl flex items-center space-x-3">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0" />
              <p className="text-red-700 text-sm">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Full Name Field */}
            <div className="floating-input">
              <UserCheck className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-4 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Họ và tên</label>
            </div>

            {/* Username Field */}
            <div className="floating-input">
              <User className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-4 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Tên đăng nhập</label>
            </div>

            {/* Email Field */}
            <div className="floating-input">
              <Mail className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-4 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Email</label>
            </div>

            {/* Phone Field */}
            <div className="floating-input">
              <UserCheck className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-4 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Số điện thoại (tùy chọn)</label>
            </div>

            {/* Address Field */}
            <div className="floating-input">
              <UserCheck className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type="text"
                name="address"
                value={formData.address}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-4 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Địa chỉ (tùy chọn)</label>
            </div>

            {/* Password Field */}
            <div className="floating-input">
              <Lock className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-12 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Mật khẩu</label>
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition z-10"
              >
                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>

            {/* Confirm Password Field */}
            <div className="floating-input">
              <Lock className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
              <input
                type={showConfirmPassword ? 'text' : 'password'}
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder=" "
                disabled={isLoading}
                className="w-full pl-12 pr-12 py-4 border-2 border-gray-300 rounded-2xl text-gray-900 bg-transparent focus:ring-2 focus:ring-green-500 focus:border-green-500 transition disabled:bg-gray-50 disabled:cursor-not-allowed disabled:border-gray-200"
              />
              <label>Xác nhận mật khẩu</label>
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition z-10"
              >
                {showConfirmPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>

            {/* Terms & Conditions */}
            <div className="flex items-start space-x-3">
              <input
                type="checkbox"
                id="terms"
                className="w-4 h-4 text-green-600 border-gray-300 rounded focus:ring-green-500 mt-1"
                required
              />
              <label htmlFor="terms" className="text-sm text-gray-600">
                Tôi đồng ý với{' '}
                <Link to="/terms" className="text-green-600 hover:text-green-700 font-medium transition">
                  Điều khoản sử dụng
                </Link>{' '}
                và{' '}
                <Link to="/privacy" className="text-green-600 hover:text-green-700 font-medium transition">
                  Chính sách bảo mật
                </Link>
              </label>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-green-600 text-white py-4 rounded-2xl font-semibold text-lg hover:bg-green-700 focus:ring-4 focus:ring-green-200 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center space-x-2"
            >
              {isLoading ? (
                <>
                  <Loader2 className="w-5 h-5 animate-spin" />
                  <span>Đang tạo tài khoản...</span>
                </>
              ) : (
                <span>Tạo tài khoản</span>
              )}
            </button>
          </form>

          {/* Divider */}
          <div className="mt-8 mb-6">
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-200" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-4 bg-white text-gray-500">Hoặc</span>
              </div>
            </div>
          </div>

          {/* Login Link */}
          <div className="text-center">
            <p className="text-gray-600">
              Đã có tài khoản?{' '}
              <Link
                to="/login"
                className="text-green-600 hover:text-green-700 font-semibold transition"
              >
                Đăng nhập ngay
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUpPage;
