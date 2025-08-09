package com.codyssey.api.controller;

import com.codyssey.api.dto.auth.AuthResponseDto;
import com.codyssey.api.dto.auth.LoginDto;
import com.codyssey.api.security.JwtAuthenticationDetails;
import com.codyssey.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Authentication operations
 * <p>
 * Provides endpoints for user authentication including
 * login, logout, and token validation.
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {

    private final AuthService authService;

    /**
     * User login
     * 
     * @param loginDto login credentials
     * @return JWT token and user information
     */
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or account disabled")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Parameter(description = "Login credentials (username/email and password)", required = true)
            @Valid @RequestBody LoginDto loginDto) {

        log.info("POST /v1/auth/login - Login attempt for: {}", loginDto.getUsernameOrEmail());
        AuthResponseDto authResponse = authService.login(loginDto);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Logout user (client-side token invalidation)
     * 
     * @return logout confirmation
     */
    @Operation(summary = "User logout", description = "Logout user (client should discard the token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        
        log.info("POST /v1/auth/logout - User logout: {}", username);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("instruction", "Please discard the JWT token on the client side");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Validate current JWT token and get user info
     * 
     * @return current user information from token
     */
    @Operation(summary = "Validate token", description = "Validates current JWT token and returns user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", auth.getName());
        userInfo.put("authorities", auth.getAuthorities());
        userInfo.put("authenticated", auth.isAuthenticated());

        // Add user ID if available
        if (auth.getDetails() instanceof JwtAuthenticationDetails details) {
            userInfo.put("userId", details.getUserId());
        }

        log.debug("GET /v1/auth/me - Current user info retrieved for: {}", auth.getName());
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Health check for authentication service
     * 
     * @return service status
     */
    @Operation(summary = "Auth service health check", description = "Checks if authentication service is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Authentication Service");
        health.put("timestamp", java.time.Instant.now().toString());
        
        return ResponseEntity.ok(health);
    }
}
