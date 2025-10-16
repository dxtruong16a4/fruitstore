package com.fruitstore.repository;

import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 * Provides data access methods for product operations
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category and active status
     * @param category the category to filter by
     * @param isActive the active status
     * @return list of products matching the criteria
     */
    List<Product> findByCategoryAndIsActiveTrue(Category category);

    /**
     * Find products by category ID and active status
     * @param categoryId the category ID to filter by
     * @param isActive the active status
     * @return list of products matching the criteria
     */
    List<Product> findByCategory_CategoryIdAndIsActiveTrue(Long categoryId);

    /**
     * Find products by price range and active status
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @param isActive the active status
     * @return list of products within the price range
     */
    List<Product> findByPriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find products by name containing (case-insensitive) and active status
     * @param name the name pattern to search for
     * @param isActive the active status
     * @return list of products matching the name pattern
     */
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    /**
     * Find products by stock quantity greater than specified value
     * @param stockQuantity the minimum stock quantity
     * @return list of products with sufficient stock
     */
    List<Product> findByStockQuantityGreaterThanAndIsActiveTrue(Integer stockQuantity);

    /**
     * Find products with zero stock
     * @return list of products with no stock
     */
    List<Product> findByStockQuantityAndIsActiveTrue(Integer stockQuantity);

    /**
     * Find all active products
     * @return list of all active products
     */
    List<Product> findByIsActiveTrue();

    /**
     * Find all active products ordered by name
     * @return list of active products ordered by name
     */
    List<Product> findByIsActiveTrueOrderByNameAsc();

    /**
     * Find all active products ordered by price ascending
     * @return list of active products ordered by price
     */
    List<Product> findByIsActiveTrueOrderByPriceAsc();

    /**
     * Find all active products ordered by price descending
     * @return list of active products ordered by price descending
     */
    List<Product> findByIsActiveTrueOrderByPriceDesc();

    /**
     * Find all active products ordered by creation date descending
     * @return list of active products ordered by creation date
     */
    List<Product> findByIsActiveTrueOrderByCreatedAtDesc();

    /**
     * Find products by category and active status with pagination
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of products in the specified category
     */
    Page<Product> findByCategory_CategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    /**
     * Find all active products with pagination
     * @param pageable pagination information
     * @return page of active products
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);

    /**
     * Find products by name containing with pagination
     * @param name the name pattern
     * @param pageable pagination information
     * @return page of products matching the name pattern
     */
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    /**
     * Custom query: Find products with filters (as specified in roadmap)
     * @param categoryId the category ID (can be null)
     * @param minPrice the minimum price (can be null)
     * @param maxPrice the maximum price (can be null)
     * @param keyword the search keyword (can be null)
     * @param pageable pagination information
     * @return page of products matching the filters
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findProductsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * Custom query: Find top products by stock quantity
     * @param limit the maximum number of products to return
     * @return list of products with highest stock
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.stockQuantity DESC")
    List<Product> findTopProductsByStockQuantity(Pageable pageable);

    /**
     * Custom query: Find low stock products
     * @param threshold the stock threshold
     * @return list of products with stock below threshold
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity <= :threshold ORDER BY p.stockQuantity ASC")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * Custom query: Find products by multiple categories
     * @param categoryIds list of category IDs
     * @param pageable pagination information
     * @return page of products in any of the specified categories
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category.categoryId IN :categoryIds")
    Page<Product> findByCategoryIdInAndIsActiveTrue(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    /**
     * Custom query: Find products with price range and category
     * @param categoryId the category ID
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @param pageable pagination information
     * @return page of products matching all criteria
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "AND p.category.categoryId = :categoryId " +
           "AND p.price >= :minPrice AND p.price <= :maxPrice")
    Page<Product> findByCategoryAndPriceRange(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    /**
     * Count products by category and active status
     * @param categoryId the category ID
     * @param isActive the active status
     * @return number of products in the category
     */
    long countByCategory_CategoryIdAndIsActiveTrue(Long categoryId, Boolean isActive);

    /**
     * Count products by active status
     * @param isActive the active status
     * @return number of products with the specified status
     */
    long countByIsActive(Boolean isActive);

    /**
     * Find product by name (exact match, case-insensitive)
     * @param name the product name
     * @return Optional containing the product if found
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Product> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Check if product exists by name (exact match, case-insensitive)
     * @param name the product name
     * @return true if product exists, false otherwise
     */
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}
