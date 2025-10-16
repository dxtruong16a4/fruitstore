package com.fruitstore.domain.user;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User entity
 * Tests entity creation, validation, and mapping
 */
class UserEntityTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testUserEntityCreation() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword123");
        user.setFullName("Test User");
        user.setPhone("0123456789");
        user.setAddress("123 Test Street");
        user.setRole(UserRole.CUSTOMER);

        // Then
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getPassword());
        assertEquals("Test User", user.getFullName());
        assertEquals("0123456789", user.getPhone());
        assertEquals("123 Test Street", user.getAddress());
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testUserConstructor() {
        // When
        User user = new User("testuser", "test@example.com", "password123", "Test User");

        // Then
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Test User", user.getFullName());
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testUserRoleDefaultValue() {
        // When
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");

        // Then
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testUserValidation_ValidUser() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertTrue(violations.isEmpty(), "Valid user should have no violations");
    }

    @Test
    void testUserValidation_BlankUsername() {
        // Given
        User user = new User();
        user.setUsername("");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Username is required")));
    }

    @Test
    void testUserValidation_InvalidEmail() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("invalid-email");
        user.setPassword("password123");
        user.setFullName("Test User");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testUserValidation_BlankPassword() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("");
        user.setFullName("Test User");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    void testUserValidation_BlankFullName() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Full name is required")));
    }

    @Test
    void testUserRoleEnum() {
        // Test CUSTOMER role
        assertEquals("customer", UserRole.CUSTOMER.getValue());
        
        // Test ADMIN role
        assertEquals("admin", UserRole.ADMIN.getValue());
    }

    @Test
    void testUserRoleFromValue() {
        // Test valid values
        assertEquals(UserRole.CUSTOMER, UserRole.fromValue("customer"));
        assertEquals(UserRole.ADMIN, UserRole.fromValue("admin"));
        assertEquals(UserRole.CUSTOMER, UserRole.fromValue("CUSTOMER"));
        assertEquals(UserRole.ADMIN, UserRole.fromValue("ADMIN"));
    }

    @Test
    void testUserRoleFromValue_Invalid() {
        // Test invalid value
        assertThrows(IllegalArgumentException.class, () -> {
            UserRole.fromValue("invalid");
        });
    }

    @Test
    void testUserToString() {
        // Given
        User user = new User("testuser", "test@example.com", "password123", "Test User");
        user.setUserId(1L);

        // When
        String result = user.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("Test User"));
    }
}

