package models;

public class CartCombo {
    private int cartComboId;
    private int userId;
    private int comboId;
    private int quantity;
    private boolean selected;
    private Combo combo; // For joined queries

    // Constructors
    public CartCombo() {}

    public CartCombo(int userId, int comboId, int quantity) {
        this.userId = userId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.selected = true;
    }

    // Getters and Setters
    public int getCartComboId() { return cartComboId; }
    public void setCartComboId(int cartComboId) { this.cartComboId = cartComboId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public Combo getCombo() { return combo; }
    public void setCombo(Combo combo) { this.combo = combo; }

    // Business methods
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public void decreaseQuantity(int amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
        }
    }

    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public void toggleSelection() {
        this.selected = !this.selected;
    }

    public void select() {
        this.selected = true;
    }

    public void unselect() {
        this.selected = false;
    }

    public boolean isAvailable() {
        return combo != null && combo.isCurrentlyActive();
    }

    public boolean canBeAddedToCart() {
        return isValidQuantity() && isAvailable() && isSelected();
    }
}
