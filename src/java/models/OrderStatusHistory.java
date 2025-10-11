package models;

import java.time.LocalDateTime;

public class OrderStatusHistory {
    private int historyId;
    private int orderId;
    private String status;
    private String notes;
    private Integer updatedBy;
    private LocalDateTime createdAt;
    private Order order; // For joined queries
    private User updatedByUser; // For joined queries

    // Constructors
    public OrderStatusHistory() {}

    public OrderStatusHistory(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public OrderStatusHistory(int orderId, String status, String notes, Integer updatedBy) {
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

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public User getUpdatedByUser() { return updatedByUser; }
    public void setUpdatedByUser(User updatedByUser) { this.updatedByUser = updatedByUser; }

    // Business methods
    public boolean isSystemUpdate() {
        return updatedBy == null;
    }

    public boolean isUserUpdate() {
        return updatedBy != null;
    }

    public String getStatusDisplayName() {
        try {
            OrderStatus orderStatus = OrderStatus.fromString(status);
            return orderStatus.getDisplayName();
        } catch (IllegalArgumentException e) {
            return status; // Return raw status if not found in enum
        }
    }

    public boolean isRecentUpdate() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return createdAt.isAfter(oneHourAgo);
    }

    public String getFormattedCreatedAt() {
        return createdAt.toString();
    }
}
