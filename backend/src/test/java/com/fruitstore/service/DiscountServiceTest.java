package com.fruitstore.service;

import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountType;
import com.fruitstore.domain.discount.DiscountUsage;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.user.User;
import com.fruitstore.dto.request.discount.CreateDiscountRequest;
import com.fruitstore.dto.request.discount.UpdateDiscountRequest;
import com.fruitstore.dto.response.discount.DiscountResponse;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.repository.DiscountRepository;
import com.fruitstore.repository.DiscountUsageRepository;
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
 * Test class for DiscountService
 */
@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountUsageRepository discountUsageRepository;

    @InjectMocks
    private DiscountService discountService;

    private Discount activeDiscount;
    private Discount inactiveDiscount;
    private Discount expiredDiscount;
    private CreateDiscountRequest createRequest;
    private UpdateDiscountRequest updateRequest;
    private User testUser;
    private Order testOrder;
    private DiscountUsage testUsage;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        // Create active discount
        activeDiscount = new Discount();
        activeDiscount.setDiscountId(1L);
        activeDiscount.setCode("WELCOME10");
        activeDiscount.setDescription("Welcome discount 10%");
        activeDiscount.setDiscountType(DiscountType.PERCENTAGE);
        activeDiscount.setDiscountValue(new BigDecimal("10.00"));
        activeDiscount.setMinOrderAmount(new BigDecimal("100000.00"));
        activeDiscount.setMaxDiscountAmount(new BigDecimal("50000.00"));
        activeDiscount.setUsageLimit(100);
        activeDiscount.setUsedCount(10);
        activeDiscount.setStartDate(now.minusDays(1));
        activeDiscount.setEndDate(now.plusDays(30));
        activeDiscount.setIsActive(true);
        activeDiscount.setCreatedAt(now);
        activeDiscount.setUpdatedAt(now);

        // Create inactive discount
        inactiveDiscount = new Discount();
        inactiveDiscount.setDiscountId(2L);
        inactiveDiscount.setCode("INACTIVE20");
        inactiveDiscount.setDescription("Inactive discount");
        inactiveDiscount.setDiscountType(DiscountType.PERCENTAGE);
        inactiveDiscount.setDiscountValue(new BigDecimal("20.00"));
        inactiveDiscount.setIsActive(false);

        // Create expired discount
        expiredDiscount = new Discount();
        expiredDiscount.setDiscountId(3L);
        expiredDiscount.setCode("EXPIRED15");
        expiredDiscount.setDescription("Expired discount");
        expiredDiscount.setDiscountType(DiscountType.PERCENTAGE);
        expiredDiscount.setDiscountValue(new BigDecimal("15.00"));
        expiredDiscount.setStartDate(now.minusDays(30));
        expiredDiscount.setEndDate(now.minusDays(1));
        expiredDiscount.setIsActive(true);

        // Create test user
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Create test order
        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setOrderNumber("ORD001");
        testOrder.setUser(testUser);
        testOrder.setTotalAmount(new BigDecimal("150000.00"));

        // Create test usage
        testUsage = new DiscountUsage();
        testUsage.setUsageId(1L);
        testUsage.setDiscount(activeDiscount);
        testUsage.setUser(testUser);
        testUsage.setOrder(testOrder);
        testUsage.setDiscountAmount(new BigDecimal("15000.00"));
        testUsage.setUsedAt(now);

        // Create request DTOs
        createRequest = new CreateDiscountRequest();
        createRequest.setCode("NEW10");
        createRequest.setDescription("New discount 10%");
        createRequest.setDiscountType(DiscountType.PERCENTAGE);
        createRequest.setDiscountValue(new BigDecimal("10.00"));
        createRequest.setMinOrderAmount(new BigDecimal("50000.00"));
        createRequest.setUsageLimit(50);

        updateRequest = new UpdateDiscountRequest();
        updateRequest.setDescription("Updated description");
        updateRequest.setDiscountValue(new BigDecimal("15.00"));
        updateRequest.setUsageLimit(75);
    }

    @Test
    public void testGetAllDiscounts() {
        // Given
        List<Discount> discounts = Arrays.asList(activeDiscount, inactiveDiscount, expiredDiscount);
        Page<Discount> discountPage = new PageImpl<>(discounts);
        Pageable pageable = PageRequest.of(0, 10);

        when(discountRepository.findAll(pageable)).thenReturn(discountPage);

        // When
        Page<DiscountResponse> result = discountService.getAllDiscounts(pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("WELCOME10");
        assertThat(result.getContent().get(1).getCode()).isEqualTo("INACTIVE20");
        assertThat(result.getContent().get(2).getCode()).isEqualTo("EXPIRED15");
    }

    @Test
    public void testGetActiveDiscounts() {
        // Given
        List<Discount> activeDiscounts = Arrays.asList(activeDiscount, expiredDiscount);
        Page<Discount> discountPage = new PageImpl<>(activeDiscounts);
        Pageable pageable = PageRequest.of(0, 10);

        when(discountRepository.findByIsActiveTrue(pageable)).thenReturn(discountPage);

        // When
        Page<DiscountResponse> result = discountService.getActiveDiscounts(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("WELCOME10");
        assertThat(result.getContent().get(1).getCode()).isEqualTo("EXPIRED15");
    }

    @Test
    public void testGetDiscountById() {
        // Given
        when(discountRepository.findById(1L)).thenReturn(Optional.of(activeDiscount));

        // When
        DiscountResponse result = discountService.getDiscountById(1L);

        // Then
        assertThat(result.getDiscountId()).isEqualTo(1L);
        assertThat(result.getCode()).isEqualTo("WELCOME10");
        assertThat(result.getDescription()).isEqualTo("Welcome discount 10%");
        assertThat(result.getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
    }

    @Test
    public void testGetDiscountById_NotFound() {
        // Given
        when(discountRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> discountService.getDiscountById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount not found with id: 999");
    }

    @Test
    public void testCreateDiscount() {
        // Given
        when(discountRepository.existsByCodeIgnoreCase("NEW10")).thenReturn(false);
        when(discountRepository.save(any(Discount.class))).thenReturn(activeDiscount);

        // When
        DiscountResponse result = discountService.createDiscount(createRequest);

        // Then
        verify(discountRepository).save(any(Discount.class));
        assertThat(result.getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testCreateDiscount_CodeAlreadyExists() {
        // Given
        when(discountRepository.existsByCodeIgnoreCase("NEW10")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> discountService.createDiscount(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount code already exists: NEW10");
    }

    @Test
    public void testUpdateDiscount() {
        // Given
        when(discountRepository.findById(1L)).thenReturn(Optional.of(activeDiscount));
        when(discountRepository.save(any(Discount.class))).thenReturn(activeDiscount);

        // When
        discountService.updateDiscount(1L, updateRequest);

        // Then
        verify(discountRepository).save(activeDiscount);
        assertThat(activeDiscount.getDescription()).isEqualTo("Updated description");
        assertThat(activeDiscount.getDiscountValue()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertThat(activeDiscount.getUsageLimit()).isEqualTo(75);
    }

    @Test
    public void testUpdateDiscount_NotFound() {
        // Given
        when(discountRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> discountService.updateDiscount(999L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount not found with id: 999");
    }

    @Test
    public void testDeleteDiscount() {
        // Given
        when(discountRepository.findById(1L)).thenReturn(Optional.of(activeDiscount));

        // When
        discountService.deleteDiscount(1L);

        // Then
        verify(discountRepository).delete(activeDiscount);
    }

    @Test
    public void testDeleteDiscount_NotFound() {
        // Given
        when(discountRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> discountService.deleteDiscount(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount not found with id: 999");
    }

    @Test
    public void testValidateDiscount_Valid() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("WELCOME10")).thenReturn(Optional.of(activeDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("WELCOME10", new BigDecimal("150000.00"));

        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.getCode()).isEqualTo("WELCOME10");
        assertThat(result.getMessage()).isEqualTo("Discount is valid");
        assertThat(result.getCalculatedDiscountAmount()).isEqualByComparingTo(new BigDecimal("15000.00"));
    }

    @Test
    public void testValidateDiscount_NotFound() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("NOTFOUND")).thenReturn(Optional.empty());

        // When
        DiscountValidationResponse result = discountService.validateDiscount("NOTFOUND", new BigDecimal("150000.00"));

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Discount code not found");
    }

    @Test
    public void testValidateDiscount_Inactive() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("INACTIVE20")).thenReturn(Optional.of(inactiveDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("INACTIVE20", new BigDecimal("150000.00"));

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Discount is inactive");
    }

    @Test
    public void testValidateDiscount_Expired() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("EXPIRED15")).thenReturn(Optional.of(expiredDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("EXPIRED15", new BigDecimal("150000.00"));

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Discount has expired");
    }

    @Test
    public void testValidateDiscount_InsufficientOrderAmount() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("WELCOME10")).thenReturn(Optional.of(activeDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("WELCOME10", new BigDecimal("50000.00"));

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("Order amount must be at least");
    }

    @Test
    public void testValidateDiscount_UsageLimitReached() {
        // Given
        Discount limitReachedDiscount = new Discount();
        limitReachedDiscount.setCode("LIMIT100");
        limitReachedDiscount.setDiscountType(DiscountType.PERCENTAGE);
        limitReachedDiscount.setDiscountValue(new BigDecimal("10.00"));
        limitReachedDiscount.setUsageLimit(10);
        limitReachedDiscount.setUsedCount(10);
        limitReachedDiscount.setIsActive(true);
        limitReachedDiscount.setMinOrderAmount(BigDecimal.ZERO);

        when(discountRepository.findByCodeIgnoreCase("LIMIT100")).thenReturn(Optional.of(limitReachedDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("LIMIT100", new BigDecimal("150000.00"));

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Discount usage limit has been reached");
    }

    @Test
    public void testApplyDiscount_Valid() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("WELCOME10")).thenReturn(Optional.of(activeDiscount));

        // When
        BigDecimal result = discountService.applyDiscount("WELCOME10", new BigDecimal("150000.00"));

        // Then
        assertThat(result).isEqualByComparingTo(new BigDecimal("15000.00"));
    }

    @Test
    public void testApplyDiscount_Invalid() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("NOTFOUND")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> discountService.applyDiscount("NOTFOUND", new BigDecimal("150000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid discount: Discount code not found");
    }

    @Test
    public void testRecordDiscountUsage() {
        // Given
        when(discountRepository.findById(1L)).thenReturn(Optional.of(activeDiscount));
        when(discountUsageRepository.save(any(DiscountUsage.class))).thenReturn(testUsage);
        when(discountRepository.save(any(Discount.class))).thenReturn(activeDiscount);

        // When
        discountService.recordDiscountUsage(1L, 1L, 1L, new BigDecimal("15000.00"));

        // Then
        verify(discountUsageRepository).save(any(DiscountUsage.class));
        verify(discountRepository).save(activeDiscount);
        assertThat(activeDiscount.getUsedCount()).isEqualTo(11);
    }

    @Test
    public void testRecordDiscountUsageWithObjects() {
        // Given
        when(discountUsageRepository.save(any(DiscountUsage.class))).thenReturn(testUsage);
        when(discountRepository.save(any(Discount.class))).thenReturn(activeDiscount);

        // When
        discountService.recordDiscountUsage(activeDiscount, testUser, testOrder, new BigDecimal("15000.00"));

        // Then
        verify(discountUsageRepository).save(any(DiscountUsage.class));
        verify(discountRepository).save(activeDiscount);
        assertThat(activeDiscount.getUsedCount()).isEqualTo(11);
    }

    @Test
    public void testGetDiscountUsageStats() {
        // Given
        when(discountRepository.findById(1L)).thenReturn(Optional.of(activeDiscount));
        when(discountUsageRepository.countByDiscount_DiscountId(1L)).thenReturn(10L);
        when(discountUsageRepository.findTotalDiscountAmountByDiscount(1L)).thenReturn(new BigDecimal("150000.00"));

        // When
        DiscountService.DiscountUsageStats result = discountService.getDiscountUsageStats(1L);

        // Then
        assertThat(result.getDiscountId()).isEqualTo(1L);
        assertThat(result.getCode()).isEqualTo("WELCOME10");
        assertThat(result.getTotalUsages()).isEqualTo(10L);
        assertThat(result.getTotalDiscountAmount()).isEqualByComparingTo(new BigDecimal("150000.00"));
        assertThat(result.getUsedCount()).isEqualTo(10);
        assertThat(result.getUsageLimit()).isEqualTo(100);
        assertThat(result.getRemainingUsage()).isEqualTo(90);
    }

    @Test
    public void testGetDiscountUsages() {
        // Given
        List<DiscountUsage> usages = Arrays.asList(testUsage);
        Page<DiscountUsage> usagePage = new PageImpl<>(usages);
        Pageable pageable = PageRequest.of(0, 10);

        when(discountUsageRepository.findByDiscount_DiscountId(1L, pageable)).thenReturn(usagePage);

        // When
        Page<DiscountUsage> result = discountService.getDiscountUsages(1L, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDiscountAmount()).isEqualByComparingTo(new BigDecimal("15000.00"));
    }

    @Test
    public void testGetUserDiscountUsages() {
        // Given
        List<DiscountUsage> usages = Arrays.asList(testUsage);
        Page<DiscountUsage> usagePage = new PageImpl<>(usages);
        Pageable pageable = PageRequest.of(0, 10);

        when(discountUsageRepository.findByUser_UserId(1L, pageable)).thenReturn(usagePage);

        // When
        Page<DiscountUsage> result = discountService.getUserDiscountUsages(1L, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getUserId()).isEqualTo(1L);
    }

    @Test
    public void testHasUserUsedDiscount() {
        // Given
        when(discountUsageRepository.existsByUser_UserIdAndDiscount_DiscountId(1L, 1L)).thenReturn(true);

        // When
        boolean result = discountService.hasUserUsedDiscount(1L, 1L);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void testGetAvailableDiscountsForUser() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<Discount> availableDiscounts = Arrays.asList(activeDiscount);

        when(discountRepository.findValidDiscountsForOrderAmount(new BigDecimal("150000.00"), now))
                .thenReturn(availableDiscounts);

        // When
        List<DiscountResponse> result = discountService.getAvailableDiscountsForUser(1L, new BigDecimal("150000.00"));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testGetDiscountByCode() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("WELCOME10")).thenReturn(Optional.of(activeDiscount));

        // When
        DiscountResponse result = discountService.getDiscountByCode("WELCOME10");

        // Then
        assertThat(result.getDiscountId()).isEqualTo(1L);
        assertThat(result.getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testGetDiscountByCode_NotFound() {
        // Given
        when(discountRepository.findByCodeIgnoreCase("NOTFOUND")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> discountService.getDiscountByCode("NOTFOUND"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount not found with code: NOTFOUND");
    }

    @Test
    public void testValidateDiscount_FixedAmountDiscount() {
        // Given
        Discount fixedDiscount = new Discount();
        fixedDiscount.setCode("FIXED50K");
        fixedDiscount.setDescription("Fixed discount 50k");
        fixedDiscount.setDiscountType(DiscountType.FIXED_AMOUNT);
        fixedDiscount.setDiscountValue(new BigDecimal("50000.00"));
        fixedDiscount.setMinOrderAmount(new BigDecimal("100000.00"));
        fixedDiscount.setUsageLimit(100);
        fixedDiscount.setUsedCount(10);
        fixedDiscount.setIsActive(true);
        fixedDiscount.setStartDate(LocalDateTime.now().minusDays(1));
        fixedDiscount.setEndDate(LocalDateTime.now().plusDays(30));

        when(discountRepository.findByCodeIgnoreCase("FIXED50K")).thenReturn(Optional.of(fixedDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("FIXED50K", new BigDecimal("200000.00"));

        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.getDiscountType()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(result.getCalculatedDiscountAmount()).isEqualByComparingTo(new BigDecimal("50000.00"));
    }

    @Test
    public void testValidateDiscount_WithMaxDiscountLimit() {
        // Given
        Discount maxLimitDiscount = new Discount();
        maxLimitDiscount.setCode("MAX20K");
        maxLimitDiscount.setDescription("Max discount 20k");
        maxLimitDiscount.setDiscountType(DiscountType.PERCENTAGE);
        maxLimitDiscount.setDiscountValue(new BigDecimal("20.00"));
        maxLimitDiscount.setMaxDiscountAmount(new BigDecimal("20000.00"));
        maxLimitDiscount.setMinOrderAmount(BigDecimal.ZERO);
        maxLimitDiscount.setUsageLimit(100);
        maxLimitDiscount.setUsedCount(10);
        maxLimitDiscount.setIsActive(true);
        maxLimitDiscount.setStartDate(LocalDateTime.now().minusDays(1));
        maxLimitDiscount.setEndDate(LocalDateTime.now().plusDays(30));

        when(discountRepository.findByCodeIgnoreCase("MAX20K")).thenReturn(Optional.of(maxLimitDiscount));

        // When
        DiscountValidationResponse result = discountService.validateDiscount("MAX20K", new BigDecimal("200000.00"));

        // Then
        assertThat(result.isValid()).isTrue();
        assertThat(result.getCalculatedDiscountAmount()).isEqualByComparingTo(new BigDecimal("20000.00")); // Capped at max
    }
}
