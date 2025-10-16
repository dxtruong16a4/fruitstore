package com.fruitstore.domain.cart;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for Cart entity mapping
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class CartEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        // Create test user
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setRole(UserRole.CUSTOMER);

        // Create test cart
        cart = new Cart();
        cart.setUser(user);
    }

    @Test
    public void testCartEntityMapping() {
        // When
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);

        // Then
        assertThat(savedCart.getCartId()).isNotNull();
        assertThat(savedCart.getUser()).isNotNull();
        assertThat(savedCart.getUser().getUserId()).isNotNull();
        assertThat(savedCart.getCartItems()).isEmpty();
        assertThat(savedCart.getCreatedAt()).isNotNull();
        assertThat(savedCart.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testCartWithCartItems() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);

        // Create product
        com.fruitstore.domain.product.Category category = new com.fruitstore.domain.product.Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);
        com.fruitstore.domain.product.Category savedCategory = entityManager.persistAndFlush(category);

        com.fruitstore.domain.product.Product product = new com.fruitstore.domain.product.Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(java.math.BigDecimal.valueOf(100000));
        product.setStockQuantity(10);
        product.setCategory(savedCategory);
        product.setIsActive(true);
        com.fruitstore.domain.product.Product savedProduct = entityManager.persistAndFlush(product);

        // Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);
        cartItem.setQuantity(2);

        // When
        savedCart.addCartItem(cartItem);
        CartItem savedCartItem = entityManager.persistAndFlush(cartItem);
        entityManager.refresh(savedCart);

        // Then
        assertThat(savedCartItem.getCartItemId()).isNotNull();
        assertThat(savedCartItem.getCart().getCartId()).isEqualTo(savedCart.getCartId());
        assertThat(savedCartItem.getProduct().getProductId()).isEqualTo(savedProduct.getProductId());
        assertThat(savedCartItem.getQuantity()).isEqualTo(2);
        assertThat(savedCart.getCartItems()).hasSize(1);
        assertThat(savedCart.getTotalItems()).isEqualTo(2);
        assertThat(savedCart.getItemCount()).isEqualTo(1);
    }

    @Test
    public void testCartBusinessLogic() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);

        // When & Then
        assertThat(savedCart.isEmpty()).isTrue();
        assertThat(savedCart.getTotalItems()).isEqualTo(0);
        assertThat(savedCart.getItemCount()).isEqualTo(0);

        // Add cart item
        com.fruitstore.domain.product.Category category = new com.fruitstore.domain.product.Category();
        category.setName("Test Category");
        category.setIsActive(true);
        com.fruitstore.domain.product.Category savedCategory = entityManager.persistAndFlush(category);

        com.fruitstore.domain.product.Product product = new com.fruitstore.domain.product.Product();
        product.setName("Test Product");
        product.setPrice(java.math.BigDecimal.valueOf(50000));
        product.setStockQuantity(5);
        product.setCategory(savedCategory);
        product.setIsActive(true);
        com.fruitstore.domain.product.Product savedProduct = entityManager.persistAndFlush(product);

        CartItem cartItem = new CartItem(savedCart, savedProduct, 3);
        savedCart.addCartItem(cartItem);
        entityManager.persistAndFlush(cartItem);
        entityManager.refresh(savedCart);

        // Then
        assertThat(savedCart.isEmpty()).isFalse();
        assertThat(savedCart.getTotalItems()).isEqualTo(3);
        assertThat(savedCart.getItemCount()).isEqualTo(1);
    }

    @Test
    public void testCartTimestampFields() {
        // When
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);

        // Then
        assertThat(savedCart.getCreatedAt()).isNotNull();
        assertThat(savedCart.getUpdatedAt()).isNotNull();
        assertThat(savedCart.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(savedCart.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    public void testCartClear() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);

        // Add items
        com.fruitstore.domain.product.Category category = new com.fruitstore.domain.product.Category();
        category.setName("Test Category");
        category.setIsActive(true);
        com.fruitstore.domain.product.Category savedCategory = entityManager.persistAndFlush(category);

        com.fruitstore.domain.product.Product product = new com.fruitstore.domain.product.Product();
        product.setName("Test Product");
        product.setPrice(java.math.BigDecimal.valueOf(30000));
        product.setStockQuantity(10);
        product.setCategory(savedCategory);
        product.setIsActive(true);
        com.fruitstore.domain.product.Product savedProduct = entityManager.persistAndFlush(product);

        CartItem cartItem = new CartItem(savedCart, savedProduct, 2);
        savedCart.addCartItem(cartItem);
        entityManager.persistAndFlush(cartItem);
        entityManager.refresh(savedCart);

        assertThat(savedCart.getCartItems()).hasSize(1);

        // When
        savedCart.clearCart();

        // Then
        assertThat(savedCart.getCartItems()).isEmpty();
        assertThat(savedCart.isEmpty()).isTrue();
    }
}
