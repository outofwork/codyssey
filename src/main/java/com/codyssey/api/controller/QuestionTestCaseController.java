package com.codyssey.api.controller;

import com.codyssey.api.dto.testcase.*;
import com.codyssey.api.service.QuestionTestCaseService;
import com.codyssey.api.dto.testcase.TestCaseSequenceDto;
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
 * REST Controller for QuestionTestCase operations
 * <p>
 * Provides endpoints for test case management including creation, retrieval,
 * updating, deletion, and bulk operations for coding question test cases.
 */
@RestController
@RequestMapping("/v1/test-cases")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Test Case Management", description = "APIs for managing coding question test cases with input/output validation")
public class QuestionTestCaseController {

    private final QuestionTestCaseService testCaseService;

    @Operation(summary = "Create a new test case", description = "Creates a new test case for a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Test case created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "409", description = "Sequence number already exists for the question")
    })
    @PostMapping
    public ResponseEntity<QuestionTestCaseDto> createTestCase(
            @Parameter(description = "Test case creation data", required = true)
            @Valid @RequestBody QuestionTestCaseCreateDto createDto) {

        log.info("POST /v1/test-cases - Creating test case for question: {}", createDto.getQuestionId());
        QuestionTestCaseDto createdTestCase = testCaseService.createTestCase(createDto);
        return new ResponseEntity<>(createdTestCase, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple test cases in bulk", description = "Creates multiple test cases for one or more coding questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Test cases created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions not found"),
            @ApiResponse(responseCode = "409", description = "One or more sequence conflicts")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionTestCaseDto>> createTestCasesBulk(
            @Parameter(description = "Bulk test case creation data", required = true)
            @Valid @RequestBody QuestionTestCaseBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/test-cases/bulk - Creating {} test cases in bulk", bulkCreateDto.getTestCases().size());
        List<QuestionTestCaseDto> createdTestCases = testCaseService.createTestCasesBulk(bulkCreateDto);
        return new ResponseEntity<>(createdTestCases, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all test cases", description = "Retrieves all test cases (non-deleted)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test cases retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<QuestionTestCaseDto>> getAllTestCases() {
        log.info("GET /v1/test-cases - Retrieving all test cases");
        List<QuestionTestCaseDto> testCases = testCaseService.getAllTestCases();
        return ResponseEntity.ok(testCases);
    }

    @Operation(summary = "Get test case by ID", description = "Retrieves a specific test case by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test case retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Test case not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionTestCaseDto> getTestCaseById(
            @Parameter(description = "Test case ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/test-cases/{} - Retrieving test case by ID", id);
        return testCaseService.getTestCaseById(id)
                .map(testCase -> ResponseEntity.ok(testCase))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update test case", description = "Updates an existing test case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test case updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Test case not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionTestCaseDto> updateTestCase(
            @Parameter(description = "Test case ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Test case update data", required = true)
            @Valid @RequestBody com.codyssey.api.dto.testcase.QuestionTestCaseUpdateDto updateDto) {

        log.info("PUT /v1/test-cases/{} - Updating test case", id);
        QuestionTestCaseDto updatedTestCase = testCaseService.updateTestCase(id, updateDto);
        return ResponseEntity.ok(updatedTestCase);
    }

    @Operation(summary = "Delete test case", description = "Soft deletes a test case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Test case deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Test case not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(
            @Parameter(description = "Test case ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/test-cases/{} - Deleting test case", id);
        testCaseService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get test cases by question", description = "Retrieves all test cases for a specific question ordered by sequence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test cases retrieved successfully")
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<QuestionTestCaseDto>> getTestCasesByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/test-cases/question/{} - Retrieving test cases by question ID", questionId);
        List<QuestionTestCaseDto> testCases = testCaseService.getTestCasesByQuestionId(questionId);
        return ResponseEntity.ok(testCases);
    }

    @Operation(summary = "Get sample test cases", description = "Retrieves sample test cases for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sample test cases retrieved successfully")
    })
    @GetMapping("/question/{questionId}/sample")
    public ResponseEntity<List<QuestionTestCaseDto>> getSampleTestCasesByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/test-cases/question/{}/sample - Retrieving sample test cases", questionId);
        List<QuestionTestCaseDto> testCases = testCaseService.getSampleTestCasesByQuestionId(questionId);
        return ResponseEntity.ok(testCases);
    }

    @Operation(summary = "Get hidden test cases", description = "Retrieves hidden test cases for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hidden test cases retrieved successfully")
    })
    @GetMapping("/question/{questionId}/hidden")
    public ResponseEntity<List<QuestionTestCaseDto>> getHiddenTestCasesByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/test-cases/question/{}/hidden - Retrieving hidden test cases", questionId);
        List<QuestionTestCaseDto> testCases = testCaseService.getHiddenTestCasesByQuestionId(questionId);
        return ResponseEntity.ok(testCases);
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

        log.info("GET /v1/test-cases/check-sequence?questionId={}&sequence={} - Checking sequence availability", questionId, sequence);
        boolean isAvailable = testCaseService.checkSequenceAvailability(questionId, sequence);
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

        log.info("GET /v1/test-cases/next-sequence/{} - Getting next sequence number", questionId);
        Integer nextSequence = testCaseService.getNextSequenceNumber(questionId);
        return ResponseEntity.ok(nextSequence);
    }

    @Operation(summary = "Reorder test cases", description = "Reorders test cases for a question by updating their sequences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Test cases reordered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reorder data"),
            @ApiResponse(responseCode = "404", description = "Question or test cases not found")
    })
    @PutMapping("/reorder/{questionId}")
    public ResponseEntity<Void> reorderTestCases(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of test case IDs with new sequences", required = true)
            @Valid @RequestBody List<TestCaseSequenceDto> testCaseSequences) {

        log.info("PUT /v1/test-cases/reorder/{} - Reordering {} test cases", questionId, testCaseSequences.size());
        testCaseService.reorderTestCases(questionId, testCaseSequences);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Toggle sample status", description = "Toggles whether a test case is a sample or hidden")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sample status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Test case not found")
    })
    @PutMapping("/{id}/sample")
    public ResponseEntity<QuestionTestCaseDto> toggleSampleStatus(
            @Parameter(description = "Test case ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Whether the test case should be a sample", required = true)
            @RequestParam Boolean isSample) {

        log.info("PUT /v1/test-cases/{}/sample?isSample={} - Toggling sample status", id, isSample);
        QuestionTestCaseDto updatedTestCase = testCaseService.toggleSampleStatus(id, isSample);
        return ResponseEntity.ok(updatedTestCase);
    }

    @Operation(summary = "Delete all test cases for question", description = "Deletes all test cases for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All test cases deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Void> deleteAllTestCasesForQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("DELETE /v1/test-cases/question/{} - Deleting all test cases for question", questionId);
        testCaseService.deleteAllTestCasesForQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get test cases count", description = "Gets the count of test cases for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test cases count retrieved successfully")
    })
    @GetMapping("/question/{questionId}/count")
    public ResponseEntity<Long> getTestCasesCountByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/test-cases/question/{}/count - Getting test cases count", questionId);
        Long count = testCaseService.getTestCasesCountByQuestionId(questionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get sample test cases count", description = "Gets the count of sample test cases for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sample test cases count retrieved successfully")
    })
    @GetMapping("/question/{questionId}/sample/count")
    public ResponseEntity<Long> getSampleTestCasesCountByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/test-cases/question/{}/sample/count - Getting sample test cases count", questionId);
        Long count = testCaseService.getSampleTestCasesCountByQuestionId(questionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Search test cases by input", description = "Searches test cases by input data containing specific text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search/input")
    public ResponseEntity<List<QuestionTestCaseDto>> searchTestCasesByInputData(
            @Parameter(description = "Search term", required = true)
            @RequestParam String q) {

        log.info("GET /v1/test-cases/search/input?q={} - Searching test cases by input data", q);
        List<QuestionTestCaseDto> testCases = testCaseService.searchTestCasesByInputData(q);
        return ResponseEntity.ok(testCases);
    }

    @Operation(summary = "Clone test cases", description = "Clones test cases from one question to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Test cases cloned successfully"),
            @ApiResponse(responseCode = "404", description = "Source or target question not found")
    })
    @PostMapping("/clone")
    public ResponseEntity<List<QuestionTestCaseDto>> cloneTestCases(
            @Parameter(description = "Source question ID", required = true)
            @RequestParam @ValidId String sourceQuestionId,
            @Parameter(description = "Target question ID", required = true)
            @RequestParam @ValidId String targetQuestionId) {

        log.info("POST /v1/test-cases/clone?sourceQuestionId={}&targetQuestionId={} - Cloning test cases", sourceQuestionId, targetQuestionId);
        List<QuestionTestCaseDto> clonedTestCases = testCaseService.cloneTestCases(sourceQuestionId, targetQuestionId);
        return new ResponseEntity<>(clonedTestCases, HttpStatus.CREATED);
    }
}