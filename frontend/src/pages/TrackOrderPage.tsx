import React, { useState } from 'react';
import { Search, Package, Truck, CheckCircle, Clock, MapPin } from 'lucide-react';
import { PageLayout } from '../components/layout';

const TrackOrderPage: React.FC = () => {
  const [trackingNumber, setTrackingNumber] = useState('');
  const [orderStatus, setOrderStatus] = useState<'idle' | 'loading' | 'found' | 'not-found'>('idle');
  const [mockOrder, setMockOrder] = useState<any>(null);

  const handleTrackOrder = (e: React.FormEvent) => {
    e.preventDefault();
    if (!trackingNumber.trim()) return;

    setOrderStatus('loading');
    
    // Simulate API call
    setTimeout(() => {
      if (trackingNumber.toLowerCase().includes('fs')) {
        setMockOrder({
          orderNumber: trackingNumber,
          status: 'shipped',
          estimatedDelivery: '2025-01-15',
          carrier: 'FruitStore Express',
          trackingSteps: [
            { status: 'confirmed', title: 'Order Confirmed', description: 'Your order has been confirmed and payment processed.', completed: true, date: '2025-01-10 10:30 AM' },
            { status: 'preparing', title: 'Preparing Order', description: 'We are carefully selecting and packing your fresh fruits.', completed: true, date: '2025-01-10 2:15 PM' },
            { status: 'shipped', title: 'Shipped', description: 'Your order is on its way!', completed: true, date: '2025-01-11 9:00 AM' },
            { status: 'out-for-delivery', title: 'Out for Delivery', description: 'Your order is out for delivery and will arrive today.', completed: false, date: '2025-01-15 8:00 AM' },
            { status: 'delivered', title: 'Delivered', description: 'Your order has been delivered successfully.', completed: false, date: null }
          ],
          items: [
            { name: 'Organic Apples', quantity: 2, price: 12.99 },
            { name: 'Fresh Bananas', quantity: 1, price: 4.99 },
            { name: 'Mixed Berries', quantity: 1, price: 8.99 }
          ],
          shippingAddress: '123 Main St, City, State 12345'
        });
        setOrderStatus('found');
      } else {
        setOrderStatus('not-found');
      }
    }, 1500);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'confirmed':
      case 'preparing':
        return 'text-blue-600 bg-blue-100';
      case 'shipped':
      case 'out-for-delivery':
        return 'text-orange-600 bg-orange-100';
      case 'delivered':
        return 'text-green-600 bg-green-100';
      default:
        return 'text-gray-600 bg-gray-100';
    }
  };

  const getStatusIcon = (status: string, completed: boolean) => {
    if (completed) {
      return <CheckCircle className="w-5 h-5 text-green-600" />;
    }
    
    switch (status) {
      case 'shipped':
      case 'out-for-delivery':
        return <Truck className="w-5 h-5 text-orange-600" />;
      default:
        return <Clock className="w-5 h-5 text-gray-400" />;
    }
  };

  return (
    <PageLayout
      title="Track Your Order"
      subtitle="Enter your tracking number to see the status of your fresh fruit delivery."
      showHero={true}
      navigationProps={{ title: "Track Your Order" }}
      heroProps={{
        showSearch: false
      }}
    >
      {/* Tracking Form */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <form onSubmit={handleTrackOrder} className="max-w-2xl mx-auto">
            <div className="flex gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                <input
                  type="text"
                  placeholder="Enter tracking number (e.g., FS123456789)"
                  value={trackingNumber}
                  onChange={(e) => setTrackingNumber(e.target.value)}
                  className="w-full pl-12 pr-4 py-4 rounded-full text-gray-900 placeholder-gray-500 bg-white border-2 border-gray-200 focus:outline-none focus:ring-4 focus:ring-green-300 focus:border-green-500"
                  required
                />
              </div>
              <button
                type="submit"
                disabled={orderStatus === 'loading'}
                className="bg-green-600 text-white px-8 py-4 rounded-full font-semibold hover:bg-green-700 transition transform hover:scale-105 shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {orderStatus === 'loading' ? 'Tracking...' : 'Track Order'}
              </button>
            </div>
          </form>
        </div>
      </section>

      {/* Tracking Results */}
      {orderStatus === 'found' && mockOrder && (
        <section className="py-16">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
            {/* Order Summary */}
            <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-2xl font-bold text-gray-900">Order #{mockOrder.orderNumber}</h2>
                <span className={`px-4 py-2 rounded-full font-semibold ${getStatusColor(mockOrder.status)}`}>
                  {mockOrder.status.replace('-', ' ').toUpperCase()}
                </span>
              </div>
              
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Shipping Information</h3>
                  <p className="text-gray-600 mb-1"><strong>Carrier:</strong> {mockOrder.carrier}</p>
                  <p className="text-gray-600 mb-1"><strong>Estimated Delivery:</strong> {mockOrder.estimatedDelivery}</p>
                  <div className="flex items-start space-x-2 mt-2">
                    <MapPin className="w-5 h-5 text-gray-400 mt-0.5" />
                    <div>
                      <p className="text-gray-600 font-medium">Delivery Address:</p>
                      <p className="text-gray-600">{mockOrder.shippingAddress}</p>
                    </div>
                  </div>
                </div>
                
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Order Items</h3>
                  <div className="space-y-2">
                    {mockOrder.items.map((item: any, index: number) => (
                      <div key={index} className="flex justify-between text-sm">
                        <span className="text-gray-600">{item.quantity}x {item.name}</span>
                        <span className="font-medium">${item.price}</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>

            {/* Tracking Timeline */}
            <div className="bg-white rounded-2xl shadow-lg p-6">
              <h3 className="text-2xl font-bold text-gray-900 mb-6">Delivery Timeline</h3>
              
              <div className="space-y-6">
                {mockOrder.trackingSteps.map((step: any, index: number) => (
                  <div key={index} className="flex items-start space-x-4">
                    <div className={`flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center ${
                      step.completed ? 'bg-green-100' : 'bg-gray-100'
                    }`}>
                      {getStatusIcon(step.status, step.completed)}
                    </div>
                    
                    <div className="flex-1">
                      <div className="flex items-center justify-between">
                        <h4 className={`text-lg font-semibold ${step.completed ? 'text-gray-900' : 'text-gray-500'}`}>
                          {step.title}
                        </h4>
                        {step.date && (
                          <span className="text-sm text-gray-500">{step.date}</span>
                        )}
                      </div>
                      <p className={`text-sm mt-1 ${step.completed ? 'text-gray-600' : 'text-gray-400'}`}>
                        {step.description}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </section>
      )}

      {/* Not Found */}
      {orderStatus === 'not-found' && (
        <section className="py-16">
          <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <div className="bg-white rounded-2xl shadow-lg p-8">
              <Package className="w-16 h-16 text-gray-400 mx-auto mb-4" />
              <h2 className="text-2xl font-bold text-gray-900 mb-4">Order Not Found</h2>
              <p className="text-gray-600 mb-6">
                We couldn't find an order with that tracking number. Please check the number and try again.
              </p>
              <button
                onClick={() => {
                  setOrderStatus('idle');
                  setTrackingNumber('');
                }}
                className="bg-green-600 text-white px-6 py-3 rounded-full font-semibold hover:bg-green-700 transition"
              >
                Try Again
              </button>
            </div>
          </div>
        </section>
      )}

      {/* Help Section */}
      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Need Help?</h2>
          <p className="text-gray-600 mb-8">
            Can't find your tracking number or having issues with your order?
          </p>
          
          <div className="grid md:grid-cols-2 gap-6">
            <div className="bg-gray-50 p-6 rounded-2xl">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Check Your Email</h3>
              <p className="text-gray-600 mb-4">
                Your tracking number was sent to your email when your order was confirmed.
              </p>
              <button className="text-green-600 font-semibold hover:text-green-700 transition">
                Check Email →
              </button>
            </div>
            
            <div className="bg-gray-50 p-6 rounded-2xl">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Contact Support</h3>
              <p className="text-gray-600 mb-4">
                Our customer service team is here to help you with any questions.
              </p>
              <button className="text-green-600 font-semibold hover:text-green-700 transition">
                Get Help →
              </button>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default TrackOrderPage;
