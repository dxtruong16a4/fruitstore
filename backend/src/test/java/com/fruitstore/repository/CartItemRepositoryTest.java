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
 * Integration test for CartItemRepository
 */
@DataJpaTest
@ActiveProfiles("test")
public class CartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartItemRepository cartItemRepository;


    private User user1;
    private User user2;
    private Cart cart1;
    private Cart cart2;
    private Category category;
    private Product product1;
    private Product product2;
    private CartItem cartItem1;
    private CartItem cartItem2;

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

        // Create test category
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);

        // Create test products
        product1 = new Product();
        product1.setName("Product One");
        product1.setDescription("Test Description");
        product1.setPrice(new BigDecimal("100000.00"));
        product1.setStockQuantity(10);
        product1.setCategory(category);
        product1.setIsActive(true);

        product2 = new Product();
        product2.setName("Product Two");
        product2.setDescription("Test Description");
        product2.setPrice(new BigDecimal("150000.00"));
        product2.setStockQuantity(5);
        product2.setCategory(category);
        product2.setIsActive(true);

        // Create test cart items
        cartItem1 = new CartItem();
        cartItem1.setCart(cart1);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(2);

        cartItem2 = new CartItem();
        cartItem2.setCart(cart1);
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(1);

        // Persist entities
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.persistAndFlush(cart1);
        entityManager.persistAndFlush(cart2);
        entityManager.persistAndFlush(category);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(cartItem1);
        entityManager.persistAndFlush(cartItem2);
    }

    @Test
    public void testFindByCart_CartId() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cart1.getCartId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems).extracting(CartItem::getCartItemId)
                .contains(cartItem1.getCartItemId(), cartItem2.getCartItemId());
    }

    @Test
    public void testFindByCart_CartIdAndProduct_ProductId() {
        // When
        Optional<CartItem> foundItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(
                cart1.getCartId(), product1.getProductId());

        // Then
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getCartItemId()).isEqualTo(cartItem1.getCartItemId());
        assertThat(foundItem.get().getQuantity()).isEqualTo(2);
    }

    @Test
    public void testFindByCart_CartIdAndProduct_ProductIdNotFound() {
        // When
        Optional<CartItem> foundItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(
                cart2.getCartId(), product1.getProductId());

        // Then
        assertThat(foundItem).isEmpty();
    }

    @Test
    public void testFindByCart_CartIdWithProducts() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByCart_CartIdWithProducts(cart1.getCartId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.get(0).getProduct()).isNotNull();
        assertThat(cartItems.get(0).getProduct().getName()).isIn("Product One", "Product Two");
        assertThat(cartItems.get(0).getProduct().getCategory()).isNotNull();
    }

    @Test
    public void testFindByUser_UserId() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByUser_UserId(user1.getUserId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems).extracting(CartItem::getCartItemId)
                .contains(cartItem1.getCartItemId(), cartItem2.getCartItemId());
    }

    @Test
    public void testFindByUser_UserIdWithProducts() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByUser_UserIdWithProducts(user1.getUserId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.get(0).getProduct()).isNotNull();
        assertThat(cartItems.get(0).getProduct().getCategory()).isNotNull();
    }

    @Test
    public void testFindByProduct_ProductId() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByProduct_ProductId(product1.getProductId());

        // Then
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.get(0).getCartItemId()).isEqualTo(cartItem1.getCartItemId());
    }

    @Test
    public void testCountByCart_CartId() {
        // When
        long count = cartItemRepository.countByCart_CartId(cart1.getCartId());

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testSumQuantityByCart_CartId() {
        // When
        int totalQuantity = cartItemRepository.sumQuantityByCart_CartId(cart1.getCartId());

        // Then
        assertThat(totalQuantity).isEqualTo(3); // 2 + 1
    }

    @Test
    public void testSumQuantityByUser_UserId() {
        // When
        int totalQuantity = cartItemRepository.sumQuantityByUser_UserId(user1.getUserId());

        // Then
        assertThat(totalQuantity).isEqualTo(3); // 2 + 1
    }

    @Test
    public void testFindByQuantityGreaterThan() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByQuantityGreaterThan(1);

        // Then
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.get(0).getCartItemId()).isEqualTo(cartItem1.getCartItemId());
    }

    @Test
    public void testFindByCart_CartIdOrderByCreatedAtAsc() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByCart_CartIdOrderByCreatedAtAsc(cart1.getCartId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.get(0).getCreatedAt()).isBeforeOrEqualTo(cartItems.get(1).getCreatedAt());
    }

    @Test
    public void testFindByCart_CartIdOrderByProductNameAsc() {
        // When
        List<CartItem> cartItems = cartItemRepository.findByCart_CartIdOrderByProductNameAsc(cart1.getCartId());

        // Then
        assertThat(cartItems).hasSize(2);
        assertThat(cartItems.get(0).getProduct().getName()).isEqualTo("Product One");
        assertThat(cartItems.get(1).getProduct().getName()).isEqualTo("Product Two");
    }

    @Test
    public void testDeleteByCart_CartId() {
        // Given
        assertThat(cartItemRepository.countByCart_CartId(cart1.getCartId())).isEqualTo(2);

        // When
        cartItemRepository.deleteByCart_CartId(cart1.getCartId());

        // Then
        assertThat(cartItemRepository.countByCart_CartId(cart1.getCartId())).isEqualTo(0);
    }

    @Test
    public void testDeleteByCart_CartIdAndProduct_ProductId() {
        // Given
        assertThat(cartItemRepository.countByCart_CartId(cart1.getCartId())).isEqualTo(2);

        // When
        cartItemRepository.deleteByCart_CartIdAndProduct_ProductId(cart1.getCartId(), product1.getProductId());

        // Then
        assertThat(cartItemRepository.countByCart_CartId(cart1.getCartId())).isEqualTo(1);
    }

    @Test
    public void testDeleteByUser_UserId() {
        // Given
        assertThat(cartItemRepository.findByUser_UserId(user1.getUserId())).hasSize(2);

        // When
        cartItemRepository.deleteByUser_UserId(user1.getUserId());

        // Then
        assertThat(cartItemRepository.findByUser_UserId(user1.getUserId())).isEmpty();
    }

    @Test
    public void testFindCartItemsWithInactiveProducts() {
        // Given
        Product inactiveProduct = new Product();
        inactiveProduct.setName("Inactive Product");
        inactiveProduct.setDescription("Test Description");
        inactiveProduct.setPrice(new BigDecimal("200000.00"));
        inactiveProduct.setStockQuantity(5);
        inactiveProduct.setCategory(category);
        inactiveProduct.setIsActive(false);
        Product savedInactiveProduct = entityManager.persistAndFlush(inactiveProduct);

        CartItem inactiveCartItem = new CartItem();
        inactiveCartItem.setCart(cart2);
        inactiveCartItem.setProduct(savedInactiveProduct);
        inactiveCartItem.setQuantity(1);
        entityManager.persistAndFlush(inactiveCartItem);

        // When
        List<CartItem> inactiveItems = cartItemRepository.findCartItemsWithInactiveProducts();

        // Then
        assertThat(inactiveItems).hasSize(1);
        assertThat(inactiveItems.get(0).getProduct().getIsActive()).isFalse();
    }

    @Test
    public void testFindCartItemsWithOutOfStockProducts() {
        // Given
        Product outOfStockProduct = new Product();
        outOfStockProduct.setName("Out of Stock Product");
        outOfStockProduct.setDescription("Test Description");
        outOfStockProduct.setPrice(new BigDecimal("300000.00"));
        outOfStockProduct.setStockQuantity(0); // Out of stock
        outOfStockProduct.setCategory(category);
        outOfStockProduct.setIsActive(true);
        Product savedOutOfStockProduct = entityManager.persistAndFlush(outOfStockProduct);

        CartItem outOfStockCartItem = new CartItem();
        outOfStockCartItem.setCart(cart2);
        outOfStockCartItem.setProduct(savedOutOfStockProduct);
        outOfStockCartItem.setQuantity(1);
        entityManager.persistAndFlush(outOfStockCartItem);

        // When
        List<CartItem> outOfStockItems = cartItemRepository.findCartItemsWithOutOfStockProducts();

        // Then
        assertThat(outOfStockItems).hasSize(1);
        assertThat(outOfStockItems.get(0).getProduct().getStockQuantity()).isEqualTo(0);
    }

    @Test
    public void testFindCartItemsWithInsufficientStock() {
        // Given
        Product lowStockProduct = new Product();
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setDescription("Test Description");
        lowStockProduct.setPrice(new BigDecimal("400000.00"));
        lowStockProduct.setStockQuantity(2); // Low stock
        lowStockProduct.setCategory(category);
        lowStockProduct.setIsActive(true);
        Product savedLowStockProduct = entityManager.persistAndFlush(lowStockProduct);

        CartItem insufficientStockItem = new CartItem();
        insufficientStockItem.setCart(cart2);
        insufficientStockItem.setProduct(savedLowStockProduct);
        insufficientStockItem.setQuantity(5); // Requesting more than available
        entityManager.persistAndFlush(insufficientStockItem);

        // When
        List<CartItem> insufficientStockItems = cartItemRepository.findCartItemsWithInsufficientStock();

        // Then
        assertThat(insufficientStockItems).hasSize(1);
        assertThat(insufficientStockItems.get(0).getQuantity()).isGreaterThan(
                insufficientStockItems.get(0).getProduct().getStockQuantity());
    }

    @Test
    public void testFindByCreatedAtAfter() {
        // Given
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);

        // When
        List<CartItem> recentItems = cartItemRepository.findByCreatedAtAfter(threshold);

        // Then
        assertThat(recentItems).hasSize(2);
        assertThat(recentItems).extracting(CartItem::getCartItemId)
                .contains(cartItem1.getCartItemId(), cartItem2.getCartItemId());
    }

    @Test
    public void testFindCartItemsWithSubtotalGreaterThan() {
        // When
        List<CartItem> highSubtotalItems = cartItemRepository.findCartItemsWithSubtotalGreaterThan(
                new BigDecimal("120000.00"));

        // Then
        assertThat(highSubtotalItems).hasSize(1);
        assertThat(highSubtotalItems.get(0).getCartItemId()).isEqualTo(cartItem2.getCartItemId());
    }
}
