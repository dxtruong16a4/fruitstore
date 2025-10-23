package com.fruitstore.controller;

import com.fruitstore.dto.request.product.CreateProductRequest;
import com.fruitstore.dto.request.product.ProductFilterRequest;
import com.fruitstore.dto.request.product.UpdateProductRequest;
import com.fruitstore.dto.response.product.ProductListResponse;
import com.fruitstore.dto.response.product.ProductResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for product management
 * Handles CRUD operations, search, filtering, and stock management for products
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products with pagination
     * Public endpoint - no authentication required
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (name, price, createdAt, stockQuantity)
     * @param sortDirection sort direction (asc, desc)
     * @return page of products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get product by ID
     * Public endpoint - no authentication required
     * 
     * @param id the product ID
     * @return product response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable("id") Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Get active product by ID
     * Public endpoint - no authentication required
     *
     * @param id the product ID
     * @return active product response
     */
    @GetMapping("/{id}/active")
    public ResponseEntity<ApiResponse<ProductResponse>> getActiveProductById(@PathVariable("id") Long id) {
        ProductResponse product = productService.getActiveProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Get products by category with pagination
     * Public endpoint - no authentication required
     * 
     * @param categoryId the category ID
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (name, price, createdAt, stockQuantity)
     * @param sortDirection sort direction (asc, desc)
     * @return page of products in the category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Search products with filters
     * Public endpoint - no authentication required
     * 
     * @param filterRequest filter criteria
     * @return filtered product list response
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductListResponse>> searchProducts(
            @Valid @ModelAttribute ProductFilterRequest filterRequest) {
        
        ProductListResponse products = productService.searchProducts(filterRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Search products by name with pagination
     * Public endpoint - no authentication required
     * 
     * @param name the name pattern to search for
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (name, price, createdAt, stockQuantity)
     * @param sortDirection sort direction (asc, desc)
     * @return page of matching products
     */
    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProductsByName(
            @RequestParam String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<ProductResponse> products = productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get products by price range
     * Public endpoint - no authentication required
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products within price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        List<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get products by multiple categories
     * Public endpoint - no authentication required
     * 
     * @param categoryIds comma-separated list of category IDs
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (name, price, createdAt, stockQuantity)
     * @param sortDirection sort direction (asc, desc)
     * @return page of products in any of the specified categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategories(
            @RequestParam List<Long> categoryIds,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<ProductResponse> products = productService.getProductsByCategories(categoryIds, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Create a new product
     * Admin only endpoint - requires ADMIN role
     * 
     * @param request product creation request
     * @return created product response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", product));
    }

    /**
     * Update an existing product
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the product ID
     * @param request product update request
     * @return updated product response
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }

    /**
     * Delete a product (soft delete)
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the product ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    /**
     * Permanently delete a product
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the product ID
     * @return success response
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteProduct(@PathVariable Long id) {
        productService.permanentlyDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product permanently deleted successfully", null));
    }

    /**
     * Check stock availability for a product
     * Public endpoint - no authentication required
     * 
     * @param id the product ID
     * @param quantity the requested quantity
     * @return stock availability status
     */
    @GetMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Boolean>> checkStockAvailability(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        Boolean available = productService.checkStockAvailability(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    /**
     * Reduce stock quantity for a product
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the product ID
     * @param quantity the quantity to reduce
     * @return success response
     */
    @PutMapping("/{id}/stock/reduce")
    public ResponseEntity<ApiResponse<Void>> reduceStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        productService.reduceStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock reduced successfully", null));
    }

    /**
     * Add stock quantity for a product
     * Admin only endpoint - requires ADMIN role
     * 
     * @param id the product ID
     * @param quantity the quantity to add
     * @return success response
     */
    @PutMapping("/{id}/stock/add")
    public ResponseEntity<ApiResponse<Void>> addStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        productService.addStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", null));
    }

    /**
     * Get low stock products
     * Admin only endpoint - requires ADMIN role
     * 
     * @param threshold the stock threshold
     * @return list of products with stock below threshold
     */
    @GetMapping("/admin/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts(
            @RequestParam(value = "threshold", defaultValue = "10") Integer threshold) {
        
        List<ProductResponse> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get top products by stock quantity
     * Admin only endpoint - requires ADMIN role
     * 
     * @param limit the maximum number of products to return
     * @return list of products with highest stock
     */
    @GetMapping("/admin/top-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getTopProductsByStock(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        
        List<ProductResponse> products = productService.getTopProductsByStock(limit);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Create Pageable from pagination and sort parameters
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDirection sort direction
     * @return pageable object
     */
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort sort = createSort(sortBy, sortDirection);
        return PageRequest.of(page, size, sort);
    }

    /**
     * Create Sort object from sort parameters
     * 
     * @param sortBy the field to sort by
     * @param sortDirection the sort direction
     * @return sort object
     */
    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        switch (sortBy.toLowerCase()) {
            case "price":
                return Sort.by(direction, "price");
            case "name":
                return Sort.by(direction, "name");
            case "createdat":
            case "created_at":
                return Sort.by(direction, "createdAt");
            case "stock":
            case "stockquantity":
            case "stock_quantity":
                return Sort.by(direction, "stockQuantity");
            default:
                return Sort.by(direction, "name"); // Default sort by name
        }
    }
}
