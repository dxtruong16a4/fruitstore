package com.fruitstore.security;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation that wraps our User entity
 * Adapts User entity to Spring Security's UserDetails interface
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert UserRole to Spring Security GrantedAuthority
        String roleName = "ROLE_" + user.getRole().name();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Use email as username for authentication
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // We don't track account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // We don't track account locking
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // We don't track credential expiration
    }

    @Override
    public boolean isEnabled() {
        return true; // All accounts are enabled by default
    }

    /**
     * Get the underlying User entity
     * @return the User entity
     */
    public User getUser() {
        return user;
    }

    /**
     * Get user ID
     * @return user ID
     */
    public Long getUserId() {
        return user.getUserId();
    }

    /**
     * Get user role
     * @return user role
     */
    public UserRole getRole() {
        return user.getRole();
    }

    /**
     * Get full name
     * @return full name
     */
    public String getFullName() {
        return user.getFullName();
    }
}

