package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Combo {
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

    // Constructors
    public Combo() {}

    public Combo(String name, String description, BigDecimal originalPrice, 
                 BigDecimal comboPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.originalPrice = originalPrice;
        this.comboPrice = comboPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
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

    // Business methods
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && 
               (startDate == null || now.isAfter(startDate) || now.isEqual(startDate)) &&
               (endDate == null || now.isBefore(endDate) || now.isEqual(endDate));
    }

    public BigDecimal getSavingsAmount() {
        return originalPrice.subtract(comboPrice);
    }

    public BigDecimal getSavingsPercentage() {
        if (originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getSavingsAmount().divide(originalPrice, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
}