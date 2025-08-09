package com.codyssey.api.repository;

import com.codyssey.api.model.SystemDesignLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SystemDesignLabel entity
 * <p>
 * Provides data access methods for SystemDesignLabel entities
 * including operations for managing system design-label associations.
 */
@Repository
public interface SystemDesignLabelRepository extends JpaRepository<SystemDesignLabel, String> {

    /**
     * Find all system design labels that are not soft deleted
     *
     * @return List of all non-deleted system design labels
     */
    List<SystemDesignLabel> findByDeletedFalse();

    /**
     * Find system design label by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the system design label if found and not deleted
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl WHERE sdl.id = :id AND sdl.deleted = false")
    Optional<SystemDesignLabel> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all labels for a specific system design
     *
     * @param systemDesignId the system design ID
     * @return List of system design labels for the system design
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl " +
           "LEFT JOIN FETCH sdl.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.deleted = false " +
           "ORDER BY sdl.displayOrder ASC, sdl.createdAt ASC")
    List<SystemDesignLabel> findBySystemDesignId(@Param("systemDesignId") String systemDesignId);

    /**
     * Find primary labels for a specific system design
     *
     * @param systemDesignId the system design ID
     * @return List of primary system design labels for the system design
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl " +
           "LEFT JOIN FETCH sdl.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.isPrimary = true AND sdl.deleted = false " +
           "ORDER BY sdl.displayOrder ASC, sdl.createdAt ASC")
    List<SystemDesignLabel> findPrimaryBySystemDesignId(@Param("systemDesignId") String systemDesignId);

    /**
     * Find all system designs for a specific label
     *
     * @param labelId the label ID
     * @return List of system design labels for the label
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl " +
           "LEFT JOIN FETCH sdl.systemDesign sd " +
           "WHERE sdl.label.id = :labelId AND sdl.deleted = false AND sd.deleted = false " +
           "ORDER BY sd.createdAt DESC")
    List<SystemDesignLabel> findByLabelId(@Param("labelId") String labelId);

    /**
     * Check if system design-label association exists
     *
     * @param systemDesignId the system design ID
     * @param labelId the label ID
     * @return true if association exists
     */
    @Query("SELECT COUNT(sdl) > 0 FROM SystemDesignLabel sdl " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.label.id = :labelId AND sdl.deleted = false")
    boolean existsBySystemDesignIdAndLabelId(@Param("systemDesignId") String systemDesignId, @Param("labelId") String labelId);

    /**
     * Find specific system design-label association
     *
     * @param systemDesignId the system design ID
     * @param labelId the label ID
     * @return Optional containing the system design label if found
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.label.id = :labelId AND sdl.deleted = false")
    Optional<SystemDesignLabel> findBySystemDesignIdAndLabelId(@Param("systemDesignId") String systemDesignId, 
                                                               @Param("labelId") String labelId);

    /**
     * Count labels for a specific system design
     *
     * @param systemDesignId the system design ID
     * @return count of labels for the system design
     */
    @Query("SELECT COUNT(sdl) FROM SystemDesignLabel sdl " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.deleted = false")
    Long countBySystemDesignId(@Param("systemDesignId") String systemDesignId);

    /**
     * Count system designs for a specific label
     *
     * @param labelId the label ID
     * @return count of system designs for the label
     */
    @Query("SELECT COUNT(sdl) FROM SystemDesignLabel sdl " +
           "WHERE sdl.label.id = :labelId AND sdl.deleted = false")
    Long countByLabelId(@Param("labelId") String labelId);

    /**
     * Find labels by category for a specific system design
     *
     * @param systemDesignId the system design ID
     * @param categoryId the category ID
     * @return List of system design labels in the specified category
     */
    @Query("SELECT sdl FROM SystemDesignLabel sdl " +
           "LEFT JOIN FETCH sdl.label l " +
           "LEFT JOIN FETCH l.category c " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND c.id = :categoryId AND sdl.deleted = false " +
           "ORDER BY sdl.displayOrder ASC, sdl.createdAt ASC")
    List<SystemDesignLabel> findBySystemDesignIdAndCategoryId(@Param("systemDesignId") String systemDesignId, 
                                                              @Param("categoryId") String categoryId);

    /**
     * Delete system design-label association (soft delete)
     *
     * @param systemDesignId the system design ID
     * @param labelId the label ID
     */
    @Query("UPDATE SystemDesignLabel sdl SET sdl.deleted = true, sdl.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.label.id = :labelId")
    void deleteBySystemDesignIdAndLabelId(@Param("systemDesignId") String systemDesignId, @Param("labelId") String labelId);

    /**
     * Get the maximum display order for a system design
     *
     * @param systemDesignId the system design ID
     * @return the maximum display order value
     */
    @Query("SELECT COALESCE(MAX(sdl.displayOrder), 0) FROM SystemDesignLabel sdl " +
           "WHERE sdl.systemDesign.id = :systemDesignId AND sdl.deleted = false")
    Integer getMaxDisplayOrderBySystemDesignId(@Param("systemDesignId") String systemDesignId);
}
