package models;

import java.math.BigDecimal;

public class OrderComboItem {
    private int orderComboItemId;
    private int orderId;
    private int comboId;
    private int quantity;
    private BigDecimal price;
    private Combo combo; // For joined queries

    // Constructors
    public OrderComboItem() {}

    public OrderComboItem(int orderId, int comboId, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public int getOrderComboItemId() { return orderComboItemId; }
    public void setOrderComboItemId(int orderComboItemId) { this.orderComboItemId = orderComboItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Combo getCombo() { return combo; }
    public void setCombo(Combo combo) { this.combo = combo; }

    // Business methods
    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(quantity));
    }

    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isValidPrice() {
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public String getFormattedTotalPrice() {
        return String.format("%.2f", getTotalPrice());
    }

    public String getFormattedPrice() {
        return String.format("%.2f", price);
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
        }
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
            this.price = newPrice;
        }
    }
}
