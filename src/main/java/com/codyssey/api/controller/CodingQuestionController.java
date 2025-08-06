package com.codyssey.api.controller;

import com.codyssey.api.dto.question.*;
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
     * Get coding question by ID
     * 
     * @param id coding question ID
     * @return coding question if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodingQuestionDto> getQuestionById(@PathVariable @ValidId String id) {
        log.info("GET /v1/coding-questions/{} - Retrieving coding question by ID", id);
        Optional<CodingQuestionDto> question = codingQuestionService.getQuestionById(id);
        return question.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update coding question
     * 
     * @param id coding question ID
     * @param updateDto updated coding question data
     * @return updated coding question
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodingQuestionDto> updateQuestion(
            @PathVariable @ValidId String id,
            @Valid @RequestBody CodingQuestionUpdateDto updateDto) {
        log.info("PUT /v1/coding-questions/{} - Updating coding question", id);
        CodingQuestionDto updatedQuestion = codingQuestionService.updateQuestion(id, updateDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    /**
     * Delete coding question
     * 
     * @param id coding question ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable @ValidId String id) {
        log.info("DELETE /v1/coding-questions/{} - Deleting coding question", id);
        codingQuestionService.deleteQuestion(id);
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
    @GetMapping("/source/{sourceId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsBySource(@PathVariable @ValidId String sourceId) {
        log.info("GET /v1/coding-questions/source/{} - Retrieving questions by source", sourceId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsBySource(sourceId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by difficulty
     * 
     * @param difficultyId difficulty label ID
     * @return list of questions with the specified difficulty
     */
    @GetMapping("/difficulty/{difficultyId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByDifficulty(@PathVariable @ValidId String difficultyId) {
        log.info("GET /v1/coding-questions/difficulty/{} - Retrieving questions by difficulty", difficultyId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByDifficulty(difficultyId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by label/tag
     * 
     * @param labelId label ID
     * @return list of questions tagged with the specified label
     */
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByLabel(@PathVariable @ValidId String labelId) {
        log.info("GET /v1/coding-questions/label/{} - Retrieving questions by label", labelId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByLabel(labelId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get coding questions by company
     * 
     * @param companyId company label ID
     * @return list of questions asked by the specified company
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByCompany(@PathVariable @ValidId String companyId) {
        log.info("GET /v1/coding-questions/company/{} - Retrieving questions by company", companyId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByCompany(companyId);
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
}