package com.fruitstore.service;

import com.fruitstore.controller.UserController;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for user management (admin operations)
 * Handles user listing, role management, and user statistics
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users with pagination
     * 
     * @param pageable pagination parameters
     * @return page of user profile responses
     */
    @Transactional(readOnly = true)
    public Page<UserProfileResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserProfileResponse);
    }

    /**
     * Get user by ID
     * 
     * @param userId the user ID
     * @return user profile response
     * @throws IllegalArgumentException if user not found
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return mapToUserProfileResponse(user);
    }

    /**
     * Update user role
     * 
     * @param userId the user ID
     * @param role the new role (ADMIN or CUSTOMER)
     * @return updated user profile response
     * @throws IllegalArgumentException if user not found or invalid role
     */
    public UserProfileResponse updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            user.setRole(userRole);
            User updatedUser = userRepository.save(user);
            return mapToUserProfileResponse(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    /**
     * Get user statistics
     * 
     * @return user statistics
     */
    @Transactional(readOnly = true)
    public UserController.UserStatistics getUserStatistics() {
        long totalUsers = userRepository.count();
        long adminUsers = userRepository.countByRole(UserRole.ADMIN);
        long customerUsers = userRepository.countByRole(UserRole.CUSTOMER);

        return new UserController.UserStatistics(totalUsers, adminUsers, customerUsers);
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

