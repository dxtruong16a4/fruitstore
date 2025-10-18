package com.fruitstore.controller;

import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user management (admin only)
 * Handles user listing, role management, and user statistics
 */
@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users with pagination (admin)
     * Requires ADMIN role
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (username, email, createdAt, role)
     * @param sortDirection sort direction (asc, desc)
     * @return page of users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserProfileResponse>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserProfileResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Get user by ID (admin)
     * Requires ADMIN role
     * 
     * @param userId the user ID
     * @return user profile response
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserById(@PathVariable Long userId) {
        UserProfileResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Update user role (admin)
     * Requires ADMIN role
     * 
     * @param userId the user ID
     * @param role the new role (ADMIN or CUSTOMER)
     * @return updated user profile response
     */
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        
        UserProfileResponse user = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", user));
    }

    /**
     * Get user statistics (admin)
     * Requires ADMIN role
     * 
     * @return user statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserStatistics>> getUserStatistics() {
        UserStatistics stats = userService.getUserStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private long totalUsers;
        private long adminUsers;
        private long customerUsers;

        public UserStatistics(long totalUsers, long adminUsers, long customerUsers) {
            this.totalUsers = totalUsers;
            this.adminUsers = adminUsers;
            this.customerUsers = customerUsers;
        }

        public long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public long getAdminUsers() {
            return adminUsers;
        }

        public void setAdminUsers(long adminUsers) {
            this.adminUsers = adminUsers;
        }

        public long getCustomerUsers() {
            return customerUsers;
        }

        public void setCustomerUsers(long customerUsers) {
            this.customerUsers = customerUsers;
        }
    }
}

