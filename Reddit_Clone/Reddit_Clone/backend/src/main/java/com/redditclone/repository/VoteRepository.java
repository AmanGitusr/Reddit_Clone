package com.redditclone.repository;

import com.redditclone.domain.Post;
import com.redditclone.domain.UserAccount;
import com.redditclone.domain.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Provides database access for vote rows and aggregate scores.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
    /**
     * Finds the existing vote for a user and post pair.
     */
    Optional<Vote> findByUserAndPost(UserAccount user, Post post);

    /**
     * Calculates the score for one post.
     */
    @Query("""
        select coalesce(sum(case when v.type = 'UPVOTE' then 1 when v.type = 'DOWNVOTE' then -1 else 0 end), 0)
        from Vote v
        where v.post.id = :postId
        """)
    long scoreForPost(@Param("postId") Long postId);
}
