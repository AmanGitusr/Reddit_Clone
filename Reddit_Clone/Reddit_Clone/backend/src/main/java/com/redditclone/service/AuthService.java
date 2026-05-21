package com.redditclone.service;

import com.redditclone.domain.UserAccount;
import com.redditclone.dto.AuthDtos.AuthResponse;
import com.redditclone.dto.AuthDtos.LoginRequest;
import com.redditclone.dto.AuthDtos.SignupRequest;
import com.redditclone.exception.BadRequestException;
import com.redditclone.repository.UserAccountRepository;
import com.redditclone.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Owns signup, login, password hashing, and JWT issuance.
 */
@Service
public class AuthService {
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Injects dependencies for credential persistence and authentication.
     */
    public AuthService(
            UserAccountRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user and returns an immediate login token.
     */
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }
        UserAccount user = userRepository.save(
                new UserAccount(email, username, passwordEncoder.encode(request.password())));
        return response(user);
    }

    /**
     * Authenticates an existing user and returns a fresh JWT.
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserAccount user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));
        return response(user);
    }

    /**
     * Converts a user into the shared auth response payload.
     */
    private AuthResponse response(UserAccount user) {
        return new AuthResponse(jwtService.generateToken(user.getUsername()), user.getId(), user.getUsername(), user.getEmail());
    }
}
