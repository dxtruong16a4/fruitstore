package com.fruitstore.domain.discount;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Discount entity
 * Tests entity creation, validation, and business logic
 */
class DiscountEntityTest {

    private ValidatorFactory factory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testDiscountEntityCreation() {
        // Given
        Discount discount = new Discount();
        discount.setCode("TEST10");
        discount.setDescription("Test discount");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));
        discount.setMinOrderAmount(new BigDecimal("100.00"));
        discount.setMaxDiscountAmount(new BigDecimal("50.00"));
        discount.setUsageLimit(100);
        discount.setUsedCount(0);
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(30));
        discount.setIsActive(true);

        // Then
        assertNotNull(discount);
        assertEquals("TEST10", discount.getCode());
        assertEquals("Test discount", discount.getDescription());
        assertEquals(DiscountType.PERCENTAGE, discount.getDiscountType());
        assertEquals(new BigDecimal("10.00"), discount.getDiscountValue());
        assertEquals(new BigDecimal("100.00"), discount.getMinOrderAmount());
        assertEquals(new BigDecimal("50.00"), discount.getMaxDiscountAmount());
        assertEquals(100, discount.getUsageLimit());
        assertEquals(0, discount.getUsedCount());
        assertTrue(discount.getIsActive());
    }

    @Test
    void testDiscountConstructor() {
        // When
        Discount discount = new Discount("TEST10", "Test discount", DiscountType.PERCENTAGE, new BigDecimal("10.00"));

        // Then
        assertNotNull(discount);
        assertEquals("TEST10", discount.getCode());
        assertEquals("Test discount", discount.getDescription());
        assertEquals(DiscountType.PERCENTAGE, discount.getDiscountType());
        assertEquals(new BigDecimal("10.00"), discount.getDiscountValue());
    }

    @Test
    void testDiscountValidation_ValidDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setCode("TEST10");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        // Then
        assertTrue(violations.isEmpty(), "Valid discount should have no violations");
    }

    @Test
    void testDiscountValidation_BlankCode() {
        // Given
        Discount discount = new Discount();
        discount.setCode("");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));

        // When
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount code is required")));
    }

    @Test
    void testDiscountValidation_NullDiscountValue() {
        // Given
        Discount discount = new Discount();
        discount.setCode("TEST10");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(null);

        // When
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount value is required")));
    }

    @Test
    void testDiscountValidation_NegativeDiscountValue() {
        // Given
        Discount discount = new Discount();
        discount.setCode("TEST10");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("-5.00"));

        // When
        Set<ConstraintViolation<Discount>> violations = validator.validate(discount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount value must be greater than 0")));
    }

    @Test
    void testIsCurrentlyActive_ActiveDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(30));

        // When & Then
        assertTrue(discount.isCurrentlyActive());
    }

    @Test
    void testIsCurrentlyActive_InactiveDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(false);
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(30));

        // When & Then
        assertFalse(discount.isCurrentlyActive());
    }

    @Test
    void testIsCurrentlyActive_ExpiredDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setStartDate(LocalDateTime.now().minusDays(30));
        discount.setEndDate(LocalDateTime.now().minusDays(1));

        // When & Then
        assertFalse(discount.isCurrentlyActive());
    }

    @Test
    void testIsCurrentlyActive_FutureDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setStartDate(LocalDateTime.now().plusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(30));

        // When & Then
        assertFalse(discount.isCurrentlyActive());
    }

    @Test
    void testCanBeUsed_UnlimitedUsage() {
        // Given
        Discount discount = new Discount();
        discount.setUsageLimit(null);
        discount.setUsedCount(100);

        // When & Then
        assertTrue(discount.canBeUsed());
    }

    @Test
    void testCanBeUsed_WithinLimit() {
        // Given
        Discount discount = new Discount();
        discount.setUsageLimit(100);
        discount.setUsedCount(50);

        // When & Then
        assertTrue(discount.canBeUsed());
    }

    @Test
    void testCanBeUsed_ExceededLimit() {
        // Given
        Discount discount = new Discount();
        discount.setUsageLimit(100);
        discount.setUsedCount(100);

        // When & Then
        assertFalse(discount.canBeUsed());
    }

    @Test
    void testIsValidForOrderAmount_MeetsMinimum() {
        // Given
        Discount discount = new Discount();
        discount.setMinOrderAmount(new BigDecimal("100.00"));

        // When & Then
        assertTrue(discount.isValidForOrderAmount(new BigDecimal("150.00")));
        assertTrue(discount.isValidForOrderAmount(new BigDecimal("100.00")));
        assertFalse(discount.isValidForOrderAmount(new BigDecimal("50.00")));
    }

    @Test
    void testIsValidForOrderAmount_NoMinimum() {
        // Given
        Discount discount = new Discount();
        discount.setMinOrderAmount(null);

        // When & Then
        assertTrue(discount.isValidForOrderAmount(new BigDecimal("50.00")));
        assertTrue(discount.isValidForOrderAmount(BigDecimal.ZERO));
    }

    @Test
    void testCalculateDiscountAmount_PercentageDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setUsageLimit(null);
        discount.setUsedCount(0);
        discount.setMinOrderAmount(null);
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));

        // When
        BigDecimal result = discount.calculateDiscountAmount(new BigDecimal("1000.00"));

        // Then
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    void testCalculateDiscountAmount_FixedAmountDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setUsageLimit(null);
        discount.setUsedCount(0);
        discount.setMinOrderAmount(null);
        discount.setDiscountType(DiscountType.FIXED_AMOUNT);
        discount.setDiscountValue(new BigDecimal("50.00"));

        // When
        BigDecimal result = discount.calculateDiscountAmount(new BigDecimal("1000.00"));

        // Then
        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    void testCalculateDiscountAmount_WithMaxDiscountLimit() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setUsageLimit(null);
        discount.setUsedCount(0);
        discount.setMinOrderAmount(null);
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("20.00"));
        discount.setMaxDiscountAmount(new BigDecimal("100.00"));

        // When
        BigDecimal result = discount.calculateDiscountAmount(new BigDecimal("1000.00"));

        // Then
        assertEquals(new BigDecimal("100.00"), result); // Capped at max discount amount
    }

    @Test
    void testCalculateDiscountAmount_DiscountExceedsOrderAmount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(true);
        discount.setUsageLimit(null);
        discount.setUsedCount(0);
        discount.setMinOrderAmount(null);
        discount.setDiscountType(DiscountType.FIXED_AMOUNT);
        discount.setDiscountValue(new BigDecimal("150.00"));

        // When
        BigDecimal result = discount.calculateDiscountAmount(new BigDecimal("100.00"));

        // Then
        assertEquals(new BigDecimal("100.00"), result); // Discount capped at order amount
    }

    @Test
    void testCalculateDiscountAmount_InactiveDiscount() {
        // Given
        Discount discount = new Discount();
        discount.setIsActive(false);
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));

        // When
        BigDecimal result = discount.calculateDiscountAmount(new BigDecimal("1000.00"));

        // Then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testIncrementUsedCount() {
        // Given
        Discount discount = new Discount();
        discount.setUsedCount(5);

        // When
        discount.incrementUsedCount();

        // Then
        assertEquals(6, discount.getUsedCount());
    }

    @Test
    void testDiscountToString() {
        // Given
        Discount discount = new Discount("TEST10", "Test discount", DiscountType.PERCENTAGE, new BigDecimal("10.00"));
        discount.setDiscountId(1L);

        // When
        String result = discount.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("TEST10"));
        assertTrue(result.contains("Test discount"));
        assertTrue(result.contains("PERCENTAGE"));
    }
}
