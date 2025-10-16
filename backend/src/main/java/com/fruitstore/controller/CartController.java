package com.fruitstore.controller;

import com.fruitstore.dto.request.cart.AddToCartRequest;
import com.fruitstore.dto.request.cart.UpdateCartItemRequest;
import com.fruitstore.dto.response.cart.CartResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST Controller for cart management
 * Handles cart operations, item management, and cart state
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Get current user's cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return cart response with all items
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        CartResponse cart = cartService.getCartByUserId(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    /**
     * Get cart summary (lightweight version)
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return cart summary response
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<CartResponse>> getCartSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        CartResponse cart = cartService.getCartSummary(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    /**
     * Add item to cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param request add to cart request
     * @return updated cart response
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AddToCartRequest request) {
        
        CartResponse cart = cartService.addItemToCart(userDetails.getUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully", cart));
    }

    /**
     * Update cart item quantity
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param cartItemId the cart item ID
     * @param request update cart item request
     * @return updated cart response
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        
        CartResponse cart = cartService.updateCartItem(userDetails.getUserId(), cartItemId, request);
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cart));
    }

    /**
     * Remove item from cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @param cartItemId the cart item ID
     * @return updated cart response
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId) {
        
        CartResponse cart = cartService.removeCartItem(userDetails.getUserId(), cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", cart));
    }

    /**
     * Clear all items from cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return updated cart response
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        CartResponse cart = cartService.clearCart(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", cart));
    }

    /**
     * Get cart total amount
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return cart total amount
     */
    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getCartTotal(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        // First get the cart to get cartId
        CartResponse cart = cartService.getCartByUserId(userDetails.getUserId());
        BigDecimal total = cartService.calculateCartTotal(cart.getCartId());
        
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    /**
     * Check if cart has items
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return true if cart has items
     */
    @GetMapping("/has-items")
    public ResponseEntity<ApiResponse<Boolean>> hasItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        boolean hasItems = cartService.hasItems(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(hasItems));
    }

    /**
     * Get cart item count (number of different products)
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return number of different products in cart
     */
    @GetMapping("/item-count")
    public ResponseEntity<ApiResponse<Integer>> getItemCount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        int itemCount = cartService.getItemCount(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(itemCount));
    }

    /**
     * Get total quantity of items in cart
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return total quantity of all items
     */
    @GetMapping("/total-items")
    public ResponseEntity<ApiResponse<Integer>> getTotalItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        int totalItems = cartService.getTotalItems(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.success(totalItems));
    }

    /**
     * Get cart statistics
     * Requires authentication
     * 
     * @param userDetails authenticated user details
     * @return cart statistics response
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<CartStatsResponse>> getCartStats(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        CartResponse cart = cartService.getCartByUserId(userDetails.getUserId());
        BigDecimal total = cartService.calculateCartTotal(cart.getCartId());
        
        CartStatsResponse stats = new CartStatsResponse(
                cart.getItemCount(),
                cart.getTotalItems(),
                cart.getSubtotal(),
                total,
                cart.isEmpty()
        );
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * DTO for cart statistics response
     */
    public static class CartStatsResponse {
        private Integer itemCount;
        private Integer totalItems;
        private BigDecimal subtotal;
        private BigDecimal totalAmount;
        private Boolean isEmpty;

        public CartStatsResponse() {}

        public CartStatsResponse(Integer itemCount, Integer totalItems, BigDecimal subtotal, 
                               BigDecimal totalAmount, Boolean isEmpty) {
            this.itemCount = itemCount;
            this.totalItems = totalItems;
            this.subtotal = subtotal;
            this.totalAmount = totalAmount;
            this.isEmpty = isEmpty;
        }

        // Getters and Setters
        public Integer getItemCount() {
            return itemCount;
        }

        public void setItemCount(Integer itemCount) {
            this.itemCount = itemCount;
        }

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Boolean getIsEmpty() {
            return isEmpty;
        }

        public void setIsEmpty(Boolean isEmpty) {
            this.isEmpty = isEmpty;
        }

        @Override
        public String toString() {
            return "CartStatsResponse{" +
                    "itemCount=" + itemCount +
                    ", totalItems=" + totalItems +
                    ", subtotal=" + subtotal +
                    ", totalAmount=" + totalAmount +
                    ", isEmpty=" + isEmpty +
                    '}';
        }
    }
}
