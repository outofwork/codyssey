package com.codyssey.api.repository;

import com.codyssey.api.model.ArticleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ArticleLabel entity
 * <p>
 * Provides data access methods for ArticleLabel entities with
 * additional custom query methods for managing article-label relationships.
 */
@Repository
public interface ArticleLabelRepository extends JpaRepository<ArticleLabel, String> {

    /**
     * Find all article labels that are not soft deleted
     *
     * @return List of all non-deleted article labels
     */
    List<ArticleLabel> findByDeletedFalse();

    /**
     * Find article label by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the article label if found and not deleted
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.id = :id AND al.deleted = false")
    Optional<ArticleLabel> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all labels for a specific article
     *
     * @param articleId the article ID
     * @return List of article labels for the article
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.deleted = false")
    List<ArticleLabel> findByArticleId(@Param("articleId") String articleId);

    /**
     * Find all articles that have a specific label
     *
     * @param labelId the label ID
     * @return List of article labels for the label
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.label.id = :labelId AND al.deleted = false")
    List<ArticleLabel> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find primary labels for a specific article
     *
     * @param articleId the article ID
     * @return List of primary article labels for the article
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.isPrimary = true AND al.deleted = false")
    List<ArticleLabel> findPrimaryLabelsByArticleId(@Param("articleId") String articleId);

    /**
     * Find article labels with high relevance score for a specific article
     *
     * @param articleId the article ID
     * @param minRelevanceScore minimum relevance score
     * @return List of highly relevant article labels for the article
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.relevanceScore >= :minRelevanceScore AND al.deleted = false ORDER BY al.relevanceScore DESC")
    List<ArticleLabel> findByArticleIdAndMinRelevanceScore(@Param("articleId") String articleId, @Param("minRelevanceScore") Integer minRelevanceScore);

    /**
     * Check if an article-label relationship exists
     *
     * @param articleId the article ID
     * @param labelId the label ID
     * @return true if the relationship exists
     */
    @Query("SELECT COUNT(al) > 0 FROM ArticleLabel al WHERE al.article.id = :articleId AND al.label.id = :labelId AND al.deleted = false")
    boolean existsByArticleIdAndLabelId(@Param("articleId") String articleId, @Param("labelId") String labelId);

    /**
     * Find article label by article ID and label ID
     *
     * @param articleId the article ID
     * @param labelId the label ID
     * @return Optional containing the article label if found
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.label.id = :labelId AND al.deleted = false")
    Optional<ArticleLabel> findByArticleIdAndLabelId(@Param("articleId") String articleId, @Param("labelId") String labelId);

    /**
     * Find all article labels for a specific article with label details (eager fetch)
     *
     * @param articleId the article ID
     * @return List of article labels with label details
     */
    @Query("SELECT al FROM ArticleLabel al " +
           "LEFT JOIN FETCH al.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE al.article.id = :articleId AND al.deleted = false " +
           "ORDER BY al.isPrimary DESC, al.relevanceScore DESC")
    List<ArticleLabel> findByArticleIdWithLabelDetails(@Param("articleId") String articleId);

    /**
     * Find the most relevant labels for a specific article (top N)
     *
     * @param articleId the article ID
     * @param limit maximum number of labels to return
     * @return List of most relevant article labels
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.deleted = false " +
           "ORDER BY al.isPrimary DESC, al.relevanceScore DESC LIMIT :limit")
    List<ArticleLabel> findTopRelevantLabelsByArticleId(@Param("articleId") String articleId, @Param("limit") Integer limit);

    /**
     * Count labels for a specific article
     *
     * @param articleId the article ID
     * @return count of labels for the article
     */
    @Query("SELECT COUNT(al) FROM ArticleLabel al WHERE al.article.id = :articleId AND al.deleted = false")
    Long countByArticleId(@Param("articleId") String articleId);

    /**
     * Count articles for a specific label
     *
     * @param labelId the label ID
     * @return count of articles for the label
     */
    @Query("SELECT COUNT(al) FROM ArticleLabel al WHERE al.label.id = :labelId AND al.deleted = false")
    Long countByLabelId(@Param("labelId") String labelId);

    /**
     * Delete all article labels for a specific article (soft delete)
     *
     * @param articleId the article ID
     */
    @Query("UPDATE ArticleLabel al SET al.deleted = true WHERE al.article.id = :articleId")
    void softDeleteByArticleId(@Param("articleId") String articleId);

    /**
     * Find articles with a specific label and minimum relevance score
     *
     * @param labelId the label ID
     * @param minRelevanceScore minimum relevance score
     * @return List of article labels matching the criteria
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.label.id = :labelId AND al.relevanceScore >= :minRelevanceScore AND al.deleted = false ORDER BY al.relevanceScore DESC")
    List<ArticleLabel> findByLabelIdAndMinRelevanceScore(@Param("labelId") String labelId, @Param("minRelevanceScore") Integer minRelevanceScore);

    /**
     * Find all primary article labels
     *
     * @return List of all primary article labels
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.isPrimary = true AND al.deleted = false")
    List<ArticleLabel> findAllPrimaryLabels();
}