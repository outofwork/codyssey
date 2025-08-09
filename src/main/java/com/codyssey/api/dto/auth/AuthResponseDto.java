package com.codyssey.api.dto.auth;

import com.codyssey.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for authentication response
 * <p>
 * Contains JWT token and user information returned after successful login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    /**
     * JWT access token
     */
    private String token;

    /**
     * Token type (always "Bearer")
     */
    private String type = "Bearer";

    /**
     * User information
     */
    private UserDto user;

    /**
     * Token expiration time in seconds
     */
    private long expiresIn;

    /**
     * Constructor without token type (defaults to Bearer)
     */
    public AuthResponseDto(String token, UserDto user, long expiresIn) {
        this.token = token;
        this.type = "Bearer";
        this.user = user;
        this.expiresIn = expiresIn;
    }
}
