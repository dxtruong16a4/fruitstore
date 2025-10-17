package com.fruitstore.service;

import com.fruitstore.domain.cart.Cart;
import com.fruitstore.domain.cart.CartItem;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.order.OrderItem;
import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.dto.request.order.CreateOrderRequest;
import com.fruitstore.dto.request.order.UpdateOrderStatusRequest;
import com.fruitstore.dto.response.order.OrderItemResponse;
import com.fruitstore.dto.response.order.OrderListResponse;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.dto.response.order.OrderSummaryResponse;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.dto.response.product.ProductSummaryResponse;
import com.fruitstore.dto.response.user.UserSummaryResponse;
import com.fruitstore.repository.CartRepository;
import com.fruitstore.repository.DiscountRepository;
import com.fruitstore.repository.OrderItemRepository;
import com.fruitstore.repository.OrderRepository;
import com.fruitstore.repository.ProductRepository;
import com.fruitstore.repository.UserRepository;
import com.fruitstore.domain.discount.Discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for order management
 * Handles order operations, business logic, and transaction management
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final DiscountRepository discountRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                       CartRepository cartRepository, CartService cartService, UserRepository userRepository, 
                       ProductRepository productRepository, DiscountService discountService,
                       DiscountRepository discountRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
        this.discountRepository = discountRepository;
    }

    /**
     * Create a new order from cart
     * 
     * @param userId the user ID
     * @param request the order creation request
     * @return created order response
     */
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Get user's cart (this will create cart if it doesn't exist)
        Cart cart = cartRepository.findByUser_UserIdWithItemsAndProducts(userId)
                .orElseGet(() -> {
                    // If cart doesn't exist, create it using CartService
                    cartService.getCartByUserId(userId);
                    return cartRepository.findByUser_UserIdWithItemsAndProducts(userId)
                            .orElseThrow(() -> new IllegalArgumentException("Failed to create cart for user: " + userId));
                });

        
        // Validate cart is not empty
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cannot create order from empty cart");
        }

        // Validate all cart items have sufficient stock
        for (CartItem cartItem : cart.getCartItems()) {
            if (!cartItem.hasSufficientStock()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + cartItem.getProduct().getName());
            }
        }

        // Calculate total amount from cart items first
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            BigDecimal subtotal = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        // Create order with calculated total amount
        Order order = new Order(user, request.getShippingAddress(), request.getCustomerName(), 
                               request.getCustomerEmail(), request.getPhoneNumber());
        order.setNotes(request.getNotes());
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        // Create order items from cart items
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItem = orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);

            // Update product stock
            cartItem.getProduct().reduceStock(cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());
        }

        // Apply discount if provided
        BigDecimal discountAmount = BigDecimal.ZERO;
        Discount appliedDiscount = null;
        if (request.getDiscountCode() != null && !request.getDiscountCode().trim().isEmpty()) {
            try {
                // Validate discount first
                DiscountValidationResponse validation = discountService.validateDiscount(request.getDiscountCode(), totalAmount);
                if (validation.isValid()) {
                    // Get the discount entity
                    appliedDiscount = discountRepository.findByCodeIgnoreCase(request.getDiscountCode())
                            .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
                    discountAmount = discountService.applyDiscount(request.getDiscountCode(), totalAmount);
                    // Record discount usage
                    discountService.recordDiscountUsage(appliedDiscount.getDiscountId(), userId, order.getOrderId(), discountAmount);
                } else {
                    throw new IllegalArgumentException(validation.getMessage());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid discount code: " + e.getMessage());
            }
        }

        // Calculate final amount after discount
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        // Set total amount and save order
        order.setTotalAmount(finalAmount);
        order = orderRepository.save(order);

        // Clear cart after successful order creation
        cart.clearCart();
        cartRepository.save(cart);

        return mapToOrderResponse(order);
    }

    /**
     * Create a new order with discount code validation
     * 
     * @param userId the user ID
     * @param request the order creation request with discount code
     * @return created order response
     */
    @Transactional
    public OrderResponse createOrderWithDiscount(Long userId, CreateOrderRequest request) {
        return createOrder(userId, request);
    }

    /**
     * Validate discount code for order
     * 
     * @param discountCode the discount code to validate
     * @param orderAmount the order amount
     * @return discount validation response
     */
    @Transactional(readOnly = true)
    public DiscountValidationResponse validateDiscountForOrder(String discountCode, BigDecimal orderAmount) {
        return discountService.validateDiscount(discountCode, orderAmount);
    }

    /**
     * Get order by ID
     * 
     * @param orderId the order ID
     * @return order response
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        return mapToOrderResponse(order);
    }

    /**
     * Get order by ID for specific user
     * 
     * @param orderId the order ID
     * @param userId the user ID
     * @return order response
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        
        if (!order.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user: " + userId);
        }
        
        return mapToOrderResponse(order);
    }

    /**
     * Get orders by user ID with pagination
     * 
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of orders for the user
     */
    @Transactional(readOnly = true)
    public OrderListResponse getOrdersByUser(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUser_UserIdWithItems(userId, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get all orders with pagination (admin)
     * 
     * @param pageable pagination information
     * @return page of all orders
     */
    @Transactional(readOnly = true)
    public OrderListResponse getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get orders with filters (admin)
     * 
     * @param userId the user ID filter (can be null)
     * @param status the order status filter (can be null)
     * @param minAmount the minimum total amount filter (can be null)
     * @param maxAmount the maximum total amount filter (can be null)
     * @param customerName the customer name filter (can be null)
     * @param customerEmail the customer email filter (can be null)
     * @param startDate the start date filter (can be null)
     * @param endDate the end date filter (can be null)
     * @param pageable pagination information
     * @return page of filtered orders
     */
    @Transactional(readOnly = true)
    public OrderListResponse getOrdersWithFilters(Long userId, OrderStatus status, BigDecimal minAmount, 
                                                BigDecimal maxAmount, String customerName, String customerEmail,
                                                LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersWithFilters(userId, status, minAmount, maxAmount, 
                                                                  customerName, customerEmail, startDate, endDate, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Update order status (admin)
     * 
     * @param orderId the order ID
     * @param request the status update request
     * @return updated order response
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Validate status transition
        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        // Update status
        order.setStatus(newStatus);
        
        // Set specific timestamps based on status
        switch (newStatus) {
            case PENDING:
                // No action needed for pending
                break;
            case CONFIRMED:
                order.confirm();
                break;
            case SHIPPED:
                order.ship();
                break;
            case DELIVERED:
                order.deliver();
                break;
            case CANCELLED:
                order.cancel();
                break;
        }

        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    /**
     * Cancel order
     * 
     * @param orderId the order ID
     * @return cancelled order response
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.canBeCancelled()) {
            throw new IllegalArgumentException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        order.cancel();
        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    /**
     * Cancel order for specific user
     * 
     * @param orderId the order ID
     * @param userId the user ID
     * @return cancelled order response
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user: " + userId);
        }

        if (!order.canBeCancelled()) {
            throw new IllegalArgumentException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        order.cancel();
        order = orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    /**
     * Get order by order number
     * 
     * @param orderNumber the order number
     * @return order response
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with number: " + orderNumber));
        return mapToOrderResponse(order);
    }

    /**
     * Get order by order number for specific user
     * 
     * @param orderNumber the order number
     * @param userId the user ID
     * @return order response
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with number: " + orderNumber));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user: " + userId);
        }

        return mapToOrderResponse(order);
    }

    /**
     * Get orders by status
     * 
     * @param status the order status
     * @param pageable pagination information
     * @return page of orders with the specified status
     */
    @Transactional(readOnly = true)
    public OrderListResponse getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get orders by user ID and status
     * 
     * @param userId the user ID
     * @param status the order status
     * @param pageable pagination information
     * @return page of orders matching the criteria
     */
    @Transactional(readOnly = true)
    public OrderListResponse getOrdersByUserAndStatus(Long userId, OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUser_UserIdAndStatus(userId, status, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get recent orders (last N days)
     * 
     * @param days the number of days
     * @param pageable pagination information
     * @return page of recent orders
     */
    @Transactional(readOnly = true)
    public OrderListResponse getRecentOrders(int days, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        Page<Order> orders = orderRepository.findRecentOrders(startDate, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get recent orders for user (last N days)
     * 
     * @param userId the user ID
     * @param days the number of days
     * @param pageable pagination information
     * @return page of recent orders for the user
     */
    @Transactional(readOnly = true)
    public OrderListResponse getRecentOrdersByUser(Long userId, int days, Pageable pageable) {
        // For now, return all user orders since we don't have date filtering in repository
        // TODO: Add date filtering to repository method
        Page<Order> orders = orderRepository.findByUser_UserId(userId, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get orders that can be cancelled
     * 
     * @param pageable pagination information
     * @return page of orders that can be cancelled
     */
    @Transactional(readOnly = true)
    public OrderListResponse getCancellableOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findCancellableOrders(pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get orders that can be cancelled for specific user
     * 
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of orders that can be cancelled by the user
     */
    @Transactional(readOnly = true)
    public OrderListResponse getCancellableOrdersByUser(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findCancellableOrdersByUser(userId, pageable);
        return mapToOrderListResponse(orders);
    }

    /**
     * Get order statistics
     * 
     * @return order statistics
     */
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics() {
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long confirmedOrders = orderRepository.countByStatus(OrderStatus.CONFIRMED);
        long deliveredOrders = orderRepository.countByStatus(OrderStatus.DELIVERED);
        long cancelledOrders = orderRepository.countByStatus(OrderStatus.CANCELLED);

        // Calculate total revenue from delivered orders
        BigDecimal totalRevenue = orderRepository.findByStatusOrderByCreatedAtDesc(OrderStatus.DELIVERED)
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = totalOrders > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;

        return new OrderStatistics(totalOrders, pendingOrders, confirmedOrders, 
                                 deliveredOrders, cancelledOrders, totalRevenue, averageOrderValue);
    }

    /**
     * Get order statistics for user
     * 
     * @param userId the user ID
     * @return order statistics for the user
     */
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatisticsByUser(Long userId) {
        long totalOrders = orderRepository.countByUser_UserId(userId);
        long pendingOrders = orderRepository.countByUser_UserIdAndStatus(userId, OrderStatus.PENDING);
        long confirmedOrders = orderRepository.countByUser_UserIdAndStatus(userId, OrderStatus.CONFIRMED);
        long deliveredOrders = orderRepository.countByUser_UserIdAndStatus(userId, OrderStatus.DELIVERED);
        long cancelledOrders = orderRepository.countByUser_UserIdAndStatus(userId, OrderStatus.CANCELLED);

        // Calculate total revenue from delivered orders for user
        BigDecimal totalRevenue = orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.DELIVERED)
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = totalOrders > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;

        return new OrderStatistics(totalOrders, pendingOrders, confirmedOrders, 
                                 deliveredOrders, cancelledOrders, totalRevenue, averageOrderValue);
    }

    /**
     * Check if order can be cancelled
     * 
     * @param orderId the order ID
     * @return true if order can be cancelled
     */
    @Transactional(readOnly = true)
    public boolean canCancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null && order.canBeCancelled();
    }

    /**
     * Check if order can be cancelled by user
     * 
     * @param orderId the order ID
     * @param userId the user ID
     * @return true if order can be cancelled by the user
     */
    @Transactional(readOnly = true)
    public boolean canCancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null && order.getUser().getUserId().equals(userId) && order.canBeCancelled();
    }

    /**
     * Get order count by status
     * 
     * @param status the order status
     * @return number of orders with the specified status
     */
    @Transactional(readOnly = true)
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * Get order count by user
     * 
     * @param userId the user ID
     * @return number of orders for the user
     */
    @Transactional(readOnly = true)
    public long getOrderCountByUser(Long userId) {
        return orderRepository.countByUser_UserId(userId);
    }

    /**
     * Get order count by user and status
     * 
     * @param userId the user ID
     * @param status the order status
     * @return number of orders matching the criteria
     */
    @Transactional(readOnly = true)
    public long getOrderCountByUserAndStatus(Long userId, OrderStatus status) {
        return orderRepository.countByUser_UserIdAndStatus(userId, status);
    }

    // Helper methods

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus newStatus) {
        switch (current) {
            case PENDING:
                return newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELLED;
            case DELIVERED:
                return false; // Final state
            case CANCELLED:
                return false; // Final state
            default:
                return false;
        }
    }

    private OrderListResponse mapToOrderListResponse(Page<Order> orders) {
        List<OrderSummaryResponse> orderSummaries = orders.getContent().stream()
                .map(this::mapToOrderSummaryResponse)
                .collect(Collectors.toList());

        return OrderListResponse.success(orderSummaries, 
                orders.getTotalElements(), 
                orders.getTotalPages(), 
                orders.getNumber() + 1, 
                orders.getSize(), 
                orders.hasNext(), 
                orders.hasPrevious(), 
                orders.isFirst(), 
                orders.isLast());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUser(mapToUserSummaryResponse(order.getUser()));
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setNotes(order.getNotes());
        response.setShippedAt(order.getShippedAt());
        response.setDeliveredAt(order.getDeliveredAt());
        response.setCancelledAt(order.getCancelledAt());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        
        if (order.getOrderItems() != null) {
            List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                    .map(this::mapToOrderItemResponse)
                    .collect(Collectors.toList());
            response.setOrderItems(orderItemResponses);
        }
        
        return response;
    }

    private OrderSummaryResponse mapToOrderSummaryResponse(Order order) {
        OrderSummaryResponse response = new OrderSummaryResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUser(mapToUserSummaryResponse(order.getUser()));
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setTotalItems(order.getTotalItems());
        response.setItemCount(order.getOrderItems() != null ? order.getOrderItems().size() : 0);
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setOrderItemId(orderItem.getOrderItemId());
        response.setProduct(mapToProductSummaryResponse(orderItem.getProduct()));
        response.setQuantity(orderItem.getQuantity());
        response.setUnitPrice(orderItem.getUnitPrice());
        response.setSubtotal(orderItem.getSubtotal());
        response.setCreatedAt(orderItem.getCreatedAt());
        return response;
    }

    private UserSummaryResponse mapToUserSummaryResponse(User user) {
        UserSummaryResponse response = new UserSummaryResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    private ProductSummaryResponse mapToProductSummaryResponse(Product product) {
        ProductSummaryResponse response = new ProductSummaryResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setIsActive(product.getIsActive());
        return response;
    }

    /**
     * Inner class for order statistics
     */
    public static class OrderStatistics {
        private long totalOrders;
        private long pendingOrders;
        private long confirmedOrders;
        private long deliveredOrders;
        private long cancelledOrders;
        private BigDecimal totalRevenue;
        private BigDecimal averageOrderValue;

        // Constructors
        public OrderStatistics() {}

        public OrderStatistics(long totalOrders, long pendingOrders, long confirmedOrders, 
                             long deliveredOrders, long cancelledOrders, BigDecimal totalRevenue, 
                             BigDecimal averageOrderValue) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.confirmedOrders = confirmedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
            this.totalRevenue = totalRevenue;
            this.averageOrderValue = averageOrderValue;
        }

        // Getters and Setters
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

        public long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }

        public long getConfirmedOrders() { return confirmedOrders; }
        public void setConfirmedOrders(long confirmedOrders) { this.confirmedOrders = confirmedOrders; }

        public long getDeliveredOrders() { return deliveredOrders; }
        public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }

        public long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(long cancelledOrders) { this.cancelledOrders = cancelledOrders; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public BigDecimal getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }

        @Override
        public String toString() {
            return "OrderStatistics{" +
                    "totalOrders=" + totalOrders +
                    ", pendingOrders=" + pendingOrders +
                    ", confirmedOrders=" + confirmedOrders +
                    ", deliveredOrders=" + deliveredOrders +
                    ", cancelledOrders=" + cancelledOrders +
                    ", totalRevenue=" + totalRevenue +
                    ", averageOrderValue=" + averageOrderValue +
                    '}';
        }
    }
}