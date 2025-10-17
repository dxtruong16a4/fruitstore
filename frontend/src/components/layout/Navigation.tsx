import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Leaf, ShoppingCart } from 'lucide-react';
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

  const handleLogout = () => {
    dispatch(logout());
    navigate('/');
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
              Shop
            </button>
            {isAuthenticated && (
              <>
                <button 
                  onClick={() => navigate('/cart')}
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Cart
                </button>
                <button 
                  onClick={() => navigate('/orders')}
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Orders
                </button>
              </>
            )}
          </div>

          <div className="flex items-center space-x-4">
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
                  className="text-gray-700 hover:text-green-600 transition font-medium"
                >
                  Logout
                </button>
              ) : (
                <button 
                  onClick={() => navigate('/login')}
                  className="bg-green-600 text-white px-4 py-2 rounded-full font-semibold hover:bg-green-700 transition"
                >
                  Sign In
                </button>
              )
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navigation;
