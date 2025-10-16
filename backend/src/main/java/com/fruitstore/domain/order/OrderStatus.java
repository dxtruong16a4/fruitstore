package com.fruitstore.domain.order;

/**
 * Enum representing order statuses in the system
 */
public enum OrderStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert database string value to enum
     */
    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + value);
    }

    /**
     * Check if the order can be cancelled
     * @return true if order can be cancelled
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    /**
     * Check if the order is in a final state
     * @return true if order is completed or cancelled
     */
    public boolean isFinalState() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * Check if the order is active (not cancelled)
     * @return true if order is active
     */
    public boolean isActive() {
        return this != CANCELLED;
    }
}
