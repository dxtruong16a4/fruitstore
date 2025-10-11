# Shared DTOs Documentation

This document provides an overview of the Shared Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The Shared DTOs provide common functionality used across all modules including API responses, pagination, search criteria, file uploads, and validation. These DTOs ensure consistency and reusability throughout the application.

## 🗂️ DTO Components

### 1. ApiResponseDTO.java
**Standard API response wrapper**
- ✅ Consistent response format across all endpoints
- ✅ Static factory methods: `success()`, `error()`, `notFound()`, `unauthorized()`, etc.
- ✅ Error handling: Validation errors, error codes, status codes
- ✅ Rich business methods: `hasErrors()`, `addError()`, `getFieldError()`, `isRetryable()`

### 2. PaginationDTO.java
**Comprehensive pagination handling**
- ✅ Complete pagination information for all endpoints
- ✅ Smart page navigation: Page numbers, ellipsis, navigation buttons
- ✅ Range calculations: Start/end indices, remaining items, completion percentage
- ✅ Display helpers: Formatted page info, navigation displays, status indicators

### 3. SearchCriteriaDTO.java
**Advanced search and filter parameters**
- ✅ Comprehensive search and filter handling
- ✅ Multiple filter types: Query, categories, price range, date range, statuses
- ✅ Sorting support: Sort by field, sort order, validation
- ✅ Custom filters: Extensible custom filter system with metadata

### 4. FileUploadDTO.java
**Complete file upload management**
- ✅ Complete file upload handling across all endpoints
- ✅ File type detection: Image, video, audio, document, archive detection
- ✅ Metadata support: Dimensions, checksums, categories, upload tracking
- ✅ Security features: File validation, size limits, type checking

### 5. ValidationErrorDTO.java
**Comprehensive validation error reporting**
- ✅ Comprehensive form validation error reporting
- ✅ Error categorization: Required, format, size, range, custom error types
- ✅ Severity levels: Critical, error, warning, info with visual indicators
- ✅ Helpful messages: User-friendly error messages and resolution hints

## 🎯 Key Features

### API Response Management
- ✅ **Consistent format** - Standardized response structure across all endpoints
- ✅ **Error handling** - Comprehensive error reporting with codes and messages
- ✅ **Success responses** - Clean success response handling with data
- ✅ **HTTP status codes** - Proper status code mapping and handling

### Pagination System
- ✅ **Smart pagination** - Intelligent page number generation with ellipsis
- ✅ **Navigation support** - Previous/next page, page jumping, range display
- ✅ **Progress tracking** - Completion percentage, remaining items
- ✅ **Display optimization** - Compact and user-friendly pagination displays

### Search & Filtering
- ✅ **Multi-criteria search** - Query, categories, price, date, status filters
- ✅ **Flexible sorting** - Multiple sort fields with ascending/descending
- ✅ **Custom filters** - Extensible filter system for specialized needs
- ✅ **Validation support** - Input validation and error reporting

### File Upload Management
- ✅ **File type detection** - Automatic detection of image, video, audio, document types
- ✅ **Metadata handling** - Dimensions, file size, upload tracking, categories
- ✅ **Security features** - File validation, size limits, type checking
- ✅ **Display optimization** - Formatted file sizes, type icons, resolution info

### Validation Error System
- ✅ **Error categorization** - Required, format, size, range, custom error types
- ✅ **Severity levels** - Critical, error, warning, info with appropriate handling
- ✅ **User-friendly messages** - Helpful error messages and resolution hints
- ✅ **Field mapping** - Specific field errors with context and suggestions

### Display & UI Support
- ✅ **Visual indicators** - Icons, colors, badges for all statuses and types
- ✅ **Formatted data** - Proper formatting for dates, sizes, ranges, percentages
- ✅ **Status indicators** - Success/error states, progress indicators
- ✅ **User guidance** - Helpful messages, hints, and resolution suggestions

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Generic support** - Type-safe generic implementations
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent structure** - Follows established patterns

## 🚀 Advanced Features

### ApiResponseDTO Advanced Features
- ✅ **Static factory methods** - Convenient creation methods for common responses
- ✅ **Error aggregation** - Multiple error handling with field-specific errors
- ✅ **Metadata support** - Pagination and search metadata integration
- ✅ **Retry logic** - Retryable error detection for client handling

### PaginationDTO Advanced Features
- ✅ **Smart page numbers** - Intelligent page number generation with ellipsis
- ✅ **Progress tracking** - Completion percentage and remaining items
- ✅ **Navigation optimization** - Efficient navigation button generation
- ✅ **Range calculations** - Start/end indices and item ranges

### SearchCriteriaDTO Advanced Features
- ✅ **Multi-type search** - Product, order, user, promotion search types
- ✅ **Custom filters** - Extensible filter system with metadata
- ✅ **Validation support** - Input validation with error reporting
- ✅ **Smart summaries** - Comprehensive search summary generation

### FileUploadDTO Advanced Features
- ✅ **Type detection** - Automatic file type detection with icons
- ✅ **Dimension analysis** - Image dimensions, aspect ratios, orientation
- ✅ **Security features** - File validation, size limits, type checking
- ✅ **Metadata system** - Extensible metadata with custom properties

### ValidationErrorDTO Advanced Features
- ✅ **Error categorization** - Automatic error type detection
- ✅ **Helpful messages** - User-friendly error messages and hints
- ✅ **Resolution guidance** - Specific resolution suggestions
- ✅ **Severity management** - Critical, error, warning, info levels

## 🔗 Related Documentation

- [Order DTOs](../order/README.md)
- [Product DTOs](../product/README.md)
- [User DTOs](../user/README.md)
- [Promotion DTOs](../promotion/README.md)
- [System DTOs](../system/README.md)

## 📝 Usage Notes

All Shared DTOs are designed to be reusable across all modules of the application. They provide consistent patterns for API responses, data handling, and user interaction. These DTOs form the foundation for consistent data exchange throughout the Fruit Store application.

## 🎯 Integration Benefits

### Consistency Across Modules
- Standardized API response formats
- Uniform pagination handling
- Consistent search and filter patterns
- Common file upload workflows

### Developer Experience
- Reduced code duplication
- Consistent error handling patterns
- Reusable validation logic
- Type-safe implementations

### User Experience
- Consistent UI patterns
- Uniform error messaging
- Standardized navigation
- Predictable interaction flows