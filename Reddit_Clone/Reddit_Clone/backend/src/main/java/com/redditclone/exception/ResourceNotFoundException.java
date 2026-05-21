package com.redditclone.exception;

/**
 * Signals that a requested entity does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Creates an exception with a user-facing message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
