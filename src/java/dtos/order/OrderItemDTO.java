package dtos.order;

import java.math.BigDecimal;

/**
 * DTO for order item details
 * Used for displaying individual order items with product information
 */
public class OrderItemDTO {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    
    // Product information (flattened for display)
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productSlug;
    private String categoryName;
    private String subcategoryName;
    private BigDecimal currentPrice; // Current price of the product (may differ from order price)
    private boolean isProductActive;
    private boolean isProductInStock;

    // Constructors
    public OrderItemDTO() {}

    public OrderItemDTO(int orderItemId, int orderId, int productId, int quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price.multiply(new BigDecimal(quantity));
    }

    // Getters and Setters
    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getProductSlug() { return productSlug; }
    public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public boolean isProductActive() { return isProductActive; }
    public void setProductActive(boolean productActive) { isProductActive = productActive; }

    public boolean isProductInStock() { return isProductInStock; }
    public void setProductInStock(boolean productInStock) { isProductInStock = productInStock; }

    // Business methods
    public boolean isValidQuantity() {
        return quantity > 0;
    }

    public boolean isValidPrice() {
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public BigDecimal getCalculatedTotalPrice() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(new BigDecimal(quantity));
    }

    public String getFormattedPrice() {
        return price != null ? String.format("%.2f", price) : "0.00";
    }

    public String getFormattedTotalPrice() {
        return totalPrice != null ? String.format("%.2f", totalPrice) : "0.00";
    }

    public String getFormattedCurrentPrice() {
        return currentPrice != null ? String.format("%.2f", currentPrice) : "0.00";
    }

    public String getDisplayName() {
        return productName != null ? productName : "Unknown Product";
    }

    public String getShortDescription() {
        if (productDescription != null && productDescription.length() > 100) {
            return productDescription.substring(0, 97) + "...";
        }
        return productDescription;
    }

    public String getProductUrl() {
        if (productSlug != null) {
            return "/product/" + productSlug;
        }
        return "/product/" + productId;
    }

    public boolean hasProductChanged() {
        return currentPrice != null && price != null && !currentPrice.equals(price);
    }

    public BigDecimal getPriceDifference() {
        if (hasProductChanged()) {
            return currentPrice.subtract(price);
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedPriceDifference() {
        return String.format("%.2f", getPriceDifference());
    }

    public boolean isPriceIncreased() {
        return getPriceDifference().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isPriceDecreased() {
        return getPriceDifference().compareTo(BigDecimal.ZERO) < 0;
    }

    public String getPriceChangeIndicator() {
        if (isPriceIncreased()) return "↗️";
        if (isPriceDecreased()) return "↘️";
        return "";
    }

    public boolean isProductAvailable() {
        return isProductActive && isProductInStock;
    }

    public String getAvailabilityStatus() {
        if (!isProductActive) return "Discontinued";
        if (!isProductInStock) return "Out of Stock";
        return "Available";
    }

    public String getAvailabilityColor() {
        if (!isProductActive) return "red";
        if (!isProductInStock) return "orange";
        return "green";
    }

    public boolean canReorder() {
        return isProductAvailable();
    }

    public String getCategoryPath() {
        if (categoryName == null && subcategoryName == null) return "Unknown Category";
        if (subcategoryName == null) return categoryName;
        return categoryName + " > " + subcategoryName;
    }

    public boolean hasImage() {
        return productImageUrl != null && !productImageUrl.trim().isEmpty();
    }

    public String getImageUrlOrDefault() {
        return hasImage() ? productImageUrl : "/images/default-product.jpg";
    }

    public boolean isHighQuantity() {
        return quantity >= 10;
    }

    public boolean isLowQuantity() {
        return quantity == 1;
    }

    public String getQuantityDisplay() {
        if (quantity == 1) return "1 item";
        return quantity + " items";
    }

    public BigDecimal getSavingsFromCurrentPrice() {
        if (hasProductChanged() && isPriceDecreased()) {
            return getPriceDifference().abs().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    public String getFormattedSavings() {
        return String.format("%.2f", getSavingsFromCurrentPrice());
    }

    public boolean hasSavings() {
        return getSavingsFromCurrentPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public String getItemSummary() {
        return getQuantityDisplay() + " of " + getDisplayName() + " - " + getFormattedTotalPrice();
    }

    public boolean isEligibleForReview() {
        return productId > 0 && quantity > 0;
    }
}
