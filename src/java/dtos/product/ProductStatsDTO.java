package dtos.product;

import java.time.LocalDateTime;

/**
 * DTO for product statistics information
 * Used for displaying product sales and performance data
 */
public class ProductStatsDTO {
    private int productId;
    private int totalSold;
    private LocalDateTime lastSoldAt;
    
    // Product information (flattened for context)
    private String productName;
    private String productImageUrl;
    
    // Additional statistics
    private double averageRating;
    private int totalReviews;
    private int totalViews;
    private int totalWishlistAdds;
    private int totalCartAdds;
    private LocalDateTime lastViewedAt;
    private LocalDateTime lastWishlistAddedAt;

    // Constructors
    public ProductStatsDTO() {}

    public ProductStatsDTO(int productId, int totalSold, LocalDateTime lastSoldAt) {
        this.productId = productId;
        this.totalSold = totalSold;
        this.lastSoldAt = lastSoldAt;
    }

    public ProductStatsDTO(int productId) {
        this.productId = productId;
        this.totalSold = 0;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getTotalSold() { return totalSold; }
    public void setTotalSold(int totalSold) { this.totalSold = totalSold; }

    public LocalDateTime getLastSoldAt() { return lastSoldAt; }
    public void setLastSoldAt(LocalDateTime lastSoldAt) { this.lastSoldAt = lastSoldAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

    public int getTotalViews() { return totalViews; }
    public void setTotalViews(int totalViews) { this.totalViews = totalViews; }

    public int getTotalWishlistAdds() { return totalWishlistAdds; }
    public void setTotalWishlistAdds(int totalWishlistAdds) { this.totalWishlistAdds = totalWishlistAdds; }

    public int getTotalCartAdds() { return totalCartAdds; }
    public void setTotalCartAdds(int totalCartAdds) { this.totalCartAdds = totalCartAdds; }

    public LocalDateTime getLastViewedAt() { return lastViewedAt; }
    public void setLastViewedAt(LocalDateTime lastViewedAt) { this.lastViewedAt = lastViewedAt; }

    public LocalDateTime getLastWishlistAddedAt() { return lastWishlistAddedAt; }
    public void setLastWishlistAddedAt(LocalDateTime lastWishlistAddedAt) { this.lastWishlistAddedAt = lastWishlistAddedAt; }

    // Business methods
    public boolean hasBeenSold() {
        return totalSold > 0;
    }

    public boolean isBestSeller(int threshold) {
        return totalSold >= threshold;
    }

    public boolean isBestSeller() {
        return isBestSeller(100); // Default threshold
    }

    public boolean wasSoldRecently(int hours) {
        if (lastSoldAt == null) return false;
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        return lastSoldAt.isAfter(cutoff);
    }

    public boolean wasSoldToday() {
        return wasSoldRecently(24);
    }

    public boolean wasSoldThisWeek() {
        return wasSoldRecently(168); // 7 days * 24 hours
    }

    public String getLastSoldFormatted() {
        if (lastSoldAt == null) return "Never sold";
        return lastSoldAt.toLocalDate().toString();
    }

    public String getLastSoldTimeAgo() {
        if (lastSoldAt == null) return "Never";
        
        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(lastSoldAt, now).toDays();
        
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        if (days < 365) return (days / 30) + " months ago";
        return (days / 365) + " years ago";
    }

    public String getSalesStatus() {
        if (totalSold == 0) return "Not sold yet";
        if (totalSold < 10) return "Low sales";
        if (totalSold < 50) return "Moderate sales";
        if (totalSold < 100) return "Good sales";
        if (totalSold < 500) return "High sales";
        return "Very high sales";
    }

    public String getSalesStatusVietnamese() {
        if (totalSold == 0) return "Chưa bán";
        if (totalSold < 10) return "Ít bán";
        if (totalSold < 50) return "Bán chậm";
        if (totalSold < 100) return "Bán tốt";
        return "Bán chạy";
    }

    public boolean isTrending() {
        return wasSoldRecently(24) && totalSold >= 5;
    }

    public boolean isPopular() {
        return totalViews > 100 || totalWishlistAdds > 20 || totalCartAdds > 10;
    }

    public String getPopularityLevel() {
        int score = totalViews + (totalWishlistAdds * 2) + (totalCartAdds * 3);
        
        if (score >= 500) return "Very Popular";
        if (score >= 200) return "Popular";
        if (score >= 100) return "Moderately Popular";
        if (score >= 50) return "Somewhat Popular";
        return "Not Popular";
    }

    public double getConversionRate() {
        if (totalViews == 0) return 0.0;
        return (double) totalSold / totalViews * 100;
    }

    public String getFormattedConversionRate() {
        return String.format("%.2f%%", getConversionRate());
    }

    public double getWishlistConversionRate() {
        if (totalWishlistAdds == 0) return 0.0;
        return (double) totalSold / totalWishlistAdds * 100;
    }

    public String getFormattedWishlistConversionRate() {
        return String.format("%.2f%%", getWishlistConversionRate());
    }

    public String getFormattedAverageRating() {
        return String.format("%.1f", averageRating);
    }

    public boolean hasGoodRating() {
        return averageRating >= 4.0;
    }

    public boolean hasPoorRating() {
        return averageRating < 3.0;
    }

    public String getRatingStars() {
        int fullStars = (int) averageRating;
        int halfStar = (averageRating - fullStars) >= 0.5 ? 1 : 0;
        int emptyStars = 5 - fullStars - halfStar;
        
        StringBuilder stars = new StringBuilder();
        stars.append("★".repeat(fullStars));
        if (halfStar > 0) stars.append("☆");
        stars.append("☆".repeat(emptyStars));
        
        return stars.toString();
    }

    public boolean isRecentlyViewed() {
        if (lastViewedAt == null) return false;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return lastViewedAt.isAfter(oneHourAgo);
    }

    public boolean isRecentlyWishlisted() {
        if (lastWishlistAddedAt == null) return false;
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return lastWishlistAddedAt.isAfter(oneDayAgo);
    }

    public String getEngagementScore() {
        int score = totalViews + (totalWishlistAdds * 3) + (totalCartAdds * 5) + (totalSold * 10);
        
        if (score >= 1000) return "Very High";
        if (score >= 500) return "High";
        if (score >= 200) return "Medium";
        if (score >= 100) return "Low";
        return "Very Low";
    }

    public String getSalesPerformance() {
        if (totalSold == 0) return "No Sales";
        if (totalSold < 5) return "Poor";
        if (totalSold < 20) return "Fair";
        if (totalSold < 50) return "Good";
        if (totalSold < 100) return "Very Good";
        return "Excellent";
    }

    public boolean isHighPerformer() {
        return isBestSeller() && hasGoodRating() && isPopular();
    }

    public String getPerformanceBadge() {
        if (isHighPerformer()) return "High Performer";
        if (isBestSeller()) return "Best Seller";
        if (isTrending()) return "Trending";
        if (isPopular()) return "Popular";
        return "";
    }

    public String getFormattedTotalSold() {
        if (totalSold == 0) return "0";
        if (totalSold < 1000) return String.valueOf(totalSold);
        if (totalSold < 1000000) return String.format("%.1fK", totalSold / 1000.0);
        return String.format("%.1fM", totalSold / 1000000.0);
    }
}
