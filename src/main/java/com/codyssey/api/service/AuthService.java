package com.codyssey.api.service;

import com.codyssey.api.dto.auth.AuthResponseDto;
import com.codyssey.api.dto.auth.LoginDto;

/**
 * Service interface for Authentication operations
 * <p>
 * Defines the contract for authentication-related business logic operations.
 */
public interface AuthService {

    /**
     * Authenticate user and generate JWT token
     *
     * @param loginDto login credentials (username/email and password)
     * @return authentication response with JWT token and user details
     */
    AuthResponseDto login(LoginDto loginDto);

    /**
     * Validate JWT token and extract user information
     *
     * @param token JWT token
     * @return true if token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Extract username from JWT token
     *
     * @param token JWT token
     * @return username from token
     */
    String getUsernameFromToken(String token);

    /**
     * Extract user ID from JWT token
     *
     * @param token JWT token
     * @return user ID from token
     */
    String getUserIdFromToken(String token);
}
