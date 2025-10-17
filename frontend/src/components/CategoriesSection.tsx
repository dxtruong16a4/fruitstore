import React from 'react';
import { useNavigate } from 'react-router-dom';

interface Category {
  categoryId?: number;
  id?: number;
  name: string;
  description?: string;
}

interface CategoriesSectionProps {
  categories: Category[];
  title?: string;
  subtitle?: string;
  maxCategories?: number;
  className?: string;
}

const CategoriesSection: React.FC<CategoriesSectionProps> = ({
  categories,
  title = "Mua theo danh mục",
  subtitle = "Khám phá sự đa dạng của trái cây tươi ngon",
  maxCategories = 4,
  className = ''
}) => {
  const navigate = useNavigate();

  const getCategoryIcon = (categoryName: string) => {
    const name = categoryName.toLowerCase();
    if (name.includes('berry') || name.includes('strawberry') || name.includes('blueberry')) return '🍓';
    if (name.includes('citrus') || name.includes('orange') || name.includes('lemon')) return '🍊';
    if (name.includes('tropical') || name.includes('mango') || name.includes('banana')) return '🥭';
    if (name.includes('apple')) return '🍎';
    if (name.includes('grape')) return '🍇';
    if (name.includes('peach')) return '🍑';
    if (name.includes('quà')) return '🎁';
    if (name.includes('khẩu')) return '✈️';
    if (name.includes('sấy')) return '🍯';
    if (name.includes('tươi')) return '🥬';
    return '🍎';
  };

  const getCategoryColor = (index: number) => {
    const colors = [
      'bg-red-50 hover:bg-red-100',
      'bg-orange-50 hover:bg-orange-100', 
      'bg-yellow-50 hover:bg-yellow-100',
      'bg-rose-50 hover:bg-rose-100',
      'bg-green-50 hover:bg-green-100',
      'bg-blue-50 hover:bg-blue-100'
    ];
    return colors[index % colors.length];
  };

  const displayCategories = categories.slice(0, maxCategories);

  return (
    <section className={`py-16 ${className}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-bold text-gray-900 mb-4">{title}</h2>
          <p className="text-gray-600 text-lg">{subtitle}</p>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
          {displayCategories.map((category, index) => (
            <button
              key={category.categoryId || category.id}
              className={`${getCategoryColor(index)} p-8 rounded-2xl transition transform hover:scale-105 shadow-sm hover:shadow-md`}
              onClick={() => navigate(`/products?category=${category.categoryId || category.id}`)}
            >
              <div className="text-5xl mb-3">{getCategoryIcon(category.name)}</div>
              <h3 className="text-lg font-semibold text-gray-800">{category.name}</h3>
            </button>
          ))}
        </div>
      </div>
    </section>
  );
};

export default CategoriesSection;
