package com.codyssey.api.service;

import com.codyssey.api.dto.mcq.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for MCQ Question operations
 */
public interface MCQQuestionService {

    /**
     * Create a new MCQ question (simple creation)
     */
    MCQQuestionDto createMCQQuestion(MCQQuestionCreateDto createDto);

    /**
     * Create a new MCQ question with multiple categories and labels
     */
    MCQQuestionDto createMCQQuestionBulk(MCQBulkCreateDto createDto);

    /**
     * Get MCQ question by ID
     */
    MCQQuestionDto getMCQQuestionById(String id);

    /**
     * Get MCQ question by URL slug
     */
    MCQQuestionDto getMCQQuestionBySlug(String urlSlug);

    /**
     * Update MCQ question
     */
    MCQQuestionDto updateMCQQuestion(String id, MCQQuestionUpdateDto updateDto);

    /**
     * Delete MCQ question
     */
    void deleteMCQQuestion(String id);

    /**
     * Get all MCQ questions with pagination
     */
    Page<MCQQuestionSummaryDto> getAllMCQQuestions(Pageable pageable);

    /**
     * Get all MCQ questions in full format (without pagination)
     */
    List<MCQQuestionDto> getAllMCQQuestionsWithFullDetails();

    /**
     * Get all MCQ questions in simplified format (without pagination)
     */
    List<SimplifiedMCQQuestionDto> getAllMCQQuestionsSimplified();

    /**
     * Get MCQ questions by label (using label ID)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByLabel(String labelId);

    /**
     * Get MCQ questions by label (using label slug)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByLabelSlug(String labelSlug);

    /**
     * Get MCQ questions by label with full details (using label slug)
     */
    List<MCQQuestionDto> getMCQQuestionsByLabelSlugWithFullDetails(String labelSlug);

    /**
     * Get MCQ questions by label in simplified format (using label slug)
     */
    List<SimplifiedMCQQuestionDto> getMCQQuestionsByLabelSlugSimplified(String labelSlug);

    /**
     * Get MCQ questions by label with pagination (using label ID)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByLabel(String labelId, Pageable pageable);

    /**
     * Get MCQ questions by label with pagination (using label slug)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelSlug(String labelSlug, Pageable pageable);

    /**
     * Get MCQ questions by label hierarchy (including children) (using label ID)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchy(String labelId);

    /**
     * Get MCQ questions by label hierarchy (including children) (using label slug)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchySlug(String labelSlug);

    /**
     * Get MCQ questions by label hierarchy with pagination (using label ID)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchy(String labelId, Pageable pageable);

    /**
     * Get MCQ questions by label hierarchy with pagination (using label slug)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchySlug(String labelSlug, Pageable pageable);

    /**
     * Get random MCQ questions by label (using label ID)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabel(String labelId, int count);

    /**
     * Get random MCQ questions by label (using label slug)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelSlug(String labelSlug, int count);

    /**
     * Get random MCQ questions by label hierarchy (using label ID)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelHierarchy(String labelId, int count);

    /**
     * Get random MCQ questions by label hierarchy (using label slug)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelHierarchySlug(String labelSlug, int count);

    /**
     * Add label to MCQ question
     */
    MCQLabelReferenceDto addLabelToMCQQuestion(MCQLabelCreateDto createDto);

    /**
     * Remove label from MCQ question
     */
    void removeLabelFromMCQQuestion(String mcqQuestionId, String labelId);

    /**
     * Get MCQ questions count by label (using label ID)
     */
    long getMCQQuestionCountByLabel(String labelId);

    /**
     * Get MCQ questions count by label (using label slug)
     */
    long getMCQQuestionCountByLabelSlug(String labelSlug);

    /**
     * Get MCQ questions count by label hierarchy (using label ID)
     */
    long getMCQQuestionCountByLabelHierarchy(String labelId);

    /**
     * Get MCQ questions count by label hierarchy (using label slug)
     */
    long getMCQQuestionCountByLabelHierarchySlug(String labelSlug);

    // Category-based methods

    /**
     * Get MCQ questions by category (using category ID)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByCategory(String categoryId);

    /**
     * Get MCQ questions by category (using category slug)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByCategorySlug(String categorySlug);

    /**
     * Get MCQ questions by category with full details (using category slug)
     */
    List<MCQQuestionDto> getMCQQuestionsByCategorySlugWithFullDetails(String categorySlug);

    /**
     * Get MCQ questions by category in simplified format (using category slug)
     */
    List<SimplifiedMCQQuestionDto> getMCQQuestionsByCategorySlugSimplified(String categorySlug);

    /**
     * Get MCQ questions by category with pagination (using category ID)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByCategory(String categoryId, Pageable pageable);

    /**
     * Get MCQ questions by category with pagination (using category slug)
     */
    Page<MCQQuestionSummaryDto> getMCQQuestionsByCategorySlug(String categorySlug, Pageable pageable);

    /**
     * Get random MCQ questions by category (using category ID)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByCategory(String categoryId, int count);

    /**
     * Get random MCQ questions by category (using category slug)
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsByCategorySlug(String categorySlug, int count);

    /**
     * Get MCQ questions count by category (using category ID)
     */
    long getMCQQuestionCountByCategory(String categoryId);

    /**
     * Get MCQ questions count by category (using category slug)
     */
    long getMCQQuestionCountByCategorySlug(String categorySlug);

    // Enhanced hierarchical methods

    /**
     * Get MCQ questions with full hierarchical access (includes category fallback)
     * This returns MCQs that should be accessible when querying a specific label
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsWithHierarchicalAccess(String labelSlug);

    /**
     * Get random MCQ questions with full hierarchical access
     */
    List<MCQQuestionSummaryDto> getRandomMCQQuestionsWithHierarchicalAccess(String labelSlug, int count);

    /**
     * Get MCQ questions count with full hierarchical access
     */
    long getMCQQuestionCountWithHierarchicalAccess(String labelSlug);

    // MCQ Category management methods

    /**
     * Add category to MCQ question
     */
    MCQCategoryReferenceDto addCategoryToMCQQuestion(MCQCategoryCreateDto createDto);

    /**
     * Remove category from MCQ question
     */
    void removeCategoryFromMCQQuestion(String mcqQuestionId, String categoryId);

    /**
     * Update MCQ category association
     */
    MCQCategoryReferenceDto updateMCQCategoryAssociation(String mcqCategoryId, MCQCategoryCreateDto updateDto);

    /**
     * Get MCQ categories for a question
     */
    List<MCQCategoryReferenceDto> getMCQCategoriesForQuestion(String mcqQuestionId);

    // Enhanced multi-category and multi-label queries

    /**
     * Find MCQ questions by multiple categories
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByMultipleCategories(List<String> categoryIds);

    /**
     * Find MCQ questions by multiple labels
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByMultipleLabels(List<String> labelIds);

    /**
     * Find MCQ questions by multiple categories and labels (intersection)
     */
    List<MCQQuestionSummaryDto> getMCQQuestionsByMultipleCategoriesAndLabels(List<String> categoryIds, List<String> labelIds);
}
