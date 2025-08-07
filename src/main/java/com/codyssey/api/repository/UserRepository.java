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
public interface UserRepository extends JpaRepository<User, String> {

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

    /**
     * Find all users that are not soft deleted
     *
     * @return List of all non-deleted users
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findByDeletedFalse();

    /**
     * Find user by ID that is not deleted
     *
     * @param id the user ID
     * @return Optional containing the user if found and not deleted
     */
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find user by URL slug
     *
     * @param urlSlug the URL slug
     * @return Optional containing the user if found
     */
    @Query("SELECT u FROM User u WHERE u.urlSlug = :urlSlug AND u.deleted = false")
    Optional<User> findByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Check if URL slug exists (excluding specific ID)
     *
     * @param urlSlug the URL slug to check
     * @param excludeId the ID to exclude from the check
     * @return true if URL slug exists for a different entity
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.urlSlug = :urlSlug AND u.id != :excludeId AND u.deleted = false")
    boolean existsByUrlSlugAndIdNot(@Param("urlSlug") String urlSlug, @Param("excludeId") String excludeId);

    /**
     * Check if URL slug exists
     *
     * @param urlSlug the URL slug to check
     * @return true if URL slug exists
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.urlSlug = :urlSlug AND u.deleted = false")
    boolean existsByUrlSlug(@Param("urlSlug") String urlSlug);
}