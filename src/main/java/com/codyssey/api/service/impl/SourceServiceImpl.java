package com.codyssey.api.service.impl;

import com.codyssey.api.dto.source.SourceCreateDto;
import com.codyssey.api.dto.source.SourceDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.dto.source.SourceUpdateDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.Source;
import com.codyssey.api.repository.SourceRepository;
import com.codyssey.api.service.SourceService;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of SourceService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    @Override
    public SourceDto createSource(SourceCreateDto createDto) {
        log.info("Creating new source with code: {}", createDto.getCode());
        
        // Check if code already exists
        if (sourceRepository.existsByCodeIgnoreCase(createDto.getCode())) {
            throw new DuplicateResourceException("Source with code '" + createDto.getCode() + "' already exists");
        }

        Source source = new Source();
        source.setCode(createDto.getCode().toUpperCase());
        source.setName(createDto.getName());
        source.setBaseUrl(createDto.getBaseUrl());
        source.setDescription(createDto.getDescription());
        source.setIsActive(createDto.getIsActive() != null ? createDto.getIsActive() : true);
        source.setColorCode(createDto.getColorCode());
        
        // Generate unique URL slug
        String baseSlug = UrlSlugGenerator.generateSourceSlug(createDto.getName());
        String uniqueSlug = generateUniqueSlug(baseSlug);
        source.setUrlSlug(uniqueSlug);

        Source savedSource = sourceRepository.save(source);
        log.info("Successfully created source with ID: {}", savedSource.getId());
        
        return mapToDto(savedSource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceDto> getAllSources() {
        log.info("Retrieving all sources");
        return sourceRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceDto> getActiveSources() {
        log.info("Retrieving active sources");
        return sourceRepository.findActiveSource()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceSummaryDto> getSourceSummaries() {
        log.info("Retrieving source summaries");
        return sourceRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToSummaryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDto> getSourceById(String id) {
        log.info("Retrieving source by ID: {}", id);
        return sourceRepository.findByIdAndNotDeleted(id)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDto> getSourceByCode(String code) {
        log.info("Retrieving source by code: {}", code);
        return sourceRepository.findByCodeIgnoreCase(code)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDto> getSourceByUrlSlug(String urlSlug) {
        log.info("Retrieving source by URL slug: {}", urlSlug);
        return sourceRepository.findByUrlSlug(urlSlug)
                .map(this::mapToDto);
    }

    @Override
    public SourceDto updateSource(String id, SourceUpdateDto updateDto) {
        log.info("Updating source with ID: {}", id);
        
        Source source = sourceRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + id));

        if (updateDto.getName() != null) {
            source.setName(updateDto.getName());
        }
        if (updateDto.getBaseUrl() != null) {
            source.setBaseUrl(updateDto.getBaseUrl());
        }
        if (updateDto.getDescription() != null) {
            source.setDescription(updateDto.getDescription());
        }
        if (updateDto.getIsActive() != null) {
            source.setIsActive(updateDto.getIsActive());
        }
        if (updateDto.getColorCode() != null) {
            source.setColorCode(updateDto.getColorCode());
        }

        Source updatedSource = sourceRepository.save(source);
        log.info("Successfully updated source with ID: {}", updatedSource.getId());
        
        return mapToDto(updatedSource);
    }

    @Override
    public void deleteSource(String id) {
        log.info("Deleting source with ID: {}", id);
        
        Source source = sourceRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + id));

        // Check if source has questions
        Long questionsCount = sourceRepository.countQuestionsBySourceId(id);
        if (questionsCount > 0) {
            throw new IllegalStateException("Cannot delete source with " + questionsCount + " associated questions");
        }

        source.setDeleted(true);
        sourceRepository.save(source);
        log.info("Successfully deleted source with ID: {}", id);
    }

    @Override
    public SourceDto updateSourceByUrlSlug(String urlSlug, SourceUpdateDto updateDto) {
        log.info("Updating source with URL slug: {}", urlSlug);
        
        Source source = sourceRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with URL slug: " + urlSlug));

        if (updateDto.getName() != null) {
            source.setName(updateDto.getName());
            // Update URL slug if name changes
            String baseSlug = UrlSlugGenerator.generateSourceSlug(updateDto.getName());
            String uniqueSlug = generateUniqueSlug(baseSlug, source.getId());
            source.setUrlSlug(uniqueSlug);
        }
        if (updateDto.getBaseUrl() != null) {
            source.setBaseUrl(updateDto.getBaseUrl());
        }
        if (updateDto.getDescription() != null) {
            source.setDescription(updateDto.getDescription());
        }
        if (updateDto.getIsActive() != null) {
            source.setIsActive(updateDto.getIsActive());
        }
        if (updateDto.getColorCode() != null) {
            source.setColorCode(updateDto.getColorCode());
        }

        Source updatedSource = sourceRepository.save(source);
        log.info("Successfully updated source with URL slug: {}", urlSlug);
        
        return mapToDto(updatedSource);
    }

    @Override
    public void deleteSourceByUrlSlug(String urlSlug) {
        log.info("Deleting source with URL slug: {}", urlSlug);
        
        Source source = sourceRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with URL slug: " + urlSlug));

        // Check if source has questions
        Long questionsCount = sourceRepository.countQuestionsBySourceId(source.getId());
        if (questionsCount > 0) {
            throw new IllegalStateException("Cannot delete source with " + questionsCount + " associated questions");
        }

        source.setDeleted(true);
        sourceRepository.save(source);
        log.info("Successfully deleted source with URL slug: {}", urlSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceDto> searchSources(String searchTerm) {
        log.info("Searching sources with term: {}", searchTerm);
        return sourceRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        log.info("Checking if source code exists: {}", code);
        return sourceRepository.existsByCodeIgnoreCase(code);
    }

    @Override
    public SourceDto toggleActiveStatus(String id) {
        log.info("Toggling active status for source with ID: {}", id);
        
        Source source = sourceRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + id));

        source.setIsActive(!source.getIsActive());
        Source updatedSource = sourceRepository.save(source);
        log.info("Successfully toggled active status for source with ID: {}", id);
        
        return mapToDto(updatedSource);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getQuestionsCountBySource(String sourceId) {
        log.info("Getting questions count for source: {}", sourceId);
        return sourceRepository.countQuestionsBySourceId(sourceId);
    }

    private SourceDto mapToDto(Source source) {
        SourceDto dto = new SourceDto();
        dto.setId(source.getId());
        dto.setCode(source.getCode());
        dto.setName(source.getName());
        dto.setBaseUrl(source.getBaseUrl());
        dto.setDescription(source.getDescription());
        dto.setIsActive(source.getIsActive());
        dto.setColorCode(source.getColorCode());
        dto.setUrlSlug(source.getUrlSlug());
        dto.setUri("/api/v1/sources/" + source.getUrlSlug());
        dto.setCreatedAt(source.getCreatedAt());
        dto.setUpdatedAt(source.getUpdatedAt());
        dto.setVersion(source.getVersion());
        dto.setQuestionsCount(sourceRepository.countQuestionsBySourceId(source.getId()));
        return dto;
    }

    private SourceSummaryDto mapToSummaryDto(Source source) {
        SourceSummaryDto dto = new SourceSummaryDto();
        dto.setId(source.getId());
        dto.setCode(source.getCode());
        dto.setName(source.getName());
        dto.setBaseUrl(source.getBaseUrl());
        dto.setColorCode(source.getColorCode());
        dto.setUrlSlug(source.getUrlSlug());
        dto.setUri("/api/v1/sources/" + source.getUrlSlug());
        return dto;
    }

    /**
     * Generate unique URL slug for new entities
     */
    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }

    /**
     * Generate unique URL slug, excluding a specific entity ID
     */
    private String generateUniqueSlug(String baseSlug, String excludeId) {
        String slug = baseSlug;
        int counter = 1;
        
        while (excludeId != null ? 
               sourceRepository.existsByUrlSlugAndIdNot(slug, excludeId) : 
               sourceRepository.existsByUrlSlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
}