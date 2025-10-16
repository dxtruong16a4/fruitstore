package com.fruitstore.dto.response.discount;

import com.fruitstore.domain.discount.DiscountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DiscountValidationResponse DTO
 */
public class DiscountValidationResponseTest {

    @Test
    public void testValidDiscountValidationResponse() {
        // Given
        DiscountValidationResponse response = new DiscountValidationResponse();
        response.setValid(true);
        response.setCode("WELCOME10");
        response.setMessage("Discount is valid");
        response.setDiscountType(DiscountType.PERCENTAGE);
        response.setDiscountValue(new BigDecimal("10.00"));
        response.setCalculatedDiscountAmount(new BigDecimal("15000.00"));
        response.setMinOrderAmount(new BigDecimal("100000.00"));
        response.setMaxDiscountAmount(new BigDecimal("50000.00"));
        response.setUsageLimit(100);
        response.setUsedCount(10);
        response.setRemainingUsage(90);
        response.setDescription("Welcome discount 10%");

        // Then
        assertThat(response.isValid()).isTrue();
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getMessage()).isEqualTo("Discount is valid");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getCalculatedDiscountAmount()).isEqualTo(new BigDecimal("15000.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(response.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(100);
        assertThat(response.getUsedCount()).isEqualTo(10);
        assertThat(response.getRemainingUsage()).isEqualTo(90);
        assertThat(response.getDescription()).isEqualTo("Welcome discount 10%");
    }

    @Test
    public void testDiscountValidationResponseWithConstructor() {
        // Given
        DiscountValidationResponse response = new DiscountValidationResponse(
                true, "WELCOME10", "Discount is valid", DiscountType.PERCENTAGE,
                new BigDecimal("10.00"), new BigDecimal("15000.00"),
                new BigDecimal("100000.00"), new BigDecimal("50000.00"),
                100, 10, 90, "Welcome discount 10%"
        );

        // Then
        assertThat(response.isValid()).isTrue();
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getMessage()).isEqualTo("Discount is valid");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getCalculatedDiscountAmount()).isEqualTo(new BigDecimal("15000.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(response.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(100);
        assertThat(response.getUsedCount()).isEqualTo(10);
        assertThat(response.getRemainingUsage()).isEqualTo(90);
        assertThat(response.getDescription()).isEqualTo("Welcome discount 10%");
    }

    @Test
    public void testDiscountValidationResponseInvalid() {
        // Given
        DiscountValidationResponse response = new DiscountValidationResponse();
        response.setValid(false);
        response.setCode("EXPIRED10");
        response.setMessage("Discount has expired");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("EXPIRED10");
        assertThat(response.getMessage()).isEqualTo("Discount has expired");
    }

    @Test
    public void testStaticFactoryMethodValid() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.valid(
                "WELCOME10", DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                new BigDecimal("15000.00"), new BigDecimal("100000.00"),
                new BigDecimal("50000.00"), 100, 10, 90, "Welcome discount 10%"
        );

        // Then
        assertThat(response.isValid()).isTrue();
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getMessage()).isEqualTo("Discount is valid");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getCalculatedDiscountAmount()).isEqualTo(new BigDecimal("15000.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(response.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(100);
        assertThat(response.getUsedCount()).isEqualTo(10);
        assertThat(response.getRemainingUsage()).isEqualTo(90);
        assertThat(response.getDescription()).isEqualTo("Welcome discount 10%");
    }

    @Test
    public void testStaticFactoryMethodInvalid() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.invalid("WELCOME10", "Custom error message");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getMessage()).isEqualTo("Custom error message");
    }

    @Test
    public void testStaticFactoryMethodNotFound() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.notFound("NOTFOUND");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("NOTFOUND");
        assertThat(response.getMessage()).isEqualTo("Discount code not found");
    }

    @Test
    public void testStaticFactoryMethodExpired() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.expired("EXPIRED10");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("EXPIRED10");
        assertThat(response.getMessage()).isEqualTo("Discount has expired");
    }

    @Test
    public void testStaticFactoryMethodNotStarted() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.notStarted("FUTURE10");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("FUTURE10");
        assertThat(response.getMessage()).isEqualTo("Discount has not started yet");
    }

    @Test
    public void testStaticFactoryMethodInactive() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.inactive("INACTIVE10");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("INACTIVE10");
        assertThat(response.getMessage()).isEqualTo("Discount is inactive");
    }

    @Test
    public void testStaticFactoryMethodUsageLimitReached() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.usageLimitReached("LIMIT10");

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("LIMIT10");
        assertThat(response.getMessage()).isEqualTo("Discount usage limit has been reached");
    }

    @Test
    public void testStaticFactoryMethodInsufficientOrderAmount() {
        // When
        DiscountValidationResponse response = DiscountValidationResponse.insufficientOrderAmount("MIN10", new BigDecimal("100000.00"));

        // Then
        assertThat(response.isValid()).isFalse();
        assertThat(response.getCode()).isEqualTo("MIN10");
        assertThat(response.getMessage()).isEqualTo("Order amount must be at least 100000.00 to use this discount");
    }

    @Test
    public void testDiscountValidationResponseWithFixedAmountDiscount() {
        // Given
        DiscountValidationResponse response = DiscountValidationResponse.valid(
                "FRUIT50K", DiscountType.FIXED_AMOUNT, new BigDecimal("50000.00"),
                new BigDecimal("50000.00"), new BigDecimal("200000.00"),
                null, 200, 50, 150, "Fixed discount 50k"
        );

        // Then
        assertThat(response.isValid()).isTrue();
        assertThat(response.getCode()).isEqualTo("FRUIT50K");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getCalculatedDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("200000.00"));
        assertThat(response.getMaxDiscountAmount()).isNull();
        assertThat(response.getUsageLimit()).isEqualTo(200);
        assertThat(response.getUsedCount()).isEqualTo(50);
        assertThat(response.getRemainingUsage()).isEqualTo(150);
        assertThat(response.getDescription()).isEqualTo("Fixed discount 50k");
    }

    @Test
    public void testDiscountValidationResponseToString() {
        // Given
        DiscountValidationResponse response = DiscountValidationResponse.valid(
                "WELCOME10", DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                new BigDecimal("15000.00"), new BigDecimal("100000.00"),
                new BigDecimal("50000.00"), 100, 10, 90, "Welcome discount 10%"
        );

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("true");
        assertThat(result).contains("WELCOME10");
        assertThat(result).contains("Discount is valid");
        assertThat(result).contains("PERCENTAGE");
        assertThat(result).contains("10.00");
        assertThat(result).contains("15000.00");
    }
}
