package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.dto.request.cart.AddToCartRequest;
import com.fruitstore.dto.request.cart.UpdateCartItemRequest;
import com.fruitstore.dto.response.cart.CartResponse;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CartController
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "app.jwt.secret=testSecretKeyForJWTTokenGenerationAndValidationInTests",
    "app.jwt.expiration=86400000",
    "app.cors.allowed-origins=http://localhost:3000",
    "app.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS",
    "app.cors.allowed-headers=*",
    "app.cors.allow-credentials=true"
})
public class CartControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CustomUserDetails userDetails;
    private CartResponse cartResponse;
    private AddToCartRequest addToCartRequest;
    private UpdateCartItemRequest updateCartItemRequest;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        // Create test user and user details
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setRole(UserRole.CUSTOMER);
        userDetails = new CustomUserDetails(user);

        // Create test cart response
        cartResponse = new CartResponse();
        cartResponse.setCartId(1L);
        cartResponse.setUserId(1L);
        cartResponse.setItemCount(2);
        cartResponse.setTotalItems(3);
        cartResponse.setSubtotal(new BigDecimal("250000.00"));
        cartResponse.setTotalAmount(new BigDecimal("275000.00"));
        cartResponse.setEmpty(false);
        cartResponse.setCreatedAt(LocalDateTime.now());
        cartResponse.setUpdatedAt(LocalDateTime.now());

        // Create test requests
        addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId(1L);
        addToCartRequest.setQuantity(2);

        updateCartItemRequest = new UpdateCartItemRequest();
        updateCartItemRequest.setQuantity(3);
    }

    @Test
    public void testGetCart() throws Exception {
        // Given
        when(cartService.getCartByUserId(1L)).thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(get("/api/cart")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.itemCount").value(2))
                .andExpect(jsonPath("$.data.totalItems").value(3))
                .andExpect(jsonPath("$.data.subtotal").value(250000.00))
                .andExpect(jsonPath("$.data.totalAmount").value(275000.00))
                .andExpect(jsonPath("$.data.empty").value(false));
    }

    @Test
    public void testGetCartSummary() throws Exception {
        // Given
        when(cartService.getCartSummary(1L)).thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(get("/api/cart/summary")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.data.itemCount").value(2));
    }

    @Test
    public void testAddItemToCart() throws Exception {
        // Given
        when(cartService.addItemToCart(anyLong(), any(AddToCartRequest.class))).thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Item added to cart successfully"))
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    public void testAddItemToCartValidation() throws Exception {
        // Given
        AddToCartRequest invalidRequest = new AddToCartRequest();
        invalidRequest.setProductId(null); // Invalid - null product ID
        invalidRequest.setQuantity(0); // Invalid - quantity must be at least 1

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateCartItem() throws Exception {
        // Given
        when(cartService.updateCartItem(anyLong(), anyLong(), any(UpdateCartItemRequest.class)))
                .thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(put("/api/cart/items/1")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cart item updated successfully"))
                .andExpect(jsonPath("$.data.cartId").value(1));
    }

    @Test
    public void testUpdateCartItemValidation() throws Exception {
        // Given
        UpdateCartItemRequest invalidRequest = new UpdateCartItemRequest();
        invalidRequest.setQuantity(0); // Invalid - quantity must be at least 1

        // When & Then
        mockMvc.perform(put("/api/cart/items/1")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveCartItem() throws Exception {
        // Given
        when(cartService.removeCartItem(anyLong(), anyLong())).thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(delete("/api/cart/items/1")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Item removed from cart successfully"))
                .andExpect(jsonPath("$.data.cartId").value(1));
    }

    @Test
    public void testClearCart() throws Exception {
        // Given
        when(cartService.clearCart(anyLong())).thenReturn(cartResponse);

        // When & Then
        mockMvc.perform(delete("/api/cart/clear")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cart cleared successfully"))
                .andExpect(jsonPath("$.data.cartId").value(1));
    }

    @Test
    public void testGetCartTotal() throws Exception {
        // Given
        BigDecimal total = new BigDecimal("275000.00");
        when(cartService.getCartByUserId(1L)).thenReturn(cartResponse);
        when(cartService.calculateCartTotal(1L)).thenReturn(total);

        // When & Then
        mockMvc.perform(get("/api/cart/total")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(275000.00));
    }

    @Test
    public void testHasItems() throws Exception {
        // Given
        when(cartService.hasItems(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/cart/has-items")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testGetItemCount() throws Exception {
        // Given
        when(cartService.getItemCount(1L)).thenReturn(2);

        // When & Then
        mockMvc.perform(get("/api/cart/item-count")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(2));
    }

    @Test
    public void testGetTotalItems() throws Exception {
        // Given
        when(cartService.getTotalItems(1L)).thenReturn(3);

        // When & Then
        mockMvc.perform(get("/api/cart/total-items")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(3));
    }

    @Test
    public void testGetCartStats() throws Exception {
        // Given
        BigDecimal total = new BigDecimal("275000.00");
        when(cartService.getCartByUserId(1L)).thenReturn(cartResponse);
        when(cartService.calculateCartTotal(1L)).thenReturn(total);

        // When & Then
        mockMvc.perform(get("/api/cart/stats")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.itemCount").value(2))
                .andExpect(jsonPath("$.data.totalItems").value(3))
                .andExpect(jsonPath("$.data.subtotal").value(250000.00))
                .andExpect(jsonPath("$.data.totalAmount").value(275000.00))
                .andExpect(jsonPath("$.data.isEmpty").value(false));
    }

    @Test
    public void testCartStatsResponseToString() {
        // Given
        CartController.CartStatsResponse stats = new CartController.CartStatsResponse(
                2, 3, new BigDecimal("250000.00"), new BigDecimal("275000.00"), false
        );

        // When
        String result = stats.toString();

        // Then
        assert result.contains("CartStatsResponse");
        assert result.contains("itemCount=2");
        assert result.contains("totalItems=3");
        assert result.contains("subtotal=250000.00");
        assert result.contains("totalAmount=275000.00");
        assert result.contains("isEmpty=false");
    }

    @Test
    public void testCartStatsResponseSettersAndGetters() {
        // Given
        CartController.CartStatsResponse stats = new CartController.CartStatsResponse();

        // When
        stats.setItemCount(5);
        stats.setTotalItems(10);
        stats.setSubtotal(new BigDecimal("500000.00"));
        stats.setTotalAmount(new BigDecimal("550000.00"));
        stats.setIsEmpty(false);

        // Then
        assert stats.getItemCount() == 5;
        assert stats.getTotalItems() == 10;
        assert stats.getSubtotal().equals(new BigDecimal("500000.00"));
        assert stats.getTotalAmount().equals(new BigDecimal("550000.00"));
        assert !stats.getIsEmpty();
    }

    @Test
    public void testCartStatsResponseConstructor() {
        // Given
        CartController.CartStatsResponse stats = new CartController.CartStatsResponse(
                1, 2, new BigDecimal("100000.00"), new BigDecimal("110000.00"), true
        );

        // Then
        assert stats.getItemCount() == 1;
        assert stats.getTotalItems() == 2;
        assert stats.getSubtotal().equals(new BigDecimal("100000.00"));
        assert stats.getTotalAmount().equals(new BigDecimal("110000.00"));
        assert stats.getIsEmpty();
    }
}
