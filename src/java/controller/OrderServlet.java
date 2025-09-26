package controller;

import dao.OrderDAO;
import dao.CartDAO;
import dao.ProductDAO;
import model.Order;
import model.OrderWithUser;
import model.Cart;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            System.out.println("Initializing OrderServlet...");
            orderDAO = new OrderDAO();
            cartDAO = new CartDAO();
            productDAO = new ProductDAO();
            System.out.println("OrderServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing OrderServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize OrderServlet", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            int userId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            
            System.out.println("Orders request for userId: " + userId + ", role: " + role);
            
            List<? extends Order> orders;
            if ("admin".equals(role)) {
                orders = orderDAO.getAllOrders();
            } else {
                orders = orderDAO.getOrdersByUserId(userId);
            }
            
            request.setAttribute("orders", orders);
            request.setAttribute("isAdmin", "admin".equals(role));
            request.getRequestDispatcher("/jsp/orders.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in OrderServlet doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while loading orders. Please try again.");
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            String action = request.getParameter("action");
            System.out.println("OrderServlet doPost action: " + action);
            
            if ("place".equals(action) || "checkout".equals(action)) {
                handlePlaceOrder(request, response, session);
            } else if ("updateStatus".equals(action)) {
                handleUpdateOrderStatus(request, response, session);
            } else {
                doGet(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error in OrderServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/orders?error=An+error+occurred+while+processing+your+request");
        }
    }
    
    private void handlePlaceOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws IOException {
        
        try {
            int userId = (Integer) session.getAttribute("userId");
            String username = (String) session.getAttribute("username");
            
            System.out.println("Processing order for userId: " + userId + ", username: " + username);
            
            // Get cart items
            List<Cart> cartItems = cartDAO.getCartByUserId(userId);
            System.out.println("Found " + cartItems.size() + " items in cart");
            
            if (cartItems.isEmpty()) {
                System.out.println("Cart is empty, redirecting back");
                response.sendRedirect(request.getContextPath() + "/cart?error=Cart+is+empty");
                return;
            }
            
            // Validate stock availability first
            for (Cart item : cartItems) {
                if (item.getProduct() != null) {
                    int productId = item.getProduct().getProductId();
                    int requestedQuantity = item.getQuantity();
                    
                    if (!productDAO.isStockAvailable(productId, requestedQuantity)) {
                        System.out.println("Insufficient stock for product: " + item.getProduct().getName());
                        response.sendRedirect(request.getContextPath() + "/cart?error=Insufficient+stock+for+" + 
                                           item.getProduct().getName().replace(" ", "+"));
                        return;
                    }
                }
            }
            
            // Calculate total
            BigDecimal total = BigDecimal.ZERO;
            for (Cart item : cartItems) {
                if (item.getProduct() != null) {
                    BigDecimal itemTotal = item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                    total = total.add(itemTotal);
                    System.out.println("Item: " + item.getProduct().getName() + ", Quantity: " + item.getQuantity() + ", Total: " + itemTotal);
                }
            }
            System.out.println("Order total: " + total);
            
            // Create order
            Order order = new Order(userId, total, "pending");
            int orderId = orderDAO.createOrder(order);
            System.out.println("Created order with ID: " + orderId);
            
            if (orderId > 0) {
                // Decrease stock quantities for ordered items
                boolean stockUpdated = true;
                for (Cart item : cartItems) {
                    if (item.getProduct() != null) {
                        int productId = item.getProduct().getProductId();
                        int quantity = item.getQuantity();
                        
                        if (!productDAO.decreaseStock(productId, quantity)) {
                            System.err.println("Failed to decrease stock for product ID: " + productId);
                            stockUpdated = false;
                        } else {
                            System.out.println("Decreased stock for " + item.getProduct().getName() + " by " + quantity);
                        }
                    }
                }
                
                if (!stockUpdated) {
                    System.err.println("Warning: Some stock quantities may not have been updated correctly");
                }
                
                // Clear cart after successful order
                boolean cartCleared = cartDAO.clearCart(userId);
                System.out.println("Cart cleared: " + cartCleared);
                
                // Send confirmation email (optional) - don't let email errors break the order
                try {
                    String userEmail = request.getParameter("email");
                    if (userEmail != null && !userEmail.isEmpty()) {
                        EmailUtil.sendOrderConfirmation(userEmail, username, orderId, total.toString());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to send order confirmation email: " + e.getMessage());
                    // Continue with order completion even if email fails
                }
                
                response.sendRedirect(request.getContextPath() + "/orders?success=Order+placed+successfully&orderId=" + orderId);
            } else {
                System.err.println("Failed to create order in database");
                response.sendRedirect(request.getContextPath() + "/cart?error=Failed+to+place+order");
            }
            
        } catch (Exception e) {
            System.err.println("Error in handlePlaceOrder: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?error=An+error+occurred+while+placing+order");
        }
    }
    
    private void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws IOException {
        
        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/orders?error=Access+denied");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            
            if (status == null || status.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/orders?error=Invalid+status");
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, status);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/orders?success=Order+status+updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/orders?error=Failed+to+update+status");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders?error=Invalid+order+ID");
        }
    }
}