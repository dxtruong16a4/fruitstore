package com.fruitstore.dto.request.discount;

import com.fruitstore.domain.discount.DiscountType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CreateDiscountRequest DTO
 */
public class CreateDiscountRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCreateDiscountRequest() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDescription("Welcome discount 10%");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setMinOrderAmount(new BigDecimal("100000.00"));
        request.setMaxDiscountAmount(new BigDecimal("50000.00"));
        request.setUsageLimit(100);
        request.setStartDate(LocalDateTime.now().plusDays(1));
        request.setEndDate(LocalDateTime.now().plusDays(30));
        request.setIsActive(true);

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidCreateDiscountRequestWithConstructor() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest(
                "WELCOME10",
                "Welcome discount 10%",
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00")
        );

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCode()).isEqualTo("WELCOME10");
        assertThat(request.getDescription()).isEqualTo("Welcome discount 10%");
        assertThat(request.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(request.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    public void testValidCreateDiscountRequestWithAllFields() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(30);
        
        CreateDiscountRequest request = new CreateDiscountRequest(
                "WELCOME10",
                "Welcome discount 10%",
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("100000.00"),
                new BigDecimal("50000.00"),
                100,
                startDate,
                endDate,
                true
        );

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCode()).isEqualTo("WELCOME10");
        assertThat(request.getDescription()).isEqualTo("Welcome discount 10%");
        assertThat(request.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(request.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(request.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(request.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(request.getUsageLimit()).isEqualTo(100);
        assertThat(request.getStartDate()).isEqualTo(startDate);
        assertThat(request.getEndDate()).isEqualTo(endDate);
        assertThat(request.getIsActive()).isTrue();
    }

    @Test
    public void testInvalidCreateDiscountRequestMissingCode() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setDescription("Welcome discount 10%");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code is required"));
    }

    @Test
    public void testInvalidCreateDiscountRequestBlankCode() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code is required"));
    }

    @Test
    public void testInvalidCreateDiscountRequestCodeTooLong() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("A".repeat(51)); // 51 characters
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount code must not exceed 50 characters"));
    }

    @Test
    public void testInvalidCreateDiscountRequestMissingDiscountType() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount type is required"));
    }

    @Test
    public void testInvalidCreateDiscountRequestMissingDiscountValue() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount value is required"));
    }

    @Test
    public void testInvalidCreateDiscountRequestNegativeDiscountValue() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("-10.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount value must be greater than 0"));
    }

    @Test
    public void testInvalidCreateDiscountRequestZeroDiscountValue() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount value must be greater than 0"));
    }

    @Test
    public void testInvalidCreateDiscountRequestNegativeMinOrderAmount() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setMinOrderAmount(new BigDecimal("-100.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Minimum order amount must be 0 or greater"));
    }

    @Test
    public void testInvalidCreateDiscountRequestNegativeMaxDiscountAmount() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setMaxDiscountAmount(new BigDecimal("-100.00"));

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Maximum discount amount must be greater than 0"));
    }

    @Test
    public void testInvalidCreateDiscountRequestZeroUsageLimit() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setUsageLimit(0);

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Usage limit must be greater than 0"));
    }

    @Test
    public void testInvalidCreateDiscountRequestDescriptionTooLong() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setDescription("A".repeat(2001)); // 2001 characters

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Description must not exceed 2000 characters"));
    }

    @Test
    public void testValidCreateDiscountRequestWithNullOptionalFields() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest();
        request.setCode("WELCOME10");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("10.00"));
        request.setMinOrderAmount(null);
        request.setMaxDiscountAmount(null);
        request.setUsageLimit(null);
        request.setStartDate(null);
        request.setEndDate(null);

        // When
        Set<ConstraintViolation<CreateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testCreateDiscountRequestToString() {
        // Given
        CreateDiscountRequest request = new CreateDiscountRequest("WELCOME10", "Welcome discount", 
                DiscountType.PERCENTAGE, new BigDecimal("10.00"));

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("WELCOME10");
        assertThat(result).contains("Welcome discount");
        assertThat(result).contains("PERCENTAGE");
        assertThat(result).contains("10.00");
    }
}
