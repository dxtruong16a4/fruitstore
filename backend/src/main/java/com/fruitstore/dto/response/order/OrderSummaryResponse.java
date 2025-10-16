package com.fruitstore.dto.response.order;

import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.dto.response.user.UserSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for order summary response (used in lists)
 */
public class OrderSummaryResponse {

    private Long orderId;
    private String orderNumber;
    private UserSummaryResponse user;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String customerName;
    private String customerEmail;
    private Integer totalItems;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public OrderSummaryResponse() {
    }

    public OrderSummaryResponse(Long orderId, String orderNumber, UserSummaryResponse user, 
                              OrderStatus status, BigDecimal totalAmount, String customerName, 
                              String customerEmail, Integer totalItems, Integer itemCount, 
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.user = user;
        this.status = status;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalItems = totalItems;
        this.itemCount = itemCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "OrderSummaryResponse{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", user=" + user +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", totalItems=" + totalItems +
                ", itemCount=" + itemCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
