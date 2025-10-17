import React from 'react';
import { Truck, Clock, MapPin, Package, Shield, Zap, Globe } from 'lucide-react';
import { PageLayout } from '../components/layout';

const ShippingInfoPage: React.FC = () => {
  const shippingOptions = [
    {
      name: 'Giao hàng ngay',
      icon: <Zap className="w-8 h-8 text-yellow-600" />,
      description: 'Nhận trái cây tươi ngay trong ngày',
      time: '2-4 giờ',
      cost: '$9.99',
      freeThreshold: 'Đơn hàng trên $75',
      features: ['Đặt hàng trước 2 PM', 'Giao hàng được kiểm soát nhiệt độ', 'Theo dõi thực tế']
    },
    {
      name: 'Giao hàng ngày mai',
      icon: <Truck className="w-8 h-8 text-blue-600" />,
      description: 'Giao hàng tiêu chuẩn cho ngày mai làm việc',
      time: '24 giờ',
      cost: '$5.99',
      freeThreshold: 'Đơn hàng trên $50',
      features: ['Đặt hàng trước 6 PM', 'Cửa hàng giao hàng được lên lịch', 'Đảm bảo chất lượng']
    },
    {
      name: 'Giao hàng được lên lịch',
      icon: <Clock className="w-8 h-8 text-purple-600" />,
      description: 'Chọn ngày và giờ giao hàng theo ý muốn của bạn',
      time: 'Linh hoạt',
      cost: '$3.99',
      freeThreshold: 'Đơn hàng trên $30',
      features: ['Chọn thời gian giao hàng', 'Có sẵn gói giao hàng hàng tuần', 'Phù hợp cho việc lên lịch']
    },
    {
      name: 'Giao hàng miễn phí tiêu chuẩn',
      icon: <Package className="w-8 h-8 text-green-600" />,
      description: 'Giao hàng miễn phí trên đơn hàng đáp ứng điều kiện',
      time: '2-3 ngày làm việc',
      cost: 'MIỄN PHÍ',
      freeThreshold: 'Đơn hàng trên $25',
      features: ['Không có đơn hàng tối thiểu', 'Bao bì thân thiện với môi trường', 'Theo dõi tiêu chuẩn']
    }
  ];

  const deliveryAreas = [
    { city: 'Thành phố Hồ Chí Minh', state: 'HCM', status: 'Có sẵn', coverage: 'Tất cả quận huyện' },
    { city: 'Los Angeles', state: 'CA', status: 'Available', coverage: 'LA County' },
    { city: 'Hà Nội', state: 'HN', status: 'Có sẵn', coverage: 'Tất cả quận huyện' },
    { city: 'Đà Nẵng', state: 'DN', status: 'Có sẵn', coverage: 'Tất cả quận huyện' },
    { city: 'Hải Phòng', state: 'HP', status: 'Có sẵn', coverage: 'Tất cả quận huyện' },
    { city: 'Philadelphia', state: 'PA', status: 'Coming Soon', coverage: 'Limited areas' },
    { city: 'San Antonio', state: 'TX', status: 'Available', coverage: 'Bexar County' },
    { city: 'San Diego', state: 'CA', status: 'Available', coverage: 'San Diego County' }
  ];

  const packagingInfo = [
    {
      title: 'Kiểm soát nhiệt độ',
      description: 'Tất cả trái cây tươi được đóng gói với băng đá và thùng cách nhiệt để duy trì nhiệt độ tối ưu.',
      icon: '❄️'
    },
    {
      title: 'Vật liệu thân thiện với môi trường',
      description: 'Chúng tôi sử dụng vật liệu bao bì có thể tái sử dụng và phân hủy sinh học khi có thể.',
      icon: '🌱'
    },
    {
      title: 'Bao bì bảo vệ',
      description: 'Mỗi trái cây được bao bì bảo vệ cẩn thận để tránh vỡ và duy trì tươi ngon trong quá trình vận chuyển.',
      icon: '🛡️'
    },
    {
      title: 'Kiểm tra chất lượng',
      description: 'Mọi đơn hàng được kiểm tra trước khi đóng gói để đảm bảo chỉ có trái cây tốt nhất được gửi.',
      icon: '✅'
    }
  ];

  return (
    <PageLayout
      title="Thông tin giao hàng"
      subtitle="Giao hàng nhanh chóng, đáng tin cậy và trái cây tươi đến cửa hàng của bạn."
      showHero={true}
    >

      {/* Shipping Options */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Các tùy chọn giao hàng</h2>
            <p className="text-gray-600 text-lg">Chọn tùy chọn giao hàng phù hợp nhất cho bạn</p>
          </div>

          <div className="grid md:grid-cols-2 gap-8">
            {shippingOptions.map((option, index) => (
              <div key={index} className="bg-white rounded-2xl shadow-lg p-8 hover:shadow-xl transition">
                <div className="flex items-center space-x-4 mb-6">
                  <div className="flex-shrink-0">
                    {option.icon}
                  </div>
                  <div>
                    <h3 className="text-2xl font-bold text-gray-900">{option.name}</h3>
                    <p className="text-gray-600">{option.description}</p>
                  </div>
                </div>

                <div className="space-y-4 mb-6">
                  <div className="flex justify-between items-center py-2 border-b border-gray-100">
                    <span className="text-gray-600">Thời gian giao hàng:</span>
                    <span className="font-semibold text-gray-900">{option.time}</span>
                  </div>
                  <div className="flex justify-between items-center py-2 border-b border-gray-100">
                    <span className="text-gray-600">Chi phí:</span>
                    <span className="font-semibold text-green-600">{option.cost}</span>
                  </div>
                  <div className="flex justify-between items-center py-2">
                    <span className="text-gray-600">Miễn phí trên:</span>
                    <span className="font-semibold text-gray-900">{option.freeThreshold}</span>
                  </div>
                </div>

                <div>
                  <h4 className="font-semibold text-gray-900 mb-3">Tính năng:</h4>
                  <ul className="space-y-2">
                    {option.features.map((feature, featureIndex) => (
                      <li key={featureIndex} className="flex items-center space-x-2 text-sm text-gray-600">
                        <div className="w-2 h-2 bg-green-600 rounded-full"></div>
                        <span>{feature}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Delivery Areas */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Các khu vực giao hàng</h2>
            <p className="text-gray-600 text-lg">Chúng tôi giao hàng đến các thành phố lớn trên toàn quốc</p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            {deliveryAreas.map((area, index) => (
              <div key={index} className="bg-gray-50 rounded-xl p-6 text-center">
                <div className="flex items-center justify-center space-x-2 mb-2">
                  <MapPin className="w-5 h-5 text-green-600" />
                  <span className="font-semibold text-gray-900">{area.city}</span>
                </div>
                <p className="text-gray-600 text-sm mb-2">{area.state}</p>
                <div className={`inline-block px-3 py-1 rounded-full text-xs font-semibold ${
                  area.status === 'Available' 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-yellow-100 text-yellow-800'
                }`}>
                  {area.status}
                </div>
                <p className="text-xs text-gray-500 mt-2">{area.coverage}</p>
              </div>
            ))}
          </div>

          <div className="text-center mt-8">
            <p className="text-gray-600 mb-4">Không thấy thành phố của bạn? Chúng tôi đang phát triển nhanh chóng!</p>
            <button className="bg-green-600 text-white px-6 py-3 rounded-full font-semibold hover:bg-green-700 transition">
              Yêu cầu giao hàng đến khu vực của bạn
            </button>
          </div>
        </div>
      </section>

      {/* Packaging & Care */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Bao bì & Chăm sóc</h2>
            <p className="text-gray-600 text-lg">Làm thế nào để đảm bảo trái cây đến tươi và hoàn hảo</p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            {packagingInfo.map((info, index) => (
              <div key={index} className="text-center">
                <div className="text-5xl mb-4">{info.icon}</div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">{info.title}</h3>
                <p className="text-gray-600 leading-relaxed">{info.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Important Information */}
      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-8">
            <div className="bg-green-50 rounded-2xl p-8">
              <div className="flex items-center space-x-3 mb-4">
                <Shield className="w-8 h-8 text-green-600" />
                <h3 className="text-2xl font-bold text-gray-900">Đảm bảo chất lượng</h3>
              </div>
              <p className="text-gray-600 leading-relaxed">
                Chúng tôi đảm bảo chất lượng của trái cây của bạn khi đến tận nơi. Nếu bạn không hài lòng với tươi ngon 
                hoặc chất lượng của bất kỳ mục nào, liên hệ chúng tôi trong vòng 24 giờ để được hoàn trả hoặc thay thế.
              </p>
            </div>

            <div className="bg-blue-50 rounded-2xl p-8">
              <div className="flex items-center space-x-3 mb-4">
                <Globe className="w-8 h-8 text-blue-600" />
                <h3 className="text-2xl font-bold text-gray-900">Cam kết môi trường</h3>
              </div>
              <p className="text-gray-600 leading-relaxed">
                Chúng tôi cam kết áp dụng các thực hành giao hàng bền vững. Bao bì của chúng tôi là thân thiện với môi trường, 
                và chúng tôi tối ưu hóa các tuyến giao hàng để giảm thiểu phát thải carbon của chúng tôi.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Câu hỏi thường gặp về giao hàng</h2>
          
          <div className="space-y-6">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi có thể theo dõi đơn hàng của mình trực tiếp không?</h3>
              <p className="text-gray-600">Có! Một khi đơn hàng của bạn được gửi, bạn sẽ nhận được liên kết theo dõi để theo dõi đơn hàng của bạn trực tiếp.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi không ở nhà khi đơn hàng đến?</h3>
              <p className="text-gray-600">Đối với các mục dễ hỏng, chúng tôi sẽ để đơn hàng của bạn trong một khu vực mát mẻ và gửi cho bạn một ảnh chụp xác nhận. Bạn cũng có thể lên lịch giao hàng cho một thời gian cụ thể.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Chúng tôi giao hàng vào cuối tuần không?</h3>
              <p className="text-gray-600">Có! Chúng tôi cung cấp giao hàng vào cuối tuần cho các tùy chọn cùng ngày và ngày mai. Giao hàng tiêu chuẩn hoạt động từ thứ Hai đến thứ Sáu.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi có thể thay đổi địa chỉ giao hàng sau khi đặt đơn hàng không?</h3>
              <p className="text-gray-600">Bạn có thể thay đổi địa chỉ giao hàng đến 2 giờ trước thời gian giao hàng đã lên lịch bằng cách liên hệ dịch vụ khách hàng của chúng tôi.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default ShippingInfoPage;
