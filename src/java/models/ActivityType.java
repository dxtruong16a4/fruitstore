package models;

public enum ActivityType {
    LOGIN("login", "Đăng nhập"),
    LOGOUT("logout", "Đăng xuất"),
    REGISTER("register", "Đăng ký"),
    VIEW_PRODUCT("view_product", "Xem sản phẩm"),
    ADD_TO_CART("add_to_cart", "Thêm vào giỏ hàng"),
    PURCHASE("purchase", "Mua hàng"),
    REVIEW("review", "Đánh giá");

    private final String value;
    private final String displayName;

    ActivityType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static ActivityType fromString(String value) {
        for (ActivityType type : ActivityType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid activity type: " + value);
    }
}