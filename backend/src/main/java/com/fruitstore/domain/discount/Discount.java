package com.fruitstore.domain.discount;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Discount entity representing discount codes in the system
 * Maps to 'discounts' table in database
 */
@Entity
@Table(name = "discounts", indexes = {
    @Index(name = "idx_code", columnList = "code"),
    @Index(name = "idx_is_active", columnList = "is_active"),
    @Index(name = "idx_dates", columnList = "start_date, end_date")
})
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @NotBlank(message = "Discount code is required")
    @Size(max = 50, message = "Discount code must not exceed 50 characters")
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Convert(converter = com.fruitstore.util.DiscountTypeConverter.class)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType = DiscountType.PERCENTAGE;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Discount value must have at most 8 integer digits and 2 decimal places")
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @DecimalMin(value = "0.00", message = "Minimum order amount must be 0 or greater")
    @Digits(integer = 8, fraction = 2, message = "Minimum order amount must have at most 8 integer digits and 2 decimal places")
    @Column(name = "min_order_amount", precision = 10, scale = 2)
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.01", message = "Maximum discount amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Maximum discount amount must have at most 8 integer digits and 2 decimal places")
    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Min(value = 1, message = "Usage limit must be greater than 0")
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Min(value = 0, message = "Used count must be 0 or greater")
    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiscountUsage> discountUsages = new ArrayList<>();

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.usedCount == null) {
            this.usedCount = 0;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.minOrderAmount == null) {
            this.minOrderAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Discount() {
    }

    public Discount(String code, String description, DiscountType discountType, BigDecimal discountValue) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
    }

    // Business logic methods
    /**
     * Check if the discount is currently active
     * @return true if discount is active and within date range
     */
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && 
               (startDate == null || now.isAfter(startDate) || now.isEqual(startDate)) &&
               (endDate == null || now.isBefore(endDate) || now.isEqual(endDate));
    }

    /**
     * Check if the discount can still be used
     * @return true if usage limit allows more uses
     */
    public boolean canBeUsed() {
        return usageLimit == null || usedCount < usageLimit;
    }

    /**
     * Check if the discount is valid for the given order amount
     * @param orderAmount the order amount to check
     * @return true if order amount meets minimum requirement
     */
    public boolean isValidForOrderAmount(BigDecimal orderAmount) {
        return minOrderAmount == null || orderAmount.compareTo(minOrderAmount) >= 0;
    }

    /**
     * Calculate the discount amount for the given order amount
     * @param orderAmount the order amount
     * @return the discount amount
     */
    public BigDecimal calculateDiscountAmount(BigDecimal orderAmount) {
        if (!isCurrentlyActive() || !canBeUsed() || !isValidForOrderAmount(orderAmount)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount;
        if (discountType.isPercentage()) {
            discountAmount = orderAmount.multiply(discountValue).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        } else {
            discountAmount = discountValue;
        }

        // Apply maximum discount amount limit if specified
        if (maxDiscountAmount != null && discountAmount.compareTo(maxDiscountAmount) > 0) {
            discountAmount = maxDiscountAmount;
        }

        // Ensure discount doesn't exceed order amount
        if (discountAmount.compareTo(orderAmount) > 0) {
            discountAmount = orderAmount;
        }

        // Ensure the result is properly scaled to 2 decimal places
        return discountAmount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Increment the used count
     */
    public void incrementUsedCount() {
        this.usedCount++;
    }

    // Getters and Setters
    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public List<DiscountUsage> getDiscountUsages() {
        return discountUsages;
    }

    public void setDiscountUsages(List<DiscountUsage> discountUsages) {
        this.discountUsages = discountUsages;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "discountId=" + discountId +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", minOrderAmount=" + minOrderAmount +
                ", maxDiscountAmount=" + maxDiscountAmount +
                ", usageLimit=" + usageLimit +
                ", usedCount=" + usedCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
