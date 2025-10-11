package models;

import java.time.LocalDateTime;

public class SystemSetting {
    private int settingId;
    private String settingKey;
    private String settingValue;
    private String description;
    private LocalDateTime updatedAt;

    // Constructors
    public SystemSetting() {}

    public SystemSetting(String settingKey, String settingValue, String description) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getSettingId() { return settingId; }
    public void setSettingId(int settingId) { this.settingId = settingId; }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { 
        this.settingValue = settingValue; 
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public int getIntValue() {
        try {
            return Integer.parseInt(settingValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getBooleanValue() {
        return "true".equalsIgnoreCase(settingValue) || "1".equals(settingValue);
    }

    public double getDoubleValue() {
        try {
            return Double.parseDouble(settingValue);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}