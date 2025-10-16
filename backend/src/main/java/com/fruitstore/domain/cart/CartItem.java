package com.fruitstore.domain.cart;

import com.fruitstore.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * CartItem entity representing a product in a shopping cart
 * Many-to-one relationship with Cart
 * Many-to-one relationship with Product
 */
@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_id", columnList = "cart_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructors
    public CartItem() {}

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    // Business logic methods
    /**
     * Update the quantity of this cart item
     * @param newQuantity the new quantity
     */
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = newQuantity;
    }

    /**
     * Increase quantity by specified amount
     * @param amount the amount to increase
     */
    public void increaseQuantity(Integer amount) {
        if (amount == null || amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        this.quantity += amount;
    }

    /**
     * Decrease quantity by specified amount
     * @param amount the amount to decrease
     */
    public void decreaseQuantity(Integer amount) {
        if (amount == null || amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        if (this.quantity - amount < 1) {
            throw new IllegalArgumentException("Quantity cannot be less than 1");
        }
        this.quantity -= amount;
    }

    /**
     * Calculate the subtotal for this cart item
     * @return quantity * product price
     */
    public java.math.BigDecimal getSubtotal() {
        if (product == null || product.getPrice() == null || quantity == null) {
            return java.math.BigDecimal.ZERO;
        }
        return product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity));
    }

    /**
     * Check if the product is still available (active and in stock)
     * @return true if product is available
     */
    public boolean isProductAvailable() {
        return product != null && 
               product.getIsActive() && 
               product.getStockQuantity() > 0;
    }

    /**
     * Check if there's sufficient stock for the requested quantity
     * @return true if sufficient stock available
     */
    public boolean hasSufficientStock() {
        return product != null && 
               product.getStockQuantity() >= quantity;
    }

    // Getters and Setters
    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", cartId=" + (cart != null ? cart.getCartId() : null) +
                ", productId=" + (product != null ? product.getProductId() : null) +
                ", quantity=" + quantity +
                ", subtotal=" + getSubtotal() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        return cartItemId != null ? cartItemId.equals(cartItem.cartItemId) : cartItem.cartItemId == null;
    }

    @Override
    public int hashCode() {
        return cartItemId != null ? cartItemId.hashCode() : 0;
    }
}
