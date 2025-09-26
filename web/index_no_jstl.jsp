<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FruitStore - Welcome</title>
    <link href="css/styles.css" rel="stylesheet">
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
                        <a class="nav-link active" href="<%= request.getContextPath() %>/index.jsp">Home</a>
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
                                <li><a class="dropdown-item" href="<%= request.getContextPath() %>/orders">My Orders</a></li>
                                <% if (isAdmin) { %>
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

    <!-- Hero Section -->
    <div class="hero-section bg-light py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold text-success">Fresh Fruits Delivered</h1>
                    <p class="lead">Get the freshest, highest quality fruits delivered right to your doorstep. 
                       From crisp apples to juicy oranges, we have everything you need for a healthy lifestyle.</p>
                    <a href="<%= request.getContextPath() %>/products" class="btn btn-success btn-lg">Shop Now</a>
                </div>
                <div class="col-lg-6">
                    <img src="images/fruits/hero-fruits.jpg" alt="Fresh Fruits" class="img-fluid rounded" 
                         onerror="this.src='https://via.placeholder.com/500x300/28a745/ffffff?text=Fresh+Fruits'">
                </div>
            </div>
        </div>
    </div>

    <!-- Features Section -->
    <div class="container py-5">
        <div class="row">
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100 border-0">
                    <div class="card-body">
                        <div class="mb-3">
                            <i class="display-4 text-success">🚚</i>
                        </div>
                        <h5 class="card-title">Fast Delivery</h5>
                        <p class="card-text">Same-day delivery available for orders placed before 2 PM.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100 border-0">
                    <div class="card-body">
                        <div class="mb-3">
                            <i class="display-4 text-success">🌱</i>
                        </div>
                        <h5 class="card-title">Organic & Fresh</h5>
                        <p class="card-text">All our fruits are sourced from local organic farms.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 text-center mb-4">
                <div class="card h-100 border-0">
                    <div class="card-body">
                        <div class="mb-3">
                            <i class="display-4 text-success">💰</i>
                        </div>
                        <h5 class="card-title">Best Prices</h5>
                        <p class="card-text">Competitive prices with regular discounts and offers.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Popular Products Section -->
    <div class="bg-light py-5">
        <div class="container">
            <h2 class="text-center mb-5">Popular Fruits</h2>
            <div class="row">
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="images/fruits/apple.jpg" class="card-img-top" alt="Apple"
                             onerror="this.src='https://via.placeholder.com/200x150/28a745/ffffff?text=Apple'">
                        <div class="card-body text-center">
                            <h5 class="card-title">Fresh Apples</h5>
                            <p class="text-success fw-bold">$2.99/kg</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="images/fruits/orange.jpg" class="card-img-top" alt="Orange"
                             onerror="this.src='https://via.placeholder.com/200x150/ff6b35/ffffff?text=Orange'">
                        <div class="card-body text-center">
                            <h5 class="card-title">Juicy Oranges</h5>
                            <p class="text-success fw-bold">$3.49/kg</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="images/fruits/banana.jpg" class="card-img-top" alt="Banana"
                             onerror="this.src='https://via.placeholder.com/200x150/ffdd44/000000?text=Banana'">
                        <div class="card-body text-center">
                            <h5 class="card-title">Sweet Bananas</h5>
                            <p class="text-success fw-bold">$1.99/kg</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="images/fruits/grape.jpg" class="card-img-top" alt="Grape"
                             onerror="this.src='https://via.placeholder.com/200x150/8e44ad/ffffff?text=Grapes'">
                        <div class="card-body text-center">
                            <h5 class="card-title">Fresh Grapes</h5>
                            <p class="text-success fw-bold">$4.99/kg</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="text-center mt-4">
                <a href="<%= request.getContextPath() %>/products" class="btn btn-success btn-lg">View All Products</a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4">
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