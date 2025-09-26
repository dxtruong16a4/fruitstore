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
    <title>User Management - FruitStore Admin</title>
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
                        <a class="nav-link" href="<%= request.getContextPath() %>/jsp/admin/orders.jsp">
                            <i class="fas fa-shopping-cart me-2"></i> Orders
                        </a>
                        <a class="nav-link active" href="<%= request.getContextPath() %>/jsp/admin/users.jsp">
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
                        <i class="fas fa-users me-2"></i>User Management
                    </h2>
                </div>
                
                <!-- Users Table -->
                <div class="p-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-success">
                                        <tr>
                                            <th>User ID</th>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Joined Date</th>
                                            <th>Total Orders</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                        try (Connection conn = DatabaseConnection.getConnection()) {
                                            String sql = "SELECT u.user_id, u.username, u.email, u.role, u.created_at, " +
                                                       "COUNT(o.order_id) as total_orders " +
                                                       "FROM users u LEFT JOIN orders o ON u.user_id = o.user_id " +
                                                       "GROUP BY u.user_id, u.username, u.email, u.role, u.created_at " +
                                                       "ORDER BY u.created_at DESC";
                                            
                                            try (Statement stmt = conn.createStatement()) {
                                                ResultSet rs = stmt.executeQuery(sql);
                                                while (rs.next()) {
                                                    int userId = rs.getInt("user_id");
                                                    String userName = rs.getString("username");
                                                    String email = rs.getString("email");
                                                    String userRole = rs.getString("role");
                                                    Timestamp createdAt = rs.getTimestamp("created_at");
                                                    int totalOrders = rs.getInt("total_orders");
                                                    
                                                    String roleClass = "admin".equals(userRole) ? "bg-danger" : "bg-primary";
                                        %>
                                        <tr>
                                            <td><strong>#<%= userId %></strong></td>
                                            <td><%= userName %></td>
                                            <td><%= email %></td>
                                            <td><span class="badge <%= roleClass %>"><%= userRole %></span></td>
                                            <td><%= createdAt %></td>
                                            <td><%= totalOrders %></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-info view-user-btn" 
                                                            data-id="<%= userId %>" 
                                                            data-username="<%= userName %>" 
                                                            data-email="<%= email %>" 
                                                            data-role="<%= userRole %>">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                    <% if (!"admin".equals(userRole)) { %>
                                                    <button class="btn btn-sm btn-outline-warning promote-btn" 
                                                            data-id="<%= userId %>" 
                                                            data-username="<%= userName %>">
                                                        <i class="fas fa-arrow-up"></i>
                                                    </button>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        } catch (SQLException e) {
                                            out.println("<tr><td colspan='7' class='text-center text-danger'>Error loading users: " + e.getMessage() + "</td></tr>");
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
    
    <!-- User Details Modal -->
    <div class="modal fade" id="userDetailsModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">User Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="userDetailsContent">
                    <!-- User details will be loaded here -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // View user button handlers
            document.querySelectorAll('.view-user-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const username = this.dataset.username;
                    const email = this.dataset.email;
                    const role = this.dataset.role;
                    
                    document.getElementById('userDetailsContent').innerHTML = `
                        <div class="row">
                            <div class="col-sm-4"><strong>User ID:</strong></div>
                            <div class="col-sm-8">#${id}</div>
                        </div>
                        <div class="row mt-2">
                            <div class="col-sm-4"><strong>Username:</strong></div>
                            <div class="col-sm-8">${username}</div>
                        </div>
                        <div class="row mt-2">
                            <div class="col-sm-4"><strong>Email:</strong></div>
                            <div class="col-sm-8">${email}</div>
                        </div>
                        <div class="row mt-2">
                            <div class="col-sm-4"><strong>Role:</strong></div>
                            <div class="col-sm-8"><span class="badge ${role === 'admin' ? 'bg-danger' : 'bg-primary'}">${role}</span></div>
                        </div>
                    `;
                    new bootstrap.Modal(document.getElementById('userDetailsModal')).show();
                });
            });
            
            // Promote to admin button handlers
            document.querySelectorAll('.promote-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const username = this.dataset.username;
                    
                    if (confirm('Promote "' + username + '" to admin role?')) {
                        // Create a form and submit it
                        const form = document.createElement('form');
                        form.method = 'POST';
                        form.action = '<%= request.getContextPath() %>/admin/users';
                        
                        const actionInput = document.createElement('input');
                        actionInput.type = 'hidden';
                        actionInput.name = 'action';
                        actionInput.value = 'promote';
                        
                        const idInput = document.createElement('input');
                        idInput.type = 'hidden';
                        idInput.name = 'userId';
                        idInput.value = id;
                        
                        form.appendChild(actionInput);
                        form.appendChild(idInput);
                        document.body.appendChild(form);
                        form.submit();
                    }
                });
            });
        });
    </script>
</body>
</html>