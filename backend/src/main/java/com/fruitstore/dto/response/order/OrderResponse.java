package com.fruitstore.dto.response.order;

import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.dto.response.user.UserSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order response
 */
public class OrderResponse {

    private Long orderId;
    private String orderNumber;
    private UserSummaryResponse user;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String phoneNumber;
    private String customerName;
    private String customerEmail;
    private String notes;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> orderItems;
    private Integer totalItems;
    private Integer itemCount;

    // Constructors
    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String orderNumber, UserSummaryResponse user, OrderStatus status, 
                        BigDecimal totalAmount, String shippingAddress, String phoneNumber, 
                        String customerName, String customerEmail, String notes, 
                        LocalDateTime shippedAt, LocalDateTime deliveredAt, LocalDateTime cancelledAt, 
                        LocalDateTime createdAt, LocalDateTime updatedAt, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.user = user;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.notes = notes;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = orderItems;
        this.totalItems = orderItems != null ? orderItems.stream().mapToInt(OrderItemResponse::getQuantity).sum() : 0;
        this.itemCount = orderItems != null ? orderItems.size() : 0;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public void setUser(UserSummaryResponse user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(LocalDateTime shippedAt) {
        this.shippedAt = shippedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
        this.totalItems = orderItems != null ? orderItems.stream().mapToInt(OrderItemResponse::getQuantity).sum() : 0;
        this.itemCount = orderItems != null ? orderItems.size() : 0;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", user=" + user +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", notes='" + notes + '\'' +
                ", shippedAt=" + shippedAt +
                ", deliveredAt=" + deliveredAt +
                ", cancelledAt=" + cancelledAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", orderItems=" + orderItems +
                ", totalItems=" + totalItems +
                ", itemCount=" + itemCount +
                '}';
    }
}
