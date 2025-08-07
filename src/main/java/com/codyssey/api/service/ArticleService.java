package com.codyssey.api.service;

import com.codyssey.api.dto.article.*;
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
     * Get articles by source
     *
     * @param sourceId source ID
     * @return list of articles from the source
     */
    List<ArticleSummaryDto> getArticlesBySource(String sourceId);

    /**
     * Get articles by label/tag
     *
     * @param labelId label ID
     * @return list of articles tagged with the specified label
     */
    List<ArticleSummaryDto> getArticlesByLabel(String labelId);

    /**
     * Get articles by label/tag URL slug
     *
     * @param labelSlug label URL slug
     * @return list of articles tagged with the specified label
     */
    List<ArticleSummaryDto> getArticlesByLabelSlug(String labelSlug);

    /**
     * Get articles by source URL slug
     *
     * @param sourceSlug source URL slug
     * @return list of articles from the specified source
     */
    List<ArticleSummaryDto> getArticlesBySourceSlug(String sourceSlug);

    /**
     * Advanced search with multiple filters
     *
     * @param sourceIds list of source IDs to filter by
     * @param labelIds list of label IDs to filter by
     * @param searchTerm search term for title/description
     * @return list of articles matching the filters
     */
    List<ArticleSummaryDto> searchWithFilters(List<String> sourceIds,
                                              List<String> labelIds,
                                              String searchTerm);

    /**
     * Get article statistics
     *
     * @return article statistics
     */
    ArticleStatisticsDto getArticleStatistics();

    /**
     * Check if an article title is available within a source
     *
     * @param title article title
     * @param sourceId source ID
     * @return true if title is available
     */
    boolean checkTitleAvailability(String title, String sourceId);

    /**
     * Add a label to an article
     *
     * @param createDto article-label relationship data
     */
    void addLabelToArticle(ArticleLabelCreateDto createDto);

    /**
     * Add multiple labels to an article
     *
     * @param bulkCreateDto bulk article-label relationship data
     */
    void addLabelsToArticle(ArticleLabelBulkCreateDto bulkCreateDto);

    /**
     * Remove a label from an article
     *
     * @param articleId article ID
     * @param labelId label ID
     */
    void removeLabelFromArticle(String articleId, String labelId);

    /**
     * Get the markdown content of an article
     *
     * @param id article ID
     * @return markdown content of the article
     * @throws Exception if file cannot be read
     */
    String getArticleContent(String id) throws Exception;

    /**
     * Get the markdown content of an article by URL slug
     *
     * @param urlSlug article URL slug
     * @return markdown content of the article
     * @throws Exception if file cannot be read
     */
    String getArticleContentByUrlSlug(String urlSlug) throws Exception;

    /**
     * Get all labels for a specific article
     *
     * @param articleId article ID
     * @return list of article-label relationships
     */
    List<ArticleLabelReferenceDto> getArticleLabels(String articleId);

    /**
     * Get primary labels for a specific article
     *
     * @param articleId article ID
     * @return list of primary article-label relationships
     */
    List<ArticleLabelReferenceDto> getPrimaryArticleLabels(String articleId);
}