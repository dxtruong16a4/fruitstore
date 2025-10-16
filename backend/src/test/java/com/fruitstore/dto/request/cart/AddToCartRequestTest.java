package com.fruitstore.dto.request.cart;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for AddToCartRequest
 */
public class AddToCartRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidAddToCartRequest() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testConstructorWithParameters() {
        // When
        AddToCartRequest request = new AddToCartRequest(1L, 3);

        // Then
        assertThat(request.getProductId()).isEqualTo(1L);
        assertThat(request.getQuantity()).isEqualTo(3);
    }

    @Test
    public void testDefaultConstructor() {
        // When
        AddToCartRequest request = new AddToCartRequest();

        // Then
        assertThat(request.getProductId()).isNull();
        assertThat(request.getQuantity()).isNull();
    }

    @Test
    public void testNullProductId() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(null);
        request.setQuantity(2);

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Product ID is required");
    }

    @Test
    public void testNullQuantity() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(null);

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity is required");
    }

    @Test
    public void testZeroQuantity() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(0);

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity must be at least 1");
    }

    @Test
    public void testNegativeQuantity() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(-1);

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity must be at least 1");
    }

    @Test
    public void testValidQuantityBoundary() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(1); // Minimum valid quantity

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testLargeQuantity() {
        // Given
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(1000); // Large but valid quantity

        // When
        Set<ConstraintViolation<AddToCartRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testToString() {
        // Given
        AddToCartRequest request = new AddToCartRequest(1L, 2);

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("AddToCartRequest");
        assertThat(result).contains("productId=1");
        assertThat(result).contains("quantity=2");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Given
        AddToCartRequest request1 = new AddToCartRequest(1L, 2);
        AddToCartRequest request2 = new AddToCartRequest(1L, 2);
        AddToCartRequest request3 = new AddToCartRequest(2L, 2);

        // When & Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        // Given
        AddToCartRequest request = new AddToCartRequest(1L, 2);

        // When & Then
        assertThat(request).isNotEqualTo(null);
        assertThat(request).isNotEqualTo("not a request");
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        AddToCartRequest request = new AddToCartRequest();

        // When
        request.setProductId(5L);
        request.setQuantity(10);

        // Then
        assertThat(request.getProductId()).isEqualTo(5L);
        assertThat(request.getQuantity()).isEqualTo(10);
    }
}
