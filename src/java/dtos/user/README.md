# User DTOs Documentation

This document provides an overview of the User-related Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The User DTOs handle all user-related functionality including authentication, profile management, addresses, wishlists, and activity tracking. They prioritize security while providing rich user experience features and comprehensive data management.

## 🗂️ DTO Components

### 1. UserDTO.java
**Public user information management**
- ✅ Public user information (excludes password and sensitive data)
- ✅ Rich display methods: `getDisplayName()`, `getInitials()`, `getFormattedLastLogin()`
- ✅ Security-focused: No password field, role as String for serialization

### 2. UserRegistrationDTO.java
**User registration form data**
- ✅ Registration form data: Username, email, password, confirmPassword, etc.
- ✅ Validation methods: `isPasswordMatch()`, `isValidEmail()`, `isFormValid()`
- ✅ Data sanitization: `getTrimmedUsername()`, `getTrimmedEmail()`

### 3. UserLoginDTO.java
**User authentication credentials**
- ✅ Login credentials: Supports both username and email login
- ✅ Login type detection: `isEmailLogin()`, `isUsernameLogin()`
- ✅ Remember me functionality: rememberMe field

### 4. UserProfileDTO.java
**Complete user profile information**
- ✅ Complete profile information: Extended UserDTO with related data
- ✅ Profile statistics: Total orders, wishlist items, reviews
- ✅ Profile completion tracking: `getProfileCompletionPercentage()`
- ✅ Related data integration: Shipping addresses, member since date

### 5. AddressDTO.java
**Shipping address management**
- ✅ Complete address information
- ✅ Address formatting: `getFullAddress()`, `getShortAddress()`
- ✅ Validation methods: `isValid()`, `isCompleteAddress()`
- ✅ Display helpers: `getDisplayName()`, `getCityState()`

### 6. WishlistDTO.java
**User wishlist with product details**
- ✅ Wishlist with product details: Flattened product information
- ✅ Product availability: Stock status, availability checks
- ✅ Pricing information: Sale prices, savings calculations
- ✅ Business logic: `canBeAddedToCart()`, `isRecentlyAdded()`

### 7. UserActivityLogDTO.java
**User activity tracking and analytics**
- ✅ Activity tracking data: User actions and system events
- ✅ Rich display features: Icons, colors, time formatting
- ✅ Device detection: Browser info, device type
- ✅ Activity categorization: Security-related, purchase-related

## 🎯 Key Features

### Security & Data Protection
- ✅ **No sensitive data** - Passwords excluded from public DTOs
- ✅ **Role serialization** - Enums converted to strings for JSON
- ✅ **Data sanitization** - Trim methods for user input
- ✅ **Input validation** - Comprehensive form validation

### Business Logic Integration
- ✅ **Validation methods** - Built-in form validation
- ✅ **Display helpers** - Formatted dates, prices, names
- ✅ **Status calculations** - Profile completion, availability checks
- ✅ **Relationship handling** - Flattened related data where needed

### User Experience Features
- ✅ **Profile completion tracking** - Progress indicators for profile setup
- ✅ **Activity logging** - Comprehensive user activity tracking
- ✅ **Address management** - Multiple shipping addresses with validation
- ✅ **Wishlist functionality** - Product wishlist with availability tracking

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent naming** - Follows Java conventions
- ✅ **Documentation** - Clear JavaDoc comments

## 🔗 Related Documentation

- [Order DTOs](../order/README.md)
- [Product DTOs](../product/README.md)
- [Promotion DTOs](../promotion/README.md)
- [Shared DTOs](../shared/README.md)
- [System DTOs](../system/README.md)

## 📝 Usage Notes

User DTOs are designed with security as the top priority while providing rich user experience features. They handle the complete user lifecycle from registration through profile management and activity tracking.

## 🚀 Special Features

### Authentication & Security
- **Flexible login options** supporting both username and email
- **Secure credential handling** with password exclusion from public DTOs
- **Data sanitization** for all user inputs
- **Role-based access control** with proper serialization

### Profile Management
- **Comprehensive profile tracking** with completion percentage
- **Rich user statistics** including orders, wishlist, and reviews
- **Address management** with validation and formatting
- **Activity logging** with device and browser tracking

### User Experience
- **Profile completion guidance** with progress tracking
- **Wishlist management** with product availability checking
- **Address formatting** for display and validation
- **Activity categorization** for security and purchase tracking

### Business Intelligence
- **User behavior tracking** through activity logs
- **Profile analytics** with completion and engagement metrics
- **Security monitoring** through categorized activity logs
- **Purchase pattern analysis** through wishlist and order data