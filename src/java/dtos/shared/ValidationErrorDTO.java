package dtos.shared;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for validation error details
 * Used for form validation error reporting across all endpoints
 */
public class ValidationErrorDTO {
    private String field;
    private String message;
    private String errorCode;
    private String errorType;
    private Object rejectedValue;
    private Object[] arguments;
    private String defaultMessage;
    private String objectName;
    private String propertyPath;
    private LocalDateTime timestamp;
    private String severity;
    private Map<String, Object> metadata;

    // Constructors
    public ValidationErrorDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ValidationErrorDTO(String field, String message) {
        this();
        this.field = field;
        this.message = message;
    }

    public ValidationErrorDTO(String field, String message, String errorCode) {
        this();
        this.field = field;
        this.message = message;
        this.errorCode = errorCode;
    }

    public ValidationErrorDTO(String field, String message, String errorCode, String errorType) {
        this();
        this.field = field;
        this.message = message;
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    // Getters and Setters
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }

    public Object getRejectedValue() { return rejectedValue; }
    public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }

    public Object[] getArguments() { return arguments; }
    public void setArguments(Object[] arguments) { this.arguments = arguments; }

    public String getDefaultMessage() { return defaultMessage; }
    public void setDefaultMessage(String defaultMessage) { this.defaultMessage = defaultMessage; }

    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }

    public String getPropertyPath() { return propertyPath; }
    public void setPropertyPath(String propertyPath) { this.propertyPath = propertyPath; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // Business methods
    public String getDisplayField() {
        return field != null ? field : "general";
    }

    public String getDisplayMessage() {
        return message != null ? message : "Validation error";
    }

    public String getFormattedTimestamp() {
        return timestamp != null ? timestamp.toString() : "Unknown";
    }

    public boolean hasField() {
        return field != null && !field.trim().isEmpty();
    }

    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }

    public boolean hasErrorType() {
        return errorType != null && !errorType.trim().isEmpty();
    }

    public boolean hasRejectedValue() {
        return rejectedValue != null;
    }

    public boolean hasArguments() {
        return arguments != null && arguments.length > 0;
    }

    public boolean hasMetadata() {
        return metadata != null && !metadata.isEmpty();
    }

    public String getErrorTypeOrDefault() {
        return errorType != null ? errorType : "validation";
    }

    public String getSeverityOrDefault() {
        return severity != null ? severity : "error";
    }

    public boolean isFieldError() {
        return hasField();
    }

    public boolean isGlobalError() {
        return !hasField();
    }

    public boolean isRequiredError() {
        return "required".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "NotNull".equalsIgnoreCase(errorCode) ||
               "NotBlank".equalsIgnoreCase(errorCode) ||
               "NotEmpty".equalsIgnoreCase(errorCode);
    }

    public boolean isFormatError() {
        return "format".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "Pattern".equalsIgnoreCase(errorCode) ||
               "Email".equalsIgnoreCase(errorCode) ||
               "URL".equalsIgnoreCase(errorCode);
    }

    public boolean isSizeError() {
        return "size".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "Size".equalsIgnoreCase(errorCode) ||
               "Length".equalsIgnoreCase(errorCode) ||
               "Min".equalsIgnoreCase(errorCode) ||
               "Max".equalsIgnoreCase(errorCode);
    }

    public boolean isRangeError() {
        return "range".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "Range".equalsIgnoreCase(errorCode) ||
               "DecimalMin".equalsIgnoreCase(errorCode) ||
               "DecimalMax".equalsIgnoreCase(errorCode);
    }

    public boolean isCustomError() {
        return "custom".equalsIgnoreCase(getErrorTypeOrDefault());
    }

    public String getErrorTypeIcon() {
        switch (getErrorTypeOrDefault().toLowerCase()) {
            case "required": return "⚠️";
            case "format": return "📝";
            case "size": return "📏";
            case "range": return "📊";
            case "custom": return "🔧";
            case "security": return "🔒";
            case "business": return "💼";
            default: return "❌";
        }
    }

    public String getSeverityIcon() {
        switch (getSeverityOrDefault().toLowerCase()) {
            case "error": return "❌";
            case "warning": return "⚠️";
            case "info": return "ℹ️";
            case "critical": return "🚨";
            default: return "❌";
        }
    }

    public String getSeverityColor() {
        switch (getSeverityOrDefault().toLowerCase()) {
            case "error": return "red";
            case "warning": return "orange";
            case "info": return "blue";
            case "critical": return "darkred";
            default: return "red";
        }
    }

    public String getErrorTypeColor() {
        switch (getErrorTypeOrDefault().toLowerCase()) {
            case "required": return "red";
            case "format": return "orange";
            case "size": return "yellow";
            case "range": return "blue";
            case "custom": return "purple";
            case "security": return "darkred";
            case "business": return "brown";
            default: return "gray";
        }
    }

    public boolean isHighSeverity() {
        return "critical".equalsIgnoreCase(getSeverityOrDefault()) ||
               "error".equalsIgnoreCase(getSeverityOrDefault());
    }

    public boolean isMediumSeverity() {
        return "warning".equalsIgnoreCase(getSeverityOrDefault());
    }

    public boolean isLowSeverity() {
        return "info".equalsIgnoreCase(getSeverityOrDefault());
    }

    public String getFormattedRejectedValue() {
        if (!hasRejectedValue()) return "N/A";
        
        if (rejectedValue instanceof String) {
            String str = (String) rejectedValue;
            return str.length() > 50 ? str.substring(0, 47) + "..." : str;
        }
        
        return rejectedValue.toString();
    }

    public String getErrorSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (hasField()) {
            summary.append(getField()).append(": ");
        }
        
        summary.append(getDisplayMessage());
        
        if (hasRejectedValue()) {
            summary.append(" (got: ").append(getFormattedRejectedValue()).append(")");
        }
        
        return summary.toString();
    }

    public String getDetailedErrorSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getErrorTypeIcon()).append(" ");
        
        if (hasField()) {
            summary.append("Field '").append(getField()).append("': ");
        }
        
        summary.append(getDisplayMessage());
        
        if (hasErrorCode()) {
            summary.append(" [").append(errorCode).append("]");
        }
        
        return summary.toString();
    }

    public String getFieldDisplayName() {
        if (field == null) return "General";
        
        // Convert camelCase to readable format
        String display = field.replaceAll("([a-z])([A-Z])", "$1 $2");
        return display.substring(0, 1).toUpperCase() + display.substring(1);
    }

    public boolean isEmailValidation() {
        return "email".equalsIgnoreCase(errorCode) ||
               "Email".equalsIgnoreCase(errorCode) ||
               (hasField() && field.toLowerCase().contains("email"));
    }

    public boolean isPasswordValidation() {
        return (hasField() && field.toLowerCase().contains("password")) ||
               "password".equalsIgnoreCase(getErrorTypeOrDefault());
    }

    public boolean isPhoneValidation() {
        return (hasField() && field.toLowerCase().contains("phone")) ||
               "phone".equalsIgnoreCase(getErrorTypeOrDefault());
    }

    public boolean isDateValidation() {
        return "date".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "Date".equalsIgnoreCase(errorCode) ||
               (hasField() && field.toLowerCase().contains("date"));
    }

    public boolean isNumericValidation() {
        return "number".equalsIgnoreCase(getErrorTypeOrDefault()) ||
               "NumberFormatException".equalsIgnoreCase(errorCode) ||
               "DecimalMin".equalsIgnoreCase(errorCode) ||
               "DecimalMax".equalsIgnoreCase(errorCode);
    }

    public String getValidationCategory() {
        if (isEmailValidation()) return "Email";
        if (isPasswordValidation()) return "Password";
        if (isPhoneValidation()) return "Phone";
        if (isDateValidation()) return "Date";
        if (isNumericValidation()) return "Number";
        if (isRequiredError()) return "Required";
        if (isFormatError()) return "Format";
        if (isSizeError()) return "Size";
        return "General";
    }

    public String getValidationCategoryIcon() {
        switch (getValidationCategory()) {
            case "Email": return "📧";
            case "Password": return "🔐";
            case "Phone": return "📞";
            case "Date": return "📅";
            case "Number": return "🔢";
            case "Required": return "⚠️";
            case "Format": return "📝";
            case "Size": return "📏";
            default: return "❌";
        }
    }

    public void addMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new java.util.HashMap<>();
        }
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    public boolean hasMetadata(String key) {
        return metadata != null && metadata.containsKey(key);
    }

    public String getHelpfulMessage() {
        if (isRequiredError()) {
            return "This field is required and cannot be empty.";
        }
        
        if (isEmailValidation()) {
            return "Please enter a valid email address (e.g., user@example.com).";
        }
        
        if (isPasswordValidation()) {
            return "Password must be at least 8 characters long and contain letters and numbers.";
        }
        
        if (isPhoneValidation()) {
            return "Please enter a valid phone number.";
        }
        
        if (isDateValidation()) {
            return "Please enter a valid date.";
        }
        
        if (isNumericValidation()) {
            return "Please enter a valid number.";
        }
        
        if (isSizeError()) {
            return "Please check the length or size of your input.";
        }
        
        if (isFormatError()) {
            return "Please check the format of your input.";
        }
        
        return getDisplayMessage();
    }

    public boolean isResolvable() {
        return !isGlobalError() && hasField();
    }

    public String getResolutionHint() {
        if (!isResolvable()) return "Contact support for assistance.";
        
        if (isRequiredError()) {
            return "Fill in this field.";
        }
        
        if (isFormatError()) {
            return "Check the format and try again.";
        }
        
        if (isSizeError()) {
            return "Adjust the length or size.";
        }
        
        return "Please correct this field.";
    }

    public String getErrorId() {
        return (hasField() ? field : "global") + "_" + 
               (hasErrorCode() ? errorCode : "error") + "_" +
               timestamp.hashCode();
    }
}
