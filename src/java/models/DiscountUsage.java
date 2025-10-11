package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DiscountUsage {
    private int usageId;
    private int discountId;
    private int userId;
    private int orderId;
    private BigDecimal discountAmount;
    private LocalDateTime usedAt;
    private Discount discount; // For joined queries
    private User user; // For joined queries
    private Order order; // For joined queries

    // Constructors
    public DiscountUsage() {}

    public DiscountUsage(int discountId, int userId, int orderId, BigDecimal discountAmount) {
        this.discountId = discountId;
        this.userId = userId;
        this.orderId = orderId;
        this.discountAmount = discountAmount;
        this.usedAt = LocalDateTime.now();
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

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    // Business methods
    public boolean isRecentUsage() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return usedAt.isAfter(oneHourAgo);
    }

    public boolean isTodayUsage() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        return usedAt.isAfter(today);
    }

    public String getUsageDateFormatted() {
        return usedAt.toLocalDate().toString();
    }

    public String getUsageTimeFormatted() {
        return usedAt.toLocalTime().toString();
    }
}
