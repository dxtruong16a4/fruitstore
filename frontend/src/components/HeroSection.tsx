import React from 'react';

interface HeroSectionProps {
  title: string;
  subtitle?: string;
  backgroundImage?: string;
  showSearch?: boolean;
  searchPlaceholder?: string;
  onSearch?: (query: string) => void;
  className?: string;
}

const HeroSection: React.FC<HeroSectionProps> = ({
  title,
  subtitle,
  backgroundImage: _backgroundImage, // Keep for future use
  showSearch = false,
  searchPlaceholder = "Tìm kiếm...",
  onSearch,
  className = ''
}) => {
  return (
    <section className={`py-20 bg-gradient-to-r from-green-600 to-emerald-600 text-white ${className}`}>
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h1 className="text-5xl font-bold mb-6">🍎 {title}</h1>
        {subtitle && (
          <p className="text-xl text-green-100 leading-relaxed mb-8">
            {subtitle}
          </p>
        )}
        
        {showSearch && onSearch && (
          <div className="max-w-2xl mx-auto">
            <div className="relative">
              <input
                type="text"
                placeholder={searchPlaceholder}
                onChange={(e) => onSearch(e.target.value)}
                className="w-full pl-4 pr-4 py-4 rounded-full text-white placeholder-gray-300 bg-gray-800 focus:outline-none focus:ring-4 focus:ring-green-300"
              />
            </div>
          </div>
        )}
      </div>
    </section>
  );
};

export default HeroSection;
