package com.fruitstore.domain.discount;

import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
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
 * Test class for DiscountUsage entity
 * Tests entity creation, validation, and relationships
 */
class DiscountUsageEntityTest {

    private ValidatorFactory factory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testDiscountUsageEntityCreation() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();
        Order order = createTestOrder();
        BigDecimal discountAmount = new BigDecimal("50.00");

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(user);
        discountUsage.setOrder(order);
        discountUsage.setDiscountAmount(discountAmount);

        // Then
        assertNotNull(discountUsage);
        assertEquals(discount, discountUsage.getDiscount());
        assertEquals(user, discountUsage.getUser());
        assertEquals(order, discountUsage.getOrder());
        assertEquals(discountAmount, discountUsage.getDiscountAmount());
        assertNotNull(discountUsage.getUsedAt());
    }

    @Test
    void testDiscountUsageConstructor_WithOrder() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();
        Order order = createTestOrder();
        BigDecimal discountAmount = new BigDecimal("50.00");

        // When
        DiscountUsage discountUsage = new DiscountUsage(discount, user, order, discountAmount);

        // Then
        assertNotNull(discountUsage);
        assertEquals(discount, discountUsage.getDiscount());
        assertEquals(user, discountUsage.getUser());
        assertEquals(order, discountUsage.getOrder());
        assertEquals(discountAmount, discountUsage.getDiscountAmount());
    }

    @Test
    void testDiscountUsageConstructor_WithoutOrder() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();
        BigDecimal discountAmount = new BigDecimal("50.00");

        // When
        DiscountUsage discountUsage = new DiscountUsage(discount, user, discountAmount);

        // Then
        assertNotNull(discountUsage);
        assertEquals(discount, discountUsage.getDiscount());
        assertEquals(user, discountUsage.getUser());
        assertEquals(null, discountUsage.getOrder());
        assertEquals(discountAmount, discountUsage.getDiscountAmount());
    }

    @Test
    void testDiscountUsageValidation_ValidUsage() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();
        BigDecimal discountAmount = new BigDecimal("50.00");

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(user);
        discountUsage.setDiscountAmount(discountAmount);

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertTrue(violations.isEmpty(), "Valid discount usage should have no violations");
    }

    @Test
    void testDiscountUsageValidation_NullDiscount() {
        // Given
        User user = createTestUser();
        BigDecimal discountAmount = new BigDecimal("50.00");

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(null);
        discountUsage.setUser(user);
        discountUsage.setDiscountAmount(discountAmount);

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount is required")));
    }

    @Test
    void testDiscountUsageValidation_NullUser() {
        // Given
        Discount discount = createTestDiscount();
        BigDecimal discountAmount = new BigDecimal("50.00");

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(null);
        discountUsage.setDiscountAmount(discountAmount);

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("User is required")));
    }

    @Test
    void testDiscountUsageValidation_NullDiscountAmount() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(user);
        discountUsage.setDiscountAmount(null);

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount amount is required")));
    }

    @Test
    void testDiscountUsageValidation_NegativeDiscountAmount() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(user);
        discountUsage.setDiscountAmount(new BigDecimal("-10.00"));

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Discount amount must be 0 or greater")));
    }

    @Test
    void testDiscountUsageValidation_ZeroDiscountAmount() {
        // Given
        User user = createTestUser();
        Discount discount = createTestDiscount();

        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscount(discount);
        discountUsage.setUser(user);
        discountUsage.setDiscountAmount(BigDecimal.ZERO);

        // When
        Set<ConstraintViolation<DiscountUsage>> violations = validator.validate(discountUsage);

        // Then
        assertTrue(violations.isEmpty(), "Zero discount amount should be valid");
    }

    @Test
    void testDiscountUsageToString() {
        // Given
        User user = createTestUser();
        user.setUsername("testuser");
        Discount discount = createTestDiscount();
        discount.setCode("TEST10");
        BigDecimal discountAmount = new BigDecimal("50.00");

        DiscountUsage discountUsage = new DiscountUsage(discount, user, discountAmount);
        discountUsage.setUsageId(1L);

        // When
        String result = discountUsage.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("TEST10"));
        assertTrue(result.contains("50.00"));
    }

    // Helper methods to create test objects
    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        user.setFullName("Test User");
        user.setRole(UserRole.CUSTOMER);
        return user;
    }

    private Discount createTestDiscount() {
        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setCode("TEST10");
        discount.setDescription("Test discount");
        discount.setDiscountType(DiscountType.PERCENTAGE);
        discount.setDiscountValue(new BigDecimal("10.00"));
        discount.setIsActive(true);
        return discount;
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("500.00"));
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }
}
