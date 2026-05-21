package com.redditclone.controller;

import com.redditclone.dto.CommentDtos.CommentRequest;
import com.redditclone.dto.CommentDtos.CommentResponse;
import com.redditclone.dto.PostDtos.PostRequest;
import com.redditclone.dto.PostDtos.PostResponse;
import com.redditclone.service.CommentService;
import com.redditclone.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes post feed, detail, creation, and comment endpoints.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    /**
     * Injects services for post and comment behavior.
     */
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * Creates a post as the authenticated user.
     */
    @PostMapping
    public PostResponse create(@Valid @RequestBody PostRequest request) {
        return postService.create(request);
    }

    /**
     * Lists all posts using latest or popular sorting.
     */
    @GetMapping
    public List<PostResponse> list(@RequestParam(name = "sort", defaultValue = "latest") String sort) {
        return postService.list(sort);
    }

    /**
     * Returns one post by id.
     */
    @GetMapping("/{id}")
    public PostResponse get(@PathVariable("id") Long id) {
        return postService.get(id);
    }

    /**
     * Lists comments for one post.
     */
    @GetMapping("/{id}/comments")
    public List<CommentResponse> comments(@PathVariable("id") Long id) {
        return commentService.listForPost(id);
    }

    /**
     * Creates a comment for one post as the authenticated user.
     */
    @PostMapping("/{id}/comments")
    public CommentResponse createComment(@PathVariable("id") Long id, @Valid @RequestBody CommentRequest request) {
        return commentService.create(id, request);
    }
}
