package dtos.promotion;

import java.math.BigDecimal;

/**
 * DTO for combo item details
 * Used for displaying individual items within a combo
 */
public class ComboItemDTO {
    private int comboItemId;
    private int comboId;
    private int productId;
    private int quantity;
    
    // Product information (flattened for display)
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productSlug;
    private BigDecimal productPrice;
    private String categoryName;
    private String subcategoryName;
    private int stockQuantity;
    private boolean isProductActive;
    private boolean isProductInStock;
    private String unit;
    private BigDecimal weight;
    
    // Combo information (flattened for context)
    private String comboName;
    private BigDecimal comboPrice;
    
    // Calculated values
    private BigDecimal totalValue;
    private boolean isProductAvailable;

    // Constructors
    public ComboItemDTO() {}

    public ComboItemDTO(int comboItemId, int comboId, int productId, int quantity) {
        this.comboItemId = comboItemId;
        this.comboId = comboId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getComboItemId() { return comboItemId; }
    public void setComboItemId(int comboItemId) { this.comboItemId = comboItemId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

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

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public boolean isProductActive() { return isProductActive; }
    public void setProductActive(boolean productActive) { isProductActive = productActive; }

    public boolean isProductInStock() { return isProductInStock; }
    public void setProductInStock(boolean productInStock) { isProductInStock = productInStock; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getComboName() { return comboName; }
    public void setComboName(String comboName) { this.comboName = comboName; }

    public BigDecimal getComboPrice() { return comboPrice; }
    public void setComboPrice(BigDecimal comboPrice) { this.comboPrice = comboPrice; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public boolean isProductAvailable() { return isProductAvailable; }
    public void setProductAvailable(boolean productAvailable) { isProductAvailable = productAvailable; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean canBeIncluded() {
        return isValidQuantity() && isProductAvailable();
    }

    public BigDecimal getCalculatedTotalValue() {
        if (productPrice == null) return BigDecimal.ZERO;
        return productPrice.multiply(new BigDecimal(quantity));
    }

    public String getFormattedProductPrice() {
        return productPrice != null ? String.format("%.2f", productPrice) : "0.00";
    }

    public String getFormattedTotalValue() {
        return totalValue != null ? String.format("%.2f", totalValue) : "0.00";
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

    public boolean isLowStock() {
        return stockQuantity <= 5;
    }

    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }

    public String getStockStatus() {
        if (!isProductActive) return "Discontinued";
        if (isOutOfStock()) return "Out of Stock";
        if (isLowStock()) return "Low Stock";
        return "In Stock";
    }

    public String getStockStatusColor() {
        if (!isProductActive) return "red";
        if (isOutOfStock()) return "red";
        if (isLowStock()) return "orange";
        return "green";
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

    public boolean hasEnoughStock() {
        return stockQuantity >= quantity;
    }

    public int getMaxAvailableQuantity() {
        return Math.max(0, stockQuantity);
    }

    public String getAvailabilityBadge() {
        if (!isProductActive) return "🚫 Discontinued";
        if (isOutOfStock()) return "❌ Out of Stock";
        if (isLowStock()) return "⚠️ Low Stock";
        if (!hasEnoughStock()) return "⚠️ Limited Stock";
        return "✅ Available";
    }

    public String getAvailabilityColor() {
        if (!isProductActive) return "red";
        if (isOutOfStock()) return "red";
        if (isLowStock()) return "orange";
        if (!hasEnoughStock()) return "orange";
        return "green";
    }

    public String getItemSummary() {
        return getQuantityDisplay() + " of " + getDisplayName() + " - " + getFormattedTotalValue();
    }

    public boolean isHeavyItem() {
        return weight != null && weight.compareTo(new BigDecimal("5.0")) > 0;
    }

    public String getFormattedWeight() {
        return weight != null ? weight.toString() + " kg" : "N/A";
    }

    public String getProductBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (isLowStock()) {
            badges.append(" ⚠️");
        }
        
        if (isHeavyItem()) {
            badges.append(" 📦");
        }
        
        return badges.toString().trim();
    }

    public boolean canBePurchasedIndividually() {
        return isProductAvailable() && isProductActive && isProductInStock;
    }

    public String getComboContext() {
        return "Part of: " + (comboName != null ? comboName : "Unknown Combo");
    }

    public String getValueContribution() {
        if (totalValue != null && comboPrice != null && comboPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percentage = totalValue.divide(comboPrice, 4, java.math.RoundingMode.HALF_UP)
                                              .multiply(new BigDecimal("100"));
            return String.format("%.1f%% of combo value", percentage);
        }
        return "Unknown contribution";
    }

    public boolean isEssentialItem() {
        return quantity > 1 || (weight != null && weight.compareTo(new BigDecimal("2.0")) > 0);
    }

    public String getItemType() {
        if (isEssentialItem()) return "Essential";
        return "Standard";
    }
}
