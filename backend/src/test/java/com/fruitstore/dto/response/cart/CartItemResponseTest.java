package com.fruitstore.dto.response.cart;

import com.fruitstore.dto.response.product.ProductSummaryResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CartItemResponse
 */
public class CartItemResponseTest {

    @Test
    public void testDefaultConstructor() {
        // When
        CartItemResponse response = new CartItemResponse();

        // Then
        assertThat(response.getCartItemId()).isNull();
        assertThat(response.getProductId()).isNull();
        assertThat(response.getProductName()).isNull();
        assertThat(response.getProductImageUrl()).isNull();
        assertThat(response.getUnitPrice()).isNull();
        assertThat(response.getQuantity()).isNull();
        assertThat(response.getSubtotal()).isNull();
        assertThat(response.getProduct()).isNull();
        assertThat(response.getAddedAt()).isNull();
    }

    @Test
    public void testConstructorWithParameters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal unitPrice = new BigDecimal("100000.00");
        BigDecimal subtotal = new BigDecimal("200000.00");

        // When
        CartItemResponse response = new CartItemResponse(
                1L, 2L, "Test Product", "/images/test.jpg",
                unitPrice, 2, subtotal, now
        );

        // Then
        assertThat(response.getCartItemId()).isEqualTo(1L);
        assertThat(response.getProductId()).isEqualTo(2L);
        assertThat(response.getProductName()).isEqualTo("Test Product");
        assertThat(response.getProductImageUrl()).isEqualTo("/images/test.jpg");
        assertThat(response.getUnitPrice()).isEqualTo(unitPrice);
        assertThat(response.getQuantity()).isEqualTo(2);
        assertThat(response.getSubtotal()).isEqualTo(subtotal);
        assertThat(response.getAddedAt()).isEqualTo(now);
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        CartItemResponse response = new CartItemResponse();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal unitPrice = new BigDecimal("150000.00");
        BigDecimal subtotal = new BigDecimal("300000.00");
        ProductSummaryResponse product = new ProductSummaryResponse();

        // When
        response.setCartItemId(5L);
        response.setProductId(10L);
        response.setProductName("Updated Product");
        response.setProductImageUrl("/images/updated.jpg");
        response.setUnitPrice(unitPrice);
        response.setQuantity(2);
        response.setSubtotal(subtotal);
        response.setProduct(product);
        response.setAddedAt(now);

        // Then
        assertThat(response.getCartItemId()).isEqualTo(5L);
        assertThat(response.getProductId()).isEqualTo(10L);
        assertThat(response.getProductName()).isEqualTo("Updated Product");
        assertThat(response.getProductImageUrl()).isEqualTo("/images/updated.jpg");
        assertThat(response.getUnitPrice()).isEqualTo(unitPrice);
        assertThat(response.getQuantity()).isEqualTo(2);
        assertThat(response.getSubtotal()).isEqualTo(subtotal);
        assertThat(response.getProduct()).isEqualTo(product);
        assertThat(response.getAddedAt()).isEqualTo(now);
    }

    @Test
    public void testToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal unitPrice = new BigDecimal("75000.00");
        BigDecimal subtotal = new BigDecimal("150000.00");

        CartItemResponse response = new CartItemResponse(
                3L, 4L, "Apple", "/images/apple.jpg",
                unitPrice, 2, subtotal, now
        );

        // When
        String result = response.toString();

        // Then
        assertThat(result).contains("CartItemResponse");
        assertThat(result).contains("cartItemId=3");
        assertThat(result).contains("productId=4");
        assertThat(result).contains("productName='Apple'");
        assertThat(result).contains("unitPrice=75000.00");
        assertThat(result).contains("quantity=2");
        assertThat(result).contains("subtotal=150000.00");
        assertThat(result).contains("addedAt=" + now);
    }

    @Test
    public void testEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BigDecimal unitPrice = new BigDecimal("50000.00");
        BigDecimal subtotal = new BigDecimal("100000.00");

        CartItemResponse response1 = new CartItemResponse(
                1L, 2L, "Orange", "/images/orange.jpg",
                unitPrice, 2, subtotal, now
        );

        CartItemResponse response2 = new CartItemResponse(
                1L, 3L, "Banana", "/images/banana.jpg",
                new BigDecimal("30000.00"), 1, new BigDecimal("30000.00"), now
        );

        CartItemResponse response3 = new CartItemResponse(
                2L, 2L, "Orange", "/images/orange.jpg",
                unitPrice, 2, subtotal, now
        );

        // When & Then
        assertThat(response1).isEqualTo(response1); // Same object
        assertThat(response1).isEqualTo(response2); // Same cartItemId (equals only compares cartItemId)
        assertThat(response1).isNotEqualTo(response3); // Different cartItemId
        assertThat(response1.hashCode()).isEqualTo(response1.hashCode());
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode()); // Same cartItemId = same hashCode
    }

    @Test
    public void testEqualsWithNull() {
        // Given
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(1L);

        // When & Then
        assertThat(response).isNotEqualTo(null);
        assertThat(response).isNotEqualTo("not a response");
    }

    @Test
    public void testEqualsWithNullCartItemId() {
        // Given
        CartItemResponse response1 = new CartItemResponse();
        CartItemResponse response2 = new CartItemResponse();

        // When & Then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    public void testProductSummaryResponseIntegration() {
        // Given
        CartItemResponse response = new CartItemResponse();
        ProductSummaryResponse product = new ProductSummaryResponse();
        product.setProductId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));
        product.setImageUrl("/images/test.jpg");
        product.setStockQuantity(10);
        product.setIsActive(true);

        // When
        response.setProduct(product);

        // Then
        assertThat(response.getProduct()).isNotNull();
        assertThat(response.getProduct().getProductId()).isEqualTo(1L);
        assertThat(response.getProduct().getName()).isEqualTo("Test Product");
        assertThat(response.getProduct().getPrice()).isEqualTo(new BigDecimal("100000.00"));
    }

    @Test
    public void testBigDecimalPrecision() {
        // Given
        CartItemResponse response = new CartItemResponse();
        BigDecimal precisePrice = new BigDecimal("123456.789");
        BigDecimal preciseSubtotal = new BigDecimal("246913.578");

        // When
        response.setUnitPrice(precisePrice);
        response.setSubtotal(preciseSubtotal);

        // Then
        assertThat(response.getUnitPrice()).isEqualByComparingTo(precisePrice);
        assertThat(response.getSubtotal()).isEqualByComparingTo(preciseSubtotal);
    }

    @Test
    public void testLargeQuantities() {
        // Given
        CartItemResponse response = new CartItemResponse();
        Integer largeQuantity = Integer.MAX_VALUE;

        // When
        response.setQuantity(largeQuantity);

        // Then
        assertThat(response.getQuantity()).isEqualTo(Integer.MAX_VALUE);
    }
}
