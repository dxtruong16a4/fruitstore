package com.fruitstore.dto.response.discount;

import com.fruitstore.domain.discount.DiscountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DiscountResponse DTO
 */
public class DiscountResponseTest {

    @Test
    public void testValidDiscountResponse() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        DiscountResponse response = new DiscountResponse();
        response.setDiscountId(1L);
        response.setCode("WELCOME10");
        response.setDescription("Welcome discount 10%");
        response.setDiscountType(DiscountType.PERCENTAGE);
        response.setDiscountValue(new BigDecimal("10.00"));
        response.setMinOrderAmount(new BigDecimal("100000.00"));
        response.setMaxDiscountAmount(new BigDecimal("50000.00"));
        response.setUsageLimit(100);
        response.setUsedCount(10);
        response.setStartDate(now.plusDays(1));
        response.setEndDate(now.plusDays(30));
        response.setIsActive(true);
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Then
        assertThat(response.getDiscountId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getDescription()).isEqualTo("Welcome discount 10%");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(response.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(100);
        assertThat(response.getUsedCount()).isEqualTo(10);
        assertThat(response.getStartDate()).isEqualTo(now.plusDays(1));
        assertThat(response.getEndDate()).isEqualTo(now.plusDays(30));
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testDiscountResponseWithConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        DiscountResponse response = new DiscountResponse(
                1L, "WELCOME10", "Welcome discount 10%", DiscountType.PERCENTAGE,
                new BigDecimal("10.00"), new BigDecimal("100000.00"), new BigDecimal("50000.00"),
                100, 10, now.plusDays(1), now.plusDays(30), true, now, now
        );

        // Then
        assertThat(response.getDiscountId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getDescription()).isEqualTo("Welcome discount 10%");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(response.getMaxDiscountAmount()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(100);
        assertThat(response.getUsedCount()).isEqualTo(10);
        assertThat(response.getStartDate()).isEqualTo(now.plusDays(1));
        assertThat(response.getEndDate()).isEqualTo(now.plusDays(30));
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testDiscountResponseWithNullFields() {
        // Given
        DiscountResponse response = new DiscountResponse();
        response.setDiscountId(1L);
        response.setCode("WELCOME10");
        response.setDiscountType(DiscountType.PERCENTAGE);
        response.setDiscountValue(new BigDecimal("10.00"));

        // Then
        assertThat(response.getDiscountId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("WELCOME10");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getDescription()).isNull();
        assertThat(response.getMinOrderAmount()).isNull();
        assertThat(response.getMaxDiscountAmount()).isNull();
        assertThat(response.getUsageLimit()).isNull();
        assertThat(response.getUsedCount()).isNull();
        assertThat(response.getStartDate()).isNull();
        assertThat(response.getEndDate()).isNull();
        assertThat(response.getIsActive()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    public void testDiscountResponseWithFixedAmountDiscount() {
        // Given
        DiscountResponse response = new DiscountResponse();
        response.setDiscountId(2L);
        response.setCode("FRUIT50K");
        response.setDescription("Fixed discount 50k");
        response.setDiscountType(DiscountType.FIXED_AMOUNT);
        response.setDiscountValue(new BigDecimal("50000.00"));
        response.setMinOrderAmount(new BigDecimal("200000.00"));
        response.setUsageLimit(200);
        response.setUsedCount(50);
        response.setIsActive(true);

        // Then
        assertThat(response.getDiscountId()).isEqualTo(2L);
        assertThat(response.getCode()).isEqualTo("FRUIT50K");
        assertThat(response.getDescription()).isEqualTo("Fixed discount 50k");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(response.getDiscountValue()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(response.getMinOrderAmount()).isEqualTo(new BigDecimal("200000.00"));
        assertThat(response.getUsageLimit()).isEqualTo(200);
        assertThat(response.getUsedCount()).isEqualTo(50);
        assertThat(response.getIsActive()).isTrue();
    }

    @Test
    public void testDiscountResponseToString() {
        // Given
        DiscountResponse response = new DiscountResponse();
        response.setDiscountId(1L);
        response.setCode("WELCOME10");
        response.setDescription("Welcome discount");
        response.setDiscountType(DiscountType.PERCENTAGE);
        response.setDiscountValue(new BigDecimal("10.00"));

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("1");
        assertThat(result).contains("WELCOME10");
        assertThat(result).contains("Welcome discount");
        assertThat(result).contains("PERCENTAGE");
        assertThat(result).contains("10.00");
    }
}
