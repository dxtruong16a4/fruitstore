package com.fruitstore.repository;

import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for ProductRepository
 * Tests custom queries, filtering, and CRUD operations
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category category1;
    private Category category2;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    @BeforeEach
    public void setUp() {
        // Create test categories
        category1 = new Category("Trái cây tươi", "Các loại trái cây tươi ngon");
        category1.setIsActive(true);
        
        category2 = new Category("Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước");
        category2.setIsActive(true);

        category1 = entityManager.persistAndFlush(category1);
        category2 = entityManager.persistAndFlush(category2);

        // Create test products
        product1 = new Product();
        product1.setName("Táo Fuji Nhật Bản");
        product1.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        product1.setPrice(new BigDecimal("150000.00"));
        product1.setStockQuantity(100);
        product1.setCategory(category2);
        product1.setIsActive(true);

        product2 = new Product();
        product2.setName("Cam Úc");
        product2.setDescription("Cam tươi nhập khẩu từ Úc");
        product2.setPrice(new BigDecimal("80000.00"));
        product2.setStockQuantity(150);
        product2.setCategory(category2);
        product2.setIsActive(true);

        product3 = new Product();
        product3.setName("Xoài Cát Hòa Lộc");
        product3.setDescription("Xoài Cát Hòa Lộc Tiền Giang");
        product3.setPrice(new BigDecimal("120000.00"));
        product3.setStockQuantity(80);
        product3.setCategory(category1);
        product3.setIsActive(true);

        product4 = new Product();
        product4.setName("Dưa Hấu Không Hạt");
        product4.setDescription("Dưa hấu không hạt tươi ngon");
        product4.setPrice(new BigDecimal("60000.00"));
        product4.setStockQuantity(0);
        product4.setCategory(category1);
        product4.setIsActive(false);

        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(product3);
        entityManager.persistAndFlush(product4);
        entityManager.clear();
    }

    @Test
    public void testFindByCategoryAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByCategoryAndIsActiveTrue(category1);

        // Then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindByCategory_CategoryIdAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByCategory_CategoryIdAndIsActiveTrue(category2.getCategoryId());

        // Then
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Táo Fuji Nhật Bản", "Cam Úc");
    }

    @Test
    public void testFindByPriceBetweenAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByPriceBetweenAndIsActiveTrue(
                new BigDecimal("70000.00"), new BigDecimal("130000.00"));

        // Then
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Cam Úc", "Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindByNameContainingIgnoreCaseAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue("táo");

        // Then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Táo Fuji Nhật Bản");
    }

    @Test
    public void testFindByStockQuantityGreaterThanAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByStockQuantityGreaterThanAndIsActiveTrue(90);

        // Then
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Táo Fuji Nhật Bản", "Cam Úc");
    }

    @Test
    public void testFindByStockQuantityAndIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByStockQuantityAndIsActiveTrue(0);

        // Then
        assertThat(products).isEmpty(); // product4 is inactive
    }

    @Test
    public void testFindByIsActiveTrue() {
        // When
        List<Product> products = productRepository.findByIsActiveTrue();

        // Then
        assertThat(products).hasSize(3);
        assertThat(products).extracting(Product::getName)
                .containsExactlyInAnyOrder("Táo Fuji Nhật Bản", "Cam Úc", "Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindByIsActiveTrueOrderByNameAsc() {
        // When
        List<Product> products = productRepository.findByIsActiveTrueOrderByNameAsc();

        // Then
        assertThat(products).hasSize(3);
        assertThat(products).extracting(Product::getName)
                .containsExactly("Cam Úc", "Táo Fuji Nhật Bản", "Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindByIsActiveTrueOrderByPriceAsc() {
        // When
        List<Product> products = productRepository.findByIsActiveTrueOrderByPriceAsc();

        // Then
        assertThat(products).hasSize(3);
        assertThat(products).extracting(Product::getPrice)
                .containsExactly(
                        new BigDecimal("80000.00"),
                        new BigDecimal("120000.00"),
                        new BigDecimal("150000.00")
                );
    }

    @Test
    public void testFindByIsActiveTrueOrderByPriceDesc() {
        // When
        List<Product> products = productRepository.findByIsActiveTrueOrderByPriceDesc();

        // Then
        assertThat(products).hasSize(3);
        assertThat(products).extracting(Product::getPrice)
                .containsExactly(
                        new BigDecimal("150000.00"),
                        new BigDecimal("120000.00"),
                        new BigDecimal("80000.00")
                );
    }

    @Test
    public void testFindByCategory_CategoryIdAndIsActiveTrueWithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> products = productRepository.findByCategory_CategoryIdAndIsActiveTrue(category2.getCategoryId(), pageable);

        // Then
        assertThat(products.getContent()).hasSize(1);
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindByIsActiveTrueWithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> products = productRepository.findByIsActiveTrue(pageable);

        // Then
        assertThat(products.getContent()).hasSize(2);
        assertThat(products.getTotalElements()).isEqualTo(3);
        assertThat(products.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindProductsWithFilters() {
        // When - Test with category filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findProductsWithFilters(
                category1.getCategoryId(), null, null, null, pageable);

        // Then
        assertThat(products.getContent()).hasSize(1);
        assertThat(products.getContent().get(0).getName()).isEqualTo("Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindProductsWithFiltersByPriceRange() {
        // When - Test with price range filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findProductsWithFilters(
                null, new BigDecimal("70000.00"), new BigDecimal("130000.00"), null, pageable);

        // Then
        assertThat(products.getContent()).hasSize(2);
        assertThat(products.getContent()).extracting(Product::getName)
                .containsExactlyInAnyOrder("Cam Úc", "Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindProductsWithFiltersByKeyword() {
        // When - Test with keyword filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findProductsWithFilters(
                null, null, null, "táo", pageable);

        // Then
        assertThat(products.getContent()).hasSize(1);
        assertThat(products.getContent().get(0).getName()).isEqualTo("Táo Fuji Nhật Bản");
    }

    @Test
    public void testFindProductsWithFiltersAllFilters() {
        // When - Test with all filters
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = productRepository.findProductsWithFilters(
                category2.getCategoryId(),
                new BigDecimal("70000.00"),
                new BigDecimal("160000.00"),
                "nhật",
                pageable);

        // Then
        assertThat(products.getContent()).hasSize(1);
        assertThat(products.getContent().get(0).getName()).isEqualTo("Táo Fuji Nhật Bản");
    }

    @Test
    public void testFindLowStockProducts() {
        // When
        List<Product> products = productRepository.findLowStockProducts(90);

        // Then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Xoài Cát Hòa Lộc");
    }

    @Test
    public void testFindByNameIgnoreCase() {
        // When
        Optional<Product> found = productRepository.findByNameIgnoreCase("TÁO FUJI NHẬT BẢN");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Táo Fuji Nhật Bản");
    }

    @Test
    public void testExistsByNameIgnoreCase() {
        // When & Then
        assertThat(productRepository.existsByNameIgnoreCase("TÁO FUJI NHẬT BẢN")).isTrue();
        assertThat(productRepository.existsByNameIgnoreCase("Non-existent Product")).isFalse();
    }

    @Test
    public void testCountByCategory_CategoryIdAndIsActiveTrue() {
        // When
        long count1 = productRepository.countByCategory_CategoryIdAndIsActive(category1.getCategoryId(), true);
        long count2 = productRepository.countByCategory_CategoryIdAndIsActive(category2.getCategoryId(), true);

        // Then
        assertThat(count1).isEqualTo(1);
        assertThat(count2).isEqualTo(2);
    }

    @Test
    public void testCountByIsActive() {
        // When
        long activeCount = productRepository.countByIsActive(true);
        long inactiveCount = productRepository.countByIsActive(false);

        // Then
        assertThat(activeCount).isEqualTo(3);
        assertThat(inactiveCount).isEqualTo(1);
    }

    @Test
    public void testCRUDOperations() {
        // Test Create
        Product newProduct = new Product();
        newProduct.setName("Test Product");
        newProduct.setDescription("Test Description");
        newProduct.setPrice(new BigDecimal("100000.00"));
        newProduct.setStockQuantity(50);
        newProduct.setCategory(category1);
        newProduct.setIsActive(true);

        Product saved = productRepository.save(newProduct);
        assertThat(saved.getProductId()).isNotNull();

        // Test Read
        Optional<Product> found = productRepository.findById(saved.getProductId());
        assertThat(found).isPresent();

        // Test Update
        found.get().setPrice(new BigDecimal("120000.00"));
        Product updated = productRepository.save(found.get());
        assertThat(updated.getPrice()).isEqualTo(new BigDecimal("120000.00"));

        // Test Delete
        productRepository.deleteById(saved.getProductId());
        Optional<Product> deleted = productRepository.findById(saved.getProductId());
        assertThat(deleted).isEmpty();
    }
}
