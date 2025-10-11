package models;

public enum DiscountType {
    PERCENTAGE("percentage", "Phần trăm"),
    FIXED_AMOUNT("fixed_amount", "Số tiền cố định");

    private final String value;
    private final String displayName;

    DiscountType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static DiscountType fromString(String value) {
        for (DiscountType type : DiscountType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid discount type: " + value);
    }
}