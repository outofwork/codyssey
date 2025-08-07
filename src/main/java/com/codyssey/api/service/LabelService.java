package com.codyssey.api.service;

import com.codyssey.api.dto.label.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Label operations
 * <p>
 * Defines the contract for label-related business logic operations.
 */
public interface LabelService {

    /**
     * Create a new label
     *
     * @param createDto label creation data
     * @return created label DTO
     */
    LabelDto createLabel(LabelCreateDto createDto);

    /**
     * Create multiple labels in bulk maintaining parent-child relationships
     *
     * @param bulkCreateDto bulk label creation data
     * @return list of created label DTOs
     */
    List<LabelDto> createLabelsBulk(LabelBulkCreateDto bulkCreateDto);

    /**
     * Get all labels (non-deleted)
     *
     * @return list of all labels
     */
    List<LabelDto> getAllLabels();

    /**
     * Get all labels with parent-child hierarchy
     *
     * @return list of labels in hierarchy format
     */
    List<LabelHierarchyDto> getLabelsHierarchy();

    /**
     * Get label by ID
     *
     * @param id label ID
     * @return label DTO if found
     */
    Optional<LabelDto> getLabelById(String id);

    /**
     * Get label by URL slug
     *
     * @param urlSlug URL slug
     * @return label DTO if found
     */
    Optional<LabelDto> getLabelByUrlSlug(String urlSlug);

    /**
     * Update label
     *
     * @param id        label ID
     * @param updateDto updated label data
     * @return updated label DTO
     */
    LabelDto updateLabel(String id, LabelUpdateDto updateDto);

    /**
     * Update label by URL slug
     *
     * @param urlSlug   label URL slug
     * @param updateDto updated label data
     * @return updated label DTO
     */
    LabelDto updateLabelByUrlSlug(String urlSlug, LabelUpdateDto updateDto);

    /**
     * Soft delete label
     *
     * @param id label ID
     */
    void deleteLabel(String id);

    /**
     * Soft delete label by URL slug
     *
     * @param urlSlug label URL slug
     */
    void deleteLabelByUrlSlug(String urlSlug);

    /**
     * Get labels by category code
     *
     * @param categoryCode the category code
     * @return list of labels in the category
     */
    List<LabelDto> getLabelsByCategory(String categoryCode);

    /**
     * Get only active labels by category code
     *
     * @param categoryCode the category code
     * @return list of active labels in the category
     */
    List<LabelDto> getActiveLabelsByCategory(String categoryCode);

    /**
     * Get root (parent) labels by category code
     *
     * @param categoryCode the category code
     * @return list of root labels in the category
     */
    List<LabelDto> getRootLabelsByCategory(String categoryCode);

    /**
     * Get child labels for a given label ID
     *
     * @param labelId the parent label ID
     * @return list of child labels
     */
    List<LabelDto> getChildLabels(String labelId);

    /**
     * Search labels by name
     *
     * @param searchTerm the search term
     * @return list of matching labels
     */
    List<LabelDto> searchLabels(String searchTerm);

    /**
     * Get list of available categories (that have labels)
     *
     * @return list of categories
     */
    List<String> getAvailableCategories();

    /**
     * Check if a label name is available within a category and parent context
     *
     * @param name         the name to check
     * @param categoryCode the category code
     * @param parentId     the parent ID (optional)
     * @return true if name is available, false otherwise
     */
    boolean checkNameAvailability(String name, String categoryCode, String parentId);

    /**
     * Toggle the active status of a label
     *
     * @param id label ID
     * @return updated label DTO
     */
    LabelDto toggleActiveStatus(String id);
}