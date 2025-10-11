package models;

import java.time.LocalDateTime;

public class Subcategory {
    private int subcategoryId;
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String slug;
    private int sortOrder;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Constructors
    public Subcategory() {}

    public Subcategory(int categoryId, String name, String slug) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.isActive = true;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
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
}