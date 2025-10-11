package models;

import java.time.LocalDateTime;

public class ProductStats {
    private int productId;
    private int totalSold;
    private LocalDateTime lastSoldAt;
    private Product product; // For joined queries

    // Constructors
    public ProductStats() {}

    public ProductStats(int productId) {
        this.productId = productId;
        this.totalSold = 0;
    }

    public ProductStats(int productId, int totalSold, LocalDateTime lastSoldAt) {
        this.productId = productId;
        this.totalSold = totalSold;
        this.lastSoldAt = lastSoldAt;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getTotalSold() { return totalSold; }
    public void setTotalSold(int totalSold) { this.totalSold = totalSold; }

    public LocalDateTime getLastSoldAt() { return lastSoldAt; }
    public void setLastSoldAt(LocalDateTime lastSoldAt) { this.lastSoldAt = lastSoldAt; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // Business methods
    public void recordSale(int quantity) {
        this.totalSold += quantity;
        this.lastSoldAt = LocalDateTime.now();
    }

    public boolean hasBeenSold() {
        return totalSold > 0;
    }

    public boolean isBestSeller(int threshold) {
        return totalSold >= threshold;
    }

    public boolean wasSoldRecently(int hours) {
        if (lastSoldAt == null) return false;
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        return lastSoldAt.isAfter(cutoff);
    }

    public String getLastSoldFormatted() {
        if (lastSoldAt == null) return "Chưa bán";
        return lastSoldAt.toLocalDate().toString();
    }

    public String getSalesStatus() {
        if (totalSold == 0) return "Chưa bán";
        if (totalSold < 10) return "Ít bán";
        if (totalSold < 50) return "Bán chậm";
        if (totalSold < 100) return "Bán tốt";
        return "Bán chạy";
    }

    public boolean isTrending() {
        return wasSoldRecently(24) && totalSold >= 5;
    }
}
