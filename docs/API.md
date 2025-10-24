# FruitStore API Documentation

## Tổng quan

FruitStore là một ứng dụng thương mại điện tử bán trái cây được xây dựng với Spring Boot 3. API này cung cấp các endpoint để quản lý sản phẩm, giỏ hàng, đơn hàng, người dùng và các chức năng quản trị.

**Base URL:** `http://localhost:8080`  
**Framework:** Spring Boot 3  
**Authentication:** JWT Token  
**CORS:** Enabled for frontend integration

---

## Mục lục

1. [Authentication & User Management](#authentication--user-management)
2. [Product Management](#product-management)
3. [Category Management](#category-management)
4. [Cart Management](#cart-management)
5. [Order Management](#order-management)
6. [Admin Order Management](#admin-order-management)
7. [Discount Management](#discount-management)
8. [Public Endpoints](#public-endpoints)
9. [Error Handling](#error-handling)
10. [Authentication & Authorization](#authentication--authorization)

---

## Authentication & User Management

**Base Path:** `/api/auth`

### Đăng ký tài khoản

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "0123456789",
  "address": "123 Main St, City"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "userId": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "0123456789",
    "address": "123 Main St, City",
    "role": "CUSTOMER",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### Đăng nhập

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "userId": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "role": "CUSTOMER"
    }
  }
}
```

### Lấy thông tin profile

```http
GET /api/auth/profile
Authorization: Bearer <token>
```

### Cập nhật profile

```http
PUT /api/auth/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "phone": "0987654321",
  "address": "456 Oak Ave, City"
}
```

### Đổi mật khẩu

```http
POST /api/auth/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "password123",
  "newPassword": "newpassword456"
}
```

---

## Product Management

**Base Path:** `/api/products`

### Lấy danh sách sản phẩm (có phân trang)

```http
GET /api/products?page=0&size=20&sortBy=name&sortDirection=asc
```

**Query Parameters:**
- `page`: Số trang (mặc định: 0)
- `size`: Số lượng sản phẩm mỗi trang (mặc định: 20)
- `sortBy`: Trường sắp xếp (`name`, `price`, `createdAt`, `stockQuantity`)
- `sortDirection`: Hướng sắp xếp (`asc`, `desc`)

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "productId": 1,
        "name": "Táo đỏ",
        "description": "Táo đỏ tươi ngon",
        "price": 50000,
        "stockQuantity": 100,
        "category": {
          "categoryId": 1,
          "name": "Táo"
        },
        "isActive": true,
        "createdAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
}
```

### Lấy sản phẩm theo ID

```http
GET /api/products/{id}
```

### Lấy sản phẩm hoạt động theo ID

```http
GET /api/products/{id}/active
```

### Lấy sản phẩm theo danh mục

```http
GET /api/products/category/{categoryId}?page=0&size=20
```

### Tìm kiếm sản phẩm

```http
GET /api/products/search?name=táo&categoryId=1&minPrice=10000&maxPrice=100000&page=0&size=20
```

### Tìm kiếm theo tên

```http
GET /api/products/search/name?name=táo&page=0&size=20
```

### Lấy sản phẩm theo khoảng giá

```http
GET /api/products/price-range?minPrice=10000&maxPrice=50000
```

### Kiểm tra tồn kho

```http
GET /api/products/{id}/stock?quantity=5
```

**Response:**
```json
{
  "success": true,
  "data": true
}
```

### Tạo sản phẩm mới (Admin)

```http
POST /api/products
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Táo xanh",
  "description": "Táo xanh giòn ngọt",
  "price": 45000,
  "stockQuantity": 50,
  "categoryId": 1
}
```

### Cập nhật sản phẩm (Admin)

```http
PUT /api/products/{id}
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Táo xanh premium",
  "description": "Táo xanh cao cấp",
  "price": 55000,
  "stockQuantity": 30
}
```

### Xóa sản phẩm (Admin)

```http
DELETE /api/products/{id}
Authorization: Bearer <admin_token>
```

### Quản lý tồn kho (Admin)

```http
PUT /api/products/{id}/stock/add?quantity=10
PUT /api/products/{id}/stock/reduce?quantity=5
```

### Lấy sản phẩm tồn kho thấp (Admin)

```http
GET /api/products/admin/low-stock?threshold=10
```

---

## Category Management

**Base Path:** `/api/categories`

### Lấy tất cả danh mục

```http
GET /api/categories
```

### Lấy danh mục hoạt động

```http
GET /api/categories/active
```

### Lấy danh mục theo ID

```http
GET /api/categories/{id}
```

### Tìm kiếm danh mục

```http
GET /api/categories/search?name=táo
```

### Lấy danh mục với số lượng sản phẩm

```http
GET /api/categories/with-counts
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "categoryId": 1,
      "name": "Táo",
      "description": "Các loại táo",
      "isActive": true,
      "productCount": 15
    }
  ]
}
```

### Tạo danh mục mới (Admin)

```http
POST /api/categories
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Cam",
  "description": "Các loại cam"
}
```

### Cập nhật danh mục (Admin)

```http
PUT /api/categories/{id}
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "Cam vàng",
  "description": "Cam vàng ngọt"
}
```

### Kích hoạt/Vô hiệu hóa danh mục (Admin)

```http
PUT /api/categories/{id}/activate
PUT /api/categories/{id}/deactivate
```

---

## Cart Management

**Base Path:** `/api/cart`  
**Authentication:** Required

### Lấy giỏ hàng

```http
GET /api/cart
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "cartId": 1,
    "userId": 1,
    "items": [
      {
        "cartItemId": 1,
        "product": {
          "productId": 1,
          "name": "Táo đỏ",
          "price": 50000
        },
        "quantity": 3,
        "subtotal": 150000
      }
    ],
    "itemCount": 1,
    "totalItems": 3,
    "subtotal": 150000,
    "isEmpty": false
  }
}
```

### Thêm sản phẩm vào giỏ hàng

```http
POST /api/cart/items
Authorization: Bearer <token>
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

### Cập nhật số lượng sản phẩm

```http
PUT /api/cart/items/{cartItemId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "quantity": 5
}
```

### Xóa sản phẩm khỏi giỏ hàng

```http
DELETE /api/cart/items/{cartItemId}
Authorization: Bearer <token>
```

### Xóa tất cả sản phẩm

```http
DELETE /api/cart/clear
Authorization: Bearer <token>
```

### Lấy tổng tiền giỏ hàng

```http
GET /api/cart/total
Authorization: Bearer <token>
```

### Kiểm tra giỏ hàng có sản phẩm

```http
GET /api/cart/has-items
Authorization: Bearer <token>
```

### Lấy thống kê giỏ hàng

```http
GET /api/cart/stats
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "itemCount": 2,
    "totalItems": 5,
    "subtotal": 250000,
    "totalAmount": 250000,
    "isEmpty": false
  }
}
```

---

## Order Management

**Base Path:** `/api/orders`  
**Authentication:** Required (CUSTOMER role)

### Tạo đơn hàng

```http
POST /api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "shippingAddress": "123 Main St, City",
  "phone": "0123456789",
  "notes": "Giao hàng vào buổi sáng"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "orderId": 1,
    "orderNumber": "ORD-20240101-001",
    "userId": 1,
    "items": [
      {
        "product": {
          "productId": 1,
          "name": "Táo đỏ",
          "price": 50000
        },
        "quantity": 3,
        "subtotal": 150000
      }
    ],
    "subtotal": 150000,
    "totalAmount": 150000,
    "status": "PENDING",
    "shippingAddress": "123 Main St, City",
    "phone": "0123456789",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### Lấy danh sách đơn hàng của user

```http
GET /api/orders?page=0&size=10&sortBy=createdAt&sortDirection=desc
Authorization: Bearer <token>
```

### Lấy đơn hàng theo ID

```http
GET /api/orders/{id}
Authorization: Bearer <token>
```

### Lấy đơn hàng theo mã đơn hàng

```http
GET /api/orders/number/{orderNumber}
Authorization: Bearer <token>
```

### Hủy đơn hàng

```http
PUT /api/orders/{id}/cancel
Authorization: Bearer <token>
```

### Lấy đơn hàng theo trạng thái

```http
GET /api/orders/status/{status}?page=0&size=10
Authorization: Bearer <token>
```

**Trạng thái đơn hàng:**
- `PENDING`: Chờ xử lý
- `CONFIRMED`: Đã xác nhận
- `DELIVERED`: Đã nhận hàng
- `CANCELLED`: Đã hủy

### Lấy đơn hàng gần đây

```http
GET /api/orders/recent?days=30&page=0&size=10
Authorization: Bearer <token>
```

### Kiểm tra có thể hủy đơn hàng

```http
GET /api/orders/{id}/can-cancel
Authorization: Bearer <token>
```

### Lấy thống kê đơn hàng

```http
GET /api/orders/statistics
Authorization: Bearer <token>
```

---

## Admin Order Management

**Base Path:** `/api/admin/orders`  
**Authentication:** Required (ADMIN role)

### Lấy tất cả đơn hàng

```http
GET /api/admin/orders?page=0&size=20&sortBy=createdAt&sortDirection=desc
Authorization: Bearer <admin_token>
```

### Lấy đơn hàng theo ID (Admin)

```http
GET /api/admin/orders/{id}
Authorization: Bearer <admin_token>
```

### Cập nhật trạng thái đơn hàng

```http
PUT /api/admin/orders/{id}/status
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "status": "CONFIRMED",
  "notes": "Đơn hàng đã được xác nhận"
}
```

### Hủy đơn hàng (Admin)

```http
PUT /api/admin/orders/{id}/cancel
Authorization: Bearer <admin_token>
```

### Lọc đơn hàng với nhiều tiêu chí

```http
GET /api/admin/orders/filter?userId=1&status=PENDING&minAmount=100000&maxAmount=500000&customerName=John&startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59&page=0&size=20
Authorization: Bearer <admin_token>
```

### Lấy đơn hàng theo trạng thái

```http
GET /api/admin/orders/status/{status}?page=0&size=20
Authorization: Bearer <admin_token>
```

### Lấy đơn hàng gần đây

```http
GET /api/admin/orders/recent?days=7&page=0&size=20
Authorization: Bearer <admin_token>
```

### Lấy thống kê đơn hàng

```http
GET /api/admin/orders/statistics
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalOrders": 150,
    "pendingOrders": 10,
    "confirmedOrders": 25,
    "shippedOrders": 30,
    "deliveredOrders": 80,
    "cancelledOrders": 5,
    "totalRevenue": 50000000,
    "averageOrderValue": 333333
  }
}
```

### Đếm đơn hàng theo trạng thái

```http
GET /api/admin/orders/count/status/{status}
Authorization: Bearer <admin_token>
```

### Đếm đơn hàng theo user

```http
GET /api/admin/orders/count/user/{userId}
Authorization: Bearer <admin_token>
```

---

## Discount Management

**Base Path:** `/api/discounts`

### Lấy mã giảm giá hoạt động

```http
GET /api/discounts/active?page=0&size=20
```

### Xác thực mã giảm giá

```http
POST /api/discounts/validate
Content-Type: application/json

{
  "code": "SAVE10",
  "orderAmount": 200000
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "isValid": true,
    "discountCode": "SAVE10",
    "discountAmount": 20000,
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "message": "Mã giảm giá hợp lệ"
  }
}
```

### Lấy mã giảm giá theo code

```http
GET /api/discounts/code/{code}
```

### Lấy mã giảm giá khả dụng cho user

```http
GET /api/discounts/available?userId=1&orderAmount=200000
```

### Tạo mã giảm giá mới (Admin)

```http
POST /api/discounts
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "code": "SAVE20",
  "description": "Giảm 20% cho đơn hàng trên 300k",
  "discountType": "PERCENTAGE",
  "discountValue": 20,
  "minOrderAmount": 300000,
  "maxDiscountAmount": 100000,
  "usageLimit": 100,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
```

### Lấy tất cả mã giảm giá (Admin)

```http
GET /api/discounts?page=0&size=20
Authorization: Bearer <admin_token>
```

### Cập nhật mã giảm giá (Admin)

```http
PUT /api/discounts/{id}
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "description": "Giảm 25% cho đơn hàng trên 500k",
  "discountValue": 25,
  "minOrderAmount": 500000
}
```

### Xóa mã giảm giá (Admin)

```http
DELETE /api/discounts/{id}
Authorization: Bearer <admin_token>
```

### Lấy thống kê sử dụng mã giảm giá (Admin)

```http
GET /api/discounts/{id}/stats
Authorization: Bearer <admin_token>
```

### Lấy lịch sử sử dụng mã giảm giá (Admin)

```http
GET /api/discounts/{id}/usages?page=0&size=20
Authorization: Bearer <admin_token>
```

---

## Public Endpoints

### Trang chủ

```http
GET /
```

**Response:**
```json
{
  "application": "🍎 FruitStore",
  "message": "Chào mừng đến với FruitStore - Ứng dụng thương mại điện tử bán trái cây!",
  "status": "success",
  "timestamp": "2024-01-01T10:00:00",
  "version": "1.0",
  "framework": "Spring Boot 3",
  "endpoints": {
    "home": "/",
    "api_public": "/api/public/",
    "health": "/api/public/health",
    "info": "/api/public/info",
    "actuator": "/actuator/health"
  }
}
```

### API Public

```http
GET /api/public/
```

### Health Check

```http
GET /api/public/health
```

### Thông tin hệ thống

```http
GET /api/public/info
```

---

## Error Handling

API sử dụng format chuẩn cho error response:

```json
{
  "success": false,
  "message": "Error message",
  "error": "ERROR_CODE",
  "timestamp": "2024-01-01T10:00:00"
}
```

### HTTP Status Codes

- `200 OK`: Thành công
- `201 Created`: Tạo mới thành công
- `400 Bad Request`: Dữ liệu đầu vào không hợp lệ
- `401 Unauthorized`: Chưa xác thực
- `403 Forbidden`: Không có quyền truy cập
- `404 Not Found`: Không tìm thấy tài nguyên
- `409 Conflict`: Xung đột dữ liệu
- `500 Internal Server Error`: Lỗi server

### Validation Errors

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email không hợp lệ"
    },
    {
      "field": "password",
      "message": "Mật khẩu phải có ít nhất 6 ký tự"
    }
  ]
}
```

---

## Authentication & Authorization

### JWT Token

API sử dụng JWT token để xác thực. Token phải được gửi trong header:

```http
Authorization: Bearer <jwt_token>
```

### Roles

- `CUSTOMER`: Khách hàng thông thường
- `ADMIN`: Quản trị viên

### Endpoint Security

- **Public**: Không cần authentication
- **Authenticated**: Cần JWT token hợp lệ
- **Admin Only**: Cần ADMIN role

### Token Expiration

JWT token có thời hạn sử dụng. Khi token hết hạn, client cần đăng nhập lại để lấy token mới.

---

## Rate Limiting

API có giới hạn số lượng request để tránh spam:

- **Public endpoints**: 100 requests/minute
- **Authenticated endpoints**: 1000 requests/minute
- **Admin endpoints**: 2000 requests/minute

---

## CORS Configuration

API hỗ trợ CORS cho frontend integration. Cấu hình trong `application.properties`:

```properties
app.cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

---

## Pagination

Hầu hết các endpoint trả về danh sách đều hỗ trợ phân trang:

### Query Parameters

- `page`: Số trang (bắt đầu từ 0)
- `size`: Số lượng items mỗi trang
- `sortBy`: Trường sắp xếp
- `sortDirection`: Hướng sắp xếp (`asc`, `desc`)

### Response Format

```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false,
  "numberOfElements": 10
}
```

---

## Support

Để được hỗ trợ hoặc báo cáo lỗi, vui lòng liên hệ team phát triển FruitStore.

**Version:** 1.0  
**Last Updated:** 24/10/2025
