package com.fruitstore.domain.cart;

import com.fruitstore.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart entity representing a user's shopping cart
 * One-to-one relationship with User
 * One-to-many relationship with CartItem
 */
@Entity
@Table(name = "cart", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    // Business logic methods
    /**
     * Add a cart item to this cart
     * @param cartItem the cart item to add
     */
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    /**
     * Remove a cart item from this cart
     * @param cartItem the cart item to remove
     */
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    /**
     * Clear all items from the cart
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Get the total number of items in the cart
     * @return total quantity of all items
     */
    public int getTotalItems() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Check if the cart is empty
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Get cart item count (number of different products)
     * @return number of different products in cart
     */
    public int getItemCount() {
        return cartItems.size();
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", itemCount=" + getItemCount() +
                ", totalItems=" + getTotalItems() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        return cartId != null ? cartId.equals(cart.cartId) : cart.cartId == null;
    }

    @Override
    public int hashCode() {
        return cartId != null ? cartId.hashCode() : 0;
    }
}
