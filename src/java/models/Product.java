package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
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
    private Category category; // For joined queries
    private Subcategory subcategory; // For joined queries
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

    // Constructors
    public Product() {}

    public Product(String name, String description, BigDecimal price, int categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = 0;
        this.minStockLevel = 10;
        this.unit = "kg";
        this.discountPercent = 0;
        this.isNew = false;
        this.isBestSeller = false;
        this.isFeatured = false;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String name, String description, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.categoryId = category != null ? category.getCategoryId() : 0;
        this.stockQuantity = 0;
        this.minStockLevel = 10;
        this.unit = "kg";
        this.discountPercent = 0;
        this.isNew = false;
        this.isBestSeller = false;
        this.isFeatured = false;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Category getCategory() { return category; }
    public void setCategory(Category category) { 
        this.category = category; 
        this.categoryId = category != null ? category.getCategoryId() : 0;
    }

    public Subcategory getSubcategory() { return subcategory; }
    public void setSubcategory(Subcategory subcategory) { 
        this.subcategory = subcategory; 
        this.subcategoryId = subcategory != null ? subcategory.getSubcategoryId() : 0;
    }

    // Business methods
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean isLowStock() {
        return stockQuantity <= minStockLevel;
    }

    public boolean isOnSale() {
        return originalPrice != null && originalPrice.compareTo(price) > 0;
    }

    public BigDecimal getSalePrice() {
        if (isOnSale()) {
            return originalPrice;
        }
        return price;
    }

    public BigDecimal getDiscountAmount() {
        if (isOnSale()) {
            return originalPrice.subtract(price);
        }
        return BigDecimal.ZERO;
    }

    public void reduceStock(int quantity) {
        if (quantity > 0 && stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void addStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
            this.updatedAt = LocalDateTime.now();
        }
    }

    // Business methods with relationships
    public String getCategoryName() {
        return category != null ? category.getName() : "Unknown Category";
    }

    public String getSubcategoryName() {
        return subcategory != null ? subcategory.getName() : null;
    }

    public boolean belongsToCategory(int categoryId) {
        return this.categoryId == categoryId || (category != null && category.getCategoryId() == categoryId);
    }

    public boolean belongsToSubcategory(int subcategoryId) {
        return this.subcategoryId == subcategoryId || (subcategory != null && subcategory.getSubcategoryId() == subcategoryId);
    }
}