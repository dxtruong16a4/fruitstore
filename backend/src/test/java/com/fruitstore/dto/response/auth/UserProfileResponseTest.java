package com.fruitstore.dto.response.auth;

import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UserProfileResponse DTO
 */
class UserProfileResponseTest {

    @Test
    void testDefaultConstructor() {
        // When
        UserProfileResponse response = new UserProfileResponse();

        // Then
        assertNotNull(response);
    }

    @Test
    void testConstructorWithAllFields() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        UserProfileResponse response = new UserProfileResponse(
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            "0123456789",
            "123 Test Street",
            UserRole.CUSTOMER,
            now
        );

        // Then
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals("0123456789", response.getPhone());
        assertEquals("123 Test Street", response.getAddress());
        assertEquals(UserRole.CUSTOMER, response.getRole());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        UserProfileResponse response = new UserProfileResponse();
        LocalDateTime now = LocalDateTime.now();

        // When
        response.setUserId(1L);
        response.setUsername("testuser");
        response.setEmail("test@example.com");
        response.setFullName("Test User");
        response.setPhone("0123456789");
        response.setAddress("123 Test Street");
        response.setRole(UserRole.ADMIN);
        response.setCreatedAt(now);

        // Then
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals("0123456789", response.getPhone());
        assertEquals("123 Test Street", response.getAddress());
        assertEquals(UserRole.ADMIN, response.getRole());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        UserProfileResponse response = new UserProfileResponse(
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            "0123456789",
            "123 Test Street",
            UserRole.CUSTOMER,
            now
        );

        // When
        String toString = response.toString();

        // Then
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("Test User"));
        assertTrue(toString.contains("CUSTOMER"));
    }

    @Test
    void testWithCustomerRole() {
        // When
        UserProfileResponse response = new UserProfileResponse();
        response.setRole(UserRole.CUSTOMER);

        // Then
        assertEquals(UserRole.CUSTOMER, response.getRole());
    }

    @Test
    void testWithAdminRole() {
        // When
        UserProfileResponse response = new UserProfileResponse();
        response.setRole(UserRole.ADMIN);

        // Then
        assertEquals(UserRole.ADMIN, response.getRole());
    }

    @Test
    void testWithNullOptionalFields() {
        // When
        UserProfileResponse response = new UserProfileResponse(
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            null, // phone
            null, // address
            UserRole.CUSTOMER,
            LocalDateTime.now()
        );

        // Then
        assertNull(response.getPhone());
        assertNull(response.getAddress());
    }
}

