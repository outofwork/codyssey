package com.codyssey.api.repository;

import com.codyssey.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity
 * <p>
 * Provides data access methods for Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Find role by name
     *
     * @param name the role name to search for
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(String name);

    /**
     * Check if role name exists
     *
     * @param name the role name to check
     * @return true if role name exists, false otherwise
     */
    Boolean existsByName(String name);
}