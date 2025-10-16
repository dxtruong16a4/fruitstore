package com.fruitstore.service;

import com.fruitstore.domain.product.Category;
import com.fruitstore.dto.request.category.CreateCategoryRequest;
import com.fruitstore.dto.request.category.UpdateCategoryRequest;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.repository.CategoryRepository;
import com.fruitstore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for CategoryService
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private Category category2;
    private CreateCategoryRequest createRequest;
    private UpdateCategoryRequest updateRequest;

    @BeforeEach
    public void setUp() {
        category1 = new Category();
        category1.setCategoryId(1L);
        category1.setName("Trái cây tươi");
        category1.setDescription("Các loại trái cây tươi ngon");
        category1.setImageUrl("/images/categories/fresh-fruits.jpg");
        category1.setIsActive(true);
        category1.setCreatedAt(LocalDateTime.now());

        category2 = new Category();
        category2.setCategoryId(2L);
        category2.setName("Trái cây nhập khẩu");
        category2.setDescription("Trái cây nhập khẩu từ các nước");
        category2.setImageUrl("/images/categories/imported.jpg");
        category2.setIsActive(false);
        category2.setCreatedAt(LocalDateTime.now());

        createRequest = new CreateCategoryRequest();
        createRequest.setName("Trái cây sấy");
        createRequest.setDescription("Các loại trái cây sấy khô dinh dưỡng");
        createRequest.setImageUrl("/images/categories/dried.jpg");

        updateRequest = new UpdateCategoryRequest();
        updateRequest.setName("Trái cây tươi cập nhật");
        updateRequest.setDescription("Mô tả cập nhật");
        updateRequest.setImageUrl("/images/categories/updated.jpg");
        updateRequest.setIsActive(true);
    }

    @Test
    public void testGetAllCategories() {
        // Given
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(categories);

        // When
        List<CategoryResponse> result = categoryService.getAllCategories();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Trái cây tươi");
        assertThat(result.get(1).getName()).isEqualTo("Trái cây nhập khẩu");
        verify(categoryRepository).findAllByOrderByNameAsc();
    }

    @Test
    public void testGetActiveCategories() {
        // Given
        List<Category> activeCategories = Arrays.asList(category1);
        when(categoryRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(activeCategories);

        // When
        List<CategoryResponse> result = categoryService.getActiveCategories();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Trái cây tươi");
        assertThat(result.get(0).getIsActive()).isTrue();
        verify(categoryRepository).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    public void testGetCategoryById() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        // When
        CategoryResponse result = categoryService.getCategoryById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCategoryId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Trái cây tươi");
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategoryById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category not found with id: 999");
        verify(categoryRepository).findById(999L);
    }

    @Test
    public void testGetActiveCategoryById() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        // When
        CategoryResponse result = categoryService.getActiveCategoryById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCategoryId()).isEqualTo(1L);
        assertThat(result.getIsActive()).isTrue();
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void testGetActiveCategoryByIdInactive() {
        // Given
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));

        // When & Then
        assertThatThrownBy(() -> categoryService.getActiveCategoryById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category is not active with id: 2");
        verify(categoryRepository).findById(2L);
    }

    @Test
    public void testCreateCategory() {
        // Given
        when(categoryRepository.findByNameIgnoreCase("Trái cây sấy")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        // When
        CategoryResponse result = categoryService.createCategory(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Trái cây tươi");
        verify(categoryRepository).findByNameIgnoreCase("Trái cây sấy");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testCreateCategoryNameExists() {
        // Given
        when(categoryRepository.findByNameIgnoreCase("Trái cây sấy")).thenReturn(Optional.of(category1));

        // When & Then
        assertThatThrownBy(() -> categoryService.createCategory(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category name already exists: Trái cây sấy");
        verify(categoryRepository).findByNameIgnoreCase("Trái cây sấy");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findByNameIgnoreCase("Trái cây tươi cập nhật")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        // When
        CategoryResponse result = categoryService.updateCategory(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findByNameIgnoreCase("Trái cây tươi cập nhật");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testUpdateCategoryNameExists() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findByNameIgnoreCase("Trái cây tươi cập nhật")).thenReturn(Optional.of(category1));

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category name already exists: Trái cây tươi cập nhật");
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findByNameIgnoreCase("Trái cây tươi cập nhật");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(productRepository.countByCategory_CategoryIdAndIsActiveTrue(1L, true)).thenReturn(0L);
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        // When
        categoryService.deleteCategory(1L);

        // Then
        verify(categoryRepository).findById(1L);
        verify(productRepository).countByCategory_CategoryIdAndIsActiveTrue(1L, true);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testDeleteCategoryWithProducts() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(productRepository.countByCategory_CategoryIdAndIsActiveTrue(1L, true)).thenReturn(5L);

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot delete category with associated products. Product count: 5");
        verify(categoryRepository).findById(1L);
        verify(productRepository).countByCategory_CategoryIdAndIsActiveTrue(1L, true);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testPermanentlyDeleteCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(productRepository.countByCategory_CategoryIdAndIsActiveTrue(1L, true)).thenReturn(0L);

        // When
        categoryService.permanentlyDeleteCategory(1L);

        // Then
        verify(categoryRepository).findById(1L);
        verify(productRepository).countByCategory_CategoryIdAndIsActiveTrue(1L, true);
        verify(categoryRepository).delete(category1);
    }

    @Test
    public void testActivateCategory() {
        // Given
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(categoryRepository.save(any(Category.class))).thenReturn(category2);

        // When
        CategoryResponse result = categoryService.activateCategory(2L);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository).findById(2L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testDeactivateCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        // When
        CategoryResponse result = categoryService.deactivateCategory(1L);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testSearchCategoriesByName() {
        // Given
        List<Category> categories = Arrays.asList(category1);
        when(categoryRepository.findByNameContainingIgnoreCase("tươi")).thenReturn(categories);

        // When
        List<CategoryResponse> result = categoryService.searchCategoriesByName("tươi");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Trái cây tươi");
        verify(categoryRepository).findByNameContainingIgnoreCase("tươi");
    }

    @Test
    public void testSearchActiveCategoriesByName() {
        // Given
        List<Category> categories = Arrays.asList(category1);
        when(categoryRepository.findByIsActiveTrueAndNameContainingIgnoreCase("tươi")).thenReturn(categories);

        // When
        List<CategoryResponse> result = categoryService.searchActiveCategoriesByName("tươi");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Trái cây tươi");
        verify(categoryRepository).findByIsActiveTrueAndNameContainingIgnoreCase("tươi");
    }

    @Test
    public void testGetCategoriesWithProductCounts() {
        // Given
        List<Category> categories = Arrays.asList(category1);
        when(categoryRepository.findActiveCategoriesWithProducts()).thenReturn(categories);
        when(productRepository.countByCategory_CategoryIdAndIsActiveTrue(1L, true)).thenReturn(10L);

        // When
        List<CategoryResponse> result = categoryService.getCategoriesWithProductCounts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductCount()).isEqualTo(10L);
        verify(categoryRepository).findActiveCategoriesWithProducts();
        verify(productRepository).countByCategory_CategoryIdAndIsActiveTrue(1L, true);
    }
}
