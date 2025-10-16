package com.fruitstore.dto.request.product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for ProductFilterRequest DTO
 */
public class ProductFilterRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidProductFilterRequest() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setCategoryId(1L);
        request.setMinPrice(new BigDecimal("50000.00"));
        request.setMaxPrice(new BigDecimal("200000.00"));
        request.setKeyword("táo");
        request.setPage(0);
        request.setSize(20);
        request.setSortBy("name");
        request.setSortDirection("asc");

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidProductFilterRequestWithConstructor() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest(
                1L, 
                new BigDecimal("50000.00"), 
                new BigDecimal("200000.00"), 
                "táo"
        );

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCategoryId()).isEqualTo(1L);
        assertThat(request.getMinPrice()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(request.getMaxPrice()).isEqualTo(new BigDecimal("200000.00"));
        assertThat(request.getKeyword()).isEqualTo("táo");
    }

    @Test
    public void testValidProductFilterRequestWithAllFields() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest(
                1L,
                new BigDecimal("50000.00"),
                new BigDecimal("200000.00"),
                "táo",
                0,
                10,
                "price",
                "desc"
        );

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCategoryId()).isEqualTo(1L);
        assertThat(request.getMinPrice()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(request.getMaxPrice()).isEqualTo(new BigDecimal("200000.00"));
        assertThat(request.getKeyword()).isEqualTo("táo");
        assertThat(request.getPage()).isEqualTo(0);
        assertThat(request.getSize()).isEqualTo(10);
        assertThat(request.getSortBy()).isEqualTo("price");
        assertThat(request.getSortDirection()).isEqualTo("desc");
    }

    @Test
    public void testDefaultValues() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();

        // When & Then
        assertThat(request.getPage()).isEqualTo(0);
        assertThat(request.getSize()).isEqualTo(20);
        assertThat(request.getSortBy()).isEqualTo("name");
        assertThat(request.getSortDirection()).isEqualTo("asc");
    }

    @Test
    public void testInvalidProductFilterRequestNegativeMinPrice() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setMinPrice(new BigDecimal("-100.00")); // Negative price

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Minimum price must be 0 or greater");
    }

    @Test
    public void testInvalidProductFilterRequestNegativeMaxPrice() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setMaxPrice(new BigDecimal("-100.00")); // Negative price

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Maximum price must be 0 or greater");
    }

    @Test
    public void testInvalidProductFilterRequestNegativePage() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setPage(-1); // Negative page

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Page number must be 0 or greater");
    }

    @Test
    public void testInvalidProductFilterRequestZeroSize() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setSize(0); // Zero size

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Page size must be at least 1");
    }

    @Test
    public void testValidProductFilterRequestWithNullValues() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();
        // All fields are null or have default values

        // When
        Set<ConstraintViolation<ProductFilterRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getCategoryId()).isNull();
        assertThat(request.getMinPrice()).isNull();
        assertThat(request.getMaxPrice()).isNull();
        assertThat(request.getKeyword()).isNull();
    }

    @Test
    public void testProductFilterRequestSettersAndGetters() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest();

        // When
        request.setCategoryId(2L);
        request.setMinPrice(new BigDecimal("100000.00"));
        request.setMaxPrice(new BigDecimal("300000.00"));
        request.setKeyword("nho");
        request.setPage(1);
        request.setSize(15);
        request.setSortBy("price");
        request.setSortDirection("desc");

        // Then
        assertThat(request.getCategoryId()).isEqualTo(2L);
        assertThat(request.getMinPrice()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(request.getMaxPrice()).isEqualTo(new BigDecimal("300000.00"));
        assertThat(request.getKeyword()).isEqualTo("nho");
        assertThat(request.getPage()).isEqualTo(1);
        assertThat(request.getSize()).isEqualTo(15);
        assertThat(request.getSortBy()).isEqualTo("price");
        assertThat(request.getSortDirection()).isEqualTo("desc");
    }

    @Test
    public void testProductFilterRequestToString() {
        // Given
        ProductFilterRequest request = new ProductFilterRequest(
                1L,
                new BigDecimal("50000.00"),
                new BigDecimal("200000.00"),
                "táo",
                0,
                20,
                "name",
                "asc"
        );

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("categoryId=1");
        assertThat(toString).contains("minPrice=50000.00");
        assertThat(toString).contains("maxPrice=200000.00");
        assertThat(toString).contains("keyword='táo'"); // String fields have quotes
        assertThat(toString).contains("page=0");
        assertThat(toString).contains("size=20");
        assertThat(toString).contains("sortBy='name'"); // String fields have quotes
        assertThat(toString).contains("sortDirection='asc'"); // String fields have quotes
    }
}
