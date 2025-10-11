package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection configuration class for FruitStore
 * Provides basic database connection management
 */
public class DatabaseConnection {
    
    private static final String PROPERTIES_FILE = "database.properties";
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String dbDriver;
    private static boolean initialized = false;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }
    
    /**
     * Initialize database connection parameters
     */
    private static void initialize() {
        if (!initialized) {
            try {
                Properties props = loadProperties();
                
                dbDriver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
                dbUrl = props.getProperty("db.url");
                dbUsername = props.getProperty("db.username");
                dbPassword = props.getProperty("db.password");
                
                // Load MySQL driver
                Class.forName(dbDriver);
                
                initialized = true;
                System.out.println("Database connection parameters initialized successfully");
                
            } catch (Exception e) {
                System.err.println("Error initializing database connection: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize database connection", e);
            }
        }
    }
    
    /**
     * Load database properties from properties file
     */
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        InputStream input = null;
        
        try {
            // Try to load from classpath first
            input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            
            if (input == null) {
                // Try alternative paths
                input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties");
            }
            
            if (input == null) {
                // Try to load from file system as fallback
                try {
                    java.io.File file = new java.io.File(PROPERTIES_FILE);
                    if (file.exists()) {
                        input = new java.io.FileInputStream(file);
                    }
                } catch (Exception e) {
                    // File not found, will use default properties
                }
            }
            
            if (input == null) {
                System.out.println("Warning: " + PROPERTIES_FILE + " not found. Using default configuration.");
                return getDefaultProperties();
            }
            
            props.load(input);
            return props;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }
    
    /**
     * Get default database properties
     */
    private static Properties getDefaultProperties() {
        Properties props = new Properties();
        props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        props.setProperty("db.url", "jdbc:mysql://localhost:3306/FruitStore?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        props.setProperty("db.username", "root");
        props.setProperty("db.password", "");
        return props;
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }
        
        if (dbUrl == null || dbUsername == null) {
            throw new SQLException("Database configuration not properly initialized");
        }
        
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database connection status
     * @return String containing connection status information
     */
    public static String getPoolStatus() {
        if (!initialized) {
            return "Database not initialized";
        }
        
        try {
            boolean connected = testConnection();
            return String.format(
                "Database Status - Initialized: %s, Connected: %s, Driver: %s",
                initialized, connected, dbDriver
            );
        } catch (Exception e) {
            return "Database Status - Error: " + e.getMessage();
        }
    }
    
    /**
     * Get database URL (for debugging)
     * @return Database URL
     */
    public static String getDatabaseUrl() {
        if (!initialized) {
            initialize();
        }
        return dbUrl;
    }
    
    /**
     * Get database username (for debugging)
     * @return Database username
     */
    public static String getDatabaseUsername() {
        if (!initialized) {
            initialize();
        }
        return dbUsername;
    }
}