package dtos.order;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for cart combo data
 * Used for displaying cart combo items with combo information
 */
public class CartComboDTO {
    private int cartComboId;
    private int userId;
    private int comboId;
    private int quantity;
    private boolean selected;
    
    // Combo information (flattened for display)
    private String comboName;
    private String comboDescription;
    private String comboImageUrl;
    private String comboSlug;
    private BigDecimal comboPrice;
    private BigDecimal originalPrice;
    private BigDecimal savingsAmount;
    private int savingsPercentage;
    private boolean isComboActive;
    private boolean isComboAvailable;
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Calculated values
    private BigDecimal totalPrice;
    private BigDecimal totalSavings;
    private boolean isCurrentlyActive;

    // Constructors
    public CartComboDTO() {}

    public CartComboDTO(int cartComboId, int userId, int comboId, int quantity, boolean selected) {
        this.cartComboId = cartComboId;
        this.userId = userId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.selected = selected;
    }

    // Getters and Setters
    public int getCartComboId() { return cartComboId; }
    public void setCartComboId(int cartComboId) { this.cartComboId = cartComboId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public String getComboName() { return comboName; }
    public void setComboName(String comboName) { this.comboName = comboName; }

    public String getComboDescription() { return comboDescription; }
    public void setComboDescription(String comboDescription) { this.comboDescription = comboDescription; }

    public String getComboImageUrl() { return comboImageUrl; }
    public void setComboImageUrl(String comboImageUrl) { this.comboImageUrl = comboImageUrl; }

    public String getComboSlug() { return comboSlug; }
    public void setComboSlug(String comboSlug) { this.comboSlug = comboSlug; }

    public BigDecimal getComboPrice() { return comboPrice; }
    public void setComboPrice(BigDecimal comboPrice) { this.comboPrice = comboPrice; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }

    public int getSavingsPercentage() { return savingsPercentage; }
    public void setSavingsPercentage(int savingsPercentage) { this.savingsPercentage = savingsPercentage; }

    public boolean isComboActive() { return isComboActive; }
    public void setComboActive(boolean comboActive) { isComboActive = comboActive; }

    public boolean isComboAvailable() { return isComboAvailable; }
    public void setComboAvailable(boolean comboAvailable) { isComboAvailable = comboAvailable; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }

    public boolean isCurrentlyActive() { return isCurrentlyActive; }
    public void setCurrentlyActive(boolean currentlyActive) { isCurrentlyActive = currentlyActive; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isComboStillAvailable() {
        return isComboActive && isComboAvailable && isCurrentlyActive;
    }

    public boolean canBeAddedToOrder() {
        return isSelected() && isValidQuantity() && isComboStillAvailable();
    }

    public BigDecimal getCalculatedTotalPrice() {
        if (comboPrice == null) return BigDecimal.ZERO;
        return comboPrice.multiply(new BigDecimal(quantity));
    }

    public BigDecimal getCalculatedTotalSavings() {
        if (savingsAmount == null) return BigDecimal.ZERO;
        return savingsAmount.multiply(new BigDecimal(quantity));
    }

    public String getFormattedComboPrice() {
        return comboPrice != null ? String.format("%.2f", comboPrice) : "0.00";
    }

    public String getFormattedOriginalPrice() {
        return originalPrice != null ? String.format("%.2f", originalPrice) : "0.00";
    }

    public String getFormattedTotalPrice() {
        return totalPrice != null ? String.format("%.2f", totalPrice) : "0.00";
    }

    public String getFormattedSavingsAmount() {
        return savingsAmount != null ? String.format("%.2f", savingsAmount) : "0.00";
    }

    public String getFormattedTotalSavings() {
        return totalSavings != null ? String.format("%.2f", totalSavings) : "0.00";
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

    public boolean hasImage() {
        return comboImageUrl != null && !comboImageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? comboImageUrl : "/images/default-combo.jpg";
    }

    public boolean hasSavings() {
        return savingsAmount != null && savingsAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasSignificantSavings() {
        return savingsPercentage >= 30;
    }

    public String getSavingsDisplay() {
        if (hasSavings()) {
            return savingsPercentage + "% OFF - Save " + getFormattedSavingsAmount();
        }
        return "";
    }

    public String getSavingsBadge() {
        if (hasSignificantSavings()) return "🔥 Best Deal";
        if (hasSavings()) return "💰 Save";
        return "";
    }

    public String getSavingsColor() {
        if (hasSignificantSavings()) return "red";
        if (hasSavings()) return "green";
        return "gray";
    }

    public boolean canIncreaseQuantity() {
        return isComboStillAvailable();
    }

    public boolean canDecreaseQuantity() {
        return quantity > 1;
    }

    public String getQuantityDisplay() {
        if (quantity == 1) return "1 combo";
        return quantity + " combos";
    }

    public boolean isExpired() {
        if (endDate == null) return false;
        return LocalDate.now().isAfter(endDate);
    }

    public boolean isNotStarted() {
        if (startDate == null) return false;
        return LocalDate.now().isBefore(startDate);
    }

    public boolean isExpiringSoon() {
        if (endDate == null) return false;
        LocalDate threeDaysFromNow = LocalDate.now().plusDays(3);
        return endDate.isBefore(threeDaysFromNow) && !isExpired();
    }

    public String getAvailabilityStatus() {
        if (!isComboActive) return "Discontinued";
        if (isExpired()) return "Expired";
        if (isNotStarted()) return "Not Started";
        if (isExpiringSoon()) return "Expiring Soon";
        if (!isComboAvailable) return "Unavailable";
        return "Available";
    }

    public String getAvailabilityColor() {
        if (!isComboActive || isExpired()) return "red";
        if (isNotStarted()) return "gray";
        if (isExpiringSoon()) return "orange";
        if (!isComboAvailable) return "orange";
        return "green";
    }

    public String getAvailabilityBadge() {
        if (!isComboActive) return "🚫 Discontinued";
        if (isExpired()) return "⏰ Expired";
        if (isNotStarted()) return "⏳ Not Started";
        if (isExpiringSoon()) return "⏰ Expiring Soon";
        if (!isComboAvailable) return "⚠️ Unavailable";
        return "✅ Available";
    }

    public String getCartComboSummary() {
        return getQuantityDisplay() + " of " + getDisplayName() + " - " + getFormattedTotalPrice();
    }

    public boolean hasTotalSavings() {
        return totalSavings != null && totalSavings.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getTotalSavingsDisplay() {
        if (hasTotalSavings()) {
            return "Total Savings: " + getFormattedTotalSavings();
        }
        return "";
    }

    public String getComboBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (hasSavings()) {
            badges.append(" ").append(getSavingsBadge());
        }
        
        if (isExpiringSoon()) {
            badges.append(" ⏰");
        }
        
        return badges.toString().trim();
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.toString() : "Not set";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.toString() : "Not set";
    }

    public String getDurationDisplay() {
        if (startDate != null && endDate != null) {
            return startDate.toString() + " to " + endDate.toString();
        }
        if (endDate != null) {
            return "Until " + endDate.toString();
        }
        return "Ongoing";
    }

    public boolean isRecentlyAdded() {
        // This would need to be tracked in the database
        // For now, we'll assume it's handled by the service layer
        return false;
    }

    public String getComboType() {
        return "Combo Deal";
    }

    public boolean isPopularCombo() {
        return savingsPercentage >= 40;
    }
}
