package com.fruitstore.dto.response.discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for discount usage response
 */
public class DiscountUsageResponse {

    private Long usageId;
    private Long discountId;
    private String discountCode;
    private Long userId;
    private String username;
    private Long orderId;
    private String orderNumber;
    private BigDecimal discountAmount;
    private LocalDateTime usedAt;

    // Constructors
    public DiscountUsageResponse() {
    }

    public DiscountUsageResponse(Long usageId, Long discountId, String discountCode, Long userId, String username,
                                Long orderId, String orderNumber, BigDecimal discountAmount, LocalDateTime usedAt) {
        this.usageId = usageId;
        this.discountId = discountId;
        this.discountCode = discountCode;
        this.userId = userId;
        this.username = username;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.discountAmount = discountAmount;
        this.usedAt = usedAt;
    }

    // Getters and Setters
    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(Long usageId) {
        this.usageId = usageId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    @Override
    public String toString() {
        return "DiscountUsageResponse{" +
                "usageId=" + usageId +
                ", discountId=" + discountId +
                ", discountCode='" + discountCode + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", discountAmount=" + discountAmount +
                ", usedAt=" + usedAt +
                '}';
    }
}
