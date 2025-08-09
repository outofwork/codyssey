package com.codyssey.api.controller;

import com.codyssey.api.dto.mcq.*;
import com.codyssey.api.service.MCQQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for MCQ (Multiple Choice Question) operations
 */
@RestController
@RequestMapping("/v1/mcq")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MCQ Questions", description = "Multiple Choice Questions management API")
public class MCQController {

    private final MCQQuestionService mcqQuestionService;

    @Operation(summary = "Create a new MCQ question", description = "Creates a new multiple choice question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MCQ question created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQQuestionDto> createMCQQuestion(@Valid @RequestBody MCQQuestionCreateDto createDto) {
        log.info("Request to create MCQ question");
        MCQQuestionDto createdQuestion = mcqQuestionService.createMCQQuestion(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @Operation(summary = "Get MCQ question by ID", description = "Retrieves an MCQ question by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ question found"),
            @ApiResponse(responseCode = "404", description = "MCQ question not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MCQQuestionDto> getMCQQuestionById(@PathVariable String id) {
        log.info("Request to get MCQ question by ID: {}", id);
        MCQQuestionDto mcqQuestion = mcqQuestionService.getMCQQuestionById(id);
        return ResponseEntity.ok(mcqQuestion);
    }

    @Operation(summary = "Get MCQ question by URL slug", description = "Retrieves an MCQ question by its URL slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ question found"),
            @ApiResponse(responseCode = "404", description = "MCQ question not found")
    })
    @GetMapping("/slug/{urlSlug}")
    public ResponseEntity<MCQQuestionDto> getMCQQuestionBySlug(@PathVariable String urlSlug) {
        log.info("Request to get MCQ question by slug: {}", urlSlug);
        MCQQuestionDto mcqQuestion = mcqQuestionService.getMCQQuestionBySlug(urlSlug);
        return ResponseEntity.ok(mcqQuestion);
    }

    @Operation(summary = "Update MCQ question", description = "Updates an existing MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ question updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "MCQ question not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQQuestionDto> updateMCQQuestion(@PathVariable String id, 
                                                           @Valid @RequestBody MCQQuestionUpdateDto updateDto) {
        log.info("Request to update MCQ question with ID: {}", id);
        MCQQuestionDto updatedQuestion = mcqQuestionService.updateMCQQuestion(id, updateDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    @Operation(summary = "Delete MCQ question", description = "Deletes an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "MCQ question deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "MCQ question not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMCQQuestion(@PathVariable String id) {
        log.info("Request to delete MCQ question with ID: {}", id);
        mcqQuestionService.deleteMCQQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all MCQ questions", description = "Retrieves all MCQ questions with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<MCQQuestionSummaryDto>> getAllMCQQuestions(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Request to get all MCQ questions - page: {}, size: {}", page, size);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getAllMCQQuestions(pageable);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by label", description = "Retrieves MCQ questions associated with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByLabel(@PathVariable String labelSlug) {
        log.info("Request to get MCQ questions by label slug: {}", labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByLabelSlug(labelSlug);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by label with pagination", description = "Retrieves MCQ questions associated with a specific label with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/paginated")
    public ResponseEntity<Page<MCQQuestionSummaryDto>> getMCQQuestionsByLabelPaginated(
            @PathVariable String labelSlug,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Request to get MCQ questions by label slug: {} with pagination", labelSlug);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByLabelSlug(labelSlug, pageable);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by label hierarchy", description = "Retrieves MCQ questions associated with a label and its children")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/hierarchy")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByLabelHierarchy(@PathVariable String labelSlug) {
        log.info("Request to get MCQ questions by label hierarchy for slug: {}", labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByLabelHierarchySlug(labelSlug);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get random MCQ questions by label", description = "Retrieves random MCQ questions associated with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/random")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getRandomMCQQuestionsByLabel(
            @PathVariable String labelSlug,
            @Parameter(description = "Number of random questions to retrieve")
            @RequestParam(defaultValue = "10") int count) {
        log.info("Request to get {} random MCQ questions by label slug: {}", count, labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getRandomMCQQuestionsByLabelSlug(labelSlug, count);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get random MCQ questions by label hierarchy", description = "Retrieves random MCQ questions from a label and its children")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/random/hierarchy")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getRandomMCQQuestionsByLabelHierarchy(
            @PathVariable String labelSlug,
            @Parameter(description = "Number of random questions to retrieve")
            @RequestParam(defaultValue = "10") int count) {
        log.info("Request to get {} random MCQ questions by label hierarchy for slug: {}", count, labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getRandomMCQQuestionsByLabelHierarchySlug(labelSlug, count);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Add label to MCQ question", description = "Associates a label with an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label added to MCQ question successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or association already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "MCQ question or label not found")
    })
    @PostMapping("/labels")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQLabelReferenceDto> addLabelToMCQQuestion(@Valid @RequestBody MCQLabelCreateDto createDto) {
        log.info("Request to add label to MCQ question");
        MCQLabelReferenceDto mcqLabel = mcqQuestionService.addLabelToMCQQuestion(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mcqLabel);
    }

    @Operation(summary = "Remove label from MCQ question", description = "Removes the association between a label and an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label removed from MCQ question successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @DeleteMapping("/{mcqQuestionId}/labels/{labelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Void> removeLabelFromMCQQuestion(@PathVariable String mcqQuestionId, 
                                                          @PathVariable String labelId) {
        log.info("Request to remove label {} from MCQ question {}", labelId, mcqQuestionId);
        mcqQuestionService.removeLabelFromMCQQuestion(mcqQuestionId, labelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get MCQ question count by label", description = "Returns the count of MCQ questions associated with a specific label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/count")
    public ResponseEntity<Long> getMCQQuestionCountByLabel(@PathVariable String labelSlug) {
        log.info("Request to get MCQ question count by label slug: {}", labelSlug);
        long count = mcqQuestionService.getMCQQuestionCountByLabelSlug(labelSlug);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get MCQ question count by label hierarchy", description = "Returns the count of MCQ questions in a label hierarchy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/count/hierarchy")
    public ResponseEntity<Long> getMCQQuestionCountByLabelHierarchy(@PathVariable String labelSlug) {
        log.info("Request to get MCQ question count by label hierarchy for slug: {}", labelSlug);
        long count = mcqQuestionService.getMCQQuestionCountByLabelHierarchySlug(labelSlug);
        return ResponseEntity.ok(count);
    }
}
