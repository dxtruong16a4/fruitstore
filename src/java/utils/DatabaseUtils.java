package utils;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utility class providing common database operations
 * for the FruitStore application
 */
public class DatabaseUtils {
    
    /**
     * Close database resources safely
     * @param connection Database connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close statement safely
     * @param statement Statement to close
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close prepared statement safely
     * @param preparedStatement PreparedStatement to close
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.err.println("Error closing prepared statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close result set safely
     * @param resultSet ResultSet to close
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close all database resources safely
     * @param connection Database connection
     * @param statement Statement or PreparedStatement
     * @param resultSet ResultSet
     */
    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }
    
    /**
     * Close connection and statement safely
     * @param connection Database connection
     * @param statement Statement or PreparedStatement
     */
    public static void closeAll(Connection connection, Statement statement) {
        closeStatement(statement);
        closeConnection(connection);
    }
    
    /**
     * Execute a query with automatic resource management
     * @param sql SQL query to execute
     * @param params Parameters for prepared statement
     * @return ResultSet from the query
     * @throws SQLException if query execution fails
     */
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        
        try {
            preparedStatement = connection.prepareStatement(sql);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeAll(connection, preparedStatement);
            throw e;
        }
    }
    
    /**
     * Execute an update query (INSERT, UPDATE, DELETE) with automatic resource management
     * @param sql SQL query to execute
     * @param params Parameters for prepared statement
     * @return Number of affected rows
     * @throws SQLException if query execution fails
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            
            return preparedStatement.executeUpdate();
        } finally {
            closeAll(connection, preparedStatement);
        }
    }
    
    /**
     * Execute an update query and return generated keys
     * @param sql SQL query to execute
     * @param params Parameters for prepared statement
     * @return Generated keys ResultSet
     * @throws SQLException if query execution fails
     */
    public static ResultSet executeUpdateWithKeys(String sql, Object... params) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys();
        } catch (SQLException e) {
            closeAll(connection, preparedStatement);
            throw e;
        }
    }
    
    /**
     * Begin a database transaction
     * @return Connection with auto-commit disabled
     * @throws SQLException if transaction start fails
     */
    public static Connection beginTransaction() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }
    
    /**
     * Commit a database transaction
     * @param connection Connection with active transaction
     * @throws SQLException if commit fails
     */
    public static void commitTransaction(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * Rollback a database transaction
     * @param connection Connection with active transaction
     * @throws SQLException if rollback fails
     */
    public static void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error during rollback: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test if a table exists in the database
     * @param tableName Name of the table to check
     * @return true if table exists, false otherwise
     */
    public static boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        
        try (ResultSet rs = executeQuery(sql, tableName)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get the last inserted ID from a table
     * @param connection Database connection
     * @return Last inserted ID, or -1 if not available
     */
    public static int getLastInsertId(Connection connection) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting last insert ID: " + e.getMessage());
        }
        
        return -1;
    }
}
