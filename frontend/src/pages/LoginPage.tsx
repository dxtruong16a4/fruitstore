import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { Eye, EyeOff, Lock, Mail, Loader2, AlertCircle, Home } from 'lucide-react';
import { useAppDispatch, useAppSelector } from '../redux';
import { loginStart, loginSuccess, loginFailure } from '../redux/slices/authSlice';
import { authApi } from '../api/authApi';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const { isLoading, error } = useAppSelector((state) => state.auth);

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [showPassword, setShowPassword] = useState(false);

  const from = (location.state as any)?.from?.pathname || '/';

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (!formData.email || !formData.password) {
      dispatch(loginFailure('Vui lòng điền đầy đủ thông tin'));
      return;
    }

    dispatch(loginStart());
    
    try {
      const response = await authApi.login({
        email: formData.email,
        password: formData.password
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
        navigate(from, { replace: true });
      } else {
        dispatch(loginFailure(response.message || 'Đăng nhập thất bại'));
      }
    } catch (error: any) {
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
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Chào mừng trở lại!</h1>
          <p className="text-gray-600">Đăng nhập vào tài khoản FruitStore của bạn</p>
          
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

        {/* Login Form */}
        <div className="bg-white rounded-3xl shadow-xl p-8">
          {error && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-2xl flex items-center space-x-3">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0" />
              <p className="text-red-700 text-sm">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6" onInvalid={(e) => e.preventDefault()}>
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

            {/* Remember Me & Forgot Password */}
            <div className="flex items-center justify-between">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  className="w-4 h-4 text-green-600 border-gray-300 rounded focus:ring-green-500"
                />
                <span className="ml-2 text-sm text-gray-600">Ghi nhớ đăng nhập</span>
              </label>
              <Link
                to="/forgot-password"
                className="text-sm text-green-600 hover:text-green-700 font-medium transition"
              >
                Quên mật khẩu?
              </Link>
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
                  <span>Đang đăng nhập...</span>
                </>
              ) : (
                <span>Đăng nhập</span>
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

          {/* Register Link */}
          <div className="text-center">
            <p className="text-gray-600">
              Chưa có tài khoản?{' '}
              <Link
                to="/register"
                className="text-green-600 hover:text-green-700 font-semibold transition"
              >
                Đăng ký ngay
              </Link>
            </p>
          </div>
        </div>

      </div>
    </div>
  );
};

export default LoginPage;
