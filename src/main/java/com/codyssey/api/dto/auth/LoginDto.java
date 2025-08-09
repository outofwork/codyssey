package com.codyssey.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login
 * <p>
 * Used for capturing user login credentials.
 * Supports login with either username or email.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    /**
     * Username or email for authentication
     */
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    /**
     * User password
     */
    @NotBlank(message = "Password is required")
    private String password;
}
