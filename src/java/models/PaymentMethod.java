package models;

public enum PaymentMethod {
    CASH_ON_DELIVERY("cash_on_delivery", "Thanh toán khi nhận hàng"),
    CREDIT_CARD("credit_card", "Thẻ tín dụng"),
    BANK_TRANSFER("bank_transfer", "Chuyển khoản ngân hàng"),
    MOMO("momo", "Ví MoMo"),
    ZALOPAY("zalopay", "ZaloPay"),
    VNPAY("vnpay", "VNPay");

    private final String value;
    private final String displayName;

    PaymentMethod(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static PaymentMethod fromString(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid payment method: " + value);
    }
}