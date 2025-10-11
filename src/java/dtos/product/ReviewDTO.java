package dtos.product;

import java.time.LocalDateTime;

/**
 * DTO for product review data
 * Used for displaying product reviews with user information
 */
public class ReviewDTO {
    private int reviewId;
    private int userId;
    private int productId;
    private int rating;
    private String comment;
    private boolean isVerifiedPurchase;
    private int helpfulCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User information (flattened for display)
    private String username;
    private String userFullName;
    private String userAvatarUrl;
    
    // Product information (flattened for context)
    private String productName;
    private String productImageUrl;

    // Constructors
    public ReviewDTO() {}

    public ReviewDTO(int reviewId, int userId, int productId, int rating, String comment) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.isVerifiedPurchase = false;
        this.helpfulCount = 0;
    }

    // Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isVerifiedPurchase() { return isVerifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { isVerifiedPurchase = verifiedPurchase; }

    public int getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserAvatarUrl() { return userAvatarUrl; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    // Business methods
    public boolean isValidRating() {
        return rating >= 1 && rating <= 5;
    }

    public boolean isHighRating() {
        return rating >= 4;
    }

    public boolean isLowRating() {
        return rating <= 2;
    }

    public String getRatingStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    public String getRatingDisplay() {
        return rating + " out of 5 stars";
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toLocalDate().toString() : "Unknown";
    }

    public String getDisplayName() {
        return userFullName != null && !userFullName.trim().isEmpty() ? userFullName : username;
    }

    public String getShortComment() {
        if (comment != null && comment.length() > 200) {
            return comment.substring(0, 197) + "...";
        }
        return comment;
    }

    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }

    public boolean isRecentReview() {
        if (createdAt == null) return false;
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return createdAt.isAfter(oneMonthAgo);
    }

    public boolean isHelpful() {
        return helpfulCount > 0;
    }

    public String getHelpfulDisplay() {
        if (helpfulCount == 0) return "No helpful votes";
        if (helpfulCount == 1) return "1 person found this helpful";
        return helpfulCount + " people found this helpful";
    }

    public boolean hasUserAvatar() {
        return userAvatarUrl != null && !userAvatarUrl.trim().isEmpty();
    }

    public String getUserAvatarOrDefault() {
        return hasUserAvatar() ? userAvatarUrl : "/images/default-avatar.jpg";
    }

    public String getRatingColor() {
        switch (rating) {
            case 5: return "green";
            case 4: return "lightgreen";
            case 3: return "orange";
            case 2: return "red";
            case 1: return "darkred";
            default: return "gray";
        }
    }

    public String getRatingLabel() {
        switch (rating) {
            case 5: return "Excellent";
            case 4: return "Good";
            case 3: return "Average";
            case 2: return "Poor";
            case 1: return "Very Poor";
            default: return "Unknown";
        }
    }

    public boolean isVerified() {
        return isVerifiedPurchase;
    }

    public String getVerifiedBadge() {
        return isVerifiedPurchase ? "✓ Verified Purchase" : "";
    }

    public String getTimeAgo() {
        if (createdAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(createdAt, now).toDays();
        
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        if (days < 365) return (days / 30) + " months ago";
        return (days / 365) + " years ago";
    }

    public boolean wasUpdated() {
        return updatedAt != null && !updatedAt.equals(createdAt);
    }

    public String getReviewTitle() {
        return "Review for " + productName;
    }

    public boolean isDetailedReview() {
        return hasComment() && comment.length() > 100;
    }

    public String getReviewSummary() {
        if (!hasComment()) return "No comment provided";
        if (comment.length() <= 50) return comment;
        return comment.substring(0, 47) + "...";
    }
}
