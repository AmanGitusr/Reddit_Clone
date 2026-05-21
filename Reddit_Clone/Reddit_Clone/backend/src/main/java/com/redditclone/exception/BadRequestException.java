package com.redditclone.exception;

/**
 * Signals invalid client input that cannot be handled by bean validation alone.
 */
public class BadRequestException extends RuntimeException {
    /**
     * Creates an exception with a user-facing message.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
