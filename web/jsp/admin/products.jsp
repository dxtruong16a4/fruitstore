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
    <title>Product Management - FruitStore Admin</title>
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
        .product-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
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
                        <a class="nav-link active" href="<%= request.getContextPath() %>/jsp/admin/products.jsp">
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
                    <h2 class="mb-0">
                        <i class="fas fa-apple-alt me-2"></i>Product Management
                    </h2>
                    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addProductModal">
                        <i class="fas fa-plus me-2"></i>Add New Product
                    </button>
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

                <!-- Products Table -->
                <div class="p-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-success">
                                        <tr>
                                            <th>Image</th>
                                            <th>Product ID</th>
                                            <th>Name</th>
                                            <th>Category</th>
                                            <th>Price</th>
                                            <th>Stock</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                        try (Connection conn = DatabaseConnection.getConnection()) {
                                            String sql = "SELECT * FROM products ORDER BY product_id DESC";
                                            try (Statement stmt = conn.createStatement()) {
                                                ResultSet rs = stmt.executeQuery(sql);
                                                while (rs.next()) {
                                                    int productId = rs.getInt("product_id");
                                                    String name = rs.getString("name");
                                                    String category = rs.getString("category");
                                                    double price = rs.getDouble("price");
                                                    int stock = rs.getInt("stock_quantity");
                                                    String imageUrl = rs.getString("image_url");
                                                    String description = rs.getString("description");
                                                    
                                                    String stockStatus = stock > 10 ? "In Stock" : (stock > 0 ? "Low Stock" : "Out of Stock");
                                                    String stockClass = stock > 10 ? "bg-success" : (stock > 0 ? "bg-warning" : "bg-danger");
                                        %>
                                        <tr>
                                            <td>
                                                <img src="<%= request.getContextPath() %>/<%= imageUrl %>" 
                                                     alt="<%= name %>" class="product-img"
                                                     onerror="this.src='https://via.placeholder.com/60x60/28a745/ffffff?text=IMG'">
                                            </td>
                                            <td>#<%= productId %></td>
                                            <td><strong><%= name %></strong></td>
                                            <td><%= category %></td>
                                            <td>$<%= String.format("%.2f", price) %></td>
                                            <td><%= stock %></td>
                                            <td><span class="badge <%= stockClass %>"><%= stockStatus %></span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary edit-btn" 
                                                            data-id="<%= productId %>" 
                                                            data-name="<%= name %>" 
                                                            data-category="<%= category %>" 
                                                            data-price="<%= price %>" 
                                                            data-stock="<%= stock %>"
                                                            data-description="<%= description != null ? description : "" %>"
                                                            data-image-url="<%= imageUrl != null ? imageUrl : "" %>">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success update-stock-btn" 
                                                            data-id="<%= productId %>" 
                                                            data-name="<%= name %>" 
                                                            data-stock="<%= stock %>">
                                                        <i class="fas fa-box"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-danger delete-btn" 
                                                            data-id="<%= productId %>" 
                                                            data-name="<%= name %>">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        } catch (SQLException e) {
                                            out.println("<tr><td colspan='8' class='text-center text-danger'>Error loading products: " + e.getMessage() + "</td></tr>");
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
    
    <!-- Add Product Modal -->
    <div class="modal fade" id="addProductModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="<%= request.getContextPath() %>/admin/products" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add">
                        <div class="mb-3">
                            <label for="productName" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="productName" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="productCategory" class="form-label">Category</label>
                            <select class="form-control" id="productCategory" name="category" required>
                                <option value="">Select Category</option>
                                <option value="Fruits">Fruits</option>
                                <option value="Citrus">Citrus</option>
                                <option value="Tropical">Tropical</option>
                                <option value="Berries">Berries</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="productPrice" class="form-label">Price ($)</label>
                            <input type="number" step="0.01" class="form-control" id="productPrice" name="price" required>
                        </div>
                        <div class="mb-3">
                            <label for="productStock" class="form-label">Stock Quantity</label>
                            <input type="number" class="form-control" id="productStock" name="stock" required>
                        </div>
                        <div class="mb-3">
                            <label for="productDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="productDescription" name="description" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="productImage" class="form-label">Image URL</label>
                            <input type="text" class="form-control" id="productImage" name="imageUrl" 
                                   placeholder="images/fruits/product.jpg">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">Add Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Edit Product Modal -->
    <div class="modal fade" id="editProductModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="<%= request.getContextPath() %>/admin/products" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="productId" id="editProductId">
                        
                        <div class="mb-3">
                            <label for="editProductName" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="editProductName" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="editProductCategory" class="form-label">Category</label>
                            <select class="form-control" id="editProductCategory" name="category" required>
                                <option value="">Select Category</option>
                                <option value="Fruits">Fruits</option>
                                <option value="Citrus">Citrus</option>
                                <option value="Tropical">Tropical</option>
                                <option value="Berries">Berries</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="editProductPrice" class="form-label">Price ($)</label>
                            <input type="number" step="0.01" class="form-control" id="editProductPrice" name="price" required>
                        </div>
                        <div class="mb-3">
                            <label for="editProductStock" class="form-label">Stock Quantity</label>
                            <input type="number" class="form-control" id="editProductStock" name="stock" required>
                        </div>
                        <div class="mb-3">
                            <label for="editProductDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="editProductDescription" name="description" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="editProductImage" class="form-label">Image URL</label>
                            <input type="text" class="form-control" id="editProductImage" name="imageUrl" 
                                   placeholder="images/fruits/product.jpg">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Update Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Update Stock Modal -->
    <div class="modal fade" id="updateStockModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Stock Quantity</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="<%= request.getContextPath() %>/admin/products" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="updateStock">
                        <input type="hidden" name="productId" id="stockProductId">
                        
                        <div class="mb-3">
                            <label class="form-label">Product:</label>
                            <p class="fw-bold" id="stockProductName"></p>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Current Stock:</label>
                            <p class="text-muted" id="currentStock"></p>
                        </div>
                        
                        <div class="mb-3">
                            <label for="stockAction" class="form-label">Action</label>
                            <select class="form-control" id="stockAction" name="stockAction" required>
                                <option value="">Select Action</option>
                                <option value="set">Set New Stock</option>
                                <option value="add">Add to Stock</option>
                                <option value="subtract">Subtract from Stock</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="stockQuantity" class="form-label">Quantity</label>
                            <input type="number" class="form-control" id="stockQuantity" name="quantity" min="0" required>
                            <div class="form-text">Enter the quantity to set, add, or subtract</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">Update Stock</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Edit button handlers
            document.querySelectorAll('.edit-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const name = this.dataset.name;
                    const category = this.dataset.category;
                    const price = this.dataset.price;
                    const stock = this.dataset.stock;
                    const description = this.dataset.description || '';
                    const imageUrl = this.dataset.imageUrl || '';
                    
                    // Populate the edit form
                    document.getElementById('editProductId').value = id;
                    document.getElementById('editProductName').value = name;
                    document.getElementById('editProductCategory').value = category;
                    document.getElementById('editProductPrice').value = price;
                    document.getElementById('editProductStock').value = stock;
                    document.getElementById('editProductDescription').value = description;
                    document.getElementById('editProductImage').value = imageUrl;
                    
                    new bootstrap.Modal(document.getElementById('editProductModal')).show();
                });
            });
            
            // Update stock button handlers
            document.querySelectorAll('.update-stock-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const name = this.dataset.name;
                    const stock = this.dataset.stock;
                    
                    document.getElementById('stockProductId').value = id;
                    document.getElementById('stockProductName').textContent = name;
                    document.getElementById('currentStock').textContent = stock + ' units';
                    
                    new bootstrap.Modal(document.getElementById('updateStockModal')).show();
                });
            });
            
            // Delete button handlers
            document.querySelectorAll('.delete-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const id = this.dataset.id;
                    const name = this.dataset.name;
                    
                    if (confirm('Are you sure you want to delete "' + name + '"?')) {
                        // Create a form and submit it
                        const form = document.createElement('form');
                        form.method = 'POST';
                        form.action = '<%= request.getContextPath() %>/admin/products';
                        
                        const actionInput = document.createElement('input');
                        actionInput.type = 'hidden';
                        actionInput.name = 'action';
                        actionInput.value = 'delete';
                        
                        const idInput = document.createElement('input');
                        idInput.type = 'hidden';
                        idInput.name = 'productId';
                        idInput.value = id;
                        
                        form.appendChild(actionInput);
                        form.appendChild(idInput);
                        document.body.appendChild(form);
                        form.submit();
                    }
                });
            });
            
            // Stock update form submission
            document.getElementById('updateStockForm').addEventListener('submit', function(e) {
                e.preventDefault();
                
                const productId = document.getElementById('stockProductId').value;
                const action = document.getElementById('stockAction').value;
                const quantity = document.getElementById('stockQuantity').value;
                
                if (!quantity || quantity <= 0) {
                    alert('Please enter a valid quantity');
                    return;
                }
                
                // Create a form and submit it
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '<%= request.getContextPath() %>/admin/products';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'updateStock';
                
                const idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'productId';
                idInput.value = productId;
                
                const stockActionInput = document.createElement('input');
                stockActionInput.type = 'hidden';
                stockActionInput.name = 'stockAction';
                stockActionInput.value = action;
                
                const quantityInput = document.createElement('input');
                quantityInput.type = 'hidden';
                quantityInput.name = 'quantity';
                quantityInput.value = quantity;
                
                form.appendChild(actionInput);
                form.appendChild(idInput);
                form.appendChild(stockActionInput);
                form.appendChild(quantityInput);
                document.body.appendChild(form);
                form.submit();
            });
        });
    </script>
</body>
</html>