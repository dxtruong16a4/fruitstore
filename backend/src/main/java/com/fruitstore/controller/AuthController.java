package com.fruitstore.controller;

import com.fruitstore.dto.request.auth.ChangePasswordRequest;
import com.fruitstore.dto.request.auth.LoginRequest;
import com.fruitstore.dto.request.auth.RegisterRequest;
import com.fruitstore.dto.request.auth.UpdateProfileRequest;
import com.fruitstore.dto.response.auth.LoginResponse;
import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication and user management
 * Handles user registration, login, profile management, and password changes
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     * 
     * @param request registration details
     * @return login response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        LoginResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    /**
     * Login user and generate JWT token
     * 
     * @param request login credentials
     * @return login response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Get current user profile
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return user profile response
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        UserProfileResponse response = authService.getUserProfile(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update current user profile
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param request update profile request
     * @return updated user profile response
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        
        UserProfileResponse response = authService.updateProfile(userDetails.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    /**
     * Change current user password
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param request change password request
     * @return success response
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        
        authService.changePassword(userDetails.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    /**
     * Get authenticated user information from security context
     * Useful for debugging and checking authentication status
     * Requires authentication
     * 
     * @param authentication spring security authentication object
     * @return basic user information
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserProfileResponse response = authService.getUserProfile(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

