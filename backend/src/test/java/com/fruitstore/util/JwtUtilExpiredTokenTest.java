package com.fruitstore.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify JWT expired token handling
 */
class JwtUtilExpiredTokenTest {

    @Test
    void testExpiredTokenHandling() {
        // Given
        JwtUtil jwtUtil = new JwtUtil();
        String testSecret = "mySecretKey123456789012345678901234567890";
        Long shortExpiration = 1L; // 1ms
        
        ReflectionTestUtils.setField(jwtUtil, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", shortExpiration);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        UserDetails userDetails = new User("test@example.com", "password", authorities);

        // Create token with very short expiration
        String token = jwtUtil.generateToken(userDetails);

        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // When - Test expired token handling
        Boolean isExpired = jwtUtil.isTokenExpired(token);
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then - Should handle expired token gracefully
        assertTrue(isExpired, "Token should be considered expired");
        assertFalse(isValid, "Expired token should be invalid");
    }
}
