package com.fruitstore.dto.response.product;

import com.fruitstore.dto.response.category.CategoryResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for ProductResponse DTO
 */
public class ProductResponseTest {

    @Test
    public void testProductResponseDefaultConstructor() {
        // Given
        ProductResponse response = new ProductResponse();

        // When & Then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getDescription()).isNull();
        assertThat(response.getPrice()).isNull();
        assertThat(response.getStockQuantity()).isNull();
        assertThat(response.getImageUrl()).isNull();
        assertThat(response.getCategory()).isNull();
        assertThat(response.getIsActive()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    public void testProductResponseWithConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse category = new CategoryResponse(1L, "Trái cây tươi", "Các loại trái cây tươi ngon", 
                                                        "/images/categories/fresh-fruits.jpg", true, now);
        
        ProductResponse response = new ProductResponse(
                1L,
                "Táo Fuji Nhật Bản",
                "Táo Fuji nhập khẩu từ Nhật Bản",
                new BigDecimal("150000.00"),
                100,
                "/images/products/apple-fuji.jpg",
                category,
                true,
                now,
                now
        );

        // When & Then
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Táo Fuji Nhật Bản");
        assertThat(response.getDescription()).isEqualTo("Táo Fuji nhập khẩu từ Nhật Bản");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(response.getStockQuantity()).isEqualTo(100);
        assertThat(response.getImageUrl()).isEqualTo("/images/products/apple-fuji.jpg");
        assertThat(response.getCategory()).isEqualTo(category);
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testProductResponseSettersAndGetters() {
        // Given
        ProductResponse response = new ProductResponse();
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse category = new CategoryResponse(2L, "Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước", 
                                                        "/images/categories/imported.jpg", true, now);

        // When
        response.setProductId(2L);
        response.setName("Cam Úc");
        response.setDescription("Cam tươi nhập khẩu từ Úc");
        response.setPrice(new BigDecimal("80000.00"));
        response.setStockQuantity(150);
        response.setImageUrl("/images/products/orange.jpg");
        response.setCategory(category);
        response.setIsActive(false);
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Then
        assertThat(response.getProductId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("Cam Úc");
        assertThat(response.getDescription()).isEqualTo("Cam tươi nhập khẩu từ Úc");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("80000.00"));
        assertThat(response.getStockQuantity()).isEqualTo(150);
        assertThat(response.getImageUrl()).isEqualTo("/images/products/orange.jpg");
        assertThat(response.getCategory()).isEqualTo(category);
        assertThat(response.getIsActive()).isFalse();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testProductResponseToString() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CategoryResponse category = new CategoryResponse(1L, "Trái cây tươi", "Các loại trái cây tươi ngon", 
                                                        "/images/categories/fresh-fruits.jpg", true, now);
        
        ProductResponse response = new ProductResponse(
                1L,
                "Táo Fuji Nhật Bản",
                "Táo Fuji nhập khẩu từ Nhật Bản",
                new BigDecimal("150000.00"),
                100,
                "/images/products/apple-fuji.jpg",
                category,
                true,
                now,
                now
        );

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("productId=1");
        assertThat(toString).contains("name='Táo Fuji Nhật Bản'"); // String fields have quotes
        assertThat(toString).contains("description='Táo Fuji nhập khẩu từ Nhật Bản'"); // String fields have quotes
        assertThat(toString).contains("price=150000.00");
        assertThat(toString).contains("stockQuantity=100");
        assertThat(toString).contains("imageUrl='/images/products/apple-fuji.jpg'"); // String fields have quotes
        assertThat(toString).contains("isActive=true");
    }

    @Test
    public void testProductResponseWithNullValues() {
        // Given
        ProductResponse response = new ProductResponse();

        // When
        response.setProductId(null);
        response.setName(null);
        response.setDescription(null);
        response.setPrice(null);
        response.setStockQuantity(null);
        response.setImageUrl(null);
        response.setCategory(null);
        response.setIsActive(null);
        response.setCreatedAt(null);
        response.setUpdatedAt(null);

        // Then
        assertThat(response.getProductId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getDescription()).isNull();
        assertThat(response.getPrice()).isNull();
        assertThat(response.getStockQuantity()).isNull();
        assertThat(response.getImageUrl()).isNull();
        assertThat(response.getCategory()).isNull();
        assertThat(response.getIsActive()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    public void testProductResponseWithZeroValues() {
        // Given
        ProductResponse response = new ProductResponse();

        // When
        response.setProductId(0L);
        response.setPrice(BigDecimal.ZERO);
        response.setStockQuantity(0);
        response.setIsActive(false);

        // Then
        assertThat(response.getProductId()).isEqualTo(0L);
        assertThat(response.getPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(response.getStockQuantity()).isEqualTo(0);
        assertThat(response.getIsActive()).isFalse();
    }

    @Test
    public void testProductResponseWithNegativeStock() {
        // Given
        ProductResponse response = new ProductResponse();

        // When
        response.setStockQuantity(-10);

        // Then
        assertThat(response.getStockQuantity()).isEqualTo(-10);
    }

    @Test
    public void testProductResponseWithLargePrice() {
        // Given
        ProductResponse response = new ProductResponse();

        // When
        response.setPrice(new BigDecimal("999999999.99"));

        // Then
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("999999999.99"));
    }

    @Test
    public void testProductResponseWithEmptyStrings() {
        // Given
        ProductResponse response = new ProductResponse();

        // When
        response.setName("");
        response.setDescription("");
        response.setImageUrl("");

        // Then
        assertThat(response.getName()).isEqualTo("");
        assertThat(response.getDescription()).isEqualTo("");
        assertThat(response.getImageUrl()).isEqualTo("");
    }
}
