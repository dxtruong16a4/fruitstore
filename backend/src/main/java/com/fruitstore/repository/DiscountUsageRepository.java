package com.fruitstore.repository;

import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountUsage;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DiscountUsage entity
 * Provides data access methods for discount usage tracking
 */
@Repository
public interface DiscountUsageRepository extends JpaRepository<DiscountUsage, Long> {

    /**
     * Find discount usages by discount
     * @param discount the discount
     * @return list of usages for the discount
     */
    List<DiscountUsage> findByDiscount(Discount discount);

    /**
     * Find discount usages by discount ID
     * @param discountId the discount ID
     * @return list of usages for the discount
     */
    List<DiscountUsage> findByDiscount_DiscountId(Long discountId);

    /**
     * Find discount usages by user
     * @param user the user
     * @return list of usages by the user
     */
    List<DiscountUsage> findByUser(User user);

    /**
     * Find discount usages by user ID
     * @param userId the user ID
     * @return list of usages by the user
     */
    List<DiscountUsage> findByUser_UserId(Long userId);

    /**
     * Find discount usages by order
     * @param order the order
     * @return list of usages for the order
     */
    List<DiscountUsage> findByOrder(Order order);

    /**
     * Find discount usages by order ID
     * @param orderId the order ID
     * @return list of usages for the order
     */
    List<DiscountUsage> findByOrder_OrderId(Long orderId);

    /**
     * Find discount usages by user and discount
     * @param user the user
     * @param discount the discount
     * @return list of usages by the user for the discount
     */
    List<DiscountUsage> findByUserAndDiscount(User user, Discount discount);

    /**
     * Find discount usages by user ID and discount ID
     * @param userId the user ID
     * @param discountId the discount ID
     * @return list of usages by the user for the discount
     */
    List<DiscountUsage> findByUser_UserIdAndDiscount_DiscountId(Long userId, Long discountId);

    /**
     * Custom query: Count usage by discount and user (as specified in roadmap)
     * @param discountId the discount ID
     * @param userId the user ID
     * @return count of usages by the user for the discount
     */
    @Query("SELECT COUNT(du) FROM DiscountUsage du WHERE du.discount.discountId = :discountId AND du.user.userId = :userId")
    long countByDiscount_DiscountIdAndUser_UserId(@Param("discountId") Long discountId, @Param("userId") Long userId);

    /**
     * Find discount usages within date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of usages within the date range
     */
    List<DiscountUsage> findByUsedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find discount usages within date range with pagination
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of usages within the date range
     */
    Page<DiscountUsage> findByUsedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find discount usages by user with pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of usages by the user
     */
    Page<DiscountUsage> findByUser_UserId(Long userId, Pageable pageable);

    /**
     * Find discount usages by discount with pagination
     * @param discountId the discount ID
     * @param pageable pagination information
     * @return page of usages for the discount
     */
    Page<DiscountUsage> findByDiscount_DiscountId(Long discountId, Pageable pageable);

    /**
     * Find all discount usages ordered by usage date descending
     * @return list of all usages ordered by date
     */
    List<DiscountUsage> findAllByOrderByUsedAtDesc();

    /**
     * Find all discount usages ordered by usage date descending with pagination
     * @param pageable pagination information
     * @return page of all usages ordered by date
     */
    Page<DiscountUsage> findAllByOrderByUsedAtDesc(Pageable pageable);

    /**
     * Custom query: Find recent discount usages (last N days)
     * @param days the number of days to look back
     * @param pageable pagination information
     * @return page of recent usages
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.usedAt >= :startDate ORDER BY du.usedAt DESC")
    Page<DiscountUsage> findRecentUsages(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    /**
     * Custom query: Find discount usages by discount code
     * @param discountCode the discount code
     * @return list of usages for the discount code
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.discount.code = :discountCode")
    List<DiscountUsage> findByDiscountCode(@Param("discountCode") String discountCode);

    /**
     * Custom query: Find discount usages by discount code with pagination
     * @param discountCode the discount code
     * @param pageable pagination information
     * @return page of usages for the discount code
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.discount.code = :discountCode")
    Page<DiscountUsage> findByDiscountCode(@Param("discountCode") String discountCode, Pageable pageable);

    /**
     * Custom query: Find discount usages by user and date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of usages by the user within the date range
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.user.userId = :userId " +
           "AND du.usedAt >= :startDate AND du.usedAt <= :endDate ORDER BY du.usedAt DESC")
    List<DiscountUsage> findByUserAndDateRange(@Param("userId") Long userId, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * Custom query: Find discount usages by discount and date range
     * @param discountId the discount ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of usages for the discount within the date range
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.discount.discountId = :discountId " +
           "AND du.usedAt >= :startDate AND du.usedAt <= :endDate ORDER BY du.usedAt DESC")
    List<DiscountUsage> findByDiscountAndDateRange(@Param("discountId") Long discountId, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Custom query: Find top users by discount usage count
     * @param discountId the discount ID
     * @param pageable pagination information
     * @return list of users ordered by usage count
     */
    @Query("SELECT du.user, COUNT(du) as usageCount FROM DiscountUsage du " +
           "WHERE du.discount.discountId = :discountId " +
           "GROUP BY du.user ORDER BY usageCount DESC")
    Page<Object[]> findTopUsersByDiscountUsage(@Param("discountId") Long discountId, Pageable pageable);

    /**
     * Custom query: Find most used discounts
     * @param pageable pagination information
     * @return list of discounts ordered by usage count
     */
    @Query("SELECT du.discount, COUNT(du) as usageCount FROM DiscountUsage du " +
           "GROUP BY du.discount ORDER BY usageCount DESC")
    Page<Object[]> findMostUsedDiscounts(Pageable pageable);

    /**
     * Custom query: Find discount usage statistics by date
     * @param startDate the start date
     * @param endDate the end date
     * @return list of usage statistics grouped by date
     */
    @Query("SELECT DATE(du.usedAt) as usageDate, COUNT(du) as usageCount, SUM(du.discountAmount) as totalDiscountAmount " +
           "FROM DiscountUsage du WHERE du.usedAt >= :startDate AND du.usedAt <= :endDate " +
           "GROUP BY DATE(du.usedAt) ORDER BY usageDate DESC")
    List<Object[]> findUsageStatisticsByDate(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * Custom query: Find total discount amount used by user
     * @param userId the user ID
     * @return total discount amount used by the user
     */
    @Query("SELECT COALESCE(SUM(du.discountAmount), 0) FROM DiscountUsage du WHERE du.user.userId = :userId")
    java.math.BigDecimal findTotalDiscountAmountByUser(@Param("userId") Long userId);

    /**
     * Custom query: Find total discount amount used for a discount
     * @param discountId the discount ID
     * @return total discount amount used for the discount
     */
    @Query("SELECT COALESCE(SUM(du.discountAmount), 0) FROM DiscountUsage du WHERE du.discount.discountId = :discountId")
    java.math.BigDecimal findTotalDiscountAmountByDiscount(@Param("discountId") Long discountId);

    /**
     * Custom query: Find discount usages with order information
     * @param pageable pagination information
     * @return page of usages with order details
     */
    @Query("SELECT du FROM DiscountUsage du LEFT JOIN FETCH du.order WHERE du.order IS NOT NULL")
    Page<DiscountUsage> findUsagesWithOrders(Pageable pageable);

    /**
     * Custom query: Find discount usages without orders (standalone usages)
     * @param pageable pagination information
     * @return page of standalone usages
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.order IS NULL")
    Page<DiscountUsage> findStandaloneUsages(Pageable pageable);

    /**
     * Count discount usages by user
     * @param userId the user ID
     * @return number of usages by the user
     */
    long countByUser_UserId(Long userId);

    /**
     * Count discount usages by discount
     * @param discountId the discount ID
     * @return number of usages for the discount
     */
    long countByDiscount_DiscountId(Long discountId);

    /**
     * Count discount usages by user and discount
     * @param userId the user ID
     * @param discountId the discount ID
     * @return number of usages by the user for the discount
     */
    long countByUser_UserIdAndDiscount_DiscountId(Long userId, Long discountId);

    /**
     * Count discount usages within date range
     * @param startDate the start date
     * @param endDate the end date
     * @return number of usages within the date range
     */
    long countByUsedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Check if user has used a specific discount
     * @param userId the user ID
     * @param discountId the discount ID
     * @return true if user has used the discount, false otherwise
     */
    @Query("SELECT COUNT(du) > 0 FROM DiscountUsage du WHERE du.user.userId = :userId AND du.discount.discountId = :discountId")
    boolean existsByUser_UserIdAndDiscount_DiscountId(@Param("userId") Long userId, @Param("discountId") Long discountId);

    /**
     * Find latest usage by user and discount
     * @param userId the user ID
     * @param discountId the discount ID
     * @return Optional containing the latest usage if found
     */
    @Query("SELECT du FROM DiscountUsage du WHERE du.user.userId = :userId AND du.discount.discountId = :discountId " +
           "ORDER BY du.usedAt DESC")
    Optional<DiscountUsage> findLatestUsageByUserAndDiscount(@Param("userId") Long userId, @Param("discountId") Long discountId);
}
