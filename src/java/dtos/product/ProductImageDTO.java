package dtos.product;

import java.time.LocalDateTime;

/**
 * DTO for product image information
 * Used for displaying and managing product images
 */
public class ProductImageDTO {
    private int imageId;
    private int productId;
    private String imageUrl;
    private String altText;
    private boolean isPrimary;
    private int sortOrder;
    private LocalDateTime createdAt;
    
    // Additional display information
    private String thumbnailUrl;
    private String mediumUrl;
    private String largeUrl;
    private long fileSize;
    private String fileType;
    private int width;
    private int height;

    // Constructors
    public ProductImageDTO() {}

    public ProductImageDTO(int productId, String imageUrl) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = false;
        this.sortOrder = 0;
    }

    public ProductImageDTO(int imageId, int productId, String imageUrl, boolean isPrimary) {
        this.imageId = imageId;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.sortOrder = 0;
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

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getMediumUrl() { return mediumUrl; }
    public void setMediumUrl(String mediumUrl) { this.mediumUrl = mediumUrl; }

    public String getLargeUrl() { return largeUrl; }
    public void setLargeUrl(String largeUrl) { this.largeUrl = largeUrl; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    // Business methods
    public String getDisplayUrl() {
        return imageUrl != null ? imageUrl : "/images/default-product.jpg";
    }

    public String getThumbnailDisplayUrl() {
        return thumbnailUrl != null ? thumbnailUrl : getDisplayUrl();
    }

    public String getMediumDisplayUrl() {
        return mediumUrl != null ? mediumUrl : getDisplayUrl();
    }

    public String getLargeDisplayUrl() {
        return largeUrl != null ? largeUrl : getDisplayUrl();
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public boolean isThumbnail() {
        return isPrimary || sortOrder == 0;
    }

    public String getAltTextOrDefault() {
        if (altText != null && !altText.trim().isEmpty()) {
            return altText;
        }
        return "Product Image";
    }

    public boolean hasValidUrl() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    public String getFileExtension() {
        if (imageUrl != null && imageUrl.contains(".")) {
            return imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();
        }
        return "unknown";
    }

    public boolean isImageFile() {
        String extension = getFileExtension();
        return extension.equals("jpg") || extension.equals("jpeg") || 
               extension.equals("png") || extension.equals("gif") || 
               extension.equals("webp");
    }

    public String getFormattedFileSize() {
        if (fileSize <= 0) return "Unknown size";
        
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
    }

    public String getDimensions() {
        if (width > 0 && height > 0) {
            return width + "x" + height;
        }
        return "Unknown";
    }

    public boolean isHighResolution() {
        return width > 1920 || height > 1080;
    }

    public boolean isLowResolution() {
        return width < 400 || height < 300;
    }

    public String getAspectRatio() {
        if (width > 0 && height > 0) {
            int gcd = gcd(width, height);
            return (width / gcd) + ":" + (height / gcd);
        }
        return "Unknown";
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public boolean isLandscape() {
        return width > height;
    }

    public boolean isPortrait() {
        return height > width;
    }

    public boolean isSquare() {
        return width == height;
    }

    public String getOrientation() {
        if (isSquare()) return "Square";
        if (isLandscape()) return "Landscape";
        if (isPortrait()) return "Portrait";
        return "Unknown";
    }

    public boolean isRecentlyAdded() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getImageType() {
        String extension = getFileExtension();
        switch (extension) {
            case "jpg":
            case "jpeg": return "JPEG";
            case "png": return "PNG";
            case "gif": return "GIF";
            case "webp": return "WebP";
            default: return "Unknown";
        }
    }

    public boolean isOptimized() {
        return fileType != null && fileType.equals("optimized");
    }

    public String getImageQuality() {
        if (isHighResolution()) return "High";
        if (isLowResolution()) return "Low";
        return "Medium";
    }

    public String getImageUrlForSize(String size) {
        switch (size.toLowerCase()) {
            case "thumbnail":
            case "thumb": return getThumbnailDisplayUrl();
            case "medium": return getMediumDisplayUrl();
            case "large": return getLargeDisplayUrl();
            default: return getDisplayUrl();
        }
    }
}
