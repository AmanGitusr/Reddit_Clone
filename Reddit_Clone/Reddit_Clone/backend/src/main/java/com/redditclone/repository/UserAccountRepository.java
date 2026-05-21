package com.redditclone.repository;

import com.redditclone.domain.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database access for registered users.
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    /**
     * Finds a user by the username used for login and JWT identity.
     */
    Optional<UserAccount> findByUsername(String username);

    /**
     * Checks whether an email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Checks whether a username is already registered.
     */
    boolean existsByUsername(String username);
}
