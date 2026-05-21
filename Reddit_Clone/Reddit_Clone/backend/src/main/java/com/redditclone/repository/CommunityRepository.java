package com.redditclone.repository;

import com.redditclone.domain.Community;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database access for communities.
 */
public interface CommunityRepository extends JpaRepository<Community, Long> {
    /**
     * Finds a community by its URL-safe slug.
     */
    Optional<Community> findBySlug(String slug);

    /**
     * Checks whether a slug is already taken.
     */
    boolean existsBySlug(String slug);
}
