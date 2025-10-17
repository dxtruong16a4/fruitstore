import React, { useState, useEffect } from 'react';
import { useAppSelector, useAppDispatch } from '../redux';
import { getUserProfile, updateUserProfile, changeUserPassword } from '../redux/thunks/authThunks';
import { useToast } from '../hooks/useToast';
import { ProfileForm, ChangePasswordForm } from '../components';
import { User as UserIcon, Settings, Lock } from 'lucide-react';
import type { User } from '../types';
import { PageLayout } from '../components/layout';

const ProfilePage: React.FC = () => {
  const dispatch = useAppDispatch();
  const { user, isLoading } = useAppSelector((state) => state.auth);
  const { showSuccess, showError, toasts, removeToast } = useToast();
  const [activeTab, setActiveTab] = useState<'profile' | 'password'>('profile');

  useEffect(() => {
    if (user) {
      dispatch(getUserProfile());
    }
  }, [dispatch, user]);

  const handleProfileUpdate = async (profileData: Partial<User>) => {
    try {
      await dispatch(updateUserProfile(profileData)).unwrap();
      showSuccess('Thông tin tài khoản đã được cập nhật thành công!');
    } catch (error: any) {
      showError('Lỗi khi cập nhật thông tin tài khoản', error.message);
    }
  };

  const handlePasswordChange = async (passwordData: { oldPassword: string; newPassword: string }) => {
    try {
      await dispatch(changeUserPassword(passwordData)).unwrap();
      showSuccess('Mật khẩu đã được thay đổi thành công!');
    } catch (error: any) {
      showError('Lỗi khi thay đổi mật khẩu', error.message);
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-green-600"></div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Vui lòng đăng nhập để xem thông tin tài khoản của bạn</h2>
          <button
            onClick={() => window.location.href = '/login'}
            className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition"
          >
            Đến trang đăng nhập
          </button>
        </div>
      </div>
    );
  }

  return (
    <PageLayout>
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
            <div className="flex items-center space-x-4">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center">
                <UserIcon className="w-8 h-8 text-green-600" />
              </div>
              <div>
                <h1 className="text-2xl font-bold text-gray-900">{user.fullName}</h1>
                <p className="text-gray-600">{user.email}</p>
                <p className="text-sm text-gray-500 capitalize" style={{ textAlign: 'left' }}>{user.role}</p>
              </div>
            </div>
          </div>

          {/* Tabs */}
          <div className="bg-white rounded-lg shadow-sm mb-6">
            <div className="border-b border-gray-200">
              <nav className="-mb-px flex space-x-8 px-6">
                <button
                  onClick={() => setActiveTab('profile')}
                  className={`py-4 px-1 border-b-2 font-medium text-sm flex items-center space-x-2 ${
                    activeTab === 'profile'
                      ? 'border-green-500 text-green-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  <Settings className="w-4 h-4" />
                  <span>Thông tin tài khoản</span>
                </button>
                <button
                  onClick={() => setActiveTab('password')}
                  className={`py-4 px-1 border-b-2 font-medium text-sm flex items-center space-x-2 ${
                    activeTab === 'password'
                      ? 'border-green-500 text-green-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  <Lock className="w-4 h-4" />
                  <span>Đổi mật khẩu</span>
                </button>
              </nav>
            </div>

            {/* Tab Content */}
            <div className="p-6">
              {activeTab === 'profile' && (
                <ProfileForm
                  user={user}
                  onSubmit={handleProfileUpdate}
                  isLoading={isLoading}
                />
              )}
              {activeTab === 'password' && (
                <ChangePasswordForm
                  onSubmit={handlePasswordChange}
                  isLoading={isLoading}
                />
              )}
            </div>
          </div>
        </div>
      </div>
      
      {/* Toast Container */}
      <div className="fixed top-4 right-4 z-50 space-y-2">
        {toasts.map((toast) => (
          <div
            key={toast.id}
            className={`p-4 rounded-lg shadow-lg border-l-4 ${
              toast.type === 'success'
                ? 'bg-green-50 border-green-400 text-green-800'
                : toast.type === 'error'
                ? 'bg-red-50 border-red-400 text-red-800'
                : toast.type === 'warning'
                ? 'bg-yellow-50 border-yellow-400 text-yellow-800'
                : 'bg-blue-50 border-blue-400 text-blue-800'
            }`}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <div className="font-medium">{toast.title}</div>
                {toast.message && <div className="text-sm opacity-75">{toast.message}</div>}
              </div>
              <button
                onClick={() => removeToast(toast.id)}
                className="ml-4 text-gray-400 hover:text-gray-600"
              >
                ×
              </button>
            </div>
          </div>
        ))}
      </div>
    </PageLayout>
  );
};

export default ProfilePage;
