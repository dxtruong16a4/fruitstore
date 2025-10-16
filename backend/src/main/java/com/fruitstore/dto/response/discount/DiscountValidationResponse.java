package com.fruitstore.dto.response.discount;

import com.fruitstore.domain.discount.DiscountType;

import java.math.BigDecimal;

/**
 * DTO for discount validation response
 */
public class DiscountValidationResponse {

    private boolean valid;
    private String code;
    private String message;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal calculatedDiscountAmount;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private Integer usageLimit;
    private Integer usedCount;
    private Integer remainingUsage;
    private String description;

    // Constructors
    public DiscountValidationResponse() {
    }

    public DiscountValidationResponse(boolean valid, String code, String message) {
        this.valid = valid;
        this.code = code;
        this.message = message;
    }

    public DiscountValidationResponse(boolean valid, String code, String message, DiscountType discountType,
                                     BigDecimal discountValue, BigDecimal calculatedDiscountAmount,
                                     BigDecimal minOrderAmount, BigDecimal maxDiscountAmount,
                                     Integer usageLimit, Integer usedCount, Integer remainingUsage,
                                     String description) {
        this.valid = valid;
        this.code = code;
        this.message = message;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.calculatedDiscountAmount = calculatedDiscountAmount;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.remainingUsage = remainingUsage;
        this.description = description;
    }

    // Static factory methods for common responses
    public static DiscountValidationResponse valid(String code, DiscountType discountType, BigDecimal discountValue,
                                                   BigDecimal calculatedDiscountAmount, BigDecimal minOrderAmount,
                                                   BigDecimal maxDiscountAmount, Integer usageLimit, Integer usedCount,
                                                   Integer remainingUsage, String description) {
        return new DiscountValidationResponse(true, code, "Discount is valid", discountType, discountValue,
                calculatedDiscountAmount, minOrderAmount, maxDiscountAmount, usageLimit, usedCount, remainingUsage, description);
    }

    public static DiscountValidationResponse invalid(String code, String message) {
        return new DiscountValidationResponse(false, code, message);
    }

    public static DiscountValidationResponse notFound(String code) {
        return new DiscountValidationResponse(false, code, "Discount code not found");
    }

    public static DiscountValidationResponse expired(String code) {
        return new DiscountValidationResponse(false, code, "Discount has expired");
    }

    public static DiscountValidationResponse notStarted(String code) {
        return new DiscountValidationResponse(false, code, "Discount has not started yet");
    }

    public static DiscountValidationResponse inactive(String code) {
        return new DiscountValidationResponse(false, code, "Discount is inactive");
    }

    public static DiscountValidationResponse usageLimitReached(String code) {
        return new DiscountValidationResponse(false, code, "Discount usage limit has been reached");
    }

    public static DiscountValidationResponse insufficientOrderAmount(String code, BigDecimal minOrderAmount) {
        return new DiscountValidationResponse(false, code, 
                "Order amount must be at least " + minOrderAmount + " to use this discount");
    }

    // Getters and Setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public BigDecimal getCalculatedDiscountAmount() {
        return calculatedDiscountAmount;
    }

    public void setCalculatedDiscountAmount(BigDecimal calculatedDiscountAmount) {
        this.calculatedDiscountAmount = calculatedDiscountAmount;
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

    public Integer getRemainingUsage() {
        return remainingUsage;
    }

    public void setRemainingUsage(Integer remainingUsage) {
        this.remainingUsage = remainingUsage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DiscountValidationResponse{" +
                "valid=" + valid +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", calculatedDiscountAmount=" + calculatedDiscountAmount +
                ", minOrderAmount=" + minOrderAmount +
                ", maxDiscountAmount=" + maxDiscountAmount +
                ", usageLimit=" + usageLimit +
                ", usedCount=" + usedCount +
                ", remainingUsage=" + remainingUsage +
                ", description='" + description + '\'' +
                '}';
    }
}
