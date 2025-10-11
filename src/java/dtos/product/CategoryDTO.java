package dtos.product;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for category information
 * Used for displaying category details with hierarchy and related data
 */
public class CategoryDTO {
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String icon;
    private String slug;
    private int sortOrder;
    private boolean isActive;
    private Integer parentId;
    private String parentCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related data
    private List<CategoryDTO> subcategories;
    private List<SubcategoryDTO> subcategoryDTOs;
    private int productCount;
    private int totalProducts;

    // Constructors
    public CategoryDTO() {}

    public CategoryDTO(int categoryId, String name, String slug) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
    }

    public CategoryDTO(String name, String slug) {
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
    }

    // Getters and Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public String getParentCategoryName() { return parentCategoryName; }
    public void setParentCategoryName(String parentCategoryName) { this.parentCategoryName = parentCategoryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<CategoryDTO> getSubcategories() { return subcategories; }
    public void setSubcategories(List<CategoryDTO> subcategories) { this.subcategories = subcategories; }

    public List<SubcategoryDTO> getSubcategoryDTOs() { return subcategoryDTOs; }
    public void setSubcategoryDTOs(List<SubcategoryDTO> subcategoryDTOs) { this.subcategoryDTOs = subcategoryDTOs; }

    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    public int getTotalProducts() { return totalProducts; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

    // Business methods
    public boolean isMainCategory() {
        return parentId == null;
    }

    public boolean isSubCategory() {
        return parentId != null;
    }

    public boolean hasParent() {
        return parentCategoryName != null || parentId != null;
    }

    public String getDisplayName() {
        return name != null ? name : "Unknown Category";
    }

    public String getCategoryUrl() {
        return slug != null ? "/category/" + slug : "/category/" + categoryId;
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toLocalDate().toString() : "Unknown";
    }

    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }

    public boolean hasProducts() {
        return productCount > 0 || totalProducts > 0;
    }

    public int getTotalProductCount() {
        return Math.max(productCount, totalProducts);
    }

    public String getBreadcrumb() {
        if (hasParent()) {
            return parentCategoryName + " > " + name;
        }
        return name;
    }

    public String getCategoryIcon() {
        if (icon != null && !icon.trim().isEmpty()) {
            return icon;
        }
        // Default icons based on category name
        if (name != null) {
            String lowerName = name.toLowerCase();
            if (lowerName.contains("fruit")) return "🍎";
            if (lowerName.contains("vegetable")) return "🥬";
            if (lowerName.contains("organic")) return "🌿";
            if (lowerName.contains("fresh")) return "🆕";
        }
        return "📦"; // Default icon
    }

    public boolean isPopular() {
        return hasProducts() && getTotalProductCount() >= 10;
    }

    public String getProductCountDisplay() {
        int count = getTotalProductCount();
        if (count == 0) return "No products";
        if (count == 1) return "1 product";
        return count + " products";
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? imageUrl : "/images/default-category.jpg";
    }

    public String getShortDescription() {
        if (description != null && description.length() > 150) {
            return description.substring(0, 147) + "...";
        }
        return description;
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public boolean isRecentlyUpdated() {
        if (updatedAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return updatedAt.isAfter(oneWeekAgo);
    }
}
