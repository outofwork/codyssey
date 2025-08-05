package com.codyssey.api.controller;

import com.codyssey.api.dto.question.QuestionLabelCreateDto;
import com.codyssey.api.dto.question.QuestionLabelBulkCreateDto;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.service.QuestionLabelService;
import com.codyssey.api.dto.question.LabelUsageDto;
import com.codyssey.api.validation.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for QuestionLabel operations
 * <p>
 * Provides endpoints for managing question-label associations including bulk operations
 * for tagging questions with multiple labels efficiently.
 */
@RestController
@RequestMapping("/v1/question-labels")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Label Management", description = "APIs for managing question-label associations (tags)")
public class QuestionLabelController {

    private final QuestionLabelService questionLabelService;

    @Operation(summary = "Create question-label association", description = "Associates a label/tag with a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Association created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or label not found"),
            @ApiResponse(responseCode = "409", description = "Association already exists")
    })
    @PostMapping
    public ResponseEntity<String> createQuestionLabel(
            @Parameter(description = "Question-label association data", required = true)
            @Valid @RequestBody QuestionLabelCreateDto createDto) {

        log.info("POST /v1/question-labels - Creating association: question={}, label={}", 
                createDto.getQuestionId(), createDto.getLabelId());
        String result = questionLabelService.createQuestionLabel(createDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple question-label associations in bulk", 
            description = "Associates multiple labels with questions in a single operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associations created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions or labels not found"),
            @ApiResponse(responseCode = "409", description = "One or more associations already exist")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<String>> createQuestionLabelsBulk(
            @Parameter(description = "Bulk question-label association data", required = true)
            @Valid @RequestBody QuestionLabelBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/question-labels/bulk - Creating {} associations in bulk", 
                bulkCreateDto.getQuestionLabels().size());
        List<String> results = questionLabelService.createQuestionLabelsBulk(bulkCreateDto);
        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @Operation(summary = "Get labels for question", description = "Retrieves all labels/tags associated with a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels retrieved successfully")
    })
    @GetMapping("/question/{questionId}/labels")
    public ResponseEntity<List<LabelSummaryDto>> getLabelsByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/question-labels/question/{}/labels - Retrieving labels for question", questionId);
        List<LabelSummaryDto> labels = questionLabelService.getLabelsByQuestionId(questionId);
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get questions for label", description = "Retrieves all question IDs tagged with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question IDs retrieved successfully")
    })
    @GetMapping("/label/{labelId}/questions")
    public ResponseEntity<List<String>> getQuestionIdsByLabelId(
            @Parameter(description = "Label ID", required = true)
            @PathVariable @ValidId String labelId) {

        log.info("GET /v1/question-labels/label/{}/questions - Retrieving questions for label", labelId);
        List<String> questionIds = questionLabelService.getQuestionIdsByLabelId(labelId);
        return ResponseEntity.ok(questionIds);
    }

    @Operation(summary = "Remove label from question", description = "Removes a specific label association from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Association removed successfully"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @DeleteMapping("/question/{questionId}/label/{labelId}")
    public ResponseEntity<Void> removeQuestionLabel(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Label ID", required = true)
            @PathVariable @ValidId String labelId) {

        log.info("DELETE /v1/question-labels/question/{}/label/{} - Removing label from question", questionId, labelId);
        questionLabelService.removeQuestionLabel(questionId, labelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove all labels from question", description = "Removes all label associations from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All labels removed successfully")
    })
    @DeleteMapping("/question/{questionId}/labels")
    public ResponseEntity<Void> removeAllLabelsFromQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("DELETE /v1/question-labels/question/{}/labels - Removing all labels from question", questionId);
        questionLabelService.removeAllLabelsFromQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if association exists", description = "Checks if a question-label association exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists")
    public ResponseEntity<Boolean> isQuestionLabelExists(
            @Parameter(description = "Question ID", required = true)
            @RequestParam @ValidId String questionId,
            @Parameter(description = "Label ID", required = true)
            @RequestParam @ValidId String labelId) {

        log.info("GET /v1/question-labels/exists?questionId={}&labelId={} - Checking association existence", questionId, labelId);
        boolean exists = questionLabelService.isQuestionLabelExists(questionId, labelId);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Replace all labels for question", description = "Replaces all labels for a question with a new set of labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels replaced successfully"),
            @ApiResponse(responseCode = "404", description = "Question or one or more labels not found")
    })
    @PutMapping("/question/{questionId}/labels")
    public ResponseEntity<List<LabelSummaryDto>> replaceQuestionLabels(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of new label IDs", required = true)
            @RequestBody List<String> labelIds) {

        log.info("PUT /v1/question-labels/question/{}/labels - Replacing labels for question", questionId);
        List<LabelSummaryDto> labels = questionLabelService.replaceQuestionLabels(questionId, labelIds);
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Add multiple labels to question", description = "Adds multiple labels to a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels added successfully"),
            @ApiResponse(responseCode = "404", description = "Question or one or more labels not found")
    })
    @PostMapping("/question/{questionId}/labels")
    public ResponseEntity<List<LabelSummaryDto>> addLabelsToQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of label IDs to add", required = true)
            @RequestBody List<String> labelIds) {

        log.info("POST /v1/question-labels/question/{}/labels - Adding {} labels to question", questionId, labelIds.size());
        List<LabelSummaryDto> labels = questionLabelService.addLabelsToQuestion(questionId, labelIds);
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Remove multiple labels from question", description = "Removes multiple labels from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels removed successfully")
    })
    @DeleteMapping("/question/{questionId}/labels/bulk")
    public ResponseEntity<List<LabelSummaryDto>> removeLabelsFromQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of label IDs to remove", required = true)
            @RequestBody List<String> labelIds) {

        log.info("DELETE /v1/question-labels/question/{}/labels/bulk - Removing {} labels from question", questionId, labelIds.size());
        List<LabelSummaryDto> remainingLabels = questionLabelService.removeLabelsFromQuestion(questionId, labelIds);
        return ResponseEntity.ok(remainingLabels);
    }

    @Operation(summary = "Get labels count for question", description = "Gets the count of labels associated with a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label count retrieved successfully")
    })
    @GetMapping("/question/{questionId}/count")
    public ResponseEntity<Long> getLabelsCountByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/question-labels/question/{}/count - Getting labels count", questionId);
        Long count = questionLabelService.getLabelsCountByQuestionId(questionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get questions count for label", description = "Gets the count of questions tagged with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question count retrieved successfully")
    })
    @GetMapping("/label/{labelId}/count")
    public ResponseEntity<Long> getQuestionsCountByLabelId(
            @Parameter(description = "Label ID", required = true)
            @PathVariable @ValidId String labelId) {

        log.info("GET /v1/question-labels/label/{}/count - Getting questions count", labelId);
        Long count = questionLabelService.getQuestionsCountByLabelId(labelId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get most used labels", description = "Retrieves the most frequently used labels across all questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Most used labels retrieved successfully")
    })
    @GetMapping("/most-used")
    public ResponseEntity<List<LabelUsageDto>> getMostUsedLabels(
            @Parameter(description = "Maximum number of labels to return", required = false)
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /v1/question-labels/most-used?limit={} - Getting most used labels", limit);
        List<LabelUsageDto> mostUsedLabels = questionLabelService.getMostUsedLabels(limit);
        return ResponseEntity.ok(mostUsedLabels);
    }
}