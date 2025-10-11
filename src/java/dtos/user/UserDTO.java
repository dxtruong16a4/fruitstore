package dtos.user;

import java.time.LocalDateTime;

/**
 * DTO for public user information without sensitive data
 * Used for displaying user information in responses
 */
public class UserDTO {
    private int userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String avatarUrl;
    private String role;
    private boolean isActive;
    private boolean emailVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    // Constructors
    public UserDTO() {}

    public UserDTO(int userId, String username, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Business methods
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public String getDisplayName() {
        return fullName != null && !fullName.trim().isEmpty() ? fullName : username;
    }

    public String getInitials() {
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] names = fullName.trim().split("\\s+");
            if (names.length >= 2) {
                return (names[0].charAt(0) + "" + names[names.length - 1].charAt(0)).toUpperCase();
            } else if (names.length == 1) {
                return names[0].substring(0, Math.min(2, names[0].length())).toUpperCase();
            }
        }
        return username != null && username.length() >= 2 ? username.substring(0, 2).toUpperCase() : "U";
    }

    public String getFormattedLastLogin() {
        return lastLogin != null ? lastLogin.toString() : "Never";
    }
}
