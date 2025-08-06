package com.codyssey.api.repository;

import com.codyssey.api.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Source entity
 * <p>
 * Provides data access methods for Source entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface SourceRepository extends JpaRepository<Source, String> {

    /**
     * Find all sources that are not soft deleted
     *
     * @return List of all non-deleted sources
     */
    List<Source> findByDeletedFalse();

    /**
     * Find source by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the source if found and not deleted
     */
    @Query("SELECT s FROM Source s WHERE s.id = :id AND s.deleted = false")
    Optional<Source> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all active sources that are not soft deleted
     *
     * @return List of active, non-deleted sources
     */
    @Query("SELECT s FROM Source s WHERE s.isActive = true AND s.deleted = false")
    List<Source> findActiveSource();

    /**
     * Find source by code (case insensitive)
     *
     * @param code the source code
     * @return Optional containing the source if found
     */
    @Query("SELECT s FROM Source s WHERE UPPER(s.code) = UPPER(:code) AND s.deleted = false")
    Optional<Source> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find sources by name containing search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching sources
     */
    @Query("SELECT s FROM Source s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND s.deleted = false")
    List<Source> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Check if a source code exists (for uniqueness validation)
     *
     * @param code the code to check
     * @return true if code exists
     */
    @Query("SELECT COUNT(s) > 0 FROM Source s WHERE UPPER(s.code) = UPPER(:code) AND s.deleted = false")
    boolean existsByCodeIgnoreCase(@Param("code") String code);

    /**
     * Count total number of questions for a source
     *
     * @param sourceId the source ID
     * @return count of questions for the source
     */
    @Query("SELECT COUNT(q) FROM CodingQuestion q WHERE q.source.id = :sourceId AND q.deleted = false")
    Long countQuestionsBySourceId(@Param("sourceId") String sourceId);
}