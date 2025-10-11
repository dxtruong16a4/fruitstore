package dtos.user;

import java.time.LocalDateTime;

/**
 * DTO for user activity log information
 * Used for displaying user activity tracking data
 */
public class UserActivityLogDTO {
    private int logId;
    private int userId;
    private String activityType;
    private String description;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
    
    // Additional display information
    private String username;
    private String userFullName;
    private String activityDisplayName;
    private String timeAgo;

    // Constructors
    public UserActivityLogDTO() {}

    public UserActivityLogDTO(int logId, int userId, String activityType, String description, 
                             LocalDateTime createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getActivityDisplayName() { return activityDisplayName; }
    public void setActivityDisplayName(String activityDisplayName) { this.activityDisplayName = activityDisplayName; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

    // Business methods
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toString() : "Unknown";
    }

    public String getFormattedDate() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedTime() {
        return createdAt != null ? createdAt.toLocalTime().toString() : "Unknown";
    }

    public String getDisplayName() {
        return userFullName != null && !userFullName.trim().isEmpty() ? userFullName : username;
    }

    public boolean isRecentActivity() {
        if (createdAt == null) return false;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return createdAt.isAfter(oneHourAgo);
    }

    public boolean isTodayActivity() {
        if (createdAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        return createdAt.isAfter(today);
    }

    public String getActivityIcon() {
        if (activityType == null) return "📝";
        
        switch (activityType.toLowerCase()) {
            case "login": return "🔑";
            case "logout": return "🚪";
            case "register": return "👤";
            case "view_product": return "👁️";
            case "add_to_cart": return "🛒";
            case "purchase": return "💰";
            case "review": return "⭐";
            default: return "📝";
        }
    }

    public String getActivityColor() {
        if (activityType == null) return "blue";
        
        switch (activityType.toLowerCase()) {
            case "login": return "green";
            case "logout": return "orange";
            case "register": return "purple";
            case "view_product": return "blue";
            case "add_to_cart": return "yellow";
            case "purchase": return "green";
            case "review": return "gold";
            default: return "gray";
        }
    }

    public boolean isSecurityRelated() {
        if (activityType == null) return false;
        String type = activityType.toLowerCase();
        return type.equals("login") || type.equals("logout") || type.equals("register");
    }

    public boolean isPurchaseRelated() {
        if (activityType == null) return false;
        String type = activityType.toLowerCase();
        return type.equals("add_to_cart") || type.equals("purchase");
    }

    public String getBrowserInfo() {
        if (userAgent == null) return "Unknown Browser";
        
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Opera")) return "Opera";
        
        return "Other Browser";
    }

    public String getDeviceType() {
        if (userAgent == null) return "Unknown Device";
        
        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablet";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iPhone")) return "iPhone";
        if (userAgent.contains("iPad")) return "iPad";
        
        return "Desktop";
    }
}
