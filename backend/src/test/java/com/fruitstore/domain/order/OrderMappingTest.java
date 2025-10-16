package com.fruitstore.domain.order;

import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Order and OrderItem relationships and business logic
 * Tests relationships, calculations, and business methods without JPA
 */
@DisplayName("Order Mapping Tests")
class OrderMappingTest {

    private User testUser;
    private Product testProduct;
    private Order order;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");

        // Create test product
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Test Apple");
        testProduct.setDescription("Fresh red apple");
        testProduct.setPrice(BigDecimal.valueOf(2.50));
        testProduct.setStockQuantity(100);

        // Create test order
        order = new Order();
        order.setOrderId(1L);
        order.setOrderNumber("ORD-001");
        order.setUser(testUser);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should establish Order-OrderItem relationship correctly")
    void shouldEstablishOrderOrderItemRelationshipCorrectly() {
        // Create test order items
        OrderItem orderItem1 = new OrderItem(order, testProduct, 3);
        OrderItem orderItem2 = new OrderItem(order, testProduct, 2);

        // Add order items to order
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        // Verify relationship
        assertEquals(2, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem1));
        assertTrue(order.getOrderItems().contains(orderItem2));
        
        // Verify bidirectional relationship
        assertEquals(order, orderItem1.getOrder());
        assertEquals(order, orderItem2.getOrder());
        assertEquals(testProduct, orderItem1.getProduct());
        assertEquals(testProduct, orderItem2.getProduct());
    }

    @Test
    @DisplayName("Should handle order status transitions correctly")
    void shouldHandleOrderStatusTransitionsCorrectly() {
        // Test status transitions
        assertEquals(OrderStatus.PENDING, order.getStatus());
        
        // Confirm order
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        
        // Ship order
        order.ship();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertNotNull(order.getShippedAt());
        
        // Deliver order
        order.deliver();
        assertNotNull(order.getDeliveredAt());
    }

    @Test
    @DisplayName("Should handle order cancellation correctly")
    void shouldHandleOrderCancellationCorrectly() {
        // Test cancellation from PENDING
        assertEquals(OrderStatus.PENDING, order.getStatus());
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertNotNull(order.getCancelledAt());
        
        // Test cancellation from CONFIRMED
        Order confirmedOrder = new Order();
        confirmedOrder.setUser(testUser);
        confirmedOrder.setStatus(OrderStatus.CONFIRMED);
        confirmedOrder.cancel();
        assertEquals(OrderStatus.CANCELLED, confirmedOrder.getStatus());
        assertNotNull(confirmedOrder.getCancelledAt());
    }

    @Test
    @DisplayName("Should calculate order total amount correctly")
    void shouldCalculateOrderTotalAmountCorrectly() {
        // Create test order items
        OrderItem orderItem1 = new OrderItem(order, testProduct, 3); // 3 * 2.50 = 7.50
        OrderItem orderItem2 = new OrderItem(order, testProduct, 2);   // 2 * 2.50 = 5.00

        // Add order items to order
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        // Verify total amount calculation
        assertEquals(BigDecimal.valueOf(12.50), order.getTotalAmount());
        assertEquals(5, order.getTotalItems());
    }

    @Test
    @DisplayName("Should handle order number generation correctly")
    void shouldHandleOrderNumberGenerationCorrectly() {
        Order order1 = new Order();
        order1.setUser(testUser);
        order1.onCreate(); // Simulate @PrePersist
        
        // Add small delay to ensure different timestamps
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Order order2 = new Order();
        order2.setUser(testUser);
        order2.onCreate(); // Simulate @PrePersist

        // Verify order numbers are generated and unique
        assertNotNull(order1.getOrderNumber());
        assertNotNull(order2.getOrderNumber());
        assertNotEquals(order1.getOrderNumber(), order2.getOrderNumber());
        assertTrue(order1.getOrderNumber().startsWith("ORD-"));
        assertTrue(order2.getOrderNumber().startsWith("ORD-"));
    }

    @Test
    @DisplayName("Should handle order item removal correctly")
    void shouldHandleOrderItemRemovalCorrectly() {
        // Create and add order items
        OrderItem orderItem1 = new OrderItem(order, testProduct, 3);
        OrderItem orderItem2 = new OrderItem(order, testProduct, 2);
        
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);
        
        assertEquals(2, order.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(12.50), order.getTotalAmount());
        
        // Remove one order item by index (since equals() depends on orderItemId)
        order.getOrderItems().remove(0); // Remove first item
        orderItem1.setOrder(null);
        order.recalculateTotalAmount();
        
        assertEquals(1, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem2));
        
        // Since equals() depends on orderItemId (which is null), we can't rely on contains()
        // Instead, verify that the remaining item has the correct properties
        OrderItem remainingItem = order.getOrderItems().get(0);
        assertEquals(2, remainingItem.getQuantity());
        assertEquals(BigDecimal.valueOf(5.00), remainingItem.getSubtotal());
        
        assertEquals(BigDecimal.valueOf(5.00), order.getTotalAmount());
        assertNull(orderItem1.getOrder());
    }

    @Test
    @DisplayName("Should handle order item updates correctly")
    void shouldHandleOrderItemUpdatesCorrectly() {
        // Create and add order item
        OrderItem orderItem = new OrderItem(order, testProduct, 3);
        order.addOrderItem(orderItem);
        
        assertEquals(BigDecimal.valueOf(7.50), order.getTotalAmount());
        
        // Update quantity
        orderItem.updateQuantity(5);
        order.recalculateTotalAmount();
        
        assertEquals(BigDecimal.valueOf(12.50), order.getTotalAmount());
        assertEquals(5, orderItem.getQuantity());
        
        // Update unit price
        orderItem.updateUnitPrice(BigDecimal.valueOf(3.00));
        order.recalculateTotalAmount();
        
        assertEquals(BigDecimal.valueOf(15.00), order.getTotalAmount());
        assertEquals(BigDecimal.valueOf(3.00), orderItem.getUnitPrice());
    }

    @Test
    @DisplayName("Should handle order business logic correctly")
    void shouldHandleOrderBusinessLogicCorrectly() {
        // Test empty order
        assertTrue(order.isEmpty());
        assertEquals(0, order.getTotalItems());
        assertFalse(order.isCompleted());
        assertTrue(order.canBeCancelled());
        
        // Add items
        OrderItem orderItem = new OrderItem(order, testProduct, 2);
        order.addOrderItem(orderItem);
        
        assertFalse(order.isEmpty());
        assertEquals(2, order.getTotalItems());
        assertFalse(order.isCompleted());
        assertTrue(order.canBeCancelled());
        
        // Complete order
        order.confirm();
        order.ship();
        order.deliver();
        
        assertTrue(order.isCompleted());
        assertFalse(order.canBeCancelled());
    }

    @Test
    @DisplayName("Should handle order item business methods correctly")
    void shouldHandleOrderItemBusinessMethodsCorrectly() {
        OrderItem orderItem = new OrderItem(order, testProduct, 5);
        
        // Test product-related methods
        assertEquals("Test Apple", orderItem.getProductName());
        assertEquals("Fresh red apple", orderItem.getProductDescription());
        assertEquals(100, orderItem.getCurrentStockQuantity());
        assertTrue(orderItem.hasSufficientStock());
        assertTrue(orderItem.isPriceCurrent());
        
        // Test with different quantity
        orderItem.setQuantity(150);
        assertFalse(orderItem.hasSufficientStock());
        
        // Test with different price
        orderItem.setUnitPrice(BigDecimal.valueOf(3.00));
        assertFalse(orderItem.isPriceCurrent());
    }

    @Test
    @DisplayName("Should handle order lifecycle callbacks correctly")
    void shouldHandleOrderLifecycleCallbacksCorrectly() {
        Order newOrder = new Order();
        newOrder.setUser(testUser);
        newOrder.setStatus(null);
        newOrder.setOrderNumber(null);
        
        // Simulate @PrePersist callback
        newOrder.onCreate();
        
        assertNotNull(newOrder.getCreatedAt());
        assertNotNull(newOrder.getUpdatedAt());
        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
        assertNotNull(newOrder.getOrderNumber());
        assertTrue(newOrder.getOrderNumber().startsWith("ORD-"));
        
        // Simulate @PreUpdate callback
        LocalDateTime originalUpdatedAt = newOrder.getUpdatedAt();
        newOrder.onUpdate();
        assertTrue(newOrder.getUpdatedAt().isAfter(originalUpdatedAt) || 
                   newOrder.getUpdatedAt().isEqual(originalUpdatedAt));
    }
}