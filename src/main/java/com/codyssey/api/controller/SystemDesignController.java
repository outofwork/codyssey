package com.codyssey.api.controller;

import com.codyssey.api.dto.systemdesign.*;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.service.SystemDesignService;
import com.codyssey.api.validation.ValidId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for SystemDesign management
 * <p>
 * Provides comprehensive endpoints for creating, retrieving, updating, and deleting
 * system design articles with advanced search and filtering capabilities.
 */
@RestController
@RequestMapping("/v1/system-designs")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SystemDesignController {

    private final SystemDesignService systemDesignService;

    /**
     * Create a new system design
     * 
     * @param createDto system design creation data
     * @return created system design with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<SystemDesignDto> createSystemDesign(@Valid @RequestBody SystemDesignCreateDto createDto) {
        log.info("POST /v1/system-designs - Creating new system design");
        SystemDesignDto createdSystemDesign = systemDesignService.createSystemDesign(createDto);
        return new ResponseEntity<>(createdSystemDesign, HttpStatus.CREATED);
    }

    /**
     * Get all system designs
     * 
     * @return list of all system designs
     */
    @GetMapping
    public ResponseEntity<List<SystemDesignSummaryDto>> getAllSystemDesigns() {
        log.info("GET /v1/system-designs - Retrieving all system designs");
        List<SystemDesignSummaryDto> systemDesigns = systemDesignService.getAllSystemDesigns();
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Get system designs with pagination
     * 
     * @param pageable pagination parameters
     * @return paginated list of system designs
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<SystemDesignSummaryDto>> getSystemDesignsWithPagination(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/system-designs/paginated - Retrieving system designs with pagination");
        Page<SystemDesignSummaryDto> systemDesigns = systemDesignService.getSystemDesignsWithPagination(pageable);
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Get system design by URL slug or ID
     * 
     * @param identifier system design URL slug or ID
     * @return system design if found, 404 if not found
     */
    @GetMapping("/{identifier}")
    public ResponseEntity<SystemDesignDto> getSystemDesign(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/{} - Retrieving system design", identifier);
        
        // First try to find by URL slug
        Optional<SystemDesignDto> systemDesignBySlug = systemDesignService.getSystemDesignByUrlSlug(identifier);
        if (systemDesignBySlug.isPresent()) {
            return ResponseEntity.ok(systemDesignBySlug.get());
        }
        
        // If not found and it looks like an ID (starts with SYS-), try ID lookup for backward compatibility
        if (identifier.startsWith("SYS-")) {
            Optional<SystemDesignDto> systemDesign = systemDesignService.getSystemDesignById(identifier);
            return systemDesign.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Update system design
     * 
     * @param identifier system design URL slug or ID
     * @param updateDto updated system design data
     * @return updated system design
     */
    @PutMapping("/{identifier}")
    public ResponseEntity<SystemDesignDto> updateSystemDesign(
            @PathVariable @NotBlank String identifier,
            @Valid @RequestBody SystemDesignUpdateDto updateDto) {
        log.info("PUT /v1/system-designs/{} - Updating system design", identifier);
        
        try {
            SystemDesignDto updatedSystemDesign;
            
            // Try to update by URL slug first, then by ID
            if (identifier.startsWith("SYS-")) {
                updatedSystemDesign = systemDesignService.updateSystemDesign(identifier, updateDto);
            } else {
                updatedSystemDesign = systemDesignService.updateSystemDesignByUrlSlug(identifier, updateDto);
            }
            
            return ResponseEntity.ok(updatedSystemDesign);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete system design
     * 
     * @param identifier system design URL slug or ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteSystemDesign(@PathVariable @NotBlank String identifier) {
        log.info("DELETE /v1/system-designs/{} - Deleting system design", identifier);
        
        try {
            if (identifier.startsWith("SYS-")) {
                systemDesignService.deleteSystemDesign(identifier);
            } else {
                systemDesignService.deleteSystemDesignByUrlSlug(identifier);
            }
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search system designs by title or description
     * 
     * @param searchTerm search term
     * @param pageable pagination parameters
     * @return paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<Page<SystemDesignSummaryDto>> searchSystemDesigns(
            @RequestParam @NotBlank String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /v1/system-designs/search - Searching system designs with term: {}", searchTerm);
        Page<SystemDesignSummaryDto> systemDesigns = systemDesignService.searchSystemDesigns(searchTerm, pageable);
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Get system designs by source
     * 
     * @param identifier source ID or URL slug
     * @return list of system designs from the source
     */
    @GetMapping("/source/{identifier}")
    public ResponseEntity<List<SystemDesignSummaryDto>> getSystemDesignsBySource(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/source/{} - Retrieving system designs by source", identifier);
        List<SystemDesignSummaryDto> systemDesigns;
        
        if (identifier.startsWith("SRC-")) {
            systemDesigns = systemDesignService.getSystemDesignsBySource(identifier);
        } else {
            systemDesigns = systemDesignService.getSystemDesignsBySourceSlug(identifier);
        }
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Get system designs by label/tag
     * 
     * @param identifier label ID or URL slug
     * @return list of system designs with the specified label
     */
    @GetMapping("/label/{identifier}")
    public ResponseEntity<List<SystemDesignSummaryDto>> getSystemDesignsByLabel(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/label/{} - Retrieving system designs by label", identifier);
        List<SystemDesignSummaryDto> systemDesigns;
        
        if (identifier.startsWith("LBL-")) {
            systemDesigns = systemDesignService.getSystemDesignsByLabel(identifier);
        } else {
            systemDesigns = systemDesignService.getSystemDesignsByLabelSlug(identifier);
        }
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Advanced search with multiple filters
     * 
     * @param sourceIds comma-separated source IDs
     * @param labelIds comma-separated label IDs
     * @param searchTerm search term for title/description
     * @return list of system designs matching the filters
     */
    @GetMapping("/filter")
    public ResponseEntity<List<SystemDesignSummaryDto>> filterSystemDesigns(
            @RequestParam(required = false) List<String> sourceIds,
            @RequestParam(required = false) List<String> labelIds,
            @RequestParam(required = false) String searchTerm) {
        log.info("GET /v1/system-designs/filter - Filtering system designs with sources: {}, labels: {}, term: {}", 
                sourceIds, labelIds, searchTerm);
        
        List<SystemDesignSummaryDto> systemDesigns = systemDesignService.searchWithFilters(sourceIds, labelIds, searchTerm);
        return ResponseEntity.ok(systemDesigns);
    }

    /**
     * Get system design statistics
     * 
     * @return system design statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<SystemDesignStatisticsDto> getSystemDesignStatistics() {
        log.info("GET /v1/system-designs/statistics - Retrieving system design statistics");
        SystemDesignStatisticsDto statistics = systemDesignService.getSystemDesignStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Check if system design title is available within a source
     * 
     * @param title system design title to check
     * @param sourceId source ID (optional)
     * @return availability status
     */
    @GetMapping("/check-title")
    public ResponseEntity<Boolean> checkTitleAvailability(
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String sourceId) {
        log.info("GET /v1/system-designs/check-title - Checking title availability: {} for source: {}", title, sourceId);
        boolean isAvailable = systemDesignService.checkTitleAvailability(title, sourceId);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Get the markdown content of a system design
     * 
     * @param identifier system design URL slug or ID
     * @return markdown content of the system design
     */
    @GetMapping(value = "/{identifier}/content", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSystemDesignContent(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/{}/content - Retrieving system design content", identifier);
        
        try {
            String content;
            if (identifier.startsWith("SYS-")) {
                content = systemDesignService.getSystemDesignContent(identifier);
            } else {
                content = systemDesignService.getSystemDesignContentByUrlSlug(identifier);
            }
            return ResponseEntity.ok(content);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error retrieving system design content for identifier: {}", identifier, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all labels for a specific system design
     * 
     * @param identifier system design URL slug or ID
     * @return list of system design-label relationships
     */
    @GetMapping("/{identifier}/labels")
    public ResponseEntity<List<SystemDesignLabelReferenceDto>> getSystemDesignLabels(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/{}/labels - Retrieving labels for system design", identifier);
        
        try {
            // First resolve the system design ID if needed
            String systemDesignId = identifier;
            if (!identifier.startsWith("SYS-")) {
                // Get by URL slug to find the ID
                Optional<SystemDesignDto> systemDesign = systemDesignService.getSystemDesignByUrlSlug(identifier);
                if (systemDesign.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                systemDesignId = systemDesign.get().getId();
            }
            
            List<SystemDesignLabelReferenceDto> labels = systemDesignService.getSystemDesignLabels(systemDesignId);
            return ResponseEntity.ok(labels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get primary labels for a specific system design
     * 
     * @param identifier system design URL slug or ID
     * @return list of primary system design-label relationships
     */
    @GetMapping("/{identifier}/labels/primary")
    public ResponseEntity<List<SystemDesignLabelReferenceDto>> getPrimarySystemDesignLabels(@PathVariable @NotBlank String identifier) {
        log.info("GET /v1/system-designs/{}/labels/primary - Retrieving primary labels for system design", identifier);
        
        try {
            // First resolve the system design ID if needed
            String systemDesignId = identifier;
            if (!identifier.startsWith("SYS-")) {
                // Get by URL slug to find the ID
                Optional<SystemDesignDto> systemDesign = systemDesignService.getSystemDesignByUrlSlug(identifier);
                if (systemDesign.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                systemDesignId = systemDesign.get().getId();
            }
            
            List<SystemDesignLabelReferenceDto> primaryLabels = systemDesignService.getPrimarySystemDesignLabels(systemDesignId);
            return ResponseEntity.ok(primaryLabels);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add a label to a system design
     * 
     * @param createDto system design-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping("/labels")
    public ResponseEntity<Void> addLabelToSystemDesign(@Valid @RequestBody SystemDesignLabelCreateDto createDto) {
        log.info("POST /v1/system-designs/labels - Adding label to system design");
        systemDesignService.addLabelToSystemDesign(createDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Add multiple labels to system designs
     * 
     * @param bulkCreateDto bulk system design-label relationship data
     * @return 201 Created if successful
     */
    @PostMapping("/labels/bulk")
    public ResponseEntity<Void> addLabelsToSystemDesigns(@Valid @RequestBody SystemDesignLabelBulkCreateDto bulkCreateDto) {
        log.info("POST /v1/system-designs/labels/bulk - Adding labels to system designs in bulk");
        systemDesignService.addLabelsToSystemDesign(bulkCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Remove a label from a system design
     * 
     * @param systemDesignId system design ID
     * @param labelId label ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/labels")
    public ResponseEntity<Void> removeLabelFromSystemDesign(
            @RequestParam @ValidId String systemDesignId,
            @RequestParam @ValidId String labelId) {
        log.info("DELETE /v1/system-designs/labels - Removing label {} from system design {}", labelId, systemDesignId);
        
        try {
            systemDesignService.removeLabelFromSystemDesign(systemDesignId, labelId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
