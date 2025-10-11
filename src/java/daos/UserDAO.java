package daos;

import config.DatabaseConnection;
import models.User;
import models.UserActivityLog;
import models.ActivityType;
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
     * Get user by username
     * @param username Username to search for
     * @return User object or null if not found
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users WHERE username = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Get user by username or email (for login)
     * @param usernameOrEmail Username or email to search for
     * @return User object or null if not found
     */
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users WHERE username = ? OR email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, usernameOrEmail);
            statement.setString(2, usernameOrEmail);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by username or email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Verify user password (for login authentication)
     * @param userId User ID
     * @param password Plain text password to verify
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(int userId, String password) {
        String sql = "SELECT password FROM users WHERE user_id = ?";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql, userId)) {
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // In a real application, you would hash the password and compare
                // For now, we'll do a simple string comparison
                return storedPassword != null && storedPassword.equals(password);
            }
        } catch (SQLException e) {
            System.err.println("Error verifying password: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Verify user password by email
     * @param email User email
     * @param password Plain text password to verify
     * @return User object if password matches, null otherwise
     */
    public User authenticateUser(String email, String password) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at, password FROM users WHERE email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword != null && storedPassword.equals(password)) {
                    User user = mapResultSetToUser(resultSet);
                    // Don't include password in the returned user object
                    user.setPassword(null);
                    return user;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Update user's last login time
     * @param userId User ID
     * @return true if update successful, false otherwise
     */
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW(), updated_at = NOW() WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New password (should be hashed in production)
     * @return true if update successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, newPassword, userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Verify email address (mark as verified)
     * @param email Email to verify
     * @return true if verification successful, false otherwise
     */
    public boolean verifyEmail(String email) {
        String sql = "UPDATE users SET email_verified = TRUE, updated_at = NOW() WHERE email = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, email);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error verifying email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get user password for verification (used in password reset)
     * @param email User email
     * @return User object with password field populated, or null if not found
     */
    public User getUserForPasswordReset(String email) {
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at, password FROM users WHERE email = ?";
        
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
            System.err.println("Error getting user for password reset: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return null;
    }
    
    /**
     * Update user profile information
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, address = ?, avatar_url = ?, updated_at = NOW() WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql,
                user.getFullName(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatarUrl(),
                user.getUserId()
            );
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get users with pagination (for admin)
     * @param offset Offset for pagination
     * @param limit Limit for pagination
     * @return List of User objects
     */
    public List<User> getUsersWithPagination(int offset, int limit) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, full_name, phone, address, avatar_url, role, is_active, email_verified, last_login, created_at FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting users with pagination: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return users;
    }
    
    /**
     * Get total count of users (for admin pagination)
     * @return Total number of users
     */
    public int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total users count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Update user status (active/inactive)
     * @param userId User ID
     * @param isActive Active status
     * @return true if update successful, false otherwise
     */
    public boolean updateUserStatus(int userId, boolean isActive) {
        String sql = "UPDATE users SET is_active = ?, updated_at = NOW() WHERE user_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, isActive, userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ========== USER ACTIVITY LOG METHODS ==========
    
    /**
     * Create a new user activity log entry
     * @param activityLog UserActivityLog object to create
     * @return Generated log ID or -1 if failed
     */
    public int createActivityLog(UserActivityLog activityLog) {
        String sql = "INSERT INTO user_activity_logs (user_id, activity_type, description, ip_address, user_agent) VALUES (?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, activityLog.getUserId());
            statement.setString(2, activityLog.getActivityType().getValue());
            statement.setString(3, activityLog.getDescription());
            statement.setString(4, activityLog.getIpAddress());
            statement.setString(5, activityLog.getUserAgent());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating activity log: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, generatedKeys);
        }
        
        return -1;
    }
    
    /**
     * Get activity logs for a specific user
     * @param userId User ID
     * @return List of UserActivityLog objects
     */
    public List<UserActivityLog> getUserActivityLogs(int userId) {
        List<UserActivityLog> logs = new ArrayList<>();
        String sql = "SELECT log_id, user_id, activity_type, description, ip_address, user_agent, created_at FROM user_activity_logs WHERE user_id = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                UserActivityLog log = mapResultSetToActivityLog(resultSet);
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user activity logs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return logs;
    }
    
    /**
     * Get activity logs for a specific user with pagination
     * @param userId User ID
     * @param offset Offset for pagination
     * @param limit Limit for pagination
     * @return List of UserActivityLog objects
     */
    public List<UserActivityLog> getUserActivityLogsWithPagination(int userId, int offset, int limit) {
        List<UserActivityLog> logs = new ArrayList<>();
        String sql = "SELECT log_id, user_id, activity_type, description, ip_address, user_agent, created_at FROM user_activity_logs WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                UserActivityLog log = mapResultSetToActivityLog(resultSet);
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user activity logs with pagination: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return logs;
    }
    
    /**
     * Get all activity logs (for admin)
     * @param offset Offset for pagination
     * @param limit Limit for pagination
     * @return List of UserActivityLog objects
     */
    public List<UserActivityLog> getAllActivityLogsWithPagination(int offset, int limit) {
        List<UserActivityLog> logs = new ArrayList<>();
        String sql = "SELECT log_id, user_id, activity_type, description, ip_address, user_agent, created_at FROM user_activity_logs ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                UserActivityLog log = mapResultSetToActivityLog(resultSet);
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all activity logs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeAll(connection, statement, resultSet);
        }
        
        return logs;
    }
    
    /**
     * Get total count of activity logs for a user
     * @param userId User ID
     * @return Total number of activity logs
     */
    public int getUserActivityLogsCount(int userId) {
        String sql = "SELECT COUNT(*) FROM user_activity_logs WHERE user_id = ?";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql, userId)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user activity logs count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total count of all activity logs (for admin)
     * @return Total number of activity logs
     */
    public int getTotalActivityLogsCount() {
        String sql = "SELECT COUNT(*) FROM user_activity_logs";
        
        try (ResultSet rs = DatabaseUtils.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total activity logs count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Delete activity log by ID
     * @param logId Log ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteActivityLog(int logId) {
        String sql = "DELETE FROM user_activity_logs WHERE log_id = ?";
        
        try {
            int affectedRows = DatabaseUtils.executeUpdate(sql, logId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting activity log: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
        
        // Handle password field if present
        try {
            String password = rs.getString("password");
            user.setPassword(password);
        } catch (SQLException e) {
            // Password field not selected, that's fine
            user.setPassword(null);
        }
        
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
    
    /**
     * Map ResultSet to UserActivityLog object
     * @param rs ResultSet
     * @return UserActivityLog object
     * @throws SQLException if mapping fails
     */
    private UserActivityLog mapResultSetToActivityLog(ResultSet rs) throws SQLException {
        UserActivityLog log = new UserActivityLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setActivityType(ActivityType.fromString(rs.getString("activity_type")));
        log.setDescription(rs.getString("description"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setUserAgent(rs.getString("user_agent"));
        
        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            log.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return log;
    }
}
