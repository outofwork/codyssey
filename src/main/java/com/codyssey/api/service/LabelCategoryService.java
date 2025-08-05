package com.codyssey.api.service;

import com.codyssey.api.dto.labelcategory.LabelCategoryCreateDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for LabelCategory operations
 * <p>
 * Defines the contract for label category-related business logic operations.
 */
public interface LabelCategoryService {

    /**
     * Create a new label category
     *
     * @param createDto label category creation data
     * @return created label category DTO
     */
    LabelCategoryDto createCategory(LabelCategoryCreateDto createDto);

    /**
     * Get all label categories (non-deleted)
     *
     * @return list of all label categories
     */
    List<LabelCategoryDto> getAllCategories();

    /**
     * Get all active label categories
     *
     * @return list of active label categories
     */
    List<LabelCategoryDto> getActiveCategories();

    /**
     * Get label category by ID
     *
     * @param id label category ID (15-character alphanumeric)
     * @return label category DTO if found
     */
    Optional<LabelCategoryDto> getCategoryById(String id);

    /**
     * Get label category by code
     *
     * @param code the category code
     * @return label category DTO if found
     */
    Optional<LabelCategoryDto> getCategoryByCode(String code);

    /**
     * Update label category
     *
     * @param id        label category ID (15-character alphanumeric)
     * @param updateDto updated label category data
     * @return updated label category DTO
     */
    LabelCategoryDto updateCategory(String id, LabelCategoryUpdateDto updateDto);

    /**
     * Soft delete label category
     *
     * @param id label category ID (15-character alphanumeric)
     */
    void deleteCategory(String id);

    /**
     * Search label categories by name
     *
     * @param name the name to search for
     * @return list of matching label categories
     */
    List<LabelCategoryDto> searchCategoriesByName(String name);

    /**
     * Check if code exists
     *
     * @param code the code to check
     * @return true if code exists, false otherwise
     */
    boolean existsByCode(String code);
}