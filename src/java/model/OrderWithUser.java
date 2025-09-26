package model;

import java.math.BigDecimal;

public class OrderWithUser extends Order {
    private String username;
    private String userEmail;
    
    // Default constructor
    public OrderWithUser() {
        super();
    }
    
    // Constructor with parameters
    public OrderWithUser(int userId, BigDecimal totalAmount, String status) {
        super(userId, totalAmount, status);
    }
    
    // Getters and Setters for user information
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    @Override
    public String toString() {
        return "OrderWithUser{" +
                "orderId=" + getOrderId() +
                ", userId=" + getUserId() +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", totalAmount=" + getTotalAmount() +
                ", status='" + getStatus() + '\'' +
                ", orderDate=" + getOrderDate() +
                '}';
    }
}