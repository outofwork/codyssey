package com.codyssey.api.controller;

import com.codyssey.api.dto.article.ArticleDto;
import com.codyssey.api.dto.article.ArticleSummaryDto;
import com.codyssey.api.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Article operations
 * <p>
 * Provides endpoints for managing and serving markdown-based learning articles
 * similar to LeetCode's learning section.
 */
@RestController
@RequestMapping("/v1/articles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Article Management", description = "APIs for managing and serving learning articles")
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "Get all articles", description = "Retrieves all published articles in summary format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ArticleSummaryDto>> getAllArticles() {
        log.info("GET /v1/articles - Retrieving all articles");
        List<ArticleSummaryDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get article by ID", description = "Retrieves a specific article by its unique ID with full content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(
            @Parameter(description = "Article unique identifier", required = true)
            @PathVariable String id) {
        
        log.info("GET /v1/articles/{} - Retrieving article by ID", id);
        return articleService.getArticleById(id)
                .map(article -> ResponseEntity.ok(article))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get article by slug", description = "Retrieves a specific article by its URL-friendly slug with full content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ArticleDto> getArticleBySlug(
            @Parameter(description = "Article URL-friendly slug", required = true)
            @PathVariable String slug) {
        
        log.info("GET /v1/articles/slug/{} - Retrieving article by slug", slug);
        return articleService.getArticleBySlug(slug)
                .map(article -> ResponseEntity.ok(article))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get articles by category", description = "Retrieves all articles in a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByCategory(
            @Parameter(description = "Category name (e.g., data-structures, algorithms)", required = true)
            @PathVariable String category) {
        
        log.info("GET /v1/articles/category/{} - Retrieving articles by category", category);
        List<ArticleSummaryDto> articles = articleService.getArticlesByCategory(category);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Search articles", description = "Search articles by title, description, or tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ArticleSummaryDto>> searchArticles(
            @Parameter(description = "Search query", required = true)
            @RequestParam String q) {
        
        log.info("GET /v1/articles/search?q={} - Searching articles", q);
        List<ArticleSummaryDto> articles = articleService.searchArticles(q);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by tags", description = "Retrieves articles matching any of the specified tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    })
    @GetMapping("/tags")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByTags(
            @Parameter(description = "Comma-separated list of tags", required = true)
            @RequestParam List<String> tags) {
        
        log.info("GET /v1/articles/tags?tags={} - Retrieving articles by tags", tags);
        List<ArticleSummaryDto> articles = articleService.getArticlesByTags(tags);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by difficulty", description = "Retrieves articles of a specific difficulty level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    })
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByDifficulty(
            @Parameter(description = "Difficulty level (Beginner, Intermediate, Advanced)", required = true)
            @PathVariable String difficulty) {
        
        log.info("GET /v1/articles/difficulty/{} - Retrieving articles by difficulty", difficulty);
        List<ArticleSummaryDto> articles = articleService.getArticlesByDifficulty(difficulty);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get related articles", description = "Retrieves articles related to a specific article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related articles retrieved successfully")
    })
    @GetMapping("/{id}/related")
    public ResponseEntity<List<ArticleSummaryDto>> getRelatedArticles(
            @Parameter(description = "Article ID to find related articles for", required = true)
            @PathVariable String id) {
        
        log.info("GET /v1/articles/{}/related - Retrieving related articles", id);
        List<ArticleSummaryDto> articles = articleService.getRelatedArticles(id);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get available categories", description = "Retrieves all available article categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        log.info("GET /v1/articles/categories - Retrieving available categories");
        List<String> categories = articleService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get available tags", description = "Retrieves all available article tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully")
    })
    @GetMapping("/tags/all")
    public ResponseEntity<List<String>> getAvailableTags() {
        log.info("GET /v1/articles/tags/all - Retrieving available tags");
        List<String> tags = articleService.getAvailableTags();
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Refresh article index", description = "Refreshes the article index from file system (development use)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Index refreshed successfully")
    })
    @PostMapping("/refresh-index")
    public ResponseEntity<String> refreshIndex() {
        log.info("POST /v1/articles/refresh-index - Refreshing article index");
        articleService.refreshIndex();
        return ResponseEntity.ok("Article index refreshed successfully");
    }
}