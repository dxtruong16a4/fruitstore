package dtos.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for product information for display
 * Used for displaying product details in listings and detail pages
 */
public class ProductDTO {
    private int productId;
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private int stockQuantity;
    private int minStockLevel;
    private BigDecimal weight;
    private String unit;
    private String imageUrl;
    private int categoryId;
    private int subcategoryId;
    private String categoryName;
    private String subcategoryName;
    private int discountPercent;
    private boolean isNew;
    private boolean isBestSeller;
    private boolean isFeatured;
    private boolean isActive;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional display information
    private boolean isInStock;
    private boolean isLowStock;
    private boolean isOnSale;
    private BigDecimal discountAmount;
    private String formattedPrice;
    private String formattedOriginalPrice;
    private String formattedDiscountAmount;
    private String stockStatus;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(int productId, String name, String description, BigDecimal price, String categoryName) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
        this.isActive = true;
        this.isInStock = true;
        this.unit = "kg";
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(int subcategoryId) { this.subcategoryId = subcategoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean aNew) { isNew = aNew; }

    public boolean isBestSeller() { return isBestSeller; }
    public void setBestSeller(boolean bestSeller) { isBestSeller = bestSeller; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }

    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isInStock() { return isInStock; }
    public void setInStock(boolean inStock) { isInStock = inStock; }

    public boolean isLowStock() { return isLowStock; }
    public void setLowStock(boolean lowStock) { isLowStock = lowStock; }

    public boolean isOnSale() { return isOnSale; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getFormattedPrice() { return formattedPrice; }
    public void setFormattedPrice(String formattedPrice) { this.formattedPrice = formattedPrice; }

    public String getFormattedOriginalPrice() { return formattedOriginalPrice; }
    public void setFormattedOriginalPrice(String formattedOriginalPrice) { this.formattedOriginalPrice = formattedOriginalPrice; }

    public String getFormattedDiscountAmount() { return formattedDiscountAmount; }
    public void setFormattedDiscountAmount(String formattedDiscountAmount) { this.formattedDiscountAmount = formattedDiscountAmount; }

    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

    // Business methods
    public String getDisplayName() {
        return name != null ? name : "Unknown Product";
    }

    public String getShortDescriptionDisplay() {
        if (shortDescription != null && !shortDescription.trim().isEmpty()) {
            return shortDescription;
        }
        if (description != null && description.length() > 100) {
            return description.substring(0, 97) + "...";
        }
        return description;
    }

    public String getFormattedWeight() {
        if (weight != null && unit != null) {
            return String.format("%.2f %s", weight, unit);
        }
        return "N/A";
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public boolean canBeAddedToCart() {
        return isActive && isInStock && stockQuantity > 0;
    }

    public boolean isAvailable() {
        return isActive && stockQuantity > 0;
    }

    public String getAvailabilityStatus() {
        if (!isActive) return "Unavailable";
        if (stockQuantity <= 0) return "Out of Stock";
        if (isLowStock) return "Low Stock";
        return "In Stock";
    }

    public String getProductUrl() {
        return slug != null ? "/product/" + slug : "/product/" + productId;
    }

    public boolean hasDiscount() {
        return isOnSale || (originalPrice != null && originalPrice.compareTo(price) > 0);
    }

    public BigDecimal getSavingsPercentage() {
        if (hasDiscount() && originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            return discountAmount.divide(originalPrice, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedSavingsPercentage() {
        return String.format("%.0f%%", getSavingsPercentage());
    }

    public String getProductBadges() {
        StringBuilder badges = new StringBuilder();
        if (isNew) badges.append("New ");
        if (isBestSeller) badges.append("Best Seller ");
        if (isFeatured) badges.append("Featured ");
        if (isOnSale) badges.append("Sale ");
        if (isLowStock && stockQuantity > 0) badges.append("Low Stock ");
        return badges.toString().trim();
    }
}
