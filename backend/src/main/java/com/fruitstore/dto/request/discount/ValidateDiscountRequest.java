package com.fruitstore.dto.request.discount;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for validating a discount code request
 */
public class ValidateDiscountRequest {

    @NotBlank(message = "Discount code is required")
    @Size(max = 50, message = "Discount code must not exceed 50 characters")
    private String code;

    @NotNull(message = "Order amount is required")
    @DecimalMin(value = "0.01", message = "Order amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Order amount must have at most 10 integer digits and 2 decimal places")
    private BigDecimal orderAmount;

    // Constructors
    public ValidateDiscountRequest() {
    }

    public ValidateDiscountRequest(String code, BigDecimal orderAmount) {
        this.code = code;
        this.orderAmount = orderAmount;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public String toString() {
        return "ValidateDiscountRequest{" +
                "code='" + code + '\'' +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
