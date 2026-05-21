package com.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Contains community request and response payloads.
 */
public final class CommunityDtos {
    private CommunityDtos() {
    }

    /**
     * Payload used to create a community.
     */
    public record CommunityRequest(
            @NotBlank @Size(min = 3, max = 80) String name,
            @Size(max = 500) String description
    ) {
    }

    /**
     * Payload returned to frontend community views.
     */
    public record CommunityResponse(
            Long id,
            String name,
            String slug,
            String description,
            Instant createdAt
    ) {
    }
}
