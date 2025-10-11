# Order DTOs Documentation

This document provides an overview of the Order-related Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The Order DTOs handle the complete order lifecycle from cart creation to order tracking, including payment processing and status management.

## 🗂️ DTO Components

### 1. OrderDTO.java
**Complete order information management**
- ✅ All order fields with customer and shipping details
- ✅ Rich business methods: `canBeCancelled()`, `isCompleted()`, `getStatusColor()`, `getStatusIcon()`
- ✅ Payment integration with order tracking URLs
- ✅ Support for both regular items and combo items

### 2. OrderCreateDTO.java
**Order creation from shopping cart**
- ✅ Cart item selection and conversion to orders
- ✅ Comprehensive validation: `isFormValid()`, `getValidationErrors()`
- ✅ Amount calculations: Total, discount, shipping, final amounts
- ✅ Payment method support: Cash on delivery and online payments

### 3. OrderItemDTO.java
**Individual order item details**
- ✅ Product information with pricing
- ✅ Price change tracking: Current vs order price comparison
- ✅ Product availability: Stock status and reorder capabilities
- ✅ Display helpers: Formatted prices, URLs, product summaries

### 4. OrderComboItemDTO.java
**Combo deal items in orders**
- ✅ Combo information with savings calculations
- ✅ Individual and total savings amounts
- ✅ Combo availability: Active status and reorder capabilities
- ✅ Deal indicators: Best value badges and savings displays

### 5. OrderStatusHistoryDTO.java
**Order status tracking timeline**
- ✅ Complete order status history
- ✅ User attribution: System vs user vs admin updates
- ✅ Time formatting: `getTimeAgo()`, recent update detection
- ✅ Visual indicators: Status icons, colors, and importance levels

### 6. PaymentDTO.java
**Payment processing information**
- ✅ Complete payment processing details
- ✅ Multiple payment methods: Cash, cards, mobile payments, bank transfers
- ✅ Payment status tracking with visual indicators
- ✅ Transaction management: Transaction IDs, refund capabilities

### 7. CartDTO.java
**Shopping cart item management**
- ✅ Product information with pricing and availability
- ✅ Stock management: Stock status, quantity limits, availability
- ✅ Price calculations: Individual and total pricing with discounts
- ✅ Cart operations: Add/remove, selection, validation

### 8. CartComboDTO.java
**Combo deals in shopping cart**
- ✅ Combo information with time-limited deals
- ✅ Savings tracking: Individual and total savings calculations
- ✅ Availability management: Active periods, expiration tracking
- ✅ Deal indicators: Best deals, expiring soon warnings

## 🎯 Key Features

### Order Management
- ✅ **Complete order lifecycle** - From cart creation to delivery tracking
- ✅ **Status management** - Pending, confirmed, processing, shipped, delivered
- ✅ **Cancellation support** - Business rules for order cancellation
- ✅ **Payment integration** - Multiple payment methods and status tracking

### Cart Functionality
- ✅ **Mixed cart support** - Regular products and combo deals
- ✅ **Selection management** - Individual item selection for checkout
- ✅ **Stock validation** - Real-time stock checking and quantity limits
- ✅ **Price calculations** - Dynamic pricing with discounts and savings

### Payment Processing
- ✅ **Multiple payment methods** - Cash, cards, mobile payments, bank transfers
- ✅ **Payment status tracking** - Pending, processing, completed, failed, refunded
- ✅ **Transaction management** - Transaction IDs and payment history
- ✅ **Refund capabilities** - Business rules for refund eligibility

### Business Logic
- ✅ **Price change tracking** - Order price vs current price comparison
- ✅ **Savings calculations** - Individual and total savings for combos
- ✅ **Availability management** - Stock status and product availability
- ✅ **Time-based deals** - Combo expiration and availability periods

### Display & UI Support
- ✅ **Formatted data** - Prices, dates, quantities, savings
- ✅ **Status indicators** - Colors, icons, badges for all statuses
- ✅ **Navigation support** - URLs for products, combos, orders
- ✅ **Validation feedback** - Comprehensive error messages and validation

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Flattened relationships** - Related data included for easy display
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent structure** - Follows established patterns

## 🔗 Related Documentation

- [Product DTOs](../product/README.md)
- [User DTOs](../user/README.md)
- [Promotion DTOs](../promotion/README.md)
- [Shared DTOs](../shared/README.md)
- [System DTOs](../system/README.md)

## 📝 Usage Notes

All DTOs in this package are designed to work together to provide a complete order management system. They include comprehensive validation, business logic, and UI support features to ensure robust order processing and user experience.