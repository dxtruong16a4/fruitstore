package daos;

import config.DatabaseConnection;
import models.User;
import utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity
 * Demonstrates how to use the database connection configuration
 */
public class UserDAO {
    
    /**
     * Get all users from the database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return users;
    }
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users WHERE user_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Get user by email
     * @param email User email
     * @return User object or null if not found
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users WHERE email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Create a new user
     * @param user User object to create
     * @return Generated user ID or -1 if failed
     */
    public int createUser(User user) {
        String sql = "INSERT INTO users (username, email, password, full_name, phone, address, avatar_url, role, is_active, email_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFullName());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getAddress());
            statement.setString(7, user.getAvatarUrl());
            statement.setString(8, user.getRole().toString());
            statement.setBoolean(9, user.isActive());
            statement.setBoolean(10, user.isEmailVerified());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, generatedKeys);
        }
        
        return -1;
    }
    
    /**
     * Update an existing user
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, full_name = ?, phone = ?, address = ?, avatar_url = ?, role = ?, is_active = ?, email_verified = ? WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatarUrl(),
                user.getRole().toString(),
                user.isActive(),
                user.isEmailVerified(),
                user.getUserId()
            );
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a user
     * @param userId User ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql, email)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql, username)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to User object
     * @param rs ResultSet
     * @return User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setRole(models.UserRole.valueOf(rs.getString("role").toUpperCase()));
        user.setActive(rs.getBoolean("is_active"));
        user.setEmailVerified(rs.getBoolean("email_verified"));
        // Convert Timestamp to LocalDateTime if needed
        java.sql.Timestamp lastLogin = rs.getTimestamp("last_login");
        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        return user;
    }
}
