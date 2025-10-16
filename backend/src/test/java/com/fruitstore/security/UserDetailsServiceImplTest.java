package com.fruitstore.security;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import com.fruitstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserDetailsServiceImpl
 * Tests user loading functionality
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$hashedPassword");
        testUser.setFullName("Test User");
        testUser.setRole(UserRole.CUSTOMER);
    }

    @Test
    void testLoadUserByUsername_WithEmail_Success() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_WithUsername_Success() {
        // Given
        when(userRepository.findByEmail("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("test@example.com", userDetails.getUsername()); // Email is used as username
        verify(userRepository, times(1)).findByEmail("testuser");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
        verify(userRepository, times(1)).findByUsername("nonexistent@example.com");
    }

    @Test
    void testLoadUserByUsername_ExceptionMessage() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        assertTrue(exception.getMessage().contains("User not found with email or username"));
        assertTrue(exception.getMessage().contains("nonexistent@example.com"));
    }

    @Test
    void testLoadUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserById(1L);

        // Then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals(1L, ((CustomUserDetails) userDetails).getUserId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testLoadUserById_NotFound() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserById(999L);
        });

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testLoadUserById_ExceptionMessage() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserById(999L);
        });

        assertTrue(exception.getMessage().contains("User not found with id"));
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void testLoadUserByUsername_ReturnsCustomUserDetails() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Then
        assertInstanceOf(CustomUserDetails.class, userDetails);
        CustomUserDetails customDetails = (CustomUserDetails) userDetails;
        assertEquals(testUser, customDetails.getUser());
        assertEquals(UserRole.CUSTOMER, customDetails.getRole());
    }

    @Test
    void testLoadUserByUsername_AdminRole() {
        // Given
        testUser.setRole(UserRole.ADMIN);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_MultipleCallsWithSameUsername() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails1 = userDetailsService.loadUserByUsername("test@example.com");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("test@example.com");

        // Then
        assertNotNull(userDetails1);
        assertNotNull(userDetails2);
        assertEquals(userDetails1.getUsername(), userDetails2.getUsername());
        verify(userRepository, times(2)).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserById_ReturnsCorrectUserDetails() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserById(1L);
        CustomUserDetails customDetails = (CustomUserDetails) userDetails;

        // Then
        assertEquals(1L, customDetails.getUserId());
        assertEquals("Test User", customDetails.getFullName());
        assertEquals(UserRole.CUSTOMER, customDetails.getRole());
    }

    @Test
    void testRepositoryInteraction() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        userDetailsService.loadUserByUsername("test@example.com");

        // Then
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, never()).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }
}

