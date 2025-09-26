<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="util.DatabaseConnection" %>
<%
    // Check if user is logged in and is admin
    String role = (String) session.getAttribute("role");
    String username = (String) session.getAttribute("username");
    
    if (role == null || !"admin".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String statusFilter = request.getParameter("status");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management - FruitStore Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #28a745, #20c997);
        }
        .sidebar .nav-link {
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            margin: 5px 10px;
            transition: all 0.3s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background: rgba(255,255,255,0.2);
            color: white;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 sidebar">
                <div class="p-3">
                    <h4 class="text-white mb-4">
                        <i class="fas fa-store"></i> FruitStore Admin
                    </h4>
                    <nav class="nav flex-column">
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-2"></i> Dashboard
                        </a>
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/products.jsp">
                            <i class="fas fa-apple-alt me-2"></i> Products
                        </a>
                        <a class="nav-link active" href="<%= request.getContextPath() %>/jsp/admin/orders.jsp">
                            <i class="fas fa-shopping-cart me-2"></i> Orders
                        </a>
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/users.jsp">
                            <i class="fas fa-users me-2"></i> Users
                        </a>
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/reports.jsp">
                            <i class="fas fa-chart-bar me-2"></i> Reports
                        </a>
                        <hr class="bg-white">
                        <a class="nav-link" href="<%= request.getContextPath() %>/index.jsp">
                            <i class="fas fa-home me-2"></i> Back to Store
                        </a>
                        <a class="nav-link" href="<%= request.getContextPath() %>/login?action=logout">
                            <i class="fas fa-sign-out-alt me-2"></i> Logout
                        </a>
                    </nav>
                </div>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-9 col-lg-10">
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center py-3 px-4 bg-white shadow-sm">
                    <h2 class="mb-0">
                        <i class="fas fa-shopping-cart me-2"></i>Order Management
                    </h2>
                    <div class="d-flex gap-2">
                        <select class="form-select" onchange="filterOrders(this.value)">
                            <option value="">All Orders</option>
                            <option value="pending" <%= "pending".equals(statusFilter) ? "selected" : "" %>>Pending</option>
                            <option value="completed" <%= "completed".equals(statusFilter) ? "selected" : "" %>>Completed</option>
                            <option value="cancelled" <%= "cancelled".equals(statusFilter) ? "selected" : "" %>>Cancelled</option>
                        </select>
                    </div>
                </div>
                
                <!-- Success/Error Messages -->
                <div class="p-4 pb-0">
                    <% String successMessage = (String) request.getAttribute("successMessage"); %>
                    <% if (successMessage != null) { %>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fas fa-check-circle me-2"></i><%= successMessage %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    <% } %>

                    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                    <% if (errorMessage != null) { %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i><%= errorMessage %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    <% } %>
                </div>

                <!-- Orders Table -->
                <div class="p-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-success">
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Customer</th>
                                            <th>Total Amount</th>
                                            <th>Status</th>
                                            <th>Order Date</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                        try (Connection conn = DatabaseConnection.getConnection()) {
                                            String sql = "SELECT o.order_id, u.username, u.email, o.total_amount, o.status, o.order_date " +
                                                       "FROM orders o JOIN users u ON o.user_id = u.user_id";
                                            
                                            if (statusFilter != null && !statusFilter.isEmpty()) {
                                                sql += " WHERE o.status = ?";
                                            }
                                            
                                            sql += " ORDER BY o.order_date DESC";
                                            
                                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                                if (statusFilter != null && !statusFilter.isEmpty()) {
                                                    pstmt.setString(1, statusFilter);
                                                }
                                                
                                                ResultSet rs = pstmt.executeQuery();
                                                while (rs.next()) {
                                                    int orderId = rs.getInt("order_id");
                                                    String customerName = rs.getString("username");
                                                    String email = rs.getString("email");
                                                    double totalAmount = rs.getDouble("total_amount");
                                                    String orderStatus = rs.getString("status");
                                                    Timestamp orderDate = rs.getTimestamp("order_date");
                                                    
                                                    String statusClass = "";
                                                    switch (orderStatus) {
                                                        case "pending": statusClass = "bg-warning"; break;
                                                        case "completed": statusClass = "bg-success"; break;
                                                        case "cancelled": statusClass = "bg-danger"; break;
                                                        default: statusClass = "bg-secondary";
                                                    }
                                        %>
                                        <tr>
                                            <td><strong>#<%= orderId %></strong></td>
                                            <td>
                                                <div>
                                                    <strong><%= customerName %></strong><br>
                                                    <small class="text-muted"><%= email %></small>
                                                </div>
                                            </td>
                                            <td><strong>$<%= String.format("%.2f", totalAmount) %></strong></td>
                                            <td><span class="badge <%= statusClass %>"><%= orderStatus %></span></td>
                                            <td><%= orderDate %></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-info view-details-btn" 
                                                            data-id="<%= orderId %>">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                    <% if ("pending".equals(orderStatus)) { %>
                                                    <button class="btn btn-sm btn-outline-success complete-btn" 
                                                            data-id="<%= orderId %>">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-danger cancel-btn" 
                                                            data-id="<%= orderId %>">
                                                        <i class="fas fa-times"></i>
                                                    </button>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        } catch (SQLException e) {
                                            out.println("<tr><td colspan='6' class='text-center text-danger'>Error loading orders: " + e.getMessage() + "</td></tr>");
                                        }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Order Details Modal -->
    <div class="modal fade" id="orderDetailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Order Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="orderDetailsContent">
                    <!-- Order details will be loaded here -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function filterOrders(status) {
            const url = new URL(window.location);
            if (status) {
                url.searchParams.set('status', status);
            } else {
                url.searchParams.delete('status');
            }
            window.location = url;
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            // View details button handlers
            document.querySelectorAll('.view-details-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const orderId = this.dataset.id;
                    loadOrderDetails(orderId);
                });
            });
            
            // Complete button handlers
            document.querySelectorAll('.complete-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const orderId = this.dataset.id;
                    if (confirm('Mark order #' + orderId + ' as completed?')) {
                        updateOrderStatus(orderId, 'completed');
                    }
                });
            });
            
            // Cancel button handlers
            document.querySelectorAll('.cancel-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const orderId = this.dataset.id;
                    if (confirm('Cancel order #' + orderId + '?')) {
                        updateOrderStatus(orderId, 'cancelled');
                    }
                });
            });
        });
        
        function updateOrderStatus(orderId, status) {
            // Create a form and submit it
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '<%= request.getContextPath() %>/admin/orders';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'updateStatus';
            
            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'orderId';
            idInput.value = orderId;
            
            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'status';
            statusInput.value = status;
            
            form.appendChild(actionInput);
            form.appendChild(idInput);
            form.appendChild(statusInput);
            document.body.appendChild(form);
            form.submit();
        }
        
        function loadOrderDetails(orderId) {
            // Show loading state
            document.getElementById('orderDetailsContent').innerHTML = 
                '<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Loading order details...</div>';
            new bootstrap.Modal(document.getElementById('orderDetailsModal')).show();
            
            // Load order details via AJAX
            fetch('<%= request.getContextPath() %>/admin/orders?action=getOrderDetails&orderId=' + orderId)
                .then(response => response.text())
                .then(html => {
                    // Extract the content from the response and update modal
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const content = doc.querySelector('.order-details-content');
                    if (content) {
                        document.getElementById('orderDetailsContent').innerHTML = content.innerHTML;
                    } else {
                        document.getElementById('orderDetailsContent').innerHTML = 
                            '<div class="alert alert-danger">Error loading order details</div>';
                    }
                })
                .catch(error => {
                    document.getElementById('orderDetailsContent').innerHTML = 
                        '<div class="alert alert-danger">Error loading order details: ' + error.message + '</div>';
                });
        }
    </script>
</body>
</html>