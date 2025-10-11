package config;

// Note: Servlet API dependencies required
// Add to project: jakarta.servlet-api-5.0.0.jar (already in ref_lib)
// import javax.servlet.ServletContextEvent;
// import javax.servlet.ServletContextListener;
// import javax.servlet.annotation.WebListener;

/**
 * Application context listener for FruitStore
 * Handles application startup and shutdown events
 */
// @WebListener
public class ApplicationContextListener /* implements ServletContextListener */ {
    
    /**
     * Initialize application - call this manually for now
     * @param sce ServletContextEvent (unused for manual initialization)
     */
    public void contextInitialized(/* ServletContextEvent sce */) {
        System.out.println("=== FruitStore Application Starting ===");
        
        try {
            // Initialize database connection pool
            System.out.println("Initializing database connection pool...");
            DatabaseConnection.getConnection(); // This will trigger pool initialization
            
            // Initialize database schema if needed
            System.out.println("Checking database initialization...");
            DatabaseInitializer.initializeDatabase();
            
            // Log successful startup
            System.out.println("Application started successfully!");
            System.out.println("Database connection pool status: " + DatabaseConnection.getPoolStatus());
            
        } catch (Exception e) {
            System.err.println("Error during application startup: " + e.getMessage());
            e.printStackTrace();
            
            // Log error (servlet context not available in manual mode)
            System.err.println("Startup error: " + e.getMessage());
        }
    }
    
    /**
     * Shutdown application - call this manually for now
     * @param sce ServletContextEvent (unused for manual shutdown)
     */
    public void contextDestroyed(/* ServletContextEvent sce */) {
        System.out.println("=== FruitStore Application Shutting Down ===");
        
        try {
            // Close database connection pool
            System.out.println("Closing database connection pool...");
            // No connection pool to close in basic implementation
            System.out.println("Database connections will be closed automatically");
            
            System.out.println("Application shutdown completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during application shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
