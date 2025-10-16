package com.fruitstore.util;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil
 * Tests JWT token generation, validation, and claims extraction
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private String testUsername = "testuser@example.com";
    private String testSecret = "mySecretKey123456789012345678901234567890"; // Must be at least 256 bits
    private Long testExpiration = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        // Set JWT secret and expiration using reflection
        ReflectionTestUtils.setField(jwtUtil, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);

        // Create test user details
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        userDetails = new User(testUsername, "password", authorities);
    }

    @Test
    void testGenerateToken_Success() {
        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts: header.payload.signature
    }

    @Test
    void testGenerateToken_WithExtraClaims() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123);
        claims.put("role", "CUSTOMER");

        // When
        String token = jwtUtil.generateToken(claims, testUsername);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Date expirationDate = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testIsTokenExpired_NotExpired() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void testIsTokenExpired_ExpiredToken() {
        // Given - Create token with very short expiration (1ms)
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String token = jwtUtil.generateToken(userDetails);

        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // When
        Boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertTrue(isExpired);
    }

    @Test
    void testValidateToken_ValidToken() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_ValidTokenWithUsername() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Boolean isValid = jwtUtil.validateToken(token, testUsername);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_WrongUsername() {
        // Given
        String token = jwtUtil.generateToken(userDetails);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        UserDetails differentUser = new User("different@example.com", "password", authorities);

        // When
        Boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_ExpiredToken() {
        // Given - Create token with very short expiration
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String token = jwtUtil.generateToken(userDetails);

        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Reset expiration for validation
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);

        // When
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testExtractClaim_CustomClaim() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123);
        String token = jwtUtil.generateToken(claims, testUsername);

        // When
        Integer userId = jwtUtil.extractClaim(token, claims1 -> claims1.get("userId", Integer.class));

        // Then
        assertEquals(123, userId);
    }

    @Test
    void testGetExpirationTime() {
        // When
        Long expirationTime = jwtUtil.getExpirationTime();

        // Then
        assertEquals(testExpiration, expirationTime);
    }

    @Test
    void testTokenStructure() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        String[] parts = token.split("\\.");

        // Then
        assertEquals(3, parts.length); // header.payload.signature
        assertTrue(parts[0].length() > 0); // header
        assertTrue(parts[1].length() > 0); // payload
        assertTrue(parts[2].length() > 0); // signature
    }

    @Test
    void testGenerateMultipleTokens_ShouldBeDifferent() {
        // When
        String token1 = jwtUtil.generateToken(userDetails);
        
        // Small delay to ensure different issued time
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }
        
        String token2 = jwtUtil.generateToken(userDetails);

        // Then
        assertNotEquals(token1, token2); // Different issued timestamps
    }

    @Test
    void testTokenPersistence() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When - Extract username multiple times
        String username1 = jwtUtil.extractUsername(token);
        String username2 = jwtUtil.extractUsername(token);
        String username3 = jwtUtil.extractUsername(token);

        // Then - Same username should be extracted each time
        assertEquals(testUsername, username1);
        assertEquals(testUsername, username2);
        assertEquals(testUsername, username3);
    }

    @Test
    void testValidateToken_Integration() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 100);
        claims.put("role", "ADMIN");
        String token = jwtUtil.generateToken(claims, testUsername);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);
        Boolean isExpired = jwtUtil.isTokenExpired(token);
        Boolean isValid = jwtUtil.validateToken(token, testUsername);

        // Then
        assertEquals(testUsername, extractedUsername);
        assertFalse(isExpired);
        assertTrue(isValid);
    }

    @Test
    void testInvalidToken_InvalidSignature() {
        // Given
        String token = jwtUtil.generateToken(userDetails);
        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX"; // Tamper with signature

        // When & Then
        assertThrows(SignatureException.class, () -> {
            jwtUtil.extractUsername(tamperedToken);
        });
    }

    @Test
    void testInvalidToken_MalformedToken() {
        // Given
        String malformedToken = "not.a.valid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(malformedToken);
        });
    }

    @Test
    void testExtractExpiration_WithinExpirationWindow() {
        // Given
        String token = jwtUtil.generateToken(userDetails);

        // When
        Date expiration = jwtUtil.extractExpiration(token);
        long timeDifference = expiration.getTime() - new Date().getTime();

        // Then
        assertTrue(timeDifference > 0);
        assertTrue(timeDifference <= testExpiration);
    }

    @Test
    void testDifferentUsersGenerateDifferentTokens() {
        // Given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        
        UserDetails user1 = new User("user1@example.com", "password", authorities);
        UserDetails user2 = new User("user2@example.com", "password", authorities);

        // When
        String token1 = jwtUtil.generateToken(user1);
        String token2 = jwtUtil.generateToken(user2);

        // Then
        assertNotEquals(token1, token2);
        assertEquals("user1@example.com", jwtUtil.extractUsername(token1));
        assertEquals("user2@example.com", jwtUtil.extractUsername(token2));
    }
}

