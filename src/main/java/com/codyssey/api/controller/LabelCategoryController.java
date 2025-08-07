package com.codyssey.api.controller;

import com.codyssey.api.dto.labelcategory.LabelCategoryCreateDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryUpdateDto;
import com.codyssey.api.service.LabelCategoryService;
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
import java.util.Optional;
import jakarta.validation.constraints.NotBlank;

/**
 * REST Controller for LabelCategory operations
 * <p>
 * Provides endpoints for label category management including
 * creation, retrieval, updating, deletion, and search.
 */
@RestController
@RequestMapping("/v1/labelcategories")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Label Category Management", description = "APIs for managing label categories")
public class LabelCategoryController {

    private final LabelCategoryService labelCategoryService;

    @Operation(summary = "Create a new label category", description = "Creates a new label category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Code already exists")
    })
    @PostMapping
    public ResponseEntity<LabelCategoryDto> createCategory(
            @Parameter(description = "Label category creation data", required = true)
            @Valid @RequestBody LabelCategoryCreateDto createDto) {

        log.info("POST /v1/labelcategories - Creating label category: {}", createDto.getCode());
        LabelCategoryDto createdCategory = labelCategoryService.createCategory(createDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all label categories", description = "Retrieves all label categories or only active ones based on query parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<LabelCategoryDto>> getAllCategories(
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active) {

        if (active != null && active) {
            log.info("GET /v1/labelcategories?active=true - Retrieving active label categories");
            List<LabelCategoryDto> categories = labelCategoryService.getActiveCategories();
            return ResponseEntity.ok(categories);
        } else {
            log.info("GET /v1/labelcategories - Retrieving all label categories");
            List<LabelCategoryDto> categories = labelCategoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        }
    }

    @Operation(summary = "Get label category by URL slug or ID", description = "Retrieves a label category by its SEO-friendly URL slug or ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label category found"),
            @ApiResponse(responseCode = "404", description = "Label category not found")
    })
    @GetMapping("/{identifier}")
    public ResponseEntity<LabelCategoryDto> getCategory(
            @Parameter(description = "Label category URL slug (e.g., difficulty-level) or ID (CAT-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier) {

        log.info("GET /v1/labelcategories/{} - Retrieving label category", identifier);
        
        // First try to find by URL slug
        Optional<LabelCategoryDto> categoryBySlug = labelCategoryService.getCategoryByUrlSlug(identifier);
        if (categoryBySlug.isPresent()) {
            return ResponseEntity.ok(categoryBySlug.get());
        }
        
        // If not found and it looks like an ID (starts with CAT-), try ID lookup for backward compatibility
        if (identifier.startsWith("CAT-")) {
            return labelCategoryService.getCategoryById(identifier)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update label category", description = "Updates an existing label category using URL slug or ID (code cannot be changed)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Label category not found")
    })
    @PutMapping("/{identifier}")
    public ResponseEntity<LabelCategoryDto> updateCategory(
            @Parameter(description = "Label category URL slug (e.g., difficulty-level) or ID (CAT-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier,
            @Parameter(description = "Updated label category data (code cannot be changed)", required = true)
            @Valid @RequestBody LabelCategoryUpdateDto updateDto) {

        log.info("PUT /v1/labelcategories/{} - Updating label category", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("CAT-")) {
            LabelCategoryDto updatedCategory = labelCategoryService.updateCategory(identifier, updateDto);
            return ResponseEntity.ok(updatedCategory);
        } else {
            LabelCategoryDto updatedCategory = labelCategoryService.updateCategoryByUrlSlug(identifier, updateDto);
            return ResponseEntity.ok(updatedCategory);
        }
    }

    @Operation(summary = "Delete label category", description = "Soft deletes a label category by URL slug or ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Label category not found")
    })
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Label category URL slug (e.g., difficulty-level) or ID (CAT-xxxxxx)", required = true)
            @PathVariable @NotBlank String identifier) {

        log.info("DELETE /v1/labelcategories/{} - Deleting label category", identifier);
        
        // Try URL slug first, then ID if it looks like an ID
        if (identifier.startsWith("CAT-")) {
            labelCategoryService.deleteCategory(identifier);
        } else {
            labelCategoryService.deleteCategoryByUrlSlug(identifier);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search label categories by name", description = "Searches label categories by name (case insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<LabelCategoryDto>> searchCategoriesByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name) {

        log.info("GET /v1/labelcategories/search?name={} - Searching categories by name", name);
        List<LabelCategoryDto> categories = labelCategoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Check code availability", description = "Checks if a code is available for use")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability checked")
    })
    @GetMapping("/check-code")
    public ResponseEntity<Boolean> checkCodeAvailability(
            @Parameter(description = "Code to check", required = true)
            @RequestParam String code) {

        log.info("GET /v1/labelcategories/check-code?code={} - Checking code availability", code);
        boolean exists = labelCategoryService.existsByCode(code);
        return ResponseEntity.ok(!exists); // Return true if available (doesn't exist)
    }
}