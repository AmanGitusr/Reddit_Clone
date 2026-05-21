package com.redditclone.service;

import com.redditclone.domain.Community;
import com.redditclone.dto.CommunityDtos.CommunityRequest;
import com.redditclone.dto.CommunityDtos.CommunityResponse;
import com.redditclone.exception.BadRequestException;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.CommunityRepository;
import java.text.Normalizer;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns community creation, lookup, and DTO mapping.
 */
@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    /**
     * Injects community persistence.
     */
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    /**
     * Creates a community with a unique URL-safe slug derived from its name.
     */
    @Transactional
    public CommunityResponse create(CommunityRequest request) {
        String slug = slugify(request.name());
        if (communityRepository.existsBySlug(slug)) {
            throw new BadRequestException("A community with that name already exists");
        }
        return toResponse(communityRepository.save(new Community(request.name().trim(), slug, request.description())));
    }

    /**
     * Lists all communities for browse screens.
     */
    public List<CommunityResponse> list() {
        return communityRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Looks up a community by slug.
     */
    public CommunityResponse getBySlug(String slug) {
        return toResponse(communityRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found")));
    }

    /**
     * Produces a lower-case slug from display text.
     */
    private String slugify(String name) {
        String normalized = Normalizer.normalize(name.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (normalized.isBlank()) {
            throw new BadRequestException("Community name must contain letters or numbers");
        }
        return normalized;
    }

    /**
     * Converts a community entity into the API response shape.
     */
    private CommunityResponse toResponse(Community community) {
        return new CommunityResponse(
                community.getId(),
                community.getName(),
                community.getSlug(),
                community.getDescription(),
                community.getCreatedAt());
    }
}
