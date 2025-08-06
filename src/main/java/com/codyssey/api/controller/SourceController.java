package com.codyssey.api.controller;

import com.codyssey.api.dto.source.*;
import com.codyssey.api.service.SourceService;
import com.codyssey.api.validation.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Source operations
 * <p>
 * Provides endpoints for source management including creation, retrieval,
 * updating, deletion, and search operations.
 */
@RestController
@RequestMapping("/v1/sources")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Source Management", description = "APIs for managing coding question sources/platforms")
public class SourceController {

    private final SourceService sourceService;

    @Operation(summary = "Create a new source", description = "Creates a new coding question source/platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Source created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Source code already exists")
    })
    @PostMapping
    public ResponseEntity<SourceDto> createSource(
            @Parameter(description = "Source creation data", required = true)
            @Valid @RequestBody SourceCreateDto createDto) {

        log.info("POST /v1/sources - Creating source: {}", createDto.getCode());
        SourceDto createdSource = sourceService.createSource(createDto);
        return new ResponseEntity<>(createdSource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all sources", description = "Retrieves all non-deleted sources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sources retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<SourceDto>> getAllSources() {
        log.info("GET /v1/sources - Retrieving all sources");
        List<SourceDto> sources = sourceService.getAllSources();
        return ResponseEntity.ok(sources);
    }

    @Operation(summary = "Get active sources", description = "Retrieves all active, non-deleted sources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active sources retrieved successfully")
    })
    @GetMapping("/active")
    public ResponseEntity<List<SourceDto>> getActiveSources() {
        log.info("GET /v1/sources/active - Retrieving active sources");
        List<SourceDto> sources = sourceService.getActiveSources();
        return ResponseEntity.ok(sources);
    }

    @Operation(summary = "Get source summaries", description = "Retrieves summary information for all sources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Source summaries retrieved successfully")
    })
    @GetMapping("/summaries")
    public ResponseEntity<List<SourceSummaryDto>> getSourceSummaries() {
        log.info("GET /v1/sources/summaries - Retrieving source summaries");
        List<SourceSummaryDto> sources = sourceService.getSourceSummaries();
        return ResponseEntity.ok(sources);
    }

    @Operation(summary = "Get a specific source by ID", description = "Retrieves a specific source by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Source found"),
            @ApiResponse(responseCode = "404", description = "Source not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SourceDto> getSourceById(
            @Parameter(description = "Source ID (SRC-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/sources/{} - Retrieving source by ID", id);
        return sourceService.getSourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get source by code", description = "Retrieves a source by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Source found"),
            @ApiResponse(responseCode = "404", description = "Source not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<SourceDto> getSourceByCode(
            @Parameter(description = "Source code (e.g., LEETCODE)", required = true)
            @PathVariable @NotBlank String code) {

        log.info("GET /v1/sources/code/{} - Retrieving source by code", code);
        return sourceService.getSourceByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing source", description = "Updates an existing source's properties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Source updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Source not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SourceDto> updateSource(
            @Parameter(description = "Source ID (SRC-xxxxxx)", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Updated source data", required = true)
            @Valid @RequestBody SourceUpdateDto updateDto) {

        log.info("PUT /v1/sources/{} - Updating source", id);
        SourceDto updatedSource = sourceService.updateSource(id, updateDto);
        return ResponseEntity.ok(updatedSource);
    }

    @Operation(summary = "Delete a source", description = "Soft deletes a source by ID. Cannot delete if source has questions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Source deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete source with questions"),
            @ApiResponse(responseCode = "404", description = "Source not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(
            @Parameter(description = "Source ID (SRC-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/sources/{} - Deleting source", id);
        sourceService.deleteSource(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search sources", description = "Search sources by name containing the search term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<SourceDto>> searchSources(
            @Parameter(description = "Search term", required = true)
            @RequestParam @NotBlank String searchTerm) {

        log.info("GET /v1/sources/search?searchTerm={} - Searching sources", searchTerm);
        List<SourceDto> sources = sourceService.searchSources(searchTerm);
        return ResponseEntity.ok(sources);
    }

    @Operation(summary = "Check source code availability", description = "Check if a source code is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked successfully")
    })
    @GetMapping("/check-code-availability")
    public ResponseEntity<Boolean> checkCodeAvailability(
            @Parameter(description = "Source code to check", required = true)
            @RequestParam @NotBlank String code) {

        log.info("GET /v1/sources/check-code-availability?code={} - Checking code availability", code);
        boolean available = !sourceService.existsByCode(code);
        return ResponseEntity.ok(available);
    }

    @Operation(summary = "Toggle source active status", description = "Toggle the active status of a source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "Source not found")
    })
    @PatchMapping("/{id}/active")
    public ResponseEntity<SourceDto> toggleActiveStatus(
            @Parameter(description = "Source ID (SRC-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("PATCH /v1/sources/{}/active - Toggling active status", id);
        SourceDto updatedSource = sourceService.toggleActiveStatus(id);
        return ResponseEntity.ok(updatedSource);
    }

    @Operation(summary = "Get questions count for source", description = "Get the count of questions for a specific source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions count retrieved successfully")
    })
    @GetMapping("/{id}/questions-count")
    public ResponseEntity<Long> getQuestionsCount(
            @Parameter(description = "Source ID (SRC-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/sources/{}/questions-count - Getting questions count", id);
        Long count = sourceService.getQuestionsCountBySource(id);
        return ResponseEntity.ok(count);
    }
}