package com.fruitstore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for password hashing and verification using BCrypt
 * BCrypt is a secure one-way hashing algorithm designed for passwords
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor initializes BCryptPasswordEncoder with strength 10
     * Strength 10 provides good balance between security and performance
     */
    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }

    /**
     * Hash a raw password using BCrypt
     * 
     * @param rawPassword the plain text password to hash
     * @return the BCrypt hashed password
     * @throws IllegalArgumentException if rawPassword is null or empty
     */
    public String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verify if a raw password matches a hashed password
     * 
     * @param rawPassword the plain text password to verify
     * @param hashedPassword the BCrypt hashed password to compare against
     * @return true if the password matches, false otherwise
     * @throws IllegalArgumentException if either parameter is null or empty
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw password cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /**
     * Check if a password meets minimum security requirements
     * 
     * @param password the password to validate
     * @return true if password meets requirements, false otherwise
     */
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // At least one digit, one lowercase, one uppercase letter
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        
        return hasDigit && hasLower && hasUpper;
    }
}

