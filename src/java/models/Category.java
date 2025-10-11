package models;

import java.time.LocalDateTime;

public class Category {
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String icon;
    private String slug;
    private int sortOrder;
    private boolean isActive;
    private Integer parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Category parentCategory; // For joined queries

    // Constructors
    public Category() {}

    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public boolean isMainCategory() {
        return parentId == null;
    }

    public boolean isSubCategory() {
        return parentId != null;
    }

    public Category getParentCategory() { return parentCategory; }
    public void setParentCategory(Category parentCategory) { 
        this.parentCategory = parentCategory; 
        this.parentId = parentCategory != null ? parentCategory.getCategoryId() : null;
    }

    public String getParentCategoryName() {
        return parentCategory != null ? parentCategory.getName() : null;
    }

    public boolean hasParent() {
        return parentCategory != null || parentId != null;
    }
}