package com.codyssey.api.controller;

import com.codyssey.api.dto.article.*;
import com.codyssey.api.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Article operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Article management endpoints")
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "Create a new article")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Article created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Article already exists")
    })
    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleCreateDto createDto) {
        log.info("POST /api/v1/articles - Creating article with title: {}", createDto.getTitle());
        ArticleDto createdArticle = articleService.createArticle(createDto);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all articles")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ArticleSummaryDto>> getAllArticles() {
        log.info("GET /api/v1/articles - Fetching all articles");
        List<ArticleSummaryDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated articles retrieved successfully")
    @GetMapping("/paginated")
    public ResponseEntity<Page<ArticleSummaryDto>> getArticlesWithPagination(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/v1/articles/paginated - Fetching articles with pagination: {}", pageable);
        Page<ArticleSummaryDto> articlePage = articleService.getArticlesWithPagination(pageable);
        return ResponseEntity.ok(articlePage);
    }

    @Operation(summary = "Get article by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article found"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(
            @Parameter(description = "Article ID") @PathVariable String id) {
        log.info("GET /api/v1/articles/{} - Fetching article by ID", id);
        
        Optional<ArticleDto> article = articleService.getArticleById(id);
        if (article.isPresent()) {
            // Increment view count
            articleService.incrementViewCount(id);
            return ResponseEntity.ok(article.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get article by URL slug")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article found"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/slug/{urlSlug}")
    public ResponseEntity<ArticleDto> getArticleByUrlSlug(
            @Parameter(description = "Article URL slug") @PathVariable String urlSlug) {
        log.info("GET /api/v1/articles/slug/{} - Fetching article by URL slug", urlSlug);
        
        Optional<ArticleDto> article = articleService.getArticleByUrlSlug(urlSlug);
        if (article.isPresent()) {
            // Increment view count
            articleService.incrementViewCount(article.get().getId());
            return ResponseEntity.ok(article.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update article by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Article not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate article title")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(
            @Parameter(description = "Article ID") @PathVariable String id,
            @Valid @RequestBody ArticleUpdateDto updateDto) {
        log.info("PUT /api/v1/articles/{} - Updating article", id);
        ArticleDto updatedArticle = articleService.updateArticle(id, updateDto);
        return ResponseEntity.ok(updatedArticle);
    }

    @Operation(summary = "Update article by URL slug")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Article not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate article title")
    })
    @PutMapping("/slug/{urlSlug}")
    public ResponseEntity<ArticleDto> updateArticleByUrlSlug(
            @Parameter(description = "Article URL slug") @PathVariable String urlSlug,
            @Valid @RequestBody ArticleUpdateDto updateDto) {
        log.info("PUT /api/v1/articles/slug/{} - Updating article by URL slug", urlSlug);
        ArticleDto updatedArticle = articleService.updateArticleByUrlSlug(urlSlug, updateDto);
        return ResponseEntity.ok(updatedArticle);
    }

    @Operation(summary = "Delete article by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "Article ID") @PathVariable String id) {
        log.info("DELETE /api/v1/articles/{} - Deleting article", id);
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete article by URL slug")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/slug/{urlSlug}")
    public ResponseEntity<Void> deleteArticleByUrlSlug(
            @Parameter(description = "Article URL slug") @PathVariable String urlSlug) {
        log.info("DELETE /api/v1/articles/slug/{} - Deleting article by URL slug", urlSlug);
        articleService.deleteArticleByUrlSlug(urlSlug);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search articles")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<ArticleSummaryDto>> searchArticles(
            @Parameter(description = "Search term") @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/v1/articles/search - Searching articles with term: {}", q);
        Page<ArticleSummaryDto> searchResults = articleService.searchArticles(q, pageable);
        return ResponseEntity.ok(searchResults);
    }

    @Operation(summary = "Get articles by type")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/type/{articleType}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByType(
            @Parameter(description = "Article type") @PathVariable String articleType) {
        log.info("GET /api/v1/articles/type/{} - Fetching articles by type", articleType);
        List<ArticleSummaryDto> articles = articleService.getArticlesByType(articleType);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by category label ID")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/category/{categoryLabelId}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByCategoryLabelId(
            @Parameter(description = "Category label ID") @PathVariable String categoryLabelId) {
        log.info("GET /api/v1/articles/category/{} - Fetching articles by category label ID", categoryLabelId);
        List<ArticleSummaryDto> articles = articleService.getArticlesByCategoryLabelId(categoryLabelId);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by category label slug")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/category/slug/{categoryLabelSlug}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByCategoryLabelSlug(
            @Parameter(description = "Category label slug") @PathVariable String categoryLabelSlug) {
        log.info("GET /api/v1/articles/category/slug/{} - Fetching articles by category label slug", categoryLabelSlug);
        List<ArticleSummaryDto> articles = articleService.getArticlesByCategoryLabelSlug(categoryLabelSlug);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by difficulty label ID")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/difficulty/{difficultyLabelId}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByDifficultyLabelId(
            @Parameter(description = "Difficulty label ID") @PathVariable String difficultyLabelId) {
        log.info("GET /api/v1/articles/difficulty/{} - Fetching articles by difficulty label ID", difficultyLabelId);
        List<ArticleSummaryDto> articles = articleService.getArticlesByDifficultyLabelId(difficultyLabelId);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by difficulty label slug")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/difficulty/slug/{difficultyLabelSlug}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByDifficultyLabelSlug(
            @Parameter(description = "Difficulty label slug") @PathVariable String difficultyLabelSlug) {
        log.info("GET /api/v1/articles/difficulty/slug/{} - Fetching articles by difficulty label slug", difficultyLabelSlug);
        List<ArticleSummaryDto> articles = articleService.getArticlesByDifficultyLabelSlug(difficultyLabelSlug);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by label ID")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByLabelId(
            @Parameter(description = "Label ID") @PathVariable String labelId) {
        log.info("GET /api/v1/articles/label/{} - Fetching articles by label ID", labelId);
        List<ArticleSummaryDto> articles = articleService.getArticlesByLabelId(labelId);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get articles by label slug")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully")
    @GetMapping("/label/slug/{labelSlug}")
    public ResponseEntity<List<ArticleSummaryDto>> getArticlesByLabelSlug(
            @Parameter(description = "Label slug") @PathVariable String labelSlug) {
        log.info("GET /api/v1/articles/label/slug/{} - Fetching articles by label slug", labelSlug);
        List<ArticleSummaryDto> articles = articleService.getArticlesByLabelSlug(labelSlug);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Get recent articles")
    @ApiResponse(responseCode = "200", description = "Recent articles retrieved successfully")
    @GetMapping("/recent")
    public ResponseEntity<Page<ArticleSummaryDto>> getRecentArticles(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("GET /api/v1/articles/recent - Fetching recent articles");
        Page<ArticleSummaryDto> recentArticles = articleService.getRecentArticles(pageable);
        return ResponseEntity.ok(recentArticles);
    }

    @Operation(summary = "Advanced search with filters")
    @ApiResponse(responseCode = "200", description = "Filtered articles retrieved successfully")
    @GetMapping("/advanced-search")
    public ResponseEntity<List<ArticleSummaryDto>> searchWithFilters(
            @Parameter(description = "Article types") @RequestParam(required = false) List<String> articleTypes,
            @Parameter(description = "Category label IDs") @RequestParam(required = false) List<String> categoryLabelIds,
            @Parameter(description = "Difficulty label IDs") @RequestParam(required = false) List<String> difficultyLabelIds,
            @Parameter(description = "Label IDs") @RequestParam(required = false) List<String> labelIds,
            @Parameter(description = "Search term") @RequestParam(required = false) String searchTerm) {
        log.info("GET /api/v1/articles/advanced-search - Advanced search with filters");
        List<ArticleSummaryDto> articles = articleService.searchWithFilters(
                articleTypes, categoryLabelIds, difficultyLabelIds, labelIds, searchTerm);
        return ResponseEntity.ok(articles);
    }

    @Operation(summary = "Create article-label relationship")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Article-label relationship created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Article or label not found"),
        @ApiResponse(responseCode = "409", description = "Relationship already exists")
    })
    @PostMapping("/labels")
    public ResponseEntity<Void> createArticleLabel(@Valid @RequestBody ArticleLabelCreateDto createDto) {
        log.info("POST /api/v1/articles/labels - Creating article-label relationship");
        boolean created = articleService.createArticleLabel(createDto);
        return created ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Create multiple article-label relationships")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Article-label relationships created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Article or labels not found")
    })
    @PostMapping("/labels/bulk")
    public ResponseEntity<String> createArticleLabels(@Valid @RequestBody ArticleLabelBulkCreateDto bulkCreateDto) {
        log.info("POST /api/v1/articles/labels/bulk - Creating bulk article-label relationships");
        int createdCount = articleService.createArticleLabels(bulkCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Created " + createdCount + " article-label relationships");
    }

    @Operation(summary = "Remove article-label relationship")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Article-label relationship removed successfully"),
        @ApiResponse(responseCode = "404", description = "Relationship not found")
    })
    @DeleteMapping("/{articleId}/labels/{labelId}")
    public ResponseEntity<Void> removeArticleLabel(
            @Parameter(description = "Article ID") @PathVariable String articleId,
            @Parameter(description = "Label ID") @PathVariable String labelId) {
        log.info("DELETE /api/v1/articles/{}/labels/{} - Removing article-label relationship", articleId, labelId);
        boolean removed = articleService.removeArticleLabel(articleId, labelId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get article statistics")
    @ApiResponse(responseCode = "200", description = "Article statistics retrieved successfully")
    @GetMapping("/statistics")
    public ResponseEntity<ArticleStatisticsDto> getArticleStatistics() {
        log.info("GET /api/v1/articles/statistics - Fetching article statistics");
        ArticleStatisticsDto statistics = articleService.getArticleStatistics();
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Increment article like count")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Like count incremented successfully"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeArticle(
            @Parameter(description = "Article ID") @PathVariable String id) {
        log.info("POST /api/v1/articles/{}/like - Incrementing like count", id);
        articleService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Increment article bookmark count")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bookmark count incremented successfully"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PostMapping("/{id}/bookmark")
    public ResponseEntity<Void> bookmarkArticle(
            @Parameter(description = "Article ID") @PathVariable String id) {
        log.info("POST /api/v1/articles/{}/bookmark - Incrementing bookmark count", id);
        articleService.incrementBookmarkCount(id);
        return ResponseEntity.ok().build();
    }

    // Special endpoint for hierarchical article access
    @Operation(summary = "Get article by hierarchical path")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article found"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{path1}")
    public ResponseEntity<ArticleDto> getArticleByPath1(
            @Parameter(description = "First level path") @PathVariable String path1) {
        log.info("GET /api/v1/articles/{} - Fetching article by path", path1);
        return getArticleByUrlSlug(path1);
    }

    @GetMapping("/{path1}/{path2}")
    public ResponseEntity<ArticleDto> getArticleByPath2(
            @PathVariable String path1, @PathVariable String path2) {
        String fullPath = path1 + "/" + path2;
        log.info("GET /api/v1/articles/{} - Fetching article by hierarchical path", fullPath);
        return getArticleByUrlSlug(fullPath);
    }

    @GetMapping("/{path1}/{path2}/{path3}")
    public ResponseEntity<ArticleDto> getArticleByPath3(
            @PathVariable String path1, @PathVariable String path2, @PathVariable String path3) {
        String fullPath = path1 + "/" + path2 + "/" + path3;
        log.info("GET /api/v1/articles/{} - Fetching article by hierarchical path", fullPath);
        return getArticleByUrlSlug(fullPath);
    }

    @GetMapping("/{path1}/{path2}/{path3}/{path4}")
    public ResponseEntity<ArticleDto> getArticleByPath4(
            @PathVariable String path1, @PathVariable String path2, 
            @PathVariable String path3, @PathVariable String path4) {
        String fullPath = path1 + "/" + path2 + "/" + path3 + "/" + path4;
        log.info("GET /api/v1/articles/{} - Fetching article by hierarchical path", fullPath);
        return getArticleByUrlSlug(fullPath);
    }
}