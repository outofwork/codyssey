package com.codyssey.api.repository;

import com.codyssey.api.model.Label;
import com.codyssey.api.model.LabelCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Label entity
 * <p>
 * Provides data access methods for Label entities with
 * additional custom query methods.
 */
@Repository
public interface LabelRepository extends JpaRepository<Label, String> {

    /**
     * Find all labels that are not soft deleted
     *
     * @return List of all non-deleted labels
     */
    List<Label> findByDeletedFalse();

    /**
     * Find label by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the label if found and not deleted
     */
    @Query("SELECT l FROM Label l WHERE l.id = :id AND l.deleted = false")
    Optional<Label> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find label by URL slug
     *
     * @param urlSlug the URL slug
     * @return Optional containing the label if found
     */
    @Query("SELECT l FROM Label l WHERE l.urlSlug = :urlSlug AND l.deleted = false")
    Optional<Label> findByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find label by name (case insensitive) and not deleted
     *
     * @param name the label name
     * @return Optional containing the label if found
     */
    @Query("SELECT l FROM Label l WHERE LOWER(l.name) = LOWER(:name) AND l.deleted = false")
    Optional<Label> findByNameIgnoreCaseAndDeletedFalse(@Param("name") String name);

    /**
     * Check if URL slug exists (excluding specific ID)
     *
     * @param urlSlug the URL slug to check
     * @param excludeId the ID to exclude from the check
     * @return true if URL slug exists for a different entity
     */
    @Query("SELECT COUNT(l) > 0 FROM Label l WHERE l.urlSlug = :urlSlug AND l.id != :excludeId AND l.deleted = false")
    boolean existsByUrlSlugAndIdNot(@Param("urlSlug") String urlSlug, @Param("excludeId") String excludeId);

    /**
     * Check if URL slug exists
     *
     * @param urlSlug the URL slug to check
     * @return true if URL slug exists
     */
    @Query("SELECT COUNT(l) > 0 FROM Label l WHERE l.urlSlug = :urlSlug AND l.deleted = false")
    boolean existsByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find all labels that are not soft deleted with category eagerly fetched
     *
     * @return List of all non-deleted labels with categories
     */
    @Query("SELECT l FROM Label l LEFT JOIN FETCH l.category WHERE l.deleted = false")
    List<Label> findByDeletedFalseWithCategory();

    /**
     * Find all active labels that are not soft deleted
     *
     * @return List of active, non-deleted labels
     */
    @Query("SELECT l FROM Label l WHERE l.active = true AND l.deleted = false")
    List<Label> findActiveLabels();

    /**
     * Find labels by category
     *
     * @param category the category
     * @return List of labels in the category
     */
    @Query("SELECT l FROM Label l WHERE l.category = :category AND l.deleted = false")
    List<Label> findByCategory(@Param("category") LabelCategory category);

    /**
     * Find active labels by category
     *
     * @param category the category
     * @return List of active labels in the category
     */
    @Query("SELECT l FROM Label l WHERE l.category = :category AND l.active = true AND l.deleted = false")
    List<Label> findActiveLabelsByCategory(@Param("category") LabelCategory category);

    /**
     * Find root labels (no parent) by category
     *
     * @param category the category
     * @return List of root labels in the category
     */
    @Query("SELECT l FROM Label l WHERE l.category = :category AND l.parent IS NULL AND l.deleted = false")
    List<Label> findRootLabelsByCategory(@Param("category") LabelCategory category);

    /**
     * Find child labels for a given parent
     *
     * @param parent the parent label
     * @return List of child labels
     */
    @Query("SELECT l FROM Label l WHERE l.parent = :parent AND l.deleted = false")
    List<Label> findByParent(@Param("parent") Label parent);

    /**
     * Find child labels for a given parent ID
     *
     * @param parentId the parent label ID
     * @return List of child labels
     */
    @Query("SELECT l FROM Label l WHERE l.parent.id = :parentId AND l.deleted = false")
    List<Label> findByParentId(@Param("parentId") String parentId);

    /**
     * Search labels by name containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching labels
     */
    @Query("SELECT l FROM Label l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND l.deleted = false")
    List<Label> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Check if a label name exists within a category (for uniqueness validation)
     *
     * @param name       the name to check
     * @param categoryId the category ID
     * @param parentId   the parent ID (can be null for root labels)
     * @return true if name exists in the category/parent combination
     */
    @Query("SELECT COUNT(l) > 0 FROM Label l WHERE LOWER(l.name) = LOWER(:name) AND l.category.id = :categoryId AND " +
            "(:parentId IS NULL AND l.parent IS NULL OR l.parent.id = :parentId) AND l.deleted = false")
    boolean existsByNameAndCategoryAndParent(@Param("name") String name,
                                             @Param("categoryId") String categoryId,
                                             @Param("parentId") String parentId);

    /**
     * Check if a label name exists within a category (excluding a specific label ID for updates)
     *
     * @param name       the name to check
     * @param categoryId the category ID
     * @param parentId   the parent ID (can be null for root labels)
     * @param excludeId  the label ID to exclude from the check
     * @return true if name exists in the category/parent combination (excluding the specified ID)
     */
    @Query("SELECT COUNT(l) > 0 FROM Label l WHERE LOWER(l.name) = LOWER(:name) AND l.category.id = :categoryId AND " +
            "(:parentId IS NULL AND l.parent IS NULL OR l.parent.id = :parentId) AND l.deleted = false AND l.id != :excludeId")
    boolean existsByNameAndCategoryAndParentExcludingId(@Param("name") String name,
                                                        @Param("categoryId") String categoryId,
                                                        @Param("parentId") String parentId,
                                                        @Param("excludeId") String excludeId);

    /**
     * Get all distinct categories that have labels
     *
     * @return List of distinct categories
     */
    @Query("SELECT DISTINCT l.category FROM Label l WHERE l.deleted = false")
    List<LabelCategory> findDistinctCategories();

    /**
     * Get the hierarchy of labels (all root labels with their children)
     *
     * @return List of root labels
     */
    @Query("SELECT l FROM Label l WHERE l.parent IS NULL AND l.deleted = false ORDER BY l.category.name, l.name")
    List<Label> findHierarchy();
}