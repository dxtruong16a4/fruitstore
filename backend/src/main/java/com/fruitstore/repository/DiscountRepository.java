package com.fruitstore.repository;

import com.fruitstore.domain.discount.Discount;
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
 * Repository interface for Discount entity
 * Provides data access methods for discount operations
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    /**
     * Find discount by code
     * @param code the discount code
     * @return Optional containing the discount if found
     */
    Optional<Discount> findByCode(String code);

    /**
     * Find discount by code (case-insensitive)
     * @param code the discount code
     * @return Optional containing the discount if found
     */
    @Query("SELECT d FROM Discount d WHERE LOWER(d.code) = LOWER(:code)")
    Optional<Discount> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Check if discount exists by code
     * @param code the discount code
     * @return true if discount exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Check if discount exists by code (case-insensitive)
     * @param code the discount code
     * @return true if discount exists, false otherwise
     */
    @Query("SELECT COUNT(d) > 0 FROM Discount d WHERE LOWER(d.code) = LOWER(:code)")
    boolean existsByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active discounts
     * @return list of active discounts
     */
    List<Discount> findByIsActiveTrue();

    /**
     * Find all active discounts ordered by creation date descending
     * @return list of active discounts ordered by creation date
     */
    List<Discount> findByIsActiveTrueOrderByCreatedAtDesc();

    /**
     * Find all active discounts ordered by discount value descending
     * @return list of active discounts ordered by discount value
     */
    List<Discount> findByIsActiveTrueOrderByDiscountValueDesc();

    /**
     * Find active discounts with pagination
     * @param pageable pagination information
     * @return page of active discounts
     */
    Page<Discount> findByIsActiveTrue(Pageable pageable);

    /**
     * Find discounts by discount type and active status
     * @param discountType the discount type
     * @param isActive the active status
     * @return list of discounts matching the criteria
     */
    List<Discount> findByDiscountTypeAndIsActiveTrue(com.fruitstore.domain.discount.DiscountType discountType);

    /**
     * Find discounts by discount type with pagination
     * @param discountType the discount type
     * @param pageable pagination information
     * @return page of discounts of the specified type
     */
    Page<Discount> findByDiscountTypeAndIsActiveTrue(com.fruitstore.domain.discount.DiscountType discountType, Pageable pageable);

    /**
     * Custom query: Find active discounts within date range (as specified in roadmap)
     * @param currentTime the current time
     * @return list of currently active discounts
     */
    @Query("SELECT d FROM Discount d WHERE d.isActive = true " +
           "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
           "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    List<Discount> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Custom query: Find active discounts that are currently valid (within date range)
     * @param currentTime the current time
     * @param pageable pagination information
     * @return page of currently valid active discounts
     */
    @Query("SELECT d FROM Discount d WHERE d.isActive = true " +
           "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
           "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    Page<Discount> findActiveDiscountsWithinDateRange(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);

    /**
     * Custom query: Find discounts that can still be used (within usage limit)
     * @param currentTime the current time
     * @return list of usable discounts
     */
    @Query("SELECT d FROM Discount d WHERE d.isActive = true " +
           "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
           "AND (d.endDate IS NULL OR d.endDate >= :currentTime) " +
           "AND (d.usageLimit IS NULL OR d.usedCount < d.usageLimit)")
    List<Discount> findUsableDiscounts(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Custom query: Find discounts valid for a specific order amount
     * @param orderAmount the order amount
     * @param currentTime the current time
     * @return list of discounts valid for the order amount
     */
    @Query("SELECT d FROM Discount d WHERE d.isActive = true " +
           "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
           "AND (d.endDate IS NULL OR d.endDate >= :currentTime) " +
           "AND (d.usageLimit IS NULL OR d.usedCount < d.usageLimit) " +
           "AND (d.minOrderAmount IS NULL OR d.minOrderAmount <= :orderAmount)")
    List<Discount> findValidDiscountsForOrderAmount(@Param("orderAmount") java.math.BigDecimal orderAmount, 
                                                    @Param("currentTime") LocalDateTime currentTime);

    /**
     * Custom query: Find discounts by multiple criteria (for admin filtering)
     * @param isActive the active status (can be null)
     * @param discountType the discount type (can be null)
     * @param currentTime the current time
     * @param pageable pagination information
     * @return page of discounts matching the criteria
     */
    @Query("SELECT d FROM Discount d WHERE " +
           "(:isActive IS NULL OR d.isActive = :isActive) " +
           "AND (:discountType IS NULL OR d.discountType = :discountType) " +
           "AND (:currentTime IS NULL OR " +
           "     ((:isActive = true OR :isActive IS NULL) AND " +
           "      (d.startDate IS NULL OR d.startDate <= :currentTime) AND " +
           "      (d.endDate IS NULL OR d.endDate >= :currentTime)))")
    Page<Discount> findDiscountsWithFilters(
            @Param("isActive") Boolean isActive,
            @Param("discountType") com.fruitstore.domain.discount.DiscountType discountType,
            @Param("currentTime") LocalDateTime currentTime,
            Pageable pageable);

    /**
     * Custom query: Find expired discounts
     * @param currentTime the current time
     * @return list of expired discounts
     */
    @Query("SELECT d FROM Discount d WHERE d.endDate IS NOT NULL AND d.endDate < :currentTime")
    List<Discount> findExpiredDiscounts(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Custom query: Find discounts that haven't started yet
     * @param currentTime the current time
     * @return list of future discounts
     */
    @Query("SELECT d FROM Discount d WHERE d.startDate IS NOT NULL AND d.startDate > :currentTime")
    List<Discount> findFutureDiscounts(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Custom query: Find discounts with usage limit reached
     * @return list of discounts that have reached their usage limit
     */
    @Query("SELECT d FROM Discount d WHERE d.usageLimit IS NOT NULL AND d.usedCount >= d.usageLimit")
    List<Discount> findDiscountsWithUsageLimitReached();

    /**
     * Custom query: Find discounts with low usage (for analytics)
     * @param threshold the usage threshold
     * @return list of discounts with usage below threshold
     */
    @Query("SELECT d FROM Discount d WHERE d.usageLimit IS NOT NULL AND d.usedCount < :threshold")
    List<Discount> findDiscountsWithLowUsage(@Param("threshold") Integer threshold);

    /**
     * Count discounts by active status
     * @param isActive the active status
     * @return number of discounts with the specified status
     */
    long countByIsActive(Boolean isActive);

    /**
     * Count active discounts within date range
     * @param currentTime the current time
     * @return number of currently active discounts
     */
    @Query("SELECT COUNT(d) FROM Discount d WHERE d.isActive = true " +
           "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
           "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    long countActiveDiscountsWithinDateRange(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Count discounts by discount type and active status
     * @param discountType the discount type
     * @param isActive the active status
     * @return number of discounts of the specified type and status
     */
    long countByDiscountTypeAndIsActiveTrue(com.fruitstore.domain.discount.DiscountType discountType);

    /**
     * Find discounts by partial code match (for search)
     * @param codePattern the code pattern to search for
     * @param pageable pagination information
     * @return page of discounts matching the code pattern
     */
    @Query("SELECT d FROM Discount d WHERE LOWER(d.code) LIKE LOWER(CONCAT('%', :codePattern, '%'))")
    Page<Discount> findByCodeContainingIgnoreCase(@Param("codePattern") String codePattern, Pageable pageable);

    /**
     * Find discounts by description containing (case-insensitive)
     * @param description the description pattern
     * @param pageable pagination information
     * @return page of discounts matching the description pattern
     */
    @Query("SELECT d FROM Discount d WHERE LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<Discount> findByDescriptionContainingIgnoreCase(@Param("description") String description, Pageable pageable);
}
