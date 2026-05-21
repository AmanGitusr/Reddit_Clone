package com.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Contains post request and response payloads.
 */
public final class PostDtos {
    private PostDtos() {
    }

    /**
     * Payload used to create a post.
     */
    public record PostRequest(
            @NotBlank @Size(max = 180) String title,
            @Size(max = 10000) String content,
            @Size(max = 1000) String imageUrl,
            @Size(max = 1000) String linkUrl,
            @NotNull Long communityId
    ) {
    }

    /**
     * Payload returned to feed and detail views.
     */
    public record PostResponse(
            Long id,
            String title,
            String content,
            String imageUrl,
            String linkUrl,
            Instant createdAt,
            String authorUsername,
            Long communityId,
            String communityName,
            String communitySlug,
            long score,
            int commentCount
    ) {
    }
}
