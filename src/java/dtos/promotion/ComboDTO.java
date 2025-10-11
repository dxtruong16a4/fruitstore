package dtos.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for combo information
 * Used for displaying combo deals with items and pricing
 */
public class ComboDTO {
    private int comboId;
    private String name;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal comboPrice;
    private String imageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Calculated values
    private BigDecimal savingsAmount;
    private BigDecimal savingsPercentage;
    private boolean isCurrentlyActive;
    private boolean isExpired;
    private boolean isNotStarted;
    private boolean isExpiringSoon;
    
    // Combo items
    private List<ComboItemDTO> comboItems;
    private int totalItems;
    private int totalQuantity;

    // Constructors
    public ComboDTO() {}

    public ComboDTO(int comboId, String name, BigDecimal originalPrice, BigDecimal comboPrice) {
        this.comboId = comboId;
        this.name = name;
        this.originalPrice = originalPrice;
        this.comboPrice = comboPrice;
        this.isActive = true;
    }

    // Getters and Setters
    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getComboPrice() { return comboPrice; }
    public void setComboPrice(BigDecimal comboPrice) { this.comboPrice = comboPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }

    public BigDecimal getSavingsPercentage() { return savingsPercentage; }
    public void setSavingsPercentage(BigDecimal savingsPercentage) { this.savingsPercentage = savingsPercentage; }

    public boolean isCurrentlyActive() { return isCurrentlyActive; }
    public void setCurrentlyActive(boolean currentlyActive) { isCurrentlyActive = currentlyActive; }

    public boolean isExpired() { return isExpired; }
    public void setExpired(boolean expired) { isExpired = expired; }

    public boolean isNotStarted() { return isNotStarted; }
    public void setNotStarted(boolean notStarted) { isNotStarted = notStarted; }

    public boolean isExpiringSoon() { return isExpiringSoon; }
    public void setExpiringSoon(boolean expiringSoon) { isExpiringSoon = expiringSoon; }

    public List<ComboItemDTO> getComboItems() { return comboItems; }
    public void setComboItems(List<ComboItemDTO> comboItems) { this.comboItems = comboItems; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    // Business methods
    public boolean hasValidPricing() {
        return originalPrice != null && comboPrice != null && 
               originalPrice.compareTo(comboPrice) > 0;
    }

    public BigDecimal getCalculatedSavingsAmount() {
        if (originalPrice != null && comboPrice != null) {
            return originalPrice.subtract(comboPrice);
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

    public String getFormattedComboPrice() {
        return comboPrice != null ? String.format("%.2f", comboPrice) : "0.00";
    }

    public String getFormattedSavingsAmount() {
        return savingsAmount != null ? String.format("%.2f", savingsAmount) : "0.00";
    }

    public String getFormattedSavingsPercentage() {
        return savingsPercentage != null ? String.format("%.1f%%", savingsPercentage) : "0.0%";
    }

    public String getDisplayName() {
        return name != null ? name : "Unknown Combo";
    }

    public String getShortDescription() {
        if (description != null && description.length() > 150) {
            return description.substring(0, 147) + "...";
        }
        return description;
    }

    public String getComboUrl() {
        return "/combo/" + comboId;
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? imageUrl : "/images/default-combo.jpg";
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
        return "gray";
    }

    public String getAvailabilityStatus() {
        if (!isActive) return "Discontinued";
        if (isExpired) return "Expired";
        if (isNotStarted) return "Not Started";
        if (isExpiringSoon) return "Expiring Soon";
        if (isCurrentlyActive) return "Available";
        return "Unavailable";
    }

    public String getAvailabilityColor() {
        if (!isActive || isExpired) return "red";
        if (isNotStarted) return "gray";
        if (isExpiringSoon) return "orange";
        if (isCurrentlyActive) return "green";
        return "orange";
    }

    public String getAvailabilityBadge() {
        if (!isActive) return "🚫 Discontinued";
        if (isExpired) return "⏰ Expired";
        if (isNotStarted) return "⏳ Not Started";
        if (isExpiringSoon) return "⏰ Expiring Soon";
        if (isCurrentlyActive) return "✅ Available";
        return "⚠️ Unavailable";
    }

    public boolean canBePurchased() {
        return isCurrentlyActive && hasValidPricing();
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.toLocalDate().toString() : "Not set";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.toLocalDate().toString() : "Not set";
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getDurationDisplay() {
        if (startDate != null && endDate != null) {
            return startDate.toLocalDate().toString() + " to " + endDate.toLocalDate().toString();
        }
        if (endDate != null) {
            return "Until " + endDate.toLocalDate().toString();
        }
        return "Ongoing";
    }

    public boolean hasComboItems() {
        return comboItems != null && !comboItems.isEmpty();
    }

    public String getItemsSummary() {
        if (totalItems > 0) {
            return totalItems + " item" + (totalItems > 1 ? "s" : "") + 
                   " (" + totalQuantity + " total)";
        }
        return "No items";
    }

    public String getComboSummary() {
        return getDisplayName() + " - " + getFormattedComboPrice() + 
               " (Save " + getFormattedSavingsAmount() + ")";
    }

    public boolean isPopularCombo() {
        return hasSignificantSavings() && isCurrentlyActive;
    }

    public String getComboType() {
        return "Combo Deal";
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getRemainingTimeDisplay() {
        if (endDate == null) return "No time limit";
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)) return "Expired";
        
        long days = java.time.Duration.between(now, endDate).toDays();
        if (days > 0) return days + " day" + (days == 1 ? "" : "s") + " left";
        
        long hours = java.time.Duration.between(now, endDate).toHours();
        if (hours > 0) return hours + " hour" + (hours == 1 ? "" : "s") + " left";
        
        long minutes = java.time.Duration.between(now, endDate).toMinutes();
        return minutes + " minute" + (minutes == 1 ? "" : "s") + " left";
    }

    public String getComboBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (hasSavings()) {
            badges.append(" ").append(getSavingsBadge());
        }
        
        if (isExpiringSoon) {
            badges.append(" ⏰");
        }
        
        if (isRecentlyCreated()) {
            badges.append(" 🆕");
        }
        
        return badges.toString().trim();
    }

    public boolean hasSavings() {
        return savingsAmount != null && savingsAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}
