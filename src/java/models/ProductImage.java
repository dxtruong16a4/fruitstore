package models;

import java.time.LocalDateTime;

public class ProductImage {
    private int imageId;
    private int productId;
    private String imageUrl;
    private String altText;
    private boolean isPrimary;
    private int sortOrder;
    private LocalDateTime createdAt;

    // Constructors
    public ProductImage() {}

    public ProductImage(int productId, String imageUrl) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = false;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
    }

    public ProductImage(int productId, String imageUrl, String altText, boolean isPrimary) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.isPrimary = isPrimary;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Business methods
    public void setAsPrimary() {
        this.isPrimary = true;
    }

    public boolean isThumbnail() {
        return isPrimary || sortOrder == 0;
    }
}
