# Database Tables and Models Mapping

## Core Entity Tables & Models

| Table | Model | Primary Relationships |
|-------|-------|----------------------|
| `users` | `User.java` | → `ShippingAddress`, `Order`, `Cart`, `Wishlist`, `Review`, `UserActivityLog`, `Notification` |
| `products` | `Product.java` | → `Category`, `Subcategory`, ← `ProductImage`, `Review`, `Cart`, `Wishlist`, `OrderItem`, `ComboItem`, `FlashSaleItem` |
| `categories` | `Category.java` | → `Product`, `Subcategory`, → `parentCategory` (self-reference) |
| `subcategories` | `Subcategory.java` | → `Product`, → `Category` (parent) |
| `orders` | `Order.java` | → `User`, `ShippingAddress`, ← `OrderItem`, `OrderComboItem`, `Payment`, `OrderStatusHistory` |
| `shipping_addresses` | `ShippingAddress.java` | → `User`, ← `Order` |

## E-commerce Tables & Models

| Table | Model | Primary Relationships |
|-------|-------|----------------------|
| `cart` | `Cart.java` | → `User`, `Product` |
| `cart_combos` | `CartCombo.java` | → `User`, `Combo` |
| `wishlists` | `Wishlist.java` | → `User`, `Product` |
| `combos` | `Combo.java` | ← `ComboItem`, `CartCombo`, `OrderComboItem` |
| `combo_items` | `ComboItem.java` | → `Combo`, `Product` |
| `order_items` | `OrderItem.java` | → `Order`, `Product` |
| `order_combo_items` | `OrderComboItem.java` | → `Order`, `Combo` |

## Promotion & Marketing Tables & Models

| Table | Model | Primary Relationships |
|-------|-------|----------------------|
| `discounts` | `Discount.java` | ← `DiscountUsage` |
| `discount_usage` | `DiscountUsage.java` | → `Discount`, `User`, `Order` |
| `flash_sales` | `FlashSale.java` | ← `FlashSaleItem` |
| `flash_sale_items` | `FlashSaleItem.java` | → `FlashSale`, `Product` |

## Support Tables & Models

| Table | Model | Primary Relationships |
|-------|-------|----------------------|
| `payments` | `Payment.java` | → `Order` |
| `product_images` | `ProductImage.java` | → `Product` |
| `product_stats` | `ProductStats.java` | → `Product` |
| `reviews` | `Review.java` | → `User`, `Product` |
| `notifications` | `Notification.java` | → `User` |
| `user_activity_logs` | `UserActivityLog.java` | → `User` |
| `order_status_history` | `OrderStatusHistory.java` | → `Order`, `User` (updated_by) |
| `system_settings` | `SystemSetting.java` | No relationships |

## Enum Models

| Purpose | Models |
|---------|--------|
| User Management | `UserRole.java` |
| Order Management | `OrderStatus.java`, `PaymentMethod.java` |
| Activity Tracking | `ActivityType.java` |
| Notifications | `NotificationType.java` |
| Discounts | `DiscountType.java` |
| Payments | `Payment.PaymentStatus.java` (nested enum) |

## Key Object Relationships

### Product Relationships
- `Product` → `Category` (many-to-one)
- `Product` → `Subcategory` (many-to-one, optional)
- `Product` ← `ProductImage` (one-to-many)
- `Product` ← `Review` (one-to-many)
- `Product` ← `Cart` (one-to-many)
- `Product` ← `Wishlist` (one-to-many)
- `Product` ← `OrderItem` (one-to-many)
- `Product` ← `ComboItem` (one-to-many)
- `Product` ← `FlashSaleItem` (one-to-many)

### User Relationships
- `User` ← `ShippingAddress` (one-to-many)
- `User` ← `Order` (one-to-many)
- `User` ← `Cart` (one-to-many)
- `User` ← `CartCombo` (one-to-many)
- `User` ← `Wishlist` (one-to-many)
- `User` ← `Review` (one-to-many)
- `User` ← `UserActivityLog` (one-to-many)
- `User` ← `Notification` (one-to-many)
- `User` ← `DiscountUsage` (one-to-many)

### Order Relationships
- `Order` → `User` (many-to-one)
- `Order` → `ShippingAddress` (many-to-one)
- `Order` ← `OrderItem` (one-to-many)
- `Order` ← `OrderComboItem` (one-to-many)
- `Order` ← `Payment` (one-to-many)
- `Order` ← `OrderStatusHistory` (one-to-many)
- `Order` ← `DiscountUsage` (one-to-many)

### Category Hierarchy
- `Category` ← `Product` (one-to-many)
- `Category` ← `Subcategory` (one-to-many)
- `Category` → `Category` (self-reference for parent-child)

## Business Logic Features

### Rich Business Methods
- **Product**: `getCategoryName()`, `belongsToCategory()`, `isInStock()`, `isLowStock()`
- **Order**: `getCustomerName()`, `getShippingAddressFormatted()`, `canBeCancelled()`
- **Cart**: `getProductName()`, `getTotalPrice()`, `canBeAddedToOrder()`
- **User**: `isAdmin()`, `updateLastLogin()`
- **ShippingAddress**: `getFullAddress()`, `belongsToUser()`

### Object Construction Patterns
- **ID-based constructors**: For database operations and backward compatibility
- **Object-based constructors**: For object-oriented operations with automatic ID synchronization
- **Relationship setters**: Automatically maintain ID consistency when setting object relationships