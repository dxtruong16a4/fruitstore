# Product DTOs Documentation

This document provides an overview of the Product-related Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The Product DTOs handle complete product management including creation, updates, categorization, reviews, and analytics. They provide rich business logic and display support for the product catalog system.

## 🗂️ DTO Components

### 1. ProductDTO.java
**Complete product display information**
- ✅ All product fields with formatted display data
- ✅ Rich business methods: `getAvailabilityStatus()`, `getProductBadges()`, `getSavingsPercentage()`
- ✅ SEO-friendly: `getProductUrl()`, meta information support
- ✅ Display helpers: Formatted prices, stock status, product badges

### 2. ProductCreateDTO.java
**Product creation form data**
- ✅ All necessary fields for creating new products
- ✅ Comprehensive validation: `isFormValid()`, `getValidationErrors()`
- ✅ Data sanitization: Trim methods for all text fields
- ✅ Business logic: Price calculations, discount validation

### 3. ProductUpdateDTO.java
**Product update form data**
- ✅ Similar to create but with product ID validation
- ✅ Update-specific validation: ID validation, change detection
- ✅ Update type detection: `isStockUpdate()`, `isPriceUpdate()`, `isStatusUpdate()`
- ✅ Change tracking: `hasChanges()` method for detecting modifications

### 4. CategoryDTO.java
**Category information with hierarchy**
- ✅ Parent-child relationships management
- ✅ Related data integration: Subcategories, product counts
- ✅ Display features: Breadcrumbs, icons, URLs
- ✅ Business methods: `isPopular()`, `getBreadcrumb()`, `getCategoryIcon()`

### 5. SubcategoryDTO.java
**Subcategory with parent information**
- ✅ Flattened parent category data
- ✅ Navigation support: Full path URLs, breadcrumbs
- ✅ SEO optimization: Meta titles and descriptions
- ✅ Display helpers: Product counts, category icons

### 6. ReviewDTO.java
**Review with user information**
- ✅ Flattened user data for display
- ✅ Rich rating system: Stars, colors, labels
- ✅ Time formatting: `getTimeAgo()`, formatted dates
- ✅ Review features: Verified badges, helpful votes, comment summaries

### 7. ProductImageDTO.java
**Image management and optimization**
- ✅ Multiple size variants: Thumbnail, medium, large
- ✅ Image metadata: Dimensions, file size, type, orientation
- ✅ Display optimization: `getImageUrlForSize()`, aspect ratio calculations
- ✅ Image analysis: Quality assessment, optimization status

### 8. ProductStatsDTO.java
**Sales and performance analytics**
- ✅ Sales statistics: Total sold, last sold date, sales status
- ✅ Performance metrics: Views, wishlist adds, cart adds, conversion rates
- ✅ Rating integration: Average rating, review counts
- ✅ Analytics features: Trending detection, popularity levels, performance badges

## 🎯 Key Features

### Data Security & Validation
- ✅ **Input validation** - Comprehensive form validation methods
- ✅ **Data sanitization** - Trim methods for all text inputs
- ✅ **Business rule validation** - Price, discount, stock validations
- ✅ **Error reporting** - Detailed validation error messages

### Rich Business Logic
- ✅ **Pricing calculations** - Sale prices, discounts, savings
- ✅ **Stock management** - Availability status, low stock detection
- ✅ **Performance analytics** - Conversion rates, engagement scores
- ✅ **SEO optimization** - URLs, meta data, breadcrumbs

### Display & UI Support
- ✅ **Formatted data** - Prices, dates, ratings, file sizes
- ✅ **Status indicators** - Badges, colors, icons
- ✅ **Navigation support** - URLs, breadcrumbs, paths
- ✅ **Responsive images** - Multiple size variants

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Flattened relationships** - Related data included for easy display
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent structure** - Follows established patterns

## 🔗 Related Documentation

- [Order DTOs](../order/README.md)
- [User DTOs](../user/README.md)
- [Promotion DTOs](../promotion/README.md)
- [Shared DTOs](../shared/README.md)
- [System DTOs](../system/README.md)

## 📝 Usage Notes

All Product DTOs are designed to work together to provide a comprehensive product management system. They include advanced features like SEO optimization, performance analytics, and responsive image handling to ensure optimal user experience and search engine visibility.

## 🚀 Special Features

### SEO & Performance
- URL generation for products, categories, and subcategories
- Meta data support for search engines
- Breadcrumb navigation
- Image optimization with multiple sizes

### Analytics & Insights
- Sales performance tracking
- User engagement metrics
- Trending product detection
- Conversion rate analysis

### User Experience
- Rich rating and review system
- Product availability indicators
- Price change tracking
- Comprehensive validation feedback