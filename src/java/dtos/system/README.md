# System DTOs Documentation

This document provides an overview of the System-related Data Transfer Objects (DTOs) in the Fruit Store application.

## 📋 Overview

The System DTOs handle system-wide functionality including notifications and system settings management. They provide comprehensive configuration management and user notification systems with advanced categorization and security features.

## 🗂️ DTO Components

### 1. NotificationDTO.java
**Complete notification management system**
- ✅ Complete notification information with user context
- ✅ Time-based categorization: Recent, today, this week detection
- ✅ Type classification: Order, payment, promotion, security, system notifications
- ✅ Rich business methods: `getTimeAgo()`, `getTypeIcon()`, `getPriority()`, `isActionable()`

### 2. SystemSettingDTO.java
**System configuration management**
- ✅ Complete system settings management
- ✅ Type-aware handling: String, integer, boolean, double, JSON, URL, email types
- ✅ Security features: Sensitive setting detection, value masking
- ✅ Validation support: Type validation, error reporting, editability control

## 🎯 Key Features

### Notification Management
- ✅ **Multi-type notifications** - Order, payment, promotion, security, system, marketing
- ✅ **Priority classification** - High, medium, low priority with visual indicators
- ✅ **Time tracking** - Recent, today, this week categorization with time ago display
- ✅ **Read status management** - Read/unread tracking with visual indicators

### System Configuration
- ✅ **Multiple data types** - String, integer, boolean, double, JSON, URL, email
- ✅ **Category organization** - General, email, payment, security, database, API, UI, etc.
- ✅ **Security features** - Sensitive setting detection and value masking
- ✅ **Validation system** - Type validation with error reporting

### Business Logic
- ✅ **Smart categorization** - Automatic priority and type detection
- ✅ **Time calculations** - Intelligent time ago formatting and period detection
- ✅ **Type conversion** - Safe type conversion with error handling
- ✅ **Security awareness** - Automatic detection of sensitive settings

### Display & UI Support
- ✅ **Visual indicators** - Icons, colors, badges for all statuses and types
- ✅ **Formatted data** - Proper formatting for different data types
- ✅ **Status indicators** - Read/unread, valid/invalid, editable/read-only
- ✅ **Action support** - Actionable notifications with action text and URLs

### API-Ready Design
- ✅ **JSON serialization** - Optimized for REST APIs
- ✅ **Flattened relationships** - User information included for easy display
- ✅ **Null safety** - Proper null handling throughout
- ✅ **Consistent structure** - Follows established patterns

## 🔗 Related Documentation

- [Order DTOs](../order/README.md)
- [Product DTOs](../product/README.md)
- [User DTOs](../user/README.md)
- [Promotion DTOs](../promotion/README.md)
- [Shared DTOs](../shared/README.md)

## 📝 Usage Notes

System DTOs provide the foundation for system-wide functionality including user notifications and configuration management. They ensure consistent notification delivery and secure system settings management across the entire application.

## 🚀 Special Features

### Notification System
- **Multi-category notifications** with priority-based sorting
- **Time-based organization** for better user experience
- **Actionable notifications** with embedded action buttons
- **Visual priority indicators** for quick recognition

### System Configuration
- **Type-safe configuration** with automatic validation
- **Security-first approach** with sensitive data masking
- **Flexible data types** supporting complex configuration needs
- **Category-based organization** for easy management

### Security Features
- **Sensitive setting detection** with automatic value masking
- **Role-based access control** for configuration management
- **Audit trail support** for configuration changes
- **Secure data handling** throughout the system

### User Experience
- **Intelligent time formatting** with relative time display
- **Visual status indicators** for quick status recognition
- **Consistent notification patterns** across all modules
- **Actionable interface elements** for improved usability