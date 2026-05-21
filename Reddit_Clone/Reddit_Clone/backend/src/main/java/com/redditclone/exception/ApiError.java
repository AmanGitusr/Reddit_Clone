package com.redditclone.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error shape returned by every API failure handler.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        Map<String, String> details
) {
}
