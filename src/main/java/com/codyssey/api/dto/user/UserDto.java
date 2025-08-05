package com.codyssey.api.dto.user;

import com.codyssey.api.validation.ValidEmail;
import com.codyssey.api.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object for User entity
 * <p>
 * Used for transferring user data between layers
 * without exposing sensitive information with enhanced validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotBlank(message = "Username is required")
    @ValidUsername
    private String username;

    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s'-]+$|^$",
            message = "First name can only contain letters, spaces, apostrophes, and hyphens"
    )
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s'-]+$|^$",
            message = "Last name can only contain letters, spaces, apostrophes, and hyphens"
    )
    private String lastName;

    private Boolean enabled;

    private Set<String> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}