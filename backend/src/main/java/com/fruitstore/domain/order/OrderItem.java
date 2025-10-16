package com.fruitstore.domain.order;

import com.fruitstore.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OrderItem entity representing a product in an order
 * Many-to-one relationship with Order
 * Many-to-one relationship with Product
 */
@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_product_id", columnList = "product_id")
})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.01", message = "Subtotal must be greater than 0")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.subtotal == null) {
            calculateSubtotal();
        }
    }

    // Constructors
    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        calculateSubtotal();
    }

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    // Business logic methods
    /**
     * Calculate the subtotal for this order item
     * @return quantity * unit price
     */
    public BigDecimal calculateSubtotal() {
        if (unitPrice == null || quantity == null) {
            this.subtotal = BigDecimal.ZERO;
        } else {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return this.subtotal;
    }

    /**
     * Update the quantity of this order item
     * @param newQuantity the new quantity
     */
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = newQuantity;
        calculateSubtotal();
    }

    /**
     * Update the unit price of this order item
     * @param newUnitPrice the new unit price
     */
    public void updateUnitPrice(BigDecimal newUnitPrice) {
        if (newUnitPrice == null || newUnitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }
        this.unitPrice = newUnitPrice;
        calculateSubtotal();
    }

    /**
     * Get the product name (for display purposes)
     * @return product name or null if product is null
     */
    public String getProductName() {
        return product != null ? product.getName() : null;
    }

    /**
     * Get the product description (for display purposes)
     * @return product description or null if product is null
     */
    public String getProductDescription() {
        return product != null ? product.getDescription() : null;
    }

    /**
     * Get the product image URL (for display purposes)
     * @return product image URL or null if product is null
     */
    public String getProductImageUrl() {
        return product != null ? product.getImageUrl() : null;
    }

    /**
     * Check if the product is still active
     * @return true if product is active
     */
    public boolean isProductActive() {
        return product != null && product.getIsActive();
    }

    /**
     * Get the current stock quantity of the product
     * @return current stock quantity or 0 if product is null
     */
    public Integer getCurrentStockQuantity() {
        return product != null ? product.getStockQuantity() : 0;
    }

    /**
     * Check if the product has sufficient stock for the ordered quantity
     * @return true if sufficient stock available
     */
    public boolean hasSufficientStock() {
        return product != null && 
               product.getStockQuantity() >= quantity;
    }

    /**
     * Check if the unit price matches the current product price
     * @return true if unit price matches current product price
     */
    public boolean isPriceCurrent() {
        return product != null && 
               product.getPrice() != null && 
               product.getPrice().equals(unitPrice);
    }

    // Getters and Setters
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + (order != null ? order.getOrderId() : null) +
                ", productId=" + (product != null ? product.getProductId() : null) +
                ", productName='" + getProductName() + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        return orderItemId != null ? orderItemId.equals(orderItem.orderItemId) : orderItem.orderItemId == null;
    }

    @Override
    public int hashCode() {
        return orderItemId != null ? orderItemId.hashCode() : 0;
    }
}
