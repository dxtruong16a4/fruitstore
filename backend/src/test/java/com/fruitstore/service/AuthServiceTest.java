package com.fruitstore.service;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.request.auth.ChangePasswordRequest;
import com.fruitstore.dto.request.auth.LoginRequest;
import com.fruitstore.dto.request.auth.RegisterRequest;
import com.fruitstore.dto.request.auth.UpdateProfileRequest;
import com.fruitstore.dto.response.auth.LoginResponse;
import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.repository.UserRepository;
import com.fruitstore.util.JwtUtil;
import com.fruitstore.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$hashedPassword");
        testUser.setFullName("Test User");
        testUser.setPhone("0123456789");
        testUser.setAddress("123 Test Street");
        testUser.setRole(UserRole.CUSTOMER);
        testUser.setCreatedAt(LocalDateTime.now());

        registerRequest = new RegisterRequest(
            "testuser",
            "test@example.com",
            "Password123",
            "Test User",
            "0123456789",
            "123 Test Street"
        );

        loginRequest = new LoginRequest("test@example.com", "Password123");
    }

    // ===== REGISTER TESTS =====

    @Test
    void testRegister_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordUtil.isPasswordStrong(anyString())).thenReturn(true);
        when(passwordUtil.hashPassword(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When
        LoginResponse response = authService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals(UserRole.CUSTOMER, response.getRole());

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).existsByUsername("testuser");
        verify(passwordUtil).isPasswordStrong("Password123");
        verify(passwordUtil).hashPassword("Password123");
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Username already exists"));
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_WeakPassword() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordUtil.isPasswordStrong(anyString())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Password must be at least 8 characters"));
        verify(passwordUtil).isPasswordStrong("Password123");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_HashesPassword() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordUtil.isPasswordStrong(anyString())).thenReturn(true);
        when(passwordUtil.hashPassword(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When
        authService.register(registerRequest);

        // Then
        verify(passwordUtil).hashPassword("Password123");
        verify(jwtUtil).generateToken(any());
    }

    @Test
    void testRegister_SetsDefaultRole() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordUtil.isPasswordStrong(anyString())).thenReturn(true);
        when(passwordUtil.hashPassword(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(UserRole.CUSTOMER, user.getRole());
            return testUser;
        });
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When
        authService.register(registerRequest);

        // Then
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any());
    }

    // ===== LOGIN TESTS =====

    @Test
    void testLogin_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When
        LoginResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals(UserRole.CUSTOMER, response.getRole());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordUtil).verifyPassword("Password123", "$2a$10$hashedPassword");
        verify(jwtUtil).generateToken(any());
    }

    @Test
    void testLogin_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Invalid email or password"));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordUtil, never()).verifyPassword(anyString(), anyString());
    }

    @Test
    void testLogin_InvalidPassword() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(anyString(), anyString())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Invalid email or password"));
        verify(passwordUtil).verifyPassword("Password123", "$2a$10$hashedPassword");
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void testLogin_GeneratesJwtToken() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When
        LoginResponse response = authService.login(loginRequest);

        // Then
        assertEquals("jwt.token.here", response.getToken());
        verify(jwtUtil).generateToken(any());
    }

    // ===== GET USER PROFILE TESTS =====

    @Test
    void testGetUserProfile_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserProfileResponse response = authService.getUserProfile(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());
        assertEquals("0123456789", response.getPhone());
        assertEquals("123 Test Street", response.getAddress());
        assertEquals(UserRole.CUSTOMER, response.getRole());

        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserProfile_UserNotFound() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUserProfile(999L);
        });

        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(999L);
    }

    // ===== UPDATE PROFILE TESTS =====

    @Test
    void testUpdateProfile_Success() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
            "Updated Name",
            "9876543210",
            "456 New Street"
        );
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("testuser");
        updatedUser.setEmail("test@example.com");
        updatedUser.setPassword("$2a$10$hashedPassword");
        updatedUser.setFullName("Updated Name");
        updatedUser.setPhone("9876543210");
        updatedUser.setAddress("456 New Street");
        updatedUser.setRole(UserRole.CUSTOMER);
        updatedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserProfileResponse response = authService.updateProfile(1L, request);

        // Then
        assertNotNull(response);
        assertEquals("Updated Name", response.getFullName());
        assertEquals("9876543210", response.getPhone());
        assertEquals("456 New Street", response.getAddress());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateProfile_UserNotFound() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("Updated Name", "9876543210", "456 New Street");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.updateProfile(999L, request);
        });

        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateProfile_DoesNotChangeEmailOrUsername() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("Updated Name", "9876543210", "456 New Street");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            // Email and username should remain unchanged
            assertEquals("test@example.com", user.getEmail());
            assertEquals("testuser", user.getUsername());
            return user;
        });

        // When
        authService.updateProfile(1L, request);

        // Then
        verify(userRepository).save(any(User.class));
    }

    // ===== CHANGE PASSWORD TESTS =====

    @Test
    void testChangePassword_Success() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "NewPassword456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword("OldPassword123", "$2a$10$hashedPassword")).thenReturn(true);
        when(passwordUtil.verifyPassword("NewPassword456", "$2a$10$hashedPassword")).thenReturn(false);
        when(passwordUtil.isPasswordStrong("NewPassword456")).thenReturn(true);
        when(passwordUtil.hashPassword("NewPassword456")).thenReturn("$2a$10$newHashedPassword");

        // When
        authService.changePassword(1L, request);

        // Then
        verify(userRepository).findById(1L);
        verify(passwordUtil).verifyPassword("OldPassword123", "$2a$10$hashedPassword");
        verify(passwordUtil).isPasswordStrong("NewPassword456");
        verify(passwordUtil).hashPassword("NewPassword456");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "NewPassword456");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.changePassword(999L, request);
        });

        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_IncorrectOldPassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("WrongPassword", "NewPassword456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword("WrongPassword", "$2a$10$hashedPassword")).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.changePassword(1L, request);
        });

        assertTrue(exception.getMessage().contains("Old password is incorrect"));
        verify(passwordUtil).verifyPassword("WrongPassword", "$2a$10$hashedPassword");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_WeakNewPassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "weak");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword("OldPassword123", "$2a$10$hashedPassword")).thenReturn(true);
        when(passwordUtil.isPasswordStrong("weak")).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.changePassword(1L, request);
        });

        assertTrue(exception.getMessage().contains("New password must be at least 8 characters"));
        verify(passwordUtil).isPasswordStrong("weak");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_NewPasswordSameAsOld() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("SamePassword123", "SamePassword123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword("SamePassword123", "$2a$10$hashedPassword")).thenReturn(true);
        when(passwordUtil.isPasswordStrong("SamePassword123")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.changePassword(1L, request);
        });

        assertTrue(exception.getMessage().contains("New password must be different from old password"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_HashesNewPassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "NewPassword456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword("OldPassword123", "$2a$10$hashedPassword")).thenReturn(true);
        when(passwordUtil.verifyPassword("NewPassword456", "$2a$10$hashedPassword")).thenReturn(false);
        when(passwordUtil.isPasswordStrong("NewPassword456")).thenReturn(true);
        when(passwordUtil.hashPassword("NewPassword456")).thenReturn("$2a$10$newHashedPassword");

        // When
        authService.changePassword(1L, request);

        // Then
        verify(passwordUtil).hashPassword("NewPassword456");
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testRegisterAndLogin_Integration() {
        // Given - Register
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordUtil.isPasswordStrong(anyString())).thenReturn(true);
        when(passwordUtil.hashPassword(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When - Register
        LoginResponse registerResponse = authService.register(registerRequest);

        // Then - Register
        assertNotNull(registerResponse);
        assertEquals("test@example.com", registerResponse.getEmail());
        assertEquals("jwt.token.here", registerResponse.getToken());

        // Given - Login
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("jwt.token.here");

        // When - Login
        LoginResponse loginResponse = authService.login(loginRequest);

        // Then - Login
        assertNotNull(loginResponse);
        assertEquals("jwt.token.here", loginResponse.getToken());
        assertEquals(registerResponse.getUserId(), loginResponse.getUserId());
    }
}

