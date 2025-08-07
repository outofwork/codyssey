package com.codyssey.api.service;

import com.codyssey.api.dto.article.*;
import com.codyssey.api.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Article operations
 * <p>
 * Defines the contract for article-related business logic operations.
 */
public interface ArticleService {

    /**
     * Create a new article
     *
     * @param createDto article creation data
     * @return created article DTO
     */
    ArticleDto createArticle(ArticleCreateDto createDto);

    /**
     * Get all articles (non-deleted)
     *
     * @return list of all articles
     */
    List<ArticleSummaryDto> getAllArticles();

    /**
     * Get article by ID
     *
     * @param id article ID
     * @return article DTO if found
     */
    Optional<ArticleDto> getArticleById(String id);

    /**
     * Get article by URL slug
     *
     * @param urlSlug article URL slug
     * @return article DTO if found
     */
    Optional<ArticleDto> getArticleByUrlSlug(String urlSlug);

    /**
     * Update article
     *
     * @param id article ID
     * @param updateDto updated article data
     * @return updated article DTO
     */
    ArticleDto updateArticle(String id, ArticleUpdateDto updateDto);

    /**
     * Update article by URL slug
     *
     * @param urlSlug article URL slug
     * @param updateDto updated article data
     * @return updated article DTO
     */
    ArticleDto updateArticleByUrlSlug(String urlSlug, ArticleUpdateDto updateDto);

    /**
     * Soft delete article
     *
     * @param id article ID
     */
    void deleteArticle(String id);

    /**
     * Soft delete article by URL slug
     *
     * @param urlSlug article URL slug
     */
    void deleteArticleByUrlSlug(String urlSlug);

    /**
     * Get articles with pagination
     *
     * @param pageable pagination information
     * @return paginated articles
     */
    Page<ArticleSummaryDto> getArticlesWithPagination(Pageable pageable);

    /**
     * Search articles by title or description
     *
     * @param searchTerm search term
     * @param pageable pagination information
     * @return paginated search results
     */
    Page<ArticleSummaryDto> searchArticles(String searchTerm, Pageable pageable);

    /**
     * Get articles by article type
     *
     * @param articleType article type
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByType(String articleType);

    /**
     * Get articles by category label ID
     *
     * @param categoryLabelId category label ID
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByCategoryLabelId(String categoryLabelId);

    /**
     * Get articles by category label slug
     *
     * @param categoryLabelSlug category label slug
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByCategoryLabelSlug(String categoryLabelSlug);

    /**
     * Get articles by difficulty label ID
     *
     * @param difficultyLabelId difficulty label ID
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByDifficultyLabelId(String difficultyLabelId);

    /**
     * Get articles by difficulty label slug
     *
     * @param difficultyLabelSlug difficulty label slug
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByDifficultyLabelSlug(String difficultyLabelSlug);

    /**
     * Get articles by associated label ID
     *
     * @param labelId label ID
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByLabelId(String labelId);

    /**
     * Get articles by associated label slug
     *
     * @param labelSlug label slug
     * @return list of articles
     */
    List<ArticleSummaryDto> getArticlesByLabelSlug(String labelSlug);

    /**
     * Create article-label relationship
     *
     * @param createDto article-label creation data
     * @return success status
     */
    boolean createArticleLabel(ArticleLabelCreateDto createDto);

    /**
     * Create multiple article-label relationships
     *
     * @param bulkCreateDto bulk creation data
     * @return number of created relationships
     */
    int createArticleLabels(ArticleLabelBulkCreateDto bulkCreateDto);

    /**
     * Remove article-label relationship
     *
     * @param articleId article ID
     * @param labelId label ID
     * @return success status
     */
    boolean removeArticleLabel(String articleId, String labelId);

    /**
     * Get article statistics
     *
     * @return article statistics
     */
    ArticleStatisticsDto getArticleStatistics();

    /**
     * Get recent articles
     *
     * @param pageable pagination information
     * @return recent articles
     */
    Page<ArticleSummaryDto> getRecentArticles(Pageable pageable);

    /**
     * Advanced search with filters
     *
     * @param articleTypes list of article types
     * @param categoryLabelIds list of category label IDs
     * @param difficultyLabelIds list of difficulty label IDs
     * @param labelIds list of label IDs
     * @param searchTerm search term
     * @return filtered articles
     */
    List<ArticleSummaryDto> searchWithFilters(List<String> articleTypes,
                                             List<String> categoryLabelIds,
                                             List<String> difficultyLabelIds,
                                             List<String> labelIds,
                                             String searchTerm);

    /**
     * Increment view count
     *
     * @param id article ID
     */
    void incrementViewCount(String id);

    /**
     * Increment like count
     *
     * @param id article ID
     */
    void incrementLikeCount(String id);

    /**
     * Increment bookmark count
     *
     * @param id article ID
     */
    void incrementBookmarkCount(String id);

    /**
     * Convert entity to DTO
     *
     * @param article article entity
     * @return article DTO
     */
    ArticleDto convertToDto(Article article);

    /**
     * Convert entity to summary DTO
     *
     * @param article article entity
     * @return article summary DTO
     */
    ArticleSummaryDto convertToSummaryDto(Article article);
}