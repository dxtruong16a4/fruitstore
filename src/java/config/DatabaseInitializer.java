package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DatabaseUtils;

/**
 * Database initialization class for FruitStore
 * Handles database setup and schema initialization
 */
public class DatabaseInitializer {
    
    private static final String SCHEMA_FILE = "Database.sql";
    
    /**
     * Initialize the database schema
     * This method should be called once during application startup
     */
    public static void initializeDatabase() {
        System.out.println("Starting database initialization...");
        
        try {
            // Test connection first
            if (!DatabaseConnection.testConnection()) {
                System.err.println("Database connection test failed. Please check your database configuration.");
                return;
            }
            
            // Check if tables already exist
            if (areTablesInitialized()) {
                System.out.println("Database tables already exist. Skipping initialization.");
                return;
            }
            
            // Execute schema script
            executeSchemaScript();
            
            System.out.println("Database initialization completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during database initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check if database tables are already initialized
     * @return true if tables exist, false otherwise
     */
    private static boolean areTablesInitialized() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if users table exists (main table)
            return DatabaseUtils.tableExists("users");
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute the database schema script
     */
    private static void executeSchemaScript() throws SQLException, IOException {
        Connection connection = null;
        Statement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            
            // Read and execute schema file
            String schemaScript = readSchemaFile();
            if (schemaScript == null || schemaScript.trim().isEmpty()) {
                throw new IOException("Schema script is empty or could not be read");
            }
            
            // Split script into individual statements
            String[] statements = schemaScript.split(";");
            
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    try {
                        statement.execute(sql);
                        System.out.println("Executed: " + sql.substring(0, Math.min(50, sql.length())) + "...");
                    } catch (SQLException e) {
                        System.err.println("Error executing statement: " + sql);
                        System.err.println("Error: " + e.getMessage());
                        throw e;
                    }
                }
            }
            
            connection.commit();
            System.out.println("Schema script executed successfully");
            
        } catch (SQLException | IOException e) {
            if (connection != null) {
                DatabaseUtils.rollbackTransaction(connection);
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            DatabaseUtils.closeAll(connection, statement);
        }
    }
    
    /**
     * Read the database schema file
     * @return Schema script content
     * @throws IOException if file reading fails
     */
    private static String readSchemaFile() throws IOException {
        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        
        try {
            // Try to read from classpath first (for compiled resources)
            inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream(SCHEMA_FILE);
            
            if (inputStream == null) {
                // Try to read from docs directory
                inputStream = DatabaseInitializer.class.getClassLoader()
                    .getResourceAsStream("../../docs/" + SCHEMA_FILE);
            }
            
            if (inputStream == null) {
                throw new IOException("Could not find schema file: " + SCHEMA_FILE);
            }
            
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            return content.toString();
            
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
    
    /**
     * Create sample data for testing
     * This method should only be called in development environment
     */
    public static void createSampleData() {
        System.out.println("Creating sample data...");
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            // Insert sample categories
            insertSampleCategories(connection);
            
            // Insert sample products
            insertSampleProducts(connection);
            
            // Insert sample users
            insertSampleUsers(connection);
            
            DatabaseUtils.commitTransaction(connection);
            System.out.println("Sample data created successfully!");
            
        } catch (SQLException e) {
            DatabaseUtils.rollbackTransaction(connection);
            System.err.println("Error creating sample data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeConnection(connection);
        }
    }
    
    /**
     * Insert sample categories
     */
    private static void insertSampleCategories(Connection connection) throws SQLException {
        String sql = "INSERT INTO categories (name, description, slug, sort_order) VALUES (?, ?, ?, ?)";
        
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Sample categories
            String[][] categories = {
                {"Trái cây nhiệt đới", "Các loại trái cây nhiệt đới tươi ngon", "trai-cay-nhiet-doi", "1"},
                {"Trái cây ôn đới", "Trái cây vùng ôn đới chất lượng cao", "trai-cay-on-doi", "2"},
                {"Trái cây sấy khô", "Trái cây sấy khô giữ nguyên hương vị", "trai-cay-say-kho", "3"}
            };
            
            for (String[] category : categories) {
                stmt.setString(1, category[0]);
                stmt.setString(2, category[1]);
                stmt.setString(3, category[2]);
                stmt.setInt(4, Integer.parseInt(category[3]));
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            System.out.println("Sample categories inserted");
        }
    }
    
    /**
     * Insert sample products
     */
    private static void insertSampleProducts(Connection connection) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category_id, slug) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Sample products
            String[][] products = {
                {"Xoài Thái", "Xoài Thái chín cây, ngọt lịm", "45000", "100", "1", "xoai-thai"},
                {"Chuối Laba", "Chuối Laba Đà Lạt thơm ngon", "25000", "150", "1", "chuoi-laba"},
                {"Táo Mỹ", "Táo đỏ Mỹ giòn ngọt", "120000", "50", "2", "tao-my"},
                {"Nho xanh", "Nho xanh không hạt", "80000", "30", "2", "nho-xanh"}
            };
            
            for (String[] product : products) {
                stmt.setString(1, product[0]);
                stmt.setString(2, product[1]);
                stmt.setBigDecimal(3, new java.math.BigDecimal(product[2]));
                stmt.setInt(4, Integer.parseInt(product[3]));
                stmt.setInt(5, Integer.parseInt(product[4]));
                stmt.setString(6, product[5]);
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            System.out.println("Sample products inserted");
        }
    }
    
    /**
     * Insert sample users
     */
    private static void insertSampleUsers(Connection connection) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, full_name, role) VALUES (?, ?, ?, ?, ?)";
        
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Admin user
            stmt.setString(1, "admin");
            stmt.setString(2, "admin@fruitstore.com");
            stmt.setString(3, "admin123"); // In production, this should be hashed
            stmt.setString(4, "Quản trị viên");
            stmt.setString(5, "admin");
            stmt.addBatch();
            
            // Sample customer
            stmt.setString(1, "customer1");
            stmt.setString(2, "customer1@example.com");
            stmt.setString(3, "customer123"); // In production, this should be hashed
            stmt.setString(4, "Khách hàng 1");
            stmt.setString(5, "customer");
            stmt.addBatch();
            
            stmt.executeBatch();
            System.out.println("Sample users inserted");
        }
    }
}
