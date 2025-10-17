package com.fruitstore.controller;

import com.fruitstore.dto.request.order.CreateOrderRequest;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.dto.response.order.OrderListResponse;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.service.OrderService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for customer order operations
 * Handles order creation, retrieval, and cancellation for authenticated users
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order from cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param request order creation request
     * @return created order response
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateOrderRequest request) {
        
        OrderResponse response = orderService.createOrder(userDetails.getUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    /**
     * Get orders for the authenticated user with pagination
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (createdAt, totalAmount, status)
     * @param sortDirection sort direction (asc, desc)
     * @return page of user orders
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrdersByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        OrderListResponse response = orderService.getOrdersByUser(userDetails.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order by ID for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param id the order ID
     * @return order response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        
        OrderResponse response = orderService.getOrderById(id, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order by order number for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param orderNumber the order number
     * @return order response
     */
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String orderNumber) {
        
        OrderResponse response = orderService.getOrderByOrderNumber(orderNumber, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Cancel order for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param id the order ID
     * @return cancelled order response
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        
        OrderResponse response = orderService.cancelOrder(id, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }

    /**
     * Get orders by status for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param status the order status
     * @param page page number (0-based)
     * @param size page size
     * @return page of orders with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrdersByUserAndStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            com.fruitstore.domain.order.OrderStatus orderStatus = 
                com.fruitstore.domain.order.OrderStatus.valueOf(status.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            OrderListResponse response = orderService.getOrdersByUserAndStatus(
                userDetails.getUserId(), orderStatus, pageable);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid order status: " + status));
        }
    }

    /**
     * Get recent orders for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param days number of days to look back
     * @param page page number (0-based)
     * @param size page size
     * @return page of recent orders
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getRecentOrdersByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "days", defaultValue = "30") int days,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        OrderListResponse response = orderService.getRecentOrdersByUser(
            userDetails.getUserId(), days, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get cancellable orders for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param page page number (0-based)
     * @param size page size
     * @return page of cancellable orders
     */
    @GetMapping("/cancellable")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getCancellableOrdersByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        OrderListResponse response = orderService.getCancellableOrdersByUser(
            userDetails.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Check if order can be cancelled by the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param id the order ID
     * @return true if order can be cancelled
     */
    @GetMapping("/{id}/can-cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Boolean>> canCancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        
        boolean canCancel = orderService.canCancelOrder(id, userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(canCancel));
    }

    /**
     * Get order statistics for the authenticated user
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return order statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<OrderService.OrderStatistics>> getOrderStatisticsByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        OrderService.OrderStatistics statistics = orderService.getOrderStatisticsByUser(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    // Helper method to create Pageable
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Validate sort field
        String validSortBy;
        switch (sortBy.toLowerCase()) {
            case "createdat":
            case "created_at":
                validSortBy = "createdAt";
                break;
            case "totalamount":
            case "total_amount":
                validSortBy = "totalAmount";
                break;
            case "status":
                validSortBy = "status";
                break;
            case "ordernumber":
            case "order_number":
                validSortBy = "orderNumber";
                break;
            default:
                validSortBy = "createdAt";
                break;
        }
        
        return PageRequest.of(page, size, Sort.by(direction, validSortBy));
    }
}
