# Data Transfer Objects (DTOs) Structure

DTOs are used to transfer data between layers: DAO ⇄ Service ⇄ Servlet ⇄ JSP

## Directory Structure

```
src/java/dtos/
├── user/
│   ├── UserDTO.java              # User information (without sensitive data)
│   ├── UserRegistrationDTO.java  # User registration data
│   ├── UserLoginDTO.java         # Login credentials
│   ├── UserProfileDTO.java       # User profile for display
│   ├── AddressDTO.java           # Shipping address data
│   ├── WishlistDTO.java          # Wishlist item data
│   └── UserActivityLogDTO.java   # User activity information
├── product/
│   ├── ProductDTO.java           # Product information for display
│   ├── ProductCreateDTO.java     # Product creation data
│   ├── ProductUpdateDTO.java     # Product update data
│   ├── CategoryDTO.java          # Category information
│   ├── SubcategoryDTO.java       # Subcategory information
│   ├── ReviewDTO.java            # Product review data
│   ├── ProductImageDTO.java      # Product image information
│   └── ProductStatsDTO.java      # Product statistics
├── order/
│   ├── OrderDTO.java             # Order information
│   ├── OrderCreateDTO.java       # Order creation data
│   ├── OrderItemDTO.java         # Order item details
│   ├── OrderComboItemDTO.java    # Order combo item details
│   ├── OrderStatusHistoryDTO.java # Order status tracking
│   ├── PaymentDTO.java           # Payment information
│   ├── CartDTO.java              # Cart item data
│   └── CartComboDTO.java         # Cart combo data
├── promotion/
│   ├── ComboDTO.java             # Combo information
│   ├── ComboItemDTO.java         # Combo item details
│   ├── DiscountDTO.java          # Discount information
│   ├── DiscountUsageDTO.java     # Discount usage tracking
│   ├── FlashSaleDTO.java         # Flash sale information
│   └── FlashSaleItemDTO.java     # Flash sale item details
├── system/
│   ├── NotificationDTO.java      # Notification data
│   └── SystemSettingDTO.java     # System configuration
└── shared/
    ├── ApiResponseDTO.java       # Standard API response wrapper
    ├── PaginationDTO.java        # Pagination information
    ├── SearchCriteriaDTO.java    # Search parameters
    ├── FileUploadDTO.java        # File upload data
    └── ValidationErrorDTO.java   # Validation error details
```

## DTO Categories

### **User DTOs**
- **UserDTO**: Public user information (no password)
- **UserRegistrationDTO**: Registration form data
- **UserLoginDTO**: Login credentials
- **UserProfileDTO**: Complete user profile for display
- **AddressDTO**: Shipping address information
- **WishlistDTO**: Wishlist item with product details
- **UserActivityLogDTO**: User activity tracking data

### **Product DTOs**
- **ProductDTO**: Complete product information for display
- **ProductCreateDTO**: Product creation form data
- **ProductUpdateDTO**: Product update form data
- **CategoryDTO**: Category information with hierarchy
- **SubcategoryDTO**: Subcategory with parent category info
- **ReviewDTO**: Product review with user information
- **ProductImageDTO**: Product image metadata
- **ProductStatsDTO**: Product sales and performance data

### **Order DTOs**
- **OrderDTO**: Complete order information
- **OrderCreateDTO**: Order creation from cart
- **OrderItemDTO**: Order item with product details
- **OrderComboItemDTO**: Order combo item with combo details
- **OrderStatusHistoryDTO**: Order status change history
- **PaymentDTO**: Payment processing information
- **CartDTO**: Cart item with product information
- **CartComboDTO**: Cart combo item with combo details

### **Promotion DTOs**
- **ComboDTO**: Combo information with items
- **ComboItemDTO**: Individual combo item details
- **DiscountDTO**: Discount code information
- **DiscountUsageDTO**: Discount usage tracking
- **FlashSaleDTO**: Flash sale event information
- **FlashSaleItemDTO**: Flash sale item with pricing

### **System DTOs**
- **NotificationDTO**: User notification data
- **SystemSettingDTO**: System configuration values

### **Shared DTOs**
- **ApiResponseDTO**: Standardized API response wrapper
- **PaginationDTO**: Pagination metadata
- **SearchCriteriaDTO**: Search and filter parameters
- **FileUploadDTO**: File upload handling
- **ValidationErrorDTO**: Form validation error details

## Key Principles

1. **Data Security**: DTOs exclude sensitive information (passwords, internal IDs)
2. **Layer Separation**: Clean data transfer between DAO, Service, Servlet, and JSP
3. **Validation**: DTOs can include validation annotations
4. **Serialization**: Optimized for JSON/XML serialization
5. **Versioning**: Separate DTOs for create/update operations
6. **Relationship Handling**: Flattened or nested object relationships as needed