package com.redditclone.domain;

/**
 * Represents a user's stance on a post and maps directly to score deltas.
 */
public enum VoteType {
    UPVOTE(1),
    DOWNVOTE(-1);

    private final int score;

    VoteType(int score) {
        this.score = score;
    }

    /**
     * Returns the numeric score contribution for this vote type.
     */
    public int score() {
        return score;
    }
}
