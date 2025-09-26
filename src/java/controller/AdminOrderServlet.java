package controller;

import dao.OrderDAO;
import dao.CartDAO;
import dao.ProductDAO;
import model.Order;
import model.OrderWithUser;
import model.Cart;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("role");
        
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("getOrderDetails".equals(action)) {
            getOrderDetails(request, response);
        } else {
            // Default: show orders page
            List<OrderWithUser> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            
            // Check for success/error messages
            String message = request.getParameter("message");
            String error = request.getParameter("error");
            
            if (message != null) {
                request.setAttribute("successMessage", message);
            }
            if (error != null) {
                request.setAttribute("errorMessage", error);
            }
            
            request.getRequestDispatcher("/jsp/admin/orders.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("role");
        
        if (!"admin".equals(userRole)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("updateStatus".equals(action)) {
            handleStatusUpdate(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
    
    private void getOrderDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order != null) {
                // Get order items from cart history (simplified approach)
                // In a real system, you'd have an order_items table
                List<Cart> orderItems = getOrderItemsFromCart(order.getUserId());
                List<Map<String, Object>> orderDetails = new ArrayList<>();
                
                for (Cart item : orderItems) {
                    Product product = productDAO.getProductById(item.getProductId());
                    if (product != null) {
                        Map<String, Object> detail = new HashMap<>();
                        detail.put("productName", product.getName());
                        detail.put("quantity", item.getQuantity());
                        detail.put("price", product.getPrice());
                        detail.put("subtotal", product.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
                        orderDetails.add(detail);
                    }
                }
                
                request.setAttribute("order", order);
                request.setAttribute("orderDetails", orderDetails);
                request.getRequestDispatcher("/jsp/admin/order-details.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=Order not found");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=Invalid order ID");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=Error retrieving order details");
        }
    }
    
    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            
            if (orderDAO.updateOrderStatus(orderId, status)) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?message=Order status updated to " + status);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=Failed to update order status");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=Invalid order ID");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=An error occurred while updating order status");
        }
    }
    
    // Simplified method to get order items (in a real system, you'd have proper order_items table)
    private List<Cart> getOrderItemsFromCart(int userId) {
        // This is a simplified approach - in reality you'd store order items separately
        // For now, we'll return a few sample items or recent cart items
        List<Cart> items = new ArrayList<>();
        
        // Get some cart items for demonstration
        List<Cart> cartItems = cartDAO.getCartByUserId(userId);
        if (!cartItems.isEmpty()) {
            // Take first few items as sample order items
            items.addAll(cartItems.subList(0, Math.min(3, cartItems.size())));
        }
        
        return items;
    }
}