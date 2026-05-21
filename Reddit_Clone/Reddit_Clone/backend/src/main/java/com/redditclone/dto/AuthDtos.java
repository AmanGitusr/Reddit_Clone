package com.redditclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Contains authentication request and response payloads.
 */
public final class AuthDtos {
    private AuthDtos() {
    }

    /**
     * Payload used to register a new user.
     */
    public record SignupRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 3, max = 40) String username,
            @NotBlank @Size(min = 6, max = 120) String password
    ) {
    }

    /**
     * Payload used to authenticate an existing user.
     */
    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }

    /**
     * Payload returned after signup or login.
     */
    public record AuthResponse(
            String token,
            Long userId,
            String username,
            String email
    ) {
    }
}
