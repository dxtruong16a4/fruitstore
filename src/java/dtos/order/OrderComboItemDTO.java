package dtos.order;

import java.math.BigDecimal;

/**
 * DTO for order combo item details
 * Used for displaying combo items in orders with combo information
 */
public class OrderComboItemDTO {
    private int orderComboItemId;
    private int orderId;
    private int comboId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    
    // Combo information (flattened for display)
    private String comboName;
    private String comboDescription;
    private String comboImageUrl;
    private String comboSlug;
    private BigDecimal originalPrice; // Original combo price
    private BigDecimal currentPrice; // Current combo price (may differ from order price)
    private boolean isComboActive;
    private boolean isComboAvailable;
    private BigDecimal savingsAmount;
    private int savingsPercentage;

    // Constructors
    public OrderComboItemDTO() {}

    public OrderComboItemDTO(int orderComboItemId, int orderId, int comboId, int quantity, BigDecimal price) {
        this.orderComboItemId = orderComboItemId;
        this.orderId = orderId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price.multiply(new BigDecimal(quantity));
    }

    // Getters and Setters
    public int getOrderComboItemId() { return orderComboItemId; }
    public void setOrderComboItemId(int orderComboItemId) { this.orderComboItemId = orderComboItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getComboName() { return comboName; }
    public void setComboName(String comboName) { this.comboName = comboName; }

    public String getComboDescription() { return comboDescription; }
    public void setComboDescription(String comboDescription) { this.comboDescription = comboDescription; }

    public String getComboImageUrl() { return comboImageUrl; }
    public void setComboImageUrl(String comboImageUrl) { this.comboImageUrl = comboImageUrl; }

    public String getComboSlug() { return comboSlug; }
    public void setComboSlug(String comboSlug) { this.comboSlug = comboSlug; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public boolean isComboActive() { return isComboActive; }
    public void setComboActive(boolean comboActive) { isComboActive = comboActive; }

    public boolean isComboAvailable() { return isComboAvailable; }
    public void setComboAvailable(boolean comboAvailable) { isComboAvailable = comboAvailable; }

    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }

    public int getSavingsPercentage() { return savingsPercentage; }
    public void setSavingsPercentage(int savingsPercentage) { this.savingsPercentage = savingsPercentage; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isValidPrice() {
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public BigDecimal getCalculatedTotalPrice() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(new BigDecimal(quantity));
    }

    public String getFormattedPrice() {
        return price != null ? String.format("%.2f", price) : "0.00";
    }

    public String getFormattedTotalPrice() {
        return totalPrice != null ? String.format("%.2f", totalPrice) : "0.00";
    }

    public String getFormattedOriginalPrice() {
        return originalPrice != null ? String.format("%.2f", originalPrice) : "0.00";
    }

    public String getFormattedCurrentPrice() {
        return currentPrice != null ? String.format("%.2f", currentPrice) : "0.00";
    }

    public String getFormattedSavingsAmount() {
        return savingsAmount != null ? String.format("%.2f", savingsAmount) : "0.00";
    }

    public String getDisplayName() {
        return comboName != null ? comboName : "Unknown Combo";
    }

    public String getShortDescription() {
        if (comboDescription != null && comboDescription.length() > 100) {
            return comboDescription.substring(0, 97) + "...";
        }
        return comboDescription;
    }

    public String getComboUrl() {
        if (comboSlug != null) {
            return "/combo/" + comboSlug;
        }
        return "/combo/" + comboId;
    }

    public boolean hasComboChanged() {
        return currentPrice != null && price != null && !currentPrice.equals(price);
    }

    public BigDecimal getPriceDifference() {
        if (hasComboChanged()) {
            return currentPrice.subtract(price);
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedPriceDifference() {
        return String.format("%.2f", getPriceDifference());
    }

    public boolean isPriceIncreased() {
        return getPriceDifference().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isPriceDecreased() {
        return getPriceDifference().compareTo(BigDecimal.ZERO) < 0;
    }

    public String getPriceChangeIndicator() {
        if (isPriceIncreased()) return "↗️";
        if (isPriceDecreased()) return "↘️";
        return "";
    }

    public boolean isComboStillAvailable() {
        return isComboActive && isComboAvailable;
    }

    public String getAvailabilityStatus() {
        if (!isComboActive) return "Discontinued";
        if (!isComboAvailable) return "Unavailable";
        return "Available";
    }

    public String getAvailabilityColor() {
        if (!isComboActive) return "red";
        if (!isComboAvailable) return "orange";
        return "green";
    }

    public boolean canReorder() {
        return isComboStillAvailable();
    }

    public boolean hasImage() {
        return comboImageUrl != null && !comboImageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? comboImageUrl : "/images/default-combo.jpg";
    }

    public boolean isHighQuantity() {
        return quantity >= 5;
    }

    public boolean isLowQuantity() {
        return quantity == 1;
    }

    public String getQuantityDisplay() {
        if (quantity == 1) return "1 combo";
        return quantity + " combos";
    }

    public boolean hasSavings() {
        return savingsAmount != null && savingsAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasSignificantSavings() {
        return savingsPercentage >= 20;
    }

    public String getSavingsDisplay() {
        if (hasSavings()) {
            return "Save " + getFormattedSavingsAmount() + " (" + savingsPercentage + "%)";
        }
        return "No savings";
    }

    public String getSavingsBadge() {
        if (hasSignificantSavings()) return "🔥 Great Deal";
        if (hasSavings()) return "💰 Save";
        return "";
    }

    public String getSavingsColor() {
        if (hasSignificantSavings()) return "red";
        if (hasSavings()) return "green";
        return "gray";
    }

    public BigDecimal getTotalSavings() {
        if (hasSavings()) {
            return savingsAmount.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedTotalSavings() {
        return String.format("%.2f", getTotalSavings());
    }

    public BigDecimal getSavingsFromCurrentPrice() {
        if (hasComboChanged() && isPriceDecreased()) {
            return getPriceDifference().abs().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedSavingsFromCurrentPrice() {
        return String.format("%.2f", getSavingsFromCurrentPrice());
    }

    public boolean hasAdditionalSavings() {
        return getSavingsFromCurrentPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public String getComboSummary() {
        return getQuantityDisplay() + " of " + getDisplayName() + " - " + getFormattedTotalPrice();
    }

    public boolean isEligibleForReview() {
        return comboId > 0 && quantity > 0;
    }

    public String getComboBadge() {
        if (hasSignificantSavings()) return "🔥 Best Value";
        if (hasSavings()) return "💰 Good Deal";
        return "";
    }

    public boolean isPopularCombo() {
        return savingsPercentage >= 30;
    }

    public String getComboType() {
        return "Combo Deal";
    }
}
