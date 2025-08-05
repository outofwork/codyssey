package com.codyssey.api.controller;

import com.codyssey.api.dto.solution.*;
import com.codyssey.api.service.QuestionSolutionService;
import com.codyssey.api.dto.solution.SolutionSequenceDto;
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
 * REST Controller for QuestionSolution operations
 * <p>
 * Provides endpoints for solution management including creation, retrieval,
 * updating, deletion, and bulk operations for coding question solutions.
 */
@RestController
@RequestMapping("/v1/solutions")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Solution Management", description = "APIs for managing coding question solutions in multiple programming languages")
public class QuestionSolutionController {

    private final QuestionSolutionService solutionService;

    @Operation(summary = "Create a new solution", description = "Creates a new solution for a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solution created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or user not found"),
            @ApiResponse(responseCode = "409", description = "Sequence number already exists for the question")
    })
    @PostMapping
    public ResponseEntity<QuestionSolutionDto> createSolution(
            @Parameter(description = "Solution creation data", required = true)
            @Valid @RequestBody QuestionSolutionCreateDto createDto) {

        log.info("POST /v1/solutions - Creating solution for question: {}", createDto.getQuestionId());
        QuestionSolutionDto createdSolution = solutionService.createSolution(createDto);
        return new ResponseEntity<>(createdSolution, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple solutions in bulk", description = "Creates multiple solutions for one or more coding questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solutions created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions not found"),
            @ApiResponse(responseCode = "409", description = "One or more sequence conflicts")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionSolutionDto>> createSolutionsBulk(
            @Parameter(description = "Bulk solution creation data", required = true)
            @Valid @RequestBody QuestionSolutionBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/solutions/bulk - Creating {} solutions in bulk", bulkCreateDto.getSolutions().size());
        List<QuestionSolutionDto> createdSolutions = solutionService.createSolutionsBulk(bulkCreateDto);
        return new ResponseEntity<>(createdSolutions, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all solutions", description = "Retrieves all solutions (non-deleted)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<QuestionSolutionDto>> getAllSolutions() {
        log.info("GET /v1/solutions - Retrieving all solutions");
        List<QuestionSolutionDto> solutions = solutionService.getAllSolutions();
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Get solution by ID", description = "Retrieves a specific solution by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionSolutionDto> getSolutionById(
            @Parameter(description = "Solution ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/solutions/{} - Retrieving solution by ID", id);
        return solutionService.getSolutionById(id)
                .map(solution -> ResponseEntity.ok(solution))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update solution", description = "Updates an existing solution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionSolutionDto> updateSolution(
            @Parameter(description = "Solution ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Solution update data", required = true)
            @Valid @RequestBody QuestionSolutionUpdateDto updateDto) {

        log.info("PUT /v1/solutions/{} - Updating solution", id);
        QuestionSolutionDto updatedSolution = solutionService.updateSolution(id, updateDto);
        return ResponseEntity.ok(updatedSolution);
    }

    @Operation(summary = "Delete solution", description = "Soft deletes a solution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solution deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolution(
            @Parameter(description = "Solution ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/solutions/{} - Deleting solution", id);
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get solutions by question", description = "Retrieves all solutions for a specific question ordered by sequence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<QuestionSolutionDto>> getSolutionsByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/solutions/question/{} - Retrieving solutions by question ID", questionId);
        List<QuestionSolutionDto> solutions = solutionService.getSolutionsByQuestionId(questionId);
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Get solutions by language", description = "Retrieves solutions filtered by programming language")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    @GetMapping("/language/{language}")
    public ResponseEntity<List<QuestionSolutionDto>> getSolutionsByLanguage(
            @Parameter(description = "Programming language (e.g., Java, Python, C++)", required = true)
            @PathVariable String language) {

        log.info("GET /v1/solutions/language/{} - Retrieving solutions by language", language);
        List<QuestionSolutionDto> solutions = solutionService.getSolutionsByLanguage(language);
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Get optimal solutions", description = "Retrieves optimal solutions for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Optimal solutions retrieved successfully")
    })
    @GetMapping("/question/{questionId}/optimal")
    public ResponseEntity<List<QuestionSolutionDto>> getOptimalSolutionsByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/solutions/question/{}/optimal - Retrieving optimal solutions", questionId);
        List<QuestionSolutionDto> solutions = solutionService.getOptimalSolutionsByQuestionId(questionId);
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Get solutions by question and language", description = "Retrieves solutions for a question in a specific language")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    @GetMapping("/question/{questionId}/language/{language}")
    public ResponseEntity<List<QuestionSolutionDto>> getSolutionsByQuestionIdAndLanguage(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Programming language", required = true)
            @PathVariable String language) {

        log.info("GET /v1/solutions/question/{}/language/{} - Retrieving solutions by question and language", questionId, language);
        List<QuestionSolutionDto> solutions = solutionService.getSolutionsByQuestionIdAndLanguage(questionId, language);
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Get solutions by user", description = "Retrieves solutions created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuestionSolutionDto>> getSolutionsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable @ValidId String userId) {

        log.info("GET /v1/solutions/user/{} - Retrieving solutions by user", userId);
        List<QuestionSolutionDto> solutions = solutionService.getSolutionsByUser(userId);
        return ResponseEntity.ok(solutions);
    }

    @Operation(summary = "Check sequence availability", description = "Checks if a sequence number is available for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability check completed")
    })
    @GetMapping("/check-sequence")
    public ResponseEntity<Boolean> checkSequenceAvailability(
            @Parameter(description = "Question ID", required = true)
            @RequestParam @ValidId String questionId,
            @Parameter(description = "Sequence number", required = true)
            @RequestParam Integer sequence) {

        log.info("GET /v1/solutions/check-sequence?questionId={}&sequence={} - Checking sequence availability", questionId, sequence);
        boolean isAvailable = solutionService.checkSequenceAvailability(questionId, sequence);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Get next sequence number", description = "Gets the next available sequence number for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next sequence number retrieved successfully")
    })
    @GetMapping("/next-sequence/{questionId}")
    public ResponseEntity<Integer> getNextSequenceNumber(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/solutions/next-sequence/{} - Getting next sequence number", questionId);
        Integer nextSequence = solutionService.getNextSequenceNumber(questionId);
        return ResponseEntity.ok(nextSequence);
    }

    @Operation(summary = "Reorder solutions", description = "Reorders solutions for a question by updating their sequences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solutions reordered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reorder data"),
            @ApiResponse(responseCode = "404", description = "Question or solutions not found")
    })
    @PutMapping("/reorder/{questionId}")
    public ResponseEntity<Void> reorderSolutions(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of solution IDs with new sequences", required = true)
            @Valid @RequestBody List<SolutionSequenceDto> solutionSequences) {

        log.info("PUT /v1/solutions/reorder/{} - Reordering {} solutions", questionId, solutionSequences.size());
        solutionService.reorderSolutions(questionId, solutionSequences);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark solution as optimal", description = "Marks a solution as optimal or non-optimal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution optimal status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    @PutMapping("/{id}/optimal")
    public ResponseEntity<QuestionSolutionDto> markSolutionOptimal(
            @Parameter(description = "Solution ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Whether the solution is optimal", required = true)
            @RequestParam Boolean isOptimal) {

        log.info("PUT /v1/solutions/{}/optimal?isOptimal={} - Marking solution optimal status", id, isOptimal);
        QuestionSolutionDto updatedSolution = solutionService.markSolutionOptimal(id, isOptimal);
        return ResponseEntity.ok(updatedSolution);
    }
}