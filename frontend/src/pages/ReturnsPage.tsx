import React, { useState } from 'react';
import { ArrowLeft, Package, CheckCircle, Clock, AlertCircle } from 'lucide-react';
import { PageLayout } from '../components/layout';

const ReturnsPage: React.FC = () => {
  const [selectedReason, setSelectedReason] = useState('');
  const [orderNumber, setOrderNumber] = useState('');
  const [returnItems, setReturnItems] = useState<string[]>([]);

  const returnReasons = [
    'Damaged or defective item',
    'Wrong item received',
    'Item not as described',
    'Quality not satisfactory',
    'Changed my mind',
    'Ordered by mistake',
    'Other'
  ];

  const mockOrderItems = [
    { id: 1, name: 'Organic Apples', quantity: 2, price: 12.99 },
    { id: 2, name: 'Fresh Bananas', quantity: 1, price: 4.99 },
    { id: 3, name: 'Mixed Berries', quantity: 1, price: 8.99 }
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
      title="Returns & Refunds"
      subtitle="We want you to be completely satisfied with your fresh fruit order."
      showHero={true}
      navigationProps={{ title: "Returns & Refunds" }}
    >

      {/* Return Policy */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="bg-white rounded-2xl shadow-lg p-8 mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Return Policy</h2>
            
            <div className="grid md:grid-cols-2 gap-8">
              <div className="space-y-4">
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">30-Day Return Window</h3>
                    <p className="text-gray-600 text-sm">Return items within 30 days of delivery for a full refund.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Quality Guarantee</h3>
                    <p className="text-gray-600 text-sm">Not satisfied with quality? We'll make it right, guaranteed.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <CheckCircle className="w-6 h-6 text-green-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Free Return Shipping</h3>
                    <p className="text-gray-600 text-sm">We cover return shipping costs for quality issues.</p>
                  </div>
                </div>
              </div>
              
              <div className="space-y-4">
                <div className="flex items-start space-x-3">
                  <Clock className="w-6 h-6 text-blue-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Quick Processing</h3>
                    <p className="text-gray-600 text-sm">Refunds processed within 3-5 business days.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <AlertCircle className="w-6 h-6 text-orange-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Fresh Items</h3>
                    <p className="text-gray-600 text-sm">Perishable items must be returned within 48 hours.</p>
                  </div>
                </div>
                
                <div className="flex items-start space-x-3">
                  <Package className="w-6 h-6 text-purple-600 mt-1 flex-shrink-0" />
                  <div>
                    <h3 className="font-semibold text-gray-900">Original Packaging</h3>
                    <p className="text-gray-600 text-sm">Items should be returned in original packaging when possible.</p>
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
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Start a Return</h2>
          
          <form onSubmit={handleSubmitReturn} className="space-y-8">
            {/* Order Number */}
            <div>
              <label htmlFor="orderNumber" className="block text-sm font-medium text-gray-700 mb-2">
                Order Number
              </label>
              <input
                type="text"
                id="orderNumber"
                value={orderNumber}
                onChange={(e) => setOrderNumber(e.target.value)}
                required
                className="w-full px-4 py-3 border border-gray-300 rounded-lg text-white placeholder-gray-300 bg-gray-800 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                placeholder="Enter your order number"
              />
            </div>

            {/* Items to Return */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-4">
                Select Items to Return
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
                      <span className="text-sm text-gray-500">Quantity: {item.quantity}</span>
                    </label>
                  </div>
                ))}
              </div>
            </div>

            {/* Return Reason */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-4">
                Reason for Return
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
                Additional Comments (Optional)
              </label>
              <textarea
                id="comments"
                rows={4}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg text-white placeholder-gray-300 bg-gray-800 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                placeholder="Please provide any additional details about your return..."
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
                <span>Submit Return Request</span>
              </button>
            </div>
          </form>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Return FAQ</h2>
          
          <div className="space-y-6">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">How long do I have to return items?</h3>
              <p className="text-gray-600">You have 30 days from the delivery date to return most items. Fresh produce should be returned within 48 hours if there are quality issues.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Do I have to pay for return shipping?</h3>
              <p className="text-gray-600">We provide free return shipping for quality issues, damaged items, or wrong items. For other reasons, return shipping costs may apply.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">How will I receive my refund?</h3>
              <p className="text-gray-600">Refunds are processed to your original payment method within 3-5 business days after we receive your return.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Can I exchange items instead of returning?</h3>
              <p className="text-gray-600">Yes! Contact our customer service team and we'll help you arrange an exchange for similar items.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default ReturnsPage;
