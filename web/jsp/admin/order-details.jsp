<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    Order order = (Order) request.getAttribute("order");
    List<Map<String, Object>> orderDetails = (List<Map<String, Object>>) request.getAttribute("orderDetails");
%>

<div class="order-details-content">
    <% if (order != null) { %>
        <div class="row">
            <div class="col-md-6">
                <h6 class="text-primary">Order Information</h6>
                <table class="table table-sm">
                    <tr>
                        <th width="40%">Order ID:</th>
                        <td>#<%= order.getOrderId() %></td>
                    </tr>
                    <tr>
                        <th>Status:</th>
                        <td>
                            <% 
                            String statusClass = "";
                            switch (order.getStatus()) {
                                case "pending": statusClass = "bg-warning"; break;
                                case "completed": statusClass = "bg-success"; break;
                                case "cancelled": statusClass = "bg-danger"; break;
                                default: statusClass = "bg-secondary";
                            }
                            %>
                            <span class="badge <%= statusClass %>"><%= order.getStatus() %></span>
                        </td>
                    </tr>
                    <tr>
                        <th>Order Date:</th>
                        <td><%= order.getOrderDate() %></td>
                    </tr>
                    <tr>
                        <th>Total Amount:</th>
                        <td><strong>$<%= String.format("%.2f", order.getTotalAmount()) %></strong></td>
                    </tr>
                </table>
            </div>
            <div class="col-md-6">
                <h6 class="text-primary">Customer Information</h6>
                <table class="table table-sm">
                    <tr>
                        <th width="40%">User ID:</th>
                        <td><%= order.getUserId() %></td>
                    </tr>
                    <tr>
                        <th>Customer:</th>
                        <td>
                            <% if (order instanceof model.OrderWithUser) { %>
                                <%= ((model.OrderWithUser) order).getUsername() %>
                            <% } else { %>
                                User #<%= order.getUserId() %>
                            <% } %>
                        </td>
                    </tr>
                    <tr>
                        <th>Email:</th>
                        <td>
                            <% if (order instanceof model.OrderWithUser) { %>
                                <%= ((model.OrderWithUser) order).getUserEmail() %>
                            <% } else { %>
                                N/A
                            <% } %>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        
        <hr>
        
        <h6 class="text-primary">Order Items</h6>
        <% if (orderDetails != null && !orderDetails.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-sm">
                    <thead class="table-light">
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        java.math.BigDecimal grandTotal = java.math.BigDecimal.ZERO;
                        for (Map<String, Object> item : orderDetails) { 
                            java.math.BigDecimal subtotal = (java.math.BigDecimal) item.get("subtotal");
                            grandTotal = grandTotal.add(subtotal);
                        %>
                        <tr>
                            <td><%= item.get("productName") %></td>
                            <td><%= item.get("quantity") %></td>
                            <td>$<%= String.format("%.2f", (java.math.BigDecimal) item.get("price")) %></td>
                            <td>$<%= String.format("%.2f", subtotal) %></td>
                        </tr>
                        <% } %>
                    </tbody>
                    <tfoot class="table-light">
                        <tr>
                            <th colspan="3">Total:</th>
                            <th>$<%= String.format("%.2f", grandTotal) %></th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        <% } else { %>
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                No order items available. This might be due to the simplified order system.
            </div>
        <% } %>
        
        <% if ("pending".equals(order.getStatus())) { %>
        <hr>
        <div class="d-flex gap-2">
            <button class="btn btn-success btn-sm" onclick="updateOrderStatusFromModal('<%= order.getOrderId() %>', 'completed')">
                <i class="fas fa-check me-1"></i>Mark as Completed
            </button>
            <button class="btn btn-danger btn-sm" onclick="updateOrderStatusFromModal('<%= order.getOrderId() %>', 'cancelled')">
                <i class="fas fa-times me-1"></i>Cancel Order
            </button>
        </div>
        <% } %>
        
    <% } else { %>
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle me-2"></i>
            Order not found or error loading order details.
        </div>
    <% } %>
</div>

<script>
function updateOrderStatusFromModal(orderId, status) {
    if (confirm('Are you sure you want to change the order status to ' + status + '?')) {
        // Close the modal first
        bootstrap.Modal.getInstance(document.getElementById('orderDetailsModal')).hide();
        
        // Update the status
        updateOrderStatus(orderId, status);
    }
}
</script>