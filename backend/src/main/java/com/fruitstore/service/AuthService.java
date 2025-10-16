package com.fruitstore.service;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.request.auth.ChangePasswordRequest;
import com.fruitstore.dto.request.auth.LoginRequest;
import com.fruitstore.dto.request.auth.RegisterRequest;
import com.fruitstore.dto.request.auth.UpdateProfileRequest;
import com.fruitstore.dto.response.auth.LoginResponse;
import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.repository.UserRepository;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.util.JwtUtil;
import com.fruitstore.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication and user management
 * Handles registration, login, profile management, and password changes
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user
     * 
     * @param request registration details
     * @return user profile response
     * @throws IllegalArgumentException if email or username already exists
     */
    public UserProfileResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        // Validate password strength
        if (!passwordUtil.isPasswordStrong(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters with uppercase, lowercase, and digit");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordUtil.hashPassword(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(UserRole.CUSTOMER); // Default role

        // Save user
        User savedUser = userRepository.save(user);

        // Return user profile response
        return mapToUserProfileResponse(savedUser);
    }

    /**
     * Authenticate user and generate JWT token
     * 
     * @param request login credentials
     * @return login response with JWT token
     * @throws IllegalArgumentException if credentials are invalid
     */
    public LoginResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Verify password
        if (!passwordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate JWT token
        UserDetails userDetails = new CustomUserDetails(user);
        String token = jwtUtil.generateToken(userDetails);

        // Return login response
        return new LoginResponse(
                token,
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    /**
     * Get user profile by user ID
     * 
     * @param userId the user ID
     * @return user profile response
     * @throws IllegalArgumentException if user not found
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return mapToUserProfileResponse(user);
    }

    /**
     * Update user profile
     * 
     * @param userId the user ID
     * @param request update profile request
     * @return updated user profile response
     * @throws IllegalArgumentException if user not found
     */
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Update user fields
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        // Save updated user
        User updatedUser = userRepository.save(user);

        return mapToUserProfileResponse(updatedUser);
    }

    /**
     * Change user password
     * 
     * @param userId the user ID
     * @param request change password request
     * @throws IllegalArgumentException if user not found or old password is incorrect
     */
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Verify old password
        if (!passwordUtil.verifyPassword(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Validate new password strength
        if (!passwordUtil.isPasswordStrong(request.getNewPassword())) {
            throw new IllegalArgumentException("New password must be at least 8 characters with uppercase, lowercase, and digit");
        }

        // Check if new password is different from old password
        if (passwordUtil.verifyPassword(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from old password");
        }

        // Hash and update password
        user.setPassword(passwordUtil.hashPassword(request.getNewPassword()));

        // Save updated user
        userRepository.save(user);
    }

    /**
     * Map User entity to UserProfileResponse DTO
     * 
     * @param user the user entity
     * @return user profile response
     */
    private UserProfileResponse mapToUserProfileResponse(User user) {
        return new UserProfileResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}

