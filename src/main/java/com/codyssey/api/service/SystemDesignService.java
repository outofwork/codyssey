package com.codyssey.api.service;

import com.codyssey.api.dto.systemdesign.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for SystemDesign operations
 * <p>
 * Defines the contract for system design-related business logic operations.
 */
public interface SystemDesignService {

    /**
     * Create a new system design
     *
     * @param createDto system design creation data
     * @return created system design DTO
     */
    SystemDesignDto createSystemDesign(SystemDesignCreateDto createDto);

    /**
     * Get all system designs (non-deleted)
     *
     * @return list of all system designs
     */
    List<SystemDesignSummaryDto> getAllSystemDesigns();

    /**
     * Get system design by ID
     *
     * @param id system design ID
     * @return system design DTO if found
     */
    Optional<SystemDesignDto> getSystemDesignById(String id);

    /**
     * Get system design by URL slug
     *
     * @param urlSlug system design URL slug
     * @return system design DTO if found
     */
    Optional<SystemDesignDto> getSystemDesignByUrlSlug(String urlSlug);

    /**
     * Update system design
     *
     * @param id system design ID
     * @param updateDto updated system design data
     * @return updated system design DTO
     */
    SystemDesignDto updateSystemDesign(String id, SystemDesignUpdateDto updateDto);

    /**
     * Update system design by URL slug
     *
     * @param urlSlug system design URL slug
     * @param updateDto updated system design data
     * @return updated system design DTO
     */
    SystemDesignDto updateSystemDesignByUrlSlug(String urlSlug, SystemDesignUpdateDto updateDto);

    /**
     * Soft delete system design
     *
     * @param id system design ID
     */
    void deleteSystemDesign(String id);

    /**
     * Soft delete system design by URL slug
     *
     * @param urlSlug system design URL slug
     */
    void deleteSystemDesignByUrlSlug(String urlSlug);

    /**
     * Get system designs with pagination
     *
     * @param pageable pagination information
     * @return paginated system designs
     */
    Page<SystemDesignSummaryDto> getSystemDesignsWithPagination(Pageable pageable);

    /**
     * Search system designs by title or description
     *
     * @param searchTerm search term
     * @param pageable pagination information
     * @return paginated search results
     */
    Page<SystemDesignSummaryDto> searchSystemDesigns(String searchTerm, Pageable pageable);

    /**
     * Get system designs by source
     *
     * @param sourceId source ID
     * @return list of system designs from the source
     */
    List<SystemDesignSummaryDto> getSystemDesignsBySource(String sourceId);

    /**
     * Get system designs by label/tag
     *
     * @param labelId label ID
     * @return list of system designs tagged with the specified label
     */
    List<SystemDesignSummaryDto> getSystemDesignsByLabel(String labelId);

    /**
     * Get system designs by label/tag URL slug
     *
     * @param labelSlug label URL slug
     * @return list of system designs tagged with the specified label
     */
    List<SystemDesignSummaryDto> getSystemDesignsByLabelSlug(String labelSlug);

    /**
     * Get system designs by source URL slug
     *
     * @param sourceSlug source URL slug
     * @return list of system designs from the specified source
     */
    List<SystemDesignSummaryDto> getSystemDesignsBySourceSlug(String sourceSlug);

    /**
     * Advanced search with multiple filters
     *
     * @param sourceIds list of source IDs to filter by
     * @param labelIds list of label IDs to filter by
     * @param searchTerm search term for title/description
     * @return list of system designs matching the filters
     */
    List<SystemDesignSummaryDto> searchWithFilters(List<String> sourceIds,
                                                   List<String> labelIds,
                                                   String searchTerm);

    /**
     * Get comprehensive system design statistics with detailed breakdowns
     *
     * @return comprehensive system design statistics
     */
    SystemDesignStatisticsDto getSystemDesignStatistics();

    /**
     * Check if a system design title is available within a source
     *
     * @param title system design title
     * @param sourceId source ID
     * @return true if title is available
     */
    boolean checkTitleAvailability(String title, String sourceId);

    /**
     * Add a label to a system design
     *
     * @param createDto system design-label relationship data
     */
    void addLabelToSystemDesign(SystemDesignLabelCreateDto createDto);

    /**
     * Add multiple labels to a system design
     *
     * @param bulkCreateDto bulk system design-label relationship data
     */
    void addLabelsToSystemDesign(SystemDesignLabelBulkCreateDto bulkCreateDto);

    /**
     * Remove a label from a system design
     *
     * @param systemDesignId system design ID
     * @param labelId label ID
     */
    void removeLabelFromSystemDesign(String systemDesignId, String labelId);

    /**
     * Get the markdown content of a system design
     *
     * @param id system design ID
     * @return markdown content of the system design
     * @throws Exception if file cannot be read
     */
    String getSystemDesignContent(String id) throws Exception;

    /**
     * Get the markdown content of a system design by URL slug
     *
     * @param urlSlug system design URL slug
     * @return markdown content of the system design
     * @throws Exception if file cannot be read
     */
    String getSystemDesignContentByUrlSlug(String urlSlug) throws Exception;

    /**
     * Get all labels for a specific system design
     *
     * @param systemDesignId system design ID
     * @return list of system design-label relationships
     */
    List<SystemDesignLabelReferenceDto> getSystemDesignLabels(String systemDesignId);

    /**
     * Get primary labels for a specific system design
     *
     * @param systemDesignId system design ID
     * @return list of primary system design-label relationships
     */
    List<SystemDesignLabelReferenceDto> getPrimarySystemDesignLabels(String systemDesignId);
}
