package dtos.promotion;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for flash sale information
 * Used for displaying flash sale events and their details
 */
public class FlashSaleDTO {
    private int flashSaleId;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Calculated values
    private boolean isCurrentlyActive;
    private boolean hasStarted;
    private boolean hasEnded;
    private long remainingMinutes;
    private long totalMinutes;
    private double progressPercentage;
    private boolean isExpiringSoon;
    
    // Flash sale items
    private List<FlashSaleItemDTO> flashSaleItems;
    private int totalItems;
    private int totalSoldItems;
    private int totalAvailableItems;

    // Constructors
    public FlashSaleDTO() {}

    public FlashSaleDTO(int flashSaleId, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.flashSaleId = flashSaleId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = true;
    }

    // Getters and Setters
    public int getFlashSaleId() { return flashSaleId; }
    public void setFlashSaleId(int flashSaleId) { this.flashSaleId = flashSaleId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isCurrentlyActive() { return isCurrentlyActive; }
    public void setCurrentlyActive(boolean currentlyActive) { isCurrentlyActive = currentlyActive; }

    public boolean isHasStarted() { return hasStarted; }
    public void setHasStarted(boolean hasStarted) { this.hasStarted = hasStarted; }

    public boolean isHasEnded() { return hasEnded; }
    public void setHasEnded(boolean hasEnded) { this.hasEnded = hasEnded; }

    public long getRemainingMinutes() { return remainingMinutes; }
    public void setRemainingMinutes(long remainingMinutes) { this.remainingMinutes = remainingMinutes; }

    public long getTotalMinutes() { return totalMinutes; }
    public void setTotalMinutes(long totalMinutes) { this.totalMinutes = totalMinutes; }

    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public boolean isExpiringSoon() { return isExpiringSoon; }
    public void setExpiringSoon(boolean expiringSoon) { isExpiringSoon = expiringSoon; }

    public List<FlashSaleItemDTO> getFlashSaleItems() { return flashSaleItems; }
    public void setFlashSaleItems(List<FlashSaleItemDTO> flashSaleItems) { this.flashSaleItems = flashSaleItems; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalSoldItems() { return totalSoldItems; }
    public void setTotalSoldItems(int totalSoldItems) { this.totalSoldItems = totalSoldItems; }

    public int getTotalAvailableItems() { return totalAvailableItems; }
    public void setTotalAvailableItems(int totalAvailableItems) { this.totalAvailableItems = totalAvailableItems; }

    // Business methods
    public String getDisplayName() {
        return name != null ? name : "Unknown Flash Sale";
    }

    public String getShortDescription() {
        if (description != null && description.length() > 150) {
            return description.substring(0, 147) + "...";
        }
        return description;
    }

    public String getFlashSaleUrl() {
        return "/flash-sale/" + flashSaleId;
    }

    public String getFormattedStartTime() {
        return startTime != null ? startTime.toLocalDate().toString() : "Not set";
    }

    public String getFormattedEndTime() {
        return endTime != null ? endTime.toLocalDate().toString() : "Not set";
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "Unknown";
    }

    public String getFormattedStartDateTime() {
        return startTime != null ? startTime.toString() : "Not set";
    }

    public String getFormattedEndDateTime() {
        return endTime != null ? endTime.toString() : "Not set";
    }

    public String getAvailabilityStatus() {
        if (!isActive) return "Inactive";
        if (hasEnded) return "Ended";
        if (!hasStarted) return "Not Started";
        if (isCurrentlyActive) return "Live Now";
        if (isExpiringSoon) return "Ending Soon";
        return "Unknown";
    }

    public String getAvailabilityColor() {
        if (!isActive || hasEnded) return "red";
        if (!hasStarted) return "gray";
        if (isCurrentlyActive) return "green";
        if (isExpiringSoon) return "orange";
        return "blue";
    }

    public String getAvailabilityBadge() {
        if (!isActive) return "🚫 Inactive";
        if (hasEnded) return "⏰ Ended";
        if (!hasStarted) return "⏳ Not Started";
        if (isCurrentlyActive) return "🔥 Live Now";
        if (isExpiringSoon) return "⏰ Ending Soon";
        return "❓ Unknown";
    }

    public String getRemainingTimeDisplay() {
        if (hasEnded) return "Ended";
        if (!hasStarted) return "Not started";
        if (remainingMinutes < 0) return "No time limit";
        
        if (remainingMinutes == 0) return "Ending now";
        
        if (remainingMinutes < 60) {
            return remainingMinutes + " minute" + (remainingMinutes == 1 ? "" : "s") + " left";
        }
        
        long hours = remainingMinutes / 60;
        long minutes = remainingMinutes % 60;
        
        if (hours < 24) {
            String result = hours + " hour" + (hours == 1 ? "" : "s");
            if (minutes > 0) {
                result += " " + minutes + " min";
            }
            return result + " left";
        }
        
        long days = hours / 24;
        hours = hours % 24;
        
        String result = days + " day" + (days == 1 ? "" : "s");
        if (hours > 0) {
            result += " " + hours + "h";
        }
        return result + " left";
    }

    public String getDurationDisplay() {
        if (startTime != null && endTime != null) {
            return startTime.toLocalDate().toString() + " to " + endTime.toLocalDate().toString();
        }
        if (endTime != null) {
            return "Until " + endTime.toLocalDate().toString();
        }
        return "Ongoing";
    }

    public boolean canBePurchased() {
        return isCurrentlyActive && hasFlashSaleItems();
    }

    public boolean hasFlashSaleItems() {
        return flashSaleItems != null && !flashSaleItems.isEmpty();
    }

    public String getItemsSummary() {
        if (totalItems > 0) {
            return totalItems + " item" + (totalItems > 1 ? "s" : "") + 
                   " (" + totalAvailableItems + " available)";
        }
        return "No items";
    }

    public String getSalesProgressDisplay() {
        if (totalItems > 0) {
            return totalSoldItems + " / " + totalItems + " sold";
        }
        return "No sales data";
    }

    public double getSalesProgressPercentage() {
        if (totalItems == 0) return 0.0;
        return (double) totalSoldItems / totalItems * 100;
    }

    public String getFormattedSalesProgress() {
        return String.format("%.1f%% sold", getSalesProgressPercentage());
    }

    public boolean isHighDemand() {
        return getSalesProgressPercentage() >= 70;
    }

    public boolean isAlmostSoldOut() {
        return getSalesProgressPercentage() >= 90;
    }

    public String getDemandBadge() {
        if (isAlmostSoldOut()) return "🔥 Almost Sold Out";
        if (isHighDemand()) return "⚡ High Demand";
        return "";
    }

    public String getDemandColor() {
        if (isAlmostSoldOut()) return "red";
        if (isHighDemand()) return "orange";
        return "green";
    }

    public boolean isRecentlyCreated() {
        if (createdAt == null) return false;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return createdAt.isAfter(oneWeekAgo);
    }

    public String getFlashSaleBadges() {
        StringBuilder badges = new StringBuilder();
        
        if (isCurrentlyActive) {
            badges.append(" 🔥");
        }
        
        if (isExpiringSoon) {
            badges.append(" ⏰");
        }
        
        if (isHighDemand()) {
            badges.append(" ⚡");
        }
        
        if (isRecentlyCreated()) {
            badges.append(" 🆕");
        }
        
        return badges.toString().trim();
    }

    public String getFlashSaleSummary() {
        return getDisplayName() + " - " + getRemainingTimeDisplay() + " (" + getFormattedSalesProgress() + ")";
    }

    public boolean isPopularFlashSale() {
        return isCurrentlyActive && isHighDemand();
    }

    public String getFlashSaleType() {
        return "Flash Sale";
    }

    public boolean hasLimitedTime() {
        return endTime != null;
    }

    public String getTimeStatus() {
        if (!hasLimitedTime()) return "No time limit";
        if (hasEnded) return "Ended";
        if (!hasStarted) return "Starting soon";
        if (isCurrentlyActive) return "Live";
        return "Unknown";
    }

    public String getTimeStatusColor() {
        if (hasEnded) return "red";
        if (!hasStarted) return "blue";
        if (isCurrentlyActive) return "green";
        return "gray";
    }
}
