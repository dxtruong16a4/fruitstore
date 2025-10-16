package com.fruitstore.service;

import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import com.fruitstore.dto.request.product.CreateProductRequest;
import com.fruitstore.dto.request.product.ProductFilterRequest;
import com.fruitstore.dto.request.product.UpdateProductRequest;
import com.fruitstore.dto.response.product.ProductListResponse;
import com.fruitstore.dto.response.product.ProductResponse;
import com.fruitstore.repository.CategoryRepository;
import com.fruitstore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ProductService
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product1;
    private Product product2;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;
    private ProductFilterRequest filterRequest;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Trái cây tươi");
        category.setDescription("Các loại trái cây tươi ngon");
        category.setIsActive(true);

        product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Táo Fuji Nhật Bản");
        product1.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        product1.setPrice(new BigDecimal("150000.00"));
        product1.setStockQuantity(100);
        product1.setImageUrl("/images/products/apple-fuji.jpg");
        product1.setCategory(category);
        product1.setIsActive(true);
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());

        product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Cam Úc");
        product2.setDescription("Cam tươi nhập khẩu từ Úc");
        product2.setPrice(new BigDecimal("80000.00"));
        product2.setStockQuantity(150);
        product2.setImageUrl("/images/products/orange.jpg");
        product2.setCategory(category);
        product2.setIsActive(true);
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateProductRequest();
        createRequest.setName("Nho Mỹ");
        createRequest.setDescription("Nho xanh không hạt nhập khẩu từ Mỹ");
        createRequest.setPrice(new BigDecimal("200000.00"));
        createRequest.setStockQuantity(60);
        createRequest.setImageUrl("/images/products/grape.jpg");
        createRequest.setCategoryId(1L);
        createRequest.setIsActive(true);

        updateRequest = new UpdateProductRequest();
        updateRequest.setName("Táo Fuji Nhật Bản cập nhật");
        updateRequest.setDescription("Mô tả cập nhật");
        updateRequest.setPrice(new BigDecimal("160000.00"));
        updateRequest.setStockQuantity(120);
        updateRequest.setImageUrl("/images/products/updated.jpg");
        updateRequest.setCategoryId(1L);
        updateRequest.setIsActive(true);

        filterRequest = new ProductFilterRequest();
        filterRequest.setCategoryId(1L);
        filterRequest.setMinPrice(new BigDecimal("100000.00"));
        filterRequest.setMaxPrice(new BigDecimal("300000.00"));
        filterRequest.setKeyword("táo");
        filterRequest.setPage(0);
        filterRequest.setSize(10);
        filterRequest.setSortBy("name");
        filterRequest.setSortDirection("asc");
    }

    @Test
    public void testGetAllProducts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, 2);
        when(productRepository.findByIsActiveTrue(pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.getAllProducts(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(productRepository).findByIsActiveTrue(pageable);
    }

    @Test
    public void testGetProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When
        ProductResponse result = productService.getProductById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Táo Fuji Nhật Bản");
        verify(productRepository).findById(1L);
    }

    @Test
    public void testGetProductByIdNotFound() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found with id: 999");
        verify(productRepository).findById(999L);
    }

    @Test
    public void testGetActiveProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When
        ProductResponse result = productService.getActiveProductById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getIsActive()).isTrue();
        verify(productRepository).findById(1L);
    }

    @Test
    public void testGetActiveProductByIdInactive() {
        // Given
        product1.setIsActive(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When & Then
        assertThatThrownBy(() -> productService.getActiveProductById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product is not active with id: 1");
        verify(productRepository).findById(1L);
    }

    @Test
    public void testGetProductsByCategory() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, 2);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory_CategoryIdAndIsActiveTrue(1L, pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.getProductsByCategory(1L, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(categoryRepository).findById(1L);
        verify(productRepository).findByCategory_CategoryIdAndIsActiveTrue(1L, pageable);
    }

    @Test
    public void testGetProductsByCategoryNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductsByCategory(999L, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category not found with id: 999");
        verify(categoryRepository).findById(999L);
        verify(productRepository, never()).findByCategory_CategoryIdAndIsActiveTrue(anyLong(), any(Pageable.class));
    }

    @Test
    public void testSearchProducts() {
        // Given
        List<Product> products = Arrays.asList(product1);
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 10), 1);
        when(productRepository.findProductsWithFilters(anyLong(), any(), any(), anyString(), any(Pageable.class)))
                .thenReturn(productPage);

        // When
        ProductListResponse result = productService.searchProducts(filterRequest);

        // Then
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        verify(productRepository).findProductsWithFilters(anyLong(), any(), any(), anyString(), any(Pageable.class));
    }

    @Test
    public void testCreateProduct() {
        // Given
        when(productRepository.existsByNameIgnoreCase("Nho Mỹ")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // When
        ProductResponse result = productService.createProduct(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Táo Fuji Nhật Bản");
        verify(productRepository).existsByNameIgnoreCase("Nho Mỹ");
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testCreateProductNameExists() {
        // Given
        when(productRepository.existsByNameIgnoreCase("Nho Mỹ")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name already exists: Nho Mỹ");
        verify(productRepository).existsByNameIgnoreCase("Nho Mỹ");
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testCreateProductCategoryNotFound() {
        // Given
        when(productRepository.existsByNameIgnoreCase("Nho Mỹ")).thenReturn(false);
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category not found with id: 999");
        verify(productRepository).existsByNameIgnoreCase("Nho Mỹ");
        verify(categoryRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.existsByNameIgnoreCase("Táo Fuji Nhật Bản cập nhật")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // When
        ProductResponse result = productService.updateProduct(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).findById(1L);
        verify(productRepository).existsByNameIgnoreCase("Táo Fuji Nhật Bản cập nhật");
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProductNameExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.existsByNameIgnoreCase("Táo Fuji Nhật Bản cập nhật")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> productService.updateProduct(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name already exists: Táo Fuji Nhật Bản cập nhật");
        verify(productRepository).findById(1L);
        verify(productRepository).existsByNameIgnoreCase("Táo Fuji Nhật Bản cập nhật");
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testCheckStockAvailability() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When
        Boolean result = productService.checkStockAvailability(1L, 50);

        // Then
        assertThat(result).isTrue();
        verify(productRepository).findById(1L);
    }

    @Test
    public void testCheckStockAvailabilityInsufficient() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When
        Boolean result = productService.checkStockAvailability(1L, 150);

        // Then
        assertThat(result).isFalse();
        verify(productRepository).findById(1L);
    }

    @Test
    public void testReduceStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // When
        productService.reduceStock(1L, 20);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testReduceStockInsufficient() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When & Then
        assertThatThrownBy(() -> productService.reduceStock(1L, 150))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testAddStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // When
        productService.addStock(1L, 20);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testGetLowStockProducts() {
        // Given
        List<Product> products = Arrays.asList(product1);
        when(productRepository.findLowStockProducts(90)).thenReturn(products);

        // When
        List<ProductResponse> result = productService.getLowStockProducts(90);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        verify(productRepository).findLowStockProducts(90);
    }

    @Test
    public void testGetTopProductsByStock() {
        // Given
        List<Product> products = Arrays.asList(product2, product1);
        when(productRepository.findTopProductsByStockQuantity(any(Pageable.class))).thenReturn(products);

        // When
        List<ProductResponse> result = productService.getTopProductsByStock(10);

        // Then
        assertThat(result).hasSize(2);
        verify(productRepository).findTopProductsByStockQuantity(any(Pageable.class));
    }

    @Test
    public void testSearchProductsByName() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(product1);
        Page<Product> productPage = new PageImpl<>(products, pageable, 1);
        when(productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue("táo", pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.searchProductsByName("táo", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(productRepository).findByNameContainingIgnoreCaseAndIsActiveTrue("táo", pageable);
    }

    @Test
    public void testGetProductsByPriceRange() {
        // Given
        List<Product> products = Arrays.asList(product1);
        when(productRepository.findByPriceBetweenAndIsActiveTrue(
                new BigDecimal("100000.00"), new BigDecimal("200000.00"))).thenReturn(products);

        // When
        List<ProductResponse> result = productService.getProductsByPriceRange(
                new BigDecimal("100000.00"), new BigDecimal("200000.00"));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        verify(productRepository).findByPriceBetweenAndIsActiveTrue(
                new BigDecimal("100000.00"), new BigDecimal("200000.00"));
    }

    @Test
    public void testGetProductsByCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, 2);
        when(productRepository.findByCategoryIdInAndIsActiveTrue(categoryIds, pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.getProductsByCategories(categoryIds, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(productRepository).findByCategoryIdInAndIsActiveTrue(categoryIds, pageable);
    }
}
