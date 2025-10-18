package com.fruitstore.service;

import com.fruitstore.domain.cart.Cart;
import com.fruitstore.domain.cart.CartItem;
import com.fruitstore.domain.product.Product;
import com.fruitstore.domain.user.User;
import com.fruitstore.dto.request.cart.AddToCartRequest;
import com.fruitstore.dto.request.cart.UpdateCartItemRequest;
import com.fruitstore.dto.response.cart.CartItemResponse;
import com.fruitstore.dto.response.cart.CartResponse;
import com.fruitstore.repository.CartItemRepository;
import com.fruitstore.repository.CartRepository;
import com.fruitstore.repository.ProductRepository;
import com.fruitstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for cart management
 * Handles cart operations, item management, and business logic
 */
@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                      UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get cart by user ID
     * Automatically creates cart if it doesn't exist
     *
     * @param userId the user ID
     * @return cart response
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public CartResponse getCartByUserId(Long userId) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Get or create cart
        Cart cart = getOrCreateCart(user);

        // Load cart with items and products
        cart = cartRepository.findByUser_UserIdWithItemsAndProducts(userId)
                .orElse(cart);

        return mapToCartResponse(cart);
    }

    /**
     * Add item to cart
     * Automatically creates cart if it doesn't exist
     * Updates quantity if product already exists in cart
     * 
     * @param userId the user ID
     * @param request add to cart request
     * @return updated cart response
     * @throws IllegalArgumentException if user, product not found or insufficient stock
     */
    public CartResponse addItemToCart(Long userId, AddToCartRequest request) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Verify product exists and is active
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

        if (!product.getIsActive()) {
            throw new IllegalArgumentException("Product is not active with id: " + request.getProductId());
        }

        // Check stock availability
        if (!product.hasSufficientStock(request.getQuantity())) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getStockQuantity() + 
                    ", Requested: " + request.getQuantity());
        }

        // Get or create cart
        Cart cart = getOrCreateCart(user);

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(
                cart.getCartId(), product.getProductId());

        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            // Check if new total quantity exceeds stock
            if (!product.hasSufficientStock(newQuantity)) {
                throw new IllegalArgumentException("Insufficient stock for additional quantity. " +
                        "Current in cart: " + cartItem.getQuantity() + 
                        ", Additional requested: " + request.getQuantity() + 
                        ", Available stock: " + product.getStockQuantity());
            }
            
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        }

        // Return updated cart
        return getCartByUserId(userId);
    }

    /**
     * Update cart item quantity
     * 
     * @param userId the user ID
     * @param cartItemId the cart item ID
     * @param request update cart item request
     * @return updated cart response
     * @throws IllegalArgumentException if user, cart item not found or insufficient stock
     */
    public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Find cart item and verify ownership
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cart item does not belong to user with id: " + userId);
        }

        // Verify product is still active
        if (!cartItem.getProduct().getIsActive()) {
            throw new IllegalArgumentException("Product is no longer active: " + cartItem.getProduct().getName());
        }

        // Check stock availability for new quantity
        if (!cartItem.getProduct().hasSufficientStock(request.getQuantity())) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + 
                    cartItem.getProduct().getStockQuantity() + 
                    ", Requested: " + request.getQuantity());
        }

        // Update quantity
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        // Return updated cart
        return getCartByUserId(userId);
    }

    /**
     * Remove cart item
     * 
     * @param userId the user ID
     * @param cartItemId the cart item ID
     * @return updated cart response
     * @throws IllegalArgumentException if user or cart item not found
     */
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Find cart item and verify ownership
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cart item does not belong to user with id: " + userId);
        }

        // Remove cart item
        cartItemRepository.delete(cartItem);

        // Return updated cart
        return getCartByUserId(userId);
    }

    /**
     * Clear all items from cart
     * 
     * @param userId the user ID
     * @return updated cart response
     * @throws IllegalArgumentException if user not found
     */
    public CartResponse clearCart(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Get cart
        Optional<Cart> cartOpt = cartRepository.findByUser_UserId(userId);
        if (cartOpt.isPresent()) {
            // Clear all cart items
            cartItemRepository.deleteByCart_CartId(cartOpt.get().getCartId());
        }

        // Return updated cart
        return getCartByUserId(userId);
    }

    /**
     * Calculate cart total
     * 
     * @param cartId the cart ID
     * @return cart total amount
     * @throws IllegalArgumentException if cart not found
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with id: " + cartId));

        // Load cart with items
        cart = cartRepository.findByUser_UserIdWithItems(cart.getUser().getUserId())
                .orElse(cart);

        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getIsActive()) {
                total = total.add(item.getSubtotal());
            }
        }

        return total;
    }

    /**
     * Get or create cart for user
     * 
     * @param user the user
     * @return existing or newly created cart
     */
    private Cart getOrCreateCart(User user) {
        Optional<Cart> cartOpt = cartRepository.findByUser_UserId(user.getUserId());
        
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        } else {
            // Create new cart
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        }
    }

    /**
     * Map Cart entity to CartResponse DTO
     * 
     * @param cart the cart entity
     * @return cart response
     */
    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getIsActive()) // Only include active products
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal subtotal = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getCartId(),
                cart.getUser().getUserId(),
                itemResponses,
                itemResponses.size(), // item count (different products)
                itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum(), // total items
                subtotal,
                subtotal, // For now, total amount equals subtotal (no taxes/discounts)
                itemResponses.isEmpty(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    /**
     * Map CartItem entity to CartItemResponse DTO
     * 
     * @param cartItem the cart item entity
     * @return cart item response
     */
    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {

        return new CartItemResponse(
                cartItem.getCartItemId(),
                cartItem.getProduct().getProductId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                cartItem.getSubtotal(),
                cartItem.getCreatedAt()
        );
    }

    /**
     * Get cart summary (lightweight version for quick access)
     * 
     * @param userId the user ID
     * @return cart summary with basic information
     */
    @Transactional(readOnly = true)
    public CartResponse getCartSummary(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Get cart with basic info
        Optional<Cart> cartOpt = cartRepository.findByUser_UserId(userId);
        if (!cartOpt.isPresent()) {
            // Return empty cart
            return new CartResponse(
                    null, userId, null, 0, 0, 
                    BigDecimal.ZERO, BigDecimal.ZERO, true, 
                    LocalDateTime.now(), LocalDateTime.now()
            );
        }

        Cart cart = cartOpt.get();
        int itemCount = cartRepository.countDifferentProductsByUserId(userId);
        int totalItems = cartRepository.countTotalItemsByUserId(userId);
        BigDecimal subtotal = calculateCartTotal(cart.getCartId());

        return new CartResponse(
                cart.getCartId(),
                cart.getUser().getUserId(),
                null, // No items for summary
                itemCount,
                totalItems,
                subtotal,
                subtotal,
                itemCount == 0,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    /**
     * Check if cart has any items
     * 
     * @param userId the user ID
     * @return true if cart has items
     */
    @Transactional(readOnly = true)
    public boolean hasItems(Long userId) {
        return cartRepository.countDifferentProductsByUserId(userId) > 0;
    }

    /**
     * Get cart item count (number of different products)
     * 
     * @param userId the user ID
     * @return number of different products in cart
     */
    @Transactional(readOnly = true)
    public int getItemCount(Long userId) {
        return cartRepository.countDifferentProductsByUserId(userId);
    }

    /**
     * Get total quantity of items in cart
     * 
     * @param userId the user ID
     * @return total quantity of all items
     */
    @Transactional(readOnly = true)
    public int getTotalItems(Long userId) {
        return cartRepository.countTotalItemsByUserId(userId);
    }
}
