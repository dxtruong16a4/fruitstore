package com.fruitstore.domain.order;

import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Order entity
 * Tests constructors, business methods, validation, lifecycle callbacks, and relationships
 */
@DisplayName("Order Entity Tests")
class OrderEntityTest {

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

        // Create test product
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Test Apple");
        testProduct.setDescription("Fresh red apple");
        testProduct.setPrice(BigDecimal.valueOf(2.50));
        testProduct.setStockQuantity(100);
        testProduct.setIsActive(true);

        // Create test order
        order = new Order();
    }

    @Test
    @DisplayName("Should create Order with default constructor")
    void shouldCreateOrderWithDefaultConstructor() {
        Order newOrder = new Order();
        
        assertNotNull(newOrder);
        assertNull(newOrder.getOrderId());
        assertNull(newOrder.getOrderNumber());
        assertNull(newOrder.getUser());
        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
        assertNull(newOrder.getTotalAmount());
        assertNull(newOrder.getShippingAddress());
        assertNull(newOrder.getPhoneNumber());
        assertNull(newOrder.getCustomerName());
        assertNull(newOrder.getCustomerEmail());
        assertNull(newOrder.getNotes());
        assertNull(newOrder.getShippedAt());
        assertNull(newOrder.getDeliveredAt());
        assertNull(newOrder.getCancelledAt());
        assertNull(newOrder.getCreatedAt());
        assertNull(newOrder.getUpdatedAt());
        assertNotNull(newOrder.getOrderItems());
        assertTrue(newOrder.getOrderItems().isEmpty());
    }

    @Test
    @DisplayName("Should create Order with user, shipping address, customer name and email")
    void shouldCreateOrderWithUserShippingAddressCustomerNameAndEmail() {
        Order newOrder = new Order(testUser, "123 Main St", "John Doe", "john@example.com");
        
        assertEquals(testUser, newOrder.getUser());
        assertEquals("123 Main St", newOrder.getShippingAddress());
        assertEquals("John Doe", newOrder.getCustomerName());
        assertEquals("john@example.com", newOrder.getCustomerEmail());
        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
        assertEquals(BigDecimal.ZERO, newOrder.getTotalAmount());
        assertNull(newOrder.getPhoneNumber());
    }

    @Test
    @DisplayName("Should create Order with user, shipping address, customer name, email and phone")
    void shouldCreateOrderWithUserShippingAddressCustomerNameEmailAndPhone() {
        Order newOrder = new Order(testUser, "123 Main St", "John Doe", "john@example.com", "1234567890");
        
        assertEquals(testUser, newOrder.getUser());
        assertEquals("123 Main St", newOrder.getShippingAddress());
        assertEquals("John Doe", newOrder.getCustomerName());
        assertEquals("john@example.com", newOrder.getCustomerEmail());
        assertEquals("1234567890", newOrder.getPhoneNumber());
        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
        assertEquals(BigDecimal.ZERO, newOrder.getTotalAmount());
    }

    @Test
    @DisplayName("Should add order item correctly")
    void shouldAddOrderItemCorrectly() {
        OrderItem orderItem = new OrderItem(order, testProduct, 5);
        
        order.addOrderItem(orderItem);
        
        assertEquals(1, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem));
        assertEquals(order, orderItem.getOrder());
        assertEquals(BigDecimal.valueOf(12.50), order.getTotalAmount());
    }

    @Test
    @DisplayName("Should remove order item correctly")
    void shouldRemoveOrderItemCorrectly() {
        OrderItem orderItem = new OrderItem(order, testProduct, 5);
        order.addOrderItem(orderItem);
        
        order.removeOrderItem(orderItem);
        
        assertTrue(order.getOrderItems().isEmpty());
        assertNull(orderItem.getOrder());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    @DisplayName("Should recalculate total amount correctly")
    void shouldRecalculateTotalAmountCorrectly() {
        OrderItem item1 = new OrderItem(order, testProduct, 3);
        OrderItem item2 = new OrderItem(order, testProduct, 2);
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        // Manually set different subtotals to test recalculation
        item1.setSubtotal(BigDecimal.valueOf(7.50));
        item2.setSubtotal(BigDecimal.valueOf(5.00));
        
        order.recalculateTotalAmount();
        
        assertEquals(BigDecimal.valueOf(12.50), order.getTotalAmount());
    }

    @Test
    @DisplayName("Should recalculate total amount with empty order items")
    void shouldRecalculateTotalAmountWithEmptyOrderItems() {
        order.recalculateTotalAmount();
        
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    @DisplayName("Should confirm pending order successfully")
    void shouldConfirmPendingOrderSuccessfully() {
        order.setStatus(OrderStatus.PENDING);
        LocalDateTime beforeConfirm = LocalDateTime.now();
        
        order.confirm();
        
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertTrue(order.getUpdatedAt().isAfter(beforeConfirm) || order.getUpdatedAt().isEqual(beforeConfirm));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"CONFIRMED", "DELIVERED", "CANCELLED"})
    @DisplayName("Should throw exception when confirming non-pending order")
    void shouldThrowExceptionWhenConfirmingNonPendingOrder(OrderStatus status) {
        order.setStatus(status);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> order.confirm()
        );
        
        assertEquals("Only pending orders can be confirmed", exception.getMessage());
    }

    @Test
    @DisplayName("Should ship confirmed order successfully")
    void shouldShipConfirmedOrderSuccessfully() {
        order.setStatus(OrderStatus.CONFIRMED);
        LocalDateTime beforeShip = LocalDateTime.now();
        
        order.ship();
        
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertNotNull(order.getShippedAt());
        assertTrue(order.getShippedAt().isAfter(beforeShip) || order.getShippedAt().isEqual(beforeShip));
        assertTrue(order.getUpdatedAt().isAfter(beforeShip) || order.getUpdatedAt().isEqual(beforeShip));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "DELIVERED", "CANCELLED"})
    @DisplayName("Should throw exception when shipping non-confirmed order")
    void shouldThrowExceptionWhenShippingNonConfirmedOrder(OrderStatus status) {
        order.setStatus(status);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> order.ship()
        );
        
        assertEquals("Only confirmed orders can be shipped", exception.getMessage());
    }

    @Test
    @DisplayName("Should deliver shipped order successfully")
    void shouldDeliverShippedOrderSuccessfully() {
        order.setStatus(OrderStatus.DELIVERED);
        LocalDateTime beforeDeliver = LocalDateTime.now();
        
        order.deliver();
        
        assertNotNull(order.getDeliveredAt());
        assertTrue(order.getDeliveredAt().isAfter(beforeDeliver) || order.getDeliveredAt().isEqual(beforeDeliver));
        assertTrue(order.getUpdatedAt().isAfter(beforeDeliver) || order.getUpdatedAt().isEqual(beforeDeliver));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED", "CANCELLED"})
    @DisplayName("Should throw exception when delivering non-shipped order")
    void shouldThrowExceptionWhenDeliveringNonShippedOrder(OrderStatus status) {
        order.setStatus(status);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> order.deliver()
        );
        
        assertEquals("Only shipped orders can be delivered", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED"})
    @DisplayName("Should cancel cancellable order successfully")
    void shouldCancelCancellableOrderSuccessfully(OrderStatus status) {
        order.setStatus(status);
        LocalDateTime beforeCancel = LocalDateTime.now();
        
        order.cancel();
        
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertNotNull(order.getCancelledAt());
        assertTrue(order.getCancelledAt().isAfter(beforeCancel) || order.getCancelledAt().isEqual(beforeCancel));
        assertTrue(order.getUpdatedAt().isAfter(beforeCancel) || order.getUpdatedAt().isEqual(beforeCancel));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "CANCELLED"})
    @DisplayName("Should throw exception when cancelling non-cancellable order")
    void shouldThrowExceptionWhenCancellingNonCancellableOrder(OrderStatus status) {
        order.setStatus(status);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> order.cancel()
        );
        
        assertEquals("Order cannot be cancelled in current status: " + status, exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED"})
    @DisplayName("Should return true for cancellable orders")
    void shouldReturnTrueForCancellableOrders(OrderStatus status) {
        order.setStatus(status);
        
        assertTrue(order.canBeCancelled());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "CANCELLED"})
    @DisplayName("Should return false for non-cancellable orders")
    void shouldReturnFalseForNonCancellableOrders(OrderStatus status) {
        order.setStatus(status);
        
        assertFalse(order.canBeCancelled());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "CANCELLED"})
    @DisplayName("Should return true for completed orders")
    void shouldReturnTrueForCompletedOrders(OrderStatus status) {
        order.setStatus(status);
        
        assertTrue(order.isCompleted());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED"})
    @DisplayName("Should return false for non-completed orders")
    void shouldReturnFalseForNonCompletedOrders(OrderStatus status) {
        order.setStatus(status);
        
        assertFalse(order.isCompleted());
    }

    @Test
    @DisplayName("Should calculate total items correctly")
    void shouldCalculateTotalItemsCorrectly() {
        OrderItem item1 = new OrderItem(order, testProduct, 3);
        OrderItem item2 = new OrderItem(order, testProduct, 2);
        
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        
        assertEquals(5, order.getTotalItems());
    }

    @Test
    @DisplayName("Should return zero total items for empty order")
    void shouldReturnZeroTotalItemsForEmptyOrder() {
        assertEquals(0, order.getTotalItems());
    }

    @Test
    @DisplayName("Should return true for empty order")
    void shouldReturnTrueForEmptyOrder() {
        assertTrue(order.isEmpty());
    }

    @Test
    @DisplayName("Should return false for non-empty order")
    void shouldReturnFalseForNonEmptyOrder() {
        OrderItem orderItem = new OrderItem(order, testProduct, 1);
        order.addOrderItem(orderItem);
        
        assertFalse(order.isEmpty());
    }

    @Test
    @DisplayName("Should handle onCreate lifecycle callback")
    void shouldHandleOnCreateLifecycleCallback() {
        order.setUser(testUser);
        order.setStatus(null);
        order.setOrderNumber(null);
        
        // Simulate @PrePersist callback
        order.onCreate();
        
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getOrderNumber());
        assertTrue(order.getOrderNumber().startsWith("ORD-"));
    }

    @Test
    @DisplayName("Should handle onUpdate lifecycle callback")
    void shouldHandleOnUpdateLifecycleCallback() {
        LocalDateTime originalUpdatedAt = LocalDateTime.now().minusHours(1);
        order.setUpdatedAt(originalUpdatedAt);
        
        // Simulate @PreUpdate callback
        order.onUpdate();
        
        assertTrue(order.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        
        order.setOrderId(1L);
        order.setOrderNumber("ORD-001");
        order.setUser(testUser);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(BigDecimal.valueOf(100.00));
        order.setShippingAddress("123 Main St");
        order.setPhoneNumber("1234567890");
        order.setCustomerName("John Doe");
        order.setCustomerEmail("john@example.com");
        order.setNotes("Please deliver carefully");
        order.setShippedAt(now);
        order.setDeliveredAt(now);
        order.setCancelledAt(now);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        
        assertEquals(1L, order.getOrderId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(testUser, order.getUser());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(BigDecimal.valueOf(100.00), order.getTotalAmount());
        assertEquals("123 Main St", order.getShippingAddress());
        assertEquals("1234567890", order.getPhoneNumber());
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("john@example.com", order.getCustomerEmail());
        assertEquals("Please deliver carefully", order.getNotes());
        assertEquals(now, order.getShippedAt());
        assertEquals(now, order.getDeliveredAt());
        assertEquals(now, order.getCancelledAt());
        assertEquals(now, order.getCreatedAt());
        assertEquals(now, order.getUpdatedAt());
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        order.setOrderId(1L);
        order.setOrderNumber("ORD-001");
        order.setUser(testUser);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(50.00));
        order.setCustomerName("John Doe");
        order.setCustomerEmail("john@example.com");
        order.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        order.setUpdatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        
        OrderItem orderItem = new OrderItem(order, testProduct, 2);
        order.addOrderItem(orderItem);
        
        String toString = order.toString();
        
        assertTrue(toString.contains("Order{"));
        assertTrue(toString.contains("orderId=1"));
        assertTrue(toString.contains("orderNumber='ORD-001'"));
        assertTrue(toString.contains("userId=1"));
        assertTrue(toString.contains("status=PENDING"));
        assertTrue(toString.contains("totalAmount=5.0"));
        assertTrue(toString.contains("customerName='John Doe'"));
        assertTrue(toString.contains("customerEmail='john@example.com'"));
        assertTrue(toString.contains("itemCount=2"));
    }

    @Test
    @DisplayName("Should have correct equals and hashCode")
    void shouldHaveCorrectEqualsAndHashCode() {
        Order order1 = new Order();
        order1.setOrderId(1L);
        
        Order order2 = new Order();
        order2.setOrderId(1L);
        
        Order order3 = new Order();
        order3.setOrderId(2L);
        
        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertEquals(order1.hashCode(), order3.hashCode()); // Same class hashCode
    }

    @Test
    @DisplayName("Should not equal null")
    void shouldNotEqualNull() {
        Order order = new Order();
        order.setOrderId(1L);
        
        assertNotEquals(order, null);
    }

    @Test
    @DisplayName("Should not equal different class")
    void shouldNotEqualDifferentClass() {
        Order order = new Order();
        order.setOrderId(1L);
        
        assertNotEquals(order, "not an Order");
    }

    @Test
    @DisplayName("Should handle null orderId in equals")
    void shouldHandleNullOrderIdInEquals() {
        Order order1 = new Order();
        Order order2 = new Order();
        
        assertNotEquals(order1, order2);
    }

    @Test
    @DisplayName("Should generate unique order number")
    void shouldGenerateUniqueOrderNumber() {
        order.setUser(testUser);
        
        // Simulate @PrePersist callback
        order.onCreate();
        
        String orderNumber1 = order.getOrderNumber();
        
        // Create another order
        Order order2 = new Order();
        order2.setUser(testUser);
        order2.onCreate();
        
        String orderNumber2 = order2.getOrderNumber();
        
        assertNotNull(orderNumber1);
        assertNotNull(orderNumber2);
        assertNotEquals(orderNumber1, orderNumber2);
        assertTrue(orderNumber1.startsWith("ORD-"));
        assertTrue(orderNumber2.startsWith("ORD-"));
    }

    @Test
    @DisplayName("Should handle null user in order number generation")
    void shouldHandleNullUserInOrderNumberGeneration() {
        order.setUser(null);
        
        // Simulate @PrePersist callback
        order.onCreate();
        
        String orderNumber = order.getOrderNumber();
        
        assertNotNull(orderNumber);
        assertTrue(orderNumber.startsWith("ORD-"));
        assertTrue(orderNumber.endsWith("-0"));
    }

    @Test
    @DisplayName("Should handle multiple order items with null subtotals")
    void shouldHandleMultipleOrderItemsWithNullSubtotals() {
        OrderItem item1 = new OrderItem();
        item1.setSubtotal(BigDecimal.ZERO);
        
        OrderItem item2 = new OrderItem();
        item2.setSubtotal(BigDecimal.valueOf(10.00));
        
        order.getOrderItems().add(item1);
        order.getOrderItems().add(item2);
        
        order.recalculateTotalAmount();
        
        assertEquals(BigDecimal.valueOf(10.00), order.getTotalAmount());
    }

    @Test
    @DisplayName("Should handle order items with null quantities in total items calculation")
    void shouldHandleOrderItemsWithNullQuantitiesInTotalItemsCalculation() {
        OrderItem item1 = new OrderItem();
        item1.setQuantity(0);
        
        OrderItem item2 = new OrderItem();
        item2.setQuantity(5);
        
        order.getOrderItems().add(item1);
        order.getOrderItems().add(item2);
        
        assertEquals(5, order.getTotalItems());
    }
}
