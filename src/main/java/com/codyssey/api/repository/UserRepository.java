package com.codyssey.api.repository;

import com.codyssey.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * <p>
 * Provides data access methods for User entities with
 * additional custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     *
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    Boolean existsByEmail(String email);

    /**
     * Find all enabled users
     *
     * @return List of enabled users
     */
    List<User> findByEnabledTrue();

    /**
     * Find users by role name
     *
     * @param roleName the role name to search for
     * @return List of users with the specified role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    /**
     * Find users by first name or last name containing the search term
     *
     * @param searchTerm the search term
     * @return List of matching users
     */
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm%")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);
}