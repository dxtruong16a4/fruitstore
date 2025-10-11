package dtos.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for discount usage tracking
 * Used for displaying discount usage history and statistics
 */
public class DiscountUsageDTO {
    private int usageId;
    private int discountId;
    private int userId;
    private int orderId;
    private BigDecimal discountAmount;
    private LocalDateTime usedAt;
    
    // Discount information (flattened)
    private String discountCode;
    private String discountDescription;
    private String discountType;
    private BigDecimal discountValue;
    
    // User information (flattened)
    private String username;
    private String userFullName;
    private String userEmail;
    
    // Order information (flattened)
    private String orderNumber;
    private BigDecimal orderTotalAmount;
    private String orderStatus;

    // Constructors
    public DiscountUsageDTO() {}

    public DiscountUsageDTO(int usageId, int discountId, int userId, int orderId, 
                           BigDecimal discountAmount, LocalDateTime usedAt) {
        this.usageId = usageId;
        this.discountId = discountId;
        this.userId = userId;
        this.orderId = orderId;
        this.discountAmount = discountAmount;
        this.usedAt = usedAt;
    }

    // Getters and Setters
    public int getUsageId() { return usageId; }
    public void setUsageId(int usageId) { this.usageId = usageId; }

    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    public String getDiscountDescription() { return discountDescription; }
    public void setDiscountDescription(String discountDescription) { this.discountDescription = discountDescription; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public BigDecimal getOrderTotalAmount() { return orderTotalAmount; }
    public void setOrderTotalAmount(BigDecimal orderTotalAmount) { this.orderTotalAmount = orderTotalAmount; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    // Business methods
    public String getFormattedDiscountAmount() {
        return discountAmount != null ? String.format("%.2f", discountAmount) : "0.00";
    }

    public String getFormattedOrderTotalAmount() {
        return orderTotalAmount != null ? String.format("%.2f", orderTotalAmount) : "0.00";
    }

    public String getFormattedUsedAt() {
        return usedAt != null ? usedAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedUsedDateTime() {
        return usedAt != null ? usedAt.toString() : "Unknown";
    }

    public boolean isRecentUsage() {
        if (usedAt == null) return false;
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return usedAt.isAfter(oneHourAgo);
    }

    public boolean isTodayUsage() {
        if (usedAt == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return usedAt.isAfter(today) && usedAt.isBefore(tomorrow);
    }

    public boolean isThisWeekUsage() {
        if (usedAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return usedAt.isAfter(oneWeekAgo);
    }

    public boolean isThisMonthUsage() {
        if (usedAt == null) return false;
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return usedAt.isAfter(oneMonthAgo);
    }

    public String getTimeAgo() {
        if (usedAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(usedAt, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        
        long days = hours / 24;
        if (days < 7) return days + " day" + (days == 1 ? "" : "s") + " ago";
        
        if (days < 30) return (days / 7) + " week" + (days / 7 == 1 ? "" : "s") + " ago";
        if (days < 365) return (days / 30) + " month" + (days / 30 == 1 ? "" : "s") + " ago";
        
        return (days / 365) + " year" + (days / 365 == 1 ? "" : "s") + " ago";
    }

    public String getDisplayDiscountCode() {
        return discountCode != null ? discountCode.toUpperCase() : "UNKNOWN";
    }

    public String getDisplayUserName() {
        if (userFullName != null && !userFullName.trim().isEmpty()) {
            return userFullName;
        }
        if (username != null && !username.trim().isEmpty()) {
            return username;
        }
        return "Unknown User";
    }

    public String getDiscountTypeDisplayName() {
        if ("percentage".equalsIgnoreCase(discountType)) return "Percentage Discount";
        if ("fixed_amount".equalsIgnoreCase(discountType)) return "Fixed Amount Discount";
        return "Discount";
    }

    public String getDiscountTypeIcon() {
        if ("percentage".equalsIgnoreCase(discountType)) return "📊";
        if ("fixed_amount".equalsIgnoreCase(discountType)) return "💰";
        return "🎫";
    }

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

    public String getUsageSummary() {
        return getDisplayDiscountCode() + " used for " + getFormattedDiscountAmount() + " off";
    }

    public String getDetailedUsageSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getDiscountTypeIcon()).append(" ");
        summary.append(getDisplayDiscountCode()).append(" - ");
        summary.append(getFormattedDiscountAmount()).append(" off ");
        summary.append(getFormattedOrderTotalAmount()).append(" order");
        return summary.toString();
    }

    public boolean isHighValueUsage() {
        return discountAmount != null && discountAmount.compareTo(new BigDecimal("50")) >= 0;
    }

    public boolean isLowValueUsage() {
        return discountAmount != null && discountAmount.compareTo(new BigDecimal("10")) < 0;
    }

    public String getValueBadge() {
        if (isHighValueUsage()) return "🔥 High Value";
        if (isLowValueUsage()) return "💰 Small Savings";
        return "💵 Standard";
    }

    public String getValueColor() {
        if (isHighValueUsage()) return "red";
        if (isLowValueUsage()) return "blue";
        return "green";
    }

    public String getOrderUrl() {
        return orderNumber != null ? "/order/" + orderNumber : "/order/" + orderId;
    }

    public String getDiscountUrl() {
        return "/discount/" + discountId;
    }

    public String getUsagePeriod() {
        if (isRecentUsage()) return "Recent";
        if (isTodayUsage()) return "Today";
        if (isThisWeekUsage()) return "This Week";
        if (isThisMonthUsage()) return "This Month";
        return "Older";
    }

    public String getUsagePeriodColor() {
        if (isRecentUsage()) return "green";
        if (isTodayUsage()) return "blue";
        if (isThisWeekUsage()) return "orange";
        if (isThisMonthUsage()) return "purple";
        return "gray";
    }

    public boolean isSuccessfulUsage() {
        return orderStatus != null && ("delivered".equalsIgnoreCase(orderStatus) || 
                                      "completed".equalsIgnoreCase(orderStatus));
    }

    public boolean isPendingUsage() {
        return orderStatus != null && ("pending".equalsIgnoreCase(orderStatus) || 
                                      "processing".equalsIgnoreCase(orderStatus));
    }

    public boolean isFailedUsage() {
        return orderStatus != null && ("cancelled".equalsIgnoreCase(orderStatus) || 
                                      "failed".equalsIgnoreCase(orderStatus));
    }

    public String getUsageStatus() {
        if (isSuccessfulUsage()) return "Successful";
        if (isPendingUsage()) return "Pending";
        if (isFailedUsage()) return "Failed";
        return "Unknown";
    }

    public String getUsageStatusColor() {
        if (isSuccessfulUsage()) return "green";
        if (isPendingUsage()) return "orange";
        if (isFailedUsage()) return "red";
        return "gray";
    }

    public String getUsageStatusIcon() {
        if (isSuccessfulUsage()) return "✅";
        if (isPendingUsage()) return "⏳";
        if (isFailedUsage()) return "❌";
        return "❓";
    }

    public String getUsageBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (isHighValueUsage()) {
            badges.append(" ").append(getValueBadge());
        }
        
        if (isRecentUsage()) {
            badges.append(" 🆕");
        }
        
        badges.append(" ").append(getUsageStatusIcon());
        
        return badges.toString().trim();
    }
}
