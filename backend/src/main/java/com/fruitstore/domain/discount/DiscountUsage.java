package com.fruitstore.domain.discount;

import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DiscountUsage entity representing discount usage tracking
 * Maps to 'discount_usage' table in database
 */
@Entity
@Table(name = "discount_usage", indexes = {
    @Index(name = "idx_discount_id", columnList = "discount_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_used_at", columnList = "used_at")
})
public class DiscountUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    @NotNull(message = "Discount is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.00", message = "Discount amount must be 0 or greater")
    @Digits(integer = 8, fraction = 2, message = "Discount amount must have at most 8 integer digits and 2 decimal places")
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "used_at", nullable = false, updatable = false)
    private LocalDateTime usedAt;

    // JPA lifecycle callback
    @PrePersist
    protected void onCreate() {
        this.usedAt = LocalDateTime.now();
    }

    // Constructors
    public DiscountUsage() {
    }

    public DiscountUsage(Discount discount, User user, BigDecimal discountAmount) {
        this.discount = discount;
        this.user = user;
        this.discountAmount = discountAmount;
    }

    public DiscountUsage(Discount discount, User user, Order order, BigDecimal discountAmount) {
        this.discount = discount;
        this.user = user;
        this.order = order;
        this.discountAmount = discountAmount;
    }

    // Getters and Setters
    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(Long usageId) {
        this.usageId = usageId;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        return "DiscountUsage{" +
                "usageId=" + usageId +
                ", discount=" + (discount != null ? discount.getCode() : null) +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", order=" + (order != null ? order.getOrderId() : null) +
                ", discountAmount=" + discountAmount +
                ", usedAt=" + usedAt +
                '}';
    }
}
