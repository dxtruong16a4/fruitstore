import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Loader2 } from 'lucide-react';
import { productApi, type Product } from '../api/productApi';
import { categoryApi, type Category } from '../api/categoryApi';
import { useAppSelector, useAppDispatch } from '../redux';
import { addToCart } from '../redux/slices/cartSlice';
import { ProductGrid, FeaturesSection, CategoriesSection, NewsletterSection } from '../components';
import ProductDetailModal from '../components/ProductDetailModal';
import ToastContainer from '../components/ToastContainer';
import { useToast } from '../hooks/useToast';
import { PageLayout } from '../components/layout';

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const { toasts, removeToast, showSuccess, showError } = useToast();
  
  const [featuredProducts, setFeaturedProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Fetch categories
        const categoriesResponse = await categoryApi.getActiveCategories();
        if (categoriesResponse.success) {
          setCategories(categoriesResponse.data);
        } else {
          // Set mock categories for testing
          setCategories([
            { categoryId: 1, name: 'Apples', description: 'Fresh apples', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 2, name: 'Tropical', description: 'Tropical fruits', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 3, name: 'Berries', description: 'Fresh berries', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 4, name: 'Citrus', description: 'Citrus fruits', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() }
          ]);
        }

        // Fetch featured products (first 6 active products)
        const productsResponse = await productApi.searchProducts({
          isActive: true,
          page: 0,
          size: 6,
          sortBy: 'createdAt',
          sortDirection: 'desc'
        });
        
        if (productsResponse.success) {
          // API returns products in data.products
          const products = productsResponse.data.products || productsResponse.data.content || productsResponse.data || [];
          setFeaturedProducts(products);
        } else {
          // Set mock products if API fails
          setFeaturedProducts([
            {
              productId: 1,
              name: 'Fresh Apples',
              description: 'Crisp and sweet organic apples from local farms',
              price: 4.99,
              imageUrl: 'https://images.pexels.com/photos/672101/pexels-photo-672101.jpeg?auto=compress&cs=tinysrgb&w=400',
              stockQuantity: 50,
              categoryId: 1,
              categoryName: 'Apples',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            },
            {
              productId: 2,
              name: 'Organic Bananas',
              description: 'Perfectly ripe organic bananas',
              price: 3.99,
              imageUrl: 'https://images.pexels.com/photos/61127/pexels-photo-61127.jpeg?auto=compress&cs=tinysrgb&w=400',
              stockQuantity: 30,
              categoryId: 2,
              categoryName: 'Tropical',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            },
            {
              productId: 3,
              name: 'Mixed Berries',
              description: 'Fresh strawberries, blueberries, and raspberries',
              price: 8.99,
              imageUrl: 'https://images.pexels.com/photos/143133/pexels-photo-143133.jpeg?auto=compress&cs=tinysrgb&w=400',
              stockQuantity: 20,
              categoryId: 3,
              categoryName: 'Berries',
              isActive: true,
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            }
          ]);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        // Set some mock data for testing
        setFeaturedProducts([
          {
            productId: 1,
            name: 'Fresh Apples',
            description: 'Crisp and sweet organic apples from local farms',
            price: 4.99,
            imageUrl: 'https://images.pexels.com/photos/672101/pexels-photo-672101.jpeg?auto=compress&cs=tinysrgb&w=400',
            stockQuantity: 50,
            categoryId: 1,
            categoryName: 'Apples',
            isActive: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          },
          {
            productId: 2,
            name: 'Organic Bananas',
            description: 'Perfectly ripe organic bananas',
            price: 3.99,
            imageUrl: 'https://images.pexels.com/photos/61127/pexels-photo-61127.jpeg?auto=compress&cs=tinysrgb&w=400',
            stockQuantity: 30,
            categoryId: 2,
            categoryName: 'Tropical',
            isActive: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          },
          {
            productId: 3,
            name: 'Mixed Berries',
            description: 'Fresh strawberries, blueberries, and raspberries',
            price: 8.99,
            imageUrl: 'https://images.pexels.com/photos/143133/pexels-photo-143133.jpeg?auto=compress&cs=tinysrgb&w=400',
            stockQuantity: 20,
            categoryId: 3,
            categoryName: 'Berries',
            isActive: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          }
        ]);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleAddToCart = (product: Product) => {
    try {
      if (!isAuthenticated) {
        showError('Authentication Required', 'Please sign in to add items to your cart');
        navigate('/login');
        return;
      }
      
      if (product.stockQuantity === 0) {
        showError('Out of Stock', `${product.name} is currently out of stock`);
        return;
      }
      
      dispatch(addToCart({
        productId: product.productId || product.id || 0,
        productName: product.name,
        productPrice: product.price,
        productImage: product.imageUrl || '',
        quantity: 1
      }));
      
      showSuccess('Added to Cart!', `${product.name} has been added to your cart`);
    } catch (error) {
      showError('Failed to Add Item', 'There was an error adding the item to your cart. Please try again.');
    }
  };

  const handleProductClick = (product: Product) => {
    setSelectedProduct(product);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedProduct(null);
  };


  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-green-50 to-white flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-green-600 animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Loading fresh fruits...</p>
        </div>
      </div>
    );
  }

  return (
    <PageLayout>
      {/* Hero Section */}
      <section className="relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div className="space-y-6 animate-fade-in">
              <div className="inline-block">
                <span className="bg-green-100 text-green-700 px-4 py-2 rounded-full text-sm font-semibold">
                  Fresh & Organic
                </span>
              </div>
              <h1 className="text-5xl md:text-6xl font-bold text-gray-900 leading-tight">
                Farm Fresh
                <span className="block bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
                  Fruits Delivered
                </span>
              </h1>
              <p className="text-xl text-gray-600 leading-relaxed">
                Experience the taste of nature with our hand-picked, organic fruits delivered fresh to your doorstep every day.
              </p>
              <div className="flex flex-wrap gap-4">
                <button 
                  className="bg-green-600 text-white px-8 py-4 rounded-full font-semibold hover:bg-green-700 transition transform hover:scale-105 shadow-lg hover:shadow-xl"
                  onClick={() => navigate('/products')}
                >
                  Shop Now
                </button>
                {!isAuthenticated && (
                  <button 
                    className="bg-white text-gray-700 px-8 py-4 rounded-full font-semibold hover:bg-gray-50 transition border-2 border-gray-200"
                    onClick={() => navigate('/login')}
                  >
                    Sign In
                  </button>
                )}
              </div>
            </div>

            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-r from-green-400 to-emerald-400 rounded-full blur-3xl opacity-20 animate-pulse"></div>
              <img
                src="https://images.pexels.com/photos/1132047/pexels-photo-1132047.jpeg?auto=compress&cs=tinysrgb&w=800"
                alt="Fresh fruits"
                className="relative rounded-3xl shadow-2xl w-full h-auto object-cover"
              />
            </div>
          </div>
        </div>
      </section>

      <FeaturesSection />

      {categories.length > 0 && (
        <CategoriesSection categories={categories} />
      )}

      {/* Featured Products */}
      {featuredProducts.length > 0 && (
        <section className="py-16 bg-white">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center mb-12">
              <div>
                <h2 className="text-4xl font-bold text-gray-900 mb-2">Featured Products</h2>
                <p className="text-gray-600 text-lg">Handpicked just for you</p>
              </div>
              <button 
                className="text-green-600 font-semibold hover:text-green-700 transition"
                onClick={() => navigate('/products')}
              >
                View All →
              </button>
            </div>

            <ProductGrid
              products={featuredProducts}
              onAddToCart={handleAddToCart}
              onProductClick={handleProductClick}
              viewMode="grid"
              showAddToCart={true}
            />
          </div>
        </section>
      )}

      <NewsletterSection />

      {/* Product Detail Modal */}
      <ProductDetailModal
        product={selectedProduct}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onAddToCart={handleAddToCart}
      />

      {/* Toast Notifications */}
      <ToastContainer
        toasts={toasts}
        onRemoveToast={removeToast}
      />
    </PageLayout>
  );
};

export default HomePage;
