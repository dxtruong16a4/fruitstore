<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/cart">Cart</a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Welcome, ${sessionScope.username}
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item active" href="${pageContext.request.contextPath}/orders">My Orders</a></li>
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
                <h1 class="text-success">
                    <c:choose>
                        <c:when test="${isAdmin}">All Orders</c:when>
                        <c:otherwise>My Orders</c:otherwise>
                    </c:choose>
                </h1>
                <p class="lead">
                    <c:choose>
                        <c:when test="${isAdmin}">Manage all customer orders</c:when>
                        <c:otherwise>View your order history and track deliveries</c:otherwise>
                    </c:choose>
                </p>
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

        <!-- Orders List -->
        <div class="row">
            <div class="col-12">
                <c:choose>
                    <c:when test="${not empty orders}">
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
                                                <c:if test="${isAdmin}">
                                                    <th>Customer</th>
                                                </c:if>
                                                <th>Date</th>
                                                <th>Total Amount</th>
                                                <th>Status</th>
                                                <c:if test="${isAdmin}">
                                                    <th>Actions</th>
                                                </c:if>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="order" items="${orders}">
                                                <tr>
                                                    <td>
                                                        <strong>#${order.orderId}</strong>
                                                    </td>
                                                    <c:if test="${isAdmin}">
                                                        <td>${order.user.username}</td>
                                                    </c:if>
                                                    <td>
                                                        <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm"/>
                                                    </td>
                                                    <td>
                                                        <span class="fw-bold text-success">
                                                            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.status == 'pending'}">
                                                                <span class="badge bg-warning">Pending</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'processing'}">
                                                                <span class="badge bg-info">Processing</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'shipped'}">
                                                                <span class="badge bg-primary">Shipped</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'delivered'}">
                                                                <span class="badge bg-success">Delivered</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'cancelled'}">
                                                                <span class="badge bg-danger">Cancelled</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${order.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <c:if test="${isAdmin}">
                                                        <td>
                                                            <c:if test="${order.status != 'delivered' && order.status != 'cancelled'}">
                                                                <div class="btn-group" role="group">
                                                                    <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle" 
                                                                            data-bs-toggle="dropdown">
                                                                        Update Status
                                                                    </button>
                                                                    <ul class="dropdown-menu">
                                                                        <c:if test="${order.status != 'processing'}">
                                                                            <li>
                                                                                <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                                                                    <input type="hidden" name="action" value="updateStatus">
                                                                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                                                                    <input type="hidden" name="status" value="processing">
                                                                                    <button type="submit" class="dropdown-item">Mark Processing</button>
                                                                                </form>
                                                                            </li>
                                                                        </c:if>
                                                                        <c:if test="${order.status != 'shipped'}">
                                                                            <li>
                                                                                <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                                                                    <input type="hidden" name="action" value="updateStatus">
                                                                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                                                                    <input type="hidden" name="status" value="shipped">
                                                                                    <button type="submit" class="dropdown-item">Mark Shipped</button>
                                                                                </form>
                                                                            </li>
                                                                        </c:if>
                                                                        <li>
                                                                            <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                                                                <input type="hidden" name="action" value="updateStatus">
                                                                                <input type="hidden" name="orderId" value="${order.orderId}">
                                                                                <input type="hidden" name="status" value="delivered">
                                                                                <button type="submit" class="dropdown-item">Mark Delivered</button>
                                                                            </form>
                                                                        </li>
                                                                        <li><hr class="dropdown-divider"></li>
                                                                        <li>
                                                                            <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                                                                <input type="hidden" name="action" value="updateStatus">
                                                                                <input type="hidden" name="orderId" value="${order.orderId}">
                                                                                <input type="hidden" name="status" value="cancelled">
                                                                                <button type="submit" class="dropdown-item text-danger">Cancel Order</button>
                                                                            </form>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                            </c:if>
                                                        </td>
                                                    </c:if>
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
                                <i class="display-1 text-muted">📦</i>
                            </div>
                            <h3 class="text-muted">No Orders Found</h3>
                            <c:choose>
                                <c:when test="${isAdmin}">
                                    <p>No customer orders have been placed yet.</p>
                                </c:when>
                                <c:otherwise>
                                    <p>You haven't placed any orders yet. Start shopping to see your orders here!</p>
                                    <a href="${pageContext.request.contextPath}/products" class="btn btn-success btn-lg mt-3">
                                        Start Shopping
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:otherwise>
                </c:choose>
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