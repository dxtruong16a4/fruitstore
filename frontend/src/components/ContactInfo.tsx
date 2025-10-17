import React from 'react';
import { Mail, Phone, MapPin, Clock } from 'lucide-react';

interface ContactInfoProps {
  className?: string;
}

const ContactInfo: React.FC<ContactInfoProps> = ({ className = '' }) => {
  return (
    <div className={className}>
      <h2 className="text-3xl font-bold text-gray-900 mb-8">Thông tin liên hệ</h2>
      
      <div className="space-y-6">
        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Mail className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Email: hello@fruitstore.com</h3>
            <p className="text-gray-600">hello@fruitstore.com</p>
            <p className="text-gray-600">support@fruitstore.com</p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Phone className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Số điện thoại: +84 909 090 909</h3>
            <p className="text-gray-600">+1 (555) 123-4567</p>
            <p className="text-gray-600">Mon-Fri 9AM-6PM EST</p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <MapPin className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Địa chỉ: Số 123, Hà Nội, Việt Nam</h3>
            <p className="text-gray-600">
              Số 123, Hà Nội, Việt Nam
            </p>
          </div>
        </div>

        <div className="flex items-start space-x-4">
          <div className="flex-shrink-0">
            <Clock className="w-6 h-6 text-green-600 mt-1" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Giờ làm việc: Thứ Hai - Thứ Sáu: 9:00 - 18:00, Chủ Nhật: 10:00 - 16:00</h3>
            <div className="text-gray-600 space-y-1">
              <p>Thứ Hai - Thứ Sáu: 9:00 - 18:00</p>
              <p>Chủ Nhật: 10:00 - 16:00</p>
            </div>
          </div>
        </div>
      </div>

      <div className="mt-8 p-6 bg-green-50 rounded-2xl">
        <h3 className="text-lg font-semibold text-gray-900 mb-2">Cần trợ giúp ngay lập tức?</h3>
        <p className="text-gray-600 mb-4">
          Với các vấn đề khẩn cấp, vui lòng gọi đường dây dịch vụ khách hàng của chúng tôi. 
          Chúng tôi sẵn sàng giúp bạn với bất kỳ câu hỏi hoặc cảm giác nào.
        </p>
        <button className="bg-green-600 text-white px-6 py-2 rounded-full font-semibold hover:bg-green-700 transition">
          Gọi ngay
        </button>
      </div>
    </div>
  );
};

export default ContactInfo;
