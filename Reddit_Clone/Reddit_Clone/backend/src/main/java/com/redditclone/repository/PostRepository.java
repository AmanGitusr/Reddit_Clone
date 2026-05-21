package com.redditclone.repository;

import com.redditclone.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Provides database access for posts and feed sorting queries.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Returns community posts with newest posts first.
     */
    List<Post> findByCommunitySlugOrderByCreatedAtDesc(String slug);

    /**
     * Returns all posts with newest posts first.
     */
    List<Post> findAllByOrderByCreatedAtDesc();

    /**
     * Returns all posts ordered by computed vote score and recency.
     */
    @Query("""
        select p from Post p
        left join Vote v on v.post = p
        group by p
        order by coalesce(sum(case when v.type = 'UPVOTE' then 1 when v.type = 'DOWNVOTE' then -1 else 0 end), 0) desc,
                 p.createdAt desc
        """)
    List<Post> findPopular();

    /**
     * Returns community posts ordered by computed vote score and recency.
     */
    @Query("""
        select p from Post p
        left join Vote v on v.post = p
        where p.community.slug = :slug
        group by p
        order by coalesce(sum(case when v.type = 'UPVOTE' then 1 when v.type = 'DOWNVOTE' then -1 else 0 end), 0) desc,
                 p.createdAt desc
        """)
    List<Post> findPopularByCommunitySlug(@Param("slug") String slug);
}
