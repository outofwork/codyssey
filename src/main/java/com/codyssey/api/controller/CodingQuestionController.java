package com.codyssey.api.controller;

import com.codyssey.api.dto.question.*;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.service.CodingQuestionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for CodingQuestion management
 * <p>
 * Provides comprehensive endpoints for creating, retrieving, updating, and deleting
 * coding questions with advanced search and filtering capabilities.
 */
@RestController
@RequestMapping("/v1/coding-questions")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CodingQuestionController {

    private final CodingQuestionService codingQuestionService;

    /**
     * Create a new coding question
     * 
     * @param createDto coding question creation data
     * @return created coding question with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CodingQuestionDto> createQuestion(@Valid @RequestBody CodingQuestionCreateDto createDto) {
        log.info("POST /v1/coding-questions - Creating new coding question");
        CodingQuestionDto createdQuestion = codingQuestionService.createQuestion(createDto);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * Get all coding questions
     * 
     * @return list of all coding questions
     */
    @GetMapping
    public ResponseEntity<List<CodingQuestionSummaryDto>> getAllQuestions() {
        log.info("GET /v1/coding-questions - Retrieving all coding questions");
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions with pagination
     * 
     * @param pageable pagination parameters
     * @return paginated list of coding questions
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<CodingQuestionSummaryDto>> getQuestionsWithPagination(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/coding-questions/paginated - Retrieving coding questions with pagination");
        Page<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsWithPagination(pageable);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding question by URL slug or ID
     * 
     * @param identifier coding question URL slug or ID
     * @return coding question if found, 404 if not found
     */
    @GetMapping("/{identifier}")
    public ResponseEntity<CodingQuestionDto> getQuestion(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/{} - Retrieving coding question", identifier);
        
        // First try to find by URL slug
        Optional<CodingQuestionDto> questionBySlug = codingQuestionService.getQuestionByUrlSlug(identifier);
        if (questionBySlug.isPresent()) {
            return ResponseEntity.ok(questionBySlug.get());
        }
        
        // If not found and it looks like an ID (starts with QST-), try ID lookup for backward compatibility
        if (identifier.startsWith("QST-")) {
            Optional<CodingQuestionDto> question = codingQuestionService.getQuestionById(identifier);
            return question.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Update coding question
     * 
     * @param identifier coding question URL slug or ID
     * @param updateDto updated coding question data
     * @return updated coding question
     */
    @PutMapping("/{identifier}")
    public ResponseEntity<CodingQuestionDto> updateQuestion(
            @PathVariable @NotBlank String identifier,
            @Valid @RequestBody CodingQuestionUpdateDto updateDto) {
        log.info("PUT /v1/coding-questions/{} - Updating coding question", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("QST-")) {
            CodingQuestionDto updatedQuestion = codingQuestionService.updateQuestion(identifier, updateDto);
            return ResponseEntity.ok(updatedQuestion);
        } else {
            CodingQuestionDto updatedQuestion = codingQuestionService.updateQuestionByUrlSlug(identifier, updateDto);
            return ResponseEntity.ok(updatedQuestion);
        }
    }

    /**
     * Delete coding question
     * 
     * @param identifier coding question URL slug or ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable @NotBlank String identifier) {
        log.info("DELETE /v1/coding-questions/{} - Deleting coding question", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("QST-")) {
            codingQuestionService.deleteQuestion(identifier);
        } else {
            codingQuestionService.deleteQuestionByUrlSlug(identifier);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Search coding questions by title or description
     * 
     * @param searchTerm search term
     * @param pageable pagination parameters
     * @return paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CodingQuestionSummaryDto>> searchQuestions(
            @RequestParam @NotBlank String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/coding-questions/search - Searching questions with term: {}", searchTerm);
        Page<CodingQuestionSummaryDto> questions = codingQuestionService.searchQuestions(searchTerm, pageable);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by source
     * 
     * @param sourceId source ID
     * @return list of questions from the source
     */
    @GetMapping("/source/{identifier}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsBySource(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/source/{} - Retrieving questions by source", identifier);
        List<CodingQuestionSummaryDto> questions;
        
        if (identifier.startsWith("SRC-")) {
            questions = codingQuestionService.getQuestionsBySource(identifier);
        } else {
            questions = codingQuestionService.getQuestionsBySourceSlug(identifier);
        }
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by difficulty (accepts both URL slug and ID)
     * 
     * @param identifier difficulty label URL slug or ID
     * @return list of questions with the specified difficulty
     */
    @GetMapping("/difficulty/{identifier}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByDifficulty(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/difficulty/{} - Retrieving questions by difficulty", identifier);
        List<CodingQuestionSummaryDto> questions;
        
        if (identifier.startsWith("LBL-")) {
            questions = codingQuestionService.getQuestionsByDifficulty(identifier);
        } else {
            questions = codingQuestionService.getQuestionsByDifficultySlug(identifier);
        }
        
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by label/tag (accepts both URL slug and ID)
     * 
     * @param identifier label URL slug or ID
     * @return list of questions tagged with the specified label
     */
    @GetMapping("/label/{identifier}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByLabel(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/label/{} - Retrieving questions by label", identifier);
        List<CodingQuestionSummaryDto> questions;
        
        if (identifier.startsWith("LBL-")) {
            questions = codingQuestionService.getQuestionsByLabel(identifier);
        } else {
            questions = codingQuestionService.getQuestionsByLabelSlug(identifier);
        }
        
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by company (accepts both URL slug and ID)
     * 
     * @param identifier company label URL slug or ID
     * @return list of questions asked by the specified company
     */
    @GetMapping("/company/{identifier}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByCompany(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/company/{} - Retrieving questions by company", identifier);
        List<CodingQuestionSummaryDto> questions;
        
        if (identifier.startsWith("LBL-")) {
            questions = codingQuestionService.getQuestionsByCompany(identifier);
        } else {
            questions = codingQuestionService.getQuestionsByCompanySlug(identifier);
        }
        
        return ResponseEntity.ok(questions);
    }

    /**
     * Advanced search with multiple filters
     * 
     * @param sourceIds comma-separated list of source IDs
     * @param difficultyIds comma-separated list of difficulty label IDs
     * @param labelIds comma-separated list of label IDs
     * @param companyIds comma-separated list of company label IDs
     * @param searchTerm search term for title/description
     * @return list of questions matching the filters
     */
    @GetMapping("/filter")
    public ResponseEntity<List<CodingQuestionSummaryDto>> searchWithFilters(
            @RequestParam(required = false) List<String> sourceIds,
            @RequestParam(required = false) List<String> difficultyIds,
            @RequestParam(required = false) List<String> labelIds,
            @RequestParam(required = false) List<String> companyIds,
            @RequestParam(required = false) String searchTerm) {
        log.info("GET /v1/coding-questions/filter - Advanced search with filters");
        List<CodingQuestionSummaryDto> questions = codingQuestionService.searchWithFilters(
                sourceIds, difficultyIds, labelIds, companyIds, searchTerm);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get question statistics
     * 
     * @param id question ID
     * @return question statistics
     */
    @GetMapping("/{id}/statistics")
    public ResponseEntity<QuestionStatisticsDto> getQuestionStatistics(@PathVariable @ValidId String id) {
        log.info("GET /v1/coding-questions/{}/statistics - Retrieving question statistics", id);
        QuestionStatisticsDto statistics = codingQuestionService.getQuestionStatistics(id);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Check if a question title is available within a source
     * 
     * @param title question title
     * @param sourceId source ID
     * @return availability status
     */
    @GetMapping("/check-title-availability")
    public ResponseEntity<Boolean> checkTitleAvailability(
            @RequestParam @NotBlank String title,
            @RequestParam @ValidId String sourceId) {
        log.info("GET /v1/coding-questions/check-title-availability - Checking title: {} for source: {}", title, sourceId);
        boolean isAvailable = codingQuestionService.checkTitleAvailability(title, sourceId);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Get the markdown content of a coding question
     * 
     * @param identifier coding question ID or URL slug
     * @return markdown content of the question
     */
    @GetMapping("/{identifier}/content")
    public ResponseEntity<String> getQuestionContent(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/coding-questions/{}/content - Fetching question content", identifier);
        try {
            String content;
            // First try to get by URL slug, then fall back to ID
            try {
                content = codingQuestionService.getQuestionContentByUrlSlug(identifier);
            } catch (ResourceNotFoundException e) {
                // If not found by URL slug, try by ID
                log.info("Question not found by URL slug {}, trying by ID", identifier);
                content = codingQuestionService.getQuestionContent(identifier);
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "text/markdown; charset=UTF-8")
                    .body(content);
        } catch (Exception e) {
            log.error("Error reading question content for identifier {}: {}", identifier, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading question content: " + e.getMessage());
        }
    }
}