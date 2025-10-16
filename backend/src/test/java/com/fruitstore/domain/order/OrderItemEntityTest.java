package com.fruitstore.domain.order;

import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderItem entity
 * Tests constructors, business methods, validation, and relationships
 */
@DisplayName("OrderItem Entity Tests")
class OrderItemEntityTest {

    private Order testOrder;
    private Product testProduct;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        // Create test user
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Create test order
        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(BigDecimal.valueOf(100.00));

        // Create test product
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Test Apple");
        testProduct.setDescription("Fresh red apple");
        testProduct.setPrice(BigDecimal.valueOf(2.50));
        testProduct.setStockQuantity(100);
        testProduct.setIsActive(true);

        // Create test order item
        orderItem = new OrderItem();
    }

    @Test
    @DisplayName("Should create OrderItem with default constructor")
    void shouldCreateOrderItemWithDefaultConstructor() {
        OrderItem item = new OrderItem();
        
        assertNotNull(item);
        assertNull(item.getOrderItemId());
        assertNull(item.getOrder());
        assertNull(item.getProduct());
        assertNull(item.getQuantity());
        assertNull(item.getUnitPrice());
        assertNull(item.getSubtotal());
        assertNull(item.getCreatedAt());
    }

    @Test
    @DisplayName("Should create OrderItem with order, product, and quantity")
    void shouldCreateOrderItemWithOrderProductAndQuantity() {
        OrderItem item = new OrderItem(testOrder, testProduct, 5);
        
        assertEquals(testOrder, item.getOrder());
        assertEquals(testProduct, item.getProduct());
        assertEquals(5, item.getQuantity());
        assertEquals(BigDecimal.valueOf(2.50), item.getUnitPrice());
        assertEquals(BigDecimal.valueOf(12.50), item.getSubtotal());
    }

    @Test
    @DisplayName("Should create OrderItem with order, product, quantity, and unit price")
    void shouldCreateOrderItemWithOrderProductQuantityAndUnitPrice() {
        BigDecimal customPrice = BigDecimal.valueOf(3.00);
        OrderItem item = new OrderItem(testOrder, testProduct, 3, customPrice);
        
        assertEquals(testOrder, item.getOrder());
        assertEquals(testProduct, item.getProduct());
        assertEquals(3, item.getQuantity());
        assertEquals(customPrice, item.getUnitPrice());
        assertEquals(BigDecimal.valueOf(9.00), item.getSubtotal());
    }

    @Test
    @DisplayName("Should calculate subtotal correctly")
    void shouldCalculateSubtotalCorrectly() {
        orderItem.setQuantity(4);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        BigDecimal subtotal = orderItem.calculateSubtotal();
        
        assertEquals(BigDecimal.valueOf(10.00), subtotal);
        assertEquals(BigDecimal.valueOf(10.00), orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should handle null values in subtotal calculation")
    void shouldHandleNullValuesInSubtotalCalculation() {
        orderItem.setQuantity(null);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        BigDecimal subtotal = orderItem.calculateSubtotal();
        
        assertEquals(BigDecimal.ZERO, subtotal);
        assertEquals(BigDecimal.ZERO, orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should handle null unit price in subtotal calculation")
    void shouldHandleNullUnitPriceInSubtotalCalculation() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(null);
        
        BigDecimal subtotal = orderItem.calculateSubtotal();
        
        assertEquals(BigDecimal.ZERO, subtotal);
        assertEquals(BigDecimal.ZERO, orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should update quantity successfully")
    void shouldUpdateQuantitySuccessfully() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        orderItem.updateQuantity(10);
        
        assertEquals(10, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(25.00), orderItem.getSubtotal());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("Should throw exception for invalid quantity updates")
    void shouldThrowExceptionForInvalidQuantityUpdates(int invalidQuantity) {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderItem.updateQuantity(invalidQuantity)
        );
        
        assertEquals("Quantity must be at least 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null quantity update")
    void shouldThrowExceptionForNullQuantityUpdate() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderItem.updateQuantity(null)
        );
        
        assertEquals("Quantity must be at least 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should update unit price successfully")
    void shouldUpdateUnitPriceSuccessfully() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        BigDecimal newPrice = BigDecimal.valueOf(3.00);
        orderItem.updateUnitPrice(newPrice);
        
        assertEquals(newPrice, orderItem.getUnitPrice());
        assertEquals(BigDecimal.valueOf(15.00), orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should throw exception for null unit price update")
    void shouldThrowExceptionForNullUnitPriceUpdate() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderItem.updateUnitPrice(null)
        );
        
        assertEquals("Unit price must be greater than 0", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for zero unit price update")
    void shouldThrowExceptionForZeroUnitPriceUpdate() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderItem.updateUnitPrice(BigDecimal.ZERO)
        );
        
        assertEquals("Unit price must be greater than 0", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for negative unit price update")
    void shouldThrowExceptionForNegativeUnitPriceUpdate() {
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderItem.updateUnitPrice(BigDecimal.valueOf(-1.00))
        );
        
        assertEquals("Unit price must be greater than 0", exception.getMessage());
    }

    @Test
    @DisplayName("Should get product name correctly")
    void shouldGetProductNameCorrectly() {
        orderItem.setProduct(testProduct);
        
        assertEquals("Test Apple", orderItem.getProductName());
    }

    @Test
    @DisplayName("Should return null product name when product is null")
    void shouldReturnNullProductNameWhenProductIsNull() {
        orderItem.setProduct(null);
        
        assertNull(orderItem.getProductName());
    }

    @Test
    @DisplayName("Should get product description correctly")
    void shouldGetProductDescriptionCorrectly() {
        orderItem.setProduct(testProduct);
        
        assertEquals("Fresh red apple", orderItem.getProductDescription());
    }

    @Test
    @DisplayName("Should return null product description when product is null")
    void shouldReturnNullProductDescriptionWhenProductIsNull() {
        orderItem.setProduct(null);
        
        assertNull(orderItem.getProductDescription());
    }

    @Test
    @DisplayName("Should get product image URL correctly")
    void shouldGetProductImageUrlCorrectly() {
        testProduct.setImageUrl("http://example.com/apple.jpg");
        orderItem.setProduct(testProduct);
        
        assertEquals("http://example.com/apple.jpg", orderItem.getProductImageUrl());
    }

    @Test
    @DisplayName("Should return null product image URL when product is null")
    void shouldReturnNullProductImageUrlWhenProductIsNull() {
        orderItem.setProduct(null);
        
        assertNull(orderItem.getProductImageUrl());
    }

    @Test
    @DisplayName("Should check if product is active correctly")
    void shouldCheckIfProductIsActiveCorrectly() {
        orderItem.setProduct(testProduct);
        
        assertTrue(orderItem.isProductActive());
        
        testProduct.setIsActive(false);
        assertFalse(orderItem.isProductActive());
    }

    @Test
    @DisplayName("Should return false for product active when product is null")
    void shouldReturnFalseForProductActiveWhenProductIsNull() {
        orderItem.setProduct(null);
        
        assertFalse(orderItem.isProductActive());
    }

    @Test
    @DisplayName("Should get current stock quantity correctly")
    void shouldGetCurrentStockQuantityCorrectly() {
        orderItem.setProduct(testProduct);
        
        assertEquals(100, orderItem.getCurrentStockQuantity());
    }

    @Test
    @DisplayName("Should return 0 for stock quantity when product is null")
    void shouldReturnZeroForStockQuantityWhenProductIsNull() {
        orderItem.setProduct(null);
        
        assertEquals(0, orderItem.getCurrentStockQuantity());
    }

    @Test
    @DisplayName("Should check sufficient stock correctly")
    void shouldCheckSufficientStockCorrectly() {
        orderItem.setProduct(testProduct);
        orderItem.setQuantity(50);
        
        assertTrue(orderItem.hasSufficientStock());
        
        orderItem.setQuantity(150);
        assertFalse(orderItem.hasSufficientStock());
    }

    @Test
    @DisplayName("Should return false for sufficient stock when product is null")
    void shouldReturnFalseForSufficientStockWhenProductIsNull() {
        orderItem.setProduct(null);
        orderItem.setQuantity(5);
        
        assertFalse(orderItem.hasSufficientStock());
    }

    @Test
    @DisplayName("Should check if price is current correctly")
    void shouldCheckIfPriceIsCurrentCorrectly() {
        orderItem.setProduct(testProduct);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        assertTrue(orderItem.isPriceCurrent());
        
        orderItem.setUnitPrice(BigDecimal.valueOf(3.00));
        assertFalse(orderItem.isPriceCurrent());
    }

    @Test
    @DisplayName("Should return false for price current when product is null")
    void shouldReturnFalseForPriceCurrentWhenProductIsNull() {
        orderItem.setProduct(null);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        assertFalse(orderItem.isPriceCurrent());
    }

    @Test
    @DisplayName("Should return false for price current when product price is null")
    void shouldReturnFalseForPriceCurrentWhenProductPriceIsNull() {
        testProduct.setPrice(null);
        orderItem.setProduct(testProduct);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        
        assertFalse(orderItem.isPriceCurrent());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        
        orderItem.setOrderItemId(1L);
        orderItem.setOrder(testOrder);
        orderItem.setProduct(testProduct);
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        orderItem.setSubtotal(BigDecimal.valueOf(12.50));
        orderItem.setCreatedAt(now);
        
        assertEquals(1L, orderItem.getOrderItemId());
        assertEquals(testOrder, orderItem.getOrder());
        assertEquals(testProduct, orderItem.getProduct());
        assertEquals(5, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(2.50), orderItem.getUnitPrice());
        assertEquals(BigDecimal.valueOf(12.50), orderItem.getSubtotal());
        assertEquals(now, orderItem.getCreatedAt());
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        orderItem.setOrderItemId(1L);
        orderItem.setOrder(testOrder);
        orderItem.setProduct(testProduct);
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(BigDecimal.valueOf(2.50));
        orderItem.setSubtotal(BigDecimal.valueOf(12.50));
        orderItem.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        
        String toString = orderItem.toString();
        
        assertTrue(toString.contains("OrderItem{"));
        assertTrue(toString.contains("orderItemId=1"));
        assertTrue(toString.contains("orderId=1"));
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("productName='Test Apple'"));
        assertTrue(toString.contains("quantity=5"));
        assertTrue(toString.contains("unitPrice=2.5"));
        assertTrue(toString.contains("subtotal=12.5"));
    }

    @Test
    @DisplayName("Should have correct equals and hashCode")
    void shouldHaveCorrectEqualsAndHashCode() {
        OrderItem item1 = new OrderItem();
        item1.setOrderItemId(1L);
        
        OrderItem item2 = new OrderItem();
        item2.setOrderItemId(1L);
        
        OrderItem item3 = new OrderItem();
        item3.setOrderItemId(2L);
        
        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    @DisplayName("Should handle null orderItemId in equals")
    void shouldHandleNullOrderItemIdInEquals() {
        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();
        
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Should not equal null")
    void shouldNotEqualNull() {
        OrderItem item = new OrderItem();
        item.setOrderItemId(1L);
        
        assertNotEquals(item, null);
    }

    @Test
    @DisplayName("Should not equal different class")
    void shouldNotEqualDifferentClass() {
        OrderItem item = new OrderItem();
        item.setOrderItemId(1L);
        
        assertNotEquals(item, "not an OrderItem");
    }

    @Test
    @DisplayName("Should handle onCreate lifecycle callback")
    void shouldHandleOnCreateLifecycleCallback() {
        OrderItem item = new OrderItem();
        item.setQuantity(3);
        item.setUnitPrice(BigDecimal.valueOf(2.50));
        
        // Simulate @PrePersist callback
        item.onCreate();
        
        assertNotNull(item.getCreatedAt());
        assertEquals(BigDecimal.valueOf(7.50), item.getSubtotal());
    }

    @Test
    @DisplayName("Should handle onCreate with null subtotal")
    void shouldHandleOnCreateWithNullSubtotal() {
        OrderItem item = new OrderItem();
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(3.00));
        item.setSubtotal(null);
        
        // Simulate @PrePersist callback
        item.onCreate();
        
        assertNotNull(item.getCreatedAt());
        assertEquals(BigDecimal.valueOf(6.00), item.getSubtotal());
    }
}
