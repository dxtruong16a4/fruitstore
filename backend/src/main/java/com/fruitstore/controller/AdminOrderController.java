package com.fruitstore.controller;

import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.dto.request.order.UpdateOrderStatusRequest;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.dto.response.order.OrderListResponse;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.service.OrderService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * REST Controller for admin order management
 * Handles order management operations for administrators
 */
@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AdminOrderController {

    private final OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders with pagination (admin)
     * Requires ADMIN role
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (createdAt, totalAmount, status)
     * @param sortDirection sort direction (asc, desc)
     * @return page of all orders
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        OrderListResponse response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order by ID (admin)
     * Requires ADMIN role
     * 
     * @param id the order ID
     * @return order response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable("id") Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order by order number (admin)
     * Requires ADMIN role
     * 
     * @param orderNumber the order number
     * @return order response
     */
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(@PathVariable String orderNumber) {
        OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update order status (admin)
     * Requires ADMIN role
     *
     * @param id the order ID
     * @param request the status update request
     * @return updated order response
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        try {
            OrderResponse response = orderService.updateOrderStatus(id, request);
            return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Cancel order (admin)
     * Requires ADMIN role
     * 
     * @param id the order ID
     * @return cancelled order response
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable("id") Long id) {
        OrderResponse response = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }

    /**
     * Get orders with filters (admin)
     * Requires ADMIN role
     * 
     * @param userId the user ID filter (optional)
     * @param status the order status filter (optional)
     * @param minAmount the minimum total amount filter (optional)
     * @param maxAmount the maximum total amount filter (optional)
     * @param customerName the customer name filter (optional)
     * @param customerEmail the customer email filter (optional)
     * @param startDate the start date filter (optional)
     * @param endDate the end date filter (optional)
     * @param page page number (0-based)
     * @param size page size
     * @return page of filtered orders
     */
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrdersWithFilters(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        try {
            OrderStatus orderStatus = null;
            if (status != null && !status.isEmpty()) {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            }
            
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;
            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDateTime.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDateTime.parse(endDate);
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            OrderListResponse response = orderService.getOrdersWithFilters(
                userId, orderStatus, minAmount, maxAmount, customerName, customerEmail, 
                startDateTime, endDateTime, pageable);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid filter parameters: " + e.getMessage()));
        }
    }

    /**
     * Get orders by status (admin)
     * Requires ADMIN role
     * 
     * @param status the order status
     * @param page page number (0-based)
     * @param size page size
     * @return page of orders with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            OrderListResponse response = orderService.getOrdersByStatus(orderStatus, pageable);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid order status: " + status));
        }
    }

    /**
     * Get recent orders (admin)
     * Requires ADMIN role
     * 
     * @param days number of days to look back
     * @param page page number (0-based)
     * @param size page size
     * @return page of recent orders
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getRecentOrders(
            @RequestParam(value = "days", defaultValue = "7") int days,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        OrderListResponse response = orderService.getRecentOrders(days, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get cancellable orders (admin)
     * Requires ADMIN role
     * 
     * @param page page number (0-based)
     * @param size page size
     * @return page of cancellable orders
     */
    @GetMapping("/cancellable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderListResponse>> getCancellableOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        OrderListResponse response = orderService.getCancellableOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order statistics (admin)
     * Requires ADMIN role
     * 
     * @return order statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderService.OrderStatistics>> getOrderStatistics() {
        OrderService.OrderStatistics statistics = orderService.getOrderStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    /**
     * Get order count by status (admin)
     * Requires ADMIN role
     * 
     * @param status the order status
     * @return number of orders with the specified status
     */
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByStatus(@PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            long count = orderService.getOrderCountByStatus(orderStatus);
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid order status: " + status));
        }
    }

    /**
     * Get order count by user (admin)
     * Requires ADMIN role
     * 
     * @param userId the user ID
     * @return number of orders for the user
     */
    @GetMapping("/count/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByUser(@PathVariable Long userId) {
        long count = orderService.getOrderCountByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * Get order count by user and status (admin)
     * Requires ADMIN role
     * 
     * @param userId the user ID
     * @param status the order status
     * @return number of orders matching the criteria
     */
    @GetMapping("/count/user/{userId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByUserAndStatus(
            @PathVariable Long userId, 
            @PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            long count = orderService.getOrderCountByUserAndStatus(userId, orderStatus);
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid order status: " + status));
        }
    }

    /**
     * Check if order can be cancelled (admin)
     * Requires ADMIN role
     * 
     * @param id the order ID
     * @return true if order can be cancelled
     */
    @GetMapping("/{id}/can-cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> canCancelOrder(@PathVariable Long id) {
        boolean canCancel = orderService.canCancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(canCancel));
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
            case "customername":
            case "customer_name":
                validSortBy = "customerName";
                break;
            case "customeremail":
            case "customer_email":
                validSortBy = "customerEmail";
                break;
            default:
                validSortBy = "createdAt";
                break;
        }
        
        return PageRequest.of(page, size, Sort.by(direction, validSortBy));
    }
}
