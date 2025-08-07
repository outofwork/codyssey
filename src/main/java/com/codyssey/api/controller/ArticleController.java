package com.codyssey.api.controller;

import com.codyssey.api.dto.article.*;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.service.ArticleService;
import com.codyssey.api.validation.ValidId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Article management
 * <p>
 * Provides comprehensive endpoints for creating, retrieving, updating, and deleting
 * articles with advanced search and filtering capabilities.
 */
@RestController
@RequestMapping("/v1/articles")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Create a new article
     * 
     * @param createDto article creation data
     * @return created article with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleCreateDto createDto) {
        log.info("POST /v1/articles - Creating new article");
        ArticleDto createdArticle = articleService.createArticle(createDto);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    /**
     * Get all articles
     * 
     * @return list of all articles
     */
    @GetMapping
    public ResponseEntity<List<ArticleSummaryDto>> getAllArticles() {
        log.info("GET /v1/articles - Retrieving all articles");
        List<ArticleSummaryDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    /**
     * Get articles with pagination
     * 
     * @param pageable pagination parameters
     * @return paginated list of articles
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<ArticleSummaryDto>> getArticlesWithPagination(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/articles/paginated - Retrieving articles with pagination");
        Page<ArticleSummaryDto> articles = articleService.getArticlesWithPagination(pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * Get article by URL slug or ID
     * 
     * @param identifier article URL slug or ID
     * @return article if found, 404 if not found
     */
    @GetMapping("/{identifier}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/{} - Retrieving article", identifier);
        
        // First try to find by URL slug
        Optional<ArticleDto> articleBySlug = articleService.getArticleByUrlSlug(identifier);
        if (articleBySlug.isPresent()) {
            return ResponseEntity.ok(articleBySlug.get());
        }
        
        // If not found and it looks like an ID (starts with ART-), try ID lookup for backward compatibility
        if (identifier.startsWith("ART-")) {
            Optional<ArticleDto> article = articleService.getArticleById(identifier);
            return article.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Update article
     * 
     * @param identifier article URL slug or ID
     * @param updateDto updated article data
     * @return updated article
     */
    @PutMapping("/{identifier}")
    public ResponseEntity<ArticleDto> updateArticle(
            @PathVariable @NotBlank String identifier,
            @Valid @RequestBody ArticleUpdateDto updateDto) {
        log.info("PUT /v1/articles/{} - Updating article", identifier);
        
        try {
            ArticleDto updatedArticle;
            
            // Try to update by URL slug first, then by ID
            if (identifier.startsWith("ART-")) {
                updatedArticle = articleService.updateArticle(identifier, updateDto);
            } else {
                updatedArticle = articleService.updateArticleByUrlSlug(identifier, updateDto);
            }
            
            return ResponseEntity.ok(updatedArticle);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete article
     * 
     * @param identifier article URL slug or ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteArticle(@PathVariable @NotBlank String identifier) {
        log.info("DELETE /v1/articles/{} - Deleting article", identifier);
        
        try {
            if (identifier.startsWith("ART-")) {
                articleService.deleteArticle(identifier);
            } else {
                articleService.deleteArticleByUrlSlug(identifier);
            }
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search articles by title or description
     * 
     * @param searchTerm search term
     * @param pageable pagination parameters
     * @return paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ArticleSummaryDto>> searchArticles(
            @RequestParam @NotBlank String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/articles/search - Searching articles with term: {}", searchTerm);
        Page<ArticleSummaryDto> articles = articleService.searchArticles(searchTerm, pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * Get articles by source
     * 
     * @param identifier source ID or URL slug
     * @return list of articles from the source
     */
    @GetMapping("/source/{identifier}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesBySource(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/source/{} - Retrieving articles by source", identifier);
        List<ArticleSummaryDto> articles;
        
        if (identifier.startsWith("SRC-")) {
            articles = articleService.getArticlesBySource(identifier);
        } else {
            articles = articleService.getArticlesBySourceSlug(identifier);
        }
        return ResponseEntity.ok(articles);
    }

    /**
     * Get articles by label/tag
     * 
     * @param identifier label ID or URL slug
     * @return list of articles with the specified label
     */
    @GetMapping("/label/{identifier}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByLabel(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/label/{} - Retrieving articles by label", identifier);
        List<ArticleSummaryDto> articles;
        
        if (identifier.startsWith("LBL-")) {
            articles = articleService.getArticlesByLabel(identifier);
        } else {
            articles = articleService.getArticlesByLabelSlug(identifier);
        }
        return ResponseEntity.ok(articles);
    }

    /**
     * Advanced search with multiple filters
     * 
     * @param sourceIds comma-separated source IDs
     * @param labelIds comma-separated label IDs
     * @param searchTerm search term for title/description
     * @return list of articles matching the filters
     */
    @GetMapping("/filter")
    public ResponseEntity<List<ArticleSummaryDto>> filterArticles(
            @RequestParam(required = false) List<String> sourceIds,
            @RequestParam(required = false) List<String> labelIds,
            @RequestParam(required = false) String searchTerm) {
        log.info("GET /v1/articles/filter - Filtering articles with sources: {}, labels: {}, term: {}", 
                sourceIds, labelIds, searchTerm);
        
        List<ArticleSummaryDto> articles = articleService.searchWithFilters(sourceIds, labelIds, searchTerm);
        return ResponseEntity.ok(articles);
    }

    /**
     * Get article statistics
     * 
     * @return article statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ArticleStatisticsDto> getArticleStatistics() {
        log.info("GET /v1/articles/statistics - Retrieving article statistics");
        ArticleStatisticsDto statistics = articleService.getArticleStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Check if article title is available within a source
     * 
     * @param title article title to check
     * @param sourceId source ID (optional)
     * @return availability status
     */
    @GetMapping("/check-title")
    public ResponseEntity<Boolean> checkTitleAvailability(
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String sourceId) {
        log.info("GET /v1/articles/check-title - Checking title availability: {} for source: {}", title, sourceId);
        boolean isAvailable = articleService.checkTitleAvailability(title, sourceId);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Get the markdown content of an article
     * 
     * @param identifier article URL slug or ID
     * @return markdown content of the article
     */
    @GetMapping(value = "/{identifier}/content", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getArticleContent(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/{}/content - Retrieving article content", identifier);
        
        try {
            String content;
            if (identifier.startsWith("ART-")) {
                content = articleService.getArticleContent(identifier);
            } else {
                content = articleService.getArticleContentByUrlSlug(identifier);
            }
            return ResponseEntity.ok(content);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error retrieving article content for identifier: {}", identifier, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all labels for a specific article
     * 
     * @param identifier article URL slug or ID
     * @return list of article-label relationships
     */
    @GetMapping("/{identifier}/labels")
    public ResponseEntity<List<ArticleLabelReferenceDto>> getArticleLabels(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/{}/labels - Retrieving labels for article", identifier);
        
        try {
            // First resolve the article ID if needed
            String articleId = identifier;
            if (!identifier.startsWith("ART-")) {
                // Get by URL slug to find the ID
                Optional<ArticleDto> article = articleService.getArticleByUrlSlug(identifier);
                if (article.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                articleId = article.get().getId();
            }
            
            List<ArticleLabelReferenceDto> labels = articleService.getArticleLabels(articleId);
            return ResponseEntity.ok(labels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get primary labels for a specific article
     * 
     * @param identifier article URL slug or ID
     * @return list of primary article-label relationships
     */
    @GetMapping("/{identifier}/labels/primary")
    public ResponseEntity<List<ArticleLabelReferenceDto>> getPrimaryArticleLabels(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/articles/{}/labels/primary - Retrieving primary labels for article", identifier);
        
        try {
            // First resolve the article ID if needed
            String articleId = identifier;
            if (!identifier.startsWith("ART-")) {
                // Get by URL slug to find the ID
                Optional<ArticleDto> article = articleService.getArticleByUrlSlug(identifier);
                if (article.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                articleId = article.get().getId();
            }
            
            List<ArticleLabelReferenceDto> primaryLabels = articleService.getPrimaryArticleLabels(articleId);
            return ResponseEntity.ok(primaryLabels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add a label to an article
     * 
     * @param createDto article-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping("/labels")
    public ResponseEntity<Void> addLabelToArticle(@Valid @RequestBody ArticleLabelCreateDto createDto) {
        log.info("POST /v1/articles/labels - Adding label to article");
        articleService.addLabelToArticle(createDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Add multiple labels to articles
     * 
     * @param bulkCreateDto bulk article-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping("/labels/bulk")
    public ResponseEntity<Void> addLabelsToArticles(@Valid @RequestBody ArticleLabelBulkCreateDto bulkCreateDto) {
        log.info("POST /v1/articles/labels/bulk - Adding labels to articles in bulk");
        articleService.addLabelsToArticle(bulkCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove a label from an article
     * 
     * @param articleId article ID
     * @param labelId label ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/labels")
    public ResponseEntity<Void> removeLabelFromArticle(
            @RequestParam @ValidId String articleId,
            @RequestParam @ValidId String labelId) {
        log.info("DELETE /v1/articles/labels - Removing label {} from article {}", labelId, articleId);
        
        try {
            articleService.removeLabelFromArticle(articleId, labelId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}