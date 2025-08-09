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

    @Operation(summary = "Create a new MCQ question", description = "Creates a new multiple choice question (simple)")
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

    @Operation(summary = "Create MCQ question with multiple categories and labels", 
               description = "Creates a new MCQ question with multiple categories and labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MCQ question created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQQuestionDto> createMCQQuestionBulk(@Valid @RequestBody MCQBulkCreateDto createDto) {
        log.info("Request to create MCQ question with multiple categories and labels");
        MCQQuestionDto createdQuestion = mcqQuestionService.createMCQQuestionBulk(createDto);
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

    // Category-based endpoints

    @Operation(summary = "Get MCQ questions by category", description = "Retrieves MCQ questions for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/questions")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByCategory(@PathVariable String categorySlug) {
        log.info("Request to get MCQ questions by category slug: {}", categorySlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByCategorySlug(categorySlug);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by category with pagination", description = "Retrieves MCQ questions for a specific category with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/questions/paged")
    public ResponseEntity<Page<MCQQuestionSummaryDto>> getMCQQuestionsByCategoryPaged(
            @PathVariable String categorySlug,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Request to get MCQ questions by category slug with pagination: {}", categorySlug);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByCategorySlug(categorySlug, pageable);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get random MCQ questions by category", description = "Retrieves random MCQ questions for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/questions/random")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getRandomMCQQuestionsByCategory(
            @PathVariable String categorySlug,
            @Parameter(description = "Number of random questions to retrieve")
            @RequestParam(defaultValue = "10") int count) {
        
        log.info("Request to get {} random MCQ questions by category slug: {}", count, categorySlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getRandomMCQQuestionsByCategorySlug(categorySlug, count);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ question count by category", description = "Returns the count of MCQ questions in a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categorySlug}/count")
    public ResponseEntity<Long> getMCQQuestionCountByCategory(@PathVariable String categorySlug) {
        log.info("Request to get MCQ question count by category slug: {}", categorySlug);
        long count = mcqQuestionService.getMCQQuestionCountByCategorySlug(categorySlug);
        return ResponseEntity.ok(count);
    }

    // Enhanced hierarchical access endpoints

    @Operation(summary = "Get MCQ questions with hierarchical access", 
               description = "Retrieves MCQ questions accessible through label hierarchy (includes category fallback)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/hierarchical")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsWithHierarchicalAccess(@PathVariable String labelSlug) {
        log.info("Request to get MCQ questions with hierarchical access for label slug: {}", labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsWithHierarchicalAccess(labelSlug);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get random MCQ questions with hierarchical access", 
               description = "Retrieves random MCQ questions accessible through label hierarchy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/questions/hierarchical/random")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getRandomMCQQuestionsWithHierarchicalAccess(
            @PathVariable String labelSlug,
            @Parameter(description = "Number of random questions to retrieve")
            @RequestParam(defaultValue = "10") int count) {
        
        log.info("Request to get {} random MCQ questions with hierarchical access for label slug: {}", count, labelSlug);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getRandomMCQQuestionsWithHierarchicalAccess(labelSlug, count);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ question count with hierarchical access", 
               description = "Returns the count of MCQ questions accessible through label hierarchy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping("/labels/{labelSlug}/count/hierarchical")
    public ResponseEntity<Long> getMCQQuestionCountWithHierarchicalAccess(@PathVariable String labelSlug) {
        log.info("Request to get MCQ question count with hierarchical access for label slug: {}", labelSlug);
        long count = mcqQuestionService.getMCQQuestionCountWithHierarchicalAccess(labelSlug);
        return ResponseEntity.ok(count);
    }

    // MCQ Category management endpoints

    @Operation(summary = "Add category to MCQ question", description = "Associates a category with an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MCQ question or category not found"),
            @ApiResponse(responseCode = "409", description = "Association already exists")
    })
    @PostMapping("/questions/{mcqQuestionId}/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQCategoryReferenceDto> addCategoryToMCQQuestion(
            @PathVariable String mcqQuestionId,
            @Valid @RequestBody MCQCategoryCreateDto createDto) {
        
        log.info("Request to add category to MCQ question: {}", mcqQuestionId);
        createDto.setMcqQuestionId(mcqQuestionId); // Ensure consistency
        MCQCategoryReferenceDto categoryReference = mcqQuestionService.addCategoryToMCQQuestion(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryReference);
    }

    @Operation(summary = "Remove category from MCQ question", description = "Removes a category association from an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @DeleteMapping("/questions/{mcqQuestionId}/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Void> removeCategoryFromMCQQuestion(@PathVariable String mcqQuestionId, @PathVariable String categoryId) {
        log.info("Request to remove category {} from MCQ question {}", categoryId, mcqQuestionId);
        mcqQuestionService.removeCategoryFromMCQQuestion(mcqQuestionId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get MCQ categories for question", description = "Retrieves all categories associated with an MCQ question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "MCQ question not found")
    })
    @GetMapping("/questions/{mcqQuestionId}/categories")
    public ResponseEntity<List<MCQCategoryReferenceDto>> getMCQCategoriesForQuestion(@PathVariable String mcqQuestionId) {
        log.info("Request to get categories for MCQ question: {}", mcqQuestionId);
        List<MCQCategoryReferenceDto> categories = mcqQuestionService.getMCQCategoriesForQuestion(mcqQuestionId);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Update MCQ category association", description = "Updates the relevance score and other properties of a category association")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @PutMapping("/category-associations/{mcqCategoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<MCQCategoryReferenceDto> updateMCQCategoryAssociation(
            @PathVariable String mcqCategoryId,
            @Valid @RequestBody MCQCategoryCreateDto updateDto) {
        
        log.info("Request to update MCQ category association: {}", mcqCategoryId);
        MCQCategoryReferenceDto updatedAssociation = mcqQuestionService.updateMCQCategoryAssociation(mcqCategoryId, updateDto);
        return ResponseEntity.ok(updatedAssociation);
    }

    // Multi-category and multi-label query endpoints

    @Operation(summary = "Get MCQ questions by multiple categories", description = "Retrieves MCQ questions that belong to any of the specified categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category IDs")
    })
    @PostMapping("/search/categories")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByMultipleCategories(
            @RequestBody @Valid List<String> categoryIds) {
        
        log.info("Request to get MCQ questions by multiple categories: {}", categoryIds);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByMultipleCategories(categoryIds);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by multiple labels", description = "Retrieves MCQ questions that are tagged with any of the specified labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid label IDs")
    })
    @PostMapping("/search/labels")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByMultipleLabels(
            @RequestBody @Valid List<String> labelIds) {
        
        log.info("Request to get MCQ questions by multiple labels: {}", labelIds);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByMultipleLabels(labelIds);
        return ResponseEntity.ok(mcqQuestions);
    }

    @Operation(summary = "Get MCQ questions by multiple categories and labels", 
               description = "Retrieves MCQ questions that match both specified categories and labels (intersection)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCQ questions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category or label IDs")
    })
    @PostMapping("/search/advanced")
    public ResponseEntity<List<MCQQuestionSummaryDto>> getMCQQuestionsByMultipleCategoriesAndLabels(
            @RequestParam @Valid List<String> categoryIds,
            @RequestParam @Valid List<String> labelIds) {
        
        log.info("Request to get MCQ questions by multiple categories {} and labels {}", categoryIds, labelIds);
        List<MCQQuestionSummaryDto> mcqQuestions = mcqQuestionService.getMCQQuestionsByMultipleCategoriesAndLabels(categoryIds, labelIds);
        return ResponseEntity.ok(mcqQuestions);
    }
}
