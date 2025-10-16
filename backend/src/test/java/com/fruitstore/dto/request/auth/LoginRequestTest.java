package com.fruitstore.dto.request.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LoginRequest DTO validation
 */
class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankEmail() {
        // Given
        LoginRequest request = new LoginRequest("", "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Email is required")));
    }

    @Test
    void testNullEmail() {
        // Given
        LoginRequest request = new LoginRequest(null, "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidEmailFormat() {
        // Given
        LoginRequest request = new LoginRequest("invalid-email", "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testEmailTooLong() {
        // Given
        String longEmail = "a".repeat(95) + "@test.com"; // More than 100 chars
        LoginRequest request = new LoginRequest(longEmail, "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Email must not exceed 100 characters")));
    }

    @Test
    void testBlankPassword() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    void testNullPassword() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", null);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordTooShort() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "12345");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Password must be between 6 and 100 characters")));
    }

    @Test
    void testPasswordMinimumLength() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "123456");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPasswordTooLong() {
        // Given
        String longPassword = "a".repeat(101);
        LoginRequest request = new LoginRequest("test@example.com", longPassword);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        LoginRequest request = new LoginRequest();

        // When
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Then
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testConstructorWithParameters() {
        // When
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        // Then
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testToString_DoesNotExposePassword() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        // When
        String toString = request.toString();

        // Then
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("[PROTECTED]"));
        assertFalse(toString.contains("password123"));
    }

    @Test
    void testMultipleValidationErrors() {
        // Given
        LoginRequest request = new LoginRequest("", "");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertEquals(2, violations.size()); // Both email and password are invalid
    }

    @Test
    void testValidEmailFormats() {
        // Given
        String[] validEmails = {
            "test@example.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "123@example.com"
        };

        for (String email : validEmails) {
            LoginRequest request = new LoginRequest(email, "password123");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
        }
    }
}

