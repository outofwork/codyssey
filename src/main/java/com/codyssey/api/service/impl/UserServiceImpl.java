package com.codyssey.api.service.impl;

import com.codyssey.api.dto.UserDto;
import com.codyssey.api.dto.UserRegistrationDto;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.model.Role;
import com.codyssey.api.model.User;
import com.codyssey.api.repository.RoleRepository;
import com.codyssey.api.repository.UserRepository;
import com.codyssey.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of UserService
 * 
 * Provides user management functionality including
 * registration, retrieval, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserRegistrationDto userRegistrationDto) {
        log.info("Registering new user with username: {}", userRegistrationDto.getUsername());

        // Check if username or email already exists
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new DuplicateResourceException("Username is already taken!");
        }

        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new DuplicateResourceException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEnabled(true);

        // Assign default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return convertToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        log.info("Retrieving user by ID: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUsername(String username) {
        log.info("Retrieving user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email is already in use!");
        }

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto setUserEnabled(Long id, Boolean enabled) {
        log.info("Setting user enabled status for ID: {} to {}", id, enabled);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setEnabled(enabled);
        User updatedUser = userRepository.save(user);
        
        log.info("User enabled status updated for ID: {}", id);
        return convertToDto(updatedUser);
    }

    /**
     * Convert User entity to UserDto
     * 
     * @param user the user entity
     * @return user DTO
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);
        
        return dto;
    }
}