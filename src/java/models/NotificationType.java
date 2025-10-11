package models;

public enum NotificationType {
    NEW_PRODUCT("new_product", "Sản phẩm mới"),
    DISCOUNT("discount", "Khuyến mãi"),
    ORDER_UPDATE("order_update", "Cập nhật đơn hàng"),
    GENERAL("general", "Thông báo chung");

    private final String value;
    private final String displayName;

    NotificationType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static NotificationType fromString(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid notification type: " + value);
    }
}