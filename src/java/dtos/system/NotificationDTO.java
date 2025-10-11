package dtos.system;

import java.time.LocalDateTime;

/**
 * DTO for notification data
 * Used for displaying user notifications with enhanced information
 */
public class NotificationDTO {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String type;
    private String typeDisplayName;
    private boolean isRead;
    private LocalDateTime createdAt;
    
    // User information (flattened for context)
    private String username;
    private String userFullName;
    private String userEmail;
    
    // Calculated values
    private boolean isRecent;
    private boolean isToday;
    private boolean isThisWeek;
    private String timeAgo;
    private String priority;
    private String priorityColor;

    // Constructors
    public NotificationDTO() {}

    public NotificationDTO(int notificationId, int userId, String title, String message, String type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = false;
    }

    // Getters and Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTypeDisplayName() { return typeDisplayName; }
    public void setTypeDisplayName(String typeDisplayName) { this.typeDisplayName = typeDisplayName; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public boolean isRecent() { return isRecent; }
    public void setRecent(boolean recent) { isRecent = recent; }

    public boolean isToday() { return isToday; }
    public void setToday(boolean today) { isToday = today; }

    public boolean isThisWeek() { return isThisWeek; }
    public void setThisWeek(boolean thisWeek) { this.isThisWeek = thisWeek; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getPriorityColor() { return priorityColor; }
    public void setPriorityColor(String priorityColor) { this.priorityColor = priorityColor; }

    // Business methods
    public String getDisplayTitle() {
        return title != null ? title : "No Title";
    }

    public String getDisplayMessage() {
        return message != null ? message : "No Message";
    }

    public String getShortMessage() {
        if (message != null && message.length() > 100) {
            return message.substring(0, 97) + "...";
        }
        return message;
    }

    public String getTypeDisplayNameOrDefault() {
        if (typeDisplayName != null && !typeDisplayName.trim().isEmpty()) {
            return typeDisplayName;
        }
        return getType();
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedDateTime() {
        return createdAt != null ? createdAt.toString() : "Unknown";
    }

    public boolean isCalculatedRecent() {
        if (createdAt == null) return false;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return createdAt.isAfter(oneHourAgo);
    }

    public boolean isCalculatedToday() {
        if (createdAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return createdAt.isAfter(today) && createdAt.isBefore(tomorrow);
    }

    public boolean isCalculatedThisWeek() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getCalculatedTimeAgo() {
        if (createdAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(createdAt, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        
        long days = hours / 24;
        if (days < 7) return days + " day" + (days == 1 ? "" : "s") + " ago";
        
        if (days < 30) return (days / 7) + " week" + (days / 7 == 1 ? "" : "s") + " ago";
        if (days < 365) return (days / 30) + " month" + (days / 30 == 1 ? "" : "s") + " ago";
        
        return (days / 365) + " year" + (days / 365 == 1 ? "" : "s") + " ago";
    }

    public boolean isOrderNotification() {
        return "order".equalsIgnoreCase(type);
    }

    public boolean isPaymentNotification() {
        return "payment".equalsIgnoreCase(type);
    }

    public boolean isPromotionNotification() {
        return "promotion".equalsIgnoreCase(type);
    }

    public boolean isSystemNotification() {
        return "system".equalsIgnoreCase(type);
    }

    public boolean isSecurityNotification() {
        return "security".equalsIgnoreCase(type);
    }

    public boolean isMarketingNotification() {
        return "marketing".equalsIgnoreCase(type);
    }

    public String getTypeIcon() {
        if (type == null) return "📢";
        
        switch (type.toLowerCase()) {
            case "order": return "📦";
            case "payment": return "💳";
            case "promotion": return "🎉";
            case "system": return "⚙️";
            case "security": return "🔒";
            case "marketing": return "📢";
            case "flash_sale": return "⚡";
            case "discount": return "💰";
            case "combo": return "🎁";
            case "delivery": return "🚚";
            case "review": return "⭐";
            default: return "📢";
        }
    }

    public String getTypeColor() {
        if (type == null) return "blue";
        
        switch (type.toLowerCase()) {
            case "order": return "green";
            case "payment": return "blue";
            case "promotion": return "orange";
            case "system": return "gray";
            case "security": return "red";
            case "marketing": return "purple";
            case "flash_sale": return "red";
            case "discount": return "green";
            case "combo": return "orange";
            case "delivery": return "blue";
            case "review": return "yellow";
            default: return "blue";
        }
    }

    public String getReadStatusIcon() {
        return isRead ? "✅" : "🔔";
    }

    public String getReadStatusColor() {
        return isRead ? "gray" : "blue";
    }

    public String getReadStatusText() {
        return isRead ? "Read" : "Unread";
    }

    public boolean isHighPriority() {
        return "security".equalsIgnoreCase(type) || 
               "payment".equalsIgnoreCase(type) ||
               "system".equalsIgnoreCase(type);
    }

    public boolean isMediumPriority() {
        return "order".equalsIgnoreCase(type) || 
               "delivery".equalsIgnoreCase(type);
    }

    public boolean isLowPriority() {
        return "marketing".equalsIgnoreCase(type) || 
               "promotion".equalsIgnoreCase(type);
    }

    public String getCalculatedPriority() {
        if (isHighPriority()) return "High";
        if (isMediumPriority()) return "Medium";
        if (isLowPriority()) return "Low";
        return "Normal";
    }

    public String getCalculatedPriorityColor() {
        if (isHighPriority()) return "red";
        if (isMediumPriority()) return "orange";
        if (isLowPriority()) return "blue";
        return "gray";
    }

    public String getNotificationUrl() {
        switch (type.toLowerCase()) {
            case "order": return "/orders";
            case "payment": return "/payments";
            case "promotion": return "/promotions";
            case "flash_sale": return "/flash-sales";
            case "discount": return "/discounts";
            case "combo": return "/combos";
            case "delivery": return "/tracking";
            case "review": return "/reviews";
            default: return "/notifications";
        }
    }

    public boolean isActionable() {
        return isOrderNotification() || isPaymentNotification() || 
               isPromotionNotification() || isSecurityNotification();
    }

    public String getActionText() {
        if (isOrderNotification()) return "View Order";
        if (isPaymentNotification()) return "View Payment";
        if (isPromotionNotification()) return "View Offer";
        if (isSecurityNotification()) return "Take Action";
        return "View Details";
    }

    public String getNotificationSummary() {
        return getTypeIcon() + " " + getDisplayTitle() + " - " + getCalculatedTimeAgo();
    }

    public String getNotificationBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (!isRead) {
            badges.append(" 🔔");
        }
        
        if (isHighPriority()) {
            badges.append(" ⚠️");
        }
        
        if (isCalculatedRecent()) {
            badges.append(" 🆕");
        }
        
        return badges.toString().trim();
    }

    public String getDisplayUserName() {
        if (userFullName != null && !userFullName.trim().isEmpty()) {
            return userFullName;
        }
        if (username != null && !username.trim().isEmpty()) {
            return username;
        }
        return "Unknown User";
    }

    public boolean hasAction() {
        return isActionable();
    }

    public String getNotificationType() {
        return getTypeDisplayNameOrDefault();
    }

    public boolean isExpired() {
        if (createdAt == null) return false;
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return createdAt.isBefore(thirtyDaysAgo);
    }

    public String getExpirationStatus() {
        if (isExpired()) return "Expired";
        return "Active";
    }

    public String getExpirationColor() {
        return isExpired() ? "red" : "green";
    }
}
