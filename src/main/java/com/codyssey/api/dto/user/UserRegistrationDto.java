package com.codyssey.api.dto.user;

import com.codyssey.api.validation.ValidEmail;
import com.codyssey.api.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User registration
 * <p>
 * Used for capturing user registration information
 * including password with enhanced global standard validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Username is required")
    @ValidUsername
    private String username;

    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character (@$!%*?&)"
    )
    private String password;

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
}