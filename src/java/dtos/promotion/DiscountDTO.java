package dtos.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for discount information
 * Used for displaying discount codes and their details
 */
public class DiscountDTO {
    private int discountId;
    private String code;
    private String description;
    private String discountType;
    private String discountTypeDisplayName;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer usageLimit;
    private int usedCount;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Calculated values
    private boolean isCurrentlyActive;
    private boolean isExpired;
    private boolean isNotStarted;
    private boolean isExpiringSoon;
    private boolean isUsageLimitReached;
    private boolean canBeUsed;
    private int remainingUsage;

    // Constructors
    public DiscountDTO() {}

    public DiscountDTO(int discountId, String code, String discountType, BigDecimal discountValue) {
        this.discountId = discountId;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.isActive = true;
    }

    // Getters and Setters
    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public String getDiscountTypeDisplayName() { return discountTypeDisplayName; }
    public void setDiscountTypeDisplayName(String discountTypeDisplayName) { this.discountTypeDisplayName = discountTypeDisplayName; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public BigDecimal getMaxDiscountAmount() { return maxDiscountAmount; }
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }

    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isCurrentlyActive() { return isCurrentlyActive; }
    public void setCurrentlyActive(boolean currentlyActive) { isCurrentlyActive = currentlyActive; }

    public boolean isExpired() { return isExpired; }
    public void setExpired(boolean expired) { isExpired = expired; }

    public boolean isNotStarted() { return isNotStarted; }
    public void setNotStarted(boolean notStarted) { isNotStarted = notStarted; }

    public boolean isExpiringSoon() { return isExpiringSoon; }
    public void setExpiringSoon(boolean expiringSoon) { isExpiringSoon = expiringSoon; }

    public boolean isUsageLimitReached() { return isUsageLimitReached; }
    public void setUsageLimitReached(boolean usageLimitReached) { isUsageLimitReached = usageLimitReached; }

    public boolean canBeUsed() { return canBeUsed; }
    public void setCanBeUsed(boolean canBeUsed) { this.canBeUsed = canBeUsed; }

    public int getRemainingUsage() { return remainingUsage; }
    public void setRemainingUsage(int remainingUsage) { this.remainingUsage = remainingUsage; }

    // Business methods
    public boolean isPercentageDiscount() {
        return "percentage".equalsIgnoreCase(discountType);
    }

    public boolean isFixedAmountDiscount() {
        return "fixed_amount".equalsIgnoreCase(discountType);
    }

    public String getFormattedDiscountValue() {
        if (discountValue == null) return "0.00";
        if (isPercentageDiscount()) {
            return String.format("%.0f%%", discountValue);
        }
        return String.format("%.2f", discountValue);
    }

    public String getFormattedMinOrderAmount() {
        return minOrderAmount != null ? String.format("%.2f", minOrderAmount) : "No minimum";
    }

    public String getFormattedMaxDiscountAmount() {
        return maxDiscountAmount != null ? String.format("%.2f", maxDiscountAmount) : "No limit";
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.toLocalDate().toString() : "Not set";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.toLocalDate().toString() : "No expiry";
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getDisplayCode() {
        return code != null ? code.toUpperCase() : "UNKNOWN";
    }

    public String getDiscountTypeDisplayNameOrDefault() {
        if (discountTypeDisplayName != null && !discountTypeDisplayName.trim().isEmpty()) {
            return discountTypeDisplayName;
        }
        if (isPercentageDiscount()) return "Percentage Discount";
        if (isFixedAmountDiscount()) return "Fixed Amount Discount";
        return "Discount";
    }

    public String getAvailabilityStatus() {
        if (!isActive) return "Inactive";
        if (isExpired) return "Expired";
        if (isNotStarted) return "Not Started";
        if (isUsageLimitReached) return "Limit Reached";
        if (isExpiringSoon) return "Expiring Soon";
        if (isCurrentlyActive) return "Active";
        return "Unavailable";
    }

    public String getAvailabilityColor() {
        if (!isActive || isExpired || isUsageLimitReached) return "red";
        if (isNotStarted) return "gray";
        if (isExpiringSoon) return "orange";
        if (isCurrentlyActive) return "green";
        return "orange";
    }

    public String getAvailabilityBadge() {
        if (!isActive) return "🚫 Inactive";
        if (isExpired) return "⏰ Expired";
        if (isNotStarted) return "⏳ Not Started";
        if (isUsageLimitReached) return "🔒 Limit Reached";
        if (isExpiringSoon) return "⏰ Expiring Soon";
        if (isCurrentlyActive) return "✅ Active";
        return "⚠️ Unavailable";
    }

    public String getDiscountTypeIcon() {
        if (isPercentageDiscount()) return "📊";
        if (isFixedAmountDiscount()) return "💰";
        return "🎫";
    }

    public boolean hasMinimumOrderAmount() {
        return minOrderAmount != null && minOrderAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasMaximumDiscountAmount() {
        return maxDiscountAmount != null && maxDiscountAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasUsageLimit() {
        return usageLimit != null && usageLimit > 0;
    }

    public String getUsageDisplay() {
        if (!hasUsageLimit()) return "Unlimited";
        return usedCount + " / " + usageLimit + " used";
    }

    public String getRemainingUsageDisplay() {
        if (!hasUsageLimit()) return "Unlimited";
        if (remainingUsage <= 0) return "None left";
        return remainingUsage + " left";
    }

    public String getDurationDisplay() {
        if (startDate != null && endDate != null) {
            return startDate.toLocalDate().toString() + " to " + endDate.toLocalDate().toString();
        }
        if (endDate != null) {
            return "Until " + endDate.toLocalDate().toString();
        }
        return "No expiry";
    }

    public String getDiscountSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getDiscountTypeIcon()).append(" ");
        summary.append(getDisplayCode()).append(" - ");
        summary.append(getFormattedDiscountValue());
        
        if (hasMinimumOrderAmount()) {
            summary.append(" (Min: ").append(getFormattedMinOrderAmount()).append(")");
        }
        
        return summary.toString();
    }

    public String getDiscountDescription() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }
        
        StringBuilder desc = new StringBuilder();
        if (isPercentageDiscount()) {
            desc.append("Get ").append(getFormattedDiscountValue()).append(" off");
        } else {
            desc.append("Save ").append(getFormattedDiscountValue());
        }
        
        if (hasMinimumOrderAmount()) {
            desc.append(" on orders over ").append(getFormattedMinOrderAmount());
        }
        
        return desc.toString();
    }

    public boolean isHighValueDiscount() {
        if (isPercentageDiscount()) {
            return discountValue.compareTo(new BigDecimal("20")) >= 0;
        }
        return discountValue.compareTo(new BigDecimal("50")) >= 0;
    }

    public String getValueBadge() {
        if (isHighValueDiscount()) return "🔥 High Value";
        return "💰 Standard";
    }

    public String getValueColor() {
        if (isHighValueDiscount()) return "red";
        return "green";
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getRemainingTimeDisplay() {
        if (endDate == null) return "No expiry";
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)) return "Expired";
        
        long days = java.time.Duration.between(now, endDate).toDays();
        if (days > 0) return days + " day" + (days == 1 ? "" : "s") + " left";
        
        long hours = java.time.Duration.between(now, endDate).toHours();
        if (hours > 0) return hours + " hour" + (hours == 1 ? "" : "s") + " left";
        
        long minutes = java.time.Duration.between(now, endDate).toMinutes();
        return minutes + " minute" + (minutes == 1 ? "" : "s") + " left";
    }

    public String getDiscountBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (isHighValueDiscount()) {
            badges.append(" ").append(getValueBadge());
        }
        
        if (isExpiringSoon) {
            badges.append(" ⏰");
        }
        
        if (isRecentlyCreated()) {
            badges.append(" 🆕");
        }
        
        return badges.toString().trim();
    }
}
