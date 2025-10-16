package com.fruitstore.repository;

import com.fruitstore.domain.product.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for CategoryRepository
 * Tests custom queries and CRUD operations
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    public void setUp() {
        // Create test categories
        category1 = new Category("Trái cây tươi", "Các loại trái cây tươi ngon");
        category1.setIsActive(true);
        
        category2 = new Category("Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước");
        category2.setIsActive(true);
        
        category3 = new Category("Trái cây sấy", "Các loại trái cây sấy khô dinh dưỡng");
        category3.setIsActive(false);

        entityManager.persistAndFlush(category1);
        entityManager.persistAndFlush(category2);
        entityManager.persistAndFlush(category3);
        entityManager.clear();
    }

    @Test
    public void testFindByName() {
        // When
        Optional<Category> found = categoryRepository.findByName("Trái cây tươi");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Trái cây tươi");
    }

    @Test
    public void testFindByNameNotFound() {
        // When
        Optional<Category> found = categoryRepository.findByName("Non-existent Category");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    public void testExistsByName() {
        // When & Then
        assertThat(categoryRepository.existsByName("Trái cây tươi")).isTrue();
        assertThat(categoryRepository.existsByName("Non-existent Category")).isFalse();
    }

    @Test
    public void testFindByIsActiveTrue() {
        // When
        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();

        // Then
        assertThat(activeCategories).hasSize(2);
        assertThat(activeCategories).extracting(Category::getName)
                .containsExactlyInAnyOrder("Trái cây tươi", "Trái cây nhập khẩu");
    }

    @Test
    public void testFindByIsActiveFalse() {
        // When
        List<Category> inactiveCategories = categoryRepository.findByIsActiveFalse();

        // Then
        assertThat(inactiveCategories).hasSize(1);
        assertThat(inactiveCategories.get(0).getName()).isEqualTo("Trái cây sấy");
    }

    @Test
    public void testFindAllByOrderByNameAsc() {
        // When
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();

        // Then
        assertThat(categories).hasSize(3);
        assertThat(categories).extracting(Category::getName)
                .containsExactly("Trái cây nhập khẩu", "Trái cây sấy", "Trái cây tươi");
    }

    @Test
    public void testFindByIsActiveTrueOrderByNameAsc() {
        // When
        List<Category> activeCategories = categoryRepository.findByIsActiveTrueOrderByNameAsc();

        // Then
        assertThat(activeCategories).hasSize(2);
        assertThat(activeCategories).extracting(Category::getName)
                .containsExactly("Trái cây nhập khẩu", "Trái cây tươi");
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        // When
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase("tươi");

        // Then
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("Trái cây tươi");
    }

    @Test
    public void testFindByNameContainingIgnoreCaseMultiple() {
        // When
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase("trái cây");

        // Then
        assertThat(categories).hasSize(3);
    }

    @Test
    public void testFindByIsActiveTrueAndNameContainingIgnoreCase() {
        // When
        List<Category> categories = categoryRepository.findByIsActiveTrueAndNameContainingIgnoreCase("nhập khẩu");

        // Then
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("Trái cây nhập khẩu");
        assertThat(categories.get(0).getIsActive()).isTrue();
    }

    @Test
    public void testCountByIsActive() {
        // When
        long activeCount = categoryRepository.countByIsActive(true);
        long inactiveCount = categoryRepository.countByIsActive(false);

        // Then
        assertThat(activeCount).isEqualTo(2);
        assertThat(inactiveCount).isEqualTo(1);
    }

    @Test
    public void testFindByNameIgnoreCase() {
        // When
        Optional<Category> found = categoryRepository.findByNameIgnoreCase("TRÁI CÂY TƯƠI");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Trái cây tươi");
    }

    @Test
    public void testFindActiveCategoriesWithProducts() {
        // Given - Create a product for category1
        // Note: This would require Product entity, but we're testing the query structure
        // When
        List<Category> categories = categoryRepository.findActiveCategoriesWithProducts();

        // Then
        assertThat(categories).hasSize(2); // Should return active categories
        assertThat(categories).extracting(Category::getIsActive).containsOnly(true);
    }

    @Test
    public void testFindActiveCategoriesWithProductCountGreaterThan() {
        // When
        List<Category> categories = categoryRepository.findActiveCategoriesWithProductCountGreaterThan(0L);

        // Then
        assertThat(categories).hasSize(2); // Both active categories
    }

    @Test
    public void testCRUDOperations() {
        // Test Create
        Category newCategory = new Category("Test Category", "Test Description");
        Category saved = categoryRepository.save(newCategory);
        assertThat(saved.getCategoryId()).isNotNull();

        // Test Read
        Optional<Category> found = categoryRepository.findById(saved.getCategoryId());
        assertThat(found).isPresent();

        // Test Update
        found.get().setDescription("Updated Description");
        Category updated = categoryRepository.save(found.get());
        assertThat(updated.getDescription()).isEqualTo("Updated Description");

        // Test Delete
        categoryRepository.deleteById(saved.getCategoryId());
        Optional<Category> deleted = categoryRepository.findById(saved.getCategoryId());
        assertThat(deleted).isEmpty();
    }
}
