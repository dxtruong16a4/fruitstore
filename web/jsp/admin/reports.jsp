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
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports - FruitStore Admin</title>
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
        .report-card {
            border-radius: 15px;
            border: none;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-bottom: 20px;
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
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/orders.jsp">
                            <i class="fas fa-shopping-cart me-2"></i> Orders
                        </a>
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/users.jsp">
                            <i class="fas fa-users me-2"></i> Users
                        </a>
                        <a class="nav-link active" href="<%= request.getContextPath() %>/jsp/admin/reports.jsp">
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
                        <i class="fas fa-chart-bar me-2"></i>Sales Reports
                    </h2>
                    <button class="btn btn-success" onclick="window.print()">
                        <i class="fas fa-print me-2"></i>Print Report
                    </button>
                </div>
                
                <!-- Reports Content -->
                <div class="p-4">
                    <!-- Sales Summary -->
                    <div class="card report-card">
                        <div class="card-header bg-success text-white">
                            <h5 class="mb-0"><i class="fas fa-chart-line me-2"></i>Sales Summary</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <%
                                double todayRevenue = 0.0;
                                double weeklyRevenue = 0.0;
                                double monthlyRevenue = 0.0;
                                int todayOrders = 0;
                                int weeklyOrders = 0;
                                int monthlyOrders = 0;
                                
                                try (Connection conn = DatabaseConnection.getConnection()) {
                                    // Today's revenue and orders
                                    String todaySql = "SELECT COUNT(*) as orders, COALESCE(SUM(total_amount), 0) as revenue FROM orders WHERE DATE(order_date) = CURDATE()";
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery(todaySql);
                                        if (rs.next()) {
                                            todayOrders = rs.getInt("orders");
                                            todayRevenue = rs.getDouble("revenue");
                                        }
                                    }
                                    
                                    // Weekly revenue and orders
                                    String weeklySql = "SELECT COUNT(*) as orders, COALESCE(SUM(total_amount), 0) as revenue FROM orders WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery(weeklySql);
                                        if (rs.next()) {
                                            weeklyOrders = rs.getInt("orders");
                                            weeklyRevenue = rs.getDouble("revenue");
                                        }
                                    }
                                    
                                    // Monthly revenue and orders
                                    String monthlySql = "SELECT COUNT(*) as orders, COALESCE(SUM(total_amount), 0) as revenue FROM orders WHERE MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery(monthlySql);
                                        if (rs.next()) {
                                            monthlyOrders = rs.getInt("orders");
                                            monthlyRevenue = rs.getDouble("revenue");
                                        }
                                    }
                                } catch (SQLException e) {
                                    out.println("<!-- Error: " + e.getMessage() + " -->");
                                }
                                %>
                                <div class="col-md-4">
                                    <div class="text-center">
                                        <h3 class="text-success">$<%= String.format("%.2f", todayRevenue) %></h3>
                                        <p class="mb-0">Today's Revenue</p>
                                        <small class="text-muted"><%= todayOrders %> orders</small>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="text-center">
                                        <h3 class="text-info">$<%= String.format("%.2f", weeklyRevenue) %></h3>
                                        <p class="mb-0">This Week</p>
                                        <small class="text-muted"><%= weeklyOrders %> orders</small>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="text-center">
                                        <h3 class="text-primary">$<%= String.format("%.2f", monthlyRevenue) %></h3>
                                        <p class="mb-0">This Month</p>
                                        <small class="text-muted"><%= monthlyOrders %> orders</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Top Selling Products -->
                    <div class="card report-card">
                        <div class="card-header bg-info text-white">
                            <h5 class="mb-0"><i class="fas fa-star me-2"></i>Top Selling Products</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th>Rank</th>
                                            <th>Product</th>
                                            <th>Category</th>
                                            <th>Total Sold</th>
                                            <th>Revenue</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                        // This is placeholder data since we don't have order_items table
                                        // In a real application, you would query the order_items table
                                        String[] topProducts = {"Fresh Apples", "Juicy Oranges", "Sweet Bananas", "Fresh Grapes", "Strawberries"};
                                        String[] categories = {"Fruits", "Citrus", "Tropical", "Berries", "Berries"};
                                        int[] quantities = {45, 38, 52, 29, 35};
                                        double[] revenues = {134.55, 132.62, 103.48, 144.71, 209.65};
                                        
                                        for (int i = 0; i < topProducts.length; i++) {
                                        %>
                                        <tr>
                                            <td><span class="badge bg-success">#<%= (i+1) %></span></td>
                                            <td><%= topProducts[i] %></td>
                                            <td><%= categories[i] %></td>
                                            <td><%= quantities[i] %> kg</td>
                                            <td>$<%= String.format("%.2f", revenues[i]) %></td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Order Status Report -->
                    <div class="card report-card">
                        <div class="card-header bg-warning text-dark">
                            <h5 class="mb-0"><i class="fas fa-clipboard-list me-2"></i>Order Status Report</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <%
                                int pendingCount = 0, completedCount = 0, cancelledCount = 0;
                                
                                try (Connection conn = DatabaseConnection.getConnection()) {
                                    // Get order status counts
                                    String statusSql = "SELECT status, COUNT(*) as count FROM orders GROUP BY status";
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery(statusSql);
                                        while (rs.next()) {
                                            String status = rs.getString("status");
                                            int count = rs.getInt("count");
                                            switch (status) {
                                                case "pending": pendingCount = count; break;
                                                case "completed": completedCount = count; break;
                                                case "cancelled": cancelledCount = count; break;
                                            }
                                        }
                                    }
                                } catch (SQLException e) {
                                    out.println("<!-- Error: " + e.getMessage() + " -->");
                                }
                                %>
                                <div class="col-md-4 text-center">
                                    <div class="p-3">
                                        <div class="display-6 text-warning"><%= pendingCount %></div>
                                        <p class="mb-0">Pending Orders</p>
                                    </div>
                                </div>
                                <div class="col-md-4 text-center">
                                    <div class="p-3">
                                        <div class="display-6 text-success"><%= completedCount %></div>
                                        <p class="mb-0">Completed Orders</p>
                                    </div>
                                </div>
                                <div class="col-md-4 text-center">
                                    <div class="p-3">
                                        <div class="display-6 text-danger"><%= cancelledCount %></div>
                                        <p class="mb-0">Cancelled Orders</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Customer Activity -->
                    <div class="card report-card">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="fas fa-users me-2"></i>Customer Activity</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <%
                                int totalCustomers = 0;
                                int newCustomersToday = 0;
                                int activeCustomers = 0;
                                
                                try (Connection conn = DatabaseConnection.getConnection()) {
                                    // Total customers
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'customer'");
                                        if (rs.next()) totalCustomers = rs.getInt(1);
                                    }
                                    
                                    // New customers today
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'customer' AND DATE(created_at) = CURDATE()");
                                        if (rs.next()) newCustomersToday = rs.getInt(1);
                                    }
                                    
                                    // Active customers (with orders)
                                    try (Statement stmt = conn.createStatement()) {
                                        ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT user_id) FROM orders");
                                        if (rs.next()) activeCustomers = rs.getInt(1);
                                    }
                                } catch (SQLException e) {
                                    out.println("<!-- Error: " + e.getMessage() + " -->");
                                }
                                %>
                                <div class="col-md-4">
                                    <div class="text-center p-3">
                                        <h4 class="text-primary"><%= totalCustomers %></h4>
                                        <p class="mb-0">Total Customers</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="text-center p-3">
                                        <h4 class="text-success"><%= newCustomersToday %></h4>
                                        <p class="mb-0">New Today</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="text-center p-3">
                                        <h4 class="text-info"><%= activeCustomers %></h4>
                                        <p class="mb-0">Active Customers</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>