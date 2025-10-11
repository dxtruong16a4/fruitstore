package models;

import java.time.LocalDateTime;

public class Wishlist {
    private int wishlistId;
    private int userId;
    private int productId;
    private LocalDateTime createdAt;
    private User user; // For joined queries
    private Product product; // For joined queries

    // Constructors
    public Wishlist() {}

    public Wishlist(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
        this.createdAt = LocalDateTime.now();
    }

    public Wishlist(User user, Product product) {
        this.user = user;
        this.userId = user != null ? user.getUserId() : 0;
        this.product = product;
        this.productId = product != null ? product.getProductId() : 0;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getWishlistId() { return wishlistId; }
    public void setWishlistId(int wishlistId) { this.wishlistId = wishlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { 
        this.product = product; 
        this.productId = product != null ? product.getProductId() : 0;
    }

    public User getUser() { return user; }
    public void setUser(User user) { 
        this.user = user; 
        this.userId = user != null ? user.getUserId() : 0;
    }

    // Business methods with relationships
    public String getProductName() {
        return product != null ? product.getName() : "Unknown Product";
    }

    public String getProductImageUrl() {
        return product != null ? product.getImageUrl() : null;
    }

    public java.math.BigDecimal getProductPrice() {
        return product != null ? product.getPrice() : java.math.BigDecimal.ZERO;
    }

    public boolean isProductAvailable() {
        return product != null && product.isActive() && product.isInStock();
    }

    public String getCustomerName() {
        return user != null ? user.getFullName() : "Unknown Customer";
    }
}