import React from 'react';
import { type Product } from '../api/productApi';
import ProductCard from './ProductCard';

interface ProductGridProps {
  products: Product[];
  onAddToCart: (product: Product) => void;
  onToggleFavorite?: (product: Product) => void;
  viewMode?: 'grid' | 'list';
  showAddToCart?: boolean;
  className?: string;
}

const ProductGrid: React.FC<ProductGridProps> = ({
  products,
  onAddToCart,
  onToggleFavorite,
  viewMode = 'grid',
  showAddToCart = true,
  className = ''
}) => {
  const gridClasses = `grid gap-6 ${
    viewMode === 'grid' 
      ? 'grid-cols-1 sm:grid-cols-2 lg:grid-cols-3' 
      : 'grid-cols-1'
  } ${className}`;

  if (products.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500 text-lg">No products found</p>
      </div>
    );
  }

  return (
    <div className={gridClasses}>
      {products.map((product) => (
        <ProductCard
          key={product.productId || product.id}
          product={product}
          onAddToCart={onAddToCart}
          onToggleFavorite={onToggleFavorite}
          viewMode={viewMode}
          showAddToCart={showAddToCart}
        />
      ))}
    </div>
  );
};

export default ProductGrid;
