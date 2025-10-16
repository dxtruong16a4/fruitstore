package com.fruitstore.dto.response.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for cart response
 */
public class CartResponse {

    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
    private Integer itemCount;
    private Integer totalItems;
    private BigDecimal subtotal;
    private BigDecimal totalAmount;
    private boolean isEmpty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CartResponse() {}

    public CartResponse(Long cartId, Long userId, List<CartItemResponse> items, Integer itemCount,
                       Integer totalItems, BigDecimal subtotal, BigDecimal totalAmount, boolean isEmpty,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.itemCount = itemCount;
        this.totalItems = totalItems;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.isEmpty = isEmpty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
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
        return "CartResponse{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", itemCount=" + itemCount +
                ", totalItems=" + totalItems +
                ", subtotal=" + subtotal +
                ", totalAmount=" + totalAmount +
                ", isEmpty=" + isEmpty +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartResponse that = (CartResponse) o;

        return cartId != null ? cartId.equals(that.cartId) : that.cartId == null;
    }

    @Override
    public int hashCode() {
        return cartId != null ? cartId.hashCode() : 0;
    }
}
