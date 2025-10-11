package dtos.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for payment information
 * Used for displaying payment details and processing information
 */
public class PaymentDTO {
    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private String paymentMethodDisplayName;
    private String paymentStatus;
    private String paymentStatusDisplayName;
    private BigDecimal amount;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String notes;
    
    // Order information (flattened for context)
    private String orderNumber;
    private String customerName;
    private String customerEmail;

    // Constructors
    public PaymentDTO() {}

    public PaymentDTO(int paymentId, int orderId, String paymentMethod, String paymentStatus, BigDecimal amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public PaymentDTO(int orderId, String paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = "pending";
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentMethodDisplayName() { return paymentMethodDisplayName; }
    public void setPaymentMethodDisplayName(String paymentMethodDisplayName) { this.paymentMethodDisplayName = paymentMethodDisplayName; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentStatusDisplayName() { return paymentStatusDisplayName; }
    public void setPaymentStatusDisplayName(String paymentStatusDisplayName) { this.paymentStatusDisplayName = paymentStatusDisplayName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    // Business methods
    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(paymentStatus);
    }

    public boolean isPending() {
        return "pending".equalsIgnoreCase(paymentStatus);
    }

    public boolean isProcessing() {
        return "processing".equalsIgnoreCase(paymentStatus);
    }

    public boolean isFailed() {
        return "failed".equalsIgnoreCase(paymentStatus);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(paymentStatus);
    }

    public boolean isRefunded() {
        return "refunded".equalsIgnoreCase(paymentStatus);
    }

    public boolean canBeRefunded() {
        return isCompleted();
    }

    public boolean canBeCancelled() {
        return isPending() || isProcessing();
    }

    public String getFormattedAmount() {
        return amount != null ? String.format("%.2f", amount) : "0.00";
    }

    public String getPaymentDateFormatted() {
        return paymentDate != null ? paymentDate.toLocalDate().toString() : "Not paid";
    }

    public String getPaymentDateTimeFormatted() {
        return paymentDate != null ? paymentDate.toString() : "Not paid";
    }

    public String getPaymentStatusColor() {
        if (paymentStatus == null) return "gray";
        
        switch (paymentStatus.toLowerCase()) {
            case "pending": return "orange";
            case "processing": return "blue";
            case "completed": return "green";
            case "failed": return "red";
            case "cancelled": return "gray";
            case "refunded": return "purple";
            default: return "gray";
        }
    }

    public String getPaymentStatusIcon() {
        if (paymentStatus == null) return "💳";
        
        switch (paymentStatus.toLowerCase()) {
            case "pending": return "⏳";
            case "processing": return "⚙️";
            case "completed": return "✅";
            case "failed": return "❌";
            case "cancelled": return "🚫";
            case "refunded": return "↩️";
            default: return "💳";
        }
    }

    public String getPaymentMethodIcon() {
        if (paymentMethod == null) return "💳";
        
        switch (paymentMethod.toLowerCase()) {
            case "cash_on_delivery": return "💰";
            case "bank_transfer": return "🏦";
            case "credit_card": return "💳";
            case "debit_card": return "💳";
            case "paypal": return "🅿️";
            case "momo": return "📱";
            case "zalopay": return "📱";
            case "vnpay": return "📱";
            default: return "💳";
        }
    }

    public String getPaymentMethodDisplayNameOrDefault() {
        if (paymentMethodDisplayName != null && !paymentMethodDisplayName.trim().isEmpty()) {
            return paymentMethodDisplayName;
        }
        return getPaymentMethod();
    }

    public String getPaymentStatusDisplayNameOrDefault() {
        if (paymentStatusDisplayName != null && !paymentStatusDisplayName.trim().isEmpty()) {
            return paymentStatusDisplayName;
        }
        return getPaymentStatus();
    }

    public boolean hasTransactionId() {
        return transactionId != null && !transactionId.trim().isEmpty();
    }

    public String getTransactionIdDisplay() {
        if (hasTransactionId()) {
            if (transactionId.length() > 12) {
                return transactionId.substring(0, 8) + "..." + transactionId.substring(transactionId.length() - 4);
            }
            return transactionId;
        }
        return "No transaction ID";
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public String getShortNotes() {
        if (!hasNotes()) return "";
        if (notes.length() > 100) {
            return notes.substring(0, 97) + "...";
        }
        return notes;
    }

    public boolean isCashOnDelivery() {
        return "cash_on_delivery".equalsIgnoreCase(paymentMethod);
    }

    public boolean isOnlinePayment() {
        return !isCashOnDelivery();
    }

    public boolean isMobilePayment() {
        return paymentMethod != null && (
            "momo".equalsIgnoreCase(paymentMethod) ||
            "zalopay".equalsIgnoreCase(paymentMethod) ||
            "vnpay".equalsIgnoreCase(paymentMethod)
        );
    }

    public boolean isCardPayment() {
        return paymentMethod != null && (
            "credit_card".equalsIgnoreCase(paymentMethod) ||
            "debit_card".equalsIgnoreCase(paymentMethod)
        );
    }

    public boolean isBankTransfer() {
        return "bank_transfer".equalsIgnoreCase(paymentMethod);
    }

    public String getPaymentType() {
        if (isCashOnDelivery()) return "Cash on Delivery";
        if (isMobilePayment()) return "Mobile Payment";
        if (isCardPayment()) return "Card Payment";
        if (isBankTransfer()) return "Bank Transfer";
        return "Online Payment";
    }

    public boolean isRecentPayment() {
        if (paymentDate == null) return false;
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return paymentDate.isAfter(oneDayAgo);
    }

    public boolean isTodayPayment() {
        if (paymentDate == null) return false;
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        return paymentDate.isAfter(today) && paymentDate.isBefore(tomorrow);
    }

    public String getTimeAgo() {
        if (paymentDate == null) return "Not paid";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(paymentDate, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        
        long days = hours / 24;
        if (days < 7) return days + " day" + (days == 1 ? "" : "s") + " ago";
        
        return getPaymentDateFormatted();
    }

    public String getPaymentSummary() {
        return getFormattedAmount() + " via " + getPaymentMethodDisplayNameOrDefault() + 
               " - " + getPaymentStatusDisplayNameOrDefault();
    }

    public String getOrderPaymentUrl() {
        return "/order/" + orderNumber + "/payment";
    }

    public boolean requiresConfirmation() {
        return isPending() || isProcessing();
    }

    public boolean isSuccessful() {
        return isCompleted();
    }

    public boolean isUnsuccessful() {
        return isFailed() || isCancelled();
    }

    public String getPaymentBadge() {
        if (isSuccessful()) return "✅ Paid";
        if (isUnsuccessful()) return "❌ Failed";
        if (isRefunded()) return "↩️ Refunded";
        if (requiresConfirmation()) return "⏳ Pending";
        return "💳 Payment";
    }
}
