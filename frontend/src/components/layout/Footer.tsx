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
          <p className="text-gray-400">&copy; 2025 FruitStore. Tất cả quyền được bảo lưu.</p>
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
              Nguồn cung cấp trái cây tươi, hữu cơ đáng tin cậy được giao hàng hàng ngày.
            </p>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Liên kết nhanh</h4>
            <ul className="space-y-2 text-gray-400">
              <li><button onClick={() => navigate('/')} className="hover:text-green-500 transition">Trang chủ</button></li>
              <li><button onClick={() => navigate('/products')} className="hover:text-green-500 transition">Cửa hàng</button></li>
              <li><button onClick={() => navigate('/about')} className="hover:text-green-500 transition">Về chúng tôi</button></li>
              <li><button onClick={() => navigate('/contact')} className="hover:text-green-500 transition">Liên hệ</button></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Dịch vụ khách hàng</h4>
            <ul className="space-y-2 text-gray-400">
              <li><button onClick={() => navigate('/help')} className="hover:text-green-500 transition">Trung tâm trợ giúp</button></li>
              <li><button onClick={() => navigate('/track-order')} className="hover:text-green-500 transition">Theo dõi đơn hàng</button></li>
              <li><button onClick={() => navigate('/returns')} className="hover:text-green-500 transition">Đổi trả</button></li>
              <li><button onClick={() => navigate('/shipping-info')} className="hover:text-green-500 transition">Thông tin giao hàng</button></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Liên hệ với chúng tôi</h4>
            <ul className="space-y-2 text-gray-400">
              <li>Email: hello@fruitstore.com</li>
              <li>Điện thoại: (555) 123-4567</li>
              <li>Địa chỉ: 123 Fresh Street</li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-800 pt-8 text-center text-gray-400">
          <p>&copy; 2025 FruitStore. Tất cả quyền được bảo lưu.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;