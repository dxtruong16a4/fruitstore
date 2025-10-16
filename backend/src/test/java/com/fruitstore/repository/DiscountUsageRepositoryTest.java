package com.fruitstore.repository;

import com.fruitstore.domain.discount.Discount;
import com.fruitstore.domain.discount.DiscountType;
import com.fruitstore.domain.discount.DiscountUsage;
import com.fruitstore.domain.order.Order;
import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DiscountUsageRepository
 * Tests custom queries, filtering, and CRUD operations
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class DiscountUsageRepositoryTest {

    @Autowired
    private DiscountUsageRepository discountUsageRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Discount discount1;
    private Discount discount2;
    private Order order1;
    private Order order2;
    private DiscountUsage usage1;
    private DiscountUsage usage2;
    private DiscountUsage usage3;
    private DiscountUsage usage4;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        
        // Create test users
        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setFullName("User One");
        user1.setRole(UserRole.CUSTOMER);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setFullName("User Two");
        user2.setRole(UserRole.CUSTOMER);

        // Create test discounts
        discount1 = new Discount();
        discount1.setCode("DISCOUNT10");
        discount1.setDescription("10% discount");
        discount1.setDiscountType(DiscountType.PERCENTAGE);
        discount1.setDiscountValue(new BigDecimal("10.00"));
        discount1.setIsActive(true);

        discount2 = new Discount();
        discount2.setCode("DISCOUNT50K");
        discount2.setDescription("50k fixed discount");
        discount2.setDiscountType(DiscountType.FIXED_AMOUNT);
        discount2.setDiscountValue(new BigDecimal("50000.00"));
        discount2.setIsActive(true);

        // Create test orders
        order1 = new Order();
        order1.setOrderNumber("ORD001");
        order1.setUser(user1);
        order1.setStatus(OrderStatus.PENDING);
        order1.setTotalAmount(new BigDecimal("100000.00"));
        order1.setShippingAddress("Address 1");
        order1.setCustomerName("User One");

        order2 = new Order();
        order2.setOrderNumber("ORD002");
        order2.setUser(user2);
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("200000.00"));
        order2.setShippingAddress("Address 2");
        order2.setCustomerName("User Two");

        // Persist entities
        user1 = entityManager.persistAndFlush(user1);
        user2 = entityManager.persistAndFlush(user2);
        discount1 = entityManager.persistAndFlush(discount1);
        discount2 = entityManager.persistAndFlush(discount2);
        order1 = entityManager.persistAndFlush(order1);
        order2 = entityManager.persistAndFlush(order2);

        // Create test discount usages
        usage1 = new DiscountUsage();
        usage1.setDiscount(discount1);
        usage1.setUser(user1);
        usage1.setOrder(order1);
        usage1.setDiscountAmount(new BigDecimal("10000.00"));
        usage1.setUsedAt(now.minusDays(2));

        usage2 = new DiscountUsage();
        usage2.setDiscount(discount1);
        usage2.setUser(user1);
        usage2.setOrder(order2);
        usage2.setDiscountAmount(new BigDecimal("15000.00"));
        usage2.setUsedAt(now.minusDays(1));

        usage3 = new DiscountUsage();
        usage3.setDiscount(discount2);
        usage3.setUser(user2);
        usage3.setOrder(null); // Standalone usage
        usage3.setDiscountAmount(new BigDecimal("50000.00"));
        usage3.setUsedAt(now);

        usage4 = new DiscountUsage();
        usage4.setDiscount(discount1);
        usage4.setUser(user2);
        usage4.setOrder(null); // Standalone usage
        usage4.setDiscountAmount(new BigDecimal("8000.00"));
        usage4.setUsedAt(now);

        // Persist discount usages
        entityManager.persistAndFlush(usage1);
        entityManager.persistAndFlush(usage2);
        entityManager.persistAndFlush(usage3);
        entityManager.persistAndFlush(usage4);
        entityManager.clear();
    }

    @Test
    public void testFindByDiscount() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByDiscount(discount1);

        // Then
        assertThat(result).hasSize(3); // usage1, usage2, usage4
        assertThat(result).extracting(du -> du.getDiscount().getCode())
            .containsOnly("DISCOUNT10");
    }

    @Test
    public void testFindByDiscount_DiscountId() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByDiscount_DiscountId(discount1.getDiscountId());

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(du -> du.getDiscount().getCode())
            .containsOnly("DISCOUNT10");
    }

    @Test
    public void testFindByUser() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByUser(user1);

        // Then
        assertThat(result).hasSize(2); // usage1, usage2
        assertThat(result).extracting(du -> du.getUser().getUsername())
            .containsOnly("user1");
    }

    @Test
    public void testFindByUser_UserId() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByUser_UserId(user1.getUserId());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(du -> du.getUser().getUsername())
            .containsOnly("user1");
    }

    @Test
    public void testFindByOrder() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByOrder(order1);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDiscount().getCode()).isEqualTo("DISCOUNT10");
    }

    @Test
    public void testFindByOrder_OrderId() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByOrder_OrderId(order1.getOrderId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDiscount().getCode()).isEqualTo("DISCOUNT10");
    }

    @Test
    public void testFindByUserAndDiscount() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByUserAndDiscount(user1, discount1);

        // Then
        assertThat(result).hasSize(2); // usage1, usage2
        assertThat(result).extracting(du -> du.getUser().getUsername())
            .containsOnly("user1");
        assertThat(result).extracting(du -> du.getDiscount().getCode())
            .containsOnly("DISCOUNT10");
    }

    @Test
    public void testFindByUser_UserIdAndDiscount_DiscountId() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByUser_UserIdAndDiscount_DiscountId(
            user1.getUserId(), discount1.getDiscountId());

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    public void testCountByDiscount_DiscountIdAndUser_UserId() {
        // When
        long count = discountUsageRepository.countByDiscount_DiscountIdAndUser_UserId(
            discount1.getDiscountId(), user1.getUserId());

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testCountByDiscount_DiscountIdAndUser_UserId_NoUsage() {
        // When
        long count = discountUsageRepository.countByDiscount_DiscountIdAndUser_UserId(
            discount2.getDiscountId(), user1.getUserId());

        // Then
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testFindByUsedAtBetween() {
        // When
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = now.minusHours(12);
        List<DiscountUsage> result = discountUsageRepository.findByUsedAtBetween(startDate, endDate);

        // Then
        assertThat(result).hasSize(2); // usage1, usage2
    }

    @Test
    public void testFindByUsedAtBetween_WithPagination() {
        // When
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = now.plusDays(1);
        Pageable pageable = PageRequest.of(0, 2);
        Page<DiscountUsage> result = discountUsageRepository.findByUsedAtBetween(startDate, endDate, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindByUser_UserId_WithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 1);
        Page<DiscountUsage> result = discountUsageRepository.findByUser_UserId(user1.getUserId(), pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindByDiscount_DiscountId_WithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 2);
        Page<DiscountUsage> result = discountUsageRepository.findByDiscount_DiscountId(discount1.getDiscountId(), pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindAllByOrderByUsedAtDesc() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findAllByOrderByUsedAtDesc();

        // Then
        assertThat(result).hasSize(4);
        // Should be ordered by used date descending (newest first)
        assertThat(result.get(0).getUsedAt()).isEqualTo(now); // usage3 or usage4
        assertThat(result.get(1).getUsedAt()).isEqualTo(now); // usage3 or usage4
    }

    @Test
    public void testFindRecentUsages() {
        // When
        LocalDateTime startDate = now.minusHours(1);
        Pageable pageable = PageRequest.of(0, 10);
        Page<DiscountUsage> result = discountUsageRepository.findRecentUsages(startDate, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // usage3, usage4
    }

    @Test
    public void testFindByDiscountCode() {
        // When
        List<DiscountUsage> result = discountUsageRepository.findByDiscountCode("DISCOUNT10");

        // Then
        assertThat(result).hasSize(3); // usage1, usage2, usage4
        assertThat(result).extracting(du -> du.getDiscount().getCode())
            .containsOnly("DISCOUNT10");
    }

    @Test
    public void testFindByDiscountCode_WithPagination() {
        // When
        Pageable pageable = PageRequest.of(0, 2);
        Page<DiscountUsage> result = discountUsageRepository.findByDiscountCode("DISCOUNT10", pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testFindByUserAndDateRange() {
        // When
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = now.minusHours(12);
        List<DiscountUsage> result = discountUsageRepository.findByUserAndDateRange(
            user1.getUserId(), startDate, endDate);

        // Then
        assertThat(result).hasSize(2); // usage1, usage2
        assertThat(result).extracting(du -> du.getUser().getUsername())
            .containsOnly("user1");
    }

    @Test
    public void testFindByDiscountAndDateRange() {
        // When
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = now.plusDays(1);
        List<DiscountUsage> result = discountUsageRepository.findByDiscountAndDateRange(
            discount1.getDiscountId(), startDate, endDate);

        // Then
        assertThat(result).hasSize(3); // usage1, usage2, usage4
        assertThat(result).extracting(du -> du.getDiscount().getCode())
            .containsOnly("DISCOUNT10");
    }

    @Test
    public void testFindTotalDiscountAmountByUser() {
        // When
        BigDecimal totalAmount = discountUsageRepository.findTotalDiscountAmountByUser(user1.getUserId());

        // Then
        assertThat(totalAmount).isEqualTo(new BigDecimal("25000.00")); // 10000 + 15000
    }

    @Test
    public void testFindTotalDiscountAmountByDiscount() {
        // When
        BigDecimal totalAmount = discountUsageRepository.findTotalDiscountAmountByDiscount(discount1.getDiscountId());

        // Then
        assertThat(totalAmount).isEqualTo(new BigDecimal("33000.00")); // 10000 + 15000 + 8000
    }

    @Test
    public void testFindUsagesWithOrders() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<DiscountUsage> result = discountUsageRepository.findUsagesWithOrders(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // usage1, usage2 (have orders)
        assertThat(result.getContent()).extracting(DiscountUsage::getOrder)
            .allMatch(order -> order != null);
    }

    @Test
    public void testFindStandaloneUsages() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<DiscountUsage> result = discountUsageRepository.findStandaloneUsages(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // usage3, usage4 (no orders)
        assertThat(result.getContent()).extracting(DiscountUsage::getOrder)
            .allMatch(order -> order == null);
    }

    @Test
    public void testCountByUser_UserId() {
        // When
        long count = discountUsageRepository.countByUser_UserId(user1.getUserId());

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testCountByDiscount_DiscountId() {
        // When
        long count = discountUsageRepository.countByDiscount_DiscountId(discount1.getDiscountId());

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void testCountByUser_UserIdAndDiscount_DiscountId() {
        // When
        long count = discountUsageRepository.countByUser_UserIdAndDiscount_DiscountId(
            user1.getUserId(), discount1.getDiscountId());

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testCountByUsedAtBetween() {
        // When
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = now.plusDays(1);
        long count = discountUsageRepository.countByUsedAtBetween(startDate, endDate);

        // Then
        assertThat(count).isEqualTo(4);
    }

    @Test
    public void testExistsByUser_UserIdAndDiscount_DiscountId() {
        // When
        boolean exists = discountUsageRepository.existsByUser_UserIdAndDiscount_DiscountId(
            user1.getUserId(), discount1.getDiscountId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByUser_UserIdAndDiscount_DiscountId_NoUsage() {
        // When
        boolean exists = discountUsageRepository.existsByUser_UserIdAndDiscount_DiscountId(
            user1.getUserId(), discount2.getDiscountId());

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindLatestUsageByUserAndDiscount() {
        // When
        Optional<DiscountUsage> result = discountUsageRepository.findLatestUsageByUserAndDiscount(
            user1.getUserId(), discount1.getDiscountId());

        // Then
        assertThat(result).isPresent();
        // Should be the most recent usage by user1 for discount1
        assertThat(result.get().getUsedAt()).isEqualTo(now.minusDays(1)); // usage2 is more recent than usage1
    }

    @Test
    public void testFindLatestUsageByUserAndDiscount_NoUsage() {
        // When
        Optional<DiscountUsage> result = discountUsageRepository.findLatestUsageByUserAndDiscount(
            user1.getUserId(), discount2.getDiscountId());

        // Then
        assertThat(result).isEmpty();
    }
}
