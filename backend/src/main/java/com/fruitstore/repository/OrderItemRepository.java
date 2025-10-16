package com.fruitstore.repository;

import com.fruitstore.domain.order.OrderItem;
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
 * Repository interface for OrderItem entity
 * Provides data access methods for order item operations
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find order items by order ID
     * @param orderId the order ID
     * @return list of order items for the order
     */
    List<OrderItem> findByOrder_OrderId(Long orderId);

    /**
     * Find order items by order ID with pagination
     * @param orderId the order ID
     * @param pageable pagination information
     * @return page of order items for the order
     */
    Page<OrderItem> findByOrder_OrderId(Long orderId, Pageable pageable);

    /**
     * Find order items by order ID ordered by creation date
     * @param orderId the order ID
     * @return list of order items ordered by creation date
     */
    List<OrderItem> findByOrder_OrderIdOrderByCreatedAtAsc(Long orderId);

    /**
     * Find order items by product ID
     * @param productId the product ID
     * @return list of order items for the product
     */
    List<OrderItem> findByProduct_ProductId(Long productId);

    /**
     * Find order items by product ID with pagination
     * @param productId the product ID
     * @param pageable pagination information
     * @return page of order items for the product
     */
    Page<OrderItem> findByProduct_ProductId(Long productId, Pageable pageable);

    /**
     * Find order items by product ID ordered by creation date descending
     * @param productId the product ID
     * @return list of order items ordered by creation date
     */
    List<OrderItem> findByProduct_ProductIdOrderByCreatedAtDesc(Long productId);

    /**
     * Find order items by order and product
     * @param orderId the order ID
     * @param productId the product ID
     * @return Optional containing the order item if found
     */
    Optional<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Long orderId, Long productId);

    /**
     * Find order items by quantity range
     * @param minQuantity the minimum quantity
     * @param maxQuantity the maximum quantity
     * @return list of order items within the quantity range
     */
    List<OrderItem> findByQuantityBetween(Integer minQuantity, Integer maxQuantity);

    /**
     * Find order items by quantity range with pagination
     * @param minQuantity the minimum quantity
     * @param maxQuantity the maximum quantity
     * @param pageable pagination information
     * @return page of order items within the quantity range
     */
    Page<OrderItem> findByQuantityBetween(Integer minQuantity, Integer maxQuantity, Pageable pageable);

    /**
     * Find order items by unit price range
     * @param minPrice the minimum unit price
     * @param maxPrice the maximum unit price
     * @return list of order items within the price range
     */
    List<OrderItem> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find order items by unit price range with pagination
     * @param minPrice the minimum unit price
     * @param maxPrice the maximum unit price
     * @param pageable pagination information
     * @return page of order items within the price range
     */
    Page<OrderItem> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Find order items by subtotal range
     * @param minSubtotal the minimum subtotal
     * @param maxSubtotal the maximum subtotal
     * @return list of order items within the subtotal range
     */
    List<OrderItem> findBySubtotalBetween(BigDecimal minSubtotal, BigDecimal maxSubtotal);

    /**
     * Find order items by subtotal range with pagination
     * @param minSubtotal the minimum subtotal
     * @param maxSubtotal the maximum subtotal
     * @param pageable pagination information
     * @return page of order items within the subtotal range
     */
    Page<OrderItem> findBySubtotalBetween(BigDecimal minSubtotal, BigDecimal maxSubtotal, Pageable pageable);

    /**
     * Find order items created between dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of order items created between the dates
     */
    List<OrderItem> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find order items created between dates with pagination
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of order items created between the dates
     */
    Page<OrderItem> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find order items with product details (eager loading)
     * @param orderId the order ID
     * @return list of order items with product details loaded
     */
    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrder_OrderIdWithProductDetails(@Param("orderId") Long orderId);

    /**
     * Find order items with product details and pagination
     * @param orderId the order ID
     * @param pageable pagination information
     * @return page of order items with product details loaded
     */
    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE oi.order.orderId = :orderId")
    Page<OrderItem> findByOrder_OrderIdWithProductDetails(@Param("orderId") Long orderId, Pageable pageable);

    /**
     * Find order items by user ID (through order relationship)
     * @param userId the user ID
     * @return list of order items for the user
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId")
    List<OrderItem> findByUser_UserId(@Param("userId") Long userId);

    /**
     * Find order items by user ID with pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of order items for the user
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId")
    Page<OrderItem> findByUser_UserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find order items by user ID ordered by creation date descending
     * @param userId the user ID
     * @return list of order items for the user ordered by creation date
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId ORDER BY oi.createdAt DESC")
    List<OrderItem> findByUser_UserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * Find order items by user ID and product ID
     * @param userId the user ID
     * @param productId the product ID
     * @return list of order items for the user and product
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId AND oi.product.productId = :productId")
    List<OrderItem> findByUser_UserIdAndProduct_ProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Find order items by user ID and product ID with pagination
     * @param userId the user ID
     * @param productId the product ID
     * @param pageable pagination information
     * @return page of order items for the user and product
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId AND oi.product.productId = :productId")
    Page<OrderItem> findByUser_UserIdAndProduct_ProductId(@Param("userId") Long userId, @Param("productId") Long productId, Pageable pageable);

    /**
     * Find order items with high quantities (above threshold)
     * @param threshold the quantity threshold
     * @return list of order items with quantities above threshold
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity > :threshold ORDER BY oi.quantity DESC")
    List<OrderItem> findHighQuantityItems(@Param("threshold") Integer threshold);

    /**
     * Find order items with high quantities with pagination
     * @param threshold the quantity threshold
     * @param pageable pagination information
     * @return page of order items with quantities above threshold
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity > :threshold ORDER BY oi.quantity DESC")
    Page<OrderItem> findHighQuantityItems(@Param("threshold") Integer threshold, Pageable pageable);

    /**
     * Find order items with high subtotals (above threshold)
     * @param threshold the subtotal threshold
     * @return list of order items with subtotals above threshold
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.subtotal > :threshold ORDER BY oi.subtotal DESC")
    List<OrderItem> findHighSubtotalItems(@Param("threshold") BigDecimal threshold);

    /**
     * Find order items with high subtotals with pagination
     * @param threshold the subtotal threshold
     * @param pageable pagination information
     * @return page of order items with subtotals above threshold
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.subtotal > :threshold ORDER BY oi.subtotal DESC")
    Page<OrderItem> findHighSubtotalItems(@Param("threshold") BigDecimal threshold, Pageable pageable);

    /**
     * Find most popular products (by total quantity ordered)
     * @param pageable pagination information
     * @return page of products ordered by total quantity
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product ORDER BY totalQuantity DESC")
    Page<Object[]> findMostPopularProducts(Pageable pageable);

    /**
     * Find most popular products by user
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of products ordered by total quantity for the user
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId GROUP BY oi.product ORDER BY totalQuantity DESC")
    Page<Object[]> findMostPopularProductsByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find order items by product category
     * @param categoryId the category ID
     * @return list of order items for products in the category
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.product p WHERE p.category.categoryId = :categoryId")
    List<OrderItem> findByProductCategory(@Param("categoryId") Long categoryId);

    /**
     * Find order items by product category with pagination
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of order items for products in the category
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.product p WHERE p.category.categoryId = :categoryId")
    Page<OrderItem> findByProductCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Find order items by product category ordered by creation date descending
     * @param categoryId the category ID
     * @return list of order items for products in the category ordered by creation date
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.product p WHERE p.category.categoryId = :categoryId ORDER BY oi.createdAt DESC")
    List<OrderItem> findByProductCategoryOrderByCreatedAtDesc(@Param("categoryId") Long categoryId);

    /**
     * Find order items by multiple products
     * @param productIds list of product IDs
     * @return list of order items for any of the specified products
     */
    List<OrderItem> findByProduct_ProductIdIn(List<Long> productIds);

    /**
     * Find order items by multiple products with pagination
     * @param productIds list of product IDs
     * @param pageable pagination information
     * @return page of order items for any of the specified products
     */
    Page<OrderItem> findByProduct_ProductIdIn(List<Long> productIds, Pageable pageable);

    /**
     * Find order items by multiple orders
     * @param orderIds list of order IDs
     * @return list of order items for any of the specified orders
     */
    List<OrderItem> findByOrder_OrderIdIn(List<Long> orderIds);

    /**
     * Find order items by multiple orders with pagination
     * @param orderIds list of order IDs
     * @param pageable pagination information
     * @return page of order items for any of the specified orders
     */
    Page<OrderItem> findByOrder_OrderIdIn(List<Long> orderIds, Pageable pageable);

    /**
     * Count order items by order ID
     * @param orderId the order ID
     * @return number of order items for the order
     */
    long countByOrder_OrderId(Long orderId);

    /**
     * Count order items by product ID
     * @param productId the product ID
     * @return number of order items for the product
     */
    long countByProduct_ProductId(Long productId);

    /**
     * Count order items by user ID
     * @param userId the user ID
     * @return number of order items for the user
     */
    @Query("SELECT COUNT(oi) FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId")
    long countByUser_UserId(@Param("userId") Long userId);

    /**
     * Sum total quantity by product ID
     * @param productId the product ID
     * @return total quantity ordered for the product
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.productId = :productId")
    Long sumQuantityByProduct_ProductId(@Param("productId") Long productId);

    /**
     * Sum total quantity by user ID
     * @param userId the user ID
     * @return total quantity ordered by the user
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId")
    Long sumQuantityByUser_UserId(@Param("userId") Long userId);

    /**
     * Sum total subtotal by order ID
     * @param orderId the order ID
     * @return total subtotal for the order
     */
    @Query("SELECT COALESCE(SUM(oi.subtotal), 0) FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    BigDecimal sumSubtotalByOrder_OrderId(@Param("orderId") Long orderId);

    /**
     * Sum total subtotal by user ID
     * @param userId the user ID
     * @return total subtotal for the user
     */
    @Query("SELECT COALESCE(SUM(oi.subtotal), 0) FROM OrderItem oi JOIN oi.order o WHERE o.user.userId = :userId")
    BigDecimal sumSubtotalByUser_UserId(@Param("userId") Long userId);

    /**
     * Find order items with product details by order ID
     * @param orderId the order ID
     * @return list of order items with product details
     */
    @Query("SELECT oi FROM OrderItem oi " +
           "LEFT JOIN FETCH oi.product p " +
           "LEFT JOIN FETCH p.category " +
           "WHERE oi.order.orderId = :orderId " +
           "ORDER BY oi.createdAt ASC")
    List<OrderItem> findOrderItemsWithProductDetails(@Param("orderId") Long orderId);

    /**
     * Find order items by order ID with product and category details
     * @param orderId the order ID
     * @return list of order items with full product and category details
     */
    @Query("SELECT oi FROM OrderItem oi " +
           "LEFT JOIN FETCH oi.product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE oi.order.orderId = :orderId " +
           "ORDER BY oi.createdAt ASC")
    List<OrderItem> findOrderItemsWithFullDetails(@Param("orderId") Long orderId);

    /**
     * Find recent order items (last N days)
     * @param startDate the start date
     * @return list of recent order items
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.createdAt >= :startDate ORDER BY oi.createdAt DESC")
    List<OrderItem> findRecentOrderItems(@Param("startDate") LocalDateTime startDate);

    /**
     * Find recent order items with pagination
     * @param startDate the start date
     * @param pageable pagination information
     * @return page of recent order items
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.createdAt >= :startDate ORDER BY oi.createdAt DESC")
    Page<OrderItem> findRecentOrderItems(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    /**
     * Find order items with quantity greater than specified value
     * @param quantity the minimum quantity
     * @return list of order items with quantity greater than specified
     */
    List<OrderItem> findByQuantityGreaterThan(Integer quantity);

    /**
     * Find order items with quantity greater than specified value with pagination
     * @param quantity the minimum quantity
     * @param pageable pagination information
     * @return page of order items with quantity greater than specified
     */
    Page<OrderItem> findByQuantityGreaterThan(Integer quantity, Pageable pageable);

    /**
     * Find order items with subtotal greater than specified value
     * @param subtotal the minimum subtotal
     * @return list of order items with subtotal greater than specified
     */
    List<OrderItem> findBySubtotalGreaterThan(BigDecimal subtotal);

    /**
     * Find order items with subtotal greater than specified value with pagination
     * @param subtotal the minimum subtotal
     * @param pageable pagination information
     * @return page of order items with subtotal greater than specified
     */
    Page<OrderItem> findBySubtotalGreaterThan(BigDecimal subtotal, Pageable pageable);

    /**
     * Delete order items by order ID
     * @param orderId the order ID
     */
    void deleteByOrder_OrderId(Long orderId);

    /**
     * Delete order items by product ID
     * @param productId the product ID
     */
    void deleteByProduct_ProductId(Long productId);
}
