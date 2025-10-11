package dtos.product;

import java.time.LocalDateTime;

/**
 * DTO for subcategory information
 * Used for displaying subcategory details with parent category info
 */
public class SubcategoryDTO {
    private int subcategoryId;
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String slug;
    private int sortOrder;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Parent category information (flattened)
    private String categoryName;
    private String categorySlug;
    private String parentCategoryIcon;

    // Additional information
    private int productCount;

    // Constructors
    public SubcategoryDTO() {}

    public SubcategoryDTO(int subcategoryId, int categoryId, String name, String slug) {
        this.subcategoryId = subcategoryId;
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
    }

    public SubcategoryDTO(int categoryId, String name, String slug) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
    }

    // Getters and Setters
    public int getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(int subcategoryId) { this.subcategoryId = subcategoryId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategorySlug() { return categorySlug; }
    public void setCategorySlug(String categorySlug) { this.categorySlug = categorySlug; }

    public String getParentCategoryIcon() { return parentCategoryIcon; }
    public void setParentCategoryIcon(String parentCategoryIcon) { this.parentCategoryIcon = parentCategoryIcon; }

    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    // Business methods
    public String getDisplayName() {
        return name != null ? name : "Unknown Subcategory";
    }

    public String getSubcategoryUrl() {
        return slug != null ? "/subcategory/" + slug : "/subcategory/" + subcategoryId;
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public boolean hasParentCategory() {
        return categoryName != null && categoryId > 0;
    }

    public String getBreadcrumb() {
        if (hasParentCategory()) {
            return categoryName + " > " + name;
        }
        return name;
    }

    public String getFullPath() {
        if (hasParentCategory()) {
            return "/category/" + categorySlug + "/subcategory/" + slug;
        }
        return getSubcategoryUrl();
    }

    public boolean hasProducts() {
        return productCount > 0;
    }

    public String getProductCountDisplay() {
        if (productCount == 0) return "No products";
        if (productCount == 1) return "1 product";
        return productCount + " products";
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? imageUrl : "/images/default-subcategory.jpg";
    }

    public String getShortDescription() {
        if (description != null && description.length() > 100) {
            return description.substring(0, 97) + "...";
        }
        return description;
    }

    public String getCategoryIcon() {
        if (parentCategoryIcon != null && !parentCategoryIcon.trim().isEmpty()) {
            return parentCategoryIcon;
        }
        return "📦"; // Default icon
    }

    public boolean isPopular() {
        return hasProducts() && productCount >= 5;
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getParentCategoryUrl() {
        return categorySlug != null ? "/category/" + categorySlug : "/category/" + categoryId;
    }

    public boolean belongsToCategory(int categoryId) {
        return this.categoryId == categoryId;
    }

    public String getDisplayTitle() {
        return name + " - " + categoryName;
    }

    public boolean hasValidParent() {
        return categoryId > 0 && categoryName != null && !categoryName.trim().isEmpty();
    }

    public String getMetaTitle() {
        return name + " | " + categoryName + " | Fruit Store";
    }

    public String getMetaDescription() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }
        return "Browse " + name + " products in " + categoryName + " category. Fresh and organic fruits and vegetables.";
    }
}
