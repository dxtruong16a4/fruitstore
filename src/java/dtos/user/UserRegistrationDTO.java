package dtos.user;

/**
 * DTO for user registration form data
 * Used for receiving registration data from frontend
 */
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String phone;
    private boolean agreeToTerms;

    // Constructors
    public UserRegistrationDTO() {}

    public UserRegistrationDTO(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isAgreeToTerms() { return agreeToTerms; }
    public void setAgreeToTerms(boolean agreeToTerms) { this.agreeToTerms = agreeToTerms; }

    // Business methods
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }

    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean isPasswordValid() {
        return password != null && password.length() >= 6;
    }

    public boolean isUsernameValid() {
        return username != null && username.length() >= 3 && username.matches("[a-zA-Z0-9_]+");
    }

    public boolean isFormValid() {
        return isUsernameValid() && 
               isValidEmail() && 
               isPasswordValid() && 
               isPasswordMatch() && 
               fullName != null && !fullName.trim().isEmpty() &&
               agreeToTerms;
    }

    public String getTrimmedUsername() {
        return username != null ? username.trim() : null;
    }

    public String getTrimmedEmail() {
        return email != null ? email.trim().toLowerCase() : null;
    }

    public String getTrimmedFullName() {
        return fullName != null ? fullName.trim() : null;
    }
}
