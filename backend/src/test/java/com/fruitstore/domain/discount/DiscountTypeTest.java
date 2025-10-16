package com.fruitstore.domain.discount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DiscountType enum
 * Tests enum values, conversion methods, and utility methods
 */
class DiscountTypeTest {

    @Test
    void testDiscountTypeValues() {
        // Test enum values exist
        assertNotNull(DiscountType.PERCENTAGE);
        assertNotNull(DiscountType.FIXED_AMOUNT);
    }

    @Test
    void testDiscountTypeGetValue() {
        // Test string values
        assertEquals("percentage", DiscountType.PERCENTAGE.getValue());
        assertEquals("fixed_amount", DiscountType.FIXED_AMOUNT.getValue());
    }

    @Test
    void testDiscountTypeFromValue_ValidValues() {
        // Test valid string conversion
        assertEquals(DiscountType.PERCENTAGE, DiscountType.fromValue("percentage"));
        assertEquals(DiscountType.FIXED_AMOUNT, DiscountType.fromValue("fixed_amount"));
        
        // Test case insensitive conversion
        assertEquals(DiscountType.PERCENTAGE, DiscountType.fromValue("PERCENTAGE"));
        assertEquals(DiscountType.FIXED_AMOUNT, DiscountType.fromValue("FIXED_AMOUNT"));
        assertEquals(DiscountType.PERCENTAGE, DiscountType.fromValue("Percentage"));
        assertEquals(DiscountType.FIXED_AMOUNT, DiscountType.fromValue("Fixed_Amount"));
    }

    @Test
    void testDiscountTypeFromValue_InvalidValue() {
        // Test invalid value throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            DiscountType.fromValue("invalid_type");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            DiscountType.fromValue("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            DiscountType.fromValue(null);
        });
    }

    @Test
    void testIsPercentage() {
        // Test percentage check
        assertTrue(DiscountType.PERCENTAGE.isPercentage());
        assertFalse(DiscountType.FIXED_AMOUNT.isPercentage());
    }

    @Test
    void testIsFixedAmount() {
        // Test fixed amount check
        assertTrue(DiscountType.FIXED_AMOUNT.isFixedAmount());
        assertFalse(DiscountType.PERCENTAGE.isFixedAmount());
    }
}
