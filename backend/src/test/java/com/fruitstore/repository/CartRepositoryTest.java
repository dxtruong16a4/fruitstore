package com.fruitstore.repository;

import com.fruitstore.domain.cart.Cart;
import com.fruitstore.domain.cart.CartItem;
import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for CartRepository
 */
@DataJpaTest
@ActiveProfiles("test")
public class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private User user1;
    private User user2;
    private Cart cart1;
    private Cart cart2;

    @BeforeEach
    public void setUp() {
        // Create test users
        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");
        user1.setFullName("User One");
        user1.setRole(UserRole.CUSTOMER);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setFullName("User Two");
        user2.setRole(UserRole.CUSTOMER);

        // Create test carts
        cart1 = new Cart();
        cart1.setUser(user1);

        cart2 = new Cart();
        cart2.setUser(user2);

        // Persist users and carts
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.persistAndFlush(cart1);
        entityManager.persistAndFlush(cart2);
    }

    @Test
    public void testFindByUser_UserId() {
        // When
        Optional<Cart> foundCart = cartRepository.findByUser_UserId(user1.getUserId());

        // Then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getCartId()).isEqualTo(cart1.getCartId());
        assertThat(foundCart.get().getUser().getUserId()).isEqualTo(user1.getUserId());
    }

    @Test
    public void testFindByUser_UserIdNotFound() {
        // When
        Optional<Cart> foundCart = cartRepository.findByUser_UserId(999L);

        // Then
        assertThat(foundCart).isEmpty();
    }

    @Test
    public void testFindByUser_UserIdWithItems() {
        // Given
        Category category = createAndPersistCategory();
        Product product = createAndPersistProduct(category);
        
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart1);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        entityManager.persistAndFlush(cartItem);
        
        // Ensure the relationship is properly managed by adding to cart's items list
        cart1.addCartItem(cartItem);
        entityManager.flush();
        entityManager.clear(); // Clear the persistence context to ensure fresh fetch

        // When
        Optional<Cart> foundCart = cartRepository.findByUser_UserIdWithItems(user1.getUserId());

        // Then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getCartItems()).hasSize(1);
        assertThat(foundCart.get().getCartItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    public void testFindByUser_UserIdWithItemsAndProducts() {
        // Given
        Category category = createAndPersistCategory();
        Product product = createAndPersistProduct(category);
        
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart1);
        cartItem.setProduct(product);
        cartItem.setQuantity(3);
        entityManager.persistAndFlush(cartItem);
        
        // Ensure the relationship is properly managed by adding to cart's items list
        cart1.addCartItem(cartItem);
        entityManager.flush();
        entityManager.clear(); // Clear the persistence context to ensure fresh fetch

        // When
        Optional<Cart> foundCart = cartRepository.findByUser_UserIdWithItemsAndProducts(user1.getUserId());

        // Then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getCartItems()).hasSize(1);
        CartItem foundItem = foundCart.get().getCartItems().get(0);
        assertThat(foundItem.getProduct()).isNotNull();
        assertThat(foundItem.getProduct().getCategory()).isNotNull();
        assertThat(foundItem.getProduct().getName()).isEqualTo("Test Product");
    }

    @Test
    public void testExistsByUser_UserId() {
        // When
        boolean exists = cartRepository.existsByUser_UserId(user1.getUserId());
        boolean notExists = cartRepository.existsByUser_UserId(999L);

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    public void testDeleteByUser_UserId() {
        // Given
        assertThat(cartRepository.existsByUser_UserId(user1.getUserId())).isTrue();

        // When
        cartRepository.deleteByUser_UserId(user1.getUserId());

        // Then
        assertThat(cartRepository.existsByUser_UserId(user1.getUserId())).isFalse();
    }

    @Test
    public void testCountTotalItemsByUserId() {
        // Given
        Category category = createAndPersistCategory();
        Product product1 = createAndPersistProduct(category);
        Product product2 = createAndPersistProduct(category);
        
        CartItem cartItem1 = new CartItem(cart1, product1, 2);
        CartItem cartItem2 = new CartItem(cart1, product2, 3);
        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
        
        // Ensure the relationship is properly managed
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        entityManager.flush();

        // When
        int totalItems = cartRepository.countTotalItemsByUserId(user1.getUserId());

        // Then
        assertThat(totalItems).isEqualTo(5); // 2 + 3
    }

    @Test
    public void testCountDifferentProductsByUserId() {
        // Given
        Category category = createAndPersistCategory();
        Product product1 = createAndPersistProduct(category);
        Product product2 = createAndPersistProduct(category);
        
        CartItem cartItem1 = new CartItem(cart1, product1, 2);
        CartItem cartItem2 = new CartItem(cart1, product2, 3);
        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
        
        // Ensure the relationship is properly managed
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        entityManager.flush();

        // When
        int differentProducts = cartRepository.countDifferentProductsByUserId(user1.getUserId());

        // Then
        assertThat(differentProducts).isEqualTo(2);
    }

    @Test
    public void testFindCartsCreatedAfter() {
        // Given
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);

        // When
        List<Cart> carts = cartRepository.findCartsCreatedAfter(threshold);

        // Then
        assertThat(carts).hasSize(2);
        assertThat(carts).extracting(Cart::getCartId).contains(cart1.getCartId(), cart2.getCartId());
    }

    @Test
    public void testFindCartsUpdatedAfter() {
        // Given
        LocalDateTime threshold = LocalDateTime.now().minusHours(1);

        // When
        List<Cart> carts = cartRepository.findCartsUpdatedAfter(threshold);

        // Then
        assertThat(carts).hasSize(2);
        assertThat(carts).extracting(Cart::getCartId).contains(cart1.getCartId(), cart2.getCartId());
    }

    @Test
    public void testFindEmptyCarts() {
        // Given - cart1 and cart2 are empty initially

        // When
        List<Cart> emptyCarts = cartRepository.findEmptyCarts();

        // Then
        assertThat(emptyCarts).hasSize(2);
        assertThat(emptyCarts).extracting(Cart::getCartId).contains(cart1.getCartId(), cart2.getCartId());
    }

    @Test
    public void testFindCartsWithItemCountGreaterThan() {
        // Given
        Category category = createAndPersistCategory();
        Product product1 = createAndPersistProduct(category);
        Product product2 = createAndPersistProduct(category);
        
        CartItem cartItem1 = new CartItem(cart1, product1, 1);
        CartItem cartItem2 = new CartItem(cart1, product2, 1);
        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
        
        // Ensure the relationship is properly managed
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        entityManager.flush();

        // When
        List<Cart> carts = cartRepository.findCartsWithItemCountGreaterThan(1);

        // Then
        assertThat(carts).hasSize(1);
        assertThat(carts.get(0).getCartId()).isEqualTo(cart1.getCartId());
    }

    @Test
    public void testFindCartsWithTotalQuantityGreaterThan() {
        // Given
        Category category = createAndPersistCategory();
        Product product1 = createAndPersistProduct(category);
        Product product2 = createAndPersistProduct(category);
        
        CartItem cartItem1 = new CartItem(cart1, product1, 3);
        CartItem cartItem2 = new CartItem(cart1, product2, 2);
        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
        
        // Ensure the relationship is properly managed
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        entityManager.flush();

        // When
        List<Cart> carts = cartRepository.findCartsWithTotalQuantityGreaterThan(4);

        // Then
        assertThat(carts).hasSize(1);
        assertThat(carts.get(0).getCartId()).isEqualTo(cart1.getCartId());
    }

    // Helper methods
    private Category createAndPersistCategory() {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);
        return entityManager.persistAndFlush(category);
    }

    private Product createAndPersistProduct(Category category) {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("100000.00"));
        product.setStockQuantity(10);
        product.setCategory(category);
        product.setIsActive(true);
        return entityManager.persistAndFlush(product);
    }
}
