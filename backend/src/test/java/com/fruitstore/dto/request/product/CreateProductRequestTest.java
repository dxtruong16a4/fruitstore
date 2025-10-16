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
 * Test class for CreateProductRequest DTO
 */
public class CreateProductRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCreateProductRequest() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        request.setPrice(new BigDecimal("150000.00"));
        request.setStockQuantity(100);
        request.setImageUrl("/images/products/apple-fuji.jpg");
        request.setCategoryId(1L);
        request.setIsActive(true);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidCreateProductRequestWithConstructor() {
        // Given
        CreateProductRequest request = new CreateProductRequest(
                "Táo Fuji Nhật Bản", 
                "Táo Fuji nhập khẩu từ Nhật Bản", 
                new BigDecimal("150000.00"), 
                1L
        );

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getName()).isEqualTo("Táo Fuji Nhật Bản");
        assertThat(request.getDescription()).isEqualTo("Táo Fuji nhập khẩu từ Nhật Bản");
        assertThat(request.getPrice()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(request.getCategoryId()).isEqualTo(1L);
    }

    @Test
    public void testValidCreateProductRequestWithAllFields() {
        // Given
        CreateProductRequest request = new CreateProductRequest(
                "Táo Fuji Nhật Bản",
                "Táo Fuji nhập khẩu từ Nhật Bản",
                new BigDecimal("150000.00"),
                100,
                "/images/products/apple-fuji.jpg",
                1L,
                true
        );

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getName()).isEqualTo("Táo Fuji Nhật Bản");
        assertThat(request.getDescription()).isEqualTo("Táo Fuji nhập khẩu từ Nhật Bản");
        assertThat(request.getPrice()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(request.getStockQuantity()).isEqualTo(100);
        assertThat(request.getImageUrl()).isEqualTo("/images/products/apple-fuji.jpg");
        assertThat(request.getCategoryId()).isEqualTo(1L);
        assertThat(request.getIsActive()).isTrue();
    }

    @Test
    public void testInvalidCreateProductRequestMissingName() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        request.setPrice(new BigDecimal("150000.00"));
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Product name is required");
    }

    @Test
    public void testInvalidCreateProductRequestMissingPrice() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price is required");
    }

    @Test
    public void testInvalidCreateProductRequestMissingCategoryId() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        request.setPrice(new BigDecimal("150000.00"));

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Category ID is required");
    }

    @Test
    public void testInvalidCreateProductRequestPriceTooLow() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setPrice(new BigDecimal("0.00")); // Too low
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be greater than 0");
    }

    @Test
    public void testInvalidCreateProductRequestNegativeStock() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setPrice(new BigDecimal("150000.00"));
        request.setStockQuantity(-1); // Negative stock
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Stock quantity must be 0 or greater");
    }

    @Test
    public void testInvalidCreateProductRequestNameTooLong() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("A".repeat(101)); // 101 characters
        request.setPrice(new BigDecimal("150000.00"));
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Product name must not exceed 100 characters");
    }

    @Test
    public void testInvalidCreateProductRequestDescriptionTooLong() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Táo Fuji Nhật Bản");
        request.setDescription("A".repeat(2001)); // 2001 characters
        request.setPrice(new BigDecimal("150000.00"));
        request.setCategoryId(1L);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description must not exceed 2000 characters");
    }

    @Test
    public void testDefaultValues() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Test Product");
        request.setPrice(new BigDecimal("100000.00"));
        request.setCategoryId(1L);

        // When & Then
        assertThat(request.getStockQuantity()).isEqualTo(0); // Default value
        assertThat(request.getIsActive()).isTrue(); // Default value
    }

    @Test
    public void testCreateProductRequestSettersAndGetters() {
        // Given
        CreateProductRequest request = new CreateProductRequest();

        // When
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("100000.00"));
        request.setStockQuantity(50);
        request.setImageUrl("/test/image.jpg");
        request.setCategoryId(1L);
        request.setIsActive(false);

        // Then
        assertThat(request.getName()).isEqualTo("Test Product");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getPrice()).isEqualTo(new BigDecimal("100000.00"));
        assertThat(request.getStockQuantity()).isEqualTo(50);
        assertThat(request.getImageUrl()).isEqualTo("/test/image.jpg");
        assertThat(request.getCategoryId()).isEqualTo(1L);
        assertThat(request.getIsActive()).isFalse();
    }

    @Test
    public void testCreateProductRequestToString() {
        // Given
        CreateProductRequest request = new CreateProductRequest(
                "Test Product", 
                "Test Description", 
                new BigDecimal("100000.00"), 
                50, 
                "/test/image.jpg", 
                1L, 
                true
        );

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("Test Product");
        assertThat(toString).contains("Test Description");
        assertThat(toString).contains("100000.00");
        assertThat(toString).contains("/test/image.jpg");
    }
}
