import React from 'react';
import { Heart, Users, Award, Globe } from 'lucide-react';
import { PageLayout } from '../components/layout';

const AboutPage: React.FC = () => {
  return (
    <PageLayout
      title="Về FruitStore"
      subtitle="Chúng tôi đam mê và làm việc tốt nhất."
      showHero={true}
    >

      {/* Our Story */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-4xl font-bold text-gray-900 mb-6">Tuyên ngôn</h2>
              <div className="space-y-4 text-gray-600 leading-relaxed">
                <p>
                  FruitStore được thành lập vào năm 2025 với sứ mệnh đơn giản: kết nối mọi người với trái cây tươi nhất, ngon nhất.
                </p>
              </div>
            </div>
            <div className="relative">
              <img
                src="https://images.pexels.com/photos/1070531/pexels-photo-1070531.jpeg?auto=compress&cs=tinysrgb&w=800"
                alt="Fresh fruits from local farms"
                className="rounded-2xl shadow-xl w-full h-auto"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Our Values */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Giá trị cốt lõi</h2>
            <p className="text-gray-600 text-lg">Những điều mà chúng tôi tin tưởng vào mỗi ngày</p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Heart className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Chất lượng</h3>
              <p className="text-gray-600">Chúng tôi không bao giờ để lỡ chất lượng và tươi nhất của trái cây.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Users className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Cộng đồng</h3>
              <p className="text-gray-600">Hỗ trợ nông dân địa phương và xây dựng mối quan hệ lâu dài.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Globe className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Bền vững</h3>
              <p className="text-gray-600">Chúng tôi cam kết với những thói quen sạch và bền vững.</p>
            </div>

            <div className="text-center p-6 rounded-2xl hover:bg-green-50 transition">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4">
                <Award className="w-8 h-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Tuyệt vời</h3>
              <p className="text-gray-600">Chúng tôi luôn cố gắng để làm việc tốt nhất.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Team Section */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Gặp gỡ đội ngũ</h2>
            <p className="text-gray-600 text-lg">Những người đam mê và làm việc tốt nhất</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">DT</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Đỗ Xuân Trường</h3>
              <p className="text-green-600 font-medium mb-2">Tổng giám đốc</p>
              <p className="text-gray-600 text-sm">Đam mê và làm việc tốt nhất.</p>
            </div>

            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">MN</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Nguyễn Minh Ngọc</h3>
              <p className="text-green-600 font-medium mb-2">Trưởng phòng vận hành</p>
              <p className="text-gray-600 text-sm">Đảm bảo rằng mọi đơn hàng đều được xử lý với cẩn thận và chú ý đến chi tiết.</p>
            </div>

            <div className="text-center">
              <div className="w-32 h-32 mx-auto mb-4 rounded-full bg-gradient-to-r from-green-400 to-emerald-400 flex items-center justify-center">
                <span className="text-4xl text-white font-bold">NV</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Nguyễn Văn B</h3>
              <p className="text-green-600 font-medium mb-2">Kiểm tra chất lượng</p>
              <p className="text-gray-600 text-sm">Đảm bảo rằng chỉ có trái cây tốt nhất đến được bàn của bạn.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default AboutPage;
