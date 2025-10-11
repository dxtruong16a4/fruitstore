// Test class - no package declaration needed for simple testing
import config.DatabaseConnection;
import config.DatabaseInitializer;
import daos.UserDAO;
import models.User;
import models.UserRole;

/**
 * Test class to demonstrate database connection usage
 * This class can be used to test the database configuration
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== FruitStore Database Connection Test ===");
        
        // Check if properties file exists
        checkPropertiesFile();
        
        try {
            // Test 1: Basic connection test
            System.out.println("\n1. Testing basic database connection...");
            boolean connected = DatabaseConnection.testConnection();
            if (connected) {
                System.out.println("✓ Database connection successful!");
            } else {
                System.out.println("✗ Database connection failed!");
                printConnectionHelp();
                return;
            }
            
            // Test 2: Display connection status
            System.out.println("\n2. Database connection status:");
            System.out.println("   " + DatabaseConnection.getPoolStatus());
            System.out.println("   URL: " + DatabaseConnection.getDatabaseUrl());
            System.out.println("   Username: " + DatabaseConnection.getDatabaseUsername());
            
            // Test 3: Initialize database schema
            System.out.println("\n3. Testing database initialization...");
            try {
                DatabaseInitializer.initializeDatabase();
                System.out.println("✓ Database initialization completed!");
            } catch (Exception e) {
                System.out.println("✗ Database initialization failed: " + e.getMessage());
                // Continue with other tests even if initialization fails
            }
            
            // Test 4: Test DAO operations
            System.out.println("\n4. Testing DAO operations...");
            testDAOOperations();
            
            System.out.println("\n=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test DAO operations
     */
    private static void testDAOOperations() {
        UserDAO userDAO = new UserDAO();
        
        try {
            // Test getting all users
            System.out.println("   Testing get all users...");
            java.util.List<User> users = userDAO.getAllUsers();
            System.out.println("   ✓ Found " + users.size() + " users");
            
            // Test checking if email exists
            System.out.println("   Testing email existence check...");
            boolean emailExists = userDAO.emailExists("admin@fruitstore.com");
            System.out.println("   ✓ Email check completed: " + emailExists);
            
            // Test checking if username exists
            System.out.println("   Testing username existence check...");
            boolean usernameExists = userDAO.usernameExists("admin");
            System.out.println("   ✓ Username check completed: " + usernameExists);
            
        } catch (Exception e) {
            System.out.println("   ✗ DAO operations failed: " + e.getMessage());
        }
    }
    
    /**
     * Test user creation (optional - uncomment to test)
     */
    private static void testUserCreation() {
        UserDAO userDAO = new UserDAO();
        
        try {
            // Create a test user
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword("testpass");
            testUser.setFullName("Test User");
            testUser.setRole(UserRole.CUSTOMER);
            testUser.setActive(true);
            testUser.setEmailVerified(false);
            
            int userId = userDAO.createUser(testUser);
            if (userId > 0) {
                System.out.println("   ✓ Test user created with ID: " + userId);
                
                // Get the created user
                User retrievedUser = userDAO.getUserById(userId);
                if (retrievedUser != null) {
                    System.out.println("   ✓ Test user retrieved successfully");
                }
            } else {
                System.out.println("   ✗ Failed to create test user");
            }
            
        } catch (Exception e) {
            System.out.println("   ✗ User creation test failed: " + e.getMessage());
        }
    }
    
    /**
     * Check if properties file exists and provide guidance
     */
    private static void checkPropertiesFile() {
        System.out.println("\nChecking configuration files...");
        
        // Check if properties file exists in classpath
        java.io.InputStream input = DatabaseTest.class.getClassLoader().getResourceAsStream("database.properties");
        if (input != null) {
            try {
                input.close();
            } catch (Exception e) {
                // Ignore
            }
            System.out.println("✓ Found database.properties in classpath");
        } else {
            System.out.println("⚠ database.properties not found in classpath");
            System.out.println("  Using default configuration (localhost:3306, username: root, password: empty)");
            System.out.println("  To customize, create src/java/resources/database.properties");
        }
        
        // Check if database.sql exists
        java.io.InputStream sqlInput = DatabaseTest.class.getClassLoader().getResourceAsStream("Database.sql");
        if (sqlInput != null) {
            try {
                sqlInput.close();
            } catch (Exception e) {
                // Ignore
            }
            System.out.println("✓ Found Database.sql in classpath");
        } else {
            System.out.println("⚠ Database.sql not found in classpath");
            System.out.println("  Copy docs/Database.sql to src/java/resources/Database.sql");
        }
    }
    
    /**
     * Print connection help information
     */
    private static void printConnectionHelp() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DATABASE CONNECTION HELP");
        System.out.println("=".repeat(60));
        
        System.out.println("\n1. Make sure MySQL server is running:");
        System.out.println("   - Check if MySQL service is started");
        System.out.println("   - Default port: 3306");
        
        System.out.println("\n2. Create the database:");
        System.out.println("   mysql -u root -p");
        System.out.println("   CREATE DATABASE IF NOT EXISTS FruitStore;");
        
        System.out.println("\n3. Update database configuration:");
        System.out.println("   - Create: src/java/resources/database.properties");
        System.out.println("   - Set your MySQL username and password");
        
        System.out.println("\n4. Copy schema file:");
        System.out.println("   - Copy docs/Database.sql to src/java/resources/Database.sql");
        
        System.out.println("\n5. Run with correct classpath:");
        System.out.println("   javac -cp \"ref_lib/*\" src/java/config/*.java src/java/utils/*.java src/java/daos/*.java src/java/models/*.java");
        System.out.println("   java -cp \"ref_lib/*:src/java\" config.DatabaseTest");
        
        System.out.println("\n6. Default configuration (if no properties file):");
        System.out.println("   - Host: localhost:3306");
        System.out.println("   - Database: FruitStore");
        System.out.println("   - Username: root");
        System.out.println("   - Password: (empty)");
        
        System.out.println("\n" + "=".repeat(60));
    }
}
