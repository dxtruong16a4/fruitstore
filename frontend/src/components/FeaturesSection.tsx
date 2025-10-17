import React from 'react';
import { Truck, Shield, Clock } from 'lucide-react';

interface Feature {
  icon: React.ReactNode;
  title: string;
  description: string;
}

interface FeaturesSectionProps {
  features?: Feature[];
  className?: string;
}

const FeaturesSection: React.FC<FeaturesSectionProps> = ({
  features,
  className = ''
}) => {
  const defaultFeatures: Feature[] = [
    {
      icon: <Truck className="w-8 h-8 text-green-600" />,
      title: "Miễn phí giao hàng",
      description: "Trên 50.000 VNĐ"
    },
    {
      icon: <Shield className="w-8 h-8 text-green-600" />,
      title: "100% Trái cây tươi ngon",
      description: "Trái cây tươi ngon được chọn lọc, đóng gói và giao hàng tới cửa bạn mỗi ngày."
    },
    {
      icon: <Clock className="w-8 h-8 text-green-600" />,
      title: "24/7 Hỗ trợ",
      description: "Luôn sẵn sàng hỗ trợ bạn"
    }
  ];

  const featuresToShow = features || defaultFeatures;

  return (
    <section className={`py-16 bg-white ${className}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid md:grid-cols-3 gap-8">
          {featuresToShow.map((feature, index) => (
            <div key={index} className="text-center p-6 rounded-2xl hover:bg-green-50 transition group">
              <div className="inline-block p-4 bg-green-100 rounded-full mb-4 group-hover:scale-110 transition">
                {feature.icon}
              </div>
              <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
              <p className="text-gray-600">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default FeaturesSection;
