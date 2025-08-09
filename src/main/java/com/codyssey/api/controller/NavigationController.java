package com.codyssey.api.controller;

import com.codyssey.api.dto.navigation.CategoryNavigationDto;
import com.codyssey.api.dto.navigation.LabelNavigationDto;
import com.codyssey.api.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Navigation operations
 * Provides hierarchical navigation through label categories and labels
 */
@RestController
@RequestMapping("/v1/navigation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Navigation", description = "Hierarchical navigation API for labels and categories")
public class NavigationController {

    private final NavigationService navigationService;

    @Operation(summary = "Get all label categories", description = "Retrieves all active label categories for navigation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label categories retrieved successfully")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryNavigationDto>> getAllLabelCategories() {
        log.info("Request to get all label categories for navigation");
        List<CategoryNavigationDto> categories = navigationService.getAllLabelCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get labels by category", description = "Retrieves all labels within a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/labels")
    public ResponseEntity<List<LabelNavigationDto>> getLabelsByCategory(@PathVariable String categorySlug) {
        log.info("Request to get labels by category slug: {}", categorySlug);
        List<LabelNavigationDto> labels = navigationService.getLabelsByCategorySlug(categorySlug);
        return ResponseEntity.ok(labels);
    }

    @Operation(summary = "Get root labels by category", description = "Retrieves root labels (labels without parent) within a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Root labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/labels/root")
    public ResponseEntity<List<LabelNavigationDto>> getRootLabelsByCategory(@PathVariable String categorySlug) {
        log.info("Request to get root labels by category slug: {}", categorySlug);
        List<LabelNavigationDto> rootLabels = navigationService.getRootLabelsByCategorySlug(categorySlug);
        return ResponseEntity.ok(rootLabels);
    }

    @Operation(summary = "Get label navigation details", description = "Retrieves detailed navigation information for a specific label including children, MCQ counts, and navigation links")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label navigation details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}")
    public ResponseEntity<LabelNavigationDto> getLabelNavigation(@PathVariable String labelSlug) {
        log.info("Request to get label navigation for slug: {}", labelSlug);
        LabelNavigationDto labelNavigation = navigationService.getLabelNavigationBySlug(labelSlug);
        return ResponseEntity.ok(labelNavigation);
    }

    @Operation(summary = "Get child labels", description = "Retrieves child labels for a specific parent label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Child labels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Parent label not found")
    })
    @GetMapping("/labels/{parentLabelSlug}/children")
    public ResponseEntity<List<LabelNavigationDto>> getChildLabels(@PathVariable String parentLabelSlug) {
        log.info("Request to get child labels for parent slug: {}", parentLabelSlug);
        List<LabelNavigationDto> childLabels = navigationService.getChildLabelsBySlug(parentLabelSlug);
        return ResponseEntity.ok(childLabels);
    }
}
