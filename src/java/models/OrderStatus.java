package models;

public enum OrderStatus {
    PENDING("pending", "Chờ xử lý"),
    CONFIRMED("confirmed", "Đã xác nhận"),
    PROCESSING("processing", "Đang xử lý"),
    SHIPPED("shipped", "Đang giao hàng"),
    DELIVERED("delivered", "Đã giao hàng"),
    CANCELLED("cancelled", "Đã hủy"),
    RETURNED("returned", "Đã trả hàng");

    private final String value;
    private final String displayName;

    OrderStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static OrderStatus fromString(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + value);
    }
}