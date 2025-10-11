package models;

import java.math.BigDecimal;

public class FlashSaleItem {
    private int flashSaleItemId;
    private int flashSaleId;
    private int productId;
    private BigDecimal flashSalePrice;
    private Integer quantityLimit;
    private int soldQuantity;
    private Product product; // For joined queries

    // Constructors
    public FlashSaleItem() {}

    public FlashSaleItem(int flashSaleId, int productId, BigDecimal flashSalePrice) {
        this.flashSaleId = flashSaleId;
        this.productId = productId;
        this.flashSalePrice = flashSalePrice;
        this.soldQuantity = 0;
    }

    public FlashSaleItem(int flashSaleId, int productId, BigDecimal flashSalePrice, Integer quantityLimit) {
        this.flashSaleId = flashSaleId;
        this.productId = productId;
        this.flashSalePrice = flashSalePrice;
        this.quantityLimit = quantityLimit;
        this.soldQuantity = 0;
    }

    // Getters and Setters
    public int getFlashSaleItemId() { return flashSaleItemId; }
    public void setFlashSaleItemId(int flashSaleItemId) { this.flashSaleItemId = flashSaleItemId; }

    public int getFlashSaleId() { return flashSaleId; }
    public void setFlashSaleId(int flashSaleId) { this.flashSaleId = flashSaleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public BigDecimal getFlashSalePrice() { return flashSalePrice; }
    public void setFlashSalePrice(BigDecimal flashSalePrice) { this.flashSalePrice = flashSalePrice; }

    public Integer getQuantityLimit() { return quantityLimit; }
    public void setQuantityLimit(Integer quantityLimit) { this.quantityLimit = quantityLimit; }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // Business methods
    public boolean isAvailable() {
        return quantityLimit == null || soldQuantity < quantityLimit;
    }

    public int getRemainingQuantity() {
        if (quantityLimit == null) return Integer.MAX_VALUE;
        return Math.max(0, quantityLimit - soldQuantity);
    }

    public boolean canSell(int requestedQuantity) {
        return isAvailable() && getRemainingQuantity() >= requestedQuantity;
    }

    public void sell(int quantity) {
        if (canSell(quantity)) {
            this.soldQuantity += quantity;
        }
    }

    public BigDecimal getSavingsAmount(BigDecimal originalPrice) {
        return originalPrice.subtract(flashSalePrice);
    }

    public BigDecimal getSavingsPercentage(BigDecimal originalPrice) {
        if (originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getSavingsAmount(originalPrice)
                    .divide(originalPrice, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    public boolean isSoldOut() {
        return quantityLimit != null && soldQuantity >= quantityLimit;
    }
}
