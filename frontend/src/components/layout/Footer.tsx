import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Leaf } from 'lucide-react';

interface FooterProps {
  className?: string;
  showFullFooter?: boolean;
}

const Footer: React.FC<FooterProps> = ({ 
  className = '',
  showFullFooter = true 
}) => {
  const navigate = useNavigate();

  if (!showFullFooter) {
    return (
      <footer className={`bg-gray-900 text-white py-12 ${className}`}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <div className="flex items-center justify-center space-x-2 mb-4">
            <Leaf className="w-8 h-8 text-green-500" />
            <span className="text-xl font-bold">FruitStore</span>
          </div>
          <p className="text-gray-400">&copy; 2025 FruitStore. All rights reserved.</p>
        </div>
      </footer>
    );
  }

  return (
    <footer className={`bg-gray-900 text-white py-12 ${className}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid md:grid-cols-4 gap-8 mb-8">
          <div>
            <div className="flex items-center space-x-2 mb-4">
              <Leaf className="w-8 h-8 text-green-500" />
              <span className="text-xl font-bold">FruitStore</span>
            </div>
            <p className="text-gray-400">
              Your trusted source for fresh, organic fruits delivered daily.
            </p>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Quick Links</h4>
            <ul className="space-y-2 text-gray-400">
              <li><button onClick={() => navigate('/')} className="hover:text-green-500 transition">Home</button></li>
              <li><button onClick={() => navigate('/products')} className="hover:text-green-500 transition">Shop</button></li>
              <li><button onClick={() => navigate('/about')} className="hover:text-green-500 transition">About Us</button></li>
              <li><button onClick={() => navigate('/contact')} className="hover:text-green-500 transition">Contact</button></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Customer Service</h4>
            <ul className="space-y-2 text-gray-400">
              <li><button onClick={() => navigate('/help')} className="hover:text-green-500 transition">Help Center</button></li>
              <li><button onClick={() => navigate('/track-order')} className="hover:text-green-500 transition">Track Order</button></li>
              <li><button onClick={() => navigate('/returns')} className="hover:text-green-500 transition">Returns</button></li>
              <li><button onClick={() => navigate('/shipping-info')} className="hover:text-green-500 transition">Shipping Info</button></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Contact Us</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Email: hello@fruitstore.com</li>
              <li>Phone: (555) 123-4567</li>
              <li>Address: 123 Fresh Street</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-800 pt-8 text-center text-gray-400">
          <p>&copy; 2025 FruitStore. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;