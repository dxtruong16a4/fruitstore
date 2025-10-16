package com.fruitstore.security;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomUserDetails
 * Tests UserDetails implementation and User entity wrapping
 */
class CustomUserDetailsTest {

    private User testUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$hashedPassword");
        testUser.setFullName("Test User");
        testUser.setPhone("0123456789");
        testUser.setAddress("123 Test Street");
        testUser.setRole(UserRole.CUSTOMER);

        customUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    void testGetUsername() {
        // When
        String username = customUserDetails.getUsername();

        // Then
        assertEquals("test@example.com", username); // Email is used as username
    }

    @Test
    void testGetPassword() {
        // When
        String password = customUserDetails.getPassword();

        // Then
        assertEquals("$2a$10$hashedPassword", password);
    }

    @Test
    void testGetAuthorities_Customer() {
        // When
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    void testGetAuthorities_Admin() {
        // Given
        testUser.setRole(UserRole.ADMIN);
        CustomUserDetails adminDetails = new CustomUserDetails(testUser);

        // When
        Collection<? extends GrantedAuthority> authorities = adminDetails.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testIsAccountNonExpired() {
        // When
        boolean isNonExpired = customUserDetails.isAccountNonExpired();

        // Then
        assertTrue(isNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        // When
        boolean isNonLocked = customUserDetails.isAccountNonLocked();

        // Then
        assertTrue(isNonLocked);
    }

    @Test
    void testIsCredentialsNonExpired() {
        // When
        boolean isNonExpired = customUserDetails.isCredentialsNonExpired();

        // Then
        assertTrue(isNonExpired);
    }

    @Test
    void testIsEnabled() {
        // When
        boolean isEnabled = customUserDetails.isEnabled();

        // Then
        assertTrue(isEnabled);
    }

    @Test
    void testGetUser() {
        // When
        User user = customUserDetails.getUser();

        // Then
        assertNotNull(user);
        assertEquals(testUser, user);
        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetUserId() {
        // When
        Long userId = customUserDetails.getUserId();

        // Then
        assertEquals(1L, userId);
    }

    @Test
    void testGetRole() {
        // When
        UserRole role = customUserDetails.getRole();

        // Then
        assertEquals(UserRole.CUSTOMER, role);
    }

    @Test
    void testGetFullName() {
        // When
        String fullName = customUserDetails.getFullName();

        // Then
        assertEquals("Test User", fullName);
    }

    @Test
    void testConstructor_WithUser() {
        // When
        CustomUserDetails details = new CustomUserDetails(testUser);

        // Then
        assertNotNull(details);
        assertEquals(testUser, details.getUser());
    }

    @Test
    void testUserDetails_AllFieldsAccessible() {
        // Then
        assertEquals("test@example.com", customUserDetails.getUsername());
        assertEquals("$2a$10$hashedPassword", customUserDetails.getPassword());
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());
        assertNotNull(customUserDetails.getAuthorities());
    }

    @Test
    void testDifferentRoles_ProducesDifferentAuthorities() {
        // Given
        User customerUser = new User();
        customerUser.setEmail("customer@example.com");
        customerUser.setPassword("password");
        customerUser.setRole(UserRole.CUSTOMER);

        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("password");
        adminUser.setRole(UserRole.ADMIN);

        // When
        CustomUserDetails customerDetails = new CustomUserDetails(customerUser);
        CustomUserDetails adminDetails = new CustomUserDetails(adminUser);

        // Then
        assertTrue(customerDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(adminDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}

