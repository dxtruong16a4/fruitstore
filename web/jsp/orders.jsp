<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderWithUser" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - FruitStore</title>
    <link href="../css/styles.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%
        // Get session attributes
        Object userObj = session.getAttribute("user");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        boolean isLoggedIn = (userObj != null);
        boolean isAdmin = "admin".equals(role);
        
        // Get orders and isAdmin from request attributes
        List<Order> orders = (List<Order>) request.getAttribute("orders");
        Boolean isAdminAttr = (Boolean) request.getAttribute("isAdmin");
        if (isAdminAttr != null) {
            isAdmin = isAdminAttr.booleanValue();
        }
        
        // Get error and success messages
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        
        // Format helpers
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    %>
    
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/index.jsp">🍎 FruitStore</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/index.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/products">Products</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <% if (isLoggedIn) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= request.getContextPath() %>/cart">Cart</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                Welcome, <%= username %>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item active" href="<%= request.getContextPath() %>/orders">My Orders</a></li>
                                <% if ("admin".equals(role)) { %>
                                    <li><a class="dropdown-item" href="<%= request.getContextPath() %>/jsp/admin/dashboard.jsp">Admin Panel</a></li>
                                <% } %>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="<%= request.getContextPath() %>/login?action=logout">Logout</a></li>
                            </ul>
                        </li>
                    <% } else { %>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= request.getContextPath() %>/login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= request.getContextPath() %>/jsp/register.jsp">Register</a>
                        </li>
                    <% } %>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <!-- Page Header -->
        <div class="row mb-4">
            <div class="col-12">
                <h1 class="text-success">
                    <%= isAdmin ? "All Orders" : "My Orders" %>
                </h1>
                <p class="lead">
                    <%= isAdmin ? "Manage all customer orders" : "View your order history and track deliveries" %>
                </p>
            </div>
        </div>

        <!-- Alerts -->
        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        
        <% if (success != null && !success.isEmpty()) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <!-- Orders List -->
        <div class="row">
            <div class="col-12">
                <% if (orders != null && !orders.isEmpty()) { %>
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">Orders</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead class="table-success">
                                        <tr>
                                            <th>Order ID</th>
                                            <% if (isAdmin) { %>
                                                <th>Customer</th>
                                            <% } %>
                                            <th>Date</th>
                                            <th>Total Amount</th>
                                            <th>Status</th>
                                            <% if (isAdmin) { %>
                                                <th>Actions</th>
                                            <% } %>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Order order : orders) { %>
                                            <tr>
                                                <td>
                                                    <strong>#<%= order.getOrderId() %></strong>
                                                </td>
                                                <% if (isAdmin) { %>
                                                    <td>
                                                        <%
                                                            if (order instanceof OrderWithUser) {
                                                                OrderWithUser orderWithUser = (OrderWithUser) order;
                                                                out.print(orderWithUser.getUsername() != null ? orderWithUser.getUsername() : "User #" + order.getUserId());
                                                            } else {
                                                                out.print("User #" + order.getUserId());
                                                            }
                                                        %>
                                                    </td>
                                                <% } %>
                                                <td>
                                                    <%= dateFormat.format(order.getOrderDate()) %>
                                                </td>
                                                <td>
                                                    <span class="fw-bold text-success">
                                                        <%= currencyFormat.format(order.getTotalAmount()) %>
                                                    </span>
                                                </td>
                                                <td>
                                                    <%
                                                        String status = order.getStatus();
                                                        String badgeClass = "bg-secondary";
                                                        if ("pending".equals(status)) {
                                                            badgeClass = "bg-warning";
                                                        } else if ("processing".equals(status)) {
                                                            badgeClass = "bg-info";
                                                        } else if ("shipped".equals(status)) {
                                                            badgeClass = "bg-primary";
                                                        } else if ("delivered".equals(status) || "completed".equals(status)) {
                                                            badgeClass = "bg-success";
                                                        } else if ("cancelled".equals(status)) {
                                                            badgeClass = "bg-danger";
                                                        }
                                                    %>
                                                    <span class="badge <%= badgeClass %>">
                                                        <%= status.substring(0, 1).toUpperCase() + status.substring(1) %>
                                                    </span>
                                                </td>
                                                <% if (isAdmin) { %>
                                                    <td>
                                                        <% if (!"delivered".equals(status) && !"completed".equals(status) && !"cancelled".equals(status)) { %>
                                                            <div class="btn-group" role="group">
                                                                <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle" 
                                                                        data-bs-toggle="dropdown">
                                                                    Update Status
                                                                </button>
                                                                <ul class="dropdown-menu">
                                                                    <% if (!"processing".equals(status)) { %>
                                                                        <li>
                                                                            <form action="<%= request.getContextPath() %>/orders" method="post" class="d-inline">
                                                                                <input type="hidden" name="action" value="updateStatus">
                                                                                <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                                                                <input type="hidden" name="status" value="processing">
                                                                                <button type="submit" class="dropdown-item">Mark Processing</button>
                                                                            </form>
                                                                        </li>
                                                                    <% } %>
                                                                    <% if (!"shipped".equals(status)) { %>
                                                                        <li>
                                                                            <form action="<%= request.getContextPath() %>/orders" method="post" class="d-inline">
                                                                                <input type="hidden" name="action" value="updateStatus">
                                                                                <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                                                                <input type="hidden" name="status" value="shipped">
                                                                                <button type="submit" class="dropdown-item">Mark Shipped</button>
                                                                            </form>
                                                                        </li>
                                                                    <% } %>
                                                                    <li>
                                                                        <form action="<%= request.getContextPath() %>/orders" method="post" class="d-inline">
                                                                            <input type="hidden" name="action" value="updateStatus">
                                                                            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                                                            <input type="hidden" name="status" value="completed">
                                                                            <button type="submit" class="dropdown-item">Mark Completed</button>
                                                                        </form>
                                                                    </li>
                                                                    <li><hr class="dropdown-divider"></li>
                                                                    <li>
                                                                        <form action="<%= request.getContextPath() %>/orders" method="post" class="d-inline">
                                                                            <input type="hidden" name="action" value="updateStatus">
                                                                            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                                                            <input type="hidden" name="status" value="cancelled">
                                                                            <button type="submit" class="dropdown-item text-danger">Cancel Order</button>
                                                                        </form>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        <% } %>
                                                    </td>
                                                <% } %>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <% } else { %>
                    <div class="text-center py-5">
                        <div class="mb-4">
                            <i class="display-1 text-muted">📦</i>
                        </div>
                        <h3 class="text-muted">No Orders Found</h3>
                        <% if (isAdmin) { %>
                            <p>No customer orders have been placed yet.</p>
                        <% } else { %>
                            <p>You haven't placed any orders yet. Start shopping to see your orders here!</p>
                            <a href="<%= request.getContextPath() %>/products" class="btn btn-success btn-lg mt-3">
                                Start Shopping
                            </a>
                        <% } %>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>🍎 FruitStore</h5>
                    <p>Your trusted partner for fresh, organic fruits delivered to your doorstep.</p>
                </div>
                <div class="col-md-6">
                    <h5>Contact Us</h5>
                    <p>📞 1-800-FRUITS<br>
                       📧 info@fruitstore.com<br>
                       📍 123 Fresh Street, Fruit City</p>
                </div>
            </div>
            <hr>
            <div class="text-center">
                <p>&copy; 2024 FruitStore. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>