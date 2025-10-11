# Promotion DTOs Documentation

This document provides an overview of the Promotion-related Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The Promotion DTOs handle all promotional activities including combo deals, discount codes, and flash sales. They provide comprehensive time-based management, savings calculations, and real-time tracking for promotional campaigns.

## 🗂️ DTO Components

### 1. ComboDTO.java
**Combo deal information and management**
- ✅ Complete combo deal details with pricing and availability
- ✅ Time-based management: Start/end dates, expiration tracking, time remaining
- ✅ Savings calculations: Individual and percentage savings with visual indicators
- ✅ Rich business methods: `canBePurchased()`, `getSavingsBadge()`, `getRemainingTimeDisplay()`

### 2. ComboItemDTO.java
**Individual items within combo deals**
- ✅ Individual items within combos with product information
- ✅ Product integration: Flattened product data for easy display
- ✅ Stock management: Availability checking and quantity validation
- ✅ Value tracking: Individual item values and combo contribution

### 3. DiscountDTO.java
**Discount code information and validation**
- ✅ Complete discount code details and validation
- ✅ Usage tracking: Usage limits, remaining usage, expiration dates
- ✅ Flexible discount types: Percentage and fixed amount discounts
- ✅ Business rules: Minimum order amounts, maximum discount limits

### 4. DiscountUsageDTO.java
**Discount usage history and tracking**
- ✅ Complete discount usage history and statistics
- ✅ User attribution: User and order information for each usage
- ✅ Time analysis: Recent usage detection, time-based categorization
- ✅ Value assessment: High/low value usage identification

### 5. FlashSaleDTO.java
**Time-limited flash sale events**
- ✅ Time-limited sale events with live tracking
- ✅ Real-time status: Live countdown, progress tracking, demand indicators
- ✅ Sales analytics: Total items, sold items, availability percentages
- ✅ Urgency indicators: Expiring soon warnings, high demand badges

### 6. FlashSaleItemDTO.java
**Individual products in flash sales**
- ✅ Individual products in flash sales with special pricing
- ✅ Limited quantity: Quantity limits, sold tracking, availability management
- ✅ Savings calculations: Flash sale pricing vs original pricing
- ✅ Demand tracking: Almost sold out warnings, high demand indicators

## 🎯 Key Features

### Combo Management
- ✅ **Time-based deals** - Active periods, expiration tracking, time remaining
- ✅ **Savings calculations** - Original vs combo pricing with percentage savings
- ✅ **Item composition** - Multiple products bundled with individual details
- ✅ **Availability management** - Active status and purchase eligibility

### Discount System
- ✅ **Multiple discount types** - Percentage and fixed amount discounts
- ✅ **Usage controls** - Usage limits, minimum order amounts, maximum discounts
- ✅ **Time-based validity** - Start/end dates with expiration tracking
- ✅ **Usage tracking** - Complete history with user and order attribution

### Flash Sale Events
- ✅ **Time-limited sales** - Real-time countdown and progress tracking
- ✅ **Limited quantity** - Quantity limits with sold/remaining tracking
- ✅ **Urgency indicators** - Expiring soon, almost sold out warnings
- ✅ **Demand analytics** - High demand detection and sales progress

### Business Logic
- ✅ **Pricing calculations** - Savings amounts and percentages
- ✅ **Availability rules** - Complex availability logic with multiple conditions
- ✅ **Time management** - Expiration tracking, countdown timers
- ✅ **Usage validation** - Eligibility checking and limit enforcement

### Display & UI Support
- ✅ **Formatted data** - Prices, dates, percentages, quantities
- ✅ **Status indicators** - Colors, icons, badges for all statuses
- ✅ **Progress tracking** - Sales progress, availability percentages
- ✅ **Urgency warnings** - Visual indicators for time-sensitive deals

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Flattened relationships** - Related data included for easy display
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent structure** - Follows established patterns

## 🔗 Related Documentation

- [Order DTOs](../order/README.md)
- [Product DTOs](../product/README.md)
- [User DTOs](../user/README.md)
- [Shared DTOs](../shared/README.md)
- [System DTOs](../system/README.md)

## 📝 Usage Notes

All Promotion DTOs are designed to work together to create a comprehensive promotional system. They handle time-sensitive deals, complex pricing calculations, and real-time tracking to maximize promotional effectiveness and user engagement.

## 🚀 Special Features

### Time-Based Promotions
- Real-time countdown timers for flash sales
- Expiration tracking for combo deals
- Time remaining calculations and displays
- Urgency indicators for expiring promotions

### Advanced Analytics
- Sales progress tracking for flash sales
- Usage statistics for discount codes
- Demand analysis and high-demand indicators
- Performance metrics for promotional campaigns

### User Experience
- Visual urgency indicators (badges, colors, warnings)
- Progress bars for flash sale availability
- Savings calculations with percentage displays
- Real-time status updates for promotional items

### Business Intelligence
- Usage pattern analysis for discount codes
- Performance tracking for combo deals
- Demand forecasting for flash sales
- ROI calculation support for promotional campaigns