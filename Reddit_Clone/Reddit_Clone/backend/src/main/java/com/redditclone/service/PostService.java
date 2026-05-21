package com.redditclone.service;

import com.redditclone.domain.Community;
import com.redditclone.domain.Post;
import com.redditclone.domain.UserAccount;
import com.redditclone.dto.PostDtos.PostRequest;
import com.redditclone.dto.PostDtos.PostResponse;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.CommunityRepository;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.VoteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns post creation, feed retrieval, sorting, and DTO mapping.
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final CurrentUserService currentUserService;

    /**
     * Injects repositories and authenticated user access.
     */
    public PostService(
            PostRepository postRepository,
            CommunityRepository communityRepository,
            VoteRepository voteRepository,
            CommentRepository commentRepository,
            CurrentUserService currentUserService
    ) {
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Creates a post in the selected community for the authenticated user.
     */
    @Transactional
    public PostResponse create(PostRequest request) {
        UserAccount author = currentUserService.currentUser();
        Community community = communityRepository.findById(request.communityId())
                .orElseThrow(() -> new ResourceNotFoundException("Community not found"));
        Post post = postRepository.save(new Post(
                request.title().trim(),
                request.content(),
                request.imageUrl(),
                request.linkUrl(),
                author,
                community));
        return toResponse(post);
    }

    /**
     * Lists all posts using latest or popular sorting.
     */
    @Transactional(readOnly = true)
    public List<PostResponse> list(String sort) {
        List<Post> posts = "popular".equalsIgnoreCase(sort)
                ? postRepository.findPopular()
                : postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::toResponse).toList();
    }

    /**
     * Lists community posts using latest or popular sorting.
     */
    @Transactional(readOnly = true)
    public List<PostResponse> listByCommunity(String slug, String sort) {
        List<Post> posts = "popular".equalsIgnoreCase(sort)
                ? postRepository.findPopularByCommunitySlug(slug)
                : postRepository.findByCommunitySlugOrderByCreatedAtDesc(slug);
        return posts.stream().map(this::toResponse).toList();
    }

    /**
     * Returns one post by id.
     */
    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        return toResponse(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found")));
    }

    /**
     * Converts a post into the response shape with score and comment totals.
     */
    PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getLinkUrl(),
                post.getCreatedAt(),
                post.getAuthor().getUsername(),
                post.getCommunity().getId(),
                post.getCommunity().getName(),
                post.getCommunity().getSlug(),
                voteRepository.scoreForPost(post.getId()),
                commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId()).size());
    }
}
