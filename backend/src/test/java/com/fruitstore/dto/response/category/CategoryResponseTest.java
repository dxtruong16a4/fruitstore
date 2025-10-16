package com.fruitstore.dto.response.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CategoryResponse DTO
 */
public class CategoryResponseTest {

    @Test
    public void testCategoryResponseDefaultConstructor() {
        // Given
        CategoryResponse response = new CategoryResponse();

        // When & Then
        assertThat(response).isNotNull();
        assertThat(response.getCategoryId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getDescription()).isNull();
        assertThat(response.getImageUrl()).isNull();
        assertThat(response.getIsActive()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getProductCount()).isNull();
    }

    @Test
    public void testCategoryResponseWithBasicConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse response = new CategoryResponse(
                1L,
                "Trái cây tươi",
                "Các loại trái cây tươi ngon",
                "/images/categories/fresh-fruits.jpg",
                true,
                now
        );

        // When & Then
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Trái cây tươi");
        assertThat(response.getDescription()).isEqualTo("Các loại trái cây tươi ngon");
        assertThat(response.getImageUrl()).isEqualTo("/images/categories/fresh-fruits.jpg");
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getProductCount()).isNull();
    }

    @Test
    public void testCategoryResponseWithProductCountConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse response = new CategoryResponse(
                1L,
                "Trái cây tươi",
                "Các loại trái cây tươi ngon",
                "/images/categories/fresh-fruits.jpg",
                true,
                now,
                15L
        );

        // When & Then
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Trái cây tươi");
        assertThat(response.getDescription()).isEqualTo("Các loại trái cây tươi ngon");
        assertThat(response.getImageUrl()).isEqualTo("/images/categories/fresh-fruits.jpg");
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getProductCount()).isEqualTo(15L);
    }

    @Test
    public void testCategoryResponseSettersAndGetters() {
        // Given
        CategoryResponse response = new CategoryResponse();
        LocalDateTime now = LocalDateTime.now();

        // When
        response.setCategoryId(2L);
        response.setName("Trái cây nhập khẩu");
        response.setDescription("Trái cây nhập khẩu từ các nước");
        response.setImageUrl("/images/categories/imported.jpg");
        response.setIsActive(false);
        response.setCreatedAt(now);
        response.setProductCount(25L);

        // Then
        assertThat(response.getCategoryId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("Trái cây nhập khẩu");
        assertThat(response.getDescription()).isEqualTo("Trái cây nhập khẩu từ các nước");
        assertThat(response.getImageUrl()).isEqualTo("/images/categories/imported.jpg");
        assertThat(response.getIsActive()).isFalse();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getProductCount()).isEqualTo(25L);
    }

    @Test
    public void testCategoryResponseToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse response = new CategoryResponse(
                1L,
                "Trái cây tươi",
                "Các loại trái cây tươi ngon",
                "/images/categories/fresh-fruits.jpg",
                true,
                now,
                15L
        );

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("categoryId=1");
        assertThat(toString).contains("name='Trái cây tươi'"); // String fields have quotes
        assertThat(toString).contains("description='Các loại trái cây tươi ngon'"); // String fields have quotes
        assertThat(toString).contains("imageUrl='/images/categories/fresh-fruits.jpg'"); // String fields have quotes
        assertThat(toString).contains("isActive=true");
        assertThat(toString).contains("productCount=15");
    }

    @Test
    public void testCategoryResponseWithNullValues() {
        // Given
        CategoryResponse response = new CategoryResponse();

        // When
        response.setCategoryId(null);
        response.setName(null);
        response.setDescription(null);
        response.setImageUrl(null);
        response.setIsActive(null);
        response.setCreatedAt(null);
        response.setProductCount(null);

        // Then
        assertThat(response.getCategoryId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getDescription()).isNull();
        assertThat(response.getImageUrl()).isNull();
        assertThat(response.getIsActive()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getProductCount()).isNull();
    }

    @Test
    public void testCategoryResponseWithEmptyStrings() {
        // Given
        CategoryResponse response = new CategoryResponse();

        // When
        response.setName("");
        response.setDescription("");
        response.setImageUrl("");

        // Then
        assertThat(response.getName()).isEqualTo("");
        assertThat(response.getDescription()).isEqualTo("");
        assertThat(response.getImageUrl()).isEqualTo("");
    }

    @Test
    public void testCategoryResponseWithLongStrings() {
        // Given
        CategoryResponse response = new CategoryResponse();
        String longName = "A".repeat(100);
        String longDescription = "A".repeat(1000);
        String longImageUrl = "A".repeat(255);

        // When
        response.setName(longName);
        response.setDescription(longDescription);
        response.setImageUrl(longImageUrl);

        // Then
        assertThat(response.getName()).isEqualTo(longName);
        assertThat(response.getDescription()).isEqualTo(longDescription);
        assertThat(response.getImageUrl()).isEqualTo(longImageUrl);
    }
}
