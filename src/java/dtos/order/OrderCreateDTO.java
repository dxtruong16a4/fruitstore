package dtos.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for order creation from cart
 * Used for receiving order creation data from frontend
 */
public class OrderCreateDTO {
    private int userId;
    private int shippingAddressId;
    private String paymentMethod;
    private String notes;
    private LocalDate estimatedDeliveryDate;
    
    // Cart items to be converted to order
    private List<Integer> cartItemIds; // Selected cart items
    private List<Integer> cartComboIds; // Selected cart combo items
    
    // Calculated amounts (can be set by frontend for validation)
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal finalAmount;
    
    // Discount information
    private String discountCode;
    
    // Delivery preferences
    private boolean requiresSignature;
    private String deliveryInstructions;

    // Constructors
    public OrderCreateDTO() {}

    public OrderCreateDTO(int userId, int shippingAddressId, String paymentMethod) {
        this.userId = userId;
        this.shippingAddressId = shippingAddressId;
        this.paymentMethod = paymentMethod;
    }

    public OrderCreateDTO(int userId, int shippingAddressId, String paymentMethod, 
                         List<Integer> cartItemIds, List<Integer> cartComboIds) {
        this.userId = userId;
        this.shippingAddressId = shippingAddressId;
        this.paymentMethod = paymentMethod;
        this.cartItemIds = cartItemIds;
        this.cartComboIds = cartComboIds;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(int shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }

    public List<Integer> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Integer> cartItemIds) { this.cartItemIds = cartItemIds; }

    public List<Integer> getCartComboIds() { return cartComboIds; }
    public void setCartComboIds(List<Integer> cartComboIds) { this.cartComboIds = cartComboIds; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    public boolean isRequiresSignature() { return requiresSignature; }
    public void setRequiresSignature(boolean requiresSignature) { this.requiresSignature = requiresSignature; }

    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }

    // Business methods
    public boolean isValid() {
        return userId > 0 &&
               shippingAddressId > 0 &&
               paymentMethod != null && !paymentMethod.trim().isEmpty() &&
               hasCartItems() &&
               totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0 &&
               finalAmount != null && finalAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasValidUserId() {
        return userId > 0;
    }

    public boolean hasValidShippingAddress() {
        return shippingAddressId > 0;
    }

    public boolean hasValidPaymentMethod() {
        return paymentMethod != null && !paymentMethod.trim().isEmpty();
    }

    public boolean hasCartItems() {
        return (cartItemIds != null && !cartItemIds.isEmpty()) ||
               (cartComboIds != null && !cartComboIds.isEmpty());
    }

    public boolean hasRegularItems() {
        return cartItemIds != null && !cartItemIds.isEmpty();
    }

    public boolean hasComboItems() {
        return cartComboIds != null && !cartComboIds.isEmpty();
    }

    public boolean hasValidAmounts() {
        return totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0 &&
               finalAmount != null && finalAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasValidDiscount() {
        if (discountAmount == null) return true; // No discount is valid
        return discountAmount.compareTo(BigDecimal.ZERO) >= 0 && 
               discountAmount.compareTo(totalAmount) <= 0;
    }

    public boolean hasValidShippingFee() {
        if (shippingFee == null) return true; // No shipping fee is valid
        return shippingFee.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isFormValid() {
        return isValid() && hasValidDiscount() && hasValidShippingFee();
    }

    public int getTotalCartItems() {
        int count = 0;
        if (cartItemIds != null) count += cartItemIds.size();
        if (cartComboIds != null) count += cartComboIds.size();
        return count;
    }

    public boolean hasDiscountCode() {
        return discountCode != null && !discountCode.trim().isEmpty();
    }

    public String getTrimmedDiscountCode() {
        return discountCode != null ? discountCode.trim().toUpperCase() : null;
    }

    public String getTrimmedNotes() {
        return notes != null ? notes.trim() : null;
    }

    public String getTrimmedDeliveryInstructions() {
        return deliveryInstructions != null ? deliveryInstructions.trim() : null;
    }

    public boolean hasDeliveryInstructions() {
        return deliveryInstructions != null && !deliveryInstructions.trim().isEmpty();
    }

    public BigDecimal getCalculatedFinalAmount() {
        if (totalAmount == null) return BigDecimal.ZERO;
        
        BigDecimal finalAmount = totalAmount;
        
        if (discountAmount != null) {
            finalAmount = finalAmount.subtract(discountAmount);
        }
        
        if (shippingFee != null) {
            finalAmount = finalAmount.add(shippingFee);
        }
        
        return finalAmount.max(BigDecimal.ZERO);
    }

    public boolean isAmountCalculationCorrect() {
        BigDecimal calculated = getCalculatedFinalAmount();
        return finalAmount != null && calculated.equals(finalAmount);
    }

    public String getValidationErrors() {
        StringBuilder errors = new StringBuilder();
        
        if (userId <= 0) {
            errors.append("Valid user ID is required. ");
        }
        
        if (shippingAddressId <= 0) {
            errors.append("Valid shipping address is required. ");
        }
        
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            errors.append("Payment method is required. ");
        }
        
        if (!hasCartItems()) {
            errors.append("At least one cart item is required. ");
        }
        
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Valid total amount is required. ");
        }
        
        if (finalAmount == null || finalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Valid final amount is required. ");
        }
        
        if (!isAmountCalculationCorrect()) {
            errors.append("Amount calculation is incorrect. ");
        }
        
        if (!hasValidDiscount()) {
            errors.append("Invalid discount amount. ");
        }
        
        if (!hasValidShippingFee()) {
            errors.append("Invalid shipping fee. ");
        }
        
        return errors.toString().trim();
    }

    public boolean isCashOnDelivery() {
        return "cash_on_delivery".equalsIgnoreCase(paymentMethod);
    }

    public boolean isOnlinePayment() {
        return !isCashOnDelivery();
    }

    public String getFormattedTotalAmount() {
        return totalAmount != null ? String.format("%.2f", totalAmount) : "0.00";
    }

    public String getFormattedFinalAmount() {
        return finalAmount != null ? String.format("%.2f", finalAmount) : "0.00";
    }

    public String getFormattedDiscountAmount() {
        return discountAmount != null ? String.format("%.2f", discountAmount) : "0.00";
    }

    public String getFormattedShippingFee() {
        return shippingFee != null ? String.format("%.2f", shippingFee) : "0.00";
    }

    public boolean isEstimatedDeliverySet() {
        return estimatedDeliveryDate != null;
    }

    public String getEstimatedDeliveryFormatted() {
        return estimatedDeliveryDate != null ? estimatedDeliveryDate.toString() : "Not set";
    }
}
