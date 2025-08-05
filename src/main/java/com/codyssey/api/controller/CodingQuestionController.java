package com.codyssey.api.controller;

import com.codyssey.api.dto.question.*;
import com.codyssey.api.service.CodingQuestionService;
import com.codyssey.api.dto.question.QuestionStatisticsDto;
import com.codyssey.api.validation.ValidId;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for CodingQuestion operations
 * <p>
 * Provides endpoints for coding question management including creation, retrieval,
 * updating, deletion, search, and various filtering operations.
 */
@RestController
@RequestMapping("/v1/questions")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Coding Question Management", description = "APIs for managing coding questions from platforms like LeetCode, HackerRank, etc.")
public class CodingQuestionController {

    private final CodingQuestionService codingQuestionService;

    @Operation(summary = "Create a new coding question", description = "Creates a new coding question with all metadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Difficulty label or user not found"),
            @ApiResponse(responseCode = "409", description = "Question title already exists for the platform")
    })
    @PostMapping
    public ResponseEntity<CodingQuestionDto> createQuestion(
            @Parameter(description = "Question creation data", required = true)
            @Valid @RequestBody CodingQuestionCreateDto createDto) {

        log.info("POST /v1/questions - Creating question: {}", createDto.getTitle());
        CodingQuestionDto createdQuestion = codingQuestionService.createQuestion(createDto);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all coding questions", description = "Retrieves all coding questions (non-deleted)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<CodingQuestionSummaryDto>> getAllQuestions() {
        log.info("GET /v1/questions - Retrieving all questions");
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get all coding questions with pagination", description = "Retrieves all coding questions with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/paginated")
    public ResponseEntity<Page<CodingQuestionSummaryDto>> getAllQuestionsPaginated(
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("GET /v1/questions/paginated - Retrieving questions with pagination: {}", pageable);
        Page<CodingQuestionSummaryDto> questions = codingQuestionService.getAllQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get coding question by ID", description = "Retrieves a specific coding question by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CodingQuestionDto> getQuestionById(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/questions/{} - Retrieving question by ID", id);
        return codingQuestionService.getQuestionById(id)
                .map(question -> ResponseEntity.ok(question))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update coding question", description = "Updates an existing coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CodingQuestionDto> updateQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Question update data", required = true)
            @Valid @RequestBody CodingQuestionUpdateDto updateDto) {

        log.info("PUT /v1/questions/{} - Updating question", id);
        CodingQuestionDto updatedQuestion = codingQuestionService.updateQuestion(id, updateDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    @Operation(summary = "Delete coding question", description = "Soft deletes a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/questions/{} - Deleting question", id);
        codingQuestionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get questions by difficulty", description = "Retrieves questions filtered by difficulty level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Difficulty label not found")
    })
    @GetMapping("/difficulty/{difficultyLabelId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByDifficulty(
            @Parameter(description = "Difficulty label ID", required = true)
            @PathVariable @ValidId String difficultyLabelId) {

        log.info("GET /v1/questions/difficulty/{} - Retrieving questions by difficulty", difficultyLabelId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByDifficulty(difficultyLabelId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get questions by platform", description = "Retrieves questions from a specific platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/platform/{platformSource}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByPlatform(
            @Parameter(description = "Platform source (e.g., LeetCode, HackerRank)", required = true)
            @PathVariable String platformSource) {

        log.info("GET /v1/questions/platform/{} - Retrieving questions by platform", platformSource);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByPlatform(platformSource);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Search questions", description = "Searches questions by title or description with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CodingQuestionSummaryDto>> searchQuestions(
            @Parameter(description = "Search term", required = true)
            @RequestParam String q,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("GET /v1/questions/search?q={} - Searching questions", q);
        Page<CodingQuestionSummaryDto> questions = codingQuestionService.searchQuestions(q, pageable);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get questions by label", description = "Retrieves questions tagged with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByLabel(
            @Parameter(description = "Label ID", required = true)
            @PathVariable @ValidId String labelId) {

        log.info("GET /v1/questions/label/{} - Retrieving questions by label", labelId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByLabel(labelId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get questions by company", description = "Retrieves questions asked by a specific company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/company/{companyLabelId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByCompany(
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId) {

        log.info("GET /v1/questions/company/{} - Retrieving questions by company", companyLabelId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByCompany(companyLabelId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get questions by user", description = "Retrieves questions created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getQuestionsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable @ValidId String userId) {

        log.info("GET /v1/questions/user/{} - Retrieving questions by user", userId);
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByUser(userId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get active questions", description = "Retrieves only active questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active questions retrieved successfully")
    })
    @GetMapping("/active")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getActiveQuestions() {
        log.info("GET /v1/questions/active - Retrieving active questions");
        List<CodingQuestionSummaryDto> questions = codingQuestionService.getActiveQuestions();
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get questions by status", description = "Retrieves questions filtered by status with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CodingQuestionSummaryDto>> getQuestionsByStatus(
            @Parameter(description = "Question status (ACTIVE, DEPRECATED, DRAFT)", required = true)
            @PathVariable String status,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("GET /v1/questions/status/{} - Retrieving questions by status", status);
        Page<CodingQuestionSummaryDto> questions = codingQuestionService.getQuestionsByStatus(status, pageable);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Check title availability", description = "Checks if a question title is available for a platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability check completed")
    })
    @GetMapping("/check-title")
    public ResponseEntity<Boolean> checkTitleAvailability(
            @Parameter(description = "Question title", required = true)
            @RequestParam String title,
            @Parameter(description = "Platform source", required = true)
            @RequestParam String platformSource) {

        log.info("GET /v1/questions/check-title?title={}&platformSource={} - Checking title availability", title, platformSource);
        boolean isAvailable = codingQuestionService.checkTitleAvailability(title, platformSource);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Get question statistics", description = "Retrieves comprehensive statistics for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("/{id}/statistics")
    public ResponseEntity<QuestionStatisticsDto> getQuestionStatistics(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/questions/{}/statistics - Retrieving question statistics", id);
        QuestionStatisticsDto statistics = codingQuestionService.getQuestionStatistics(id);
        return ResponseEntity.ok(statistics);
    }
}