# Servlet Layer Documentation - FruitStore

## 📋 Tổng quan

Servlet layer là tầng Controller trong MVC architecture, chịu trách nhiệm xử lý HTTP requests và responses. Servlet layer nhận request từ client, gọi Service layer để xử lý business logic, và trả về response (JSP pages hoặc JSON data).

### Tech Stack
- **Framework**: Java Servlet API + JSP + JSTL
- **Server**: Apache Tomcat
- **Pattern**: MVC Pattern với Servlet làm Controller
- **Session Management**: HttpSession
- **Request Handling**: GET/POST với action parameter routing

---

## 🏗️ Directory Structure

```
src/java/servlets/
├── auth/
│   ├── AuthServlet.java              # Authentication & authorization
│   ├── LoginServlet.java             # Login/logout handling
│   └── RegistrationServlet.java      # User registration
├── user/
│   ├── UserServlet.java              # User profile management
│   ├── UserProfileServlet.java       # Profile CRUD operations
│   ├── ShippingAddressServlet.java   # Address management
│   └── UserActivityServlet.java      # User activity tracking
├── product/
│   ├── ProductServlet.java           # Product listing & details
│   ├── CategoryServlet.java          # Category management
│   ├── SubcategoryServlet.java       # Subcategory operations
│   ├── ProductImageServlet.java      # Image handling
│   └── ReviewServlet.java            # Product reviews
├── order/
│   ├── OrderServlet.java             # Order management
│   ├── OrderItemServlet.java         # Order item operations
│   ├── PaymentServlet.java           # Payment processing
│   └── OrderStatusServlet.java       # Order status updates
├── cart/
│   ├── CartServlet.java              # Shopping cart operations
│   └── WishlistServlet.java          # Wishlist management
├── promotion/
│   ├── DiscountServlet.java          # Discount code handling
│   ├── FlashSaleServlet.java         # Flash sale operations
│   └── ComboServlet.java             # Combo management
├── admin/
│   ├── AdminServlet.java             # Admin panel operations
│   ├── AdminProductServlet.java      # Admin product management
│   ├── AdminOrderServlet.java        # Admin order management
│   ├── AdminUserServlet.java         # Admin user management
│   └── AdminReportServlet.java       # Admin reports & statistics
├── system/
│   ├── NotificationServlet.java      # Notification handling
│   └── SystemServlet.java            # System operations
└── common/
    ├── BaseServlet.java              # Common servlet functionality
    ├── FileUploadServlet.java        # File upload handling
    └── ErrorServlet.java             # Error handling
```

---

## 📊 Servlet Mapping & Responsibilities

### Core Servlet Classes & Their Functions

| Servlet | Primary Services | Key DTOs | Main Functions |
|---------|------------------|----------|----------------|
| **AuthServlet** | `AuthenticationService`, `UserService` | `UserLoginDTO`, `UserRegistrationDTO` | Login, logout, registration, password reset |
| **UserServlet** | `UserService`, `UserActivityService` | `UserDTO`, `UserProfileDTO` | Profile management, user settings, activity logs |
| **ProductServlet** | `ProductService`, `CategoryService` | `ProductDTO`, `CategoryDTO` | Product listing, search, category browsing |
| **OrderServlet** | `OrderService`, `PaymentService` | `OrderDTO`, `OrderCreateDTO` | Order creation, status tracking, payment |
| **CartServlet** | `CartService`, `CartComboService` | `CartDTO`, `CartComboDTO` | Cart operations, checkout preparation |
| **AdminServlet** | All admin services | Various admin DTOs | Admin panel operations, reports |


*Tài liệu này được cập nhật lần cuối: 11/10/2025*
