package com.fruitstore.repository;

import com.fruitstore.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 * Provides data access methods for category operations
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name
     * @param name the category name to search for
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);

    /**
     * Check if a category exists with the given name
     * @param name the category name to check
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find all active categories
     * @return list of active categories
     */
    List<Category> findByIsActiveTrue();

    /**
     * Find all inactive categories
     * @return list of inactive categories
     */
    List<Category> findByIsActiveFalse();

    /**
     * Find all categories ordered by name
     * @return list of categories ordered by name
     */
    List<Category> findAllByOrderByNameAsc();

    /**
     * Find all active categories ordered by name
     * @return list of active categories ordered by name
     */
    List<Category> findByIsActiveTrueOrderByNameAsc();

    /**
     * Find categories by name containing (case-insensitive)
     * @param name the name pattern to search for
     * @return list of categories matching the pattern
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * Find active categories by name containing (case-insensitive)
     * @param name the name pattern to search for
     * @return list of active categories matching the pattern
     */
    List<Category> findByIsActiveTrueAndNameContainingIgnoreCase(String name);

    /**
     * Count categories by active status
     * @param isActive the active status to count
     * @return number of categories with the specified status
     */
    long countByIsActive(Boolean isActive);

    /**
     * Find active categories
     * @return list of active categories
     */
    @Query("SELECT c FROM Category c WHERE c.isActive = true")
    List<Category> findActiveCategoriesWithProducts();

    /**
     * Find category by name (case-insensitive)
     * @param name the category name to search for
     * @return Optional containing the category if found
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Category> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find categories with product count greater than specified value
     * @param productCount the minimum product count
     * @return list of categories with product count greater than specified value
     */
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND " +
           "(SELECT COUNT(p) FROM Product p WHERE p.category = c AND p.isActive = true) > :productCount")
    List<Category> findActiveCategoriesWithProductCountGreaterThan(@Param("productCount") Long productCount);
}
