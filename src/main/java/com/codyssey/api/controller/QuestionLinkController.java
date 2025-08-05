package com.codyssey.api.controller;

import com.codyssey.api.dto.link.QuestionLinkCreateDto;
import com.codyssey.api.dto.link.QuestionLinkBulkCreateDto;
import com.codyssey.api.dto.question.CodingQuestionSummaryDto;
import com.codyssey.api.service.QuestionLinkService;
import com.codyssey.api.dto.link.QuestionLinkDto;
import com.codyssey.api.dto.link.LinkSequenceDto;
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
 * REST Controller for QuestionLink operations
 * <p>
 * Provides endpoints for question relationship management including creation, retrieval,
 * deletion, and bulk operations for managing follow-ups, similar problems, and prerequisites.
 */
@RestController
@RequestMapping("/v1/question-links")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Link Management", description = "APIs for managing question relationships (follow-ups, similar, prerequisites)")
public class QuestionLinkController {

    private final QuestionLinkService questionLinkService;

    @Operation(summary = "Create a new question link", description = "Creates a relationship link between two coding questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question link created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Source or target question not found"),
            @ApiResponse(responseCode = "409", description = "Link already exists or sequence conflict")
    })
    @PostMapping
    public ResponseEntity<QuestionLinkDto> createQuestionLink(
            @Parameter(description = "Question link creation data", required = true)
            @Valid @RequestBody QuestionLinkCreateDto createDto) {

        log.info("POST /v1/question-links - Creating link: source={}, target={}, type={}", 
                createDto.getSourceQuestionId(), createDto.getTargetQuestionId(), createDto.getRelationshipType());
        QuestionLinkDto createdLink = questionLinkService.createQuestionLink(createDto);
        return new ResponseEntity<>(createdLink, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple question links in bulk", description = "Creates multiple relationship links between questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question links created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions not found"),
            @ApiResponse(responseCode = "409", description = "One or more links already exist or sequence conflicts")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionLinkDto>> createQuestionLinksBulk(
            @Parameter(description = "Bulk question link creation data", required = true)
            @Valid @RequestBody QuestionLinkBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/question-links/bulk - Creating {} question links in bulk", bulkCreateDto.getQuestionLinks().size());
        List<QuestionLinkDto> createdLinks = questionLinkService.createQuestionLinksBulk(bulkCreateDto);
        return new ResponseEntity<>(createdLinks, HttpStatus.CREATED);
    }

    @Operation(summary = "Get outgoing links", description = "Retrieves all outgoing links from a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outgoing links retrieved successfully")
    })
    @GetMapping("/outgoing/{sourceQuestionId}")
    public ResponseEntity<List<QuestionLinkDto>> getOutgoingLinksByQuestionId(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/outgoing/{} - Retrieving outgoing links", sourceQuestionId);
        List<QuestionLinkDto> links = questionLinkService.getOutgoingLinksByQuestionId(sourceQuestionId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Get incoming links", description = "Retrieves all incoming links to a target question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incoming links retrieved successfully")
    })
    @GetMapping("/incoming/{targetQuestionId}")
    public ResponseEntity<List<QuestionLinkDto>> getIncomingLinksByQuestionId(
            @Parameter(description = "Target question ID", required = true)
            @PathVariable @ValidId String targetQuestionId) {

        log.info("GET /v1/question-links/incoming/{} - Retrieving incoming links", targetQuestionId);
        List<QuestionLinkDto> links = questionLinkService.getIncomingLinksByQuestionId(targetQuestionId);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Get question link by ID", description = "Retrieves a specific question link by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question link retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Question link not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionLinkDto> getLinkById(
            @Parameter(description = "Link ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/question-links/{} - Retrieving link by ID", id);
        return questionLinkService.getLinkById(id)
                .map(link -> ResponseEntity.ok(link))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete question link", description = "Deletes a question relationship link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question link deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question link not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionLink(
            @Parameter(description = "Link ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/question-links/{} - Deleting question link", id);
        questionLinkService.deleteQuestionLink(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get follow-up questions", description = "Retrieves follow-up questions for a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Follow-up questions retrieved successfully")
    })
    @GetMapping("/{sourceQuestionId}/follow-ups")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getFollowUpQuestions(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/{}/follow-ups - Retrieving follow-up questions", sourceQuestionId);
        List<CodingQuestionSummaryDto> questions = questionLinkService.getFollowUpQuestions(sourceQuestionId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get similar questions", description = "Retrieves similar questions for a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Similar questions retrieved successfully")
    })
    @GetMapping("/{sourceQuestionId}/similar")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getSimilarQuestions(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/{}/similar - Retrieving similar questions", sourceQuestionId);
        List<CodingQuestionSummaryDto> questions = questionLinkService.getSimilarQuestions(sourceQuestionId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get prerequisite questions", description = "Retrieves prerequisite questions for a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prerequisite questions retrieved successfully")
    })
    @GetMapping("/{sourceQuestionId}/prerequisites")
    public ResponseEntity<List<CodingQuestionSummaryDto>> getPrerequisiteQuestions(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/{}/prerequisites - Retrieving prerequisite questions", sourceQuestionId);
        List<CodingQuestionSummaryDto> questions = questionLinkService.getPrerequisiteQuestions(sourceQuestionId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get links by relationship type", description = "Retrieves links filtered by relationship type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Links retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid relationship type")
    })
    @GetMapping("/{sourceQuestionId}/type/{relationshipType}")
    public ResponseEntity<List<QuestionLinkDto>> getLinksByRelationshipType(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId,
            @Parameter(description = "Relationship type (FOLLOW_UP, SIMILAR, PREREQUISITE)", required = true)
            @PathVariable String relationshipType) {

        log.info("GET /v1/question-links/{}/type/{} - Retrieving links by relationship type", sourceQuestionId, relationshipType);
        List<QuestionLinkDto> links = questionLinkService.getLinksByRelationshipType(sourceQuestionId, relationshipType);
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Check sequence availability", description = "Checks if a sequence number is available for a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability check completed")
    })
    @GetMapping("/check-sequence")
    public ResponseEntity<Boolean> checkSequenceAvailability(
            @Parameter(description = "Source question ID", required = true)
            @RequestParam @ValidId String sourceQuestionId,
            @Parameter(description = "Sequence number", required = true)
            @RequestParam Integer sequence) {

        log.info("GET /v1/question-links/check-sequence?sourceQuestionId={}&sequence={} - Checking sequence availability", sourceQuestionId, sequence);
        boolean isAvailable = questionLinkService.checkSequenceAvailability(sourceQuestionId, sequence);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Get next sequence number", description = "Gets the next available sequence number for a source question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next sequence number retrieved successfully")
    })
    @GetMapping("/next-sequence/{sourceQuestionId}")
    public ResponseEntity<Integer> getNextSequenceNumber(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/next-sequence/{} - Getting next sequence number", sourceQuestionId);
        Integer nextSequence = questionLinkService.getNextSequenceNumber(sourceQuestionId);
        return ResponseEntity.ok(nextSequence);
    }

    @Operation(summary = "Reorder links", description = "Reorders links for a source question by updating their sequences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Links reordered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reorder data"),
            @ApiResponse(responseCode = "404", description = "Source question or links not found")
    })
    @PutMapping("/reorder/{sourceQuestionId}")
    public ResponseEntity<Void> reorderLinks(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId,
            @Parameter(description = "List of link IDs with new sequences", required = true)
            @Valid @RequestBody List<LinkSequenceDto> linkSequences) {

        log.info("PUT /v1/question-links/reorder/{} - Reordering {} links", sourceQuestionId, linkSequences.size());
        questionLinkService.reorderLinks(sourceQuestionId, linkSequences);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove all links for question", description = "Removes all links for a question (both as source and target)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All links removed successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Void> removeAllLinksForQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("DELETE /v1/question-links/question/{} - Removing all links for question", questionId);
        questionLinkService.removeAllLinksForQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if link exists", description = "Checks if a specific link exists between two questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists")
    public ResponseEntity<Boolean> isLinkExists(
            @Parameter(description = "Source question ID", required = true)
            @RequestParam @ValidId String sourceQuestionId,
            @Parameter(description = "Target question ID", required = true)
            @RequestParam @ValidId String targetQuestionId,
            @Parameter(description = "Relationship type", required = true)
            @RequestParam String relationshipType) {

        log.info("GET /v1/question-links/exists?sourceQuestionId={}&targetQuestionId={}&relationshipType={} - Checking link existence", 
                sourceQuestionId, targetQuestionId, relationshipType);
        boolean exists = questionLinkService.isLinkExists(sourceQuestionId, targetQuestionId, relationshipType);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Get outgoing links count", description = "Gets the count of outgoing links from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outgoing links count retrieved successfully")
    })
    @GetMapping("/{sourceQuestionId}/outgoing/count")
    public ResponseEntity<Long> getOutgoingLinksCount(
            @Parameter(description = "Source question ID", required = true)
            @PathVariable @ValidId String sourceQuestionId) {

        log.info("GET /v1/question-links/{}/outgoing/count - Getting outgoing links count", sourceQuestionId);
        Long count = questionLinkService.getOutgoingLinksCount(sourceQuestionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get incoming links count", description = "Gets the count of incoming links to a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incoming links count retrieved successfully")
    })
    @GetMapping("/{targetQuestionId}/incoming/count")
    public ResponseEntity<Long> getIncomingLinksCount(
            @Parameter(description = "Target question ID", required = true)
            @PathVariable @ValidId String targetQuestionId) {

        log.info("GET /v1/question-links/{}/incoming/count - Getting incoming links count", targetQuestionId);
        Long count = questionLinkService.getIncomingLinksCount(targetQuestionId);
        return ResponseEntity.ok(count);
    }
}