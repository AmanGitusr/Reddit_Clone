package com.redditclone.service;

import com.redditclone.domain.Comment;
import com.redditclone.domain.Post;
import com.redditclone.domain.UserAccount;
import com.redditclone.dto.CommentDtos.CommentRequest;
import com.redditclone.dto.CommentDtos.CommentResponse;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns comment creation and retrieval for post detail screens.
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;

    /**
     * Injects comment persistence, post lookup, and authenticated user access.
     */
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CurrentUserService currentUserService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Adds a comment to a post as the authenticated user.
     */
    @Transactional
    public CommentResponse create(Long postId, CommentRequest request) {
        UserAccount author = currentUserService.currentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return toResponse(commentRepository.save(new Comment(request.content().trim(), author, post)));
    }

    /**
     * Lists post comments in conversation order.
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> listForPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converts a comment entity to the API response shape.
     */
    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getAuthor().getUsername());
    }
}
