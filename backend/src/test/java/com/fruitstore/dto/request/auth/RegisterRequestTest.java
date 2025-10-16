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
 * Tests for RegisterRequest DTO validation
 */
class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRegisterRequest() {
        // Given
        RegisterRequest request = new RegisterRequest(
            "testuser",
            "test@example.com",
            "Password123",
            "Test User",
            "0123456789",
            "123 Test Street"
        );

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidRegisterRequest_MinimalFields() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankUsername() {
        // Given
        RegisterRequest request = new RegisterRequest("", "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Username is required")));
    }

    @Test
    void testUsernameTooShort() {
        // Given
        RegisterRequest request = new RegisterRequest("ab", "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Username must be between 3 and 50 characters")));
    }

    @Test
    void testUsernameMinimumLength() {
        // Given
        RegisterRequest request = new RegisterRequest("abc", "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsernameTooLong() {
        // Given
        String longUsername = "a".repeat(51);
        RegisterRequest request = new RegisterRequest(longUsername, "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUsernameInvalidCharacters() {
        // Given
        RegisterRequest request = new RegisterRequest("test-user!", "test@example.com", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Username can only contain letters, numbers, and underscores")));
    }

    @Test
    void testUsernameValidCharacters() {
        // Given
        String[] validUsernames = {"testuser", "test_user", "test123", "TEST_USER_123"};

        for (String username : validUsernames) {
            RegisterRequest request = new RegisterRequest(username, "test@example.com", "Password123", "Test User");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty(), "Username " + username + " should be valid");
        }
    }

    @Test
    void testBlankEmail() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidEmailFormat() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "invalid-email", "Password123", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void testPasswordTooShort() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Pass12", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Password must be between 8 and 100 characters")));
    }

    @Test
    void testPasswordMinimumLength() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password", "Test User");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankFullName() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "");

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Full name is required")));
    }

    @Test
    void testFullNameTooLong() {
        // Given
        String longName = "a".repeat(101);
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", longName);

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPhoneTooLong() {
        // Given
        String longPhone = "0".repeat(16);
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User", longPhone, null);

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPhoneInvalidCharacters() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User", "abc123", null);

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Phone number contains invalid characters")));
    }

    @Test
    void testPhoneValidFormats() {
        // Given
        String[] validPhones = {"0123456789", "+1234567890", "(123) 456-7890", "123-456-7890"};

        for (String phone : validPhones) {
            RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User", phone, null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty(), "Phone " + phone + " should be valid");
        }
    }

    @Test
    void testAddressTooLong() {
        // Given
        String longAddress = "a".repeat(501);
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User", null, longAddress);

        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        RegisterRequest request = new RegisterRequest();

        // When
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123");
        request.setFullName("Test User");
        request.setPhone("0123456789");
        request.setAddress("123 Test Street");

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("Password123", request.getPassword());
        assertEquals("Test User", request.getFullName());
        assertEquals("0123456789", request.getPhone());
        assertEquals("123 Test Street", request.getAddress());
    }

    @Test
    void testToString_DoesNotExposePassword() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User");

        // When
        String toString = request.toString();

        // Then
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("[PROTECTED]"));
        assertFalse(toString.contains("Password123"));
    }

    @Test
    void testAllFieldsConstructor() {
        // When
        RegisterRequest request = new RegisterRequest(
            "testuser",
            "test@example.com",
            "Password123",
            "Test User",
            "0123456789",
            "123 Test Street"
        );

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("Password123", request.getPassword());
        assertEquals("Test User", request.getFullName());
        assertEquals("0123456789", request.getPhone());
        assertEquals("123 Test Street", request.getAddress());
    }

    @Test
    void testMinimalConstructor() {
        // When
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "Password123", "Test User");

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("Password123", request.getPassword());
        assertEquals("Test User", request.getFullName());
        assertNull(request.getPhone());
        assertNull(request.getAddress());
    }
}

