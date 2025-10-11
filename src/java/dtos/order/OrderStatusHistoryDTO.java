package dtos.order;

import java.time.LocalDateTime;

/**
 * DTO for order status tracking history
 * Used for displaying order status change timeline
 */
public class OrderStatusHistoryDTO {
    private int historyId;
    private int orderId;
    private String status;
    private String statusDisplayName;
    private String notes;
    private Integer updatedBy;
    private LocalDateTime createdAt;
    
    // User information (flattened)
    private String updatedByUsername;
    private String updatedByFullName;
    private String updatedByRole;
    
    // Order information (flattened for context)
    private String orderNumber;

    // Constructors
    public OrderStatusHistoryDTO() {}

    public OrderStatusHistoryDTO(int historyId, int orderId, String status, LocalDateTime createdAt) {
        this.historyId = historyId;
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public OrderStatusHistoryDTO(int orderId, String status, String notes, Integer updatedBy) {
        this.orderId = orderId;
        this.status = status;
        this.notes = notes;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusDisplayName() { return statusDisplayName; }
    public void setStatusDisplayName(String statusDisplayName) { this.statusDisplayName = statusDisplayName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getUpdatedByUsername() { return updatedByUsername; }
    public void setUpdatedByUsername(String updatedByUsername) { this.updatedByUsername = updatedByUsername; }

    public String getUpdatedByFullName() { return updatedByFullName; }
    public void setUpdatedByFullName(String updatedByFullName) { this.updatedByFullName = updatedByFullName; }

    public String getUpdatedByRole() { return updatedByRole; }
    public void setUpdatedByRole(String updatedByRole) { this.updatedByRole = updatedByRole; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    // Business methods
    public boolean isSystemUpdate() {
        return updatedBy == null;
    }

    public boolean isUserUpdate() {
        return updatedBy != null;
    }

    public boolean isAdminUpdate() {
        return "ADMIN".equalsIgnoreCase(updatedByRole);
    }

    public boolean isCustomerUpdate() {
        return "CUSTOMER".equalsIgnoreCase(updatedByRole);
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedDateTime() {
        return createdAt != null ? createdAt.toString() : "Unknown";
    }

    public boolean isRecentUpdate() {
        if (createdAt == null) return false;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return createdAt.isAfter(oneHourAgo);
    }

    public boolean isTodayUpdate() {
        if (createdAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return createdAt.isAfter(today) && createdAt.isBefore(tomorrow);
    }

    public boolean isThisWeekUpdate() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getTimeAgo() {
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

    public String getUpdatedByName() {
        if (updatedByFullName != null && !updatedByFullName.trim().isEmpty()) {
            return updatedByFullName;
        }
        if (updatedByUsername != null && !updatedByUsername.trim().isEmpty()) {
            return updatedByUsername;
        }
        return "System";
    }

    public String getUpdatedByDisplayName() {
        if (isSystemUpdate()) {
            return "System";
        }
        return getUpdatedByName();
    }

    public String getStatusColor() {
        if (status == null) return "gray";
        
        switch (status.toLowerCase()) {
            case "pending": return "orange";
            case "confirmed": return "blue";
            case "processing": return "purple";
            case "shipped": return "cyan";
            case "delivered": return "green";
            case "cancelled": return "red";
            case "returned": return "brown";
            default: return "gray";
        }
    }

    public String getStatusIcon() {
        if (status == null) return "📋";
        
        switch (status.toLowerCase()) {
            case "pending": return "⏳";
            case "confirmed": return "✅";
            case "processing": return "⚙️";
            case "shipped": return "🚚";
            case "delivered": return "📦";
            case "cancelled": return "❌";
            case "returned": return "↩️";
            default: return "📋";
        }
    }

    public String getUpdateTypeIcon() {
        if (isSystemUpdate()) return "🤖";
        if (isAdminUpdate()) return "👨‍💼";
        if (isCustomerUpdate()) return "👤";
        return "👥";
    }

    public String getUpdateTypeLabel() {
        if (isSystemUpdate()) return "System";
        if (isAdminUpdate()) return "Admin";
        if (isCustomerUpdate()) return "Customer";
        return "User";
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public String getShortNotes() {
        if (!hasNotes()) return "";
        if (notes.length() > 100) {
            return notes.substring(0, 97) + "...";
        }
        return notes;
    }

    public boolean isImportantUpdate() {
        return "delivered".equalsIgnoreCase(status) || 
               "cancelled".equalsIgnoreCase(status) || 
               "returned".equalsIgnoreCase(status);
    }

    public boolean isProgressUpdate() {
        return "confirmed".equalsIgnoreCase(status) || 
               "processing".equalsIgnoreCase(status) || 
               "shipped".equalsIgnoreCase(status);
    }

    public String getUpdateImportance() {
        if (isImportantUpdate()) return "High";
        if (isProgressUpdate()) return "Medium";
        return "Low";
    }

    public String getUpdateDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append(getStatusIcon()).append(" ").append(getStatusDisplayName());
        
        if (hasNotes()) {
            desc.append(": ").append(getShortNotes());
        }
        
        return desc.toString();
    }

    public String getFullUpdateDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append(getUpdateTypeIcon()).append(" ");
        desc.append(getUpdatedByDisplayName());
        desc.append(" changed status to ");
        desc.append(getStatusIcon()).append(" ");
        desc.append(getStatusDisplayName());
        desc.append(" ").append(getTimeAgo());
        
        if (hasNotes()) {
            desc.append("\n📝 ").append(notes);
        }
        
        return desc.toString();
    }

    public boolean isFirstUpdate() {
        return historyId == 1; // Assuming first update has ID 1
    }

    public boolean isLatestUpdate() {
        // This would need to be determined by comparing with other history entries
        // For now, we'll assume it's handled by the service layer
        return false;
    }

    public String getTimelinePosition() {
        if (isFirstUpdate()) return "first";
        if (isLatestUpdate()) return "latest";
        return "middle";
    }
}
