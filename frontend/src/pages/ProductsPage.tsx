import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Search, Filter, Grid, List, Loader2, X } from 'lucide-react';
import { productApi, type Product } from '../api/productApi';
import { categoryApi, type Category } from '../api/categoryApi';
import { useAppSelector, useAppDispatch } from '../redux';
import { addToCart } from '../redux/slices/cartSlice';
import { ProductGrid } from '../components';
import ProductDetailModal from '../components/ProductDetailModal';
import ToastContainer from '../components/ToastContainer';
import { useToast } from '../hooks/useToast';
import { PageLayout } from '../components/layout';

const ProductsPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [searchParams] = useSearchParams();
  
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const { toasts, removeToast, showSuccess, showError } = useToast();
  
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedSearchQuery, setDebouncedSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [sortBy, setSortBy] = useState<'name' | 'price' | 'createdAt'>('createdAt');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
  const [priceRange, setPriceRange] = useState<{ min: number; max: number }>({ min: 0, max: 1000000 });
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchLoading, setSearchLoading] = useState(false);
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
        }

        // Fetch products with filters
        await fetchProducts();
        
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // Debounce search query
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchQuery(searchQuery);
    }, 500); // 500ms delay

    return () => clearTimeout(timer);
  }, [searchQuery]);

  useEffect(() => {
    // Handle category filter from URL params
    const categoryId = searchParams.get('category');
    if (categoryId) {
      setSelectedCategory(parseInt(categoryId));
    }
  }, [searchParams]);

  useEffect(() => {
    // Refetch products when filters change
    fetchProducts();
  }, [selectedCategory, sortBy, sortDirection, priceRange, currentPage, debouncedSearchQuery]);

  const fetchProducts = async () => {
    try {
      setSearchLoading(true);
      const filters = {
        keyword: debouncedSearchQuery || undefined,
        categoryId: selectedCategory || undefined,
        minPrice: priceRange.min > 0 ? priceRange.min : undefined,
        maxPrice: priceRange.max < 1000000 ? priceRange.max : undefined,
        page: currentPage,
        size: 12,
        sortBy,
        sortDirection,
        isActive: true
      };

      const response = await productApi.searchProducts(filters);
      if (response.success) {
        setProducts(response.data.products || []);
        setTotalPages(response.data.totalPages || 0);
      }
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setSearchLoading(false);
    }
  };

  const handleSearch = () => {
    setCurrentPage(0);
    fetchProducts();
  };

  const handleClearSearch = () => {
    setSearchQuery('');
    setDebouncedSearchQuery('');
    setCurrentPage(0);
  };

  const handleSearchInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
    setCurrentPage(0); // Reset to first page when searching
  };

  const handleCategoryFilter = (categoryId: number | null) => {
    setSelectedCategory(categoryId);
    setCurrentPage(0);
  };

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

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-green-50 to-white flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-green-600 animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Đang tải sản phẩm...</p>
        </div>
      </div>
    );
  }

  return (
    <PageLayout
      title="Sản phẩm của chúng tôi"
      subtitle="Quả trái cây tươi được chọn cẩn thận cho chất lượng và vị"
      showHero={true}
    >

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Sidebar Filters */}
          <div className="lg:w-1/4">
            <div className="bg-white rounded-2xl shadow-md p-6 sticky top-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-6 flex items-center">
                <Filter className="w-5 h-5 mr-2" />
                Bộ lọc
              </h3>

              {/* Search */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">Tìm kiếm</label>
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={handleSearchInputChange}
                    onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                    placeholder="Tìm kiếm sản phẩm..."
                    className="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                  {searchQuery && (
                    <button
                      onClick={handleClearSearch}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  )}
                </div>
                {searchQuery && (
                  <div className="mt-2 text-sm text-gray-600">
                    {searchLoading ? (
                      <div className="flex items-center">
                        <Loader2 className="w-4 h-4 animate-spin mr-2" />
                        Đang tìm kiếm...
                      </div>
                    ) : (
                      <span>Đang tìm kiếm: "{searchQuery}"</span>
                    )}
                  </div>
                )}
              </div>

              {/* Categories */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-3">Danh mục</label>
                <div className="space-y-2">
                  <button
                    onClick={() => handleCategoryFilter(null)}
                    className={`w-full text-left px-3 py-2 rounded-lg transition ${
                      selectedCategory === null 
                        ? 'bg-green-100 text-green-800' 
                        : 'hover:bg-gray-100'
                    }`}
                  >
                    Tất cả danh mục
                  </button>
                  {categories.map((category) => (
                    <button
                      key={category.categoryId || category.id}
                      onClick={() => handleCategoryFilter(category.categoryId || category.id || 0)}
                      className={`w-full text-left px-3 py-2 rounded-lg transition flex items-center ${
                        selectedCategory === (category.categoryId || category.id)
                          ? 'bg-green-100 text-green-800' 
                          : 'hover:bg-gray-100'
                      }`}
                    >
                      <span className="mr-2">{getCategoryIcon(category.name)}</span>
                      {category.name}
                    </button>
                  ))}
                </div>
              </div>

              {/* Sort */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">Sắp xếp theo</label>
                <select
                  value={`${sortBy}-${sortDirection}`}
                  onChange={(e) => {
                    const [field, direction] = e.target.value.split('-');
                    setSortBy(field as 'name' | 'price' | 'createdAt');
                    setSortDirection(direction as 'asc' | 'desc');
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                >
                  <option value="createdAt-desc">Mới nhất</option>
                  <option value="createdAt-asc">Cũ nhất</option>
                  <option value="name-asc">Tên A-Z</option>
                  <option value="name-desc">Tên Z-A</option>
                  <option value="price-asc">Giá thấp đến cao</option>
                  <option value="price-desc">Giá cao đến thấp</option>
                </select>
              </div>

              {/* Price Range */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">Khoảng giá</label>
                <div className="space-y-2">
                  <input
                    type="number"
                    placeholder="Giá thấp nhất"
                    value={priceRange.min || ''}
                    onChange={(e) => setPriceRange(prev => ({ ...prev, min: parseInt(e.target.value) || 0 }))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                  <input
                    type="number"
                    placeholder="Giá cao nhất"
                    value={priceRange.max === 1000000 ? '' : priceRange.max}
                    onChange={(e) => setPriceRange(prev => ({ ...prev, max: parseInt(e.target.value) || 1000000 }))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 placeholder-gray-400 focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Products Grid */}
          <div className="lg:w-3/4">
            {/* Toolbar */}
            <div className="flex justify-between items-center mb-6">
              <div className="flex flex-col">
                <p className="text-gray-600">
                  {searchQuery ? (
                    <>
                      {searchLoading ? (
                        'Đang tìm kiếm...'
                      ) : (
                        `Tìm thấy ${products.length} sản phẩm cho "${searchQuery}"`
                      )}
                    </>
                  ) : (
                    `Hiển thị ${products.length} sản phẩm`
                  )}
                </p>
                {selectedCategory && (
                  <p className="text-sm text-gray-500">
                    Lọc theo: {categories.find(c => (c.categoryId || c.id) === selectedCategory)?.name}
                  </p>
                )}
              </div>
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => setViewMode('grid')}
                  className={`p-2 rounded-lg transition ${
                    viewMode === 'grid' ? 'bg-green-100 text-green-600' : 'text-gray-400 hover:text-gray-600'
                  }`}
                >
                  <Grid className="w-5 h-5" />
                </button>
                <button
                  onClick={() => setViewMode('list')}
                  className={`p-2 rounded-lg transition ${
                    viewMode === 'list' ? 'bg-green-100 text-green-600' : 'text-gray-400 hover:text-gray-600'
                  }`}
                >
                  <List className="w-5 h-5" />
                </button>
              </div>
            </div>

            {/* Products */}
            {products.length > 0 ? (
              <ProductGrid
                products={products}
                onAddToCart={handleAddToCart}
                onProductClick={handleProductClick}
                viewMode={viewMode}
                showAddToCart={true}
              />
            ) : (
              <div className="text-center py-12">
                <div className="text-gray-400 mb-4">
                  <Search className="w-16 h-16 mx-auto" />
                </div>
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  {searchQuery ? 'Không tìm thấy sản phẩm' : 'Không có sản phẩm nào'}
                </h3>
                <p className="text-gray-600 mb-4">
                  {searchQuery 
                    ? `Không tìm thấy sản phẩm nào phù hợp với "${searchQuery}". Vui lòng điều chỉnh từ khóa tìm kiếm của bạn.`
                    : 'Không có sản phẩm nào có sẵn tại thời điểm hiện tại.'
                  }
                </p>
                {searchQuery && (
                  <button
                    onClick={handleClearSearch}
                    className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition"
                  >
                    Xóa tìm kiếm
                  </button>
                )}
              </div>
            )}

            {/* Pagination */}
            {totalPages > 1 && (
              <div className="flex justify-center mt-8">
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
                    disabled={currentPage === 0}
                    className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    Previous
                  </button>
                  
                  {Array.from({ length: totalPages }, (_, i) => (
                    <button
                      key={i}
                      onClick={() => setCurrentPage(i)}
                      className={`px-4 py-2 rounded-lg ${
                        currentPage === i
                          ? 'bg-green-600 text-white'
                          : 'border border-gray-300 hover:bg-gray-50'
                      }`}
                    >
                      {i + 1}
                    </button>
                  ))}
                  
                  <button
                    onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
                    disabled={currentPage === totalPages - 1}
                    className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    Next
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

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

export default ProductsPage;
