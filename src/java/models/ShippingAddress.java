package models;

import java.time.LocalDateTime;

public class ShippingAddress {
    private int addressId;
    private int userId;
    private String fullName;
    private User user; // For joined queries
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ShippingAddress() {}

    public ShippingAddress(int userId, String fullName, String phone, 
                          String addressLine1, String city, String state, String postalCode) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "Vietnam";
        this.isDefault = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ShippingAddress(User user, String fullName, String phone, 
                          String addressLine1, String city, String state, String postalCode) {
        this.user = user;
        this.userId = user != null ? user.getUserId() : 0;
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "Vietnam";
        this.isDefault = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(addressLine1);
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        address.append(", ").append(city);
        address.append(", ").append(state);
        address.append(" ").append(postalCode);
        address.append(", ").append(country);
        return address.toString();
    }

    public User getUser() { return user; }
    public void setUser(User user) { 
        this.user = user; 
        this.userId = user != null ? user.getUserId() : 0;
    }

    // Business methods with relationships
    public String getCustomerEmail() {
        return user != null ? user.getEmail() : null;
    }

    public String getCustomerUsername() {
        return user != null ? user.getUsername() : null;
    }

    public boolean belongsToUser(int userId) {
        return this.userId == userId || (user != null && user.getUserId() == userId);
    }
}