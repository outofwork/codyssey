package com.codyssey.api.controller;

import com.codyssey.api.dto.UserDto;
import com.codyssey.api.dto.UserRegistrationDto;
import com.codyssey.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User operations
 * 
 * Provides endpoints for user management including
 * registration, retrieval, updating, and deletion.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
        @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @Parameter(description = "User registration data", required = true)
            @Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        
        log.info("POST /v1/users/register - Registering user: {}", userRegistrationDto.getUsername());
        UserDto createdUser = userService.registerUser(userRegistrationDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /v1/users - Retrieving all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        log.info("GET /v1/users/{} - Retrieving user by ID", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Username", required = true)
            @PathVariable String username) {
        
        log.info("GET /v1/users/username/{} - Retrieving user by username", username);
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user", description = "Updates an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UserDto userDto) {
        
        log.info("PUT /v1/users/{} - Updating user", id);
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        log.info("DELETE /v1/users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enable/Disable user", description = "Enables or disables a user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User status updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<UserDto> setUserEnabled(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Enabled status", required = true)
            @RequestParam Boolean enabled) {
        
        log.info("PATCH /v1/users/{}/enabled - Setting user enabled status to {}", id, enabled);
        UserDto updatedUser = userService.setUserEnabled(id, enabled);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Check username availability", description = "Checks if a username is available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability checked")
    })
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameAvailability(
            @Parameter(description = "Username to check", required = true)
            @PathVariable String username) {
        
        log.info("GET /v1/users/check-username/{} - Checking username availability", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists); // Return true if available (doesn't exist)
    }

    @Operation(summary = "Check email availability", description = "Checks if an email is available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability checked")
    })
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailAvailability(
            @Parameter(description = "Email to check", required = true)
            @PathVariable String email) {
        
        log.info("GET /v1/users/check-email/{} - Checking email availability", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists); // Return true if available (doesn't exist)
    }
}