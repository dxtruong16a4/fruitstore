package com.fruitstore.domain.discount;

/**
 * Enum representing discount types in the system
 */
public enum DiscountType {
    PERCENTAGE("percentage"),
    FIXED_AMOUNT("fixed_amount");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert database string value to enum
     */
    public static DiscountType fromValue(String value) {
        for (DiscountType type : DiscountType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid discount type: " + value);
    }

    /**
     * Check if this is a percentage discount
     * @return true if percentage discount
     */
    public boolean isPercentage() {
        return this == PERCENTAGE;
    }

    /**
     * Check if this is a fixed amount discount
     * @return true if fixed amount discount
     */
    public boolean isFixedAmount() {
        return this == FIXED_AMOUNT;
    }
}
