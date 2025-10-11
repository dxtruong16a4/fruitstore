package models;

public class ComboItem {
    private int comboItemId;
    private int comboId;
    private int productId;
    private int quantity;
    private Product product; // For joined queries
    private Combo combo; // For joined queries

    // Constructors
    public ComboItem() {}

    public ComboItem(int comboId, int productId, int quantity) {
        this.comboId = comboId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getComboItemId() { return comboItemId; }
    public void setComboItemId(int comboItemId) { this.comboItemId = comboItemId; }

    public int getComboId() { return comboId; }
    public void setComboId(int comboId) { this.comboId = comboId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Combo getCombo() { return combo; }
    public void setCombo(Combo combo) { this.combo = combo; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isProductAvailable() {
        return product != null && product.isInStock();
    }

    public boolean hasEnoughStock() {
        return product != null && product.getStockQuantity() >= quantity;
    }

    public boolean canBeIncluded() {
        return isValidQuantity() && isProductAvailable() && hasEnoughStock();
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
        }
    }

    public String getProductName() {
        return product != null ? product.getName() : "Unknown Product";
    }

    public String getComboName() {
        return combo != null ? combo.getName() : "Unknown Combo";
    }
}
