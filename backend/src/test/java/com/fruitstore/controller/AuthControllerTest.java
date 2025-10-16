package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.dto.request.auth.ChangePasswordRequest;
import com.fruitstore.dto.request.auth.LoginRequest;
import com.fruitstore.dto.request.auth.RegisterRequest;
import com.fruitstore.dto.request.auth.UpdateProfileRequest;
import com.fruitstore.dto.response.auth.LoginResponse;
import com.fruitstore.dto.response.auth.UserProfileResponse;
import com.fruitstore.security.CustomUserDetails;
import com.fruitstore.security.UserDetailsServiceImpl;
import com.fruitstore.service.AuthService;
import com.fruitstore.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for AuthController REST endpoints
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserProfileResponse userProfileResponse;
    private LoginResponse loginResponse;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
            "testuser",
            "test@example.com",
            "Password123",
            "Test User",
            "0123456789",
            "123 Test Street"
        );

        loginRequest = new LoginRequest("test@example.com", "Password123");

        userProfileResponse = new UserProfileResponse(
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            "0123456789",
            "123 Test Street",
            UserRole.CUSTOMER,
            LocalDateTime.now()
        );

        loginResponse = new LoginResponse(
            "jwt.token.here",
            1L,
            "testuser",
            "test@example.com",
            "Test User",
            UserRole.CUSTOMER
        );

        com.fruitstore.domain.user.User user = new com.fruitstore.domain.user.User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$hashedPassword");
        user.setFullName("Test User");
        user.setRole(UserRole.CUSTOMER);
        customUserDetails = new CustomUserDetails(user);
    }

    // ===== REGISTER ENDPOINT TESTS =====

    @Test
    void testRegister_Success() throws Exception {
        // Given
        when(authService.register(any(RegisterRequest.class))).thenReturn(userProfileResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void testRegister_ValidationError_BlankEmail() throws Exception {
        // Given
        registerRequest.setEmail("");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void testRegister_ValidationError_InvalidEmail() throws Exception {
        // Given
        registerRequest.setEmail("invalid-email");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void testRegister_ValidationError_PasswordTooShort() throws Exception {
        // Given
        registerRequest.setPassword("Pass12");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() throws Exception {
        // Given
        when(authService.register(any(RegisterRequest.class)))
            .thenThrow(new IllegalArgumentException("Email already exists: test@example.com"));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already exists: test@example.com"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    // ===== LOGIN ENDPOINT TESTS =====

    @Test
    void testLogin_Success() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new IllegalArgumentException("Invalid email or password"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void testLogin_ValidationError() throws Exception {
        // Given
        loginRequest.setEmail("");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    // ===== GET PROFILE ENDPOINT TESTS =====

    @Test
    void testGetProfile_Success() throws Exception {
        // Given
        when(authService.getUserProfile(anyLong())).thenReturn(userProfileResponse);

        // When & Then
        mockMvc.perform(get("/api/auth/profile")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.fullName").value("Test User"));

        verify(authService, times(1)).getUserProfile(1L);
    }

    @Test
    void testGetProfile_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/profile"))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).getUserProfile(anyLong());
    }

    // ===== UPDATE PROFILE ENDPOINT TESTS =====

    @Test
    void testUpdateProfile_Success() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
            "Updated Name",
            "9876543210",
            "456 New Street"
        );
        UserProfileResponse updatedResponse = new UserProfileResponse(
            1L,
            "testuser",
            "test@example.com",
            "Updated Name",
            "9876543210",
            "456 New Street",
            UserRole.CUSTOMER,
            LocalDateTime.now()
        );
        when(authService.updateProfile(anyLong(), any(UpdateProfileRequest.class)))
            .thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/api/auth/profile")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile updated successfully"))
                .andExpect(jsonPath("$.data.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.data.phone").value("9876543210"));

        verify(authService, times(1)).updateProfile(anyLong(), any(UpdateProfileRequest.class));
    }

    @Test
    void testUpdateProfile_Unauthorized() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("Updated Name", "9876543210", "456 New Street");

        // When & Then
        mockMvc.perform(put("/api/auth/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).updateProfile(anyLong(), any(UpdateProfileRequest.class));
    }

    @Test
    void testUpdateProfile_ValidationError() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest("", "9876543210", "456 New Street");

        // When & Then
        mockMvc.perform(put("/api/auth/profile")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).updateProfile(anyLong(), any(UpdateProfileRequest.class));
    }

    // ===== CHANGE PASSWORD ENDPOINT TESTS =====

    @Test
    void testChangePassword_Success() throws Exception {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "NewPassword456");
        doNothing().when(authService).changePassword(anyLong(), any(ChangePasswordRequest.class));

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        verify(authService, times(1)).changePassword(anyLong(), any(ChangePasswordRequest.class));
    }

    @Test
    void testChangePassword_Unauthorized() throws Exception {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123", "NewPassword456");

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).changePassword(anyLong(), any(ChangePasswordRequest.class));
    }

    @Test
    void testChangePassword_IncorrectOldPassword() throws Exception {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("WrongPassword", "NewPassword456");
        doThrow(new IllegalArgumentException("Old password is incorrect"))
            .when(authService).changePassword(anyLong(), any(ChangePasswordRequest.class));

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Old password is incorrect"));

        verify(authService, times(1)).changePassword(anyLong(), any(ChangePasswordRequest.class));
    }

    @Test
    void testChangePassword_ValidationError() throws Exception {
        // Given - password too short
        ChangePasswordRequest request = new ChangePasswordRequest("old", "new");

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).changePassword(anyLong(), any(ChangePasswordRequest.class));
    }

    // ===== GET CURRENT USER (/me) ENDPOINT TESTS =====

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetCurrentUser_Success() throws Exception {
        // Given
        when(authService.getUserProfile(anyLong())).thenReturn(userProfileResponse);

        // When & Then
        mockMvc.perform(get("/api/auth/me")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(authService, times(1)).getUserProfile(anyLong());
    }

    @Test
    void testGetCurrentUser_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).getUserProfile(anyLong());
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testRegisterAndLogin_Integration() throws Exception {
        // Given - Register
        when(authService.register(any(RegisterRequest.class))).thenReturn(userProfileResponse);

        // When - Register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        // Given - Login
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When - Login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists());

        verify(authService, times(1)).register(any(RegisterRequest.class));
        verify(authService, times(1)).login(any(LoginRequest.class));
    }
}

