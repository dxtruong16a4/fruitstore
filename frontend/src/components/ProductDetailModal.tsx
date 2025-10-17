import React from 'react';
import { X, ShoppingCart, Heart, Star, Truck, Shield, RotateCcw } from 'lucide-react';
import { type Product } from '../api/productApi';

interface ProductDetailModalProps {
  product: Product | null;
  isOpen: boolean;
  onClose: () => void;
  onAddToCart: (product: Product) => void;
  onToggleFavorite?: (product: Product) => void;
}

const ProductDetailModal: React.FC<ProductDetailModalProps> = ({
  product,
  isOpen,
  onClose,
  onAddToCart,
  onToggleFavorite
}) => {
  if (!isOpen || !product) return null;

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const getStockStatus = (stock: number) => {
    if (stock === 0) return { text: 'Out of Stock', color: 'text-red-600' };
    if (stock < 10) return { text: 'Low Stock', color: 'text-orange-600' };
    return { text: 'In Stock', color: 'text-green-600' };
  };

  const stockStatus = getStockStatus(product.stockQuantity || 0);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        <div className="flex flex-col lg:flex-row">
          {/* Product Image */}
          <div className="lg:w-1/2 p-6">
            <div className="relative">
              <img
                src={product.imageUrl || 'https://images.pexels.com/photos/672101/pexels-photo-672101.jpeg?auto=compress&cs=tinysrgb&w=800'}
                alt={product.name}
                className="w-full h-96 object-cover rounded-xl"
              />
              <div className="absolute top-4 left-4 bg-green-600 text-white px-3 py-1 rounded-full text-sm font-semibold">
                Fresh
              </div>
              {onToggleFavorite && (
                <button 
                  onClick={() => onToggleFavorite(product)}
                  className="absolute top-4 right-4 p-2 bg-white rounded-full shadow-md hover:bg-red-50 transition"
                >
                  <Heart className="w-5 h-5 text-gray-600 hover:text-red-500" />
                </button>
              )}
            </div>
          </div>

          {/* Product Details */}
          <div className="lg:w-1/2 p-6">
            <div className="flex justify-between items-start mb-4">
              <div>
                <div className="text-sm text-green-600 font-semibold mb-2">
                  {product.categoryName || 'Fruits'}
                </div>
                <h1 className="text-3xl font-bold text-gray-900 mb-4">{product.name}</h1>
              </div>
              <button
                onClick={onClose}
                className="p-2 hover:bg-gray-100 rounded-full transition"
              >
                <X className="w-6 h-6 text-gray-500" />
              </button>
            </div>

            {/* Rating */}
            <div className="flex items-center mb-4">
              <div className="flex items-center">
                {[...Array(5)].map((_, i) => (
                  <Star key={i} className="w-5 h-5 text-yellow-400 fill-current" />
                ))}
              </div>
              <span className="text-sm text-gray-600 ml-2">4.5 (128 reviews)</span>
            </div>

            {/* Price */}
            <div className="mb-6">
              <span className="text-4xl font-bold text-green-600">
                {formatPrice(product.price)}
              </span>
              <span className="text-sm text-gray-500 ml-2">per kg</span>
            </div>

            {/* Stock Status */}
            <div className="mb-6">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">Availability:</span>
                <span className={`text-sm font-semibold ${stockStatus.color}`}>
                  {stockStatus.text} ({product.stockQuantity || 0} units)
                </span>
              </div>
            </div>

            {/* Description */}
            <div className="mb-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Description</h3>
              <p className="text-gray-600 leading-relaxed">{product.description}</p>
            </div>

            {/* Features */}
            <div className="mb-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-3">Why Choose This Product?</h3>
              <div className="space-y-3">
                <div className="flex items-center">
                  <Truck className="w-5 h-5 text-green-600 mr-3" />
                  <span className="text-sm text-gray-600">Free delivery on orders over $50</span>
                </div>
                <div className="flex items-center">
                  <Shield className="w-5 h-5 text-green-600 mr-3" />
                  <span className="text-sm text-gray-600">100% fresh guarantee</span>
                </div>
                <div className="flex items-center">
                  <RotateCcw className="w-5 h-5 text-green-600 mr-3" />
                  <span className="text-sm text-gray-600">Easy returns within 7 days</span>
                </div>
              </div>
            </div>

            {/* Add to Cart Button */}
            <button 
              onClick={() => onAddToCart(product)}
              disabled={product.stockQuantity === 0}
              className={`w-full py-4 rounded-lg font-semibold transition flex items-center justify-center space-x-2 ${
                product.stockQuantity === 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-green-600 text-white hover:bg-green-700'
              }`}
            >
              <ShoppingCart className="w-5 h-5" />
              <span>
                {product.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
              </span>
            </button>

            {/* Additional Info */}
            <div className="mt-4 text-center">
              <p className="text-xs text-gray-500">
                Need help? Contact our support team for assistance.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailModal;
