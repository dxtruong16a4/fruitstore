package com.fruitstore.controller;

import com.fruitstore.dto.request.category.CreateCategoryRequest;
import com.fruitstore.dto.request.category.UpdateCategoryRequest;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for category management
 * Handles CRUD operations for categories
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories
     * Public endpoint - no authentication required
     * 
     * @return list of all categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get all active categories
     * Public endpoint - no authentication required
     * 
     * @return list of active categories
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategories() {
        List<CategoryResponse> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get category by ID
     * Public endpoint - no authentication required
     * 
     * @param id the category ID
     * @return category response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    /**
     * Get active category by ID
     * Public endpoint - no authentication required
     *
     * @param id the category ID
     * @return active category response
     */
    @GetMapping("/{id}/active")
    public ResponseEntity<ApiResponse<CategoryResponse>> getActiveCategoryById(@PathVariable("id") Long id) {
        CategoryResponse category = categoryService.getActiveCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    /**
     * Create a new category
     * Admin only endpoint - requires ADMIN role
     * 
     * @param request category creation request
     * @return created category response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", category));
    }

    /**
     * Update an existing category
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the category ID
     * @param request category update request
     * @return updated category response
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        
        CategoryResponse category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", category));
    }

    /**
     * Delete a category (soft delete)
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the category ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }

    /**
     * Permanently delete a category
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the category ID
     * @return success response
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteCategory(@PathVariable Long id) {
        categoryService.permanentlyDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category permanently deleted successfully", null));
    }

    /**
     * Activate a category
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the category ID
     * @return updated category response
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CategoryResponse>> activateCategory(@PathVariable Long id) {
        CategoryResponse category = categoryService.activateCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category activated successfully", category));
    }

    /**
     * Deactivate a category
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the category ID
     * @return updated category response
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivateCategory(@PathVariable Long id) {
        CategoryResponse category = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deactivated successfully", category));
    }

    /**
     * Search categories by name
     * Public endpoint - no authentication required
     * 
     * @param name the name pattern to search for
     * @return list of matching categories
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> searchCategoriesByName(
            @RequestParam String name) {
        
        List<CategoryResponse> categories = categoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Search active categories by name
     * Public endpoint - no authentication required
     * 
     * @param name the name pattern to search for
     * @return list of matching active categories
     */
    @GetMapping("/search/active")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> searchActiveCategoriesByName(
            @RequestParam String name) {
        
        List<CategoryResponse> categories = categoryService.searchActiveCategoriesByName(name);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /**
     * Get categories with product counts
     * Public endpoint - no authentication required
     * 
     * @return list of categories with their product counts
     */
    @GetMapping("/with-counts")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoriesWithProductCounts() {
        List<CategoryResponse> categories = categoryService.getCategoriesWithProductCounts();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
