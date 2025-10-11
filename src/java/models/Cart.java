package models;

public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private boolean selected;
    private User user; // For joined queries
    private Product product; // For joined queries

    // Constructors
    public Cart() {}

    public Cart(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.selected = true;
    }

    public Cart(User user, Product product, int quantity) {
        this.user = user;
        this.userId = user != null ? user.getUserId() : 0;
        this.product = product;
        this.productId = product != null ? product.getProductId() : 0;
        this.quantity = quantity;
        this.selected = true;
    }

    // Getters and Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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

    public User getUser() { return user; }
    public void setUser(User user) { 
        this.user = user; 
        this.userId = user != null ? user.getUserId() : 0;
    }

    // Business methods with relationships
    public String getProductName() {
        return product != null ? product.getName() : "Unknown Product";
    }

    public String getProductImageUrl() {
        return product != null ? product.getImageUrl() : null;
    }

    public java.math.BigDecimal getProductPrice() {
        return product != null ? product.getPrice() : java.math.BigDecimal.ZERO;
    }

    public java.math.BigDecimal getTotalPrice() {
        return getProductPrice().multiply(new java.math.BigDecimal(quantity));
    }

    public String getFormattedTotalPrice() {
        return String.format("%.2f", getTotalPrice());
    }

    public boolean isProductInStock() {
        return product != null && product.isInStock();
    }

    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean canBeAddedToOrder() {
        return isSelected() && isValidQuantity() && isProductInStock();
    }
}