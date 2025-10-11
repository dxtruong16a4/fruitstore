package models;

import java.time.LocalDateTime;

public class FlashSale {
    private int flashSaleId;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Constructors
    public FlashSale() {}

    public FlashSale(String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
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

    // Business methods
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && 
               (startTime == null || now.isAfter(startTime) || now.isEqual(startTime)) &&
               (endTime == null || now.isBefore(endTime) || now.isEqual(endTime));
    }

    public boolean hasStarted() {
        LocalDateTime now = LocalDateTime.now();
        return startTime == null || now.isAfter(startTime) || now.isEqual(startTime);
    }

    public boolean hasEnded() {
        LocalDateTime now = LocalDateTime.now();
        return endTime != null && now.isAfter(endTime);
    }

    public long getRemainingMinutes() {
        if (endTime == null) return -1; // No end time
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endTime)) return 0;
        
        return java.time.Duration.between(now, endTime).toMinutes();
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
