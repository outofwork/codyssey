package com.codyssey.api.controller;

import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.dto.user.UserRegistrationDto;
import com.codyssey.api.dto.user.UserUpdateDto;
import com.codyssey.api.service.UserService;
import com.codyssey.api.validation.ValidEmail;
import com.codyssey.api.validation.ValidId;
import com.codyssey.api.validation.ValidUsername;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User operations
 * <p>
 * Provides endpoints for user management including
 * registration, retrieval, updating, and deletion.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    /**
     * Register a new user
     * 
     * @param userRegistrationDto user registration data
     * @return created user with HTTP 201 status
     */
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

    /**
     * Get all users
     * 
     * @return list of all users
     */
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

    /**
     * Get user by ID
     * 
     * @param id user ID
     * @return user if found, 404 if not found
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID (15-character alphanumeric)", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/users/{} - Retrieving user by ID", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get user by username
     * 
     * @param username username
     * @return user if found, 404 if not found
     */
    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Username", required = true)
            @PathVariable @ValidUsername String username) {

        log.info("GET /v1/users/username/{} - Retrieving user by username", username);
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update user
     * 
     * @param id user ID
     * @param userUpdateDto updated user data
     * @return updated user
     */
    @Operation(summary = "Update user", description = "Updates an existing user (username cannot be changed)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID (15-character alphanumeric)", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Updated user data (username cannot be changed, password is optional)", required = true)
            @Valid @RequestBody UserUpdateDto userUpdateDto) {

        log.info("PUT /v1/users/{} - Updating user", id);
        UserDto updatedUser = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete user
     * 
     * @param id user ID
     * @return 204 No Content if successful
     */
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID (15-character alphanumeric)", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Enable/Disable user
     * 
     * @param id user ID
     * @param enabled enabled status
     * @return updated user
     */
    @Operation(summary = "Enable/Disable user", description = "Enables or disables a user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<UserDto> setUserEnabled(
            @Parameter(description = "User ID (15-character alphanumeric)", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Enabled status", required = true)
            @RequestParam Boolean enabled) {

        log.info("PATCH /v1/users/{}/enabled - Setting user enabled status to {}", id, enabled);
        UserDto updatedUser = userService.setUserEnabled(id, enabled);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Check username availability
     * 
     * @param username username to check
     * @return availability status
     */
    @Operation(summary = "Check username availability", description = "Checks if a username is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked")
    })
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameAvailability(
            @Parameter(description = "Username to check", required = true)
            @PathVariable @ValidUsername String username) {

        log.info("GET /v1/users/check-username/{} - Checking username availability", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists); // Return true if available (doesn't exist)
    }

    /**
     * Check email availability
     * 
     * @param email email to check
     * @return availability status
     */
    @Operation(summary = "Check email availability", description = "Checks if an email is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked")
    })
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailAvailability(
            @Parameter(description = "Email to check", required = true)
            @PathVariable @ValidEmail String email) {

        log.info("GET /v1/users/check-email/{} - Checking email availability", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists); // Return true if available (doesn't exist)
    }
}