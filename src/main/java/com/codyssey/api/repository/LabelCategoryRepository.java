package com.codyssey.api.repository;

import com.codyssey.api.model.LabelCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for LabelCategory entity
 * <p>
 * Provides data access methods for LabelCategory entities with
 * additional custom query methods.
 */
@Repository
public interface LabelCategoryRepository extends JpaRepository<LabelCategory, String> {

    /**
     * Find label category by code
     *
     * @param code the code to search for
     * @return Optional containing the label category if found
     */
    Optional<LabelCategory> findByCode(String code);

    /**
     * Check if code exists
     *
     * @param code the code to check
     * @return true if code exists, false otherwise
     */
    Boolean existsByCode(String code);

    /**
     * Find all active label categories
     *
     * @return List of active label categories
     */
    List<LabelCategory> findByActiveTrue();

    /**
     * Find all categories (active and inactive) that are not soft deleted
     *
     * @return List of all non-deleted label categories
     */
    List<LabelCategory> findByDeletedFalse();

    /**
     * Find active categories that are not soft deleted
     *
     * @return List of active, non-deleted label categories
     */
    @Query("SELECT lc FROM LabelCategory lc WHERE lc.active = true AND lc.deleted = false")
    List<LabelCategory> findActiveCategories();

    /**
     * Search label categories by name containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching label categories
     */
    @Query("SELECT lc FROM LabelCategory lc WHERE LOWER(lc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND lc.deleted = false")
    List<LabelCategory> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Find label category by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the label category if found and not deleted
     */
    @Query("SELECT lc FROM LabelCategory lc WHERE lc.id = :id AND lc.deleted = false")
    Optional<LabelCategory> findByIdAndNotDeleted(@Param("id") String id);
}