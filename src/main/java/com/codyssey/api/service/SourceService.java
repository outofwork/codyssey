package com.codyssey.api.service;

import com.codyssey.api.dto.source.SourceCreateDto;
import com.codyssey.api.dto.source.SourceDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.dto.source.SourceUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Source operations
 * <p>
 * Defines the contract for source-related business logic operations.
 */
public interface SourceService {

    /**
     * Create a new source
     *
     * @param createDto source creation data
     * @return created source DTO
     */
    SourceDto createSource(SourceCreateDto createDto);

    /**
     * Get all sources (non-deleted)
     *
     * @return list of all sources
     */
    List<SourceDto> getAllSources();

    /**
     * Get all active sources (non-deleted and active)
     *
     * @return list of active sources
     */
    List<SourceDto> getActiveSources();

    /**
     * Get source summary list
     *
     * @return list of source summaries
     */
    List<SourceSummaryDto> getSourceSummaries();

    /**
     * Get source by ID
     *
     * @param id source ID
     * @return source DTO if found
     */
    Optional<SourceDto> getSourceById(String id);

    /**
     * Get source by code (case insensitive)
     *
     * @param code source code
     * @return source DTO if found
     */
    Optional<SourceDto> getSourceByCode(String code);

    /**
     * Get source by URL slug
     *
     * @param urlSlug URL slug
     * @return source DTO if found
     */
    Optional<SourceDto> getSourceByUrlSlug(String urlSlug);

    /**
     * Update source
     *
     * @param id        source ID
     * @param updateDto updated source data
     * @return updated source DTO
     */
    SourceDto updateSource(String id, SourceUpdateDto updateDto);

    /**
     * Update source by URL slug
     *
     * @param urlSlug   source URL slug
     * @param updateDto updated source data
     * @return updated source DTO
     */
    SourceDto updateSourceByUrlSlug(String urlSlug, SourceUpdateDto updateDto);

    /**
     * Soft delete source
     *
     * @param id source ID
     */
    void deleteSource(String id);

    /**
     * Soft delete source by URL slug
     *
     * @param urlSlug source URL slug
     */
    void deleteSourceByUrlSlug(String urlSlug);

    /**
     * Search sources by name
     *
     * @param searchTerm search term
     * @return list of matching sources
     */
    List<SourceDto> searchSources(String searchTerm);

    /**
     * Check if source code exists
     *
     * @param code source code
     * @return true if code exists
     */
    boolean existsByCode(String code);

    /**
     * Toggle source active status
     *
     * @param id source ID
     * @return updated source DTO
     */
    SourceDto toggleActiveStatus(String id);

    /**
     * Get questions count for a source
     *
     * @param sourceId source ID
     * @return count of questions
     */
    Long getQuestionsCountBySource(String sourceId);
}