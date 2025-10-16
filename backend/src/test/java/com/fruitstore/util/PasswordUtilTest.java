package com.fruitstore.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PasswordUtil
 * Tests password hashing, verification, and validation
 */
class PasswordUtilTest {

    private PasswordUtil passwordUtil;

    @BeforeEach
    void setUp() {
        passwordUtil = new PasswordUtil();
    }

    @Test
    void testHashPassword_Success() {
        // Given
        String rawPassword = "MyPassword123";

        // When
        String hashedPassword = passwordUtil.hashPassword(rawPassword);

        // Then
        assertNotNull(hashedPassword);
        assertNotEquals(rawPassword, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$")); // BCrypt hash starts with $2a$
    }

    @Test
    void testHashPassword_DifferentHashesForSamePassword() {
        // Given
        String rawPassword = "MyPassword123";

        // When
        String hash1 = passwordUtil.hashPassword(rawPassword);
        String hash2 = passwordUtil.hashPassword(rawPassword);

        // Then
        assertNotEquals(hash1, hash2); // BCrypt generates different salts
    }

    @Test
    void testHashPassword_NullPassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.hashPassword(null);
        });
    }

    @Test
    void testHashPassword_EmptyPassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.hashPassword("");
        });
    }

    @Test
    void testHashPassword_BlankPassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.hashPassword("   ");
        });
    }

    @Test
    void testVerifyPassword_CorrectPassword() {
        // Given
        String rawPassword = "MyPassword123";
        String hashedPassword = passwordUtil.hashPassword(rawPassword);

        // When
        boolean isValid = passwordUtil.verifyPassword(rawPassword, hashedPassword);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testVerifyPassword_IncorrectPassword() {
        // Given
        String rawPassword = "MyPassword123";
        String wrongPassword = "WrongPassword456";
        String hashedPassword = passwordUtil.hashPassword(rawPassword);

        // When
        boolean isValid = passwordUtil.verifyPassword(wrongPassword, hashedPassword);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testVerifyPassword_CaseSensitive() {
        // Given
        String rawPassword = "MyPassword123";
        String wrongCasePassword = "mypassword123";
        String hashedPassword = passwordUtil.hashPassword(rawPassword);

        // When
        boolean isValid = passwordUtil.verifyPassword(wrongCasePassword, hashedPassword);

        // Then
        assertFalse(isValid); // Password should be case-sensitive
    }

    @Test
    void testVerifyPassword_NullRawPassword() {
        // Given
        String hashedPassword = passwordUtil.hashPassword("password123");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.verifyPassword(null, hashedPassword);
        });
    }

    @Test
    void testVerifyPassword_EmptyRawPassword() {
        // Given
        String hashedPassword = passwordUtil.hashPassword("password123");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.verifyPassword("", hashedPassword);
        });
    }

    @Test
    void testVerifyPassword_NullHashedPassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.verifyPassword("password123", null);
        });
    }

    @Test
    void testVerifyPassword_EmptyHashedPassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            passwordUtil.verifyPassword("password123", "");
        });
    }

    @Test
    void testIsPasswordStrong_ValidStrongPassword() {
        // Given
        String strongPassword = "MyPassword123";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(strongPassword);

        // Then
        assertTrue(isStrong);
    }

    @Test
    void testIsPasswordStrong_ValidWithSpecialChars() {
        // Given
        String strongPassword = "MyP@ssw0rd!";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(strongPassword);

        // Then
        assertTrue(isStrong);
    }

    @Test
    void testIsPasswordStrong_TooShort() {
        // Given
        String shortPassword = "Pass1";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(shortPassword);

        // Then
        assertFalse(isStrong);
    }

    @Test
    void testIsPasswordStrong_NoDigit() {
        // Given
        String password = "MyPassword";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(password);

        // Then
        assertFalse(isStrong);
    }

    @Test
    void testIsPasswordStrong_NoUppercase() {
        // Given
        String password = "mypassword123";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(password);

        // Then
        assertFalse(isStrong);
    }

    @Test
    void testIsPasswordStrong_NoLowercase() {
        // Given
        String password = "MYPASSWORD123";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(password);

        // Then
        assertFalse(isStrong);
    }

    @Test
    void testIsPasswordStrong_NullPassword() {
        // When
        boolean isStrong = passwordUtil.isPasswordStrong(null);

        // Then
        assertFalse(isStrong);
    }

    @Test
    void testIsPasswordStrong_ExactlyEightChars() {
        // Given
        String password = "MyPass12";

        // When
        boolean isStrong = passwordUtil.isPasswordStrong(password);

        // Then
        assertTrue(isStrong);
    }

    @Test
    void testHashAndVerify_IntegrationTest() {
        // Given
        String rawPassword = "SecurePassword123!";

        // When
        String hashedPassword = passwordUtil.hashPassword(rawPassword);
        boolean verificationResult = passwordUtil.verifyPassword(rawPassword, hashedPassword);

        // Then
        assertTrue(verificationResult);
        assertNotEquals(rawPassword, hashedPassword);
    }

    @Test
    void testMultipleHashVerify_IntegrationTest() {
        // Given
        String password1 = "Password1";
        String password2 = "Password2";
        String password3 = "Password3";

        // When
        String hash1 = passwordUtil.hashPassword(password1);
        String hash2 = passwordUtil.hashPassword(password2);
        String hash3 = passwordUtil.hashPassword(password3);

        // Then
        assertTrue(passwordUtil.verifyPassword(password1, hash1));
        assertTrue(passwordUtil.verifyPassword(password2, hash2));
        assertTrue(passwordUtil.verifyPassword(password3, hash3));
        
        assertFalse(passwordUtil.verifyPassword(password1, hash2));
        assertFalse(passwordUtil.verifyPassword(password2, hash3));
        assertFalse(passwordUtil.verifyPassword(password3, hash1));
    }
}

