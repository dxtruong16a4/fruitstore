package com.fruitstore.controller;

import com.fruitstore.dto.request.discount.CreateDiscountRequest;
import com.fruitstore.dto.request.discount.UpdateDiscountRequest;
import com.fruitstore.dto.request.discount.ValidateDiscountRequest;
import com.fruitstore.dto.response.discount.DiscountResponse;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.dto.response.discount.DiscountUsageResponse;
import com.fruitstore.dto.response.common.ApiResponse;
import com.fruitstore.service.DiscountService;
import com.fruitstore.service.DiscountService.DiscountUsageStats;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for discount management
 * Handles CRUD operations, validation, and discount application
 */
@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * Get active discounts (public endpoint)
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (code, discountValue, createdAt, endDate)
     * @param sortDirection sort direction (asc, desc)
     * @return page of active discounts
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<DiscountResponse>>> getActiveDiscounts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<DiscountResponse> discounts = discountService.getActiveDiscounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(discounts));
    }

    /**
     * Validate discount code
     * Public endpoint - no authentication required
     * 
     * @param request the validate discount request
     * @return discount validation response
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<DiscountValidationResponse>> validateDiscount(
            @Valid @RequestBody ValidateDiscountRequest request) {
        
        DiscountValidationResponse response = discountService.validateDiscount(
            request.getCode(), request.getOrderAmount());
        
        if (response.isValid()) {
            return ResponseEntity.ok(ApiResponse.success(response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
        }
    }

    /**
     * Get discount by code
     * Public endpoint - no authentication required
     * 
     * @param code the discount code
     * @return discount response
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getDiscountByCode(@PathVariable("code") String code) {
        DiscountResponse discount = discountService.getDiscountByCode(code);
        return ResponseEntity.ok(ApiResponse.success(discount));
    }

    /**
     * Get available discounts for user
     * Requires authentication
     * 
     * @param userId the user ID
     * @param orderAmount the order amount
     * @return list of available discounts
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAvailableDiscountsForUser(
            @RequestParam("userId") Long userId,
            @RequestParam("orderAmount") BigDecimal orderAmount) {
        
        List<DiscountResponse> discounts = discountService.getAvailableDiscountsForUser(userId, orderAmount);
        return ResponseEntity.ok(ApiResponse.success(discounts));
    }

    // ==================== ADMIN ENDPOINTS ====================

    /**
     * Get all discounts (admin)
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (code, discountValue, createdAt, endDate)
     * @param sortDirection sort direction (asc, desc)
     * @return page of all discounts
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<DiscountResponse>>> getAllDiscounts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<DiscountResponse> discounts = discountService.getAllDiscounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(discounts));
    }

    /**
     * Get discount by ID (admin)
     * 
     * @param id the discount ID
     * @return discount response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountResponse>> getDiscountById(@PathVariable("id") Long id) {
        DiscountResponse discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(ApiResponse.success(discount));
    }

    /**
     * Create discount (admin)
     * 
     * @param request the create discount request
     * @return created discount response
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountResponse>> createDiscount(
            @Valid @RequestBody CreateDiscountRequest request) {
        
        DiscountResponse discount = discountService.createDiscount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(discount));
    }

    /**
     * Update discount (admin)
     * 
     * @param id the discount ID
     * @param request the update discount request
     * @return updated discount response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountResponse>> updateDiscount(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateDiscountRequest request) {
        
        DiscountResponse discount = discountService.updateDiscount(id, request);
        return ResponseEntity.ok(ApiResponse.success(discount));
    }

    /**
     * Delete discount (admin)
     * 
     * @param id the discount ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteDiscount(@PathVariable("id") Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(ApiResponse.success("Discount deleted successfully"));
    }

    /**
     * Get discount usage statistics (admin)
     * 
     * @param id the discount ID
     * @return discount usage statistics
     */
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountUsageStats>> getDiscountUsageStats(@PathVariable("id") Long id) {
        DiscountUsageStats stats = discountService.getDiscountUsageStats(id);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * Get discount usages with pagination (admin)
     * 
     * @param id the discount ID
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (usedAt, discountAmount, user.userId)
     * @param sortDirection sort direction (asc, desc)
     * @return page of discount usages
     */
    @GetMapping("/{id}/usages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<DiscountUsageResponse>>> getDiscountUsages(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "usedAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<DiscountUsageResponse> usages = discountService.getDiscountUsages(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(usages));
    }

    /**
     * Get user's discount usages (admin)
     * 
     * @param userId the user ID
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field (usedAt, discountAmount, discount.code)
     * @param sortDirection sort direction (asc, desc)
     * @return page of user's discount usages
     */
    @GetMapping("/user/{userId}/usages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<DiscountUsageResponse>>> getUserDiscountUsages(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sortBy", defaultValue = "usedAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<DiscountUsageResponse> usages = discountService.getUserDiscountUsages(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(usages));
    }

    /**
     * Check if user has used a discount (admin)
     * 
     * @param userId the user ID
     * @param discountId the discount ID
     * @return boolean response
     */
    @GetMapping("/user/{userId}/discount/{discountId}/used")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> hasUserUsedDiscount(
            @PathVariable("userId") Long userId,
            @PathVariable("discountId") Long discountId) {
        
        boolean hasUsed = discountService.hasUserUsedDiscount(userId, discountId);
        return ResponseEntity.ok(ApiResponse.success(hasUsed));
    }

    /**
     * Apply discount to order amount (internal use)
     * 
     * @param code the discount code
     * @param orderAmount the order amount
     * @return calculated discount amount
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<BigDecimal>> applyDiscount(
            @RequestParam("code") String code,
            @RequestParam("orderAmount") BigDecimal orderAmount) {
        
        BigDecimal discountAmount = discountService.applyDiscount(code, orderAmount);
        return ResponseEntity.ok(ApiResponse.success(discountAmount));
    }

    /**
     * Record discount usage (internal use)
     * 
     * @param discountId the discount ID
     * @param userId the user ID
     * @param orderId the order ID (optional)
     * @param discountAmount the discount amount applied
     * @return success response
     */
    @PostMapping("/usage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> recordDiscountUsage(
            @RequestParam("discountId") Long discountId,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam("discountAmount") BigDecimal discountAmount) {
        
        discountService.recordDiscountUsage(discountId, userId, orderId, discountAmount);
        return ResponseEntity.ok(ApiResponse.success("Discount usage recorded successfully"));
    }

    /**
     * Helper method to create Pageable object
     */
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        // Validate and map sort field names to entity field names
        String validSortBy = validateAndMapSortField(sortBy);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), validSortBy);
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * Validate and map sort field names to entity field names
     */
    private String validateAndMapSortField(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "createdAt";
        }
        
        // Map common sort field names to actual entity field names
        switch (sortBy.toLowerCase()) {
            case "id":
                return "discountId";
            case "code":
                return "code";
            case "description":
                return "description";
            case "discounttype":
            case "discount_type":
                return "discountType";
            case "discountvalue":
            case "discount_value":
                return "discountValue";
            case "minorderamount":
            case "min_order_amount":
                return "minOrderAmount";
            case "maxdiscountamount":
            case "max_discount_amount":
                return "maxDiscountAmount";
            case "usagelimit":
            case "usage_limit":
                return "usageLimit";
            case "usedcount":
            case "used_count":
                return "usedCount";
            case "startdate":
            case "start_date":
                return "startDate";
            case "enddate":
            case "end_date":
                return "endDate";
            case "isactive":
            case "is_active":
                return "isActive";
            case "createdat":
            case "created_at":
                return "createdAt";
            case "updatedat":
            case "updated_at":
                return "updatedAt";
            default:
                // If the field name is not recognized, default to createdAt
                return "createdAt";
        }
    }
}
