package com.fruitstore.dto.response.user;

import com.fruitstore.domain.user.UserRole;

import java.time.LocalDateTime;

/**
 * DTO for user summary response (used in lists and references)
 */
public class UserSummaryResponse {

    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private LocalDateTime createdAt;

    // Constructors
    public UserSummaryResponse() {
    }

    public UserSummaryResponse(Long userId, String username, String email, String fullName, 
                              UserRole role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserSummaryResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
