import React from 'react';
import { ShoppingCart, Heart, Star } from 'lucide-react';
import { type Product } from '../api/productApi';

interface ProductCardProps {
  product: Product;
  onAddToCart: (product: Product) => void;
  onToggleFavorite?: (product: Product) => void;
  onProductClick?: (product: Product) => void;
  viewMode?: 'grid' | 'list';
  showAddToCart?: boolean;
  className?: string;
}

const ProductCard: React.FC<ProductCardProps> = ({
  product,
  onAddToCart,
  onToggleFavorite,
  onProductClick,
  viewMode = 'grid',
  showAddToCart = true,
  className = ''
}) => {
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const cardClasses = `bg-white rounded-2xl shadow-md hover:shadow-xl transition transform hover:-translate-y-1 ${
    viewMode === 'list' ? 'flex' : ''
  } ${className}`;

  const imageClasses = `relative overflow-hidden ${
    viewMode === 'list' ? 'w-48 h-48 flex-shrink-0' : ''
  }`;

  const imageSizeClasses = `object-cover group-hover:scale-110 transition duration-500 ${
    viewMode === 'list' ? 'w-full h-full' : 'w-full h-64'
  }`;

  const contentClasses = `p-6 ${viewMode === 'list' ? 'flex-1' : ''}`;

  const handleCardClick = () => {
    if (onProductClick) {
      onProductClick(product);
    }
  };

  const handleAddToCartClick = (e: React.MouseEvent) => {
    e.stopPropagation(); // Prevent card click when clicking Add to Cart button
    onAddToCart(product);
  };

  return (
    <div className={cardClasses} onClick={handleCardClick} style={{ cursor: onProductClick ? 'pointer' : 'default' }}>
      <div className={imageClasses}>
        <img
          src={product.imageUrl || 'https://images.pexels.com/photos/672101/pexels-photo-672101.jpeg?auto=compress&cs=tinysrgb&w=400'}
          alt={product.name}
          className={imageSizeClasses}
        />
        
        {onToggleFavorite && (
          <button 
            onClick={(e) => {
              e.stopPropagation(); // Prevent card click when clicking favorite button
              onToggleFavorite(product);
            }}
            className="absolute top-4 right-4 p-2 bg-white rounded-full shadow-md opacity-0 group-hover:opacity-100 transition hover:bg-red-50"
          >
            <Heart className="w-5 h-5 text-gray-600 hover:text-red-500" />
          </button>
        )}
        
        <div className="absolute top-4 left-4 bg-green-600 text-white px-3 py-1 rounded-full text-sm font-semibold">
          Fresh
        </div>
      </div>

      <div className={contentClasses}>
        <div className="text-sm text-green-600 font-semibold mb-1">
          {product.categoryName || 'Fruits'}
        </div>
        
        <h3 className="text-xl font-bold text-gray-900 mb-2">{product.name}</h3>
        
        <p className="text-gray-600 text-sm mb-3 line-clamp-2">{product.description}</p>
        
        <div className="flex items-center justify-between mb-4">
          <span className="text-2xl font-bold text-green-600">
            {formatPrice(product.price)}
          </span>
          <div className="flex items-center">
            <Star className="w-4 h-4 text-yellow-400 fill-current" />
            <span className="text-sm text-gray-600 ml-1">4.5</span>
          </div>
        </div>

        {showAddToCart && (
          <button 
            onClick={handleAddToCartClick}
            className="w-full bg-green-600 text-white py-3 rounded-lg font-semibold hover:bg-green-700 transition flex items-center justify-center space-x-2"
          >
            <ShoppingCart className="w-5 h-5" />
            <span>Add to Cart</span>
          </button>
        )}
      </div>
    </div>
  );
};

export default ProductCard;
