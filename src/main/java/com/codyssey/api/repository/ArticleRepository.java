package com.codyssey.api.repository;

import com.codyssey.api.model.Article;
import com.codyssey.api.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Article entity operations
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    /**
     * Find articles that are not deleted
     */
    List<Article> findByDeletedFalse();

    /**
     * Find articles that are not deleted with pagination
     */
    Page<Article> findByDeletedFalse(Pageable pageable);

    /**
     * Find article by URL slug
     */
    Optional<Article> findByUrlSlugAndDeletedFalse(String urlSlug);

    /**
     * Find article by ID (non-deleted)
     */
    @Query("SELECT a FROM Article a WHERE a.id = :id AND a.deleted = false")
    Optional<Article> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Search articles by title or description
     */
    @Query("SELECT a FROM Article a WHERE a.deleted = false AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Article> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find articles by article type
     */
    List<Article> findByArticleTypeAndDeletedFalse(Article.ArticleType articleType);

    /**
     * Find articles by category label
     */
    List<Article> findByCategoryLabelAndDeletedFalse(Label categoryLabel);

    /**
     * Find articles by category label ID
     */
    @Query("SELECT a FROM Article a WHERE a.categoryLabel.id = :categoryLabelId AND a.deleted = false")
    List<Article> findByCategoryLabelId(@Param("categoryLabelId") String categoryLabelId);

    /**
     * Find articles by category label URL slug
     */
    @Query("SELECT a FROM Article a WHERE a.categoryLabel.urlSlug = :categoryLabelSlug AND a.deleted = false")
    List<Article> findByCategoryLabelSlug(@Param("categoryLabelSlug") String categoryLabelSlug);

    /**
     * Find articles by difficulty label
     */
    List<Article> findByDifficultyLabelAndDeletedFalse(Label difficultyLabel);

    /**
     * Find articles by difficulty label ID
     */
    @Query("SELECT a FROM Article a WHERE a.difficultyLabel.id = :difficultyLabelId AND a.deleted = false")
    List<Article> findByDifficultyLabelId(@Param("difficultyLabelId") String difficultyLabelId);

    /**
     * Find articles by difficulty label URL slug
     */
    @Query("SELECT a FROM Article a WHERE a.difficultyLabel.urlSlug = :difficultyLabelSlug AND a.deleted = false")
    List<Article> findByDifficultyLabelSlug(@Param("difficultyLabelSlug") String difficultyLabelSlug);

    /**
     * Find articles by associated label through ArticleLabel relationship
     */
    @Query("SELECT DISTINCT a FROM Article a JOIN a.articleLabels al WHERE al.label.id = :labelId AND a.deleted = false")
    List<Article> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find articles by associated label URL slug through ArticleLabel relationship
     */
    @Query("SELECT DISTINCT a FROM Article a JOIN a.articleLabels al WHERE al.label.urlSlug = :labelSlug AND a.deleted = false")
    List<Article> findByLabelSlug(@Param("labelSlug") String labelSlug);

    /**
     * Find articles by status
     */
    List<Article> findByStatusAndDeletedFalse(Article.ArticleStatus status);

    /**
     * Find articles by created by user
     */
    @Query("SELECT a FROM Article a WHERE a.createdByUser.id = :userId AND a.deleted = false")
    List<Article> findByCreatedByUserId(@Param("userId") String userId);

    /**
     * Check if article title exists within the same article type
     */
    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE LOWER(a.title) = LOWER(:title) AND " +
           "a.articleType = :articleType AND a.deleted = false")
    boolean existsByTitleAndArticleType(@Param("title") String title, @Param("articleType") Article.ArticleType articleType);

    /**
     * Check if article title exists within the same article type excluding specific ID
     */
    @Query("SELECT COUNT(a) > 0 FROM Article a WHERE LOWER(a.title) = LOWER(:title) AND " +
           "a.articleType = :articleType AND a.id != :excludeId AND a.deleted = false")
    boolean existsByTitleAndArticleTypeExcludingId(@Param("title") String title, 
                                                   @Param("articleType") Article.ArticleType articleType,
                                                   @Param("excludeId") String excludeId);

    /**
     * Advanced search with multiple filters
     */
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN a.articleLabels al WHERE a.deleted = false " +
           "AND (:articleTypes IS NULL OR a.articleType IN :articleTypes) " +
           "AND (:categoryLabelIds IS NULL OR a.categoryLabel.id IN :categoryLabelIds) " +
           "AND (:difficultyLabelIds IS NULL OR a.difficultyLabel.id IN :difficultyLabelIds) " +
           "AND (:labelIds IS NULL OR al.label.id IN :labelIds) " +
           "AND (:searchTerm IS NULL OR " +
           "     LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "     LOWER(a.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Article> searchWithFilters(@Param("articleTypes") List<Article.ArticleType> articleTypes,
                                   @Param("categoryLabelIds") List<String> categoryLabelIds,
                                   @Param("difficultyLabelIds") List<String> difficultyLabelIds,
                                   @Param("labelIds") List<String> labelIds,
                                   @Param("searchTerm") String searchTerm);

    /**
     * Get article count by type
     */
    @Query("SELECT a.articleType, COUNT(a) FROM Article a WHERE a.deleted = false GROUP BY a.articleType")
    List<Object[]> getArticleCountByType();

    /**
     * Get recently created articles
     */
    @Query("SELECT a FROM Article a WHERE a.deleted = false ORDER BY a.createdAt DESC")
    Page<Article> findRecentArticles(Pageable pageable);
}