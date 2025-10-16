package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.domain.discount.DiscountType;
import com.fruitstore.domain.discount.DiscountUsage;
import com.fruitstore.dto.request.discount.CreateDiscountRequest;
import com.fruitstore.dto.request.discount.UpdateDiscountRequest;
import com.fruitstore.dto.request.discount.ValidateDiscountRequest;
import com.fruitstore.dto.response.discount.DiscountResponse;
import com.fruitstore.dto.response.discount.DiscountValidationResponse;
import com.fruitstore.dto.response.discount.DiscountUsageResponse;
import com.fruitstore.service.DiscountService;
import com.fruitstore.service.DiscountService.DiscountUsageStats;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for DiscountController
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "app.jwt.secret=testSecretKeyForJWTTokenGenerationAndValidationInTests",
    "app.jwt.expiration=86400000",
    "app.cors.allowed-origins=http://localhost:3000",
    "app.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS",
    "app.cors.allowed-headers=*",
    "app.cors.allow-credentials=true"
})
public class DiscountControllerTest {

    @MockBean
    private DiscountService discountService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private DiscountResponse discountResponse;
    private DiscountValidationResponse validationResponse;
    private CreateDiscountRequest createRequest;
    private UpdateDiscountRequest updateRequest;
    private ValidateDiscountRequest validateRequest;
    private DiscountUsageStats usageStats;
    private DiscountUsage discountUsage;
    private DiscountUsageResponse discountUsageResponse;
    
    // Test users for authentication
    private CustomUserDetails adminUserDetails;
    private CustomUserDetails customerUserDetails;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        // Create discount response
        discountResponse = new DiscountResponse();
        discountResponse.setDiscountId(1L);
        discountResponse.setCode("WELCOME10");
        discountResponse.setDescription("Welcome discount 10%");
        discountResponse.setDiscountType(DiscountType.PERCENTAGE);
        discountResponse.setDiscountValue(new BigDecimal("10.00"));
        discountResponse.setMinOrderAmount(new BigDecimal("100000.00"));
        discountResponse.setMaxDiscountAmount(new BigDecimal("50000.00"));
        discountResponse.setUsageLimit(100);
        discountResponse.setUsedCount(10);
        discountResponse.setStartDate(LocalDateTime.now().plusDays(1));
        discountResponse.setEndDate(LocalDateTime.now().plusDays(30));
        discountResponse.setIsActive(true);
        discountResponse.setCreatedAt(LocalDateTime.now());
        discountResponse.setUpdatedAt(LocalDateTime.now());

        // Create validation response
        validationResponse = DiscountValidationResponse.valid(
            "WELCOME10", DiscountType.PERCENTAGE, new BigDecimal("10.00"),
            new BigDecimal("15000.00"), new BigDecimal("100000.00"),
            new BigDecimal("50000.00"), 100, 10, 90, "Welcome discount 10%"
        );

        // Create usage stats
        usageStats = new DiscountService.DiscountUsageStats(
            1L, "WELCOME10", 10L, new BigDecimal("150000.00"), 10, 100
        );

        // Create discount usage
        discountUsage = new DiscountUsage();
        discountUsage.setUsageId(1L);
        discountUsage.setDiscountAmount(new BigDecimal("15000.00"));
        discountUsage.setUsedAt(LocalDateTime.now());

        // Create discount usage response
        discountUsageResponse = new DiscountUsageResponse();
        discountUsageResponse.setUsageId(1L);
        discountUsageResponse.setDiscountId(1L);
        discountUsageResponse.setDiscountCode("WELCOME10");
        discountUsageResponse.setUserId(1L);
        discountUsageResponse.setUsername("customer");
        discountUsageResponse.setOrderId(1L);
        discountUsageResponse.setOrderNumber("ORD-001");
        discountUsageResponse.setDiscountAmount(new BigDecimal("15000.00"));
        discountUsageResponse.setUsedAt(LocalDateTime.now());

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

        validateRequest = new ValidateDiscountRequest();
        validateRequest.setCode("WELCOME10");
        validateRequest.setOrderAmount(new BigDecimal("150000.00"));
        
        // Setup test users
        User adminUser = new User();
        adminUser.setUserId(1L);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@test.com");
        adminUser.setRole(UserRole.ADMIN);
        adminUserDetails = new CustomUserDetails(adminUser);
        
        User customerUser = new User();
        customerUser.setUserId(2L);
        customerUser.setUsername("customer");
        customerUser.setEmail("customer@test.com");
        customerUser.setRole(UserRole.CUSTOMER);
        customerUserDetails = new CustomUserDetails(customerUser);
    }

    // ==================== PUBLIC ENDPOINTS ====================

    @Test
    public void testGetActiveDiscounts() throws Exception {
        // Given
        List<DiscountResponse> discounts = Arrays.asList(discountResponse);
        Page<DiscountResponse> discountPage = new PageImpl<>(discounts, PageRequest.of(0, 20), 1);
        when(discountService.getActiveDiscounts(any())).thenReturn(discountPage);

        // When & Then
        mockMvc.perform(get("/api/discounts/active")
                .param("page", "0")
                .param("size", "20")
                .param("sortBy", "createdAt")
                .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].code").value("WELCOME10"));
    }

    @Test
    public void testValidateDiscount_Valid() throws Exception {
        // Given
        when(discountService.validateDiscount("WELCOME10", new BigDecimal("150000.00")))
                .thenReturn(validationResponse);

        // When & Then
        mockMvc.perform(post("/api/discounts/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"))
                .andExpect(jsonPath("$.data.calculatedDiscountAmount").value(15000.00));
    }

    @Test
    public void testValidateDiscount_Invalid() throws Exception {
        // Given
        DiscountValidationResponse invalidResponse = DiscountValidationResponse.notFound("NOTFOUND");
        when(discountService.validateDiscount("NOTFOUND", new BigDecimal("150000.00")))
                .thenReturn(invalidResponse);

        validateRequest.setCode("NOTFOUND");

        // When & Then
        mockMvc.perform(post("/api/discounts/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Discount code not found"));
    }

    @Test
    public void testGetDiscountByCode() throws Exception {
        // Given
        when(discountService.getDiscountByCode("WELCOME10")).thenReturn(discountResponse);

        // When & Then
        mockMvc.perform(get("/api/discounts/code/WELCOME10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"))
                .andExpect(jsonPath("$.data.discountType").value("PERCENTAGE"));
    }

    @Test
    public void testGetAvailableDiscountsForUser() throws Exception {
        // Given
        List<DiscountResponse> discounts = Arrays.asList(discountResponse);
        when(discountService.getAvailableDiscountsForUser(1L, new BigDecimal("150000.00")))
                .thenReturn(discounts);

        // When & Then
        mockMvc.perform(get("/api/discounts/available")
                .with(user(customerUserDetails))
                .param("userId", "1")
                .param("orderAmount", "150000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").value("WELCOME10"));
    }

    // ==================== ADMIN ENDPOINTS ====================

    @Test
    public void testGetAllDiscounts() throws Exception {
        // Given
        List<DiscountResponse> discounts = Arrays.asList(discountResponse);
        Page<DiscountResponse> discountPage = new PageImpl<>(discounts, PageRequest.of(0, 20), 1);
        when(discountService.getAllDiscounts(any())).thenReturn(discountPage);

        // When & Then
        mockMvc.perform(get("/api/discounts")
                .with(user(adminUserDetails))
                .param("page", "0")
                .param("size", "20")
                .param("sortBy", "createdAt")
                .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].code").value("WELCOME10"));
    }

    @Test
    public void testGetDiscountById() throws Exception {
        // Given
        when(discountService.getDiscountById(1L)).thenReturn(discountResponse);

        // When & Then
        mockMvc.perform(get("/api/discounts/1")
                .with(user(adminUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.discountId").value(1))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"));
    }

    @Test
    public void testCreateDiscount() throws Exception {
        // Given
        when(discountService.createDiscount(any(CreateDiscountRequest.class)))
                .thenReturn(discountResponse);

        // When & Then
        mockMvc.perform(post("/api/discounts")
                .with(user(adminUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"));
    }

    @Test
    public void testCreateDiscount_InvalidRequest() throws Exception {
        // Given - Create invalid request (missing required fields)
        CreateDiscountRequest invalidRequest = new CreateDiscountRequest();
        invalidRequest.setDescription("Invalid request");

        // When & Then
        mockMvc.perform(post("/api/discounts")
                .with(user(adminUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateDiscount() throws Exception {
        // Given
        when(discountService.updateDiscount(eq(1L), any(UpdateDiscountRequest.class)))
                .thenReturn(discountResponse);

        // When & Then
        mockMvc.perform(put("/api/discounts/1")
                .with(user(adminUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"));
    }

    @Test
    public void testDeleteDiscount() throws Exception {
        // Given
        // No return value needed for delete operation

        // When & Then
        mockMvc.perform(delete("/api/discounts/1")
                .with(user(adminUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Discount deleted successfully"));
    }

    @Test
    public void testGetDiscountUsageStats() throws Exception {
        // Given
        when(discountService.getDiscountUsageStats(1L)).thenReturn(usageStats);

        // When & Then
        mockMvc.perform(get("/api/discounts/1/stats")
                .with(user(adminUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.discountId").value(1))
                .andExpect(jsonPath("$.data.code").value("WELCOME10"))
                .andExpect(jsonPath("$.data.totalUsages").value(10))
                .andExpect(jsonPath("$.data.totalDiscountAmount").value(150000.00));
    }

    @Test
    public void testGetDiscountUsages() throws Exception {
        // Given
        List<DiscountUsageResponse> usages = Arrays.asList(discountUsageResponse);
        Page<DiscountUsageResponse> usagePage = new PageImpl<>(usages, PageRequest.of(0, 20), 1);
        when(discountService.getDiscountUsages(eq(1L), any())).thenReturn(usagePage);

        // When & Then
        mockMvc.perform(get("/api/discounts/1/usages")
                .with(user(adminUserDetails))
                .param("page", "0")
                .param("size", "20")
                .param("sortBy", "usedAt")
                .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].discountAmount").value(15000.00));
    }

    @Test
    public void testGetUserDiscountUsages() throws Exception {
        // Given
        List<DiscountUsageResponse> usages = Arrays.asList(discountUsageResponse);
        Page<DiscountUsageResponse> usagePage = new PageImpl<>(usages, PageRequest.of(0, 20), 1);
        when(discountService.getUserDiscountUsages(eq(1L), any())).thenReturn(usagePage);

        // When & Then
        mockMvc.perform(get("/api/discounts/user/1/usages")
                .with(user(adminUserDetails))
                .param("page", "0")
                .param("size", "20")
                .param("sortBy", "usedAt")
                .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].discountAmount").value(15000.00));
    }

    @Test
    public void testHasUserUsedDiscount() throws Exception {
        // Given
        when(discountService.hasUserUsedDiscount(1L, 1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/discounts/user/1/discount/1/used")
                .with(user(adminUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testApplyDiscount() throws Exception {
        // Given
        when(discountService.applyDiscount("WELCOME10", new BigDecimal("150000.00")))
                .thenReturn(new BigDecimal("15000.00"));

        // When & Then
        mockMvc.perform(post("/api/discounts/apply")
                .with(user(customerUserDetails))
                .param("code", "WELCOME10")
                .param("orderAmount", "150000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(15000.00));
    }

    @Test
    public void testRecordDiscountUsage() throws Exception {
        // Given
        // No return value needed for record operation

        // When & Then
        mockMvc.perform(post("/api/discounts/usage")
                .with(user(adminUserDetails))
                .param("discountId", "1")
                .param("userId", "1")
                .param("orderId", "1")
                .param("discountAmount", "15000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Discount usage recorded successfully"));
    }

    @Test
    public void testRecordDiscountUsage_WithoutOrder() throws Exception {
        // Given
        // No return value needed for record operation

        // When & Then
        mockMvc.perform(post("/api/discounts/usage")
                .with(user(adminUserDetails))
                .param("discountId", "1")
                .param("userId", "1")
                .param("discountAmount", "15000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Discount usage recorded successfully"));
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    public void testValidateDiscountRequest_ValidationErrors() throws Exception {
        // Given - Create invalid request (missing required fields)
        ValidateDiscountRequest invalidRequest = new ValidateDiscountRequest();
        invalidRequest.setOrderAmount(new BigDecimal("150000.00"));
        // Missing code

        // When & Then
        mockMvc.perform(post("/api/discounts/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateDiscountRequest_ValidationErrors() throws Exception {
        // Given - Create invalid request (missing required fields)
        CreateDiscountRequest invalidRequest = new CreateDiscountRequest();
        invalidRequest.setDescription("Invalid request");
        // Missing code, discountType, discountValue

        // When & Then
        mockMvc.perform(post("/api/discounts")
                .with(user(adminUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    public void testGetActiveDiscounts_WithPagination() throws Exception {
        // Given
        List<DiscountResponse> discounts = Arrays.asList(discountResponse);
        Page<DiscountResponse> discountPage = new PageImpl<>(discounts, PageRequest.of(0, 10), 1);
        when(discountService.getActiveDiscounts(any())).thenReturn(discountPage);

        // When & Then
        mockMvc.perform(get("/api/discounts/active")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    public void testGetDiscountUsages_WithPagination() throws Exception {
        // Given
        List<DiscountUsageResponse> usages = Arrays.asList(discountUsageResponse);
        Page<DiscountUsageResponse> usagePage = new PageImpl<>(usages, PageRequest.of(0, 5), 1);
        when(discountService.getDiscountUsages(eq(1L), any())).thenReturn(usagePage);

        // When & Then
        mockMvc.perform(get("/api/discounts/1/usages")
                .with(user(adminUserDetails))
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }
}
