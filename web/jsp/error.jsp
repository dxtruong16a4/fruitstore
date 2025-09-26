<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - FruitStore</title>
    <link href="../css/styles.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-8 col-lg-6">
                <div class="card shadow">
                    <div class="card-body text-center">
                        <div class="mb-4">
                            <h1 class="text-danger display-1">😞</h1>
                            <h2 class="text-danger">Oops! Something went wrong</h2>
                        </div>
                        
                        <% 
                            // Get the error status code
                            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
                            String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
                            String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
                            
                            if (statusCode == null) {
                                statusCode = 500; // Default to 500 if no status code
                            }
                        %>
                        
                        <div class="alert alert-danger" role="alert">
                            <strong>Error <%= statusCode %></strong>
                            <br>
                            <% if (statusCode == 404) { %>
                                The page you are looking for could not be found.
                            <% } else if (statusCode == 500) { %>
                                Internal server error occurred. Please try again later.
                            <% } else { %>
                                An unexpected error occurred.
                            <% } %>
                            
                            <% if (requestURI != null) { %>
                                <br><small class="text-muted">Requested URL: <%= requestURI %></small>
                            <% } %>
                        </div>
                        
                        <div class="mt-4">
                            <h5>What can you do?</h5>
                            <ul class="list-unstyled">
                                <li>✓ Check the URL for typos</li>
                                <li>✓ Go back to the previous page</li>
                                <li>✓ Visit our homepage</li>
                                <li>✓ Contact support if the problem persists</li>
                            </ul>
                        </div>
                        
                        <div class="mt-4">
                            <a href="javascript:history.back()" class="btn btn-secondary me-2">
                                ← Go Back
                            </a>
                            <a href="../index.jsp" class="btn btn-success">
                                🏠 Home Page
                            </a>
                        </div>
                        
                        <hr class="my-4">
                        
                        <div class="text-muted">
                            <small>
                                If you continue to experience problems, please contact our support team at 
                                <a href="mailto:support@fruitstore.com">support@fruitstore.com</a>
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Debug information (only show in development) -->
    <% if (application.getInitParameter("debug") != null && "true".equals(application.getInitParameter("debug"))) { %>
        <div class="container mt-4">
            <div class="card">
                <div class="card-header">
                    <h5>Debug Information</h5>
                </div>
                <div class="card-body">
                    <p><strong>Status Code:</strong> <%= statusCode %></p>
                    <p><strong>Error Message:</strong> <%= errorMessage != null ? errorMessage : "N/A" %></p>
                    <p><strong>Request URI:</strong> <%= requestURI != null ? requestURI : "N/A" %></p>
                    <p><strong>Exception Type:</strong> <%= request.getAttribute("javax.servlet.error.exception_type") %></p>
                    
                    <% if (exception != null) { %>
                    <div class="mt-3">
                        <h6>Stack Trace:</h6>
                        <pre class="bg-light p-3" style="max-height: 300px; overflow-y: auto;">
<%= exception.getMessage() %>
                        </pre>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    <% } %>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>