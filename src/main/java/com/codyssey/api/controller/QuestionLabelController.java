package com.codyssey.api.controller;

import com.codyssey.api.dto.question.QuestionLabelBulkCreateDto;
import com.codyssey.api.dto.question.QuestionLabelCreateDto;
import com.codyssey.api.service.CodingQuestionService;
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

/**
 * REST Controller for QuestionLabel relationship management
 * <p>
 * Provides endpoints for managing question-label relationships.
 */
@RestController
@RequestMapping("/v1/question-labels")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Label Management", description = "APIs for managing question-label relationships")
public class QuestionLabelController {

    private final CodingQuestionService codingQuestionService;

    /**
     * Add label to question
     * 
     * @param createDto question-label relationship data
     * @return 201 Created if successful
     */
    @Operation(summary = "Add label to question", description = "Creates a relationship between a question and a label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question-label relationship created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or label not found"),
            @ApiResponse(responseCode = "409", description = "Question-label relationship already exists")
    })
    @PostMapping
    public ResponseEntity<Void> addLabelToQuestion(
            @Parameter(description = "Question-label relationship data", required = true)
            @Valid @RequestBody QuestionLabelCreateDto createDto) {

        log.info("POST /v1/question-labels - Adding label {} to question {}", 
                createDto.getLabelId(), createDto.getQuestionId());
        codingQuestionService.addLabelToQuestion(createDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Add multiple labels to question
     * 
     * @param bulkCreateDto bulk question-label relationship data
     * @return 201 Created if successful
     */
    @Operation(summary = "Add multiple labels to question", description = "Creates multiple relationships between a question and labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question-label relationships created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or labels not found")
    })
    @PostMapping("/bulk")
    public ResponseEntity<Void> addLabelsToQuestion(
            @Parameter(description = "Bulk question-label relationship data", required = true)
            @Valid @RequestBody QuestionLabelBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/question-labels/bulk - Adding {} labels to question {}", 
                bulkCreateDto.getLabelAssignments().size(), bulkCreateDto.getQuestionId());
        codingQuestionService.addLabelsToQuestion(bulkCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove label from question
     * 
     * @param questionId question ID
     * @param labelId label ID
     * @return 204 No Content if successful
     */
    @Operation(summary = "Remove label from question", description = "Removes a relationship between a question and a label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question-label relationship removed successfully"),
            @ApiResponse(responseCode = "404", description = "Question-label relationship not found")
    })
    @DeleteMapping("/question/{questionId}/label/{labelId}")
    public ResponseEntity<Void> removeLabelFromQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Label ID", required = true)
            @PathVariable @ValidId String labelId) {

        log.info("DELETE /v1/question-labels/question/{}/label/{} - Removing label from question", 
                questionId, labelId);
        codingQuestionService.removeLabelFromQuestion(questionId, labelId);
        return ResponseEntity.noContent().build();
    }
}