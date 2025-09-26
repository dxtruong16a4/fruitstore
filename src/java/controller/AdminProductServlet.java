package controller;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
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
        
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        
        // Check for success/error messages
        String message = request.getParameter("message");
        String error = request.getParameter("error");
        
        if (message != null) {
            request.setAttribute("successMessage", message);
        }
        if (error != null) {
            request.setAttribute("errorMessage", error);
        }
        
        request.getRequestDispatcher("/jsp/admin/products.jsp").forward(request, response);
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
        
        if ("updateStock".equals(action)) {
            handleStockUpdate(request, response);
        } else if ("delete".equals(action)) {
            handleProductDelete(request, response);
        } else if ("edit".equals(action)) {
            handleProductEdit(request, response);
        } else if ("add".equals(action)) {
            handleProductAdd(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    private void handleStockUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String stockAction = request.getParameter("stockAction");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=Product not found");
                return;
            }
            
            boolean success = false;
            String message = "";
            
            switch (stockAction) {
                case "set":
                    success = productDAO.updateStock(productId, quantity);
                    message = success ? "Stock set to " + quantity + " units" : "Failed to update stock";
                    break;
                    
                case "add":
                    success = productDAO.increaseStock(productId, quantity);
                    message = success ? quantity + " units added to stock" : "Failed to add stock";
                    break;
                    
                case "subtract":
                    if (product.getStockQuantity() < quantity) {
                        response.sendRedirect(request.getContextPath() + "/admin/products?error=Cannot subtract " + quantity + " units. Current stock is only " + product.getStockQuantity());
                        return;
                    }
                    success = productDAO.decreaseStock(productId, quantity);
                    message = success ? quantity + " units subtracted from stock" : "Failed to subtract stock";
                    break;
                    
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/products?error=Invalid stock action");
                    return;
            }
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/products?message=" + message);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=" + message);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=Invalid number format");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=An error occurred while updating stock");
        }
    }
    
    private void handleProductDelete(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            if (productDAO.deleteProduct(productId)) {
                response.sendRedirect(request.getContextPath() + "/admin/products?message=Product deleted successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=Failed to delete product");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=Invalid product ID");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=An error occurred while deleting product");
        }
    }
    
    private void handleProductEdit(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String description = request.getParameter("description");
            String imageUrl = request.getParameter("imageUrl");
            
            // Create product object and update
            Product product = new Product();
            product.setProductId(productId);
            product.setName(name);
            product.setCategory(category);
            product.setPrice(java.math.BigDecimal.valueOf(price));
            product.setStockQuantity(stock);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            
            if (productDAO.updateProduct(product)) {
                response.sendRedirect(request.getContextPath() + "/admin/products?message=Product updated successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=Failed to update product");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=Invalid number format");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=An error occurred while updating product");
        }
    }
    
    private void handleProductAdd(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String description = request.getParameter("description");
            String imageUrl = request.getParameter("imageUrl");
            
            // Create product object
            Product product = new Product(name, description, java.math.BigDecimal.valueOf(price), stock, imageUrl, category);
            
            if (productDAO.createProduct(product)) {
                response.sendRedirect(request.getContextPath() + "/admin/products?message=Product added successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=Failed to add product");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=Invalid number format");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=An error occurred while adding product");
        }
    }
}