import React from 'react';
import { Truck, Clock, MapPin, Package, Shield, Zap, Globe } from 'lucide-react';
import { PageLayout } from '../components/layout';

const ShippingInfoPage: React.FC = () => {
  const shippingOptions = [
    {
      name: 'Same-Day Delivery',
      icon: <Zap className="w-8 h-8 text-yellow-600" />,
      description: 'Get your fresh fruits delivered the same day',
      time: '2-4 hours',
      cost: '$9.99',
      freeThreshold: 'Orders over $75',
      features: ['Order before 2 PM', 'Temperature-controlled delivery', 'Real-time tracking']
    },
    {
      name: 'Next-Day Delivery',
      icon: <Truck className="w-8 h-8 text-blue-600" />,
      description: 'Standard delivery for next business day',
      time: '24 hours',
      cost: '$5.99',
      freeThreshold: 'Orders over $50',
      features: ['Order by 6 PM', 'Scheduled delivery windows', 'Quality guarantee']
    },
    {
      name: 'Scheduled Delivery',
      icon: <Clock className="w-8 h-8 text-purple-600" />,
      description: 'Choose your preferred delivery date and time',
      time: 'Flexible',
      cost: '$3.99',
      freeThreshold: 'Orders over $30',
      features: ['Pick your time slot', 'Weekly subscriptions available', 'Perfect for planning']
    },
    {
      name: 'Free Standard',
      icon: <Package className="w-8 h-8 text-green-600" />,
      description: 'Complimentary delivery on qualifying orders',
      time: '2-3 business days',
      cost: 'FREE',
      freeThreshold: 'Orders over $25',
      features: ['No minimum order', 'Eco-friendly packaging', 'Standard tracking']
    }
  ];

  const deliveryAreas = [
    { city: 'New York', state: 'NY', status: 'Available', coverage: 'All boroughs' },
    { city: 'Los Angeles', state: 'CA', status: 'Available', coverage: 'LA County' },
    { city: 'Chicago', state: 'IL', status: 'Available', coverage: 'Cook County' },
    { city: 'Houston', state: 'TX', status: 'Available', coverage: 'Harris County' },
    { city: 'Phoenix', state: 'AZ', status: 'Available', coverage: 'Maricopa County' },
    { city: 'Philadelphia', state: 'PA', status: 'Coming Soon', coverage: 'Limited areas' },
    { city: 'San Antonio', state: 'TX', status: 'Available', coverage: 'Bexar County' },
    { city: 'San Diego', state: 'CA', status: 'Available', coverage: 'San Diego County' }
  ];

  const packagingInfo = [
    {
      title: 'Temperature Control',
      description: 'All fresh fruits are packed with ice packs and insulated containers to maintain optimal temperature.',
      icon: '❄️'
    },
    {
      title: 'Eco-Friendly Materials',
      description: 'We use recyclable and biodegradable packaging materials whenever possible.',
      icon: '🌱'
    },
    {
      title: 'Protective Packaging',
      description: 'Each fruit is carefully wrapped to prevent bruising and maintain freshness during transit.',
      icon: '🛡️'
    },
    {
      title: 'Quality Inspection',
      description: 'Every order is inspected before packaging to ensure only the best quality fruits are sent.',
      icon: '✅'
    }
  ];

  return (
    <PageLayout
      title="Shipping Information"
      subtitle="Fast, reliable delivery of fresh fruits to your doorstep."
      showHero={true}
      navigationProps={{ title: "Shipping Information" }}
    >

      {/* Shipping Options */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Delivery Options</h2>
            <p className="text-gray-600 text-lg">Choose the delivery option that works best for you</p>
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
                    <span className="text-gray-600">Delivery Time:</span>
                    <span className="font-semibold text-gray-900">{option.time}</span>
                  </div>
                  <div className="flex justify-between items-center py-2 border-b border-gray-100">
                    <span className="text-gray-600">Cost:</span>
                    <span className="font-semibold text-green-600">{option.cost}</span>
                  </div>
                  <div className="flex justify-between items-center py-2">
                    <span className="text-gray-600">Free on:</span>
                    <span className="font-semibold text-gray-900">{option.freeThreshold}</span>
                  </div>
                </div>

                <div>
                  <h4 className="font-semibold text-gray-900 mb-3">Features:</h4>
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
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Delivery Areas</h2>
            <p className="text-gray-600 text-lg">We deliver to major cities across the United States</p>
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
            <p className="text-gray-600 mb-4">Don't see your city? We're expanding rapidly!</p>
            <button className="bg-green-600 text-white px-6 py-3 rounded-full font-semibold hover:bg-green-700 transition">
              Request Delivery to Your Area
            </button>
          </div>
        </div>
      </section>

      {/* Packaging & Care */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Packaging & Care</h2>
            <p className="text-gray-600 text-lg">How we ensure your fruits arrive fresh and perfect</p>
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
                <h3 className="text-2xl font-bold text-gray-900">Quality Guarantee</h3>
              </div>
              <p className="text-gray-600 leading-relaxed">
                We guarantee the quality of your fruits upon delivery. If you're not satisfied with the freshness 
                or quality of any item, contact us within 24 hours for a full refund or replacement.
              </p>
            </div>

            <div className="bg-blue-50 rounded-2xl p-8">
              <div className="flex items-center space-x-3 mb-4">
                <Globe className="w-8 h-8 text-blue-600" />
                <h3 className="text-2xl font-bold text-gray-900">Environmental Commitment</h3>
              </div>
              <p className="text-gray-600 leading-relaxed">
                We're committed to sustainable delivery practices. Our packaging is eco-friendly, 
                and we optimize delivery routes to minimize our carbon footprint.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center">Shipping FAQ</h2>
          
          <div className="space-y-6">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Can I track my delivery in real-time?</h3>
              <p className="text-gray-600">Yes! Once your order is shipped, you'll receive a tracking link to monitor your delivery in real-time.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">What if I'm not home when my order arrives?</h3>
              <p className="text-gray-600">For perishable items, we'll leave your order in a cool, shaded area and send you a photo confirmation. You can also schedule delivery for a specific time.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Do you deliver on weekends?</h3>
              <p className="text-gray-600">Yes! We offer weekend delivery for same-day and next-day options. Standard delivery operates Monday through Friday.</p>
            </div>
            
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Can I change my delivery address after placing an order?</h3>
              <p className="text-gray-600">You can change your delivery address up to 2 hours before your scheduled delivery time by contacting our customer service.</p>
            </div>
          </div>
        </div>
      </section>

    </PageLayout>
  );
};

export default ShippingInfoPage;
