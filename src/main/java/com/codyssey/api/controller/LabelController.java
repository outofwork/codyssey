package com.codyssey.api.controller;

import com.codyssey.api.dto.label.*;
import com.codyssey.api.service.LabelService;
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
import java.util.Optional;

/**
 * REST Controller for Label operations
 * <p>
 * Provides endpoints for label management including creation, retrieval,
 * updating, deletion, search, and hierarchy operations.
 */
@RestController
@RequestMapping("/v1/labels")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Label Management", description = "APIs for managing labels with hierarchical relationships")
public class LabelController {

    private final LabelService labelService;

    @Operation(summary = "Create a new label", description = "Creates a new label associated with a valid label category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Label category not found"),
            @ApiResponse(responseCode = "409", description = "Label name already exists in category/parent context")
    })
    @PostMapping
    public ResponseEntity<LabelDto> createLabel(
            @Parameter(description = "Label creation data", required = true)
            @Valid @RequestBody LabelCreateDto createDto) {

        log.info("POST /v1/labels - Creating label: {}", createDto.getName());
        LabelDto createdLabel = labelService.createLabel(createDto);
        return new ResponseEntity<>(createdLabel, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple labels in bulk",
            description = "Creates multiple labels maintaining parent-child relationships. Can create parent and child from fresh or child of existing parent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Labels created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Label category or parent label not found"),
            @ApiResponse(responseCode = "409", description = "One or more label names already exist")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<LabelDto>> createLabelsBulk(
            @Parameter(description = "Bulk label creation data", required = true)
            @Valid @RequestBody LabelBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/labels/bulk - Creating {} labels in bulk", bulkCreateDto.getLabels().size());
        List<LabelDto> createdLabels = labelService.createLabelsBulk(bulkCreateDto);
        return new ResponseEntity<>(createdLabels, HttpStatus.CREATED);
    }

    @Operation(summary = "Get label by URL slug or ID", description = "Retrieves a label by its SEO-friendly URL slug or ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label found"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/{identifier}")
    public ResponseEntity<LabelDto> getLabel(
            @Parameter(description = "Label URL slug (e.g., algorithms-binary-search) or ID (LBL-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier) {

        log.info("GET /v1/labels/{} - Retrieving label", identifier);
        
        // First try to find by URL slug
        Optional<LabelDto> labelBySlug = labelService.getLabelByUrlSlug(identifier);
        if (labelBySlug.isPresent()) {
            return ResponseEntity.ok(labelBySlug.get());
        }
        
        // If not found and it looks like an ID (starts with LBL-), try ID lookup for backward compatibility
        if (identifier.startsWith("LBL-")) {
            return labelService.getLabelById(identifier)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update label", description = "Updates an existing label's properties using URL slug or ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Label not found"),
            @ApiResponse(responseCode = "409", description = "Label name already exists in category/parent context")
    })
    @PutMapping("/{identifier}")
    public ResponseEntity<LabelDto> updateLabel(
            @Parameter(description = "Label URL slug (e.g., algorithms-binary-search) or ID (LBL-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier,
            @Parameter(description = "Updated label data", required = true)
            @Valid @RequestBody LabelUpdateDto updateDto) {

        log.info("PUT /v1/labels/{} - Updating label", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("LBL-")) {
            LabelDto updatedLabel = labelService.updateLabel(identifier, updateDto);
            return ResponseEntity.ok(updatedLabel);
        } else {
            LabelDto updatedLabel = labelService.updateLabelByUrlSlug(identifier, updateDto);
            return ResponseEntity.ok(updatedLabel);
        }
    }

    @Operation(summary = "Delete label", description = "Soft deletes a label by URL slug or ID. Cannot delete if label has children.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete label with children"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteLabel(
            @Parameter(description = "Label URL slug (e.g., algorithms-binary-search) or ID (LBL-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier) {

        log.info("DELETE /v1/labels/{} - Deleting label", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("LBL-")) {
            labelService.deleteLabel(identifier);
        } else {
            labelService.deleteLabelByUrlSlug(identifier);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all labels", description = "Retrieves all non-deleted labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<LabelDto>> getAllLabels() {
        log.info("GET /v1/labels - Retrieving all labels");
        List<LabelDto> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get all labels with hierarchy", description = "Get all labels with parent-child links in hierarchical structure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label hierarchy retrieved successfully")
    })
    @GetMapping("/hierarchy")
    public ResponseEntity<List<LabelHierarchyDto>> getLabelsHierarchy() {
        log.info("GET /v1/labels/hierarchy - Retrieving labels hierarchy");
        List<LabelHierarchyDto> hierarchy = labelService.getLabelsHierarchy();
        return ResponseEntity.ok(hierarchy);
    }

    @Operation(summary = "Get labels by category", description = "Get labels by category code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LabelDto>> getLabelsByCategory(
            @Parameter(description = "Category code (case-insensitive)", required = true)
            @PathVariable String category) {

        log.info("GET /v1/labels/category/{} - Retrieving labels by category", category);
        List<LabelDto> labels = labelService.getLabelsByCategory(category.toUpperCase());
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get active labels by category", description = "Get only active labels by category code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{category}/active")
    public ResponseEntity<List<LabelDto>> getActiveLabelsByCategory(
            @Parameter(description = "Category code (case-insensitive)", required = true)
            @PathVariable String category) {

        log.info("GET /v1/labels/category/{}/active - Retrieving active labels by category", category);
        List<LabelDto> labels = labelService.getActiveLabelsByCategory(category.toUpperCase());
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get root labels by category", description = "Get root (parent) labels by category code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Root labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{category}/roots")
    public ResponseEntity<List<LabelDto>> getRootLabelsByCategory(
            @Parameter(description = "Category code (case-insensitive)", required = true)
            @PathVariable String category) {

        log.info("GET /v1/labels/category/{}/roots - Retrieving root labels by category", category);
        List<LabelDto> labels = labelService.getRootLabelsByCategory(category.toUpperCase());
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get child labels", description = "Get child labels for a given label ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Child labels retrieved successfully")
    })
    @GetMapping("/{id}/children")
    public ResponseEntity<List<LabelDto>> getChildLabels(
            @Parameter(description = "Parent label ID (LBL-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/labels/{}/children - Retrieving child labels", id);
        List<LabelDto> children = labelService.getChildLabels(id);
        return ResponseEntity.ok(children);
    }

    @Operation(summary = "Search labels by name", description = "Search labels by name containing the search term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<LabelDto>> searchLabels(
            @Parameter(description = "Search term", required = true)
            @RequestParam String searchTerm) {

        log.info("GET /v1/labels/search?searchTerm={} - Searching labels", searchTerm);
        List<LabelDto> labels = labelService.searchLabels(searchTerm);
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get available categories", description = "Get list of available categories that have labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        log.info("GET /v1/labels/categories - Retrieving available categories");
        List<String> categories = labelService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Check label name availability",
            description = "Check if a label name is available within a category and parent context")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked successfully")
    })
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkNameAvailability(
            @Parameter(description = "Label name to check", required = true)
            @RequestParam String name,
            @Parameter(description = "Category code", required = true)
            @RequestParam String categoryCode,
            @Parameter(description = "Parent label ID (optional)")
            @RequestParam(required = false) String parentId) {

        log.info("GET /v1/labels/check-availability?name={}&categoryCode={}&parentId={}",
                name, categoryCode, parentId);
        boolean available = labelService.checkNameAvailability(name, categoryCode, parentId);
        return ResponseEntity.ok(available);
    }

    @Operation(summary = "Toggle label active status", description = "Toggle the active status of a label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @PatchMapping("/{id}/active")
    public ResponseEntity<LabelDto> toggleActiveStatus(
            @Parameter(description = "Label ID (LBL-xxxxxx)", required = true)
            @PathVariable @ValidId String id) {

        log.info("PATCH /v1/labels/{}/active - Toggling active status", id);
        LabelDto updatedLabel = labelService.toggleActiveStatus(id);
        return ResponseEntity.ok(updatedLabel);
    }
}