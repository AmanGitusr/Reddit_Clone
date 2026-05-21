package com.redditclone.security;

import com.redditclone.domain.UserAccount;
import com.redditclone.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adapts application users to Spring Security's authentication contract.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userRepository;

    /**
     * Injects the user repository used during login and JWT validation.
     */
    public AppUserDetailsService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username for Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities("USER")
                .build();
    }
}
