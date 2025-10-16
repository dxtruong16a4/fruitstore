package com.fruitstore.service;

import com.fruitstore.domain.cart.Cart;
import com.fruitstore.domain.cart.CartItem;
import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountType;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.order.OrderItem;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.dto.request.order.CreateOrderRequest;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.repository.CartRepository;
import com.fruitstore.repository.DiscountRepository;
import com.fruitstore.repository.OrderItemRepository;
import com.fruitstore.repository.OrderRepository;
import com.fruitstore.repository.ProductRepository;
import com.fruitstore.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService discount integration
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceDiscountIntegrationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;
    private Discount testDiscount;
    private CreateOrderRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User("testuser", "test@example.com", "password123", "Test User");
        testUser.setUserId(1L);

        // Setup test product
        testProduct = new Product("Test Product", "Test Description", new BigDecimal("10.00"));
        testProduct.setProductId(1L);
        testProduct.setStockQuantity(100);

        // Setup test cart
        testCart = new Cart(testUser);
        testCart.setCartId(1L);
        testCartItem = new CartItem(testCart, testProduct, 2);
        testCartItem.setCartItemId(1L);
        testCart.addCartItem(testCartItem);

        // Setup test discount
        testDiscount = new Discount();
        testDiscount.setDiscountId(1L);
        testDiscount.setCode("TEST10");
        testDiscount.setDiscountType(DiscountType.PERCENTAGE);
        testDiscount.setDiscountValue(new BigDecimal("10.00"));
        testDiscount.setMinOrderAmount(new BigDecimal("15.00"));
        testDiscount.setMaxDiscountAmount(new BigDecimal("5.00"));
        testDiscount.setUsageLimit(100);
        testDiscount.setUsedCount(0);
        testDiscount.setStartDate(LocalDateTime.now().minusDays(1));
        testDiscount.setEndDate(LocalDateTime.now().plusDays(30));
        testDiscount.setIsActive(true);
        testDiscount.setDescription("Test discount");

        // Setup test request
        testRequest = new CreateOrderRequest();
        testRequest.setShippingAddress("123 Test St");
        testRequest.setCustomerName("Test Customer");
        testRequest.setCustomerEmail("customer@example.com");
        testRequest.setPhoneNumber("1234567890");
        testRequest.setNotes("Test order");
        testRequest.setDiscountCode("TEST10");
    }

    @Test
    void createOrder_WithValidDiscount_ShouldApplyDiscountAndRecordUsage() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));
        
        // Mock discount validation
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                true, "TEST10", "Valid discount", DiscountType.PERCENTAGE,
                new BigDecimal("10.00"), new BigDecimal("2.00"), new BigDecimal("15.00"),
                new BigDecimal("5.00"), 100, 0, 100, "Test discount"
        );
        when(discountService.validateDiscount("TEST10", new BigDecimal("20.00")))
                .thenReturn(validationResponse);
        when(discountRepository.findByCodeIgnoreCase("TEST10"))
                .thenReturn(Optional.of(testDiscount));
        when(discountService.applyDiscount("TEST10", new BigDecimal("20.00")))
                .thenReturn(new BigDecimal("2.00"));

        // Mock order creation
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("18.00")); // 20.00 - 2.00 discount
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        OrderResponse result = orderService.createOrder(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(discountService).validateDiscount("TEST10", new BigDecimal("20.00"));
        verify(discountService).applyDiscount("TEST10", new BigDecimal("20.00"));
        verify(discountService).recordDiscountUsage(1L, 1L, 1L, new BigDecimal("2.00"));
        verify(productRepository).save(testProduct); // Stock should be updated
        verify(cartRepository).save(testCart); // Cart should be cleared
    }

    @Test
    void createOrder_WithInvalidDiscount_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));
        
        // Mock order creation (needed before discount validation)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // Mock invalid discount validation
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                false, "INVALID", "Discount not found"
        );
        when(discountService.validateDiscount("INVALID", new BigDecimal("20.00")))
                .thenReturn(validationResponse);

        testRequest.setDiscountCode("INVALID");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(1L, testRequest)
        );
        
        assertTrue(exception.getMessage().contains("Invalid discount code"));
        assertTrue(exception.getMessage().contains("Discount not found"));
        
        verify(discountService).validateDiscount("INVALID", new BigDecimal("20.00"));
        verify(discountService, never()).applyDiscount(anyString(), any(BigDecimal.class));
        verify(discountService, never()).recordDiscountUsage(anyLong(), anyLong(), anyLong(), any(BigDecimal.class));
    }

    @Test
    void createOrder_WithExpiredDiscount_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));
        
        // Mock order creation (needed before discount validation)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // Mock expired discount validation
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                false, "EXPIRED", "Discount has expired"
        );
        when(discountService.validateDiscount("EXPIRED", new BigDecimal("20.00")))
                .thenReturn(validationResponse);

        testRequest.setDiscountCode("EXPIRED");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(1L, testRequest)
        );
        
        assertTrue(exception.getMessage().contains("Invalid discount code"));
        assertTrue(exception.getMessage().contains("Discount has expired"));
        
        verify(discountService).validateDiscount("EXPIRED", new BigDecimal("20.00"));
        verify(discountService, never()).applyDiscount(anyString(), any(BigDecimal.class));
    }

    @Test
    void createOrder_WithDiscountBelowMinimumOrder_ShouldThrowException() {
        // Arrange
        // Create a cart with lower total amount
        testCartItem.setQuantity(1); // 1 * 10.00 = 10.00 (below 15.00 minimum)
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));
        
        // Mock order creation (needed before discount validation)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // Mock discount validation with minimum order amount not met
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                false, "TEST10", "Order amount must be at least $15.00"
        );
        when(discountService.validateDiscount("TEST10", new BigDecimal("10.00")))
                .thenReturn(validationResponse);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(1L, testRequest)
        );
        
        assertTrue(exception.getMessage().contains("Invalid discount code"));
        assertTrue(exception.getMessage().contains("Order amount must be at least $15.00"));
        
        verify(discountService).validateDiscount("TEST10", new BigDecimal("10.00"));
        verify(discountService, never()).applyDiscount(anyString(), any(BigDecimal.class));
    }

    @Test
    void createOrder_WithoutDiscount_ShouldCreateOrderNormally() {
        // Arrange
        testRequest.setDiscountCode(null);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));

        // Mock order creation
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("20.00")); // No discount applied
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        OrderResponse result = orderService.createOrder(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(discountService, never()).validateDiscount(anyString(), any(BigDecimal.class));
        verify(discountService, never()).applyDiscount(anyString(), any(BigDecimal.class));
        verify(discountService, never()).recordDiscountUsage(anyLong(), anyLong(), anyLong(), any(BigDecimal.class));
        verify(productRepository).save(testProduct); // Stock should be updated
        verify(cartRepository).save(testCart); // Cart should be cleared
    }

    @Test
    void createOrder_WithEmptyDiscountCode_ShouldCreateOrderNormally() {
        // Arrange
        testRequest.setDiscountCode("   "); // Empty/whitespace discount code
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));

        // Mock order creation
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("20.00")); // No discount applied
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        OrderResponse result = orderService.createOrder(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(discountService, never()).validateDiscount(anyString(), any(BigDecimal.class));
        verify(discountService, never()).applyDiscount(anyString(), any(BigDecimal.class));
        verify(discountService, never()).recordDiscountUsage(anyLong(), anyLong(), anyLong(), any(BigDecimal.class));
    }

    @Test
    void createOrder_WithFixedAmountDiscount_ShouldApplyCorrectDiscount() {
        // Arrange
        // Create a fixed amount discount
        testDiscount.setDiscountType(DiscountType.FIXED_AMOUNT);
        testDiscount.setDiscountValue(new BigDecimal("5.00"));
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));
        
        // Mock discount validation
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                true, "FIXED5", "Valid discount", DiscountType.FIXED_AMOUNT,
                new BigDecimal("5.00"), new BigDecimal("5.00"), new BigDecimal("15.00"),
                null, 100, 0, 100, "Fixed amount discount"
        );
        when(discountService.validateDiscount("FIXED5", new BigDecimal("20.00")))
                .thenReturn(validationResponse);
        when(discountRepository.findByCodeIgnoreCase("FIXED5"))
                .thenReturn(Optional.of(testDiscount));
        when(discountService.applyDiscount("FIXED5", new BigDecimal("20.00")))
                .thenReturn(new BigDecimal("5.00"));

        testRequest.setDiscountCode("FIXED5");

        // Mock order creation
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("15.00")); // 20.00 - 5.00 discount
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        OrderResponse result = orderService.createOrder(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(discountService).validateDiscount("FIXED5", new BigDecimal("20.00"));
        verify(discountService).applyDiscount("FIXED5", new BigDecimal("20.00"));
        verify(discountService).recordDiscountUsage(1L, 1L, 1L, new BigDecimal("5.00"));
    }

    @Test
    void validateDiscountForOrder_ShouldReturnValidationResponse() {
        // Arrange
        DiscountValidationResponse expectedResponse = new DiscountValidationResponse(
                true, "TEST10", "Valid discount", DiscountType.PERCENTAGE,
                new BigDecimal("10.00"), new BigDecimal("2.00"), new BigDecimal("15.00"),
                new BigDecimal("5.00"), 100, 0, 100, "Test discount"
        );
        when(discountService.validateDiscount("TEST10", new BigDecimal("20.00")))
                .thenReturn(expectedResponse);

        // Act
        DiscountValidationResponse result = orderService.validateDiscountForOrder("TEST10", new BigDecimal("20.00"));

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        assertTrue(result.isValid());
        assertEquals("TEST10", result.getCode());
        verify(discountService).validateDiscount("TEST10", new BigDecimal("20.00"));
    }

    @Test
    void createOrderWithDiscount_ShouldCallCreateOrder() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(testCart));

        // Mock discount validation
        DiscountValidationResponse validationResponse = new DiscountValidationResponse(
                true, "TEST10", "Valid discount", DiscountType.PERCENTAGE,
                new BigDecimal("10.00"), new BigDecimal("2.00"), new BigDecimal("15.00"),
                new BigDecimal("5.00"), 100, 0, 100, "Test discount"
        );
        when(discountService.validateDiscount("TEST10", new BigDecimal("20.00")))
                .thenReturn(validationResponse);
        when(discountRepository.findByCodeIgnoreCase("TEST10"))
                .thenReturn(Optional.of(testDiscount));
        when(discountService.applyDiscount("TEST10", new BigDecimal("20.00")))
                .thenReturn(new BigDecimal("2.00"));

        // Mock order creation
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setTotalAmount(new BigDecimal("20.00"));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem orderItem = invocation.getArgument(0);
            orderItem.setOrderItemId(1L);
            return orderItem;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        OrderResponse result = orderService.createOrderWithDiscount(1L, testRequest);

        // Assert
        assertNotNull(result);
        // This method should just call createOrder internally
        verify(orderRepository, times(2)).save(any(Order.class)); // Called twice: once for initial save, once for final save
    }
}
