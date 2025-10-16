package com.fruitstore.security;

import com.fruitstore.domain.user.User;
import com.fruitstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of Spring Security's UserDetailsService
 * Loads user-specific data during authentication
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username (email in our case)
     * 
     * @param username the email of the user
     * @return UserDetails object
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find user by email first
        User user = userRepository.findByEmail(username)
                .orElseGet(() -> 
                    // If not found by email, try by username
                    userRepository.findByUsername(username)
                        .orElseThrow(() -> 
                            new UsernameNotFoundException("User not found with email or username: " + username)
                        )
                );

        return new CustomUserDetails(user);
    }

    /**
     * Load user by user ID
     * 
     * @param userId the user ID
     * @return UserDetails object
     * @throws UsernameNotFoundException if user not found
     */
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with id: " + userId)
                );

        return new CustomUserDetails(user);
    }
}

