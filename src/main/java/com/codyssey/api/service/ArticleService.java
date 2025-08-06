package com.codyssey.api.service;

import com.codyssey.api.dto.article.ArticleDto;
import com.codyssey.api.dto.article.ArticleSummaryDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Article operations
 * <p>
 * Handles file-based article management, content loading, and metadata operations.
 */
public interface ArticleService {

    /**
     * Get all articles (summary view)
     *
     * @return list of article summaries
     */
    List<ArticleSummaryDto> getAllArticles();

    /**
     * Get articles by category
     *
     * @param category the category to filter by
     * @return list of article summaries in the category
     */
    List<ArticleSummaryDto> getArticlesByCategory(String category);

    /**
     * Get article by ID (full content)
     *
     * @param id the article ID
     * @return article with full content if found
     */
    Optional<ArticleDto> getArticleById(String id);

    /**
     * Get article by slug (full content)
     *
     * @param slug the article slug
     * @return article with full content if found
     */
    Optional<ArticleDto> getArticleBySlug(String slug);

    /**
     * Search articles by query
     *
     * @param query search terms
     * @return list of matching article summaries
     */
    List<ArticleSummaryDto> searchArticles(String query);

    /**
     * Get articles by tags
     *
     * @param tags list of tags to filter by
     * @return list of article summaries matching any of the tags
     */
    List<ArticleSummaryDto> getArticlesByTags(List<String> tags);

    /**
     * Get articles by difficulty level
     *
     * @param difficulty the difficulty level
     * @return list of article summaries with the specified difficulty
     */
    List<ArticleSummaryDto> getArticlesByDifficulty(String difficulty);

    /**
     * Get related articles for a given article
     *
     * @param articleId the article ID
     * @return list of related article summaries
     */
    List<ArticleSummaryDto> getRelatedArticles(String articleId);

    /**
     * Refresh article index from file system
     * Useful for development when articles are added/modified
     */
    void refreshIndex();

    /**
     * Get available categories
     *
     * @return list of available categories
     */
    List<String> getAvailableCategories();

    /**
     * Get available tags
     *
     * @return list of all tags used in articles
     */
    List<String> getAvailableTags();
}