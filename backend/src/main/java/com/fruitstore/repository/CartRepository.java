package com.fruitstore.repository;

import com.fruitstore.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Cart entity operations
 * Handles cart data access and custom queries
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Find cart by user ID
     * 
     * @param userId the user ID
     * @return optional cart
     */
    Optional<Cart> findByUser_UserId(Long userId);

    /**
     * Find cart by user ID with cart items
     * 
     * @param userId the user ID
     * @return optional cart with items loaded
     */
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product WHERE c.user.userId = :userId")
    Optional<Cart> findByUser_UserIdWithItems(@Param("userId") Long userId);

    /**
     * Find cart by user ID with cart items and product details
     * 
     * @param userId the user ID
     * @return optional cart with items and product details loaded
     */
    @Query("SELECT c FROM Cart c " +
           "LEFT JOIN FETCH c.cartItems ci " +
           "LEFT JOIN FETCH ci.product p " +
           "LEFT JOIN FETCH p.category " +
           "WHERE c.user.userId = :userId")
    Optional<Cart> findByUser_UserIdWithItemsAndProducts(@Param("userId") Long userId);

    /**
     * Check if cart exists for user
     * 
     * @param userId the user ID
     * @return true if cart exists
     */
    boolean existsByUser_UserId(Long userId);

    /**
     * Delete cart by user ID
     * 
     * @param userId the user ID
     */
    void deleteByUser_UserId(Long userId);

    /**
     * Count total items in cart for user
     * 
     * @param userId the user ID
     * @return total quantity of items in cart
     */
    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM Cart c JOIN c.cartItems ci WHERE c.user.userId = :userId")
    int countTotalItemsByUserId(@Param("userId") Long userId);

    /**
     * Count different products in cart for user
     * 
     * @param userId the user ID
     * @return number of different products in cart
     */
    @Query("SELECT COUNT(ci) FROM Cart c JOIN c.cartItems ci WHERE c.user.userId = :userId")
    int countDifferentProductsByUserId(@Param("userId") Long userId);

    /**
     * Find carts created after specific date
     * 
     * @param date the date threshold
     * @return list of carts created after the date
     */
    @Query("SELECT c FROM Cart c WHERE c.createdAt > :date")
    java.util.List<Cart> findCartsCreatedAfter(@Param("date") java.time.LocalDateTime date);

    /**
     * Find carts updated after specific date
     * 
     * @param date the date threshold
     * @return list of carts updated after the date
     */
    @Query("SELECT c FROM Cart c WHERE c.updatedAt > :date")
    java.util.List<Cart> findCartsUpdatedAfter(@Param("date") java.time.LocalDateTime date);

    /**
     * Find empty carts (carts with no items)
     * 
     * @return list of empty carts
     */
    @Query("SELECT c FROM Cart c WHERE c.cartItems IS EMPTY")
    java.util.List<Cart> findEmptyCarts();

    /**
     * Find carts with items count greater than specified value
     * 
     * @param itemCount the minimum item count
     * @return list of carts with more items than specified
     */
    @Query("SELECT c FROM Cart c WHERE SIZE(c.cartItems) > :itemCount")
    java.util.List<Cart> findCartsWithItemCountGreaterThan(@Param("itemCount") int itemCount);

    /**
     * Find carts with total quantity greater than specified value
     * 
     * @param totalQuantity the minimum total quantity
     * @return list of carts with total quantity greater than specified
     */
    @Query("SELECT c FROM Cart c WHERE (SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart = c) > :totalQuantity")
    java.util.List<Cart> findCartsWithTotalQuantityGreaterThan(@Param("totalQuantity") int totalQuantity);
}
