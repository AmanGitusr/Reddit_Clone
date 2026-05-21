package com.redditclone.repository;

import com.redditclone.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database access for post comments.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Lists comments for a post from oldest to newest.
     */
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}
