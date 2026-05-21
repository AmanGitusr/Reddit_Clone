package com.redditclone.service;

import com.redditclone.domain.UserAccount;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Resolves the currently authenticated user from Spring Security.
 */
@Service
public class CurrentUserService {
    private final UserAccountRepository userRepository;

    /**
     * Injects the repository used to hydrate the authenticated username.
     */
    public CurrentUserService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the persisted account for the active JWT principal.
     */
    public UserAccount currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user no longer exists"));
    }
}
