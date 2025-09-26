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
    
    // Get statistics from database
    int totalUsers = 0;
    int totalProducts = 0;
    int totalOrders = 0;
    double totalRevenue = 0.0;
    int pendingOrders = 0;
    
    try (Connection conn = DatabaseConnection.getConnection()) {
        // Count total users
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'customer'");
            if (rs.next()) {
                totalUsers = rs.getInt(1);
            }
        }
        
        // Count total products
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
            if (rs.next()) {
                totalProducts = rs.getInt(1);
            }
        }
        
        // Count total orders and revenue
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*), COALESCE(SUM(total_amount), 0) FROM orders");
            if (rs.next()) {
                totalOrders = rs.getInt(1);
                totalRevenue = rs.getDouble(2);
            }
        }
        
        // Count pending orders
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders WHERE status = 'pending'");
            if (rs.next()) {
                pendingOrders = rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        out.println("<!-- Database error: " + e.getMessage() + " -->");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - FruitStore</title>
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
        .stat-card {
            border-radius: 15px;
            border: none;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
        }
        .chart-container {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
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
                        <a class="nav-link active" href="#dashboard">
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
                    <h2 class="mb-0">Dashboard</h2>
                    <div class="d-flex align-items-center">
                        <span class="me-3">Welcome, <%= username %></span>
                        <div class="dropdown">
                            <button class="btn btn-outline-success dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                <i class="fas fa-user"></i>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="<%= request.getContextPath() %>/login?action=logout">Logout</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <!-- Statistics Cards -->
                <div class="p-4">
                    <div class="row mb-4">
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card stat-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="stat-icon bg-primary me-3">
                                        <i class="fas fa-users"></i>
                                    </div>
                                    <div>
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Total Customers</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalUsers %></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card stat-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="stat-icon bg-success me-3">
                                        <i class="fas fa-apple-alt"></i>
                                    </div>
                                    <div>
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Total Products</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalProducts %></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card stat-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="stat-icon bg-info me-3">
                                        <i class="fas fa-shopping-cart"></i>
                                    </div>
                                    <div>
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">Total Orders</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalOrders %></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card stat-card h-100">
                                <div class="card-body d-flex align-items-center">
                                    <div class="stat-icon bg-warning me-3">
                                        <i class="fas fa-dollar-sign"></i>
                                    </div>
                                    <div>
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Total Revenue</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">$<%= String.format("%.2f", totalRevenue) %></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Recent Orders and Quick Actions -->
                    <div class="row">
                        <!-- Recent Orders -->
                        <div class="col-lg-8 mb-4">
                            <div class="chart-container">
                                <h5 class="mb-3">
                                    <i class="fas fa-clock me-2"></i>Recent Orders
                                    <% if (pendingOrders > 0) { %>
                                        <span class="badge bg-warning ms-2"><%= pendingOrders %> pending</span>
                                    <% } %>
                                </h5>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Order ID</th>
                                                <th>Customer</th>
                                                <th>Amount</th>
                                                <th>Status</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                            try (Connection conn = DatabaseConnection.getConnection()) {
                                                String sql = "SELECT o.order_id, u.username, o.total_amount, o.status, o.order_date " +
                                                           "FROM orders o JOIN users u ON o.user_id = u.user_id " +
                                                           "ORDER BY o.order_date DESC LIMIT 5";
                                                try (Statement stmt = conn.createStatement()) {
                                                    ResultSet rs = stmt.executeQuery(sql);
                                                    while (rs.next()) {
                                                        String statusClass = "";
                                                        switch (rs.getString("status")) {
                                                            case "pending": statusClass = "bg-warning"; break;
                                                            case "completed": statusClass = "bg-success"; break;
                                                            case "cancelled": statusClass = "bg-danger"; break;
                                                            default: statusClass = "bg-secondary";
                                                        }
                                            %>
                                            <tr>
                                                <td>#<%= rs.getInt("order_id") %></td>
                                                <td><%= rs.getString("username") %></td>
                                                <td>$<%= String.format("%.2f", rs.getDouble("total_amount")) %></td>
                                                <td><span class="badge <%= statusClass %>"><%= rs.getString("status") %></span></td>
                                                <td><%= rs.getTimestamp("order_date") %></td>
                                            </tr>
                                            <%
                                                    }
                                                }
                                            } catch (SQLException e) {
                                                out.println("<tr><td colspan='5'>Error loading orders: " + e.getMessage() + "</td></tr>");
                                            }
                                            %>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="text-end">
                                    <a href="<%= request.getContextPath() %>/jsp/admin/orders.jsp" class="btn btn-primary">View All Orders</a>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Quick Actions -->
                        <div class="col-lg-4 mb-4">
                            <div class="chart-container">
                                <h5 class="mb-3">
                                    <i class="fas fa-bolt me-2"></i>Quick Actions
                                </h5>
                                <div class="d-grid gap-2">
                                    <a href="<%= request.getContextPath() %>/jsp/admin/add-product.jsp" class="btn btn-success">
                                        <i class="fas fa-plus me-2"></i>Add New Product
                                    </a>
                                    <a href="<%= request.getContextPath() %>/jsp/admin/orders.jsp?status=pending" class="btn btn-warning">
                                        <i class="fas fa-clock me-2"></i>View Pending Orders
                                    </a>
                                    <a href="<%= request.getContextPath() %>/jsp/admin/users.jsp" class="btn btn-info">
                                        <i class="fas fa-user-plus me-2"></i>Manage Users
                                    </a>
                                    <a href="<%= request.getContextPath() %>/jsp/admin/reports.jsp" class="btn btn-primary">
                                        <i class="fas fa-chart-line me-2"></i>Sales Report
                                    </a>
                                </div>
                                
                                <!-- System Status -->
                                <hr>
                                <h6 class="mb-3">System Status</h6>
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span>Database</span>
                                    <span class="badge bg-success">Connected</span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span>Server</span>
                                    <span class="badge bg-success">Running</span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span>Last Update</span>
                                    <small class="text-muted">Just now</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Auto-refresh page every 5 minutes
        setTimeout(function() {
            location.reload();
        }, 300000);
        
        // Add active class to current nav item
        document.addEventListener('DOMContentLoaded', function() {
            const navLinks = document.querySelectorAll('.sidebar .nav-link');
            navLinks.forEach(link => {
                if (link.href === window.location.href) {
                    link.classList.add('active');
                }
            });
        });
    </script>
</body>
</html>