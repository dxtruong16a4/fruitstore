package com.fruitstore.domain.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for Product entity
 * Tests entity mapping, @ManyToOne relationship, and business logic
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class ProductEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testProductEntityMapping() {
        // Given
        Product product = new Product();
        product.setName("Táo Fuji Nhật Bản");
        product.setDescription("Táo Fuji nhập khẩu từ Nhật Bản, ngọt tự nhiên");
        product.setPrice(new BigDecimal("150000.00"));
        product.setStockQuantity(100);
        product.setImageUrl("/images/products/apple-fuji.jpg");
        product.setIsActive(true);

        // When
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product savedProduct = entityManager.find(Product.class, product.getProductId());
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Táo Fuji Nhật Bản");
        assertThat(savedProduct.getDescription()).isEqualTo("Táo Fuji nhập khẩu từ Nhật Bản, ngọt tự nhiên");
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(savedProduct.getStockQuantity()).isEqualTo(100);
        assertThat(savedProduct.getImageUrl()).isEqualTo("/images/products/apple-fuji.jpg");
        assertThat(savedProduct.getIsActive()).isTrue();
        assertThat(savedProduct.getCreatedAt()).isNotNull();
        assertThat(savedProduct.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testProductWithCategoryRelationship() {
        // Given
        Category category = new Category("Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước");
        entityManager.persist(category);
        entityManager.flush();

        Product product = new Product();
        product.setName("Nho Mỹ");
        product.setDescription("Nho xanh không hạt nhập khẩu từ Mỹ");
        product.setPrice(new BigDecimal("200000.00"));
        product.setStockQuantity(60);
        product.setCategory(category);
        product.setIsActive(true);

        // When
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product savedProduct = entityManager.find(Product.class, product.getProductId());
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getCategory()).isNotNull();
        assertThat(savedProduct.getCategory().getName()).isEqualTo("Trái cây nhập khẩu");
        assertThat(savedProduct.getCategory().getCategoryId()).isEqualTo(category.getCategoryId());
    }

    @Test
    public void testProductWithMinimalData() {
        // Given
        Product product = new Product("Cam Úc", "Cam tươi nhập khẩu từ Úc", new BigDecimal("80000.00"));

        // When
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product savedProduct = entityManager.find(Product.class, product.getProductId());
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Cam Úc");
        assertThat(savedProduct.getDescription()).isEqualTo("Cam tươi nhập khẩu từ Úc");
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("80000.00"));
        assertThat(savedProduct.getStockQuantity()).isEqualTo(0); // Default value
        assertThat(savedProduct.getImageUrl()).isNull();
        assertThat(savedProduct.getCategory()).isNull();
        assertThat(savedProduct.getIsActive()).isTrue(); // Default value
        assertThat(savedProduct.getCreatedAt()).isNotNull();
        assertThat(savedProduct.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testProductPrePersistAndPreUpdateCallbacks() {
        // Given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));

        // When
        entityManager.persist(product);
        entityManager.flush();

        // Then - check PrePersist
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
        assertThat(product.getStockQuantity()).isEqualTo(0); // Should be set by @PrePersist
        assertThat(product.getIsActive()).isTrue(); // Should be set by @PrePersist

        // When - update product
        product.setName("Updated Product");
        entityManager.flush();

        // Then - check PreUpdate
        assertThat(product.getUpdatedAt()).isAfter(product.getCreatedAt());
    }

    @Test
    public void testProductBusinessMethods() {
        // Given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("50000.00"));
        product.setStockQuantity(100);

        // Test isInStock
        assertThat(product.isInStock()).isTrue();
        product.setStockQuantity(0);
        assertThat(product.isInStock()).isFalse();

        // Test hasSufficientStock
        product.setStockQuantity(50);
        assertThat(product.hasSufficientStock(30)).isTrue();
        assertThat(product.hasSufficientStock(50)).isTrue();
        assertThat(product.hasSufficientStock(60)).isFalse();

        // Test reduceStock
        product.reduceStock(20);
        assertThat(product.getStockQuantity()).isEqualTo(30);

        // Test reduceStock with insufficient stock
        assertThatThrownBy(() -> product.reduceStock(40))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");

        // Test addStock
        product.addStock(10);
        assertThat(product.getStockQuantity()).isEqualTo(40);

        // Test addStock with invalid quantity
        assertThatThrownBy(() -> product.addStock(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be non-negative");
    }

    @Test
    public void testProductInactiveStatus() {
        // Given
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));
        product.setIsActive(false);

        // When
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product savedProduct = entityManager.find(Product.class, product.getProductId());
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getIsActive()).isFalse();
    }

    @Test
    public void testProductWithCategoryCascade() {
        // Given
        Category category = new Category("Test Category", "Test Description");
        entityManager.persist(category);
        entityManager.flush();

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));
        product.setCategory(category);

        // When
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product savedProduct = entityManager.find(Product.class, product.getProductId());
        assertThat(savedProduct.getCategory()).isNotNull();
        assertThat(savedProduct.getCategory().getName()).isEqualTo("Test Category");
    }
}
