package dtos.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for wishlist item information
 * Used for displaying wishlist items with product details
 */
public class WishlistDTO {
    private int wishlistId;
    private int userId;
    private int productId;
    private LocalDateTime createdAt;
    
    // Product information (flattened for easier display)
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private BigDecimal originalPrice;
    private String productImageUrl;
    private String categoryName;
    private boolean isProductActive;
    private boolean isProductInStock;
    private int stockQuantity;

    // Constructors
    public WishlistDTO() {}

    public WishlistDTO(int wishlistId, int userId, int productId, LocalDateTime createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
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

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public boolean isProductActive() { return isProductActive; }
    public void setProductActive(boolean productActive) { isProductActive = productActive; }

    public boolean isProductInStock() { return isProductInStock; }
    public void setProductInStock(boolean productInStock) { isProductInStock = productInStock; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    // Business methods
    public boolean isProductAvailable() {
        return isProductActive && isProductInStock;
    }

    public boolean isOnSale() {
        return originalPrice != null && originalPrice.compareTo(productPrice) > 0;
    }

    public BigDecimal getSavingsAmount() {
        if (isOnSale()) {
            return originalPrice.subtract(productPrice);
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedPrice() {
        return productPrice != null ? String.format("%.2f", productPrice) : "0.00";
    }

    public String getFormattedOriginalPrice() {
        return originalPrice != null ? String.format("%.2f", originalPrice) : null;
    }

    public String getFormattedSavings() {
        return String.format("%.2f", getSavingsAmount());
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getShortDescription() {
        if (productDescription != null && productDescription.length() > 100) {
            return productDescription.substring(0, 97) + "...";
        }
        return productDescription;
    }

    public boolean isRecentlyAdded() {
        if (createdAt == null) return false;
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return createdAt.isAfter(threeDaysAgo);
    }

    public String getStockStatus() {
        if (!isProductActive) return "Unavailable";
        if (!isProductInStock) return "Out of Stock";
        if (stockQuantity <= 5) return "Low Stock";
        return "In Stock";
    }

    public boolean canBeAddedToCart() {
        return isProductAvailable() && stockQuantity > 0;
    }
}
