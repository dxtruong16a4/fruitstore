package dtos.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order information for display
 * Used for displaying order details in listings and detail pages
 */
public class OrderDTO {
    private int orderId;
    private int userId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal finalAmount;
    private String status;
    private String statusDisplayName;
    private int shippingAddressId;
    private String paymentMethod;
    private String paymentMethodDisplayName;
    private String notes;
    private LocalDate estimatedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    
    // User information (flattened)
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Shipping address information (flattened)
    private String shippingAddressFormatted;
    private String shippingContactName;
    private String shippingContactPhone;
    
    // Order items
    private List<OrderItemDTO> orderItems;
    private List<OrderComboItemDTO> orderComboItems;
    private int totalItems;
    private int totalQuantity;
    
    // Payment information
    private List<PaymentDTO> payments;
    private String paymentStatus;
    private boolean isPaid;
    
    // Status history
    private List<OrderStatusHistoryDTO> statusHistory;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(int orderId, String orderNumber, BigDecimal totalAmount, BigDecimal finalAmount, String status) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusDisplayName() { return statusDisplayName; }
    public void setStatusDisplayName(String statusDisplayName) { this.statusDisplayName = statusDisplayName; }

    public int getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(int shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentMethodDisplayName() { return paymentMethodDisplayName; }
    public void setPaymentMethodDisplayName(String paymentMethodDisplayName) { this.paymentMethodDisplayName = paymentMethodDisplayName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public LocalDate getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(LocalDate actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getShippingAddressFormatted() { return shippingAddressFormatted; }
    public void setShippingAddressFormatted(String shippingAddressFormatted) { this.shippingAddressFormatted = shippingAddressFormatted; }

    public String getShippingContactName() { return shippingContactName; }
    public void setShippingContactName(String shippingContactName) { this.shippingContactName = shippingContactName; }

    public String getShippingContactPhone() { return shippingContactPhone; }
    public void setShippingContactPhone(String shippingContactPhone) { this.shippingContactPhone = shippingContactPhone; }

    public List<OrderItemDTO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemDTO> orderItems) { this.orderItems = orderItems; }

    public List<OrderComboItemDTO> getOrderComboItems() { return orderComboItems; }
    public void setOrderComboItems(List<OrderComboItemDTO> orderComboItems) { this.orderComboItems = orderComboItems; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public List<PaymentDTO> getPayments() { return payments; }
    public void setPayments(List<PaymentDTO> payments) { this.payments = payments; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public List<OrderStatusHistoryDTO> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<OrderStatusHistoryDTO> statusHistory) { this.statusHistory = statusHistory; }

    // Business methods
    public boolean canBeCancelled() {
        return "pending".equalsIgnoreCase(status) || "confirmed".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "delivered".equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }

    public boolean isProcessing() {
        return "processing".equalsIgnoreCase(status);
    }

    public boolean isShipped() {
        return "shipped".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }

    public boolean isReturned() {
        return "returned".equalsIgnoreCase(status);
    }

    public String getFormattedTotalAmount() {
        return String.format("%.2f", totalAmount);
    }

    public String getFormattedFinalAmount() {
        return String.format("%.2f", finalAmount);
    }

    public String getFormattedDiscountAmount() {
        return String.format("%.2f", discountAmount);
    }

    public String getFormattedShippingFee() {
        return String.format("%.2f", shippingFee);
    }

    public String getFormattedOrderDate() {
        return orderDate != null ? orderDate.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedEstimatedDelivery() {
        return estimatedDeliveryDate != null ? estimatedDeliveryDate.toString() : "Not set";
    }

    public String getFormattedActualDelivery() {
        return actualDeliveryDate != null ? actualDeliveryDate.toString() : "Not delivered";
    }

    public String getOrderUrl() {
        return "/order/" + orderNumber;
    }

    public String getTrackingUrl() {
        return "/track/" + orderNumber;
    }

    public boolean hasDiscount() {
        return discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasShippingFee() {
        return shippingFee != null && shippingFee.compareTo(BigDecimal.ZERO) > 0;
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

    public String getPaymentStatusColor() {
        if (paymentStatus == null) return "gray";
        
        switch (paymentStatus.toLowerCase()) {
            case "pending": return "orange";
            case "processing": return "blue";
            case "completed": return "green";
            case "failed": return "red";
            case "cancelled": return "gray";
            case "refunded": return "purple";
            default: return "gray";
        }
    }

    public boolean isRecentOrder() {
        if (orderDate == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return orderDate.isAfter(oneWeekAgo);
    }

    public boolean isOverdue() {
        if (estimatedDeliveryDate == null) return false;
        return LocalDate.now().isAfter(estimatedDeliveryDate) && !isCompleted();
    }

    public String getDeliveryStatus() {
        if (isCompleted()) return "Delivered";
        if (isOverdue()) return "Overdue";
        if (isShipped()) return "In Transit";
        if (isProcessing()) return "Processing";
        return "Pending";
    }

    public boolean hasItems() {
        return orderItems != null && !orderItems.isEmpty();
    }

    public boolean hasComboItems() {
        return orderComboItems != null && !orderComboItems.isEmpty();
    }

    public boolean hasMultipleItemTypes() {
        return hasItems() && hasComboItems();
    }

    public String getOrderSummary() {
        StringBuilder summary = new StringBuilder();
        if (totalQuantity > 0) {
            summary.append(totalQuantity).append(" item");
            if (totalQuantity > 1) summary.append("s");
        } else {
            summary.append("Empty order");
        }
        summary.append(" - ").append(getFormattedFinalAmount());
        return summary.toString();
    }
}
