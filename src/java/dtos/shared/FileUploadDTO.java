package dtos.shared;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for file upload data
 * Used for handling file uploads across all endpoints
 */
public class FileUploadDTO {
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private String fileUrl;
    private long fileSize;
    private String contentType;
    private String fileExtension;
    private String mimeType;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
    private String uploadType;
    private String description;
    private boolean isPublic;
    private String category;
    private Map<String, Object> metadata;
    private String checksum;
    private int width;
    private int height;
    private String altText;
    private String caption;

    // Constructors
    public FileUploadDTO() {}

    public FileUploadDTO(String originalFileName, String contentType, long fileSize) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
    }

    public FileUploadDTO(String originalFileName, String storedFileName, String filePath, 
                        long fileSize, String contentType) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getStoredFileName() { return storedFileName; }
    public void setStoredFileName(String storedFileName) { this.storedFileName = storedFileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getFileExtension() { return fileExtension; }
    public void setFileExtension(String fileExtension) { this.fileExtension = fileExtension; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getUploadType() { return uploadType; }
    public void setUploadType(String uploadType) { this.uploadType = uploadType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    // Business methods
    public String getDisplayFileName() {
        return originalFileName != null ? originalFileName : "Unknown File";
    }

    public String getShortFileName() {
        if (originalFileName == null) return "Unknown";
        if (originalFileName.length() > 30) {
            return originalFileName.substring(0, 27) + "...";
        }
        return originalFileName;
    }

    public String getFormattedFileSize() {
        if (fileSize <= 0) return "0 B";
        
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
    }

    public String getFormattedUploadedAt() {
        return uploadedAt != null ? uploadedAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedDateTime() {
        return uploadedAt != null ? uploadedAt.toString() : "Unknown";
    }

    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }

    public boolean isAudio() {
        return contentType != null && contentType.startsWith("audio/");
    }

    public boolean isDocument() {
        return contentType != null && (
            contentType.contains("pdf") || 
            contentType.contains("word") || 
            contentType.contains("excel") || 
            contentType.contains("powerpoint") ||
            contentType.contains("text")
        );
    }

    public boolean isArchive() {
        return contentType != null && (
            contentType.contains("zip") || 
            contentType.contains("rar") || 
            contentType.contains("7z") || 
            contentType.contains("tar")
        );
    }

    public String getFileTypeIcon() {
        if (isImage()) return "🖼️";
        if (isVideo()) return "🎥";
        if (isAudio()) return "🎵";
        if (isDocument()) return "📄";
        if (isArchive()) return "📦";
        return "📁";
    }

    public String getFileTypeDisplayName() {
        if (isImage()) return "Image";
        if (isVideo()) return "Video";
        if (isAudio()) return "Audio";
        if (isDocument()) return "Document";
        if (isArchive()) return "Archive";
        return "File";
    }

    public boolean isJpegImage() {
        return isImage() && (contentType.contains("jpeg") || contentType.contains("jpg"));
    }

    public boolean isPngImage() {
        return isImage() && contentType.contains("png");
    }

    public boolean isGifImage() {
        return isImage() && contentType.contains("gif");
    }

    public boolean isWebpImage() {
        return isImage() && contentType.contains("webp");
    }

    public boolean isPdfDocument() {
        return contentType != null && contentType.contains("pdf");
    }

    public String getFileExtensionFromName() {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public String getFileExtensionOrDefault() {
        if (fileExtension != null && !fileExtension.trim().isEmpty()) {
            return fileExtension.toLowerCase();
        }
        return getFileExtensionFromName();
    }

    public boolean hasValidExtension() {
        String ext = getFileExtensionOrDefault();
        return ext != null && !ext.trim().isEmpty();
    }

    public boolean isLargeFile() {
        return fileSize > 10 * 1024 * 1024; // 10MB
    }

    public boolean isSmallFile() {
        return fileSize < 1024 * 1024; // 1MB
    }

    public String getFileSizeCategory() {
        if (fileSize < 1024 * 1024) return "Small";
        if (fileSize < 10 * 1024 * 1024) return "Medium";
        if (fileSize < 100 * 1024 * 1024) return "Large";
        return "Very Large";
    }

    public String getFileSizeColor() {
        if (isSmallFile()) return "green";
        if (isLargeFile()) return "red";
        return "orange";
    }

    public boolean hasDimensions() {
        return width > 0 && height > 0;
    }

    public String getDimensionsDisplay() {
        if (hasDimensions()) {
            return width + "x" + height;
        }
        return "Unknown";
    }

    public boolean isLandscape() {
        return hasDimensions() && width > height;
    }

    public boolean isPortrait() {
        return hasDimensions() && height > width;
    }

    public boolean isSquare() {
        return hasDimensions() && width == height;
    }

    public String getOrientation() {
        if (isSquare()) return "Square";
        if (isLandscape()) return "Landscape";
        if (isPortrait()) return "Portrait";
        return "Unknown";
    }

    public String getAspectRatio() {
        if (!hasDimensions()) return "Unknown";
        
        int gcd = gcd(width, height);
        return (width / gcd) + ":" + (height / gcd);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public boolean isHighResolution() {
        return hasDimensions() && (width > 1920 || height > 1080);
    }

    public boolean isLowResolution() {
        return hasDimensions() && (width < 400 || height < 300);
    }

    public String getResolutionCategory() {
        if (!hasDimensions()) return "Unknown";
        if (isHighResolution()) return "High";
        if (isLowResolution()) return "Low";
        return "Medium";
    }

    public String getResolutionColor() {
        if (isHighResolution()) return "green";
        if (isLowResolution()) return "orange";
        return "blue";
    }

    public boolean hasMetadata() {
        return metadata != null && !metadata.isEmpty();
    }

    public void addMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new java.util.HashMap<>();
        }
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    public boolean hasChecksum() {
        return checksum != null && !checksum.trim().isEmpty();
    }

    public boolean isRecentlyUploaded() {
        if (uploadedAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return uploadedAt.isAfter(oneWeekAgo);
    }

    public boolean isTodayUploaded() {
        if (uploadedAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return uploadedAt.isAfter(today) && uploadedAt.isBefore(tomorrow);
    }

    public String getUploadTimeAgo() {
        if (uploadedAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(uploadedAt, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        
        long days = hours / 24;
        if (days < 7) return days + " day" + (days == 1 ? "" : "s") + " ago";
        
        return getFormattedUploadedAt();
    }

    public String getCategoryOrDefault() {
        return category != null ? category : "general";
    }

    public String getCategoryIcon() {
        switch (getCategoryOrDefault().toLowerCase()) {
            case "product": return "📦";
            case "user": return "👤";
            case "order": return "📋";
            case "promotion": return "🎉";
            case "system": return "⚙️";
            case "document": return "📄";
            case "image": return "🖼️";
            default: return "📁";
        }
    }

    public String getUploadTypeOrDefault() {
        return uploadType != null ? uploadType : "general";
    }

    public boolean isPublicUpload() {
        return isPublic;
    }

    public boolean isPrivateUpload() {
        return !isPublic;
    }

    public String getVisibilityIcon() {
        return isPublic ? "🌐" : "🔒";
    }

    public String getVisibilityText() {
        return isPublic ? "Public" : "Private";
    }

    public String getVisibilityColor() {
        return isPublic ? "green" : "orange";
    }

    public String getFileSummary() {
        return getFileTypeIcon() + " " + getShortFileName() + " (" + getFormattedFileSize() + ")";
    }

    public String getDetailedSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getFileTypeIcon()).append(" ");
        summary.append(getDisplayFileName()).append(" ");
        summary.append("(").append(getFormattedFileSize());
        
        if (hasDimensions()) {
            summary.append(", ").append(getDimensionsDisplay());
        }
        
        summary.append(")");
        
        return summary.toString();
    }

    public boolean isValidFile() {
        return originalFileName != null && !originalFileName.trim().isEmpty() &&
               contentType != null && !contentType.trim().isEmpty() &&
               fileSize > 0;
    }

    public String getValidationErrors() {
        StringBuilder errors = new StringBuilder();
        
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            errors.append("File name is required. ");
        }
        
        if (contentType == null || contentType.trim().isEmpty()) {
            errors.append("Content type is required. ");
        }
        
        if (fileSize <= 0) {
            errors.append("File size must be greater than 0. ");
        }
        
        return errors.toString().trim();
    }

    public String getFileUrlOrDefault() {
        return fileUrl != null ? fileUrl : filePath;
    }

    public boolean hasUrl() {
        return (fileUrl != null && !fileUrl.trim().isEmpty()) || 
               (filePath != null && !filePath.trim().isEmpty());
    }

    public String getAltTextOrDefault() {
        return altText != null && !altText.trim().isEmpty() ? altText : getDisplayFileName();
    }

    public String getCaptionOrDefault() {
        return caption != null && !caption.trim().isEmpty() ? caption : "";
    }
}
