package dtos.product;

import java.math.BigDecimal;

/**
 * DTO for product update form data
 * Used for receiving product update data from frontend
 */
public class ProductUpdateDTO {
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
    private Integer subcategoryId;
    private int discountPercent;
    private boolean isNew;
    private boolean isBestSeller;
    private boolean isFeatured;
    private boolean isActive;
    private String slug;
    private String metaTitle;
    private String metaDescription;

    // Constructors
    public ProductUpdateDTO() {}

    public ProductUpdateDTO(int productId, String name, String description, BigDecimal price, int categoryId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.isActive = true;
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

    public Integer getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Integer subcategoryId) { this.subcategoryId = subcategoryId; }

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

    // Business methods
    public boolean isValid() {
        return productId > 0 &&
               name != null && !name.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               price != null && price.compareTo(BigDecimal.ZERO) > 0 &&
               categoryId > 0 &&
               stockQuantity >= 0 &&
               minStockLevel >= 0;
    }

    public boolean hasValidId() {
        return productId > 0;
    }

    public boolean hasValidPrice() {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasValidDiscount() {
        if (originalPrice == null || discountPercent <= 0) return true;
        return originalPrice.compareTo(price) >= 0 && discountPercent <= 100;
    }

    public boolean hasValidStock() {
        return stockQuantity >= 0 && minStockLevel >= 0;
    }

    public boolean hasValidCategory() {
        return categoryId > 0;
    }

    public boolean hasValidWeight() {
        return weight == null || weight.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isFormValid() {
        return isValid() && hasValidId() && hasValidPrice() && hasValidDiscount() && 
               hasValidStock() && hasValidCategory() && hasValidWeight();
    }

    public String getTrimmedName() {
        return name != null ? name.trim() : null;
    }

    public String getTrimmedDescription() {
        return description != null ? description.trim() : null;
    }

    public String getTrimmedShortDescription() {
        return shortDescription != null ? shortDescription.trim() : null;
    }

    public String getTrimmedSlug() {
        return slug != null ? slug.trim().toLowerCase() : null;
    }

    public String getTrimmedMetaTitle() {
        return metaTitle != null ? metaTitle.trim() : null;
    }

    public String getTrimmedMetaDescription() {
        return metaDescription != null ? metaDescription.trim() : null;
    }

    public boolean hasSubcategory() {
        return subcategoryId != null && subcategoryId > 0;
    }

    public BigDecimal getCalculatedOriginalPrice() {
        if (originalPrice != null) return originalPrice;
        if (discountPercent > 0 && discountPercent <= 100) {
            return price.divide(new BigDecimal(100 - discountPercent), 2, java.math.RoundingMode.HALF_UP)
                       .multiply(new BigDecimal(100));
        }
        return null;
    }

    public String getValidationErrors() {
        StringBuilder errors = new StringBuilder();
        
        if (productId <= 0) {
            errors.append("Valid product ID is required. ");
        }
        
        if (name == null || name.trim().isEmpty()) {
            errors.append("Product name is required. ");
        }
        
        if (description == null || description.trim().isEmpty()) {
            errors.append("Product description is required. ");
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Valid price is required. ");
        }
        
        if (categoryId <= 0) {
            errors.append("Valid category is required. ");
        }
        
        if (stockQuantity < 0) {
            errors.append("Stock quantity cannot be negative. ");
        }
        
        if (minStockLevel < 0) {
            errors.append("Minimum stock level cannot be negative. ");
        }
        
        if (!hasValidDiscount()) {
            errors.append("Invalid discount configuration. ");
        }
        
        return errors.toString().trim();
    }

    public boolean hasChanges(String originalName, String originalDescription, BigDecimal originalPrice) {
        return !name.equals(originalName) || 
               !description.equals(originalDescription) || 
               !price.equals(originalPrice);
    }

    public boolean isStockUpdate() {
        return stockQuantity >= 0 || minStockLevel >= 0;
    }

    public boolean isPriceUpdate() {
        return price != null || originalPrice != null || discountPercent > 0;
    }

    public boolean isStatusUpdate() {
        return true; // isNew, isBestSeller, isFeatured, isActive can always be updated
    }
}
