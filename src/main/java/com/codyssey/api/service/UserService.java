package com.codyssey.api.service;

import com.codyssey.api.dto.UserDto;
import com.codyssey.api.dto.UserRegistrationDto;
import com.codyssey.api.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations
 * 
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
     * @param id user ID
     * @return user DTO if found
     */
    Optional<UserDto> getUserById(Long id);

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
     * @param id user ID
     * @param userDto updated user data
     * @return updated user DTO
     */
    UserDto updateUser(Long id, UserDto userDto);

    /**
     * Delete user
     * 
     * @param id user ID
     */
    void deleteUser(Long id);

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
     * @param id user ID
     * @param enabled new enabled status
     * @return updated user DTO
     */
    UserDto setUserEnabled(Long id, Boolean enabled);
}