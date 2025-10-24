# 🍎 FruitStore - Ứng dụng Thương mại Điện tử Bán Trái cây

Ứng dụng web thương mại điện tử hiện đại cho cửa hàng trái cây được xây dựng với **Spring Boot 3** (REST API, JPA, MySQL) backend và **React** frontend, tuân theo kiến trúc **Domain-Driven Design (DDD)**.

## 📋 Mục lục
- [Tổng quan](#tổng-quan)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Luồng hoạt động hệ thống](#luồng-hoạt-động-hệ-thống)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Tài liệu khác](#tài-liệu-khác)

## 🎯 Tổng quan

FruitStore là một hệ thống thương mại điện tử quy mô nhỏ, phù hợp cho dự án học tập, cho phép:
- **Khách hàng**: Duyệt sản phẩm, thêm vào giỏ hàng, đặt hàng, sử dụng mã giảm giá
- **Quản trị viên**: Quản lý sản phẩm, danh mục, đơn hàng, mã giảm giá

### Tính năng chính
- ✅ Xác thực người dùng (đăng nhập/đăng ký)
- ✅ Quản lý sản phẩm theo danh mục
- ✅ Giỏ hàng thông minh
- ✅ Đặt hàng và theo dõi đơn hàng
- ✅ Hệ thống mã giảm giá linh hoạt
- ✅ Phân quyền người dùng (Customer/Admin)

## 🛠️ Công nghệ sử dụng

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT
- **Validation**: Bean Validation (Hibernate Validator)
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18
- **State Management**: React Context API / Redux Toolkit
- **HTTP Client**: Axios
- **UI**: Material-UI / Tailwind CSS
- **Routing**: React Router v6

### DevOps & Tools
- **Version Control**: Git
- **API Testing**: Postman
- **IDE**: IntelliJ IDEA / VS Code

## 🔄 Luồng hoạt động hệ thống

### 1. Luồng đăng ký & đăng nhập

```
[Client] → Register/Login → [AuthController]
    ↓
[AuthService] → Validate & Hash Password
    ↓
[UserRepository] → Save to DB
    ↓
[JwtUtil] → Generate JWT Token
    ↓
[Client] ← Return Token & User Info
```

### 2. Luồng duyệt sản phẩm

```
[Client] → GET /api/products → [ProductController]
    ↓
[ProductService] → Get products with filters
    ↓
[ProductRepository] → Query DB (JOIN categories)
    ↓
[ProductMapper] → Entity → DTO
    ↓
[Client] ← Return ProductDTO List
```

### 3. Luồng thêm vào giỏ hàng

```
[Client] → POST /api/cart/items → [CartController]
    ↓
[CartService] → Get or Create Cart for User
    ↓
[CartItemService] → Add/Update Cart Item
    ↓
[ProductService] → Check stock availability
    ↓
[CartItemRepository] → Save to DB
    ↓
[Client] ← Return updated Cart
```

### 4. Luồng đặt hàng (Checkout)

```
[Client] → POST /api/orders → [OrderController]
    ↓
[OrderService] → Validate Cart & Calculate Total
    ↓
[DiscountService] → Apply discount code (if any)
    ↓
[OrderService] → Create Order + Order Items
    ↓
[ProductService] → Reduce stock (via Trigger)
    ↓
[CartService] → Clear Cart
    ↓
[OrderRepository] → Save Order (Transaction)
    ↓
[Client] ← Return OrderDTO with details
```

### 5. Luồng quản lý đơn hàng (Admin)

```
[Admin] → GET/PUT /api/admin/orders → [AdminOrderController]
    ↓
[OrderService] → Get orders / Update status
    ↓
[OrderRepository] → Query/Update DB
    ↓
[Admin] ← Return Order List/Updated Order
```

### 6. Luồng áp dụng mã giảm giá

```
[Client] → POST /api/discounts/validate → [DiscountController]
    ↓
[DiscountService] → Validate discount code
    ├─ Check is_active, date range
    ├─ Check min_order_amount
    ├─ Check usage_limit
    ↓
[DiscountService] → Calculate discount amount
    ↓
[Client] ← Return discount details
```

## 📁 Cấu trúc dự án

### Backend Structure (DDD-inspired)
```
fruitstore-backend/
├── src/main/java/com/fruitstore/
│   ├── config/              # Security, CORS, DB config
│   ├── domain/              # Domain models (Entities)
│   │   ├── user/
│   │   ├── product/
│   │   ├── cart/
│   │   ├── order/
│   │   └── discount/
│   ├── dto/                 # Data Transfer Objects
│   ├── repository/          # Spring Data JPA Repositories
│   ├── service/             # Business logic
│   ├── controller/          # REST API endpoints
│   ├── security/            # JWT, UserDetails
│   ├── exception/           # Custom exceptions
│   └── util/                # Helper classes
├── src/main/resources/
│   ├── application.properties
│   └── Database.sql
└── pom.xml
```

### Frontend Structure
```
fruitstore-frontend/
├── public/
├── src/
│   ├── components/          # Reusable components
│   ├── pages/               # Page components
│   ├── services/            # API calls
│   ├── context/             # Global state
│   ├── hooks/               # Custom hooks
│   ├── utils/               # Helper functions
│   └── App.js
└── package.json
```

## 🚀 Cài đặt và chạy

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0
- Maven 3.8+

### Backend Setup

1. **Clone repository**
```bash
git clone https://github.com/dxtruong16a4/fruitstore.git
cd backend
```

2. **Tạo database**
```bash
mysql -u root -p < Database.sql
```

3. **Cấu hình application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/FruitStore
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=none
```

4. **Chạy backend**
```bash
mvnd spring-boot:run
# Server sẽ chạy tại http://localhost:8080
```

### Frontend Setup

1. **Cài đặt dependencies**
```bash
cd frontend
npm install
```

2. **Cấu hình API endpoint**
```javascript
// src/config/api.js
export const API_BASE_URL = 'http://localhost:8080/api';
```

3. **Chạy frontend**
```bash
npm start
# App sẽ chạy tại http://localhost:3000
```

## 📚 Tài liệu khác

- [ARCHITECTURE.md](docs/ARCHITECTURE.md) - Chi tiết về kiến trúc, entities, DTOs, services, controllers
- [API.md](docs/API.md) - Tài liệu API endpoints (sẽ được tạo sau)

## 📝 Ghi chú

- Project này được xây dựng với mục đích học tập
- Scale nhỏ, phù hợp cho môi trường trường học

## 👥 Đóng góp

Đây là project học tập, mọi đóng góp và góp ý đều được chào đón!

## 📄 License

Dự án này được phân phối dưới [MIT License](LICENSE). Xem file `LICENSE` để biết thêm chi tiết.
MIT License - Tự do sử dụng cho mục đích học tập.   