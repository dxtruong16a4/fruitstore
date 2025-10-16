package com.fruitstore.domain.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Category entity
 * Tests entity mapping and basic persistence operations
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class CategoryEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testCategoryEntityMapping() {
        // Given
        Category category = new Category();
        category.setName("Trái cây tươi");
        category.setDescription("Các loại trái cây tươi ngon");
        category.setImageUrl("/images/categories/fresh-fruits.jpg");
        category.setIsActive(true);

        // When
        entityManager.persist(category);
        entityManager.flush();
        entityManager.clear();

        // Then
        Category savedCategory = entityManager.find(Category.class, category.getCategoryId());
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Trái cây tươi");
        assertThat(savedCategory.getDescription()).isEqualTo("Các loại trái cây tươi ngon");
        assertThat(savedCategory.getImageUrl()).isEqualTo("/images/categories/fresh-fruits.jpg");
        assertThat(savedCategory.getIsActive()).isTrue();
        assertThat(savedCategory.getCreatedAt()).isNotNull();
    }

    @Test
    public void testCategoryWithMinimalData() {
        // Given
        Category category = new Category("Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước");

        // When
        entityManager.persist(category);
        entityManager.flush();
        entityManager.clear();

        // Then
        Category savedCategory = entityManager.find(Category.class, category.getCategoryId());
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Trái cây nhập khẩu");
        assertThat(savedCategory.getDescription()).isEqualTo("Trái cây nhập khẩu từ các nước");
        assertThat(savedCategory.getImageUrl()).isNull();
        assertThat(savedCategory.getIsActive()).isTrue(); // Default value
        assertThat(savedCategory.getCreatedAt()).isNotNull();
    }

    @Test
    public void testCategoryInactiveStatus() {
        // Given
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(false);

        // When
        entityManager.persist(category);
        entityManager.flush();
        entityManager.clear();

        // Then
        Category savedCategory = entityManager.find(Category.class, category.getCategoryId());
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getIsActive()).isFalse();
    }

    @Test
    public void testCategoryPrePersistCallback() {
        // Given
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");

        // When
        entityManager.persist(category);
        entityManager.flush();

        // Then
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getIsActive()).isTrue(); // Should be set by @PrePersist
    }
}
