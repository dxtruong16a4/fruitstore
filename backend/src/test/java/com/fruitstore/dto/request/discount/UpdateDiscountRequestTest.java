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
 * Test class for UpdateDiscountRequest DTO
 */
public class UpdateDiscountRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUpdateDiscountRequest() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDescription("Updated welcome discount 15%");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("15.00"));
        request.setMinOrderAmount(new BigDecimal("150000.00"));
        request.setMaxDiscountAmount(new BigDecimal("75000.00"));
        request.setUsageLimit(150);
        request.setStartDate(LocalDateTime.now().plusDays(1));
        request.setEndDate(LocalDateTime.now().plusDays(30));
        request.setIsActive(true);

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidUpdateDiscountRequestWithConstructor() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(30);
        
        UpdateDiscountRequest request = new UpdateDiscountRequest(
                "Updated welcome discount 15%",
                DiscountType.PERCENTAGE,
                new BigDecimal("15.00"),
                new BigDecimal("150000.00"),
                new BigDecimal("75000.00"),
                150,
                startDate,
                endDate,
                true
        );

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getDescription()).isEqualTo("Updated welcome discount 15%");
        assertThat(request.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(request.getDiscountValue()).isEqualTo(new BigDecimal("15.00"));
        assertThat(request.getMinOrderAmount()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(request.getMaxDiscountAmount()).isEqualTo(new BigDecimal("75000.00"));
        assertThat(request.getUsageLimit()).isEqualTo(150);
        assertThat(request.getStartDate()).isEqualTo(startDate);
        assertThat(request.getEndDate()).isEqualTo(endDate);
        assertThat(request.getIsActive()).isTrue();
    }

    @Test
    public void testValidUpdateDiscountRequestWithAllNullFields() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty(); // All fields are optional for updates
    }

    @Test
    public void testInvalidUpdateDiscountRequestNegativeDiscountValue() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDiscountValue(new BigDecimal("-10.00"));

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount value must be greater than 0"));
    }

    @Test
    public void testInvalidUpdateDiscountRequestZeroDiscountValue() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDiscountValue(BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Discount value must be greater than 0"));
    }

    @Test
    public void testInvalidUpdateDiscountRequestNegativeMinOrderAmount() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setMinOrderAmount(new BigDecimal("-100.00"));

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Minimum order amount must be 0 or greater"));
    }

    @Test
    public void testInvalidUpdateDiscountRequestNegativeMaxDiscountAmount() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setMaxDiscountAmount(new BigDecimal("-100.00"));

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Maximum discount amount must be greater than 0"));
    }

    @Test
    public void testInvalidUpdateDiscountRequestZeroUsageLimit() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setUsageLimit(0);

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Usage limit must be greater than 0"));
    }

    @Test
    public void testInvalidUpdateDiscountRequestDescriptionTooLong() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDescription("A".repeat(2001)); // 2001 characters

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Description must not exceed 2000 characters"));
    }

    @Test
    public void testValidUpdateDiscountRequestWithFixedAmountDiscount() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDescription("Fixed amount discount");
        request.setDiscountType(DiscountType.FIXED_AMOUNT);
        request.setDiscountValue(new BigDecimal("50000.00"));
        request.setMinOrderAmount(new BigDecimal("200000.00"));

        // When
        Set<ConstraintViolation<UpdateDiscountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testUpdateDiscountRequestToString() {
        // Given
        UpdateDiscountRequest request = new UpdateDiscountRequest();
        request.setDescription("Updated discount");
        request.setDiscountType(DiscountType.PERCENTAGE);
        request.setDiscountValue(new BigDecimal("15.00"));

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("Updated discount");
        assertThat(result).contains("PERCENTAGE");
        assertThat(result).contains("15.00");
    }
}
