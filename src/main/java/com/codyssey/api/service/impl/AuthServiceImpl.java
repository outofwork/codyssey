package com.codyssey.api.service.impl;

import com.codyssey.api.dto.auth.AuthResponseDto;
import com.codyssey.api.dto.auth.LoginDto;
import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.model.Role;
import com.codyssey.api.model.User;
import com.codyssey.api.repository.UserRepository;
import com.codyssey.api.service.AuthService;
import com.codyssey.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of AuthService
 * <p>
 * Provides authentication functionality including
 * login validation and JWT token management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        log.info("Attempting login for user: {}", loginDto.getUsernameOrEmail());

        // Find user by username or email
        User user = findUserByUsernameOrEmail(loginDto.getUsernameOrEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found for: {}", loginDto.getUsernameOrEmail());
                    return new BadCredentialsException("Invalid username/email or password");
                });

        // Check if user account is enabled
        if (!user.getEnabled()) {
            log.warn("Login failed: User account is disabled for: {}", loginDto.getUsernameOrEmail());
            throw new BadCredentialsException("User account is disabled");
        }

        // Validate password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for user: {}", loginDto.getUsernameOrEmail());
            throw new BadCredentialsException("Invalid username/email or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);
        log.info("Login successful for user: {} (ID: {})", user.getUsername(), user.getId());

        // Convert user to DTO
        UserDto userDto = convertToDto(user);

        // Return authentication response
        return new AuthResponseDto(
                token,
                userDto,
                jwtService.getExpirationTime()
        );
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    @Override
    public String getUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }

    /**
     * Find user by username or email
     */
    private Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        // Try to find by username first
        Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);
        if (userByUsername.isPresent()) {
            return userByUsername;
        }

        // If not found by username, try by email
        return userRepository.findByEmail(usernameOrEmail);
    }

    /**
     * Convert User entity to UserDto
     */
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEnabled(user.getEnabled());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        // Convert roles
        if (user.getRoles() != null) {
            userDto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
        }

        return userDto;
    }
}
