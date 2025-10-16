package com.fruitstore.repository;

import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DiscountRepository
 * Tests custom queries, filtering, and CRUD operations
 */
@DataJpaTest
@ActiveProfiles("test")
public class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Discount activePercentageDiscount;
    private Discount activeFixedDiscount;
    private Discount inactiveDiscount;
    private Discount expiredDiscount;
    private Discount futureDiscount;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        
        // Create active percentage discount
        activePercentageDiscount = new Discount();
        activePercentageDiscount.setCode("WELCOME10");
        activePercentageDiscount.setDescription("Welcome discount 10%");
        activePercentageDiscount.setDiscountType(DiscountType.PERCENTAGE);
        activePercentageDiscount.setDiscountValue(new BigDecimal("10.00"));
        activePercentageDiscount.setMinOrderAmount(new BigDecimal("100000.00"));
        activePercentageDiscount.setMaxDiscountAmount(new BigDecimal("50000.00"));
        activePercentageDiscount.setUsageLimit(100);
        activePercentageDiscount.setUsedCount(10);
        activePercentageDiscount.setStartDate(now.minusDays(1));
        activePercentageDiscount.setEndDate(now.plusDays(30));
        activePercentageDiscount.setIsActive(true);

        // Create active fixed amount discount
        activeFixedDiscount = new Discount();
        activeFixedDiscount.setCode("FRUIT50K");
        activeFixedDiscount.setDescription("Fixed discount 50k");
        activeFixedDiscount.setDiscountType(DiscountType.FIXED_AMOUNT);
        activeFixedDiscount.setDiscountValue(new BigDecimal("50000.00"));
        activeFixedDiscount.setMinOrderAmount(new BigDecimal("500000.00"));
        activeFixedDiscount.setUsageLimit(200);
        activeFixedDiscount.setUsedCount(50);
        activeFixedDiscount.setStartDate(now.minusDays(1));
        activeFixedDiscount.setEndDate(now.plusDays(30));
        activeFixedDiscount.setIsActive(true);

        // Create inactive discount
        inactiveDiscount = new Discount();
        inactiveDiscount.setCode("INACTIVE20");
        inactiveDiscount.setDescription("Inactive discount");
        inactiveDiscount.setDiscountType(DiscountType.PERCENTAGE);
        inactiveDiscount.setDiscountValue(new BigDecimal("20.00"));
        inactiveDiscount.setIsActive(false);

        // Create expired discount
        expiredDiscount = new Discount();
        expiredDiscount.setCode("EXPIRED15");
        expiredDiscount.setDescription("Expired discount");
        expiredDiscount.setDiscountType(DiscountType.PERCENTAGE);
        expiredDiscount.setDiscountValue(new BigDecimal("15.00"));
        expiredDiscount.setStartDate(now.minusDays(30));
        expiredDiscount.setEndDate(now.minusDays(1));
        expiredDiscount.setIsActive(true);

        // Create future discount
        futureDiscount = new Discount();
        futureDiscount.setCode("FUTURE25");
        futureDiscount.setDescription("Future discount");
        futureDiscount.setDiscountType(DiscountType.PERCENTAGE);
        futureDiscount.setDiscountValue(new BigDecimal("25.00"));
        futureDiscount.setStartDate(now.plusDays(1));
        futureDiscount.setEndDate(now.plusDays(30));
        futureDiscount.setIsActive(true);

        // Persist all discounts
        entityManager.persistAndFlush(activePercentageDiscount);
        entityManager.persistAndFlush(activeFixedDiscount);
        entityManager.persistAndFlush(inactiveDiscount);
        entityManager.persistAndFlush(expiredDiscount);
        entityManager.persistAndFlush(futureDiscount);
        entityManager.clear();
    }

    @Test
    public void testFindByCode() {
        // When
        Optional<Discount> result = discountRepository.findByCode("WELCOME10");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("WELCOME10");
        assertThat(result.get().getDescription()).isEqualTo("Welcome discount 10%");
    }

    @Test
    public void testFindByCode_NotFound() {
        // When
        Optional<Discount> result = discountRepository.findByCode("NOTFOUND");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByCodeIgnoreCase() {
        // When
        Optional<Discount> result = discountRepository.findByCodeIgnoreCase("welcome10");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testExistsByCode() {
        // When
        boolean exists = discountRepository.existsByCode("WELCOME10");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByCode_NotFound() {
        // When
        boolean exists = discountRepository.existsByCode("NOTFOUND");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByCodeIgnoreCase() {
        // When
        boolean exists = discountRepository.existsByCodeIgnoreCase("welcome10");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByIsActiveTrue() {
        // When
        List<Discount> result = discountRepository.findByIsActiveTrue();

        // Then
        assertThat(result).hasSize(4); // activePercentageDiscount, activeFixedDiscount, expiredDiscount, futureDiscount
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10", "FRUIT50K", "EXPIRED15", "FUTURE25");
    }

    @Test
    public void testFindByIsActiveTrueOrderByCreatedAtDesc() {
        // When
        List<Discount> result = discountRepository.findByIsActiveTrueOrderByCreatedAtDesc();

        // Then
        assertThat(result).hasSize(4);
        // Should be ordered by created date descending (newest first)
        assertThat(result.get(0).getCode()).isEqualTo("FUTURE25"); // Last created
    }

    @Test
    public void testFindByDiscountTypeAndIsActiveTrue() {
        // When
        List<Discount> result = discountRepository.findByDiscountTypeAndIsActiveTrue(DiscountType.PERCENTAGE);

        // Then
        assertThat(result).hasSize(3); // activePercentageDiscount, expiredDiscount, futureDiscount
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10", "EXPIRED15", "FUTURE25");
    }

    @Test
    public void testFindByIsActiveTrueAndStartDateBeforeAndEndDateAfter() {
        // When
        List<Discount> result = discountRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(now);

        // Then
        assertThat(result).hasSize(2); // Only currently active discounts
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10", "FRUIT50K");
    }

    @Test
    public void testFindUsableDiscounts() {
        // When
        List<Discount> result = discountRepository.findUsableDiscounts(now);

        // Then
        assertThat(result).hasSize(2); // Only usable discounts
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10", "FRUIT50K");
    }

    @Test
    public void testFindValidDiscountsForOrderAmount() {
        // When - test with order amount that meets minimum requirements
        List<Discount> result = discountRepository.findValidDiscountsForOrderAmount(
            new BigDecimal("600000.00"), now);

        // Then
        assertThat(result).hasSize(2); // Both discounts valid for this amount
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10", "FRUIT50K");
    }

    @Test
    public void testFindValidDiscountsForOrderAmount_LowAmount() {
        // When - test with order amount below minimum requirements
        List<Discount> result = discountRepository.findValidDiscountsForOrderAmount(
            new BigDecimal("50000.00"), now);

        // Then
        assertThat(result).isEmpty(); // No discounts valid for this low amount
    }

    @Test
    public void testFindExpiredDiscounts() {
        // When
        List<Discount> result = discountRepository.findExpiredDiscounts(now);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("EXPIRED15");
    }

    @Test
    public void testFindFutureDiscounts() {
        // When
        List<Discount> result = discountRepository.findFutureDiscounts(now);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("FUTURE25");
    }

    @Test
    public void testFindDiscountsWithUsageLimitReached() {
        // Create a discount with usage limit reached
        Discount limitReachedDiscount = new Discount();
        limitReachedDiscount.setCode("LIMIT100");
        limitReachedDiscount.setDescription("Limit reached discount");
        limitReachedDiscount.setDiscountType(DiscountType.PERCENTAGE);
        limitReachedDiscount.setDiscountValue(new BigDecimal("10.00"));
        limitReachedDiscount.setUsageLimit(10);
        limitReachedDiscount.setUsedCount(10);
        limitReachedDiscount.setIsActive(true);
        
        entityManager.persistAndFlush(limitReachedDiscount);
        entityManager.clear();

        // When
        List<Discount> result = discountRepository.findDiscountsWithUsageLimitReached();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("LIMIT100");
    }

    @Test
    public void testFindDiscountsWithLowUsage() {
        // When - find discounts with usage below 20
        List<Discount> result = discountRepository.findDiscountsWithLowUsage(20);

        // Then
        assertThat(result).hasSize(1); // Only WELCOME10 has usedCount (10) < 20
        assertThat(result).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10");
    }

    @Test
    public void testCountByIsActive() {
        // When
        long activeCount = discountRepository.countByIsActive(true);
        long inactiveCount = discountRepository.countByIsActive(false);

        // Then
        assertThat(activeCount).isEqualTo(4);
        assertThat(inactiveCount).isEqualTo(1);
    }

    @Test
    public void testCountActiveDiscountsWithinDateRange() {
        // When
        long count = discountRepository.countActiveDiscountsWithinDateRange(now);

        // Then
        assertThat(count).isEqualTo(2); // Only currently active discounts
    }

    @Test
    public void testCountByDiscountTypeAndIsActiveTrue() {
        // When
        long percentageCount = discountRepository.countByDiscountTypeAndIsActiveTrue(DiscountType.PERCENTAGE);
        long fixedCount = discountRepository.countByDiscountTypeAndIsActiveTrue(DiscountType.FIXED_AMOUNT);

        // Then
        assertThat(percentageCount).isEqualTo(3); // WELCOME10, EXPIRED15, FUTURE25
        assertThat(fixedCount).isEqualTo(1); // FRUIT50K
    }

    @Test
    public void testFindByCodeContainingIgnoreCase() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> result = discountRepository.findByCodeContainingIgnoreCase("welcome", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testFindByDescriptionContainingIgnoreCase() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> result = discountRepository.findByDescriptionContainingIgnoreCase("welcome", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("WELCOME10");
    }

    @Test
    public void testFindDiscountsWithFilters_ActiveOnly() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> result = discountRepository.findDiscountsWithFilters(true, null, now, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // Only currently valid active discounts (WELCOME10, FRUIT50K)
    }

    @Test
    public void testFindDiscountsWithFilters_PercentageType() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> result = discountRepository.findDiscountsWithFilters(null, DiscountType.PERCENTAGE, now, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // Only currently valid percentage discounts (WELCOME10, INACTIVE20)
    }

    @Test
    public void testFindDiscountsWithFilters_ActiveAndPercentage() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Discount> result = discountRepository.findDiscountsWithFilters(true, DiscountType.PERCENTAGE, now, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1); // Only currently valid active percentage discount (WELCOME10)
        assertThat(result.getContent()).extracting(Discount::getCode)
            .containsExactlyInAnyOrder("WELCOME10");
    }

    @Test
    public void testFindByIsActiveTrue_WithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 2);
        Page<Discount> result = discountRepository.findByIsActiveTrue(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }
}
