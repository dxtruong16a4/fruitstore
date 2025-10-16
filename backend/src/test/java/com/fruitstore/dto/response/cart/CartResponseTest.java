package com.fruitstore.dto.response.cart;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CartResponse
 */
public class CartResponseTest {

    @Test
    public void testDefaultConstructor() {
        // When
        CartResponse response = new CartResponse();

        // Then
        assertThat(response.getCartId()).isNull();
        assertThat(response.getUserId()).isNull();
        assertThat(response.getItems()).isNull();
        assertThat(response.getItemCount()).isNull();
        assertThat(response.getTotalItems()).isNull();
        assertThat(response.getSubtotal()).isNull();
        assertThat(response.getTotalAmount()).isNull();
        assertThat(response.isEmpty()).isFalse(); // Default boolean value
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    public void testConstructorWithParameters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal subtotal = new BigDecimal("200000.00");
        BigDecimal totalAmount = new BigDecimal("220000.00");
        List<CartItemResponse> items = Arrays.asList(
                new CartItemResponse(),
                new CartItemResponse()
        );

        // When
        CartResponse response = new CartResponse(
                1L, 2L, items, 2, 3, subtotal, totalAmount, false, now, now
        );

        // Then
        assertThat(response.getCartId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(2L);
        assertThat(response.getItems()).isEqualTo(items);
        assertThat(response.getItemCount()).isEqualTo(2);
        assertThat(response.getTotalItems()).isEqualTo(3);
        assertThat(response.getSubtotal()).isEqualTo(subtotal);
        assertThat(response.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(response.isEmpty()).isFalse();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        CartResponse response = new CartResponse();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal subtotal = new BigDecimal("150000.00");
        BigDecimal totalAmount = new BigDecimal("165000.00");
        List<CartItemResponse> items = Arrays.asList(new CartItemResponse());

        // When
        response.setCartId(5L);
        response.setUserId(10L);
        response.setItems(items);
        response.setItemCount(1);
        response.setTotalItems(2);
        response.setSubtotal(subtotal);
        response.setTotalAmount(totalAmount);
        response.setEmpty(true);
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Then
        assertThat(response.getCartId()).isEqualTo(5L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getItems()).isEqualTo(items);
        assertThat(response.getItemCount()).isEqualTo(1);
        assertThat(response.getTotalItems()).isEqualTo(2);
        assertThat(response.getSubtotal()).isEqualTo(subtotal);
        assertThat(response.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(response.isEmpty()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testEmptyCart() {
        // Given
        CartResponse response = new CartResponse();

        // When
        response.setEmpty(true);
        response.setItems(Arrays.asList());

        // Then
        assertThat(response.isEmpty()).isTrue();
        assertThat(response.getItems()).isEmpty();
    }

    @Test
    public void testCartWithItems() {
        // Given
        CartResponse response = new CartResponse();
        CartItemResponse item1 = new CartItemResponse();
        item1.setCartItemId(1L);
        CartItemResponse item2 = new CartItemResponse();
        item2.setCartItemId(2L);
        List<CartItemResponse> items = Arrays.asList(item1, item2);

        // When
        response.setItems(items);
        response.setItemCount(2);
        response.setTotalItems(5); // Total quantity across all items
        response.setEmpty(false);

        // Then
        assertThat(response.isEmpty()).isFalse();
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getItemCount()).isEqualTo(2);
        assertThat(response.getTotalItems()).isEqualTo(5);
    }

    @Test
    public void testToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal subtotal = new BigDecimal("100000.00");
        BigDecimal totalAmount = new BigDecimal("110000.00");

        CartResponse response = new CartResponse(
                1L, 2L, null, 1, 2, subtotal, totalAmount, false, now, now
        );

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("CartResponse");
        assertThat(result).contains("cartId=1");
        assertThat(result).contains("userId=2");
        assertThat(result).contains("itemCount=1");
        assertThat(result).contains("totalItems=2");
        assertThat(result).contains("subtotal=100000.00");
        assertThat(result).contains("totalAmount=110000.00");
        assertThat(result).contains("isEmpty=false");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal subtotal = new BigDecimal("50000.00");
        BigDecimal totalAmount = new BigDecimal("55000.00");

        CartResponse response1 = new CartResponse(
                1L, 2L, null, 1, 1, subtotal, totalAmount, false, now, now
        );

        CartResponse response2 = new CartResponse(
                1L, 3L, null, 2, 3, new BigDecimal("100000.00"), new BigDecimal("110000.00"), true, now, now
        );

        CartResponse response3 = new CartResponse(
                2L, 2L, null, 1, 1, subtotal, totalAmount, false, now, now
        );

        // When & Then
        assertThat(response1).isEqualTo(response1); // Same object
        assertThat(response1).isEqualTo(response2); // Same cartId (equals only compares cartId)
        assertThat(response1).isNotEqualTo(response3); // Different cartId
        assertThat(response1.hashCode()).isEqualTo(response1.hashCode());
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode()); // Same cartId = same hashCode
    }

    @Test
    public void testEqualsWithNull() {
        // Given
        CartResponse response = new CartResponse();
        response.setCartId(1L);

        // When & Then
        assertThat(response).isNotEqualTo(null);
        assertThat(response).isNotEqualTo("not a response");
    }

    @Test
    public void testEqualsWithNullCartId() {
        // Given
        CartResponse response1 = new CartResponse();
        CartResponse response2 = new CartResponse();

        // When & Then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    public void testBigDecimalPrecision() {
        // Given
        CartResponse response = new CartResponse();
        BigDecimal preciseSubtotal = new BigDecimal("123456.789");
        BigDecimal preciseTotal = new BigDecimal("135802.4679");

        // When
        response.setSubtotal(preciseSubtotal);
        response.setTotalAmount(preciseTotal);

        // Then
        assertThat(response.getSubtotal()).isEqualByComparingTo(preciseSubtotal);
        assertThat(response.getTotalAmount()).isEqualByComparingTo(preciseTotal);
    }

    @Test
    public void testLargeNumbers() {
        // Given
        CartResponse response = new CartResponse();
        Integer maxInt = Integer.MAX_VALUE;
        BigDecimal largeAmount = new BigDecimal("999999999.99");

        // When
        response.setItemCount(maxInt);
        response.setTotalItems(maxInt);
        response.setSubtotal(largeAmount);
        response.setTotalAmount(largeAmount);

        // Then
        assertThat(response.getItemCount()).isEqualTo(Integer.MAX_VALUE);
        assertThat(response.getTotalItems()).isEqualTo(Integer.MAX_VALUE);
        assertThat(response.getSubtotal()).isEqualByComparingTo(largeAmount);
        assertThat(response.getTotalAmount()).isEqualByComparingTo(largeAmount);
    }

    @Test
    public void testItemsListOperations() {
        // Given
        CartResponse response = new CartResponse();
        CartItemResponse item1 = new CartItemResponse();
        item1.setCartItemId(1L);
        CartItemResponse item2 = new CartItemResponse();
        item2.setCartItemId(2L);

        // When
        response.setItems(Arrays.asList(item1, item2));

        // Then
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getItems()).contains(item1, item2);
    }

    @Test
    public void testEmptyItemsList() {
        // Given
        CartResponse response = new CartResponse();

        // When
        response.setItems(Arrays.asList());
        response.setEmpty(true);

        // Then
        assertThat(response.getItems()).isEmpty();
        assertThat(response.isEmpty()).isTrue();
    }
}
