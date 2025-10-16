package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.request.order.CreateOrderRequest;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for OrderController REST endpoints
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private User testUser;
    private CustomUserDetails userDetails;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRole(UserRole.CUSTOMER);

        userDetails = new CustomUserDetails(testUser);

        orderResponse = new OrderResponse();
        orderResponse.setOrderId(1L);
        orderResponse.setOrderNumber("ORD-001");
        orderResponse.setStatus(OrderStatus.PENDING);
        orderResponse.setTotalAmount(new BigDecimal("100.00"));
        orderResponse.setCustomerName("Test User");
        orderResponse.setCustomerEmail("test@example.com");
        orderResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("123 Test St");
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setPhoneNumber("1234567890");
        request.setOrderItems(new ArrayList<>());

        when(orderService.createOrder(anyLong(), any(CreateOrderRequest.class)))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andExpect(jsonPath("$.data.orderId").value(1L))
                .andExpect(jsonPath("$.data.orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getOrderById_ShouldReturnOrder() throws Exception {
        // Given
        when(orderService.getOrderById(anyLong(), anyLong()))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(get("/api/orders/1")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(1L))
                .andExpect(jsonPath("$.data.orderNumber").value("ORD-001"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void cancelOrder_ShouldReturnCancelledOrder() throws Exception {
        // Given
        orderResponse.setStatus(OrderStatus.CANCELLED);
        when(orderService.cancelOrder(anyLong(), anyLong()))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(put("/api/orders/1/cancel")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Order cancelled successfully"))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getOrderStatistics_ShouldReturnStatistics() throws Exception {
        // Given
        OrderService.OrderStatistics statistics = new OrderService.OrderStatistics();
        statistics.setTotalOrders(5L);
        statistics.setPendingOrders(2L);
        statistics.setConfirmedOrders(2L);
        statistics.setDeliveredOrders(1L);
        statistics.setCancelledOrders(0L);
        statistics.setTotalRevenue(new BigDecimal("500.00"));
        statistics.setAverageOrderValue(new BigDecimal("100.00"));

        when(orderService.getOrderStatisticsByUser(anyLong()))
                .thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/api/orders/statistics")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalOrders").value(5))
                .andExpect(jsonPath("$.data.pendingOrders").value(2))
                .andExpect(jsonPath("$.data.totalRevenue").value(500.00));
    }

    @Test
    void createOrder_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("123 Test St");
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setOrderItems(new ArrayList<>());

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOrder_WithAdminRole_ShouldReturnForbidden() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("123 Test St");
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setOrderItems(new ArrayList<>());

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
