import React, { useState } from 'react';
import { Save, Lock, Eye, EyeOff, CheckCircle } from 'lucide-react';

interface ChangePasswordFormProps {
  onSubmit: (data: { oldPassword: string; newPassword: string }) => void;
  isLoading: boolean;
}

const ChangePasswordForm: React.FC<ChangePasswordFormProps> = ({ onSubmit, isLoading }) => {
  const [formData, setFormData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  const [showPasswords, setShowPasswords] = useState({
    current: false,
    new: false,
    confirm: false,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSuccess, setIsSuccess] = useState(false);

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.currentPassword) {
      newErrors.currentPassword = 'Mật khẩu hiện tại là bắt buộc';
    }

    if (!formData.newPassword) {
      newErrors.newPassword = 'Mật khẩu mới là bắt buộc';
    } else if (formData.newPassword.length < 8) {
      newErrors.newPassword = 'Mật khẩu phải có ít nhất 8 ký tự';
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(formData.newPassword)) {
      newErrors.newPassword = 'Mật khẩu phải chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường và một số';
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Vui lòng xác nhận mật khẩu mới';
    } else if (formData.newPassword !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Mật khẩu không khớp';
    }

    if (formData.currentPassword === formData.newPassword) {
      newErrors.newPassword = 'Mật khẩu mới phải khác với mật khẩu hiện tại';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    try {
      await onSubmit({
        oldPassword: formData.currentPassword,
        newPassword: formData.newPassword,
      });

      // Show success state
      setIsSuccess(true);
      
      // Reset form after successful submission
      setFormData({
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
      });

      // Hide success state after 3 seconds
      setTimeout(() => {
        setIsSuccess(false);
      }, 3000);
    } catch (error) {
      // Error handling is done in parent component
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const togglePasswordVisibility = (field: 'current' | 'new' | 'confirm') => {
    setShowPasswords(prev => ({
      ...prev,
      [field]: !prev[field]
    }));
  };

  return (
    <div className="space-y-8">
      {/* Success Message */}
      {isSuccess && (
        <div className="bg-gradient-to-r from-green-50 to-emerald-50 border border-green-200 rounded-lg p-4 animate-in slide-in-from-top-2 duration-300">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
              <CheckCircle className="w-5 h-5 text-green-600" />
            </div>
            <div>
              <h4 className="text-sm font-semibold text-green-800">Password Changed Successfully!</h4>
              <p className="text-sm text-green-700">Your password has been updated successfully.</p>
            </div>
          </div>
        </div>
      )}

      {/* Form Header */}
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-6 border border-blue-100">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
            <Lock className="w-5 h-5 text-blue-600" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900" style={{ textAlign: 'left' }}>Đổi mật khẩu</h3>
            <p className="text-sm text-gray-600">Cập nhật mật khẩu để giữ cho tài khoản của bạn an toàn</p>
          </div>
        </div>
      </div>

      {/* Password Requirements */}
      <div className="bg-gradient-to-r from-amber-50 to-yellow-50 border border-amber-200 rounded-lg p-6">
        <div className="flex items-start space-x-3">
          <div className="flex-shrink-0">
            <div className="w-8 h-8 bg-amber-100 rounded-full flex items-center justify-center">
              <Lock className="h-4 w-4 text-amber-600" />
            </div>
          </div>
          <div className="flex-1">
            <h4 className="text-sm font-semibold text-amber-800 mb-3" style={{ textAlign: 'left' }}>
              Yêu cầu mật khẩu
            </h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm text-amber-700">
              <div className="flex items-center space-x-2">
                <div className="w-1.5 h-1.5 bg-amber-500 rounded-full"></div>
                <span>Ít nhất 8 ký tự</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-1.5 h-1.5 bg-amber-500 rounded-full"></div>
                <span>Chứa chữ cái viết hoa</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-1.5 h-1.5 bg-amber-500 rounded-full"></div>
                <span>Chứa chữ cái viết thường</span>
              </div>
              <div className="flex items-center space-x-2">
                <div className="w-1.5 h-1.5 bg-amber-500 rounded-full"></div>
                <span>Chứa ít nhất một số</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-8">

        <div className="space-y-6">
          {/* Current Password */}
          <div className="space-y-2">
            <label htmlFor="currentPassword" className="flex items-center text-sm font-medium text-gray-700">
              <Lock className="w-4 h-4 mr-2 text-blue-600" />
              Mật khẩu hiện tại
              <span className="text-red-500 ml-1">*</span>
            </label>
            <div className="relative">
              <input
                type={showPasswords.current ? 'text' : 'password'}
                id="currentPassword"
                name="currentPassword"
                value={formData.currentPassword}
                onChange={handleChange}
                className={`w-full px-4 py-3 pr-12 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.currentPassword 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-blue-400 focus:bg-white'
                }`}
                placeholder="Nhập mật khẩu hiện tại"
              />
              <button
                type="button"
                onClick={() => togglePasswordVisibility('current')}
                className="absolute inset-y-0 right-0 pr-4 flex items-center text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showPasswords.current ? (
                  <EyeOff className="h-5 w-5" />
                ) : (
                  <Eye className="h-5 w-5" />
                )}
              </button>
            </div>
            {errors.currentPassword && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.currentPassword}
              </p>
            )}
          </div>

          {/* New Password */}
          <div className="space-y-2">
            <label htmlFor="newPassword" className="flex items-center text-sm font-medium text-gray-700">
              <Lock className="w-4 h-4 mr-2 text-blue-600" />
              Mật khẩu mới
              <span className="text-red-500 ml-1">*</span>
            </label>
            <div className="relative">
              <input
                type={showPasswords.new ? 'text' : 'password'}
                id="newPassword"
                name="newPassword"
                value={formData.newPassword}
                onChange={handleChange}
                className={`w-full px-4 py-3 pr-12 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.newPassword 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-blue-400 focus:bg-white'
                }`}
                placeholder="Nhập mật khẩu mới"
              />
              <button
                type="button"
                onClick={() => togglePasswordVisibility('new')}
                className="absolute inset-y-0 right-0 pr-4 flex items-center text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showPasswords.new ? (
                  <EyeOff className="h-5 w-5" />
                ) : (
                  <Eye className="h-5 w-5" />
                )}
              </button>
            </div>
            {errors.newPassword && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.newPassword}
              </p>
            )}
          </div>

          {/* Confirm Password */}
          <div className="space-y-2">
            <label htmlFor="confirmPassword" className="flex items-center text-sm font-medium text-gray-700">
              <Lock className="w-4 h-4 mr-2 text-blue-600" />
              Xác nhận mật khẩu mới
              <span className="text-red-500 ml-1">*</span>
            </label>
            <div className="relative">
              <input
                type={showPasswords.confirm ? 'text' : 'password'}
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                className={`w-full px-4 py-3 pr-12 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.confirmPassword 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-blue-400 focus:bg-white'
                }`}
                placeholder="Xác nhận mật khẩu mới"
              />
              <button
                type="button"
                onClick={() => togglePasswordVisibility('confirm')}
                className="absolute inset-y-0 right-0 pr-4 flex items-center text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showPasswords.confirm ? (
                  <EyeOff className="h-5 w-5" />
                ) : (
                  <Eye className="h-5 w-5" />
                )}
              </button>
            </div>
            {errors.confirmPassword && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.confirmPassword}
              </p>
            )}
          </div>
        </div>

        {/* Submit Button */}
        <div className="flex justify-end pt-6 border-t border-gray-200">
          <button
            type="submit"
            disabled={isLoading}
            className="inline-flex items-center px-8 py-3 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 transform hover:scale-105"
          >
            {isLoading ? (
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-3"></div>
            ) : (
              <Save className="w-5 h-5 mr-3" />
            )}
            {isLoading ? 'Đang thay đổi mật khẩu...' : 'Thay đổi mật khẩu'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ChangePasswordForm;
