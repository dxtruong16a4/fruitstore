import React, { useState } from 'react';
import { ArrowLeft, Package, CheckCircle, Clock, AlertCircle } from 'lucide-react';
import { PageLayout } from '../components/layout';

const ReturnsPage: React.FC = () => {
  const [selectedReason, setSelectedReason] = useState('');
  const [orderNumber, setOrderNumber] = useState('');
  const [returnItems, setReturnItems] = useState<string[]>([]);

  const returnReasons = [
    'Sản phẩm bị hư hỏng hoặc lỗi',
    'Sản phẩm nhận sai',
    'Sản phẩm không như mô tả',
    'Chất lượng không đáp ứng',
    'Thay đổi ý muốn',
    'Đặt hàng bị nhầm',
    'Khác (vui lòng chỉ rõ)'
  ];

  const mockOrderItems = [
    { id: 1, name: 'Táo hữu cơ', quantity: 2, price: 12.99 },
    { id: 2, name: 'Chuối tươi', quantity: 1, price: 4.99 },
    { id: 3, name: 'Trái cây hỗn hợp', quantity: 1, price: 8.99 }
  ];

  const handleItemSelect = (itemId: number) => {
    const itemIdStr = itemId.toString();
    setReturnItems(prev => 
      prev.includes(itemIdStr) 
        ? prev.filter(id => id !== itemIdStr)
        : [...prev, itemIdStr]
    );
  };

  const handleSubmitReturn = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle return submission
    alert('Return request submitted successfully! You will receive a confirmation email shortly.');
  };

  return (
    <PageLayout
      title="Trả hàng & Hoàn trả"
      subtitle="Chúng tôi muốn bạn hoàn toàn hài lòng với đơn hàng trái cây của bạn."
      showHero={true}
    >

      {/* Return Policy */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="bg-white rounded-2xl shadow-lg p-8 mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Chính sách trả hàng của chúng tôi</h2>
            
            <div className="grid md:grid-cols-2 gap-8">
              <div className="space-y-4">
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">30 ngày trả hàng</h3>
                    <p className="text-gray-600 text-sm">Trả hàng trong vòng 30 ngày từ ngày giao hàng để nhận được hoàn trả đầy đủ.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Đảm bảo chất lượng</h3>
                    <p className="text-gray-600 text-sm">Không hài lòng với chất lượng? Chúng tôi sẽ giải quyết nó, đảm bảo.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Vận chuyển trả hàng miễn phí</h3>
                    <p className="text-gray-600 text-sm">Chúng tôi bao gồm chi phí vận chuyển trả hàng cho các vấn đề về chất lượng.</p>
                  </div>
                </div>
              </div>
              
              <div className="space-y-4">
                <div className="flex items-start space-x-3">
                  <Clock className="w-6 h-6 text-blue-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Xử lý nhanh</h3>
                    <p className="text-gray-600 text-sm">Tiền hoàn được xử lý trong vòng 3-5 ngày làm việc sau khi chúng tôi nhận được trả hàng của bạn.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <AlertCircle className="w-6 h-6 text-orange-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Sản phẩm tươi sống</h3>
                    <p className="text-gray-600 text-sm">Sản phẩm tươi sống nên được trả trong vòng 48 giờ nếu có vấn đề về chất lượng.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <Package className="w-6 h-6 text-purple-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Bao bì gốc</h3>
                    <p className="text-gray-600 text-sm">Sản phẩm nên được trả trong bao bì gốc khi có thể.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Return Request Form */}
      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Bắt đầu trả hàng</h2>
          
          <form onSubmit={handleSubmitReturn} className="space-y-8">
            {/* Order Number */}
            <div>
              <label htmlFor="orderNumber" className="block text-sm font-medium text-gray-700 mb-2">
                Số đơn hàng
              </label>
              <input
                type="text"
                id="orderNumber"
                value={orderNumber}
                onChange={(e) => setOrderNumber(e.target.value)}
                required
                className="w-full px-4 py-3 border border-gray-300 rounded-lg text-white placeholder-gray-300 bg-gray-800 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                placeholder="Nhập số đơn hàng của bạn"
              />
            </div>

            {/* Items to Return */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-4">
                Chọn sản phẩm để trả hàng
              </label>
              <div className="space-y-3">
                {mockOrderItems.map((item) => (
                  <div key={item.id} className="flex items-center space-x-3 p-4 border border-gray-200 rounded-lg">
                    <input
                      type="checkbox"
                      id={`item-${item.id}`}
                      checked={returnItems.includes(item.id.toString())}
                      onChange={() => handleItemSelect(item.id)}
                      className="w-4 h-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                    />
                    <label htmlFor={`item-${item.id}`} className="flex-1 cursor-pointer">
                      <div className="flex justify-between items-center">
                        <span className="font-medium text-gray-900">{item.name}</span>
                        <span className="text-gray-600">${item.price}</span>
                      </div>
                      <span className="text-sm text-gray-500">Số lượng: {item.quantity}</span>
                    </label>
                  </div>
                ))}
              </div>
            </div>

            {/* Return Reason */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-4">
                Lý do trả hàng
              </label>
              <div className="grid grid-cols-1 gap-3">
                {returnReasons.map((reason) => (
                  <label key={reason} className="flex items-center space-x-3 p-3 border border-gray-200 rounded-lg cursor-pointer hover:bg-gray-50">
                    <input
                      type="radio"
                      name="returnReason"
                      value={reason}
                      checked={selectedReason === reason}
                      onChange={(e) => setSelectedReason(e.target.value)}
                      className="w-4 h-4 text-green-600 focus:ring-green-500 border-gray-300"
                    />
                    <span className="text-gray-900">{reason}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Additional Comments */}
            <div>
              <label htmlFor="comments" className="block text-sm font-medium text-gray-700 mb-2">
                Bình luận bổ sung
              </label>
              <textarea
                id="comments"
                rows={4}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg text-white placeholder-gray-300 bg-gray-800 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                placeholder="Vui lòng cung cấp bất kỳ chi tiết bổ sung nào về trả hàng của bạn..."
              />
            </div>

            {/* Submit Button */}
            <div className="text-center">
              <button
                type="submit"
                disabled={!orderNumber || !selectedReason || returnItems.length === 0}
                className="bg-green-600 text-white px-8 py-4 rounded-full font-semibold hover:bg-green-700 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center space-x-2 mx-auto"
              >
                <ArrowLeft className="w-5 h-5" />
                <span>Gửi yêu cầu trả hàng</span>
              </button>
            </div>
          </form>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Câu hỏi thường gặp về trả hàng</h2>
          
          <div className="space-y-6">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Bao lâu tôi có thể trả hàng?</h3>
              <p className="text-gray-600">Bạn có 30 ngày từ ngày giao hàng để trả hàng cho hầu hết các sản phẩm. Sản phẩm tươi sống nên được trả trong vòng 48 giờ nếu có vấn đề về chất lượng.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi có phải trả phí vận chuyển trả hàng không?</h3>
              <p className="text-gray-600">Chúng tôi cung cấp vận chuyển trả hàng miễn phí cho các vấn đề về chất lượng, sản phẩm bị hư hỏng, hoặc sản phẩm sai. Đối với các lý do khác, chi phí vận chuyển trả hàng có thể áp dụng.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi sẽ nhận được tiền hoàn như thế nào?</h3>
              <p className="text-gray-600">Tiền hoàn được xử lý đến phương thức thanh toán gốc của bạn trong vòng 3-5 ngày làm việc sau khi chúng tôi nhận được trả hàng của bạn.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Tôi có thể đổi sản phẩm thay vì trả hàng không?</h3>
              <p className="text-gray-600">Có! Liên hệ với đội ngũ dịch vụ khách hàng của chúng tôi và chúng tôi sẽ giúp bạn sắp xếp đổi sản phẩm cho các sản phẩm tương tự.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default ReturnsPage;
