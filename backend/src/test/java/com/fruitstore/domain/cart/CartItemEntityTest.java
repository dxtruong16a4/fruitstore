package com.fruitstore.domain.cart;

import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration test for CartItem entity mapping
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class CartItemEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Cart cart;
    private Category category;
    private Product product;
    private CartItem cartItem;

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

        // Create test category
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);

        // Create test product
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("100000.00"));
        product.setStockQuantity(10);
        product.setCategory(category);
        product.setIsActive(true);

        // Create test cart item
        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
    }

    @Test
    public void testCartItemEntityMapping() {
        // When
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);
        Product savedProduct = entityManager.persistAndFlush(product);

        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);
        CartItem savedCartItem = entityManager.persistAndFlush(cartItem);

        // Then
        assertThat(savedCartItem.getCartItemId()).isNotNull();
        assertThat(savedCartItem.getCart()).isNotNull();
        assertThat(savedCartItem.getCart().getCartId()).isEqualTo(savedCart.getCartId());
        assertThat(savedCartItem.getProduct()).isNotNull();
        assertThat(savedCartItem.getProduct().getProductId()).isEqualTo(savedProduct.getProductId());
        assertThat(savedCartItem.getQuantity()).isEqualTo(2);
        assertThat(savedCartItem.getCreatedAt()).isNotNull();
    }

    @Test
    public void testCartItemSubtotalCalculation() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);
        Product savedProduct = entityManager.persistAndFlush(product);

        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);
        cartItem.setQuantity(3);

        // When
        CartItem savedCartItem = entityManager.persistAndFlush(cartItem);

        // Then
        BigDecimal expectedSubtotal = new BigDecimal("100000.00").multiply(new BigDecimal("3"));
        assertThat(savedCartItem.getSubtotal()).isEqualByComparingTo(expectedSubtotal);
    }

    @Test
    public void testCartItemBusinessLogic() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);
        Product savedProduct = entityManager.persistAndFlush(product);

        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);
        CartItem savedCartItem = entityManager.persistAndFlush(cartItem);

        // When & Then
        assertThat(savedCartItem.isProductAvailable()).isTrue();
        assertThat(savedCartItem.hasSufficientStock()).isTrue();

        // Test update quantity
        savedCartItem.updateQuantity(5);
        assertThat(savedCartItem.getQuantity()).isEqualTo(5);

        // Test increase quantity
        savedCartItem.increaseQuantity(2);
        assertThat(savedCartItem.getQuantity()).isEqualTo(7);

        // Test decrease quantity
        savedCartItem.decreaseQuantity(1);
        assertThat(savedCartItem.getQuantity()).isEqualTo(6);
    }

    @Test
    public void testCartItemValidation() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);
        Product savedProduct = entityManager.persistAndFlush(product);

        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);

        // When & Then - Test invalid quantity update
        assertThatThrownBy(() -> cartItem.updateQuantity(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be at least 1");

        assertThatThrownBy(() -> cartItem.updateQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be at least 1");

        assertThatThrownBy(() -> cartItem.updateQuantity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be at least 1");
    }

    @Test
    public void testCartItemQuantityOperations() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);
        Product savedProduct = entityManager.persistAndFlush(product);

        cartItem.setCart(savedCart);
        cartItem.setProduct(savedProduct);
        cartItem.setQuantity(5);
        CartItem savedCartItem = entityManager.persistAndFlush(cartItem);

        // When & Then - Test increase quantity
        savedCartItem.increaseQuantity(3);
        assertThat(savedCartItem.getQuantity()).isEqualTo(8);

        // Test invalid increase
        assertThatThrownBy(() -> savedCartItem.increaseQuantity(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be at least 1");

        // Test decrease quantity
        savedCartItem.decreaseQuantity(2);
        assertThat(savedCartItem.getQuantity()).isEqualTo(6);

        // Test invalid decrease
        assertThatThrownBy(() -> savedCartItem.decreaseQuantity(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity cannot be less than 1");
    }

    @Test
    public void testCartItemStockValidation() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);

        // Create product with low stock
        Product lowStockProduct = new Product();
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setDescription("Test Description");
        lowStockProduct.setPrice(new BigDecimal("50000.00"));
        lowStockProduct.setStockQuantity(2);
        lowStockProduct.setCategory(category);
        lowStockProduct.setIsActive(true);
        Product savedLowStockProduct = entityManager.persistAndFlush(lowStockProduct);

        CartItem lowStockCartItem = new CartItem();
        lowStockCartItem.setCart(savedCart);
        lowStockCartItem.setProduct(savedLowStockProduct);
        lowStockCartItem.setQuantity(3); // More than available stock

        // When
        CartItem savedLowStockCartItem = entityManager.persistAndFlush(lowStockCartItem);

        // Then
        assertThat(savedLowStockCartItem.hasSufficientStock()).isFalse();
        assertThat(savedLowStockCartItem.isProductAvailable()).isTrue(); // Product is still active
    }

    @Test
    public void testCartItemWithInactiveProduct() {
        // Given
        entityManager.persistAndFlush(user);
        Cart savedCart = entityManager.persistAndFlush(cart);
        entityManager.persistAndFlush(category);

        // Create inactive product
        Product inactiveProduct = new Product();
        inactiveProduct.setName("Inactive Product");
        inactiveProduct.setDescription("Test Description");
        inactiveProduct.setPrice(new BigDecimal("75000.00"));
        inactiveProduct.setStockQuantity(10);
        inactiveProduct.setCategory(category);
        inactiveProduct.setIsActive(false); // Inactive product
        Product savedInactiveProduct = entityManager.persistAndFlush(inactiveProduct);

        CartItem inactiveCartItem = new CartItem();
        inactiveCartItem.setCart(savedCart);
        inactiveCartItem.setProduct(savedInactiveProduct);
        inactiveCartItem.setQuantity(1);

        // When
        CartItem savedInactiveCartItem = entityManager.persistAndFlush(inactiveCartItem);

        // Then
        assertThat(savedInactiveCartItem.isProductAvailable()).isFalse();
        assertThat(savedInactiveCartItem.hasSufficientStock()).isFalse();
    }

    @Test
    public void testCartItemSubtotalWithNullValues() {
        // Given
        CartItem cartItemWithNulls = new CartItem();
        cartItemWithNulls.setQuantity(2);

        // When & Then
        assertThat(cartItemWithNulls.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);

        // Test with null quantity
        cartItemWithNulls.setQuantity(null);
        assertThat(cartItemWithNulls.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
