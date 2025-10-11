package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String notes;
    private Order order; // For joined queries

    // Enum for payment status
    public enum PaymentStatus {
        PENDING("pending", "Chờ xử lý"),
        PROCESSING("processing", "Đang xử lý"),
        COMPLETED("completed", "Hoàn thành"),
        FAILED("failed", "Thất bại"),
        CANCELLED("cancelled", "Đã hủy"),
        REFUNDED("refunded", "Đã hoàn tiền");

        private final String value;
        private final String displayName;

        PaymentStatus(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public String getValue() { return value; }
        public String getDisplayName() { return displayName; }

        public static PaymentStatus fromString(String value) {
            for (PaymentStatus status : PaymentStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid payment status: " + value);
        }
    }

    // Constructors
    public Payment() {}

    public Payment(int orderId, PaymentMethod paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentDate = LocalDateTime.now();
    }

    public Payment(int orderId, PaymentMethod paymentMethod, PaymentStatus paymentStatus, 
                   BigDecimal amount, String transactionId) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    // Business methods
    public boolean isCompleted() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    public boolean isPending() {
        return paymentStatus == PaymentStatus.PENDING;
    }

    public boolean isFailed() {
        return paymentStatus == PaymentStatus.FAILED;
    }

    public boolean isRefunded() {
        return paymentStatus == PaymentStatus.REFUNDED;
    }

    public boolean canBeRefunded() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    public boolean canBeCancelled() {
        return paymentStatus == PaymentStatus.PENDING || paymentStatus == PaymentStatus.PROCESSING;
    }

    public void markAsCompleted(String transactionId) {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
    }

    public void markAsFailed(String notes) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.notes = notes;
    }

    public void markAsRefunded() {
        this.paymentStatus = PaymentStatus.REFUNDED;
        this.paymentDate = LocalDateTime.now();
    }

    public String getFormattedAmount() {
        return String.format("%.2f", amount);
    }

    public String getPaymentDateFormatted() {
        return paymentDate.toLocalDate().toString();
    }
}
