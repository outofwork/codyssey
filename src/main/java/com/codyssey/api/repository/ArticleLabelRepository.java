package com.codyssey.api.repository;

import com.codyssey.api.model.Article;
import com.codyssey.api.model.ArticleLabel;
import com.codyssey.api.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ArticleLabel entity operations
 */
@Repository
public interface ArticleLabelRepository extends JpaRepository<ArticleLabel, String> {

    /**
     * Find all article labels for a specific article
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.deleted = false")
    List<ArticleLabel> findByArticleId(@Param("articleId") String articleId);

    /**
     * Find all article labels for a specific label
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.label.id = :labelId AND al.deleted = false")
    List<ArticleLabel> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find specific article-label relationship
     */
    @Query("SELECT al FROM ArticleLabel al WHERE al.article.id = :articleId AND al.label.id = :labelId AND al.deleted = false")
    Optional<ArticleLabel> findByArticleIdAndLabelId(@Param("articleId") String articleId, @Param("labelId") String labelId);

    /**
     * Check if article-label relationship exists
     */
    @Query("SELECT COUNT(al) > 0 FROM ArticleLabel al WHERE al.article.id = :articleId AND al.label.id = :labelId AND al.deleted = false")
    boolean existsByArticleIdAndLabelId(@Param("articleId") String articleId, @Param("labelId") String labelId);

    /**
     * Find all article labels that are not deleted
     */
    List<ArticleLabel> findByDeletedFalse();

    /**
     * Get label count for each article
     */
    @Query("SELECT al.article.id, COUNT(al) FROM ArticleLabel al WHERE al.deleted = false GROUP BY al.article.id")
    List<Object[]> getLabelCountByArticle();

    /**
     * Get article count for each label
     */
    @Query("SELECT al.label.id, COUNT(al) FROM ArticleLabel al WHERE al.deleted = false GROUP BY al.label.id")
    List<Object[]> getArticleCountByLabel();

    /**
     * Delete all article labels for a specific article (soft delete)
     */
    @Query("UPDATE ArticleLabel al SET al.deleted = true WHERE al.article.id = :articleId AND al.deleted = false")
    void softDeleteByArticleId(@Param("articleId") String articleId);

    /**
     * Delete all article labels for a specific label (soft delete)
     */
    @Query("UPDATE ArticleLabel al SET al.deleted = true WHERE al.label.id = :labelId AND al.deleted = false")
    void softDeleteByLabelId(@Param("labelId") String labelId);
}