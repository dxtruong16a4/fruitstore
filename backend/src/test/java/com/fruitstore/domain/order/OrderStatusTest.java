package com.fruitstore.domain.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderStatus enum
 * Tests all enum values, business methods, and conversion methods
 */
@DisplayName("OrderStatus Tests")
class OrderStatusTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        // Verify all expected enum values exist
        OrderStatus[] values = OrderStatus.values();
        assertEquals(4, values.length);
        
        assertEquals(OrderStatus.PENDING, values[0]);
        assertEquals(OrderStatus.CONFIRMED, values[1]);
        assertEquals(OrderStatus.DELIVERED, values[2]);
        assertEquals(OrderStatus.CANCELLED, values[3]);
    }

    @Test
    @DisplayName("Should return correct string values")
    void shouldReturnCorrectStringValues() {
        assertEquals("pending", OrderStatus.PENDING.getValue());
        assertEquals("confirmed", OrderStatus.CONFIRMED.getValue());
        assertEquals("delivered", OrderStatus.DELIVERED.getValue());
        assertEquals("cancelled", OrderStatus.CANCELLED.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"pending", "PENDING", "Pending", "PeNdInG"})
    @DisplayName("Should convert valid string values to PENDING enum (case insensitive)")
    void shouldConvertValidStringToPending(String input) {
        assertEquals(OrderStatus.PENDING, OrderStatus.fromValue(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"confirmed", "CONFIRMED", "Confirmed", "CoNfIrMeD"})
    @DisplayName("Should convert valid string values to CONFIRMED enum (case insensitive)")
    void shouldConvertValidStringToConfirmed(String input) {
        assertEquals(OrderStatus.CONFIRMED, OrderStatus.fromValue(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"delivered", "DELIVERED", "Delivered", "DeLiVeReD"})
    @DisplayName("Should convert valid string values to DELIVERED enum (case insensitive)")
    void shouldConvertValidStringToDelivered(String input) {
        assertEquals(OrderStatus.DELIVERED, OrderStatus.fromValue(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cancelled", "CANCELLED", "Cancelled", "CaNcElLeD"})
    @DisplayName("Should convert valid string values to CANCELLED enum (case insensitive)")
    void shouldConvertValidStringToCancelled(String input) {
        assertEquals(OrderStatus.CANCELLED, OrderStatus.fromValue(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "processing", "shipped", "completed", "", "null"})
    @DisplayName("Should throw exception for invalid string values")
    void shouldThrowExceptionForInvalidStringValues(String input) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> OrderStatus.fromValue(input)
        );
        
        assertEquals("Invalid order status: " + input, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void shouldThrowExceptionForNullInput() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> OrderStatus.fromValue(null)
        );
        
        assertEquals("Invalid order status: null", exception.getMessage());
    }

    @Test
    @DisplayName("Should correctly identify cancellable statuses")
    void shouldCorrectlyIdentifyCancellableStatuses() {
        // PENDING and CONFIRMED can be cancelled
        assertTrue(OrderStatus.PENDING.canBeCancelled());
        assertTrue(OrderStatus.CONFIRMED.canBeCancelled());
        
        // DELIVERED and CANCELLED cannot be cancelled
        assertFalse(OrderStatus.DELIVERED.canBeCancelled());
        assertFalse(OrderStatus.CANCELLED.canBeCancelled());
    }

    @Test
    @DisplayName("Should correctly identify final state statuses")
    void shouldCorrectlyIdentifyFinalStateStatuses() {
        // DELIVERED and CANCELLED are final states
        assertTrue(OrderStatus.DELIVERED.isFinalState());
        assertTrue(OrderStatus.CANCELLED.isFinalState());
        
        // PENDING and CONFIRMED are not final states
        assertFalse(OrderStatus.PENDING.isFinalState());
        assertFalse(OrderStatus.CONFIRMED.isFinalState());
    }

    @Test
    @DisplayName("Should correctly identify active statuses")
    void shouldCorrectlyIdentifyActiveStatuses() {
        // All statuses except CANCELLED are active
        assertTrue(OrderStatus.PENDING.isActive());
        assertTrue(OrderStatus.CONFIRMED.isActive());
        assertTrue(OrderStatus.DELIVERED.isActive());
        
        // CANCELLED is not active
        assertFalse(OrderStatus.CANCELLED.isActive());
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("Should have consistent toString() method")
    void shouldHaveConsistentToString(OrderStatus status) {
        // toString() should return the enum name
        assertEquals(status.name(), status.toString());
    }

    @Test
    @DisplayName("Should have correct ordinal values")
    void shouldHaveCorrectOrdinalValues() {
        assertEquals(0, OrderStatus.PENDING.ordinal());
        assertEquals(1, OrderStatus.CONFIRMED.ordinal());
        assertEquals(2, OrderStatus.DELIVERED.ordinal());
        assertEquals(3, OrderStatus.CANCELLED.ordinal());
    }

    @Test
    @DisplayName("Should support valueOf method")
    void shouldSupportValueOfMethod() {
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
        assertEquals(OrderStatus.CONFIRMED, OrderStatus.valueOf("CONFIRMED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf("DELIVERED"));
        assertEquals(OrderStatus.CANCELLED, OrderStatus.valueOf("CANCELLED"));
    }

    @Test
    @DisplayName("Should throw exception for invalid valueOf")
    void shouldThrowExceptionForInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("pending"));
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf(""));
    }
}
