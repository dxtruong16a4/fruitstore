# Service Layer Documentation - FruitStore

## 📋 Tổng quan

Service layer là tầng xử lý business logic, nằm giữa Servlet (Controller) và DAO (Data Access Object). Service layer chịu trách nhiệm:
- Xử lý business logic phức tạp
- Orchestrating multiple DAO operations
- Data validation và transformation
- Transaction management
- Error handling và logging

### Tech Stack
- **Framework**: Java Servlet + JSP + JSTL
- **Database**: MySQL với JDBC
- **Pattern**: Service Layer Pattern trong MVC Architecture
- **Dependency**: Service ↔ DAO ↔ DTO ↔ Model

---

## 🏗️ Directory Structure

```
src/java/services/
├── user/
│   ├── UserService.java              # User management business logic
│   ├── AuthenticationService.java    # Login/logout/registration logic
│   ├── ShippingAddressService.java   # Address management logic
│   ├── WishlistService.java          # Wishlist operations
│   └── UserActivityService.java      # User activity tracking
├── product/
│   ├── ProductService.java           # Product management logic
│   ├── CategoryService.java          # Category hierarchy management
│   ├── SubcategoryService.java       # Subcategory operations
│   ├── ProductImageService.java      # Image management
│   ├── ProductStatsService.java      # Product analytics
│   └── ReviewService.java            # Review management
├── order/
│   ├── OrderService.java             # Order processing logic
│   ├── OrderItemService.java         # Order item operations
│   ├── OrderComboItemService.java    # Order combo operations
│   ├── OrderStatusService.java       # Order status management
│   └── PaymentService.java           # Payment processing
├── promotion/
│   ├── ComboService.java             # Combo management
│   ├── ComboItemService.java         # Combo item operations
│   ├── DiscountService.java          # Discount validation & application
│   ├── DiscountUsageService.java     # Discount tracking
│   ├── FlashSaleService.java         # Flash sale management
│   └── FlashSaleItemService.java     # Flash sale item operations
├── cart/
│   ├── CartService.java              # Cart operations
│   └── CartComboService.java         # Cart combo operations
├── system/
│   ├── NotificationService.java      # Notification management
│   ├── SystemSettingService.java     # System configuration
│   └── EmailService.java             # Email notifications
└── shared/
    ├── BaseService.java              # Common service functionality
    ├── ValidationService.java        # Data validation utilities
    ├── FileService.java              # File upload/management
    └── PaginationService.java        # Pagination logic
```

---

## 📊 Service Layer Mapping

### Domain Services & Their Responsibilities

| Domain | Services | Primary DAOs | Key DTOs | Business Logic |
|--------|----------|--------------|----------|----------------|
| **User** | `UserService`, `AuthenticationService`, `ShippingAddressService`, `WishlistService`, `UserActivityService` | `UserDAO`, `ShippingAddressDAO`, `WishlistDAO`, `UserActivityLogDAO` | `UserDTO`, `UserRegistrationDTO`, `UserLoginDTO`, `AddressDTO`, `WishlistDTO` | Authentication, profile management, address validation, activity tracking |
| **Product** | `ProductService`, `CategoryService`, `SubcategoryService`, `ProductImageService`, `ProductStatsService`, `ReviewService` | `ProductDAO`, `CategoryDAO`, `SubcategoryDAO`, `ProductImageDAO`, `ProductStatsDAO`, `ReviewDAO` | `ProductDTO`, `CategoryDTO`, `SubcategoryDTO`, `ProductImageDTO`, `ReviewDTO` | Product lifecycle, category hierarchy, image management, review validation |
| **Order** | `OrderService`, `OrderItemService`, `OrderComboItemService`, `OrderStatusService`, `PaymentService` | `OrderDAO`, `OrderItemDAO`, `OrderComboItemDAO`, `OrderStatusHistoryDAO`, `PaymentDAO` | `OrderDTO`, `OrderCreateDTO`, `OrderItemDTO`, `PaymentDTO` | Order processing, payment validation, status transitions, inventory updates |
| **Promotion** | `ComboService`, `DiscountService`, `FlashSaleService` | `ComboDAO`, `DiscountDAO`, `FlashSaleDAO` | `ComboDTO`, `DiscountDTO`, `FlashSaleDTO` | Discount calculation, combo pricing, flash sale validation |
| **Cart** | `CartService`, `CartComboService` | `CartDAO`, `CartComboDAO` | `CartDTO`, `CartComboDTO` | Cart operations, price calculations, stock validation |
| **System** | `NotificationService`, `SystemSettingService`, `EmailService` | `NotificationDAO`, `SystemSettingDAO` | `NotificationDTO`, `SystemSettingDTO` | Notification delivery, system configuration, email templates |


*Tài liệu này được cập nhật lần cuối: 11/10/2025*
