package com.fruitstore.dto.response.auth;

import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LoginResponse DTO
 */
class LoginResponseTest {

    @Test
    void testDefaultConstructor() {
        // When
        LoginResponse response = new LoginResponse();

        // Then
        assertNotNull(response);
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void testConstructorWithAllFields() {
        // When
        LoginResponse response = new LoginResponse(
            "jwt.token.here",
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            UserRole.CUSTOMER
        );

        // Then
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals(UserRole.CUSTOMER, response.getRole());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        LoginResponse response = new LoginResponse();

        // When
        response.setToken("jwt.token.here");
        response.setTokenType("JWT");
        response.setUserId(1L);
        response.setUsername("testuser");
        response.setEmail("test@example.com");
        response.setFullName("Test User");
        response.setRole(UserRole.ADMIN);

        // Then
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("JWT", response.getTokenType());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals(UserRole.ADMIN, response.getRole());
    }

    @Test
    void testToString_DoesNotExposeToken() {
        // Given
        LoginResponse response = new LoginResponse(
            "jwt.token.here",
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            UserRole.CUSTOMER
        );

        // When
        String toString = response.toString();

        // Then
        assertTrue(toString.contains("[PROTECTED]"));
        assertFalse(toString.contains("jwt.token.here"));
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
    }

    @Test
    void testDefaultTokenType() {
        // Given
        LoginResponse response = new LoginResponse();

        // Then
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void testWithCustomerRole() {
        // When
        LoginResponse response = new LoginResponse(
            "token",
            1L,
            "customer",
            "customer@example.com",
            "Customer User",
            UserRole.CUSTOMER
        );

        // Then
        assertEquals(UserRole.CUSTOMER, response.getRole());
    }

    @Test
    void testWithAdminRole() {
        // When
        LoginResponse response = new LoginResponse(
            "token",
            1L,
            "admin",
            "admin@example.com",
            "Admin User",
            UserRole.ADMIN
        );

        // Then
        assertEquals(UserRole.ADMIN, response.getRole());
    }
}

