package com.fruitstore.service;

import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountUsage;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.user.User;
import com.fruitstore.dto.request.discount.CreateDiscountRequest;
import com.fruitstore.dto.request.discount.UpdateDiscountRequest;
import com.fruitstore.dto.response.discount.DiscountResponse;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.dto.response.discount.DiscountUsageResponse;
import com.fruitstore.repository.DiscountRepository;
import com.fruitstore.repository.DiscountUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for discount management
 * Handles CRUD operations, validation, and discount application
 */
@Service
@Transactional
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountUsageRepository discountUsageRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, DiscountUsageRepository discountUsageRepository) {
        this.discountRepository = discountRepository;
        this.discountUsageRepository = discountUsageRepository;
    }

    /**
     * Get all discounts (admin)
     * 
     * @param pageable pagination information
     * @return page of discounts
     */
    @Transactional(readOnly = true)
    public Page<DiscountResponse> getAllDiscounts(Pageable pageable) {
        Page<Discount> discounts = discountRepository.findAll(pageable);
        return discounts.map(this::mapToDiscountResponse);
    }

    /**
     * Get active discounts (public)
     * 
     * @param pageable pagination information
     * @return page of active discounts
     */
    @Transactional(readOnly = true)
    public Page<DiscountResponse> getActiveDiscounts(Pageable pageable) {
        Page<Discount> discounts = discountRepository.findByIsActiveTrue(pageable);
        return discounts.map(this::mapToDiscountResponse);
    }

    /**
     * Get discount by ID
     * 
     * @param id the discount ID
     * @return discount response
     * @throws IllegalArgumentException if discount not found
     */
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + id));

        return mapToDiscountResponse(discount);
    }

    /**
     * Create discount (admin)
     * 
     * @param request the create discount request
     * @return created discount response
     * @throws IllegalArgumentException if discount code already exists
     */
    public DiscountResponse createDiscount(CreateDiscountRequest request) {
        // Check if discount code already exists
        if (discountRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new IllegalArgumentException("Discount code already exists: " + request.getCode());
        }

        // Create new discount
        Discount discount = new Discount();
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setDiscountType(request.getDiscountType());
        discount.setDiscountValue(request.getDiscountValue());
        discount.setMinOrderAmount(request.getMinOrderAmount());
        discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        discount.setUsageLimit(request.getUsageLimit());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setIsActive(request.getIsActive());
        discount.setUsedCount(0);

        Discount savedDiscount = discountRepository.save(discount);
        return mapToDiscountResponse(savedDiscount);
    }

    /**
     * Update discount (admin)
     * 
     * @param id the discount ID
     * @param request the update discount request
     * @return updated discount response
     * @throws IllegalArgumentException if discount not found
     */
    public DiscountResponse updateDiscount(Long id, UpdateDiscountRequest request) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + id));

        // Update fields if provided
        if (request.getDescription() != null) {
            discount.setDescription(request.getDescription());
        }
        if (request.getDiscountType() != null) {
            discount.setDiscountType(request.getDiscountType());
        }
        if (request.getDiscountValue() != null) {
            discount.setDiscountValue(request.getDiscountValue());
        }
        if (request.getMinOrderAmount() != null) {
            discount.setMinOrderAmount(request.getMinOrderAmount());
        }
        if (request.getMaxDiscountAmount() != null) {
            discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        }
        if (request.getUsageLimit() != null) {
            discount.setUsageLimit(request.getUsageLimit());
        }
        if (request.getStartDate() != null) {
            discount.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            discount.setEndDate(request.getEndDate());
        }
        if (request.getIsActive() != null) {
            discount.setIsActive(request.getIsActive());
        }

        Discount updatedDiscount = discountRepository.save(discount);
        return mapToDiscountResponse(updatedDiscount);
    }

    /**
     * Delete discount (admin)
     * 
     * @param id the discount ID
     * @throws IllegalArgumentException if discount not found
     */
    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + id));

        discountRepository.delete(discount);
    }

    /**
     * Validate discount code for order amount
     * 
     * @param code the discount code
     * @param orderAmount the order amount
     * @return discount validation response
     */
    @Transactional(readOnly = true)
    public DiscountValidationResponse validateDiscount(String code, BigDecimal orderAmount) {
        // Find discount by code
        Optional<Discount> discountOpt = discountRepository.findByCodeIgnoreCase(code);
        if (discountOpt.isEmpty()) {
            return DiscountValidationResponse.notFound(code);
        }

        Discount discount = discountOpt.get();

        // Check if discount is active
        if (!discount.getIsActive()) {
            return DiscountValidationResponse.inactive(code);
        }

        // Check if discount is currently active (within date range)
        if (!discount.isCurrentlyActive()) {
            LocalDateTime now = LocalDateTime.now();
            if (discount.getStartDate() != null && now.isBefore(discount.getStartDate())) {
                return DiscountValidationResponse.notStarted(code);
            }
            if (discount.getEndDate() != null && now.isAfter(discount.getEndDate())) {
                return DiscountValidationResponse.expired(code);
            }
        }

        // Check usage limit
        if (!discount.canBeUsed()) {
            return DiscountValidationResponse.usageLimitReached(code);
        }

        // Check minimum order amount
        if (!discount.isValidForOrderAmount(orderAmount)) {
            return DiscountValidationResponse.insufficientOrderAmount(code, discount.getMinOrderAmount());
        }

        // Calculate discount amount
        BigDecimal calculatedDiscountAmount = discount.calculateDiscountAmount(orderAmount);

        // Prepare response with discount details
        Integer remainingUsage = discount.getUsageLimit() != null 
            ? discount.getUsageLimit() - discount.getUsedCount() 
            : null;

        return DiscountValidationResponse.valid(
            code,
            discount.getDiscountType(),
            discount.getDiscountValue(),
            calculatedDiscountAmount,
            discount.getMinOrderAmount(),
            discount.getMaxDiscountAmount(),
            discount.getUsageLimit(),
            discount.getUsedCount(),
            remainingUsage,
            discount.getDescription()
        );
    }

    /**
     * Apply discount to order amount
     * 
     * @param code the discount code
     * @param orderAmount the order amount
     * @return calculated discount amount
     * @throws IllegalArgumentException if discount is not valid
     */
    @Transactional(readOnly = true)
    public BigDecimal applyDiscount(String code, BigDecimal orderAmount) {
        DiscountValidationResponse validation = validateDiscount(code, orderAmount);
        
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Invalid discount: " + validation.getMessage());
        }

        return validation.getCalculatedDiscountAmount();
    }

    /**
     * Record discount usage
     * 
     * @param discountId the discount ID
     * @param userId the user ID
     * @param orderId the order ID (optional)
     * @param discountAmount the discount amount applied
     */
    @Transactional
    public void recordDiscountUsage(Long discountId, Long userId, Long orderId, BigDecimal discountAmount) {
        // Find discount
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + discountId));

        // Create discount usage record
        DiscountUsage usage = new DiscountUsage();
        usage.setDiscount(discount);
        usage.setUser(new User()); // Set user ID
        usage.getUser().setUserId(userId);
        
        if (orderId != null) {
            usage.setOrder(new Order()); // Set order ID
            usage.getOrder().setOrderId(orderId);
        }
        
        // Ensure discount amount is properly scaled to 2 decimal places
        usage.setDiscountAmount(discountAmount.setScale(2, java.math.RoundingMode.HALF_UP));

        discountUsageRepository.save(usage);

        // Increment used count
        discount.incrementUsedCount();
        discountRepository.save(discount);
    }

    /**
     * Record discount usage with user and order objects
     * 
     * @param discount the discount
     * @param user the user
     * @param order the order (optional)
     * @param discountAmount the discount amount applied
     */
    @Transactional
    public void recordDiscountUsage(Discount discount, User user, Order order, BigDecimal discountAmount) {
        // Ensure discount amount is properly scaled to 2 decimal places
        BigDecimal scaledDiscountAmount = discountAmount.setScale(2, java.math.RoundingMode.HALF_UP);
        
        // Create discount usage record
        DiscountUsage usage = new DiscountUsage(discount, user, order, scaledDiscountAmount);
        discountUsageRepository.save(usage);

        // Increment used count
        discount.incrementUsedCount();
        discountRepository.save(discount);
    }

    /**
     * Get discount usage statistics
     * 
     * @param discountId the discount ID
     * @return usage statistics
     */
    @Transactional(readOnly = true)
    public DiscountUsageStats getDiscountUsageStats(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + discountId));

        long totalUsages = discountUsageRepository.countByDiscount_DiscountId(discountId);
        BigDecimal totalDiscountAmount = discountUsageRepository.findTotalDiscountAmountByDiscount(discountId);

        return new DiscountUsageStats(
            discountId,
            discount.getCode(),
            totalUsages,
            totalDiscountAmount,
            discount.getUsedCount(),
            discount.getUsageLimit()
        );
    }

    /**
     * Get recent discount usages
     * 
     * @param discountId the discount ID
     * @param pageable pagination information
     * @return page of discount usages
     */
    @Transactional(readOnly = true)
    public Page<DiscountUsageResponse> getDiscountUsages(Long discountId, Pageable pageable) {
        Page<DiscountUsage> usages = discountUsageRepository.findByDiscount_DiscountId(discountId, pageable);
        return usages.map(this::mapToDiscountUsageResponse);
    }

    /**
     * Get user's discount usages
     * 
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of user's discount usages
     */
    @Transactional(readOnly = true)
    public Page<DiscountUsageResponse> getUserDiscountUsages(Long userId, Pageable pageable) {
        Page<DiscountUsage> usages = discountUsageRepository.findByUser_UserId(userId, pageable);
        return usages.map(this::mapToDiscountUsageResponse);
    }

    /**
     * Check if user has used a discount
     * 
     * @param userId the user ID
     * @param discountId the discount ID
     * @return true if user has used the discount
     */
    @Transactional(readOnly = true)
    public boolean hasUserUsedDiscount(Long userId, Long discountId) {
        return discountUsageRepository.existsByUser_UserIdAndDiscount_DiscountId(userId, discountId);
    }

    /**
     * Get available discounts for user
     * 
     * @param userId the user ID
     * @param orderAmount the order amount
     * @return list of available discounts
     */
    @Transactional(readOnly = true)
    public List<DiscountResponse> getAvailableDiscountsForUser(Long userId, BigDecimal orderAmount) {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> availableDiscounts = discountRepository.findValidDiscountsForOrderAmount(orderAmount, now);
        
        // Filter out discounts that user has already used (if there's a per-user limit)
        // This is a simplified implementation - in real scenario, you might want to check
        // if there's a per-user usage limit for each discount
        
        return availableDiscounts.stream()
                .map(this::mapToDiscountResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get discount by code
     * 
     * @param code the discount code
     * @return discount response
     * @throws IllegalArgumentException if discount not found
     */
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountByCode(String code) {
        Discount discount = discountRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with code: " + code));

        return mapToDiscountResponse(discount);
    }

    /**
     * Map Discount entity to DiscountResponse DTO
     */
    private DiscountResponse mapToDiscountResponse(Discount discount) {
        return new DiscountResponse(
            discount.getDiscountId(),
            discount.getCode(),
            discount.getDescription(),
            discount.getDiscountType(),
            discount.getDiscountValue(),
            discount.getMinOrderAmount(),
            discount.getMaxDiscountAmount(),
            discount.getUsageLimit(),
            discount.getUsedCount(),
            discount.getStartDate(),
            discount.getEndDate(),
            discount.getIsActive(),
            discount.getCreatedAt(),
            discount.getUpdatedAt()
        );
    }

    /**
     * Map DiscountUsage entity to DiscountUsageResponse DTO
     */
    private DiscountUsageResponse mapToDiscountUsageResponse(DiscountUsage usage) {
        return new DiscountUsageResponse(
            usage.getUsageId(),
            usage.getDiscount() != null ? usage.getDiscount().getDiscountId() : null,
            usage.getDiscount() != null ? usage.getDiscount().getCode() : null,
            usage.getUser() != null ? usage.getUser().getUserId() : null,
            usage.getUser() != null ? usage.getUser().getUsername() : null,
            usage.getOrder() != null ? usage.getOrder().getOrderId() : null,
            usage.getOrder() != null ? usage.getOrder().getOrderNumber() : null,
            usage.getDiscountAmount(),
            usage.getUsedAt()
        );
    }

    /**
     * Inner class for discount usage statistics
     */
    public static class DiscountUsageStats {
        private final Long discountId;
        private final String code;
        private final long totalUsages;
        private final BigDecimal totalDiscountAmount;
        private final Integer usedCount;
        private final Integer usageLimit;

        public DiscountUsageStats(Long discountId, String code, long totalUsages, 
                                 BigDecimal totalDiscountAmount, Integer usedCount, Integer usageLimit) {
            this.discountId = discountId;
            this.code = code;
            this.totalUsages = totalUsages;
            this.totalDiscountAmount = totalDiscountAmount;
            this.usedCount = usedCount;
            this.usageLimit = usageLimit;
        }

        // Getters
        public Long getDiscountId() { return discountId; }
        public String getCode() { return code; }
        public long getTotalUsages() { return totalUsages; }
        public BigDecimal getTotalDiscountAmount() { return totalDiscountAmount; }
        public Integer getUsedCount() { return usedCount; }
        public Integer getUsageLimit() { return usageLimit; }
        
        public Integer getRemainingUsage() {
            return usageLimit != null ? usageLimit - usedCount : null;
        }
    }
}
