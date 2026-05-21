package com.redditclone.controller;

import com.redditclone.dto.CommunityDtos.CommunityRequest;
import com.redditclone.dto.CommunityDtos.CommunityResponse;
import com.redditclone.dto.PostDtos.PostResponse;
import com.redditclone.service.CommunityService;
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
 * Exposes community browse and creation endpoints.
 */
@RestController
@RequestMapping("/api/communities")
public class CommunityController {
    private final CommunityService communityService;
    private final PostService postService;

    /**
     * Injects services for community data and community feed data.
     */
    public CommunityController(CommunityService communityService, PostService postService) {
        this.communityService = communityService;
        this.postService = postService;
    }

    /**
     * Creates a community for an authenticated user.
     */
    @PostMapping
    public CommunityResponse create(@Valid @RequestBody CommunityRequest request) {
        return communityService.create(request);
    }

    /**
     * Lists all communities.
     */
    @GetMapping
    public List<CommunityResponse> list() {
        return communityService.list();
    }

    /**
     * Returns one community by slug.
     */
    @GetMapping("/{slug}")
    public CommunityResponse get(@PathVariable("slug") String slug) {
        return communityService.getBySlug(slug);
    }

    /**
     * Returns posts for a specific community.
     */
    @GetMapping("/{slug}/posts")
    public List<PostResponse> posts(@PathVariable("slug") String slug, @RequestParam(name = "sort", defaultValue = "latest") String sort) {
        return postService.listByCommunity(slug, sort);
    }
}
