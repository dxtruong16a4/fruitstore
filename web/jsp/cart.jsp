<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - FruitStore</title>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/cart">Cart</a>
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
                <h1 class="text-success">🛒 Shopping Cart</h1>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
                        <li class="breadcrumb-item active">Cart</li>
                    </ol>
                </nav>
            </div>
        </div>

        <!-- Cart Content -->
        <div class="row">
            <div class="col-lg-8">
                <c:choose>
                    <c:when test="${not empty cartItems}">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">Cart Items (${cartCount} items)</h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover mb-0">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Product</th>
                                                <th>Price</th>
                                                <th>Quantity</th>
                                                <th>Total</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${cartItems}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <img src="${pageContext.request.contextPath}/${item.product.imageUrl}" 
                                                                 alt="${item.product.name}" class="me-3" 
                                                                 style="width: 60px; height: 60px; object-fit: cover; border-radius: 5px;"
                                                                 onerror="this.src='https://via.placeholder.com/60x60/28a745/ffffff?text=Fruit'">
                                                            <div>
                                                                <h6 class="mb-0">${item.product.name}</h6>
                                                                <small class="text-muted">${item.product.category}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="$"/>
                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="cartId" value="${item.cartId}">
                                                            <div class="input-group" style="width: 120px;">
                                                                <input type="number" class="form-control form-control-sm" 
                                                                       name="quantity" value="${item.quantity}" 
                                                                       min="1" max="${item.product.stockQuantity}">
                                                                <button class="btn btn-outline-secondary btn-sm" type="submit">
                                                                    Update
                                                                </button>
                                                            </div>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <strong>
                                                            <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency" currencySymbol="$"/>
                                                        </strong>
                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                            <input type="hidden" name="action" value="remove">
                                                            <input type="hidden" name="cartId" value="${item.cartId}">
                                                            <button class="btn btn-outline-danger btn-sm" type="submit" 
                                                                    onclick="return confirm('Remove this item from cart?')">
                                                                🗑️ Remove
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <div class="mb-4">
                                <i class="display-1">🛒</i>
                            </div>
                            <h3 class="text-muted">Your cart is empty</h3>
                            <p>Add some delicious fruits to your cart to get started!</p>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-success btn-lg">
                                🍎 Browse Products
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Cart Summary -->
            <div class="col-lg-4">
                <c:if test="${not empty cartItems}">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">Order Summary</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Subtotal:</span>
                                <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$"/></span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Shipping:</span>
                                <span class="text-success">FREE</span>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between mb-3">
                                <strong>Total:</strong>
                                <strong class="text-success">
                                    <fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$"/>
                                </strong>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <form action="${pageContext.request.contextPath}/orders" method="post">
                                    <input type="hidden" name="action" value="checkout">
                                    <button type="submit" class="btn btn-success btn-lg w-100">
                                        🛒 Proceed to Checkout
                                    </button>
                                </form>
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-success">
                                    Continue Shopping
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Promo Code Section -->
                    <div class="card mt-3">
                        <div class="card-body">
                            <h6>Have a promo code?</h6>
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Enter promo code">
                                <button class="btn btn-outline-secondary" type="button">Apply</button>
                            </div>
                        </div>
                    </div>
                </c:if>
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
        // Auto-submit quantity updates after a short delay
        document.addEventListener('DOMContentLoaded', function() {
            const quantityInputs = document.querySelectorAll('input[name="quantity"]');
            quantityInputs.forEach(input => {
                input.addEventListener('change', function() {
                    // Optional: Auto-submit after quantity change
                    // this.form.submit();
                });
            });
        });
    </script>
</body>
</html>