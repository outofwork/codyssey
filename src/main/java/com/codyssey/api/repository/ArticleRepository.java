package com.codyssey.api.repository;

import com.codyssey.api.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Article entity
 * <p>
 * Provides data access methods for Article entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    /**
     * Find all articles that are not soft deleted
     *
     * @return List of all non-deleted articles
     */
    List<Article> findByDeletedFalse();

    /**
     * Find article by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the article if found and not deleted
     */
    @Query("SELECT a FROM Article a WHERE a.id = :id AND a.deleted = false")
    Optional<Article> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find article by URL slug
     *
     * @param urlSlug the URL slug
     * @return Optional containing the article if found
     */
    @Query("SELECT a FROM Article a WHERE a.urlSlug = :urlSlug AND a.deleted = false")
    Optional<Article> findByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find article by URL slug with associated labels (eager fetch)
     *
     * @param urlSlug the URL slug
     * @return Optional containing the article with labels if found and not deleted
     */
    @Query("SELECT DISTINCT a FROM Article a " +
           "LEFT JOIN FETCH a.articleLabels al " +
           "LEFT JOIN FETCH al.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE a.urlSlug = :urlSlug AND a.deleted = false")
    Optional<Article> findByUrlSlugWithLabels(@Param("urlSlug") String urlSlug);

    /**
     * Check if URL slug exists (excluding specific ID)
     *
     * @param urlSlug the URL slug to check
     * @param excludeId the ID to exclude from the check
     * @return true if URL slug exists for a different entity
     */
    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE a.urlSlug = :urlSlug AND a.id != :excludeId AND a.deleted = false")
    boolean existsByUrlSlugAndIdNot(@Param("urlSlug") String urlSlug, @Param("excludeId") String excludeId);

    /**
     * Check if URL slug exists
     *
     * @param urlSlug the URL slug to check
     * @return true if URL slug exists
     */
    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE a.urlSlug = :urlSlug AND a.deleted = false")
    boolean existsByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find all articles that are not soft deleted with source eagerly fetched
     *
     * @return List of all non-deleted articles with sources
     */
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.source WHERE a.deleted = false")
    List<Article> findByDeletedFalseWithSource();

    /**
     * Find article by ID with associated labels (eager fetch)
     *
     * @param id the ID to search for
     * @return Optional containing the article with labels if found and not deleted
     */
    @Query("SELECT DISTINCT a FROM Article a " +
           "LEFT JOIN FETCH a.articleLabels al " +
           "LEFT JOIN FETCH al.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE a.id = :id AND a.deleted = false")
    Optional<Article> findByIdWithLabels(@Param("id") String id);

    /**
     * Find all active articles that are not soft deleted
     *
     * @return List of active, non-deleted articles
     */
    @Query("SELECT a FROM Article a WHERE a.status = 'ACTIVE' AND a.deleted = false")
    List<Article> findActiveArticles();

    /**
     * Find articles by source
     *
     * @param sourceId the source ID
     * @return List of articles from the specified source
     */
    @Query("SELECT a FROM Article a WHERE a.source.id = :sourceId AND a.deleted = false")
    List<Article> findBySourceId(@Param("sourceId") String sourceId);

    /**
     * Find articles by source code
     *
     * @param sourceCode the source code (INTERNAL, EXTERNAL_BLOG, etc.)
     * @return List of articles from the specified source
     */
    @Query("SELECT a FROM Article a WHERE UPPER(a.source.code) = UPPER(:sourceCode) AND a.deleted = false")
    List<Article> findBySourceCode(@Param("sourceCode") String sourceCode);

    /**
     * Search articles by title containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching articles
     */
    @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND a.deleted = false")
    List<Article> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Search articles by title or description containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return Page of matching articles
     */
    @Query("SELECT a FROM Article a WHERE (LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND a.deleted = false")
    Page<Article> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find articles by status with pagination
     *
     * @param status the article status
     * @param pageable pagination information
     * @return Page of articles with the specified status
     */
    @Query("SELECT a FROM Article a WHERE a.status = :status AND a.deleted = false")
    Page<Article> findByStatus(@Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * Find articles that have a specific label/tag
     *
     * @param labelId the label ID
     * @return List of articles tagged with the specified label
     */
    @Query("SELECT DISTINCT a FROM Article a JOIN a.articleLabels al WHERE al.label.id = :labelId AND a.deleted = false")
    List<Article> findByLabelId(@Param("labelId") String labelId);

    /**
     * Check if an article title exists within a source (for uniqueness validation)
     *
     * @param title the title to check
     * @param sourceId the source ID
     * @return true if title exists for the source
     */
    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE LOWER(a.title) = LOWER(:title) AND " +
           "a.source.id = :sourceId AND a.deleted = false")
    boolean existsByTitleAndSourceId(@Param("title") String title, @Param("sourceId") String sourceId);

    /**
     * Find articles with pagination
     *
     * @param pageable pagination information
     * @return Page of articles
     */
    @Query("SELECT a FROM Article a WHERE a.deleted = false ORDER BY a.createdAt DESC")
    Page<Article> findArticlesWithPagination(Pageable pageable);

    /**
     * Find articles created by a specific user
     *
     * @param userId the user ID
     * @return List of articles created by the user
     */
    @Query("SELECT a FROM Article a WHERE a.createdByUser.id = :userId AND a.deleted = false")
    List<Article> findByCreatedByUserId(@Param("userId") String userId);

    /**
     * Advanced search with multiple filters including label joins
     *
     * @param sourceIds list of source IDs to filter by (can be null)
     * @param labelIds list of label IDs to filter by (can be null)
     * @param searchTerm search term for title/description (can be null)
     * @return List of articles matching the filters
     */
    @Query("SELECT DISTINCT a FROM Article a " +
           "LEFT JOIN a.articleLabels al " +
           "WHERE a.deleted = false " +
           "AND (:sourceIds IS NULL OR a.source.id IN :sourceIds) " +
           "AND (:labelIds IS NULL OR al.label.id IN :labelIds) " +
           "AND (:searchTerm IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "     OR LOWER(a.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY a.createdAt DESC")
    List<Article> findWithFilters(@Param("sourceIds") List<String> sourceIds,
                                  @Param("labelIds") List<String> labelIds,
                                  @Param("searchTerm") String searchTerm);

    /**
     * Count articles by source
     *
     * @param sourceId the source ID
     * @return count of articles for the source
     */
    @Query("SELECT COUNT(a) FROM Article a WHERE a.source.id = :sourceId AND a.deleted = false")
    Long countBySourceId(@Param("sourceId") String sourceId);

    /**
     * Count articles by status
     *
     * @param status the article status
     * @return count of articles for the status
     */
    @Query("SELECT COUNT(a) FROM Article a WHERE a.status = :status AND a.deleted = false")
    Long countByStatus(@Param("status") Article.ArticleStatus status);

    /**
     * Find all articles ordered by creation date (for summary statistics)
     *
     * @return List of all non-deleted articles ordered by creation date
     */
    @Query("SELECT a FROM Article a WHERE a.deleted = false ORDER BY a.createdAt DESC")
    List<Article> findAllOrderByCreatedAtDesc();
}