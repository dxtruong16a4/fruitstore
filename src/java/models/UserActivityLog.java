package models;

import java.time.LocalDateTime;

public class UserActivityLog {
    private int logId;
    private int userId;
    private ActivityType activityType;
    private String description;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    // Constructors
    public UserActivityLog() {}

    public UserActivityLog(int userId, ActivityType activityType, String description, 
                          String ipAddress, String userAgent) {
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public ActivityType getActivityType() { return activityType; }
    public void setActivityType(ActivityType activityType) { this.activityType = activityType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}