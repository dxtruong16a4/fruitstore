import React, { useState, useEffect } from 'react';
import type { User } from '../types';
import { Save, User as UserIcon, Mail, Phone, MapPin, CheckCircle } from 'lucide-react';

interface ProfileFormProps {
  user: User;
  onSubmit: (data: Partial<User>) => void;
  isLoading: boolean;
}

const ProfileForm: React.FC<ProfileFormProps> = ({ user, onSubmit, isLoading }) => {
  const [formData, setFormData] = useState({
    fullName: user.fullName || '',
    email: user.email || '',
    phone: user.phone || '',
    address: user.address || '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSuccess, setIsSuccess] = useState(false);

  useEffect(() => {
    setFormData({
      fullName: user.fullName || '',
      email: user.email || '',
      phone: user.phone || '',
      address: user.address || '',
    });
  }, [user]);

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.fullName.trim()) {
      newErrors.fullName = 'Tên đầy đủ là bắt buộc';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email là bắt buộc';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Vui lòng nhập địa chỉ email hợp lệ';
    }

    if (formData.phone && !/^[0-9+\-\s()]+$/.test(formData.phone)) {
      newErrors.phone = 'Vui lòng nhập số điện thoại hợp lệ';
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
      await onSubmit(formData);
      
      // Show success state
      setIsSuccess(true);
      
      // Hide success state after 3 seconds
      setTimeout(() => {
        setIsSuccess(false);
      }, 3000);
    } catch (error) {
      // Error handling is done in parent component
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
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
              <h4 className="text-sm font-semibold text-green-800">Thông tin tài khoản đã được cập nhật thành công!</h4>
              <p className="text-sm text-green-700">Thông tin tài khoản của bạn đã được cập nhật thành công.</p>
            </div>
          </div>
        </div>
      )}

      {/* Form Header */}
      <div className="bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg p-6 border border-green-100">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
            <UserIcon className="w-5 h-5 text-green-600" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900" style={{ textAlign: 'left' }}>Thông tin tài khoản</h3>
            <p className="text-sm text-gray-600">Cập nhật thông tin tài khoản của bạn</p>
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Full Name */}
          <div className="space-y-2">
            <label htmlFor="fullName" className="flex items-center text-sm font-medium text-gray-700">
              <UserIcon className="w-4 h-4 mr-2 text-green-600" />
              Tên đầy đủ
              <span className="text-red-500 ml-1">*</span>
            </label>
            <div className="relative">
              <input
                type="text"
                id="fullName"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                className={`w-full px-4 py-3 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 ${
                  errors.fullName 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-green-400 focus:bg-white'
                }`}
                placeholder="Nhập tên đầy đủ của bạn"
              />
            </div>
            {errors.fullName && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.fullName}
              </p>
            )}
          </div>

          {/* Email */}
          <div className="space-y-2">
            <label htmlFor="email" className="flex items-center text-sm font-medium text-gray-700">
              <Mail className="w-4 h-4 mr-2 text-green-600" />
              Email
              <span className="text-red-500 ml-1">*</span>
            </label>
            <div className="relative">
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className={`w-full px-4 py-3 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 ${
                  errors.email 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-green-400 focus:bg-white'
                }`}
                placeholder="Nhập địa chỉ email của bạn"
              />
            </div>
            {errors.email && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.email}
              </p>
            )}
          </div>

          {/* Phone */}
          <div className="space-y-2">
            <label htmlFor="phone" className="flex items-center text-sm font-medium text-gray-700">
              <Phone className="w-4 h-4 mr-2 text-green-600" />
              Số điện thoại
            </label>
            <div className="relative">
              <input
                type="tel"
                id="phone"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                className={`w-full px-4 py-3 border rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 ${
                  errors.phone 
                    ? 'border-red-300 bg-red-50' 
                    : 'border-gray-300 bg-white hover:border-green-400 focus:bg-white'
                }`}
                placeholder="Nhập số điện thoại của bạn"
              />
            </div>
            {errors.phone && (
              <p className="text-sm text-red-600 flex items-center">
                <span className="w-1 h-1 bg-red-500 rounded-full mr-2"></span>
                {errors.phone}
              </p>
            )}
          </div>

          {/* Address */}
          <div className="space-y-2 lg:col-span-2">
            <label htmlFor="address" className="flex items-center text-sm font-medium text-gray-700">
              <MapPin className="w-4 h-4 mr-2 text-green-600" />
              Địa chỉ
            </label>
            <div className="relative">
              <textarea
                id="address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                rows={4}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 bg-white hover:border-green-400 focus:bg-white resize-none"
                placeholder="Nhập địa chỉ cụ thể của bạn"
              />
            </div>
          </div>
        </div>

        {/* Submit Button */}
        <div className="flex justify-end pt-6 border-t border-gray-200">
          <button
            type="submit"
            disabled={isLoading}
            className="inline-flex items-center px-8 py-3 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 transform hover:scale-105"
          >
            {isLoading ? (
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-3"></div>
            ) : (
              <Save className="w-5 h-5 mr-3" />
            )}
            {isLoading ? 'Đang cập nhật thông tin tài khoản...' : 'Cập nhật thông tin tài khoản'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProfileForm;
