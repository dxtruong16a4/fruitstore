package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.domain.order.OrderStatus;
import com.fruitstore.dto.request.order.UpdateOrderStatusRequest;
import com.fruitstore.dto.response.order.OrderListResponse;
import com.fruitstore.dto.response.order.OrderResponse;
import com.fruitstore.dto.response.order.OrderSummaryResponse;
import com.fruitstore.service.OrderService;
import com.fruitstore.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for AdminOrderController REST endpoints
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "app.jwt.secret=testSecretKeyForJWTTokenGenerationAndValidationInTests",
    "app.jwt.expiration=86400000",
    "spring.security.user.name=admin",
    "spring.security.user.password=admin",
    "spring.security.user.roles=ADMIN"
})
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil;

    private OrderResponse orderResponse;
    private OrderListResponse orderListResponse;

    @BeforeEach
    void setUp() {
        orderResponse = new OrderResponse();
        orderResponse.setOrderId(1L);
        orderResponse.setOrderNumber("ORD-001");
        orderResponse.setStatus(OrderStatus.PENDING);
        orderResponse.setTotalAmount(new BigDecimal("100.00"));
        orderResponse.setCustomerName("Test User");
        orderResponse.setCustomerEmail("test@example.com");
        orderResponse.setCreatedAt(LocalDateTime.now());

        List<OrderSummaryResponse> orders = new ArrayList<>();
        OrderSummaryResponse summary = new OrderSummaryResponse();
        summary.setOrderId(1L);
        summary.setOrderNumber("ORD-001");
        summary.setStatus(OrderStatus.PENDING);
        summary.setTotalAmount(new BigDecimal("100.00"));
        summary.setCustomerName("Test User");
        summary.setCustomerEmail("test@example.com");
        summary.setCreatedAt(LocalDateTime.now());
        orders.add(summary);

        orderListResponse = OrderListResponse.success(orders, 1L, 1, 1, 10, false, false, true, true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllOrders_ShouldReturnOrders() throws Exception {
        // Given
        when(orderService.getAllOrders(any(Pageable.class)))
                .thenReturn(orderListResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.data[0].orderId").value(1L))
                .andExpect(jsonPath("$.data.data[0].orderNumber").value("ORD-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrderById_ShouldReturnOrder() throws Exception {
        // Given
        when(orderService.getOrderById(anyLong()))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(1L))
                .andExpect(jsonPath("$.data.orderNumber").value("ORD-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setStatus(OrderStatus.CONFIRMED);
        request.setAdminNotes("Order confirmed by admin");

        orderResponse.setStatus(OrderStatus.CONFIRMED);
        when(orderService.updateOrderStatus(anyLong(), any(UpdateOrderStatusRequest.class)))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Order status updated successfully"))
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelOrder_ShouldReturnCancelledOrder() throws Exception {
        // Given
        orderResponse.setStatus(OrderStatus.CANCELLED);
        when(orderService.cancelOrder(anyLong()))
                .thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Order cancelled successfully"))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrdersByStatus_ShouldReturnFilteredOrders() throws Exception {
        // Given
        when(orderService.getOrdersByStatus(any(OrderStatus.class), any(Pageable.class)))
                .thenReturn(orderListResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/orders/status/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrderStatistics_ShouldReturnStatistics() throws Exception {
        // Given
        OrderService.OrderStatistics statistics = new OrderService.OrderStatistics();
        statistics.setTotalOrders(10L);
        statistics.setPendingOrders(3L);
        statistics.setConfirmedOrders(4L);
        statistics.setDeliveredOrders(2L);
        statistics.setCancelledOrders(1L);
        statistics.setTotalRevenue(new BigDecimal("1000.00"));
        statistics.setAverageOrderValue(new BigDecimal("100.00"));

        when(orderService.getOrderStatistics())
                .thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/api/admin/orders/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalOrders").value(10))
                .andExpect(jsonPath("$.data.pendingOrders").value(3))
                .andExpect(jsonPath("$.data.totalRevenue").value(1000.00));
    }

    @Test
    void getAllOrders_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getAllOrders_WithCustomerRole_ShouldReturnForbidden() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrdersByStatus_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/admin/orders/status/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid order status: invalid"));
    }
}
