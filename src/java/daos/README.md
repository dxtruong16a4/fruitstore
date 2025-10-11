src/java/dao/
├── user/
│   ├── UserDAO.java
│   ├── ShippingAddressDAO.java
│   ├── WishlistDAO.java
│   └── UserActivityLogDAO.java
├── product/
│   ├── ProductDAO.java
│   ├── CategoryDAO.java
│   ├── SubcategoryDAO.java
│   ├── ProductImageDAO.java
│   ├── ProductStatsDAO.java
│   └── ReviewDAO.java
├── order/
│   ├── OrderDAO.java
│   ├── OrderItemDAO.java
│   ├── OrderComboItemDAO.java
│   ├── OrderStatusHistoryDAO.java
│   └── PaymentDAO.java
├── promotion/
│   ├── ComboDAO.java
│   ├── ComboItemDAO.java
│   ├── DiscountDAO.java
│   ├── DiscountUsageDAO.java
│   ├── FlashSaleDAO.java
│   └── FlashSaleItemDAO.java
├── system/
│   ├── NotificationDAO.java
│   └── SystemSettingDAO.java
└── shared/
    └── BaseDAO.java       # Chứa hàm tiện ích chung (getConnection, closeResource, v.v.)

# 📘 DAO Mapping Table

| Domain | Models | DAOs | Database Tables |
|--------|---------|------|----------------|
| **User** | User, UserRole, UserActivityLog, ActivityType, ShippingAddress | `UserDAO`, `UserActivityLogDAO`, `ShippingAddressDAO` | `users`, `user_activity_logs`, `shipping_addresses` |
| **Product** | Product, Category, Subcategory, ProductImage, ProductStats, Review | `ProductDAO`, `CategoryDAO`, `SubcategoryDAO`, `ProductImageDAO`, `ProductStatsDAO`, `ReviewDAO` | `products`, `categories`, `subcategories`, `product_images`, `product_stats`, `reviews` |
| **Order** | Order, OrderItem, OrderComboItem, OrderStatusHistory, Payment, PaymentMethod | `OrderDAO`, `OrderItemDAO`, `OrderComboItemDAO`, `OrderStatusHistoryDAO`, `PaymentDAO` | `orders`, `order_items`, `order_combo_items`, `order_status_history`, `payments` |
| **Promotion** | Combo, ComboItem, Discount, DiscountUsage, FlashSale, FlashSaleItem | `ComboDAO`, `ComboItemDAO`, `DiscountDAO`, `DiscountUsageDAO`, `FlashSaleDAO`, `FlashSaleItemDAO` | `combos`, `combo_items`, `discounts`, `discount_usage`, `flash_sales`, `flash_sale_items` |
| **System** | Notification, NotificationType, SystemSetting | `NotificationDAO`, `SystemSettingDAO` | `notifications`, `system_settings` |
| **Cart/Wishlist** | Cart, CartCombo, Wishlist | `CartDAO`, `CartComboDAO`, `WishlistDAO` | `cart`, `cart_combos`, `wishlists` |