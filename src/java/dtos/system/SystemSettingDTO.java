package dtos.system;

import java.time.LocalDateTime;

/**
 * DTO for system configuration values
 * Used for displaying and managing system settings
 */
public class SystemSettingDTO {
    private int settingId;
    private String settingKey;
    private String settingValue;
    private String description;
    private String category;
    private String dataType;
    private LocalDateTime updatedAt;
    private String updatedBy;
    
    // Calculated values
    private Object typedValue;
    private boolean isValid;
    private String validationError;

    // Constructors
    public SystemSettingDTO() {}

    public SystemSettingDTO(int settingId, String settingKey, String settingValue, String description) {
        this.settingId = settingId;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
    }

    public SystemSettingDTO(String settingKey, String settingValue, String description, String category) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public int getSettingId() { return settingId; }
    public void setSettingId(int settingId) { this.settingId = settingId; }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Object getTypedValue() { return typedValue; }
    public void setTypedValue(Object typedValue) { this.typedValue = typedValue; }

    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }

    public String getValidationError() { return validationError; }
    public void setValidationError(String validationError) { this.validationError = validationError; }

    // Business methods
    public String getDisplayKey() {
        return settingKey != null ? settingKey : "Unknown Setting";
    }

    public String getDisplayValue() {
        return settingValue != null ? settingValue : "";
    }

    public String getDisplayDescription() {
        return description != null ? description : "No description available";
    }

    public String getDisplayCategory() {
        return category != null ? category : "General";
    }

    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedDateTime() {
        return updatedAt != null ? updatedAt.toString() : "Unknown";
    }

    public boolean isStringType() {
        return "string".equalsIgnoreCase(dataType) || dataType == null;
    }

    public boolean isIntegerType() {
        return "integer".equalsIgnoreCase(dataType) || "int".equalsIgnoreCase(dataType);
    }

    public boolean isBooleanType() {
        return "boolean".equalsIgnoreCase(dataType) || "bool".equalsIgnoreCase(dataType);
    }

    public boolean isDoubleType() {
        return "double".equalsIgnoreCase(dataType) || "float".equalsIgnoreCase(dataType);
    }

    public boolean isJsonType() {
        return "json".equalsIgnoreCase(dataType);
    }

    public boolean isUrlType() {
        return "url".equalsIgnoreCase(dataType);
    }

    public boolean isEmailType() {
        return "email".equalsIgnoreCase(dataType);
    }

    public Integer getIntValue() {
        if (!isIntegerType()) return null;
        try {
            return Integer.parseInt(settingValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getBooleanValue() {
        if (!isBooleanType()) return null;
        return "true".equalsIgnoreCase(settingValue) || "1".equals(settingValue);
    }

    public Double getDoubleValue() {
        if (!isDoubleType()) return null;
        try {
            return Double.parseDouble(settingValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getFormattedValue() {
        if (settingValue == null) return "";
        
        if (isBooleanType()) {
            Boolean boolValue = getBooleanValue();
            return boolValue != null ? (boolValue ? "Enabled" : "Disabled") : "Invalid";
        }
        
        if (isIntegerType()) {
            Integer intValue = getIntValue();
            return intValue != null ? intValue.toString() : "Invalid";
        }
        
        if (isDoubleType()) {
            Double doubleValue = getDoubleValue();
            return doubleValue != null ? String.format("%.2f", doubleValue) : "Invalid";
        }
        
        return settingValue;
    }

    public String getDataTypeIcon() {
        if (isStringType()) return "📝";
        if (isIntegerType()) return "🔢";
        if (isBooleanType()) return "🔘";
        if (isDoubleType()) return "📊";
        if (isJsonType()) return "📋";
        if (isUrlType()) return "🔗";
        if (isEmailType()) return "📧";
        return "⚙️";
    }

    public String getDataTypeDisplayName() {
        if (isStringType()) return "Text";
        if (isIntegerType()) return "Number";
        if (isBooleanType()) return "True/False";
        if (isDoubleType()) return "Decimal";
        if (isJsonType()) return "JSON";
        if (isUrlType()) return "URL";
        if (isEmailType()) return "Email";
        return "Unknown";
    }

    public String getCategoryIcon() {
        if (category == null) return "⚙️";
        
        switch (category.toLowerCase()) {
            case "general": return "⚙️";
            case "email": return "📧";
            case "payment": return "💳";
            case "security": return "🔒";
            case "database": return "🗄️";
            case "api": return "🔌";
            case "ui": return "🎨";
            case "notification": return "🔔";
            case "shipping": return "🚚";
            case "inventory": return "📦";
            case "promotion": return "🎉";
            case "system": return "🖥️";
            default: return "⚙️";
        }
    }

    public String getCategoryColor() {
        if (category == null) return "gray";
        
        switch (category.toLowerCase()) {
            case "general": return "blue";
            case "email": return "green";
            case "payment": return "purple";
            case "security": return "red";
            case "database": return "orange";
            case "api": return "cyan";
            case "ui": return "pink";
            case "notification": return "yellow";
            case "shipping": return "teal";
            case "inventory": return "brown";
            case "promotion": return "orange";
            case "system": return "gray";
            default: return "blue";
        }
    }

    public boolean isRecentlyUpdated() {
        if (updatedAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return updatedAt.isAfter(oneWeekAgo);
    }

    public boolean isTodayUpdated() {
        if (updatedAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return updatedAt.isAfter(today) && updatedAt.isBefore(tomorrow);
    }

    public String getUpdateStatus() {
        if (isTodayUpdated()) return "Updated Today";
        if (isRecentlyUpdated()) return "Recently Updated";
        return "Older Update";
    }

    public String getUpdateStatusColor() {
        if (isTodayUpdated()) return "green";
        if (isRecentlyUpdated()) return "blue";
        return "gray";
    }

    public boolean isSensitiveSetting() {
        if (settingKey == null) return false;
        String key = settingKey.toLowerCase();
        return key.contains("password") || key.contains("secret") || 
               key.contains("key") || key.contains("token") || 
               key.contains("api") || key.contains("auth");
    }

    public String getMaskedValue() {
        if (!isSensitiveSetting()) return getDisplayValue();
        if (settingValue == null || settingValue.length() < 4) return "****";
        return settingValue.substring(0, 2) + "****" + settingValue.substring(settingValue.length() - 2);
    }

    public boolean isRequired() {
        if (settingKey == null) return false;
        String key = settingKey.toLowerCase();
        return key.contains("required") || key.contains("mandatory") || 
               key.contains("essential") || key.contains("critical");
    }

    public boolean isOptional() {
        return !isRequired();
    }

    public String getRequiredBadge() {
        return isRequired() ? "Required" : "Optional";
    }

    public String getRequiredColor() {
        return isRequired() ? "red" : "green";
    }

    public String getRequiredIcon() {
        return isRequired() ? "⚠️" : "✅";
    }

    public boolean isDefaultValue() {
        // This would need to be determined by comparing with default values
        // For now, we'll assume it's handled by the service layer
        return false;
    }

    public String getSettingSummary() {
        return getCategoryIcon() + " " + getDisplayKey() + ": " + getFormattedValue();
    }

    public String getSettingBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (isRequired()) {
            badges.append(" ").append(getRequiredIcon());
        }
        
        if (isSensitiveSetting()) {
            badges.append(" 🔒");
        }
        
        if (isRecentlyUpdated()) {
            badges.append(" 🆕");
        }
        
        return badges.toString().trim();
    }

    public String getValidationStatus() {
        if (isValid) return "Valid";
        return "Invalid";
    }

    public String getValidationColor() {
        return isValid ? "green" : "red";
    }

    public String getValidationIcon() {
        return isValid ? "✅" : "❌";
    }

    public boolean hasValidationError() {
        return validationError != null && !validationError.trim().isEmpty();
    }

    public String getDisplayValidationError() {
        return validationError != null ? validationError : "No validation error";
    }

    public boolean isEditable() {
        // Some system settings might be read-only
        if (settingKey == null) return true;
        String key = settingKey.toLowerCase();
        return !key.contains("readonly") && !key.contains("immutable") && 
               !key.contains("system") && !key.contains("internal");
    }

    public String getEditabilityStatus() {
        return isEditable() ? "Editable" : "Read Only";
    }

    public String getEditabilityColor() {
        return isEditable() ? "green" : "orange";
    }

    public String getEditabilityIcon() {
        return isEditable() ? "✏️" : "🔒";
    }

    public String getSettingType() {
        return getDataTypeDisplayName();
    }

    public boolean isNumericSetting() {
        return isIntegerType() || isDoubleType();
    }

    public boolean isTextSetting() {
        return isStringType() || isEmailType() || isUrlType();
    }

    public boolean isBooleanSetting() {
        return isBooleanType();
    }

    public boolean isComplexSetting() {
        return isJsonType();
    }
}
