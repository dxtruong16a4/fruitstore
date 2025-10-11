package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int userId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private int shippingAddressId;
    private User user; // For joined queries
    private ShippingAddress shippingAddress; // For joined queries
    private PaymentMethod paymentMethod;
    private String notes;
    private LocalDate estimatedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;

    // Constructors
    public Order() {}

    public Order(int userId, String orderNumber, BigDecimal totalAmount, 
                BigDecimal finalAmount, int shippingAddressId, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.shippingAddressId = shippingAddressId;
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.PENDING;
        this.discountAmount = BigDecimal.ZERO;
        this.shippingFee = BigDecimal.ZERO;
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(User user, String orderNumber, BigDecimal totalAmount, 
                BigDecimal finalAmount, ShippingAddress shippingAddress, PaymentMethod paymentMethod) {
        this.user = user;
        this.userId = user != null ? user.getUserId() : 0;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.shippingAddress = shippingAddress;
        this.shippingAddressId = shippingAddress != null ? shippingAddress.getAddressId() : 0;
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.PENDING;
        this.discountAmount = BigDecimal.ZERO;
        this.shippingFee = BigDecimal.ZERO;
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public int getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(int shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

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

    // Business methods
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
        
        if (newStatus == OrderStatus.DELIVERED) {
            this.actualDeliveryDate = LocalDate.now();
        }
    }

    public User getUser() { return user; }
    public void setUser(User user) { 
        this.user = user; 
        this.userId = user != null ? user.getUserId() : 0;
    }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { 
        this.shippingAddress = shippingAddress; 
        this.shippingAddressId = shippingAddress != null ? shippingAddress.getAddressId() : 0;
    }

    // Business methods with relationships
    public String getCustomerName() {
        return user != null ? user.getFullName() : "Unknown Customer";
    }

    public String getCustomerEmail() {
        return user != null ? user.getEmail() : null;
    }

    public String getShippingAddressFormatted() {
        if (shippingAddress != null) {
            return String.format("%s, %s, %s", 
                shippingAddress.getAddressLine1(), 
                shippingAddress.getCity(), 
                shippingAddress.getState());
        }
        return "No shipping address";
    }

    public String getShippingContactPhone() {
        return shippingAddress != null ? shippingAddress.getPhone() : null;
    }
}