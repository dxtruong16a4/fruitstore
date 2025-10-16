package com.fruitstore.dto.request.discount;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for ValidateDiscountRequest DTO
 */
public class ValidateDiscountRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidValidateDiscountRequest() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidValidateDiscountRequestWithConstructor() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest("WELCOME10", new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCode()).isEqualTo("WELCOME10");
        assertThat(request.getOrderAmount()).isEqualTo(new BigDecimal("150000.00"));
    }

    @Test
    public void testInvalidValidateDiscountRequestMissingCode() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setOrderAmount(new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code is required"));
    }

    @Test
    public void testInvalidValidateDiscountRequestBlankCode() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("");
        request.setOrderAmount(new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code is required"));
    }

    @Test
    public void testInvalidValidateDiscountRequestCodeTooLong() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("A".repeat(51)); // 51 characters
        request.setOrderAmount(new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code must not exceed 50 characters"));
    }

    @Test
    public void testInvalidValidateDiscountRequestMissingOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Order amount is required"));
    }

    @Test
    public void testInvalidValidateDiscountRequestNegativeOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(new BigDecimal("-100.00"));

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Order amount must be greater than 0"));
    }

    @Test
    public void testInvalidValidateDiscountRequestZeroOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Order amount must be greater than 0"));
    }

    @Test
    public void testValidValidateDiscountRequestWithLargeOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(new BigDecimal("999999999.99")); // Max valid amount

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidValidateDiscountRequestWithTooLargeOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(new BigDecimal("9999999999.99")); // Too many digits

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Order amount must have at most 10 integer digits"));
    }

    @Test
    public void testValidValidateDiscountRequestWithSmallOrderAmount() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest();
        request.setCode("WELCOME10");
        request.setOrderAmount(new BigDecimal("0.01")); // Minimum valid amount

        // When
        Set<ConstraintViolation<ValidateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidateDiscountRequestToString() {
        // Given
        ValidateDiscountRequest request = new ValidateDiscountRequest("WELCOME10", new BigDecimal("150000.00"));

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("WELCOME10");
        assertThat(result).contains("150000.00");
    }
}
