package dtos.shared;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for standard API response wrapper
 * Used for consistent API response format across all endpoints
 */
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ValidationErrorDTO> errors;
    private String errorCode;
    private LocalDateTime timestamp;
    private int statusCode;
    private String requestId;
    private Object metadata;

    // Constructors
    public ApiResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public ApiResponseDTO(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO(boolean success, String message, T data, int statusCode) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    // Static factory methods for common responses
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "Operation completed successfully", data, 200);
    }

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(true, message, data, 200);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null, 500);
    }

    public static <T> ApiResponseDTO<T> error(String message, int statusCode) {
        return new ApiResponseDTO<>(false, message, null, statusCode);
    }

    public static <T> ApiResponseDTO<T> error(String message, List<ValidationErrorDTO> errors) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>(false, message, null, 400);
        response.setErrors(errors);
        return response;
    }

    public static <T> ApiResponseDTO<T> notFound(String message) {
        return new ApiResponseDTO<>(false, message, null, 404);
    }

    public static <T> ApiResponseDTO<T> unauthorized(String message) {
        return new ApiResponseDTO<>(false, message, null, 401);
    }

    public static <T> ApiResponseDTO<T> forbidden(String message) {
        return new ApiResponseDTO<>(false, message, null, 403);
    }

    public static <T> ApiResponseDTO<T> badRequest(String message) {
        return new ApiResponseDTO<>(false, message, null, 400);
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public List<ValidationErrorDTO> getErrors() { return errors; }
    public void setErrors(List<ValidationErrorDTO> errors) { this.errors = errors; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public Object getMetadata() { return metadata; }
    public void setMetadata(Object metadata) { this.metadata = metadata; }

    // Business methods
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public boolean hasData() {
        return data != null;
    }

    public boolean hasMetadata() {
        return metadata != null;
    }

    public boolean hasRequestId() {
        return requestId != null && !requestId.trim().isEmpty();
    }

    public String getFormattedTimestamp() {
        return timestamp != null ? timestamp.toString() : "Unknown";
    }

    public String getStatusText() {
        if (success) return "Success";
        return "Error";
    }

    public String getStatusColor() {
        if (success) return "green";
        if (statusCode >= 500) return "red";
        if (statusCode >= 400) return "orange";
        return "blue";
    }

    public String getStatusIcon() {
        if (success) return "✅";
        if (statusCode >= 500) return "❌";
        if (statusCode >= 400) return "⚠️";
        return "ℹ️";
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500;
    }

    public boolean isSuccessStatus() {
        return success && statusCode >= 200 && statusCode < 300;
    }

    public boolean isRedirection() {
        return statusCode >= 300 && statusCode < 400;
    }

    public String getErrorType() {
        if (statusCode >= 500) return "Server Error";
        if (statusCode == 404) return "Not Found";
        if (statusCode == 403) return "Forbidden";
        if (statusCode == 401) return "Unauthorized";
        if (statusCode >= 400) return "Client Error";
        return "Unknown Error";
    }

    public String getErrorTypeIcon() {
        switch (statusCode) {
            case 400: return "🔍";
            case 401: return "🔐";
            case 403: return "🚫";
            case 404: return "❓";
            case 500: return "💥";
            default: return "⚠️";
        }
    }

    public int getErrorCount() {
        return errors != null ? errors.size() : 0;
    }

    public String getErrorSummary() {
        if (!hasErrors()) return "No errors";
        if (getErrorCount() == 1) return "1 error";
        return getErrorCount() + " errors";
    }

    public String getResponseSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getStatusIcon()).append(" ");
        summary.append(getMessage());
        
        if (hasErrors()) {
            summary.append(" (").append(getErrorSummary()).append(")");
        }
        
        return summary.toString();
    }

    public void addError(ValidationErrorDTO error) {
        if (errors == null) {
            errors = new java.util.ArrayList<>();
        }
        errors.add(error);
    }

    public void addError(String field, String message) {
        addError(new ValidationErrorDTO(field, message));
    }

    public void addError(String message) {
        addError("general", message);
    }

    public boolean hasFieldError(String field) {
        if (!hasErrors()) return false;
        return errors.stream().anyMatch(error -> field.equals(error.getField()));
    }

    public String getFieldError(String field) {
        if (!hasErrors()) return null;
        return errors.stream()
                .filter(error -> field.equals(error.getField()))
                .map(ValidationErrorDTO::getMessage)
                .findFirst()
                .orElse(null);
    }

    public void setPaginationMetadata(PaginationDTO pagination) {
        this.metadata = pagination;
    }

    public PaginationDTO getPaginationMetadata() {
        if (metadata instanceof PaginationDTO) {
            return (PaginationDTO) metadata;
        }
        return null;
    }

    public void setSearchMetadata(SearchCriteriaDTO searchCriteria) {
        this.metadata = searchCriteria;
    }

    public SearchCriteriaDTO getSearchMetadata() {
        if (metadata instanceof SearchCriteriaDTO) {
            return (SearchCriteriaDTO) metadata;
        }
        return null;
    }

    public String getRequestTracking() {
        return "Request ID: " + (hasRequestId() ? requestId : "N/A") + 
               " | Timestamp: " + getFormattedTimestamp();
    }

    public String getDebugInfo() {
        StringBuilder debug = new StringBuilder();
        debug.append("Status: ").append(statusCode).append(" ").append(getStatusText()).append("\n");
        debug.append("Success: ").append(success).append("\n");
        debug.append("Message: ").append(message).append("\n");
        
        if (hasErrors()) {
            debug.append("Errors: ").append(getErrorCount()).append("\n");
        }
        
        if (hasRequestId()) {
            debug.append("Request ID: ").append(requestId).append("\n");
        }
        
        debug.append("Timestamp: ").append(getFormattedTimestamp());
        
        return debug.toString();
    }

    public boolean isRetryable() {
        return statusCode >= 500 || statusCode == 429; // Server errors or rate limiting
    }

    public String getRetryMessage() {
        if (isRetryable()) {
            return "Request failed. Please try again later.";
        }
        return "Request failed. Please check your input and try again.";
    }

    public String getRetryIcon() {
        return isRetryable() ? "🔄" : "❌";
    }
}
