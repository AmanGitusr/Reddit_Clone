package com.redditclone.dto;

import com.redditclone.domain.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * Contains vote request and response payloads.
 */
public final class VoteDtos {
    private VoteDtos() {
    }

    /**
     * Payload used to upvote or downvote a post.
     */
    public record VoteRequest(
            @NotNull Long postId,
            @NotNull VoteType type
    ) {
    }

    /**
     * Payload returned after a vote mutation.
     */
    public record VoteResponse(
            Long postId,
            VoteType type,
            long score
    ) {
    }
}
