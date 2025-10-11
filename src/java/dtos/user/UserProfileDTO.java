package dtos.user;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for complete user profile information
 * Used for displaying detailed user profile with related data
 */
public class UserProfileDTO {
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
    private LocalDateTime updatedAt;
    
    // Related data
    private List<AddressDTO> shippingAddresses;
    private int totalOrders;
    private int totalWishlistItems;
    private int totalReviews;
    private LocalDateTime memberSince;

    // Constructors
    public UserProfileDTO() {}

    public UserProfileDTO(int userId, String username, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.memberSince = LocalDateTime.now();
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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<AddressDTO> getShippingAddresses() { return shippingAddresses; }
    public void setShippingAddresses(List<AddressDTO> shippingAddresses) { this.shippingAddresses = shippingAddresses; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getTotalWishlistItems() { return totalWishlistItems; }
    public void setTotalWishlistItems(int totalWishlistItems) { this.totalWishlistItems = totalWishlistItems; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

    public LocalDateTime getMemberSince() { return memberSince; }
    public void setMemberSince(LocalDateTime memberSince) { this.memberSince = memberSince; }

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

    public String getMemberSinceFormatted() {
        return memberSince != null ? memberSince.toLocalDate().toString() : "Unknown";
    }

    public String getLastLoginFormatted() {
        return lastLogin != null ? lastLogin.toString() : "Never";
    }

    public boolean hasShippingAddresses() {
        return shippingAddresses != null && !shippingAddresses.isEmpty();
    }

    public AddressDTO getDefaultShippingAddress() {
        if (shippingAddresses != null) {
            return shippingAddresses.stream()
                    .filter(AddressDTO::isDefault)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public boolean isProfileComplete() {
        return fullName != null && !fullName.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty() &&
               address != null && !address.trim().isEmpty() &&
               emailVerified;
    }

    public double getProfileCompletionPercentage() {
        int totalFields = 4; // fullName, phone, address, emailVerified
        int completedFields = 0;
        
        if (fullName != null && !fullName.trim().isEmpty()) completedFields++;
        if (phone != null && !phone.trim().isEmpty()) completedFields++;
        if (address != null && !address.trim().isEmpty()) completedFields++;
        if (emailVerified) completedFields++;
        
        return (double) completedFields / totalFields * 100;
    }
}
