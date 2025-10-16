package com.fruitstore.service;

import com.fruitstore.domain.product.Category;
import com.fruitstore.domain.product.Product;
import com.fruitstore.dto.request.product.CreateProductRequest;
import com.fruitstore.dto.request.product.ProductFilterRequest;
import com.fruitstore.dto.request.product.UpdateProductRequest;
import com.fruitstore.dto.response.product.ProductListResponse;
import com.fruitstore.dto.response.product.ProductResponse;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.repository.CategoryRepository;
import com.fruitstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for product management
 * Handles CRUD operations, search, filtering, and stock management
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all products with pagination
     * 
     * @param pageable pagination information
     * @return page of products
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsActiveTrue(pageable);
        return products.map(this::mapToProductResponse);
    }

    /**
     * Get product by ID
     * 
     * @param id the product ID
     * @return product response
     * @throws IllegalArgumentException if product not found
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        return mapToProductResponse(product);
    }

    /**
     * Get active product by ID
     * 
     * @param id the product ID
     * @return product response
     * @throws IllegalArgumentException if product not found or inactive
     */
    @Transactional(readOnly = true)
    public ProductResponse getActiveProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if (!product.getIsActive()) {
            throw new IllegalArgumentException("Product is not active with id: " + id);
        }

        return mapToProductResponse(product);
    }

    /**
     * Get products by category with pagination
     * 
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of products in the category
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        // Verify category exists and is active
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        if (!category.getIsActive()) {
            throw new IllegalArgumentException("Category is not active with id: " + categoryId);
        }

        Page<Product> products = productRepository.findByCategory_CategoryIdAndIsActiveTrue(categoryId, pageable);
        return products.map(this::mapToProductResponse);
    }

    /**
     * Search products with filters
     * 
     * @param filterRequest filter criteria
     * @return filtered product list response
     */
    @Transactional(readOnly = true)
    public ProductListResponse searchProducts(ProductFilterRequest filterRequest) {
        // Create pageable from filter request
        Pageable pageable = createPageable(filterRequest);

        // Search products with filters
        Page<Product> productPage = productRepository.findProductsWithFilters(
                filterRequest.getCategoryId(),
                filterRequest.getMinPrice(),
                filterRequest.getMaxPrice(),
                filterRequest.getKeyword(),
                pageable
        );

        // Convert to response DTOs
        List<ProductResponse> products = productPage.getContent().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

        return new ProductListResponse(
                products,
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize()
        );
    }

    /**
     * Create a new product
     * 
     * @param request product creation request
     * @return created product response
     * @throws IllegalArgumentException if product name already exists or category not found
     */
    public ProductResponse createProduct(CreateProductRequest request) {
        // Check if product name already exists
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Product name already exists: " + request.getName());
        }

        // Find and validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));

        if (!category.getIsActive()) {
            throw new IllegalArgumentException("Category is not active with id: " + request.getCategoryId());
        }

        // Create new product
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setIsActive(request.getIsActive());

        // Save product
        Product savedProduct = productRepository.save(product);

        return mapToProductResponse(savedProduct);
    }

    /**
     * Update an existing product
     * 
     * @param id the product ID
     * @param request product update request
     * @return updated product response
     * @throws IllegalArgumentException if product not found, name already exists, or category not found
     */
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Check if new name conflicts with existing product (excluding current one)
        if (!product.getName().equalsIgnoreCase(request.getName()) && 
            productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Product name already exists: " + request.getName());
        }

        // Update product fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));

            if (!category.getIsActive()) {
                throw new IllegalArgumentException("Category is not active with id: " + request.getCategoryId());
            }

            product.setCategory(category);
        }

        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        // Save updated product
        Product updatedProduct = productRepository.save(product);

        return mapToProductResponse(updatedProduct);
    }

    /**
     * Delete a product (soft delete by setting isActive to false)
     * 
     * @param id the product ID
     * @throws IllegalArgumentException if product not found
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Soft delete by setting isActive to false
        product.setIsActive(false);
        productRepository.save(product);
    }

    /**
     * Permanently delete a product
     * 
     * @param id the product ID
     * @throws IllegalArgumentException if product not found
     */
    public void permanentlyDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Permanently delete product
        productRepository.delete(product);
    }

    /**
     * Check stock availability for a product
     * 
     * @param productId the product ID
     * @param quantity the requested quantity
     * @return true if sufficient stock available, false otherwise
     */
    @Transactional(readOnly = true)
    public Boolean checkStockAvailability(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (!product.getIsActive()) {
            throw new IllegalArgumentException("Product is not active with id: " + productId);
        }

        return product.hasSufficientStock(quantity);
    }

    /**
     * Reduce stock quantity for a product
     * 
     * @param productId the product ID
     * @param quantity the quantity to reduce
     * @throws IllegalArgumentException if product not found or insufficient stock
     */
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (!product.getIsActive()) {
            throw new IllegalArgumentException("Product is not active with id: " + productId);
        }

        product.reduceStock(quantity);
        productRepository.save(product);
    }

    /**
     * Add stock quantity for a product
     * 
     * @param productId the product ID
     * @param quantity the quantity to add
     * @throws IllegalArgumentException if product not found
     */
    public void addStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        product.addStock(quantity);
        productRepository.save(product);
    }

    /**
     * Get low stock products
     * 
     * @param threshold the stock threshold
     * @return list of products with stock below threshold
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(Integer threshold) {
        List<Product> products = productRepository.findLowStockProducts(threshold);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get top products by stock quantity
     * 
     * @param limit the maximum number of products to return
     * @return list of products with highest stock
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getTopProductsByStock(Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> products = productRepository.findTopProductsByStockQuantity(pageable);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search products by name
     * 
     * @param name the name pattern to search for
     * @param pageable pagination information
     * @return page of matching products
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
        return products.map(this::mapToProductResponse);
    }

    /**
     * Get products by price range
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products within price range
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get products by multiple categories
     * 
     * @param categoryIds list of category IDs
     * @param pageable pagination information
     * @return page of products in any of the specified categories
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategories(List<Long> categoryIds, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryIdInAndIsActiveTrue(categoryIds, pageable);
        return products.map(this::mapToProductResponse);
    }

    /**
     * Create Pageable from ProductFilterRequest
     * 
     * @param filterRequest the filter request
     * @return pageable object
     */
    private Pageable createPageable(ProductFilterRequest filterRequest) {
        Sort sort = createSort(filterRequest.getSortBy(), filterRequest.getSortDirection());
        return PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);
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

    /**
     * Map Product entity to ProductResponse DTO
     * 
     * @param product the product entity
     * @return product response
     */
    private ProductResponse mapToProductResponse(Product product) {
        CategoryResponse categoryResponse = null;
        if (product.getCategory() != null) {
            categoryResponse = new CategoryResponse(
                    product.getCategory().getCategoryId(),
                    product.getCategory().getName(),
                    product.getCategory().getDescription(),
                    product.getCategory().getImageUrl(),
                    product.getCategory().getIsActive(),
                    product.getCategory().getCreatedAt()
            );
        }

        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                categoryResponse,
                product.getIsActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

}
