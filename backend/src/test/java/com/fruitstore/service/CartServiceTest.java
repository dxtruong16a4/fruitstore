package com.fruitstore.service;

import com.fruitstore.domain.cart.Cart;
import com.fruitstore.domain.cart.CartItem;
import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.request.cart.AddToCartRequest;
import com.fruitstore.dto.request.cart.UpdateCartItemRequest;
import com.fruitstore.dto.response.cart.CartResponse;
import com.fruitstore.repository.CartItemRepository;
import com.fruitstore.repository.CartRepository;
import com.fruitstore.repository.ProductRepository;
import com.fruitstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for CartService
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Category category;
    private Product product1;
    private Product product2;
    private Cart cart;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private AddToCartRequest addToCartRequest;
    private UpdateCartItemRequest updateCartItemRequest;

    @BeforeEach
    public void setUp() {
        // Create test user
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setRole(UserRole.CUSTOMER);

        // Create test category
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);

        // Create test products
        product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Product One");
        product1.setDescription("Test Description");
        product1.setPrice(new BigDecimal("100000.00"));
        product1.setStockQuantity(10);
        product1.setCategory(category);
        product1.setIsActive(true);

        product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Product Two");
        product2.setDescription("Test Description");
        product2.setPrice(new BigDecimal("150000.00"));
        product2.setStockQuantity(5);
        product2.setCategory(category);
        product2.setIsActive(true);

        // Create test cart
        cart = new Cart();
        cart.setCartId(1L);
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        // Create test cart items
        cartItem1 = new CartItem();
        cartItem1.setCartItemId(1L);
        cartItem1.setCart(cart);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(2);
        cartItem1.setCreatedAt(LocalDateTime.now());

        cartItem2 = new CartItem();
        cartItem2.setCartItemId(2L);
        cartItem2.setCart(cart);
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(1);
        cartItem2.setCreatedAt(LocalDateTime.now());

        // Set cart items
        cart.setCartItems(Arrays.asList(cartItem1, cartItem2));

        // Create test requests
        addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId(1L);
        addToCartRequest.setQuantity(2);

        updateCartItemRequest = new UpdateCartItemRequest();
        updateCartItemRequest.setQuantity(3);
    }

    @Test
    public void testGetCartByUserId() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));

        // When
        CartResponse result = cartService.getCartByUserId(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCartId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItemCount()).isEqualTo(2);
        assertThat(result.getTotalItems()).isEqualTo(3);
        assertThat(result.isEmpty()).isFalse();
        verify(userRepository).findById(1L);
        verify(cartRepository).findByUser_UserIdWithItemsAndProducts(1L);
    }

    @Test
    public void testGetCartByUserIdUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.getCartByUserId(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found with id: 999");
        verify(userRepository).findById(999L);
        verify(cartRepository, never()).findByUser_UserIdWithItemsAndProducts(anyLong());
    }

    @Test
    public void testGetCartByUserIdCreateNewCart() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.empty());
        when(cartRepository.findByUser_UserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        CartResponse result = cartService.getCartByUserId(1L);

        // Then
        assertThat(result).isNotNull();
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    public void testAddItemToCart() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart_CartIdAndProduct_ProductId(1L, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem1);

        // When
        CartResponse result = cartService.addItemToCart(1L, addToCartRequest);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(cartItemRepository).findByCart_CartIdAndProduct_ProductId(1L, 1L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    public void testAddItemToCartProductNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.addItemToCart(1L, addToCartRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found with id: 999");
        verify(userRepository).findById(1L);
        verify(productRepository).findById(999L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testAddItemToCartInactiveProduct() {
        // Given
        product1.setIsActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When & Then
        assertThatThrownBy(() -> cartService.addItemToCart(1L, addToCartRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product is not active with id: 1");
        verify(userRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testAddItemToCartInsufficientStock() {
        // Given
        addToCartRequest.setQuantity(15); // More than available stock (10)
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When & Then
        assertThatThrownBy(() -> cartService.addItemToCart(1L, addToCartRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");
        verify(userRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testAddItemToCartUpdateExistingItem() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart_CartIdAndProduct_ProductId(1L, 1L)).thenReturn(Optional.of(cartItem1));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem1);

        // When
        CartResponse result = cartService.addItemToCart(1L, addToCartRequest);

        // Then
        assertThat(result).isNotNull();
        verify(cartItemRepository).save(cartItem1);
    }

    @Test
    public void testUpdateCartItem() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem1);

        // When
        CartResponse result = cartService.updateCartItem(1L, 1L, updateCartItemRequest);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).save(cartItem1);
    }

    @Test
    public void testUpdateCartItemNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 999L, updateCartItemRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cart item not found with id: 999");
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(999L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testUpdateCartItemWrongUser() {
        // Given
        User otherUser = new User();
        otherUser.setUserId(2L);
        cart.setUser(otherUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem1));

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 1L, updateCartItemRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cart item does not belong to user with id: 1");
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testRemoveCartItem() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem1));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));

        // When
        CartResponse result = cartService.removeCartItem(1L, 1L);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).delete(cartItem1);
    }

    @Test
    public void testClearCart() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.findByUser_UserIdWithItemsAndProducts(1L)).thenReturn(Optional.of(cart));

        // When
        CartResponse result = cartService.clearCart(1L);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(cartRepository).findByUser_UserId(1L);
        verify(cartItemRepository).deleteByCart_CartId(1L);
    }

    @Test
    public void testCalculateCartTotal() {
        // Given
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.findByUser_UserIdWithItems(1L)).thenReturn(Optional.of(cart));

        // When
        BigDecimal total = cartService.calculateCartTotal(1L);

        // Then
        // Expected: (2 * 100000) + (1 * 150000) = 350000
        assertThat(total).isEqualByComparingTo(new BigDecimal("350000.00"));
        verify(cartRepository).findById(1L);
        verify(cartRepository).findByUser_UserIdWithItems(1L);
    }

    @Test
    public void testCalculateCartTotalCartNotFound() {
        // Given
        when(cartRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.calculateCartTotal(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cart not found with id: 999");
        verify(cartRepository).findById(999L);
    }

    @Test
    public void testGetCartSummary() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.countDifferentProductsByUserId(1L)).thenReturn(2);
        when(cartRepository.countTotalItemsByUserId(1L)).thenReturn(3);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.findByUser_UserIdWithItems(1L)).thenReturn(Optional.of(cart));

        // When
        CartResponse result = cartService.getCartSummary(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItemCount()).isEqualTo(2);
        assertThat(result.getTotalItems()).isEqualTo(3);
        assertThat(result.getItems()).isNull(); // No items for summary
        verify(userRepository).findById(1L);
        verify(cartRepository).findByUser_UserId(1L);
        verify(cartRepository).countDifferentProductsByUserId(1L);
        verify(cartRepository).countTotalItemsByUserId(1L);
    }

    @Test
    public void testGetCartSummaryEmptyCart() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_UserId(1L)).thenReturn(Optional.empty());

        // When
        CartResponse result = cartService.getCartSummary(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getItemCount()).isEqualTo(0);
        assertThat(result.getTotalItems()).isEqualTo(0);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void testHasItems() {
        // Given
        when(cartRepository.countDifferentProductsByUserId(1L)).thenReturn(2);

        // When
        boolean result = cartService.hasItems(1L);

        // Then
        assertThat(result).isTrue();
        verify(cartRepository).countDifferentProductsByUserId(1L);
    }

    @Test
    public void testGetItemCount() {
        // Given
        when(cartRepository.countDifferentProductsByUserId(1L)).thenReturn(3);

        // When
        int result = cartService.getItemCount(1L);

        // Then
        assertThat(result).isEqualTo(3);
        verify(cartRepository).countDifferentProductsByUserId(1L);
    }

    @Test
    public void testGetTotalItems() {
        // Given
        when(cartRepository.countTotalItemsByUserId(1L)).thenReturn(5);

        // When
        int result = cartService.getTotalItems(1L);

        // Then
        assertThat(result).isEqualTo(5);
        verify(cartRepository).countTotalItemsByUserId(1L);
    }

    @Test
    public void testUpdateCartItemInactiveProduct() {
        // Given
        product1.setIsActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem1));

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 1L, updateCartItemRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product is no longer active");
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testUpdateCartItemInsufficientStock() {
        // Given
        updateCartItemRequest.setQuantity(15); // More than available stock (10)
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem1));

        // When & Then
        assertThatThrownBy(() -> cartService.updateCartItem(1L, 1L, updateCartItemRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");
        verify(userRepository).findById(1L);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }
}
