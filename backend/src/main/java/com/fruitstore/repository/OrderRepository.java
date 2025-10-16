package com.fruitstore.repository;

import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity
 * Provides data access methods for order operations
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by user ID with pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of orders for the user
     */
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);

    /**
     * Find orders by user ID ordered by creation date descending
     * @param userId the user ID
     * @return list of orders for the user ordered by creation date
     */
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find orders by status ordered by creation date descending
     * @param status the order status
     * @return list of orders with the specified status
     */
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    /**
     * Find orders by status with pagination
     * @param status the order status
     * @param pageable pagination information
     * @return page of orders with the specified status
     */
    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    /**
     * Find orders by user ID and status
     * @param userId the user ID
     * @param status the order status
     * @return list of orders matching the criteria
     */
    List<Order> findByUser_UserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Find orders by user ID and status with pagination
     * @param userId the user ID
     * @param status the order status
     * @param pageable pagination information
     * @return page of orders matching the criteria
     */
    Page<Order> findByUser_UserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    /**
     * Find order by order number
     * @param orderNumber the order number
     * @return Optional containing the order if found
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by customer email
     * @param customerEmail the customer email
     * @return list of orders for the customer
     */
    List<Order> findByCustomerEmail(String customerEmail);

    /**
     * Find orders by customer email with pagination
     * @param customerEmail the customer email
     * @param pageable pagination information
     * @return page of orders for the customer
     */
    Page<Order> findByCustomerEmail(String customerEmail, Pageable pageable);

    /**
     * Find orders by customer name containing (case-insensitive)
     * @param customerName the customer name pattern
     * @return list of orders matching the customer name
     */
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);

    /**
     * Find orders by customer name containing with pagination
     * @param customerName the customer name pattern
     * @param pageable pagination information
     * @return page of orders matching the customer name
     */
    Page<Order> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

    /**
     * Find orders by total amount range
     * @param minAmount the minimum total amount
     * @param maxAmount the maximum total amount
     * @return list of orders within the amount range
     */
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * Find orders by total amount range with pagination
     * @param minAmount the minimum total amount
     * @param maxAmount the maximum total amount
     * @param pageable pagination information
     * @return page of orders within the amount range
     */
    Page<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);

    /**
     * Find orders created between dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders created between the dates
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find orders created between dates with pagination
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of orders created between the dates
     */
    Page<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find orders by status and date range
     * @param status the order status
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders matching the criteria
     */
    List<Order> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find orders by status and date range with pagination
     * @param status the order status
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of orders matching the criteria
     */
    Page<Order> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find orders with items (eager loading)
     * @param orderId the order ID
     * @return Optional containing the order with items loaded
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.orderId = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    /**
     * Find orders by user ID with items
     * @param userId the user ID
     * @return list of orders with items loaded
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUser_UserIdWithItems(@Param("userId") Long userId);

    /**
     * Find orders by user ID with items and pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of orders with items loaded
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.userId = :userId")
    Page<Order> findByUser_UserIdWithItems(@Param("userId") Long userId, Pageable pageable);

    /**
     * Custom query: Find orders with filters (as specified in roadmap)
     * @param userId the user ID (can be null)
     * @param status the order status (can be null)
     * @param minAmount the minimum total amount (can be null)
     * @param maxAmount the maximum total amount (can be null)
     * @param customerName the customer name pattern (can be null)
     * @param customerEmail the customer email (can be null)
     * @param startDate the start date (can be null)
     * @param endDate the end date (can be null)
     * @param pageable pagination information
     * @return page of orders matching the filters
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(:userId IS NULL OR o.user.userId = :userId) " +
           "AND (:status IS NULL OR o.status = :status) " +
           "AND (:minAmount IS NULL OR o.totalAmount >= :minAmount) " +
           "AND (:maxAmount IS NULL OR o.totalAmount <= :maxAmount) " +
           "AND (:customerName IS NULL OR LOWER(o.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
           "AND (:customerEmail IS NULL OR LOWER(o.customerEmail) LIKE LOWER(CONCAT('%', :customerEmail, '%'))) " +
           "AND (:startDate IS NULL OR o.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR o.createdAt <= :endDate)")
    Page<Order> findOrdersWithFilters(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("customerName") String customerName,
            @Param("customerEmail") String customerEmail,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    /**
     * Find recent orders (last N days)
     * @param days the number of days
     * @return list of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

    /**
     * Find recent orders with pagination
     * @param days the number of days
     * @param pageable pagination information
     * @return page of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    /**
     * Find orders by multiple statuses
     * @param statuses list of order statuses
     * @return list of orders with any of the specified statuses
     */
    List<Order> findByStatusIn(List<OrderStatus> statuses);

    /**
     * Find orders by multiple statuses with pagination
     * @param statuses list of order statuses
     * @param pageable pagination information
     * @return page of orders with any of the specified statuses
     */
    Page<Order> findByStatusIn(List<OrderStatus> statuses, Pageable pageable);

    /**
     * Find orders by user ID and multiple statuses
     * @param userId the user ID
     * @param statuses list of order statuses
     * @return list of orders matching the criteria
     */
    List<Order> findByUser_UserIdAndStatusIn(Long userId, List<OrderStatus> statuses);

    /**
     * Find orders by user ID and multiple statuses with pagination
     * @param userId the user ID
     * @param statuses list of order statuses
     * @param pageable pagination information
     * @return page of orders matching the criteria
     */
    Page<Order> findByUser_UserIdAndStatusIn(Long userId, List<OrderStatus> statuses, Pageable pageable);

    /**
     * Count orders by status
     * @param status the order status
     * @return number of orders with the specified status
     */
    long countByStatus(OrderStatus status);

    /**
     * Count orders by user ID
     * @param userId the user ID
     * @return number of orders for the user
     */
    long countByUser_UserId(Long userId);

    /**
     * Count orders by user ID and status
     * @param userId the user ID
     * @param status the order status
     * @return number of orders matching the criteria
     */
    long countByUser_UserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Find orders with total amount greater than specified value
     * @param amount the minimum total amount
     * @return list of orders with total amount greater than specified
     */
    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    /**
     * Find orders with total amount greater than specified value with pagination
     * @param amount the minimum total amount
     * @param pageable pagination information
     * @return page of orders with total amount greater than specified
     */
    Page<Order> findByTotalAmountGreaterThan(BigDecimal amount, Pageable pageable);

    /**
     * Find orders by order number containing (case-insensitive)
     * @param orderNumber the order number pattern
     * @return list of orders matching the order number pattern
     */
    List<Order> findByOrderNumberContainingIgnoreCase(String orderNumber);

    /**
     * Find orders by order number containing with pagination
     * @param orderNumber the order number pattern
     * @param pageable pagination information
     * @return page of orders matching the order number pattern
     */
    Page<Order> findByOrderNumberContainingIgnoreCase(String orderNumber, Pageable pageable);

    /**
     * Find orders that can be cancelled (PENDING or CONFIRMED)
     * @return list of orders that can be cancelled
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED')")
    List<Order> findCancellableOrders();

    /**
     * Find orders that can be cancelled with pagination
     * @param pageable pagination information
     * @return page of orders that can be cancelled
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED')")
    Page<Order> findCancellableOrders(Pageable pageable);

    /**
     * Find orders by user ID that can be cancelled
     * @param userId the user ID
     * @return list of orders that can be cancelled by the user
     */
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId AND o.status IN ('PENDING', 'CONFIRMED')")
    List<Order> findCancellableOrdersByUser(@Param("userId") Long userId);

    /**
     * Find orders by user ID that can be cancelled with pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of orders that can be cancelled by the user
     */
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId AND o.status IN ('PENDING', 'CONFIRMED')")
    Page<Order> findCancellableOrdersByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Check if order exists by order number
     * @param orderNumber the order number
     * @return true if order exists, false otherwise
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Find orders by phone number
     * @param phoneNumber the phone number
     * @return list of orders with the specified phone number
     */
    List<Order> findByPhoneNumber(String phoneNumber);

    /**
     * Find orders by phone number with pagination
     * @param phoneNumber the phone number
     * @param pageable pagination information
     * @return page of orders with the specified phone number
     */
    Page<Order> findByPhoneNumber(String phoneNumber, Pageable pageable);
}
