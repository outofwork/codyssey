package com.codyssey.api.repository;

import com.codyssey.api.model.SystemDesign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SystemDesign entity
 * <p>
 * Provides data access methods for SystemDesign entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface SystemDesignRepository extends JpaRepository<SystemDesign, String> {

    /**
     * Find all system designs that are not soft deleted
     *
     * @return List of all non-deleted system designs
     */
    List<SystemDesign> findByDeletedFalse();

    /**
     * Find system design by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the system design if found and not deleted
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.id = :id AND sd.deleted = false")
    Optional<SystemDesign> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find system design by URL slug
     *
     * @param urlSlug the URL slug
     * @return Optional containing the system design if found
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.urlSlug = :urlSlug AND sd.deleted = false")
    Optional<SystemDesign> findByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find system design by URL slug with associated labels (eager fetch)
     *
     * @param urlSlug the URL slug
     * @return Optional containing the system design with labels if found and not deleted
     */
    @Query("SELECT DISTINCT sd FROM SystemDesign sd " +
           "LEFT JOIN FETCH sd.systemDesignLabels sdl " +
           "LEFT JOIN FETCH sdl.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE sd.urlSlug = :urlSlug AND sd.deleted = false")
    Optional<SystemDesign> findByUrlSlugWithLabels(@Param("urlSlug") String urlSlug);

    /**
     * Check if URL slug exists (excluding specific ID)
     *
     * @param urlSlug the URL slug to check
     * @param excludeId the ID to exclude from the check
     * @return true if URL slug exists for a different entity
     */
    @Query("SELECT COUNT(sd) > 0 FROM SystemDesign sd WHERE sd.urlSlug = :urlSlug AND sd.id != :excludeId AND sd.deleted = false")
    boolean existsByUrlSlugAndIdNot(@Param("urlSlug") String urlSlug, @Param("excludeId") String excludeId);

    /**
     * Check if URL slug exists
     *
     * @param urlSlug the URL slug to check
     * @return true if URL slug exists
     */
    @Query("SELECT COUNT(sd) > 0 FROM SystemDesign sd WHERE sd.urlSlug = :urlSlug AND sd.deleted = false")
    boolean existsByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find all system designs that are not soft deleted with source eagerly fetched
     *
     * @return List of all non-deleted system designs with sources
     */
    @Query("SELECT sd FROM SystemDesign sd LEFT JOIN FETCH sd.source WHERE sd.deleted = false")
    List<SystemDesign> findByDeletedFalseWithSource();

    /**
     * Find system design by ID with associated labels (eager fetch)
     *
     * @param id the ID to search for
     * @return Optional containing the system design with labels if found and not deleted
     */
    @Query("SELECT DISTINCT sd FROM SystemDesign sd " +
           "LEFT JOIN FETCH sd.systemDesignLabels sdl " +
           "LEFT JOIN FETCH sdl.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE sd.id = :id AND sd.deleted = false")
    Optional<SystemDesign> findByIdWithLabels(@Param("id") String id);

    /**
     * Find all active system designs that are not soft deleted
     *
     * @return List of active, non-deleted system designs
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.status = 'ACTIVE' AND sd.deleted = false")
    List<SystemDesign> findActiveSystemDesigns();

    /**
     * Find system designs by source
     *
     * @param sourceId the source ID
     * @return List of system designs from the specified source
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.source.id = :sourceId AND sd.deleted = false")
    List<SystemDesign> findBySourceId(@Param("sourceId") String sourceId);

    /**
     * Find system designs by source code
     *
     * @param sourceCode the source code (INTERNAL, EXTERNAL_BLOG, etc.)
     * @return List of system designs from the specified source
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE UPPER(sd.source.code) = UPPER(:sourceCode) AND sd.deleted = false")
    List<SystemDesign> findBySourceCode(@Param("sourceCode") String sourceCode);

    /**
     * Search system designs by title containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching system designs
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE LOWER(sd.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND sd.deleted = false")
    List<SystemDesign> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Search system designs by title or description containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return Page of matching system designs
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE (LOWER(sd.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(sd.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND sd.deleted = false")
    Page<SystemDesign> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find system designs by status with pagination
     *
     * @param status the system design status
     * @param pageable pagination information
     * @return Page of system designs with the specified status
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.status = :status AND sd.deleted = false")
    Page<SystemDesign> findByStatus(@Param("status") SystemDesign.SystemDesignStatus status, Pageable pageable);

    /**
     * Find system designs that have a specific label/tag
     *
     * @param labelId the label ID
     * @return List of system designs tagged with the specified label
     */
    @Query("SELECT DISTINCT sd FROM SystemDesign sd JOIN sd.systemDesignLabels sdl WHERE sdl.label.id = :labelId AND sd.deleted = false")
    List<SystemDesign> findByLabelId(@Param("labelId") String labelId);

    /**
     * Check if a system design title exists within a source (for uniqueness validation)
     *
     * @param title the title to check
     * @param sourceId the source ID
     * @return true if title exists for the source
     */
    @Query("SELECT COUNT(sd) > 0 FROM SystemDesign sd WHERE LOWER(sd.title) = LOWER(:title) AND " +
           "sd.source.id = :sourceId AND sd.deleted = false")
    boolean existsByTitleAndSourceId(@Param("title") String title, @Param("sourceId") String sourceId);

    /**
     * Find system designs with pagination
     *
     * @param pageable pagination information
     * @return Page of system designs
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.deleted = false ORDER BY sd.createdAt DESC")
    Page<SystemDesign> findSystemDesignsWithPagination(Pageable pageable);

    /**
     * Find system designs created by a specific user
     *
     * @param userId the user ID
     * @return List of system designs created by the user
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.createdByUser.id = :userId AND sd.deleted = false")
    List<SystemDesign> findByCreatedByUserId(@Param("userId") String userId);

    /**
     * Advanced search with multiple filters including label joins
     *
     * @param sourceIds list of source IDs to filter by (can be null)
     * @param labelIds list of label IDs to filter by (can be null)
     * @param searchTerm search term for title/description (can be null)
     * @return List of system designs matching the filters
     */
    @Query("SELECT DISTINCT sd FROM SystemDesign sd " +
           "LEFT JOIN sd.systemDesignLabels sdl " +
           "WHERE sd.deleted = false " +
           "AND (:sourceIds IS NULL OR sd.source.id IN :sourceIds) " +
           "AND (:labelIds IS NULL OR sdl.label.id IN :labelIds) " +
           "AND (:searchTerm IS NULL OR LOWER(sd.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "     OR LOWER(sd.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY sd.createdAt DESC")
    List<SystemDesign> findWithFilters(@Param("sourceIds") List<String> sourceIds,
                                      @Param("labelIds") List<String> labelIds,
                                      @Param("searchTerm") String searchTerm);

    /**
     * Count system designs by source
     *
     * @param sourceId the source ID
     * @return count of system designs for the source
     */
    @Query("SELECT COUNT(sd) FROM SystemDesign sd WHERE sd.source.id = :sourceId AND sd.deleted = false")
    Long countBySourceId(@Param("sourceId") String sourceId);

    /**
     * Count system designs by status
     *
     * @param status the system design status
     * @return count of system designs for the status
     */
    @Query("SELECT COUNT(sd) FROM SystemDesign sd WHERE sd.status = :status AND sd.deleted = false")
    Long countByStatus(@Param("status") SystemDesign.SystemDesignStatus status);

    /**
     * Find all system designs ordered by creation date (for summary statistics)
     *
     * @return List of all non-deleted system designs ordered by creation date
     */
    @Query("SELECT sd FROM SystemDesign sd WHERE sd.deleted = false ORDER BY sd.createdAt DESC")
    List<SystemDesign> findAllOrderByCreatedAtDesc();
}
