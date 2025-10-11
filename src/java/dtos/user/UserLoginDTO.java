package dtos.user;

/**
 * DTO for user login credentials
 * Used for receiving login data from frontend
 */
public class UserLoginDTO {
    private String usernameOrEmail;
    private String password;
    private boolean rememberMe;

    // Constructors
    public UserLoginDTO() {}

    public UserLoginDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public UserLoginDTO(String usernameOrEmail, String password, boolean rememberMe) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    // Getters and Setters
    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isRememberMe() { return rememberMe; }
    public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }

    // Business methods
    public boolean isValidLogin() {
        return usernameOrEmail != null && 
               !usernameOrEmail.trim().isEmpty() && 
               password != null && 
               !password.trim().isEmpty();
    }

    public boolean isEmailLogin() {
        return usernameOrEmail != null && usernameOrEmail.contains("@");
    }

    public boolean isUsernameLogin() {
        return !isEmailLogin();
    }

    public String getTrimmedUsernameOrEmail() {
        return usernameOrEmail != null ? usernameOrEmail.trim() : null;
    }

    public String getLoginIdentifier() {
        String trimmed = getTrimmedUsernameOrEmail();
        return isEmailLogin() ? trimmed.toLowerCase() : trimmed;
    }

    public boolean hasValidPassword() {
        return password != null && password.length() > 0;
    }
}
