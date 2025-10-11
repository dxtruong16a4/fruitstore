package dtos.user;

import java.time.LocalDateTime;

/**
 * DTO for shipping address information
 * Used for displaying and managing shipping addresses
 */
public class AddressDTO {
    private int addressId;
    private int userId;
    private String fullName;
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
    public AddressDTO() {}

    public AddressDTO(String fullName, String phone, String addressLine1, 
                     String city, String state, String postalCode) {
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "Vietnam";
    }

    public AddressDTO(int addressId, String fullName, String phone, String addressLine1, 
                     String city, String state, String postalCode, boolean isDefault) {
        this.addressId = addressId;
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = "Vietnam";
        this.isDefault = isDefault;
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

    public String getShortAddress() {
        return addressLine1 + ", " + city + ", " + state;
    }

    public boolean isValid() {
        return fullName != null && !fullName.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty() &&
               addressLine1 != null && !addressLine1.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               state != null && !state.trim().isEmpty() &&
               postalCode != null && !postalCode.trim().isEmpty();
    }

    public boolean hasAddressLine2() {
        return addressLine2 != null && !addressLine2.trim().isEmpty();
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toLocalDate().toString() : "Unknown";
    }

    public String getDisplayName() {
        return fullName + " - " + getShortAddress();
    }

    public boolean isCompleteAddress() {
        return isValid() && country != null && !country.trim().isEmpty();
    }

    public String getCityState() {
        return city + ", " + state;
    }
}
