import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Loader2, Search } from 'lucide-react';
import { productApi, type Product } from '../api/productApi';
import { categoryApi, type Category } from '../api/categoryApi';
import { useAppSelector, useAppDispatch } from '../redux';
import { addToCart } from '../redux/slices/cartSlice';
import { ProductGrid, FeaturesSection, CategoriesSection, NewsletterSection, SearchBar } from '../components';
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
  const [searchResults, setSearchResults] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchLoading, setSearchLoading] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [isSearchMode, setIsSearchMode] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Lấy danh mục
        const categoriesResponse = await categoryApi.getActiveCategories();
        if (categoriesResponse.success) {
          setCategories(categoriesResponse.data);
        } else {
          // Đặt danh mục giả để kiểm tra
          setCategories([
            { categoryId: 1, name: 'Apples', description: 'Fresh apples', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 2, name: 'Tropical', description: 'Tropical fruits', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 3, name: 'Berries', description: 'Fresh berries', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
            { categoryId: 4, name: 'Citrus', description: 'Citrus fruits', isActive: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() }
          ]);
        }

        // Lấy sản phẩm nổi bật (6 sản phẩm hoạt động đầu tiên)
        const productsResponse = await productApi.searchProducts({
          isActive: true,
          page: 0,
          size: 6,
          sortBy: 'createdAt',
          sortDirection: 'desc'
        });
        
        if (productsResponse.success) {
          // API trả về sản phẩm trong data.products
          const products = productsResponse.data.products || productsResponse.data.content || productsResponse.data || [];
          setFeaturedProducts(products);
        } else {
          // Đặt sản phẩm giả nếu API thất bại
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
        console.error('Lỗi khi tải dữ liệu:', error);
        // Đặt một số dữ liệu giả để kiểm tra
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
        showError('Yêu cầu xác thực', 'Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng');
        navigate('/login');
        return;
      }
      
      if (product.stockQuantity === 0) {
        showError('Hết hàng', `${product.name} hiện đang hết hàng`);
        return;
      }
      
      dispatch(addToCart({
        productId: product.productId || product.id || 0,
        productName: product.name,
        productPrice: product.price,
        productImage: product.imageUrl || '',
        quantity: 1
      }));
      
      showSuccess('Đã thêm vào giỏ!', `${product.name} đã được thêm vào giỏ hàng của bạn`);
    } catch (error) {
      showError('Thêm sản phẩm thất bại', 'Đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng. Vui lòng thử lại.');
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

  const handleSearch = async (query: string) => {
    if (!query.trim()) return;
    
    try {
      setSearchLoading(true);
      setSearchQuery(query);
      setIsSearchMode(true);
      
      const response = await productApi.searchProducts({
        keyword: query,
        isActive: true,
        page: 0,
        size: 20,
        sortBy: 'name',
        sortDirection: 'asc'
      });
      
      if (response.success) {
        const products = response.data.products || response.data.content || response.data || [];
        setSearchResults(products);
      } else {
        showError('Tìm kiếm thất bại', 'Không thể tìm kiếm sản phẩm. Vui lòng thử lại.');
        setSearchResults([]);
      }
    } catch (error) {
      console.error('Lỗi tìm kiếm:', error);
      showError('Lỗi tìm kiếm', 'Đã xảy ra lỗi khi tìm kiếm sản phẩm.');
      setSearchResults([]);
    } finally {
      setSearchLoading(false);
    }
  };

  const handleClearSearch = () => {
    setSearchQuery('');
    setSearchResults([]);
    setIsSearchMode(false);
  };


  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-green-50 to-white flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-green-600 animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Đang tải trái cây tươi ngon...</p>
        </div>
      </div>
    );
  }

  return (
    <PageLayout>
      {/* Phần Hero */}
      <section className="relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div className="space-y-6 animate-fade-in">
              <div className="inline-block">
                <span className="bg-green-100 text-green-700 px-4 py-2 rounded-full text-sm font-semibold">
                  Trái cây tươi ngon
                </span>
              </div>
              <h1 className="text-5xl md:text-6xl font-bold text-gray-900 leading-tight">
                Trái cây tươi ngon
                <span className="block bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
                  Được giao hàng tới cửa bạn
                </span>
              </h1>
              <p className="text-xl text-gray-600 leading-relaxed">
                Trải nghiệm vị ngon của thiên nhiên với trái cây tươi ngon được chọn lọc, đóng gói và giao hàng tới cửa bạn mỗi ngày.
              </p>
              <div className="flex flex-wrap gap-4">
                <button 
                  className="bg-green-600 text-white px-8 py-4 rounded-full font-semibold hover:bg-green-700 transition transform hover:scale-105 shadow-lg hover:shadow-xl"
                  onClick={() => navigate('/products')}
                >
                  Mua ngay
                </button>
                {!isAuthenticated && (
                  <button 
                    className="bg-white text-gray-700 px-8 py-4 rounded-full font-semibold hover:bg-gray-50 transition border-2 border-gray-200"
                    onClick={() => navigate('/login')}
                  >
                    Đăng nhập
                  </button>
                )}
              </div>
            </div>

            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-r from-green-400 to-emerald-400 rounded-full blur-3xl opacity-20 animate-pulse"></div>
              <img
                src="https://images.pexels.com/photos/1132047/pexels-photo-1132047.jpeg?auto=compress&cs=tinysrgb&w=800"
                alt="Trái cây tươi ngon"
                className="relative rounded-3xl shadow-2xl w-full h-auto object-cover"
              />
            </div>
          </div>
        </div>
      </section>

      <FeaturesSection />

      {/* Phần tìm kiếm */}
      <section className="py-12 bg-gray-50">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Tìm kiếm sản phẩm</h2>
            <p className="text-gray-600 text-lg">Khám phá các loại trái cây tươi ngon nhất</p>
          </div>
          <SearchBar
            onSearch={handleSearch}
            onClear={handleClearSearch}
            placeholder="Nhập tên sản phẩm bạn muốn tìm..."
            className="max-w-2xl mx-auto"
          />
        </div>
      </section>

      {categories.length > 0 && (
        <CategoriesSection categories={categories} />
      )}

      {/* Kết quả tìm kiếm hoặc Sản phẩm nổi bật */}
      {isSearchMode ? (
        <section className="py-16 bg-white">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center mb-12">
              <div>
                <h2 className="text-4xl font-bold text-gray-900 mb-2">
                  Kết quả tìm kiếm cho "{searchQuery}"
                </h2>
                <p className="text-gray-600 text-lg">
                  {searchLoading ? 'Đang tìm kiếm trái cây tươi ngon...' : `Tìm thấy ${searchResults.length} sản phẩm`}
                </p>
              </div>
              <button 
                className="text-green-600 font-semibold hover:text-green-700 transition"
                onClick={handleClearSearch}
              >
                Xóa tìm kiếm
              </button>
            </div>

            {searchLoading ? (
              <div className="text-center py-12">
                <Loader2 className="w-12 h-12 text-green-600 animate-spin mx-auto mb-4" />
                <p className="text-gray-600">Đang tìm kiếm sản phẩm...</p>
              </div>
            ) : searchResults.length > 0 ? (
              <ProductGrid
                products={searchResults}
                onAddToCart={handleAddToCart}
                onProductClick={handleProductClick}
                viewMode="grid"
                showAddToCart={true}
              />
            ) : (
              <div className="text-center py-12">
                <div className="text-gray-400 mb-4">
                  <Search className="w-16 h-16 mx-auto" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">Không tìm thấy sản phẩm</h3>
                <p className="text-gray-600 mb-4">
                  Không có sản phẩm nào phù hợp với từ khóa "{searchQuery}"
                </p>
                <button
                  onClick={handleClearSearch}
                  className="text-green-600 font-semibold hover:text-green-700 transition"
                >
                  Xem tất cả sản phẩm
                </button>
              </div>
            )}
          </div>
        </section>
      ) : (
        /* Sản phẩm nổi bật */
        featuredProducts.length > 0 && (
          <section className="py-16 bg-white">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
              <div className="flex justify-between items-center mb-12">
                <div>
                  <h2 className="text-4xl font-bold text-gray-900 mb-2">Sản phẩm nổi bật</h2>
                  <p className="text-gray-600 text-lg">Được chọn lọc đặc biệt cho bạn</p>
                </div>
                <button 
                  className="text-green-600 font-semibold hover:text-green-700 transition"
                  onClick={() => navigate('/products')}
                >
                  Xem tất cả →
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
        )
      )}

      <NewsletterSection />

      {/* Modal chi tiết sản phẩm */}
      <ProductDetailModal
        product={selectedProduct}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onAddToCart={handleAddToCart}
      />

      {/* Thông báo Toast */}
      <ToastContainer
        toasts={toasts}
        onRemoveToast={removeToast}
      />
    </PageLayout>
  );
};

export default HomePage;
