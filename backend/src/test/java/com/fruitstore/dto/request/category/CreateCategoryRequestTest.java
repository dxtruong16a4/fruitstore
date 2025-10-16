package com.fruitstore.dto.request.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CreateCategoryRequest DTO
 */
public class CreateCategoryRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCreateCategoryRequest() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Trái cây tươi");
        request.setDescription("Các loại trái cây tươi ngon");
        request.setImageUrl("/images/categories/fresh-fruits.jpg");

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidCreateCategoryRequestWithConstructor() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest("Trái cây tươi", "Các loại trái cây tươi ngon");

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getName()).isEqualTo("Trái cây tươi");
        assertThat(request.getDescription()).isEqualTo("Các loại trái cây tươi ngon");
    }

    @Test
    public void testValidCreateCategoryRequestWithAllFields() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Trái cây nhập khẩu", 
                "Trái cây nhập khẩu từ các nước", 
                "/images/categories/imported.jpg"
        );

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getName()).isEqualTo("Trái cây nhập khẩu");
        assertThat(request.getDescription()).isEqualTo("Trái cây nhập khẩu từ các nước");
        assertThat(request.getImageUrl()).isEqualTo("/images/categories/imported.jpg");
    }

    @Test
    public void testInvalidCreateCategoryRequestMissingName() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setDescription("Các loại trái cây tươi ngon");

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Category name is required");
    }

    @Test
    public void testInvalidCreateCategoryRequestBlankName() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("");
        request.setDescription("Các loại trái cây tươi ngon");

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Category name is required");
    }

    @Test
    public void testInvalidCreateCategoryRequestNameTooLong() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("A".repeat(101)); // 101 characters
        request.setDescription("Các loại trái cây tươi ngon");

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Category name must not exceed 100 characters");
    }

    @Test
    public void testInvalidCreateCategoryRequestDescriptionTooLong() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Trái cây tươi");
        request.setDescription("A".repeat(1001)); // 1001 characters

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Description must not exceed 1000 characters");
    }

    @Test
    public void testInvalidCreateCategoryRequestImageUrlTooLong() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Trái cây tươi");
        request.setDescription("Các loại trái cây tươi ngon");
        request.setImageUrl("A".repeat(256)); // 256 characters

        // When
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Image URL must not exceed 255 characters");
    }

    @Test
    public void testCreateCategoryRequestSettersAndGetters() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();

        // When
        request.setName("Test Category");
        request.setDescription("Test Description");
        request.setImageUrl("/test/image.jpg");

        // Then
        assertThat(request.getName()).isEqualTo("Test Category");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getImageUrl()).isEqualTo("/test/image.jpg");
    }

    @Test
    public void testCreateCategoryRequestToString() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest("Test Category", "Test Description", "/test/image.jpg");

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("Test Category");
        assertThat(toString).contains("Test Description");
        assertThat(toString).contains("/test/image.jpg");
    }
}
