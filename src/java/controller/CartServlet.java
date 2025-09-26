package controller;

import dao.CartDAO;
import dao.ProductDAO;
import model.Cart;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            cartDAO = new CartDAO();
            productDAO = new ProductDAO();
            System.out.println("CartServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("CartServlet initialization failed: " + e.getMessage());
            throw new ServletException("Failed to initialize CartServlet", e);
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
            System.out.println("Loading cart for user ID: " + userId);
            
            List<Cart> cartItems = cartDAO.getCartByUserId(userId);
        
        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (Cart item : cartItems) {
            if (item.getProduct() != null) {
                BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(itemTotal);
            }
        }
        
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", total);
            request.setAttribute("cartCount", cartItems.size());
            
            request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Cart error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Unable to load cart. Please try again.");
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=cart_error");
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
            int userId = (Integer) session.getAttribute("userId");
            System.out.println("Cart action: " + action + " for user: " + userId);
        
        if ("add".equals(action)) {
            handleAddToCart(request, response, userId);
        } else if ("update".equals(action)) {
            handleUpdateCart(request, response);
        } else if ("remove".equals(action)) {
            handleRemoveFromCart(request, response);
        } else if ("clear".equals(action)) {
            handleClearCart(request, response, userId);
        } else {
            doGet(request, response);
        }
        
        } catch (Exception e) {
            System.err.println("Cart POST error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?error=operation_failed");
        }
    }
    
    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            if (quantity <= 0) {
                response.sendRedirect(request.getContextPath() + "/products?error=Invalid+quantity");
                return;
            }
            
            // Check if product exists and has sufficient stock
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products?error=Product+not+found");
                return;
            }
            
            if (product.getStockQuantity() < quantity) {
                response.sendRedirect(request.getContextPath() + "/products?error=Insufficient+stock");
                return;
            }
            
            Cart cart = new Cart(userId, productId, quantity);
            boolean success = cartDAO.addToCart(cart);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?success=Item+added+to+cart");
            } else {
                response.sendRedirect(request.getContextPath() + "/products?error=Failed+to+add+item");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=Invalid+parameters");
        }
    }
    
    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            boolean success = cartDAO.updateCartQuantity(cartId, quantity);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?success=Cart+updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?error=Failed+to+update+cart");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=Invalid+parameters");
        }
    }
    
    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            
            boolean success = cartDAO.removeFromCart(cartId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?success=Item+removed");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?error=Failed+to+remove+item");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=Invalid+parameters");
        }
    }
    
    private void handleClearCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        boolean success = cartDAO.clearCart(userId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/cart?success=Cart+cleared");
        } else {
            response.sendRedirect(request.getContextPath() + "/cart?error=Failed+to+clear+cart");
        }
    }
}