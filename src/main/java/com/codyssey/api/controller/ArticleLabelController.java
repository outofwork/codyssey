package com.codyssey.api.controller;

import com.codyssey.api.dto.article.ArticleLabelBulkCreateDto;
import com.codyssey.api.dto.article.ArticleLabelCreateDto;
import com.codyssey.api.dto.article.ArticleLabelReferenceDto;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.service.ArticleService;
import com.codyssey.api.validation.ValidId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ArticleLabel relationship management
 * <p>
 * Provides endpoints for managing the relationships between articles and labels.
 */
@RestController
@RequestMapping("/v1/article-labels")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ArticleLabelController {

    private final ArticleService articleService;

    /**
     * Add a label to an article
     * 
     * @param createDto article-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping
    public ResponseEntity<Void> addLabelToArticle(@Valid @RequestBody ArticleLabelCreateDto createDto) {
        log.info("POST /v1/article-labels - Adding label {} to article {}", 
                createDto.getLabelId(), createDto.getArticleId());
        
        try {
            articleService.addLabelToArticle(createDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add multiple labels to articles in bulk
     * 
     * @param bulkCreateDto bulk article-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping("/bulk")
    public ResponseEntity<Void> addLabelsToArticlesBulk(@Valid @RequestBody ArticleLabelBulkCreateDto bulkCreateDto) {
        log.info("POST /v1/article-labels/bulk - Adding {} article-label relationships", 
                bulkCreateDto.getArticleLabels().size());
        
        try {
            articleService.addLabelsToArticle(bulkCreateDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove a label from an article
     * 
     * @param articleId article ID
     * @param labelId label ID
     * @return 204 No Content if successful
     */
    @DeleteMapping
    public ResponseEntity<Void> removeLabelFromArticle(
            @RequestParam @ValidId String articleId,
            @RequestParam @ValidId String labelId) {
        log.info("DELETE /v1/article-labels - Removing label {} from article {}", labelId, articleId);
        
        try {
            articleService.removeLabelFromArticle(articleId, labelId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all labels for a specific article
     * 
     * @param articleId article ID
     * @return list of article-label relationships
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<ArticleLabelReferenceDto>> getLabelsForArticle(@PathVariable @ValidId String articleId) {
        log.info("GET /v1/article-labels/article/{} - Retrieving labels for article", articleId);
        
        try {
            List<ArticleLabelReferenceDto> labels = articleService.getArticleLabels(articleId);
            return ResponseEntity.ok(labels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get primary labels for a specific article
     * 
     * @param articleId article ID
     * @return list of primary article-label relationships
     */
    @GetMapping("/article/{articleId}/primary")
    public ResponseEntity<List<ArticleLabelReferenceDto>> getPrimaryLabelsForArticle(@PathVariable @ValidId String articleId) {
        log.info("GET /v1/article-labels/article/{}/primary - Retrieving primary labels for article", articleId);
        
        try {
            List<ArticleLabelReferenceDto> primaryLabels = articleService.getPrimaryArticleLabels(articleId);
            return ResponseEntity.ok(primaryLabels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}