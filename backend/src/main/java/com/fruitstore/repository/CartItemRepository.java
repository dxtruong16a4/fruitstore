package com.fruitstore.repository;

import com.fruitstore.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CartItem entity operations
 * Handles cart item data access and custom queries
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Find cart items by cart ID
     * 
     * @param cartId the cart ID
     * @return list of cart items in the cart
     */
    List<CartItem> findByCart_CartId(Long cartId);

    /**
     * Find cart item by cart ID and product ID
     * 
     * @param cartId the cart ID
     * @param productId the product ID
     * @return optional cart item
     */
    Optional<CartItem> findByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);

    /**
     * Find cart items by cart ID with product details
     * 
     * @param cartId the cart ID
     * @return list of cart items with product details loaded
     */
    @Query("SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product p LEFT JOIN FETCH p.category WHERE ci.cart.cartId = :cartId")
    List<CartItem> findByCart_CartIdWithProducts(@Param("cartId") Long cartId);

    /**
     * Find cart items by user ID
     * 
     * @param userId the user ID
     * @return list of cart items for the user
     */
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.userId = :userId")
    List<CartItem> findByUser_UserId(@Param("userId") Long userId);

    /**
     * Find cart items by user ID with product details
     * 
     * @param userId the user ID
     * @return list of cart items with product details loaded
     */
    @Query("SELECT ci FROM CartItem ci " +
           "JOIN ci.cart c " +
           "LEFT JOIN FETCH ci.product p " +
           "LEFT JOIN FETCH p.category " +
           "WHERE c.user.userId = :userId")
    List<CartItem> findByUser_UserIdWithProducts(@Param("userId") Long userId);

    /**
     * Find cart items by product ID
     * 
     * @param productId the product ID
     * @return list of cart items containing the product
     */
    List<CartItem> findByProduct_ProductId(Long productId);

    /**
     * Count cart items by cart ID
     * 
     * @param cartId the cart ID
     * @return number of items in the cart
     */
    long countByCart_CartId(Long cartId);

    /**
     * Count total quantity by cart ID
     * 
     * @param cartId the cart ID
     * @return total quantity of items in the cart
     */
    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.cart.cartId = :cartId")
    int sumQuantityByCart_CartId(@Param("cartId") Long cartId);

    /**
     * Count total quantity by user ID
     * 
     * @param userId the user ID
     * @return total quantity of items in user's cart
     */
    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci JOIN ci.cart c WHERE c.user.userId = :userId")
    int sumQuantityByUser_UserId(@Param("userId") Long userId);

    /**
     * Find cart items with quantity greater than specified value
     * 
     * @param quantity the minimum quantity
     * @return list of cart items with quantity greater than specified
     */
    List<CartItem> findByQuantityGreaterThan(Integer quantity);

    /**
     * Find cart items by cart ID ordered by creation date
     * 
     * @param cartId the cart ID
     * @return list of cart items ordered by creation date
     */
    List<CartItem> findByCart_CartIdOrderByCreatedAtAsc(Long cartId);

    /**
     * Find cart items by cart ID ordered by product name
     * 
     * @param cartId the cart ID
     * @return list of cart items ordered by product name
     */
    @Query("SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product WHERE ci.cart.cartId = :cartId ORDER BY ci.product.name ASC")
    List<CartItem> findByCart_CartIdOrderByProductNameAsc(@Param("cartId") Long cartId);

    /**
     * Delete cart items by cart ID
     * 
     * @param cartId the cart ID
     */
    void deleteByCart_CartId(Long cartId);

    /**
     * Delete cart item by cart ID and product ID
     * 
     * @param cartId the cart ID
     * @param productId the product ID
     */
    void deleteByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);

    /**
     * Delete cart items by user ID
     * 
     * @param userId the user ID
     */
    @Query("DELETE FROM CartItem ci WHERE ci.cart IN (SELECT c FROM Cart c WHERE c.user.userId = :userId)")
    void deleteByUser_UserId(@Param("userId") Long userId);

    /**
     * Find cart items with inactive products
     * 
     * @return list of cart items with inactive products
     */
    @Query("SELECT ci FROM CartItem ci JOIN ci.product p WHERE p.isActive = false")
    List<CartItem> findCartItemsWithInactiveProducts();

    /**
     * Find cart items with products out of stock
     * 
     * @return list of cart items with products that have no stock
     */
    @Query("SELECT ci FROM CartItem ci JOIN ci.product p WHERE p.stockQuantity = 0")
    List<CartItem> findCartItemsWithOutOfStockProducts();

    /**
     * Find cart items with insufficient stock
     * 
     * @return list of cart items where requested quantity exceeds available stock
     */
    @Query("SELECT ci FROM CartItem ci JOIN ci.product p WHERE ci.quantity > p.stockQuantity")
    List<CartItem> findCartItemsWithInsufficientStock();

    /**
     * Update quantity for cart item
     * 
     * @param cartItemId the cart item ID
     * @param quantity the new quantity
     */
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.cartItemId = :cartItemId")
    void updateQuantityByCartItemId(@Param("cartItemId") Long cartItemId, @Param("quantity") Integer quantity);

    /**
     * Find cart items created after specific date
     * 
     * @param date the date threshold
     * @return list of cart items created after the date
     */
    List<CartItem> findByCreatedAtAfter(java.time.LocalDateTime date);

    /**
     * Find cart items with subtotal greater than specified value
     * 
     * @param subtotal the minimum subtotal
     * @return list of cart items with subtotal greater than specified
     */
    @Query("SELECT ci FROM CartItem ci WHERE (ci.quantity * ci.product.price) > :subtotal")
    List<CartItem> findCartItemsWithSubtotalGreaterThan(@Param("subtotal") java.math.BigDecimal subtotal);
}
