import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Leaf, ShoppingCart, Menu, X, User } from 'lucide-react';
import { useAppSelector, useAppDispatch } from '../../redux';
import { logout } from '../../redux/slices/authSlice';

interface NavigationProps {
  title?: string;
  showCart?: boolean;
  showAuth?: boolean;
  className?: string;
}

const Navigation: React.FC<NavigationProps> = ({
  title: _title, // Keep for future use
  showCart = true,
  showAuth = true,
  className = ''
}) => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const { totalItems } = useAppSelector((state) => state.cart);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/');
  };

  const handleNavigation = (path: string) => {
    navigate(path);
    setIsMobileMenuOpen(false);
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
    <nav className={`bg-white shadow-md sticky top-0 z-50 ${className}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-2">
            <Leaf className="w-8 h-8 text-green-600" />
            <span className="text-2xl font-bold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
              FruitStore
            </span>
          </div>

          <div className="hidden md:flex items-center space-x-8">
            <button 
              onClick={() => navigate('/')}
              className="text-gray-700 hover:text-green-600 transition font-medium"
            >
              Home
            </button>
            <button 
              onClick={() => navigate('/products')}
              className="text-gray-700 hover:text-green-600 transition font-medium"
            >
              Cửa hàng
            </button>
            {isAuthenticated && (
              <>
                <button 
                  onClick={() => navigate('/cart')}
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Giỏ hàng
                </button>
                <button 
                  onClick={() => navigate('/orders')}
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Đơn hàng
                </button>
                <button 
                  onClick={() => navigate('/profile')}
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Hồ sơ
                </button>
              </>
            )}
          </div>

          <div className="flex items-center space-x-4">
            {/* Mobile menu button */}
            <button
              onClick={toggleMobileMenu}
              className="md:hidden p-2 hover:bg-gray-100 rounded-full transition"
            >
              {isMobileMenuOpen ? (
                <X className="w-6 h-6 text-gray-600" />
              ) : (
                <Menu className="w-6 h-6 text-gray-600" />
              )}
            </button>

            {isAuthenticated && showCart && (
              <button 
                onClick={() => navigate('/cart')}
                className="p-2 hover:bg-gray-100 rounded-full transition relative"
              >
                <ShoppingCart className="w-5 h-5 text-gray-600" />
                {totalItems > 0 && (
                  <span className="absolute top-0 right-0 bg-green-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                    {totalItems}
                  </span>
                )}
              </button>
            )}
            
            {showAuth && (
              isAuthenticated ? (
                <button 
                  onClick={handleLogout}
                  className="hidden md:block text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Đăng xuất
                </button>
              ) : (
                <button 
                  onClick={() => navigate('/login')}
                  className="hidden md:block bg-green-600 text-white px-4 py-2 rounded-full font-semibold hover:bg-green-700 transition"
                >
                  Đăng nhập
                </button>
              )
            )}
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      {isMobileMenuOpen && (
        <div className="md:hidden bg-white border-t border-gray-200 shadow-lg">
          <div className="px-2 pt-2 pb-3 space-y-1">
            <button
              onClick={() => handleNavigation('/')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition"
            >
              Trang chủ
            </button>
            <button
              onClick={() => handleNavigation('/products')}
              className="block w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition"
            >
              Cửa hàng
            </button>
            
            {isAuthenticated && (
              <>
                <button
                  onClick={() => handleNavigation('/cart')}
                  className="w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition flex items-center"
                >
                  <ShoppingCart className="w-5 h-5 mr-3" />
                  Giỏ hàng
                  {totalItems > 0 && (
                    <span className="ml-auto bg-green-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                      {totalItems}
                    </span>
                  )}
                </button>
                <button
                  onClick={() => handleNavigation('/orders')}
                  className="block w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition"
                >
                  Đơn hàng
                </button>
                <button
                  onClick={() => handleNavigation('/profile')}
                  className="w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition flex items-center"
                >
                  <User className="w-5 h-5 mr-3" />
                  Hồ sơ
                </button>
              </>
            )}
            
            {showAuth && (
              <div className="border-t border-gray-200 pt-2">
                {isAuthenticated ? (
                  <button
                    onClick={handleLogout}
                    className="block w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-green-600 hover:bg-gray-50 rounded-md transition"
                  >
                    Đăng xuất
                  </button>
                ) : (
                  <button
                    onClick={() => handleNavigation('/login')}
                    className="block w-full text-left px-3 py-2 text-base font-medium text-white bg-green-600 hover:bg-green-700 rounded-md transition"
                  >
                    Đăng nhập
                  </button>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navigation;
