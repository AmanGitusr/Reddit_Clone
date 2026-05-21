package com.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Contains comment request and response payloads.
 */
public final class CommentDtos {
    private CommentDtos() {
    }

    /**
     * Payload used to create a post comment.
     */
    public record CommentRequest(
            @NotBlank @Size(max = 5000) String content
    ) {
    }

    /**
     * Payload returned to post detail comment lists.
     */
    public record CommentResponse(
            Long id,
            String content,
            Instant createdAt,
            String authorUsername
    ) {
    }
}
