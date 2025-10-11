package dtos.order;

import java.math.BigDecimal;

/**
 * DTO for cart item data
 * Used for displaying cart items with product information
 */
public class CartDTO {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private boolean selected;
    
    // Product information (flattened for display)
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productSlug;
    private BigDecimal productPrice;
    private BigDecimal originalPrice;
    private int discountPercent;
    private String categoryName;
    private String subcategoryName;
    private int stockQuantity;
    private int minStockLevel;
    private boolean isProductActive;
    private boolean isProductInStock;
    private String unit;
    private BigDecimal weight;
    
    // Calculated values
    private BigDecimal totalPrice;
    private BigDecimal savingsAmount;
    private boolean isOnSale;

    // Constructors
    public CartDTO() {}

    public CartDTO(int cartId, int userId, int productId, int quantity, boolean selected) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.selected = selected;
    }

    // Getters and Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getProductSlug() { return productSlug; }
    public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }

    public boolean isProductActive() { return isProductActive; }
    public void setProductActive(boolean productActive) { isProductActive = productActive; }

    public boolean isProductInStock() { return isProductInStock; }
    public void setProductInStock(boolean productInStock) { isProductInStock = productInStock; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }

    public boolean isOnSale() { return isOnSale; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isProductAvailable() {
        return isProductActive && isProductInStock;
    }

    public boolean canBeAddedToOrder() {
        return isSelected() && isValidQuantity() && isProductAvailable();
    }

    public BigDecimal getCalculatedTotalPrice() {
        if (productPrice == null) return BigDecimal.ZERO;
        return productPrice.multiply(new BigDecimal(quantity));
    }

    public BigDecimal getCalculatedSavingsAmount() {
        if (originalPrice == null || productPrice == null) return BigDecimal.ZERO;
        return originalPrice.subtract(productPrice).multiply(new BigDecimal(quantity));
    }

    public String getFormattedProductPrice() {
        return productPrice != null ? String.format("%.2f", productPrice) : "0.00";
    }

    public String getFormattedOriginalPrice() {
        return originalPrice != null ? String.format("%.2f", originalPrice) : "0.00";
    }

    public String getFormattedTotalPrice() {
        return totalPrice != null ? String.format("%.2f", totalPrice) : "0.00";
    }

    public String getFormattedSavingsAmount() {
        return savingsAmount != null ? String.format("%.2f", savingsAmount) : "0.00";
    }

    public String getDisplayName() {
        return productName != null ? productName : "Unknown Product";
    }

    public String getShortDescription() {
        if (productDescription != null && productDescription.length() > 100) {
            return productDescription.substring(0, 97) + "...";
        }
        return productDescription;
    }

    public String getProductUrl() {
        if (productSlug != null) {
            return "/product/" + productSlug;
        }
        return "/product/" + productId;
    }

    public boolean hasImage() {
        return productImageUrl != null && !productImageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? productImageUrl : "/images/default-product.jpg";
    }

    public boolean hasDiscount() {
        return discountPercent > 0 && originalPrice != null && productPrice != null;
    }

    public boolean hasSignificantDiscount() {
        return discountPercent >= 20;
    }

    public String getDiscountDisplay() {
        if (hasDiscount()) {
            return discountPercent + "% OFF";
        }
        return "";
    }

    public String getDiscountBadge() {
        if (hasSignificantDiscount()) return "🔥 Sale";
        if (hasDiscount()) return "💰 Save";
        return "";
    }

    public String getDiscountColor() {
        if (hasSignificantDiscount()) return "red";
        if (hasDiscount()) return "green";
        return "gray";
    }

    public boolean isLowStock() {
        return stockQuantity <= minStockLevel;
    }

    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }

    public String getStockStatus() {
        if (isOutOfStock()) return "Out of Stock";
        if (isLowStock()) return "Low Stock";
        return "In Stock";
    }

    public String getStockStatusColor() {
        if (isOutOfStock()) return "red";
        if (isLowStock()) return "orange";
        return "green";
    }

    public boolean canIncreaseQuantity() {
        return isProductAvailable() && quantity < stockQuantity;
    }

    public int getMaxQuantity() {
        return Math.max(0, stockQuantity);
    }

    public boolean canDecreaseQuantity() {
        return quantity > 1;
    }

    public String getQuantityDisplay() {
        if (quantity == 1) return "1 " + (unit != null ? unit : "item");
        return quantity + " " + (unit != null ? unit + "s" : "items");
    }

    public String getCategoryPath() {
        if (categoryName == null && subcategoryName == null) return "Unknown Category";
        if (subcategoryName == null) return categoryName;
        return categoryName + " > " + subcategoryName;
    }

    public boolean isHeavyItem() {
        return weight != null && weight.compareTo(new BigDecimal("5.0")) > 0;
    }

    public String getFormattedWeight() {
        return weight != null ? weight.toString() + " kg" : "N/A";
    }

    public String getCartItemSummary() {
        return getQuantityDisplay() + " of " + getDisplayName() + " - " + getFormattedTotalPrice();
    }

    public boolean hasSavings() {
        return savingsAmount != null && savingsAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getSavingsDisplay() {
        if (hasSavings()) {
            return "Save " + getFormattedSavingsAmount();
        }
        return "";
    }

    public String getAvailabilityBadge() {
        if (!isProductActive) return "🚫 Discontinued";
        if (isOutOfStock()) return "❌ Out of Stock";
        if (isLowStock()) return "⚠️ Low Stock";
        return "✅ Available";
    }

    public String getAvailabilityColor() {
        if (!isProductActive) return "red";
        if (isOutOfStock()) return "red";
        if (isLowStock()) return "orange";
        return "green";
    }

    public boolean isRecentlyAdded() {
        // This would need to be tracked in the database
        // For now, we'll assume it's handled by the service layer
        return false;
    }

    public String getProductBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (hasDiscount()) {
            badges.append(" ").append(getDiscountBadge());
        }
        
        if (isLowStock()) {
            badges.append(" ⚠️");
        }
        
        return badges.toString().trim();
    }
}
