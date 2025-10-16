package com.fruitstore.service;

import com.fruitstore.domain.product.Category;
import com.fruitstore.dto.request.category.CreateCategoryRequest;
import com.fruitstore.dto.request.category.UpdateCategoryRequest;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.repository.CategoryRepository;
import com.fruitstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for category management
 * Handles CRUD operations and business logic for categories
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get all categories
     * 
     * @return list of all categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active categories
     * 
     * @return list of active categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderByNameAsc();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get category by ID
     * 
     * @param id the category ID
     * @return category response
     * @throws IllegalArgumentException if category not found
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        return mapToCategoryResponse(category);
    }

    /**
     * Get active category by ID
     * 
     * @param id the category ID
     * @return category response
     * @throws IllegalArgumentException if category not found or inactive
     */
    @Transactional(readOnly = true)
    public CategoryResponse getActiveCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        if (!category.getIsActive()) {
            throw new IllegalArgumentException("Category is not active with id: " + id);
        }

        return mapToCategoryResponse(category);
    }

    /**
     * Create a new category
     * 
     * @param request category creation request
     * @return created category response
     * @throws IllegalArgumentException if category name already exists
     */
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // Check if category name already exists
        if (categoryRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists: " + request.getName());
        }

        // Create new category
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setIsActive(true); // New categories are active by default

        // Save category
        Category savedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(savedCategory);
    }

    /**
     * Update an existing category
     * 
     * @param id the category ID
     * @param request category update request
     * @return updated category response
     * @throws IllegalArgumentException if category not found or name already exists
     */
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        // Check if new name conflicts with existing category (excluding current one)
        if (!category.getName().equalsIgnoreCase(request.getName()) && 
            categoryRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists: " + request.getName());
        }

        // Update category fields
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        // Save updated category
        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Delete a category (soft delete by setting isActive to false)
     * 
     * @param id the category ID
     * @throws IllegalArgumentException if category not found or has associated products
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        // Check if category has associated products
        long productCount = productRepository.countByCategory_CategoryIdAndIsActiveTrue(id, true);
        if (productCount > 0) {
            throw new IllegalArgumentException("Cannot delete category with associated products. Product count: " + productCount);
        }

        // Soft delete by setting isActive to false
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    /**
     * Permanently delete a category
     * 
     * @param id the category ID
     * @throws IllegalArgumentException if category not found or has associated products
     */
    public void permanentlyDeleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        // Check if category has associated products
        long productCount = productRepository.countByCategory_CategoryIdAndIsActiveTrue(id, true);
        if (productCount > 0) {
            throw new IllegalArgumentException("Cannot permanently delete category with associated products. Product count: " + productCount);
        }

        // Permanently delete category
        categoryRepository.delete(category);
    }

    /**
     * Activate a category
     * 
     * @param id the category ID
     * @return updated category response
     * @throws IllegalArgumentException if category not found
     */
    public CategoryResponse activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        category.setIsActive(true);
        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Deactivate a category
     * 
     * @param id the category ID
     * @return updated category response
     * @throws IllegalArgumentException if category not found
     */
    public CategoryResponse deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        category.setIsActive(false);
        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Search categories by name
     * 
     * @param name the name pattern to search for
     * @return list of matching categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search active categories by name
     * 
     * @param name the name pattern to search for
     * @return list of matching active categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchActiveCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByIsActiveTrueAndNameContainingIgnoreCase(name);
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get categories with product counts
     * 
     * @return list of categories with their product counts
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesWithProductCounts() {
        List<Category> categories = categoryRepository.findActiveCategoriesWithProducts();
        return categories.stream()
                .map(category -> {
                    CategoryResponse response = mapToCategoryResponse(category);
                    // Set product count
                    long productCount = productRepository.countByCategory_CategoryIdAndIsActiveTrue(category.getCategoryId(), true);
                    response.setProductCount(productCount);
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * Map Category entity to CategoryResponse DTO
     * 
     * @param category the category entity
     * @return category response
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getIsActive(),
                category.getCreatedAt()
        );
    }
}
