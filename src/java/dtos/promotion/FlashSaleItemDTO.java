package dtos.promotion;

import java.math.BigDecimal;

/**
 * DTO for flash sale item details
 * Used for displaying individual items in flash sales with pricing and availability
 */
public class FlashSaleItemDTO {
    private int flashSaleItemId;
    private int flashSaleId;
    private int productId;
    private BigDecimal flashSalePrice;
    private Integer quantityLimit;
    private int soldQuantity;
    
    // Product information (flattened for display)
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productSlug;
    private BigDecimal originalPrice;
    private String categoryName;
    private String subcategoryName;
    private int stockQuantity;
    private boolean isProductActive;
    private boolean isProductInStock;
    private String unit;
    private BigDecimal weight;
    
    // Flash sale information (flattened for context)
    private String flashSaleName;
    private boolean isFlashSaleActive;
    
    // Calculated values
    private BigDecimal savingsAmount;
    private BigDecimal savingsPercentage;
    private int remainingQuantity;
    private boolean isAvailable;
    private boolean isSoldOut;
    private boolean isAlmostSoldOut;
    private double availabilityPercentage;

    // Constructors
    public FlashSaleItemDTO() {}

    public FlashSaleItemDTO(int flashSaleItemId, int flashSaleId, int productId, BigDecimal flashSalePrice) {
        this.flashSaleItemId = flashSaleItemId;
        this.flashSaleId = flashSaleId;
        this.productId = productId;
        this.flashSalePrice = flashSalePrice;
        this.soldQuantity = 0;
    }

    // Getters and Setters
    public int getFlashSaleItemId() { return flashSaleItemId; }
    public void setFlashSaleItemId(int flashSaleItemId) { this.flashSaleItemId = flashSaleItemId; }

    public int getFlashSaleId() { return flashSaleId; }
    public void setFlashSaleId(int flashSaleId) { this.flashSaleId = flashSaleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public BigDecimal getFlashSalePrice() { return flashSalePrice; }
    public void setFlashSalePrice(BigDecimal flashSalePrice) { this.flashSalePrice = flashSalePrice; }

    public Integer getQuantityLimit() { return quantityLimit; }
    public void setQuantityLimit(Integer quantityLimit) { this.quantityLimit = quantityLimit; }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getProductSlug() { return productSlug; }
    public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public boolean isProductActive() { return isProductActive; }
    public void setProductActive(boolean productActive) { isProductActive = productActive; }

    public boolean isProductInStock() { return isProductInStock; }
    public void setProductInStock(boolean productInStock) { isProductInStock = productInStock; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getFlashSaleName() { return flashSaleName; }
    public void setFlashSaleName(String flashSaleName) { this.flashSaleName = flashSaleName; }

    public boolean isFlashSaleActive() { return isFlashSaleActive; }
    public void setFlashSaleActive(boolean flashSaleActive) { isFlashSaleActive = flashSaleActive; }

    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }

    public BigDecimal getSavingsPercentage() { return savingsPercentage; }
    public void setSavingsPercentage(BigDecimal savingsPercentage) { this.savingsPercentage = savingsPercentage; }

    public int getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(int remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean isSoldOut() { return isSoldOut; }
    public void setSoldOut(boolean soldOut) { isSoldOut = soldOut; }

    public boolean isAlmostSoldOut() { return isAlmostSoldOut; }
    public void setAlmostSoldOut(boolean almostSoldOut) { isAlmostSoldOut = almostSoldOut; }

    public double getAvailabilityPercentage() { return availabilityPercentage; }
    public void setAvailabilityPercentage(double availabilityPercentage) { this.availabilityPercentage = availabilityPercentage; }

    // Business methods
    public boolean hasValidPricing() {
        return originalPrice != null && flashSalePrice != null && 
               originalPrice.compareTo(flashSalePrice) > 0;
    }

    public BigDecimal getCalculatedSavingsAmount() {
        if (originalPrice != null && flashSalePrice != null) {
            return originalPrice.subtract(flashSalePrice);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getCalculatedSavingsPercentage() {
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getCalculatedSavingsAmount()
                    .divide(originalPrice, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedOriginalPrice() {
        return originalPrice != null ? String.format("%.2f", originalPrice) : "0.00";
    }

    public String getFormattedFlashSalePrice() {
        return flashSalePrice != null ? String.format("%.2f", flashSalePrice) : "0.00";
    }

    public String getFormattedSavingsAmount() {
        return savingsAmount != null ? String.format("%.2f", savingsAmount) : "0.00";
    }

    public String getFormattedSavingsPercentage() {
        return savingsPercentage != null ? String.format("%.1f%%", savingsPercentage) : "0.0%";
    }

    public String getDisplayName() {
        return productName != null ? productName : "Unknown Product";
    }

    public String getShortDescription() {
        if (productDescription != null && productDescription.length() > 100) {
            return productDescription.substring(0, 97) + "...";
        }
        return productDescription;
    }

    public String getProductUrl() {
        if (productSlug != null) {
            return "/product/" + productSlug;
        }
        return "/product/" + productId;
    }

    public String getFlashSaleUrl() {
        return "/flash-sale/" + flashSaleId;
    }

    public boolean hasImage() {
        return productImageUrl != null && !productImageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? productImageUrl : "/images/default-product.jpg";
    }

    public boolean hasQuantityLimit() {
        return quantityLimit != null && quantityLimit > 0;
    }

    public int getCalculatedRemainingQuantity() {
        if (!hasQuantityLimit()) return Integer.MAX_VALUE;
        return Math.max(0, quantityLimit - soldQuantity);
    }

    public boolean isCalculatedAvailable() {
        return isFlashSaleActive && hasQuantityLimit() ? getCalculatedRemainingQuantity() > 0 : true;
    }

    public boolean isCalculatedSoldOut() {
        return hasQuantityLimit() && getCalculatedRemainingQuantity() <= 0;
    }

    public double getCalculatedAvailabilityPercentage() {
        if (!hasQuantityLimit()) return 100.0;
        if (quantityLimit == 0) return 0.0;
        return (double) getCalculatedRemainingQuantity() / quantityLimit * 100;
    }

    public boolean isCalculatedAlmostSoldOut() {
        return getCalculatedAvailabilityPercentage() <= 10.0 && !isCalculatedSoldOut();
    }

    public String getAvailabilityStatus() {
        if (!isFlashSaleActive) return "Flash Sale Inactive";
        if (!isProductActive) return "Product Discontinued";
        if (isCalculatedSoldOut()) return "Sold Out";
        if (isCalculatedAlmostSoldOut()) return "Almost Sold Out";
        if (isAvailable) return "Available";
        return "Unavailable";
    }

    public String getAvailabilityColor() {
        if (!isFlashSaleActive || !isProductActive) return "gray";
        if (isCalculatedSoldOut()) return "red";
        if (isCalculatedAlmostSoldOut()) return "orange";
        if (isAvailable) return "green";
        return "orange";
    }

    public String getAvailabilityBadge() {
        if (!isFlashSaleActive) return "🚫 Inactive";
        if (!isProductActive) return "🚫 Discontinued";
        if (isCalculatedSoldOut()) return "❌ Sold Out";
        if (isCalculatedAlmostSoldOut()) return "🔥 Almost Sold Out";
        if (isAvailable) return "✅ Available";
        return "⚠️ Unavailable";
    }

    public String getQuantityDisplay() {
        if (!hasQuantityLimit()) return "Unlimited";
        return getCalculatedRemainingQuantity() + " / " + quantityLimit + " left";
    }

    public String getSalesProgressDisplay() {
        if (!hasQuantityLimit()) return "No limit";
        return soldQuantity + " / " + quantityLimit + " sold";
    }

    public String getFormattedSalesProgress() {
        if (!hasQuantityLimit()) return "No limit";
        return String.format("%.1f%% sold", getCalculatedAvailabilityPercentage());
    }

    public boolean hasSignificantSavings() {
        return savingsPercentage != null && savingsPercentage.compareTo(new BigDecimal("30")) >= 0;
    }

    public boolean hasModerateSavings() {
        return savingsPercentage != null && savingsPercentage.compareTo(new BigDecimal("15")) >= 0;
    }

    public String getSavingsBadge() {
        if (hasSignificantSavings()) return "🔥 Best Deal";
        if (hasModerateSavings()) return "💰 Good Deal";
        return "";
    }

    public String getSavingsColor() {
        if (hasSignificantSavings()) return "red";
        if (hasModerateSavings()) return "green";
        return "blue";
    }

    public boolean canBePurchased() {
        return isFlashSaleActive && isAvailable && hasValidPricing();
    }

    public boolean canBePurchasedInQuantity(int requestedQuantity) {
        return canBePurchased() && 
               (!hasQuantityLimit() || getCalculatedRemainingQuantity() >= requestedQuantity);
    }

    public String getCategoryPath() {
        if (categoryName == null && subcategoryName == null) return "Unknown Category";
        if (subcategoryName == null) return categoryName;
        return categoryName + " > " + subcategoryName;
    }

    public String getItemSummary() {
        return getDisplayName() + " - " + getFormattedFlashSalePrice() + 
               " (Save " + getFormattedSavingsAmount() + ")";
    }

    public String getFlashSaleContext() {
        return "Part of: " + (flashSaleName != null ? flashSaleName : "Unknown Flash Sale");
    }

    public boolean isHeavyItem() {
        return weight != null && weight.compareTo(new BigDecimal("5.0")) > 0;
    }

    public String getFormattedWeight() {
        return weight != null ? weight.toString() + " kg" : "N/A";
    }

    public String getFlashSaleItemBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (hasSignificantSavings()) {
            badges.append(" ").append(getSavingsBadge());
        }
        
        if (isCalculatedAlmostSoldOut()) {
            badges.append(" 🔥");
        }
        
        if (isCalculatedSoldOut()) {
            badges.append(" ❌");
        }
        
        return badges.toString().trim();
    }

    public String getFlashSaleType() {
        return "Flash Sale Item";
    }

    public boolean isHighDemand() {
        return getCalculatedAvailabilityPercentage() <= 20.0 && !isCalculatedSoldOut();
    }

    public String getDemandBadge() {
        if (isCalculatedSoldOut()) return "❌ Sold Out";
        if (isHighDemand()) return "⚡ High Demand";
        return "";
    }

    public String getDemandColor() {
        if (isCalculatedSoldOut()) return "red";
        if (isHighDemand()) return "orange";
        return "green";
    }
}
