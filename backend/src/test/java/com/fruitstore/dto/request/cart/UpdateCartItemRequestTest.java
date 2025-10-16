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
 * Test class for UpdateCartItemRequest
 */
public class UpdateCartItemRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUpdateCartItemRequest() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(3);

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testConstructorWithParameters() {
        // When
        UpdateCartItemRequest request = new UpdateCartItemRequest(5);

        // Then
        assertThat(request.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testDefaultConstructor() {
        // When
        UpdateCartItemRequest request = new UpdateCartItemRequest();

        // Then
        assertThat(request.getQuantity()).isNull();
    }

    @Test
    public void testNullQuantity() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(null);

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity is required");
    }

    @Test
    public void testZeroQuantity() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(0);

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity must be at least 1");
    }

    @Test
    public void testNegativeQuantity() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(-2);

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Quantity must be at least 1");
    }

    @Test
    public void testValidQuantityBoundary() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(1); // Minimum valid quantity

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testLargeQuantity() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(999); // Large but valid quantity

        // When
        Set<ConstraintViolation<UpdateCartItemRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testToString() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest(7);

        // When
        String result = request.toString();

        // Then
        assertThat(result).contains("UpdateCartItemRequest");
        assertThat(result).contains("quantity=7");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Given
        UpdateCartItemRequest request1 = new UpdateCartItemRequest(3);
        UpdateCartItemRequest request2 = new UpdateCartItemRequest(3);
        UpdateCartItemRequest request3 = new UpdateCartItemRequest(5);

        // When & Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest(3);

        // When & Then
        assertThat(request).isNotEqualTo(null);
        assertThat(request).isNotEqualTo("not a request");
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        UpdateCartItemRequest request = new UpdateCartItemRequest();

        // When
        request.setQuantity(8);

        // Then
        assertThat(request.getQuantity()).isEqualTo(8);
    }

    @Test
    public void testEqualsWithNullValues() {
        // Given
        UpdateCartItemRequest request1 = new UpdateCartItemRequest();
        UpdateCartItemRequest request2 = new UpdateCartItemRequest();

        // When & Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }
}
