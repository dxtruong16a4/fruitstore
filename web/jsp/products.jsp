<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - FruitStore</title>
    <link href="../css/styles.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">🍎 FruitStore</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/products">Products</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                                    Cart <span class="badge bg-light text-dark">${cartCount > 0 ? cartCount : 0}</span>
                                </a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Welcome, ${sessionScope.username}
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                                    <c:if test="${sessionScope.role == 'admin'}">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/jsp/admin/dashboard.jsp">Admin Panel</a></li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/jsp/register.jsp">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <!-- Page Header -->
        <div class="row mb-4">
            <div class="col-12">
                <h1 class="text-success">Fresh Fruits</h1>
                <p class="lead">Browse our selection of fresh, organic fruits</p>
            </div>
        </div>

        <!-- Alerts -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty param.success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <!-- Sidebar -->
            <div class="col-lg-3 mb-4">
                <div class="category-filter">
                    <h5>Categories</h5>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/products" class="list-group-item list-group-item-action ${selectedCategory == null ? 'active' : ''}">
                            All Products
                        </a>
                        <c:forEach var="category" items="${categories}">
                            <a href="${pageContext.request.contextPath}/products?category=${category}" 
                               class="list-group-item list-group-item-action ${selectedCategory == category ? 'active' : ''}">
                                ${category}
                            </a>
                        </c:forEach>
                    </div>
                </div>

                <!-- Search -->
                <div class="mt-4">
                    <h5 class="text-success">Search Products</h5>
                    <form action="${pageContext.request.contextPath}/products" method="get" class="search-form">
                        <div class="input-group">
                            <input type="text" class="form-control" name="search" 
                                   placeholder="Search fruits..." value="${searchTerm}">
                            <button class="btn btn-outline-success" type="submit">
                                🔍
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Products Grid -->
            <div class="col-lg-9">
                <c:if test="${not empty searchTerm}">
                    <div class="mb-3">
                        <h6>Search results for: "<strong>${searchTerm}</strong>"</h6>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline-secondary">Clear Search</a>
                    </div>
                </c:if>

                <c:if test="${not empty selectedCategory}">
                    <div class="mb-3">
                        <h6>Category: <strong>${selectedCategory}</strong></h6>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline-secondary">View All</a>
                    </div>
                </c:if>

                <div class="row">
                    <c:choose>
                        <c:when test="${not empty products}">
                            <c:forEach var="product" items="${products}">
                                <div class="col-md-6 col-lg-4 mb-4">
                                    <div class="card product-card h-100">
                                        <img src="../${product.imageUrl}" class="card-img-top" alt="${product.name}"
                                             onerror="this.src='https://via.placeholder.com/200x150/28a745/ffffff?text=${product.name}'">
                                        <div class="card-body d-flex flex-column">
                                            <h5 class="card-title">${product.name}</h5>
                                            <p class="card-text flex-grow-1">${product.description}</p>
                                            <div class="product-info mt-auto">
                                                <div class="price mb-2">
                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                                                </div>
                                                <div class="stock-info mb-3">
                                                    <c:choose>
                                                        <c:when test="${product.stockQuantity > 0}">
                                                            <small class="text-success">✓ In Stock (${product.stockQuantity} available)</small>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <small class="text-danger">✗ Out of Stock</small>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                
                                                <c:if test="${product.stockQuantity > 0}">
                                                    <c:choose>
                                                        <c:when test="${sessionScope.user != null}">
                                                            <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                                                <input type="hidden" name="action" value="add">
                                                                <input type="hidden" name="productId" value="${product.productId}">
                                                                <div class="input-group mb-2">
                                                                    <input type="number" class="form-control form-control-sm" 
                                                                           name="quantity" value="1" min="1" max="${product.stockQuantity}">
                                                                    <button class="btn btn-success btn-sm" type="submit">
                                                                        Add to Cart
                                                                    </button>
                                                                </div>
                                                            </form>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-success btn-sm w-100">
                                                                Login to Purchase
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="col-12">
                                <div class="text-center py-5">
                                    <h3 class="text-muted">No products found</h3>
                                    <p>Try adjusting your search or browse different categories.</p>
                                    <a href="${pageContext.request.contextPath}/products" class="btn btn-success">View All Products</a>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
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
    <script>
        // Add some interactive features
        document.addEventListener('DOMContentLoaded', function() {
            // Add loading animation to add to cart buttons
            const addToCartForms = document.querySelectorAll('.add-to-cart-form');
            addToCartForms.forEach(form => {
                form.addEventListener('submit', function(e) {
                    const button = form.querySelector('button[type="submit"]');
                    button.disabled = true;
                    button.innerHTML = '<span class="spinner-border spinner-border-sm" role="status"></span> Adding...';
                });
            });
        });
    </script>
</body>
</html>