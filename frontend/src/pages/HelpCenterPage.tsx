import React, { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { HelpCategories, ContactSupport } from '../components';
import { PageLayout } from '../components/layout';

const HelpCenterPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [expandedFAQ, setExpandedFAQ] = useState<number | null>(null);

  const faqs = [
    {
      id: 1,
      question: "Làm thế nào để đặt hàng?",
      answer: "Đặt hàng rất đơn giản! Chỉ cần duyệt qua số lượng trái cây tươi, thêm vào giỏ hàng và tiến hành thanh toán. Bạn có thể tạo tài khoản để thanh toán nhanh hơn hoặc thanh toán dưới dạng khách."
    },
    {
      id: 2,
      question: "Bạn có cung cấp giao hàng nhanh không?",
      answer: "Chúng tôi cung cấp giao hàng ngay hôm đó cho đơn hàng đặt trước 2 PM, và giao hàng ngày mai cho tất cả các đơn hàng khác. Giao hàng miễn phí trên đơn hàng trên 50.000 VNĐ. Chúng tôi cũng cung cấp giao hàng đặt trước cho bạn thuận tiện."
    },
    {
      id: 3,
      question: "Làm thế nào để theo dõi đơn hàng của mình?",
      answer: "Khi đơn hàng được xác nhận, bạn sẽ nhận được một số tracking number qua email. Bạn cũng có thể theo dõi đơn hàng bằng cách đăng nhập vào tài khoản của mình và truy cập vào phần 'Đơn hàng của tôi'."
    },
    {
      id: 4,
      question: "Tôi không hài lòng với trái cây của mình, tôi có thể đổi trả không?",
      answer: "Chúng tôi đảm bảo chất lượng của sản phẩm của chúng tôi. Nếu bạn không hài lòng hoàn toàn, liên hệ chúng tôi trong vòng 24 giờ và chúng tôi sẽ cung cấp hoàn trả đầy đủ hoặc thay thế."
    },
    {
      id: 5,
      question: "Bạn có cung cấp trái cây hữu cơ không?",
      answer: "Có! Chúng tôi có một số loại trái cây hữu cơ từ nông trại địa phương. Tìm kiếm nhãn 'Hữu cơ' trên trang sản phẩm của chúng tôi."
    },
    {
      id: 6,
      question: "Làm thế nào để lưu trữ trái cây của mình?",
      answer: "Hầu hết các loại trái cây nên được lưu trữ trong tủ lạnh để duy trì tươi. Chuối và một số loại trái cây tropical có thể được lưu trữ ở nhiệt độ phòng. Kiểm tra trang sản phẩm cụ thể để có hướng dẫn lưu trữ."
    },
    {
      id: 7,
      question: "Tôi có thể sửa đổi hoặc hủy đơn hàng của mình không?",
      answer: "Đơn hàng có thể được sửa đổi hoặc hủy trong vòng 30 phút sau khi đặt hàng. Sau đó, vui lòng liên hệ đội hỗ trợ khách hàng của chúng tôi để được hỗ trợ."
    },
    {
      id: 8,
      question: "Bạn chấp nhận phương thức thanh toán nào?",
      answer: "Chúng tôi chấp nhận tất cả các thẻ tín dụng chính (Visa, MasterCard, American Express), PayPal và Apple Pay cho sự tiện lợi của bạn."
    }
  ];

  const helpCategories = [
    {
      title: "Đặt hàng & Thanh toán",
      icon: "🛒",
      topics: ["Làm thế nào để đặt hàng", "Phương thức thanh toán", "Xác nhận đơn hàng", "Thanh toán khách"]
    },
    {
      title: "Giao hàng & Vận chuyển",
      icon: "🚚",
      topics: ["Các tùy chọn giao hàng", "Chi phí vận chuyển", "Thời gian giao hàng", "Theo dõi đơn hàng của bạn"]
    },
    {
      title: "Đổi trả & Hoàn trả",
      icon: "↩️",
      topics: ["Chính sách đổi trả", "Làm thế nào để đổi trả sản phẩm", "Quy trình hoàn trả", "Đảm bảo chất lượng"]
    },
    {
      title: "Tài khoản & Hồ sơ",
      icon: "👤",
      topics: ["Tạo tài khoản", "Vấn đề đăng nhập", "Cập nhật hồ sơ", "Lịch sử đơn hàng"]
    },
    {
      title: "Sản phẩm & Chất lượng",
      icon: "🍎",
      topics: ["Thông tin sản phẩm", "Tùy chọn hữu cơ", "Hướng dẫn lưu trữ", "Khả năng sản xuất theo mùa"]
    },
    {
      title: "Hỗ trợ kỹ thuật",
      icon: "💻",
      topics: ["Vấn đề trang web", "Ứng dụng di động", "Tương thích trình duyệt", "Bảo mật tài khoản"]
    }
  ];

  const filteredFAQs = faqs.filter(faq => 
    faq.question.toLowerCase().includes(searchQuery.toLowerCase()) ||
    faq.answer.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const toggleFAQ = (id: number) => {
    setExpandedFAQ(expandedFAQ === id ? null : id);
  };

  return (
    <PageLayout
      title="Làm thế nào để chúng tôi có thể giúp bạn?"
      subtitle="Tìm câu trả lời cho các câu hỏi thường gặp hoặc liên hệ với đội hỗ trợ của chúng tôi."
      showHero={true}
      heroProps={{
        showSearch: true,
        searchPlaceholder: "Tìm kiếm hỗ trợ...",
        onSearch: setSearchQuery
      }}
    >

      <HelpCategories categories={helpCategories} />

      {/* FAQ Section */}
      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">
              {searchQuery ? 'Kết quả tìm kiếm' : 'Các câu hỏi thường gặp'}
            </h2>
            <p className="text-gray-600 text-lg">
              {searchQuery ? `Tìm thấy ${filteredFAQs.length} kết quả` : 'Câu trả lời nhanh cho các câu hỏi thường gặp'}
            </p>
          </div>

          <div className="space-y-4">
            {filteredFAQs.map((faq) => (
              <div key={faq.id} className="bg-gray-50 rounded-lg overflow-hidden">
                <button
                  onClick={() => toggleFAQ(faq.id)}
                  className="w-full px-6 py-4 text-left flex justify-between items-center hover:bg-gray-100 transition"
                >
                  <span className="text-lg font-semibold text-gray-900">{faq.question}</span>
                  {expandedFAQ === faq.id ? (
                    <ChevronUp className="w-5 h-5 text-gray-500" />
                  ) : (
                    <ChevronDown className="w-5 h-5 text-gray-500" />
                  )}
                </button>
                {expandedFAQ === faq.id && (
                  <div className="px-6 pb-4">
                    <p className="text-gray-600 leading-relaxed">{faq.answer}</p>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </section>

      <ContactSupport />
    </PageLayout>
  );
};

export default HelpCenterPage;
