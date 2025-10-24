# 🏗️ ARCHITECTURE - FruitStore System

## Mục lục
- [Tổng quan kiến trúc](#tổng-quan-kiến-trúc)
- [Domain Models (Entities)](#domain-models-entities)
- [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
- [Repositories](#repositories)
- [Services](#services)
- [Controllers](#controllers)
- [Utils & Helpers](#utils--helpers)
- [Security & Configuration](#security--configuration)

---

## 🎯 Tổng quan kiến trúc

### Layered Architecture (DDD-inspired)

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (Controllers, DTOs, Exception)       │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Service Layer                   │
│    (Business Logic, Validation)         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Repository Layer                │
│    (Data Access, JPA Repositories)      │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Domain Layer                    │
│    (Entities, Value Objects)            │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Database (MySQL)                │
└─────────────────────────────────────────┘
```

### Design Principles
- **Separation of Concerns**: Mỗi layer có trách nhiệm riêng
- **Dependency Injection**: Sử dụng Spring's DI container
- **DTO Pattern**: Tách biệt domain model và API response
- **Repository Pattern**: Abstraction cho data access
- **RESTful API**: Thiết kế API theo chuẩn REST

---

## 📦 Domain Models (Entities)

### 1. User Entity
**Package**: `com.fruitstore.domain.user`
**Enums**: `UserRole` (CUSTOMER, ADMIN)

### 2. Category Entity
**Package**: `com.fruitstore.domain.product`

### 3. Product Entity
**Package**: `com.fruitstore.domain.product`

### 4. Cart Entity
**Package**: `com.fruitstore.domain.cart`

### 5. CartItem Entity
**Package**: `com.fruitstore.domain.cart`

### 6. Order Entity
**Package**: `com.fruitstore.domain.order`
**Enums**: `OrderStatus` (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

---

### 7. OrderItem Entity
**Package**: `com.fruitstore.domain.order`

### 8. Discount Entity
**Package**: `com.fruitstore.domain.discount`
**Enums**: `DiscountType` (PERCENTAGE, FIXED_AMOUNT)

### 9. DiscountUsage Entity
**Package**: `com.fruitstore.domain.discount`

---

## 📋 Data Transfer Objects (DTOs)

### Request DTOs

#### Auth DTOs (`dto.request.auth`)
- `LoginRequest` - email, password
- `RegisterRequest` - username, email, password, fullName, phone, address
- `ChangePasswordRequest` - oldPassword, newPassword

#### Product DTOs (`dto.request.product`)
- `CreateProductRequest` - name, description, price, stockQuantity, imageUrl, categoryId
- `UpdateProductRequest` - name, description, price, stockQuantity, imageUrl, isActive
- `ProductFilterRequest` - categoryId, minPrice, maxPrice, keyword, page, size

#### Category DTOs (`dto.request.category`)
- `CreateCategoryRequest` - name, description, imageUrl
- `UpdateCategoryRequest` - name, description, imageUrl, isActive

#### Cart DTOs (`dto.request.cart`)
- `AddToCartRequest` - productId, quantity
- `UpdateCartItemRequest` - quantity

#### Order DTOs (`dto.request.order`)
- `CreateOrderRequest` - shippingAddress, phone, notes, discountCode
- `UpdateOrderStatusRequest` - status

#### Discount DTOs (`dto.request.discount`)
- `CreateDiscountRequest` - code, description, discountType, discountValue, minOrderAmount, maxDiscountAmount, usageLimit, startDate, endDate
- `ValidateDiscountRequest` - code, orderAmount

---

### Response DTOs

#### Auth DTOs (`dto.response.auth`)
- `LoginResponse` - token, userId, username, email, fullName, role
- `UserProfileResponse` - userId, username, email, fullName, phone, address, role, createdAt

#### Product DTOs (`dto.response.product`)
- `ProductResponse` - productId, name, description, price, stockQuantity, imageUrl, category, isActive, createdAt
- `ProductListResponse` - products (List), totalPages, totalElements, currentPage
- `ProductSummaryResponse` - productId, name, price, imageUrl (for cart/order)

#### Category DTOs (`dto.response.category`)
- `CategoryResponse` - categoryId, name, description, imageUrl, isActive, productCount

#### Cart DTOs (`dto.response.cart`)
- `CartResponse` - cartId, items, totalPrice, totalItems
- `CartItemResponse` - cartItemId, product, quantity, subtotal

#### Order DTOs (`dto.response.order`)
- `OrderResponse` - orderId, user, items, totalAmount, status, shippingAddress, phone, notes, orderDate
- `OrderListResponse` - orders (List), totalPages, totalElements
- `OrderItemResponse` - orderItemId, product, quantity, price, subtotal
- `OrderSummaryResponse` - orderId, orderNumber, totalAmount, status, orderDate

#### Discount DTOs (`dto.response.discount`)
- `DiscountResponse` - discountId, code, description, discountType, discountValue, minOrderAmount, maxDiscountAmount, usageLimit, usedCount, startDate, endDate, isActive
- `DiscountValidationResponse` - valid (boolean), discountAmount, message

#### Common DTOs (`dto.response.common`)
- `ApiResponse<T>` - success, message, data
- `ErrorResponse` - timestamp, status, error, message, path
- `PageResponse<T>` - content, pageNumber, pageSize, totalElements, totalPages

#### Summary DTOs (`dto.response`)
- `UserSummaryResponse` - userId, username, email, firstName, lastName
- `ProductSummaryResponse` - productId, name, price, imageUrl
- `CategorySummaryResponse` - categoryId, name, imageUrl

---

## 🗄️ Repositories

### Package: `com.fruitstore.repository`

#### 1. UserRepository
#### 2. CategoryRepository
#### 3. ProductRepository
#### 4. CartRepository
#### 5. CartItemRepository
#### 6. OrderRepository
#### 7. OrderItemRepository
#### 8. DiscountRepository
#### 9. DiscountUsageRepository

---

## 🔧 Services

### Package: `com.fruitstore.service`

#### 1. AuthService / UserService
**Responsibilities:**
- Xác thực người dùng (login/register)
- Mã hóa password (BCrypt)
- Generate JWT token
- Quản lý user profile
- Phân quyền

**Methods:**
- `register(RegisterRequest)` → UserProfileResponse
- `login(LoginRequest)` → LoginResponse
- `getUserProfile(Long userId)` → UserProfileResponse
- `updateProfile(Long userId, UpdateProfileRequest)` → UserProfileResponse
- `changePassword(Long userId, ChangePasswordRequest)` → void

---

#### 2. CategoryService
**Responsibilities:**
- CRUD categories
- Quản lý active/inactive categories

**Methods:**
- `getAllCategories()` → List<CategoryResponse>
- `getActiveCategories()` → List<CategoryResponse>
- `getCategoryById(Long id)` → CategoryResponse
- `getActiveCategoryById(Long id)` → CategoryResponse
- `searchCategoriesByName(String name)` → List<CategoryResponse>
- `searchActiveCategoriesByName(String name)` → List<CategoryResponse>
- `getCategoriesWithProductCounts()` → List<CategoryResponse>
- `createCategory(CreateCategoryRequest)` → CategoryResponse
- `updateCategory(Long id, UpdateCategoryRequest)` → CategoryResponse
- `deleteCategory(Long id)` → void
- `permanentlyDeleteCategory(Long id)` → void
- `activateCategory(Long id)` → CategoryResponse
- `deactivateCategory(Long id)` → CategoryResponse

---

#### 3. ProductService
**Responsibilities:**
- CRUD products
- Search và filter products
- Kiểm tra stock availability
- Update stock quantity

**Methods:**
- `getAllProducts(Pageable)` → Page<ProductResponse>
- `getProductById(Long id)` → ProductResponse
- `getActiveProductById(Long id)` → ProductResponse
- `getProductsByCategory(Long categoryId, Pageable)` → Page<ProductResponse>
- `getProductsByCategories(List<Long> categoryIds, Pageable)` → Page<ProductResponse>
- `searchProducts(ProductFilterRequest)` → ProductListResponse
- `searchProductsByName(String name, Pageable)` → Page<ProductResponse>
- `getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice)` → List<ProductResponse>
- `createProduct(CreateProductRequest)` → ProductResponse
- `updateProduct(Long id, UpdateProductRequest)` → ProductResponse
- `deleteProduct(Long id)` → void
- `permanentlyDeleteProduct(Long id)` → void
- `checkStockAvailability(Long productId, Integer quantity)` → Boolean
- `reduceStock(Long productId, Integer quantity)` → void
- `addStock(Long productId, Integer quantity)` → void
- `getLowStockProducts(Integer threshold)` → List<ProductResponse>
- `getTopProductsByStock(Integer limit)` → List<ProductResponse>

---

#### 4. CartService
**Responsibilities:**
- Quản lý giỏ hàng
- Tính toán tổng giá
- Thêm/sửa/xóa items trong cart

**Methods:**
- `getCartByUserId(Long userId)` → CartResponse
- `getCartSummary(Long userId)` → CartResponse
- `addItemToCart(Long userId, AddToCartRequest)` → CartResponse
- `updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest)` → CartResponse
- `removeCartItem(Long userId, Long cartItemId)` → CartResponse
- `clearCart(Long userId)` → CartResponse
- `calculateCartTotal(Long cartId)` → BigDecimal
- `hasItems(Long userId)` → Boolean
- `getItemCount(Long userId)` → Integer
- `getTotalItems(Long userId)` → Integer

---

#### 5. OrderService
**Responsibilities:**
- Tạo đơn hàng từ cart
- Cập nhật trạng thái đơn hàng
- Lịch sử đơn hàng
- Áp dụng mã giảm giá

**Methods:**
- `createOrder(Long userId, CreateOrderRequest)` → OrderResponse
- `getOrderById(Long orderId)` → OrderResponse
- `getOrderById(Long orderId, Long userId)` → OrderResponse
- `getOrderByOrderNumber(String orderNumber)` → OrderResponse
- `getOrderByOrderNumber(String orderNumber, Long userId)` → OrderResponse
- `getOrdersByUser(Long userId, Pageable)` → OrderListResponse
- `getAllOrders(Pageable)` → OrderListResponse
- `getOrdersByUserAndStatus(Long userId, OrderStatus status, Pageable)` → OrderListResponse
- `getOrdersByStatus(OrderStatus status, Pageable)` → OrderListResponse
- `getRecentOrdersByUser(Long userId, int days, Pageable)` → OrderListResponse
- `getRecentOrders(int days, Pageable)` → OrderListResponse
- `getCancellableOrdersByUser(Long userId, Pageable)` → OrderListResponse
- `getCancellableOrders(Pageable)` → OrderListResponse
- `getOrdersWithFilters(Long userId, OrderStatus status, BigDecimal minAmount, BigDecimal maxAmount, String customerName, String customerEmail, LocalDateTime startDate, LocalDateTime endDate, Pageable)` → OrderListResponse
- `updateOrderStatus(Long orderId, UpdateOrderStatusRequest)` → OrderResponse
- `cancelOrder(Long orderId)` → OrderResponse
- `cancelOrder(Long orderId, Long userId)` → OrderResponse
- `canCancelOrder(Long orderId)` → Boolean
- `canCancelOrder(Long orderId, Long userId)` → Boolean
- `getOrderStatistics()` → OrderStatistics
- `getOrderStatisticsByUser(Long userId)` → OrderStatistics
- `getOrderCountByStatus(OrderStatus status)` → Long
- `getOrderCountByUser(Long userId)` → Long
- `getOrderCountByUserAndStatus(Long userId, OrderStatus status)` → Long

---

#### 6. DiscountService
**Responsibilities:**
- CRUD mã giảm giá
- Validate mã giảm giá
- Tính toán số tiền giảm
- Track usage

**Methods:**
- `getAllDiscounts(Pageable)` → Page<DiscountResponse>
- `getActiveDiscounts(Pageable)` → Page<DiscountResponse>
- `getDiscountById(Long id)` → DiscountResponse
- `getDiscountByCode(String code)` → DiscountResponse
- `getAvailableDiscountsForUser(Long userId, BigDecimal orderAmount)` → List<DiscountResponse>
- `createDiscount(CreateDiscountRequest)` → DiscountResponse
- `updateDiscount(Long id, UpdateDiscountRequest)` → DiscountResponse
- `deleteDiscount(Long id)` → void
- `validateDiscount(String code, BigDecimal orderAmount)` → DiscountValidationResponse
- `applyDiscount(String code, BigDecimal orderAmount)` → BigDecimal
- `recordDiscountUsage(Long discountId, Long userId, Long orderId, BigDecimal amount)` → void
- `getDiscountUsageStats(Long discountId)` → DiscountUsageStats
- `getDiscountUsages(Long discountId, Pageable)` → Page<DiscountUsage>
- `getUserDiscountUsages(Long userId, Pageable)` → Page<DiscountUsage>
- `hasUserUsedDiscount(Long userId, Long discountId)` → Boolean

---

## 🎮 Controllers

### Package: `com.fruitstore.controller`

#### 1. AuthController
**Base Path**: `/api/auth`

**Endpoints:**
- `POST /register` - Đăng ký user mới
- `POST /login` - Đăng nhập
- `GET /profile` - Lấy thông tin user (authenticated)
- `PUT /profile` - Cập nhật profile (authenticated)
- `POST /change-password` - Đổi mật khẩu (authenticated)
- `GET /me` - Lấy thông tin user hiện tại (authenticated)

---

#### 2. CategoryController
**Base Path**: `/api/categories`

**Endpoints:**
- `GET /` - Lấy tất cả categories
- `GET /active` - Lấy categories đang active
- `GET /{id}` - Lấy category theo ID
- `GET /{id}/active` - Lấy active category theo ID
- `GET /search` - Tìm kiếm categories theo tên
- `GET /search/active` - Tìm kiếm active categories theo tên
- `GET /with-counts` - Lấy categories với số lượng sản phẩm
- `POST /` - Tạo category mới (admin only)
- `PUT /{id}` - Cập nhật category (admin only)
- `DELETE /{id}` - Xóa category (admin only)
- `DELETE /{id}/permanent` - Xóa vĩnh viễn category (admin only)
- `PUT /{id}/activate` - Kích hoạt category (admin only)
- `PUT /{id}/deactivate` - Vô hiệu hóa category (admin only)

---

#### 3. ProductController
**Base Path**: `/api/products`

**Endpoints:**
- `GET /` - Lấy tất cả products (có phân trang)
- `GET /{id}` - Lấy product theo ID
- `GET /{id}/active` - Lấy active product theo ID
- `GET /category/{categoryId}` - Lấy products theo category
- `GET /search` - Tìm kiếm products với filters
- `GET /search/name` - Tìm kiếm products theo tên
- `GET /price-range` - Lấy products theo khoảng giá
- `GET /categories` - Lấy products theo nhiều categories
- `GET /{id}/stock` - Kiểm tra tồn kho
- `POST /` - Tạo product mới (admin only)
- `PUT /{id}` - Cập nhật product (admin only)
- `DELETE /{id}` - Xóa product (admin only)
- `DELETE /{id}/permanent` - Xóa vĩnh viễn product (admin only)
- `PUT /{id}/stock/add` - Thêm tồn kho (admin only)
- `PUT /{id}/stock/reduce` - Giảm tồn kho (admin only)
- `GET /admin/low-stock` - Lấy products tồn kho thấp (admin only)
- `GET /admin/top-stock` - Lấy products tồn kho cao (admin only)

---

#### 4. CartController
**Base Path**: `/api/cart`

**Endpoints:**
- `GET /` - Lấy giỏ hàng hiện tại (authenticated)
- `GET /summary` - Lấy tóm tắt giỏ hàng (authenticated)
- `POST /items` - Thêm sản phẩm vào giỏ (authenticated)
- `PUT /items/{cartItemId}` - Cập nhật số lượng (authenticated)
- `DELETE /items/{cartItemId}` - Xóa item khỏi giỏ (authenticated)
- `DELETE /clear` - Xóa toàn bộ giỏ hàng (authenticated)
- `GET /total` - Lấy tổng tiền giỏ hàng (authenticated)
- `GET /has-items` - Kiểm tra giỏ hàng có sản phẩm (authenticated)
- `GET /item-count` - Lấy số lượng sản phẩm khác nhau (authenticated)
- `GET /total-items` - Lấy tổng số lượng sản phẩm (authenticated)
- `GET /stats` - Lấy thống kê giỏ hàng (authenticated)

---

#### 5. OrderController
**Base Path**: `/api/orders`

**Endpoints:**
- `POST /` - Tạo đơn hàng mới (authenticated, CUSTOMER role)
- `GET /` - Lấy danh sách đơn hàng của user (authenticated, CUSTOMER role)
- `GET /{id}` - Lấy chi tiết đơn hàng (authenticated, CUSTOMER role)
- `GET /number/{orderNumber}` - Lấy đơn hàng theo mã đơn hàng (authenticated, CUSTOMER role)
- `PUT /{id}/cancel` - Hủy đơn hàng (authenticated, CUSTOMER role)
- `GET /status/{status}` - Lấy đơn hàng theo trạng thái (authenticated, CUSTOMER role)
- `GET /recent` - Lấy đơn hàng gần đây (authenticated, CUSTOMER role)
- `GET /cancellable` - Lấy đơn hàng có thể hủy (authenticated, CUSTOMER role)
- `GET /{id}/can-cancel` - Kiểm tra có thể hủy đơn hàng (authenticated, CUSTOMER role)
- `GET /statistics` - Lấy thống kê đơn hàng của user (authenticated, CUSTOMER role)

---

#### 6. AdminOrderController
**Base Path**: `/api/admin/orders`

**Endpoints:**
- `GET /` - Lấy tất cả đơn hàng (admin only, có filter)
- `GET /{id}` - Xem chi tiết đơn hàng (admin only)
- `GET /number/{orderNumber}` - Lấy đơn hàng theo mã đơn hàng (admin only)
- `PUT /{id}/status` - Cập nhật trạng thái đơn hàng (admin only)
- `PUT /{id}/cancel` - Hủy đơn hàng (admin only)
- `GET /filter` - Lọc đơn hàng với nhiều tiêu chí (admin only)
- `GET /status/{status}` - Lấy đơn hàng theo trạng thái (admin only)
- `GET /recent` - Lấy đơn hàng gần đây (admin only)
- `GET /cancellable` - Lấy đơn hàng có thể hủy (admin only)
- `GET /statistics` - Lấy thống kê tổng quan (admin only)
- `GET /count/status/{status}` - Đếm đơn hàng theo trạng thái (admin only)
- `GET /count/user/{userId}` - Đếm đơn hàng theo user (admin only)
- `GET /count/user/{userId}/status/{status}` - Đếm đơn hàng theo user và trạng thái (admin only)
- `GET /{id}/can-cancel` - Kiểm tra có thể hủy đơn hàng (admin only)

---

#### 7. DiscountController
**Base Path**: `/api/discounts`

**Endpoints:**
- `GET /active` - Lấy mã giảm giá đang active (public)
- `POST /validate` - Validate mã giảm giá (public)
- `GET /code/{code}` - Lấy mã giảm giá theo code (public)
- `GET /available` - Lấy mã giảm giá khả dụng cho user (public)
- `GET /` - Lấy tất cả mã giảm giá (admin only)
- `GET /{id}` - Xem chi tiết mã giảm giá (admin only)
- `POST /` - Tạo mã giảm giá mới (admin only)
- `PUT /{id}` - Cập nhật mã giảm giá (admin only)
- `DELETE /{id}` - Xóa mã giảm giá (admin only)
- `GET /{id}/stats` - Lấy thống kê sử dụng mã giảm giá (admin only)
- `GET /{id}/usages` - Lấy lịch sử sử dụng mã giảm giá (admin only)
- `GET /user/{userId}/usages` - Lấy lịch sử sử dụng của user (admin only)
- `GET /user/{userId}/discount/{discountId}/used` - Kiểm tra user đã dùng mã chưa (admin only)
- `POST /apply` - Áp dụng mã giảm giá (authenticated)
- `POST /usage` - Ghi nhận sử dụng mã giảm giá (admin only)

#### 8. HomeController
**Base Path**: `/`

**Endpoints:**
- `GET /` - Trang chủ với thông tin hệ thống

---

#### 9. TestController
**Base Path**: `/api/public`

**Endpoints:**
- `GET /` - API public welcome
- `GET /health` - Health check
- `GET /info` - Thông tin hệ thống

---

### Package: `com.fruitstore.util`

#### 1. JwtUtil
**Responsibilities:**
- Generate JWT token
- Validate token
- Extract claims từ token

**Methods:**
- `generateToken(UserDetails)` → String
- `extractUsername(String token)` → String
- `validateToken(String token, UserDetails)` → Boolean
- `isTokenExpired(String token)` → Boolean

---

#### 2. PasswordUtil
**Responsibilities:**
- Hash password với BCrypt
- Verify password

**Methods:**
- `hashPassword(String rawPassword)` → String
- `verifyPassword(String rawPassword, String hashedPassword)` → Boolean

---

#### 3. EntityMapper / DTOMapper
**Responsibilities:**
- Convert Entity ↔ DTO
- ModelMapper hoặc MapStruct

**Methods:**
- `toProductResponse(Product)` → ProductResponse
- `toOrderResponse(Order)` → OrderResponse
- `toUserResponse(User)` → UserProfileResponse
- ... (các mapper khác)

---

#### 4. ValidationUtil
**Responsibilities:**
- Custom validation logic
- Business rules validation

**Methods:**
- `validateEmail(String email)` → Boolean
- `validatePhoneNumber(String phone)` → Boolean
- `validatePrice(BigDecimal price)` → Boolean

---

#### 5. DateTimeUtil
**Responsibilities:**
- Format datetime
- Timezone conversion

**Methods:**
- `formatDateTime(LocalDateTime)` → String
- `parseDateTime(String)` → LocalDateTime
- `isWithinRange(LocalDateTime, LocalDateTime, LocalDateTime)` → Boolean

---

## 🔐 Security & Configuration

### Package: `com.fruitstore.config`

#### 1. SecurityConfig
- Configure Spring Security
- JWT authentication filter
- CORS configuration
- Public vs protected endpoints

#### 2. WebConfig
- CORS mappings
- Message converters
- Interceptors

#### 3. JpaConfig
- EntityManagerFactory
- Transaction management
- Auditing

---

### Package: `com.fruitstore.security`

#### 1. JwtAuthenticationFilter
- Intercept requests
- Extract và validate JWT
- Set authentication context

#### 2. UserDetailsServiceImpl
- Load user by username/email
- Implement UserDetailsService

#### 3. CustomUserDetails
- Wrapper cho User entity
- Implement UserDetails interface

---

### Package: `com.fruitstore.exception`

#### 1. Custom Exceptions
- `ResourceNotFoundException` - Entity không tìm thấy
- `BadRequestException` - Invalid input
- `UnauthorizedException` - Không có quyền truy cập
- `InsufficientStockException` - Hết hàng
- `InvalidDiscountException` - Mã giảm giá không hợp lệ

#### 2. GlobalExceptionHandler
- `@RestControllerAdvice`
- Xử lý tất cả exceptions
- Return consistent error response

---

## 📊 Database Considerations

### Indexes
- Đã có indexes cho các foreign keys
- Indexes cho các trường thường xuyên query (email, username, code, status, date)

### Triggers
- `tr_update_stock_after_order` - Tự động giảm stock khi tạo order
- `tr_update_discount_usage` - Tự động tăng used_count khi dùng mã

### Transactions
- Order creation phải là atomic (order + order_items + stock update + discount tracking)
- Cart operations cần transaction để đảm bảo consistency

---

## 🎨 Best Practices

1. **Validation**: Sử dụng Bean Validation (`@Valid`, `@NotNull`, `@Size`, etc.)
2. **Exception Handling**: Centralized với `@RestControllerAdvice`
3. **Logging**: Sử dụng SLF4J/Logback
4. **API Response**: Consistent format với `ApiResponse<T>`
5. **Pagination**: Sử dụng Spring's `Pageable` và `Page<T>`
6. **Security**: Password hashing, JWT token, role-based access
7. **Clean Code**: Meaningful names, single responsibility, DRY principle
