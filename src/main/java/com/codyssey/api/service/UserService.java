package com.codyssey.api.service;

import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.dto.user.UserRegistrationDto;
import com.codyssey.api.dto.user.UserUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations
 * <p>
 * Defines the contract for user-related business logic operations.
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param userRegistrationDto user registration data
     * @return created user DTO
     */
    UserDto registerUser(UserRegistrationDto userRegistrationDto);

    /**
     * Get all users
     *
     * @return list of all users
     */
    List<UserDto> getAllUsers();

    /**
     * Get user by ID
     *
     * @param id user ID (15-character alphanumeric)
     * @return user DTO if found
     */
    Optional<UserDto> getUserById(String id);

    /**
     * Get user by username
     *
     * @param username the username
     * @return user DTO if found
     */
    Optional<UserDto> getUserByUsername(String username);

    /**
     * Get user by email
     *
     * @param email the email
     * @return user DTO if found
     */
    Optional<UserDto> getUserByEmail(String email);

    /**
     * Update user
     *
     * @param id           user ID (15-character alphanumeric)
     * @param userUpdateDto updated user data (excluding username)
     * @return updated user DTO
     */
    UserDto updateUser(String id, UserUpdateDto userUpdateDto);

    /**
     * Delete user
     *
     * @param id user ID (15-character alphanumeric)
     */
    void deleteUser(String id);

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email the email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Enable/disable user account
     *
     * @param id      user ID (15-character alphanumeric)
     * @param enabled new enabled status
     * @return updated user DTO
     */
    UserDto setUserEnabled(String id, Boolean enabled);
}