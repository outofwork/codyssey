package com.codyssey.api.service.impl;

import com.codyssey.api.dto.systemdesign.*;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.SystemDesignService;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of SystemDesignService
 * <p>
 * Provides system design management functionality including
 * creation, retrieval, updating, and deletion with comprehensive search capabilities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SystemDesignServiceImpl implements SystemDesignService {

    private final SystemDesignRepository systemDesignRepository;
    private final SystemDesignLabelRepository systemDesignLabelRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;

    @Override
    public SystemDesignDto createSystemDesign(SystemDesignCreateDto createDto) {
        log.info("Creating new system design with title: {}", createDto.getTitle());

        // Validate source if provided
        Source source = null;
        if (createDto.getSourceId() != null && !createDto.getSourceId().trim().isEmpty()) {
            source = sourceRepository.findByIdAndNotDeleted(createDto.getSourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + createDto.getSourceId()));
        }

        // Validate user if provided
        User createdByUser = null;
        if (createDto.getCreatedByUserId() != null && !createDto.getCreatedByUserId().trim().isEmpty()) {
            createdByUser = userRepository.findByIdAndNotDeleted(createDto.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createDto.getCreatedByUserId()));
        }

        // Check for duplicate title within source
        if (source != null) {
            if (systemDesignRepository.existsByTitleAndSourceId(createDto.getTitle(), source.getId())) {
                throw new DuplicateResourceException("System design with title '" + createDto.getTitle() + 
                        "' already exists for source: " + source.getName());
            }
        }

        // Create the system design entity
        SystemDesign systemDesign = new SystemDesign();
        systemDesign.setTitle(createDto.getTitle());
        systemDesign.setShortDescription(createDto.getShortDescription());
        systemDesign.setFilePath(createDto.getFilePath());
        systemDesign.setSource(source);
        systemDesign.setOriginalUrl(createDto.getOriginalUrl());
        systemDesign.setStatus(SystemDesign.SystemDesignStatus.valueOf(createDto.getStatus()));
        systemDesign.setCreatedByUser(createdByUser);
        
        // Generate unique URL slug
        String sourceCode = source != null ? source.getCode() : "";
        String baseSlug = UrlSlugGenerator.generateArticleSlug(createDto.getTitle(), sourceCode);
        String uniqueSlug = generateUniqueSlug(baseSlug);
        systemDesign.setUrlSlug(uniqueSlug);

        SystemDesign savedSystemDesign = systemDesignRepository.save(systemDesign);
        log.info("Successfully created system design with ID: {}", savedSystemDesign.getId());

        return convertToDto(savedSystemDesign);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> getAllSystemDesigns() {
        log.info("Retrieving all system designs");
        List<SystemDesign> systemDesigns = systemDesignRepository.findByDeletedFalse();
        return systemDesigns.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemDesignDto> getSystemDesignById(String id) {
        log.info("Retrieving system design with ID: {}", id);
        return systemDesignRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemDesignDto> getSystemDesignByUrlSlug(String urlSlug) {
        log.info("Retrieving system design with URL slug: {}", urlSlug);
        return systemDesignRepository.findByUrlSlug(urlSlug)
                .map(this::convertToDto);
    }

    @Override
    public SystemDesignDto updateSystemDesign(String id, SystemDesignUpdateDto updateDto) {
        log.info("Updating system design with ID: {}", id);
        
        SystemDesign existingSystemDesign = systemDesignRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with ID: " + id));

        updateSystemDesignFields(existingSystemDesign, updateDto);
        SystemDesign updatedSystemDesign = systemDesignRepository.save(existingSystemDesign);
        
        log.info("Successfully updated system design with ID: {}", id);
        return convertToDto(updatedSystemDesign);
    }

    @Override
    public SystemDesignDto updateSystemDesignByUrlSlug(String urlSlug, SystemDesignUpdateDto updateDto) {
        log.info("Updating system design with URL slug: {}", urlSlug);
        
        SystemDesign existingSystemDesign = systemDesignRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with URL slug: " + urlSlug));

        updateSystemDesignFields(existingSystemDesign, updateDto);
        SystemDesign updatedSystemDesign = systemDesignRepository.save(existingSystemDesign);
        
        log.info("Successfully updated system design with URL slug: {}", urlSlug);
        return convertToDto(updatedSystemDesign);
    }

    @Override
    public void deleteSystemDesign(String id) {
        log.info("Deleting system design with ID: {}", id);
        
        SystemDesign systemDesign = systemDesignRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with ID: " + id));
        
        systemDesign.setDeleted(true);
        systemDesignRepository.save(systemDesign);
        
        log.info("Successfully deleted system design with ID: {}", id);
    }

    @Override
    public void deleteSystemDesignByUrlSlug(String urlSlug) {
        log.info("Deleting system design with URL slug: {}", urlSlug);
        
        SystemDesign systemDesign = systemDesignRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with URL slug: " + urlSlug));
        
        systemDesign.setDeleted(true);
        systemDesignRepository.save(systemDesign);
        
        log.info("Successfully deleted system design with URL slug: {}", urlSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemDesignSummaryDto> getSystemDesignsWithPagination(Pageable pageable) {
        log.info("Retrieving system designs with pagination: page {}, size {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<SystemDesign> systemDesigns = systemDesignRepository.findSystemDesignsWithPagination(pageable);
        return systemDesigns.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemDesignSummaryDto> searchSystemDesigns(String searchTerm, Pageable pageable) {
        log.info("Searching system designs with term: '{}', page {}, size {}", 
                searchTerm, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<SystemDesign> systemDesigns = systemDesignRepository.searchByTitleOrDescription(searchTerm, pageable);
        return systemDesigns.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> getSystemDesignsBySource(String sourceId) {
        log.info("Retrieving system designs by source ID: {}", sourceId);
        
        List<SystemDesign> systemDesigns = systemDesignRepository.findBySourceId(sourceId);
        return systemDesigns.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> getSystemDesignsByLabel(String labelId) {
        log.info("Retrieving system designs by label ID: {}", labelId);
        
        List<SystemDesign> systemDesigns = systemDesignRepository.findByLabelId(labelId);
        return systemDesigns.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> getSystemDesignsByLabelSlug(String labelSlug) {
        log.info("Retrieving system designs by label slug: {}", labelSlug);
        
        // Find the label by slug first
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));
        
        return getSystemDesignsByLabel(label.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> getSystemDesignsBySourceSlug(String sourceSlug) {
        log.info("Retrieving system designs by source slug: {}", sourceSlug);
        
        // Find the source by slug first
        Source source = sourceRepository.findByUrlSlug(sourceSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with slug: " + sourceSlug));
        
        return getSystemDesignsBySource(source.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignSummaryDto> searchWithFilters(List<String> sourceIds, List<String> labelIds, String searchTerm) {
        log.info("Searching system designs with filters - sources: {}, labels: {}, term: '{}'", 
                sourceIds, labelIds, searchTerm);
        
        List<SystemDesign> systemDesigns = systemDesignRepository.findWithFilters(sourceIds, labelIds, searchTerm);
        return systemDesigns.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SystemDesignStatisticsDto getSystemDesignStatistics() {
        log.info("Generating system design statistics");
        
        SystemDesignStatisticsDto statistics = new SystemDesignStatisticsDto();
        
        // Overall statistics
        List<SystemDesign> allSystemDesigns = systemDesignRepository.findByDeletedFalse();
        statistics.setTotalSystemDesigns((long) allSystemDesigns.size());
        statistics.setActiveSystemDesigns(systemDesignRepository.countByStatus(SystemDesign.SystemDesignStatus.ACTIVE));
        statistics.setDraftSystemDesigns(systemDesignRepository.countByStatus(SystemDesign.SystemDesignStatus.DRAFT));
        statistics.setDeprecatedSystemDesigns(systemDesignRepository.countByStatus(SystemDesign.SystemDesignStatus.DEPRECATED));
        
        // Source statistics
        Set<Source> uniqueSources = allSystemDesigns.stream()
                .map(SystemDesign::getSource)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        statistics.setTotalSources((long) uniqueSources.size());
        statistics.setSystemDesignsWithSource(allSystemDesigns.stream()
                .filter(sd -> sd.getSource() != null).count());
        statistics.setSystemDesignsWithoutSource(allSystemDesigns.stream()
                .filter(sd -> sd.getSource() == null).count());
        
        // Create source statistics
        List<SystemDesignStatisticsDto.SourceStatistic> sourceStats = uniqueSources.stream()
                .map(source -> {
                    Long count = systemDesignRepository.countBySourceId(source.getId());
                    return new SystemDesignStatisticsDto.SourceStatistic(
                            source.getName(),
                            count,
                            "/api/v1/system-designs/source/" + source.getUrlSlug()
                    );
                })
                .sorted((a, b) -> Long.compare(b.getSystemDesignCount(), a.getSystemDesignCount()))
                .collect(Collectors.toList());
        statistics.setSystemDesignsBySource(sourceStats);
        
        // TODO: Add tag/label and category statistics similar to article service
        statistics.setTotalTags(0L);
        statistics.setTotalUniqueTagsUsed(0L);
        statistics.setMostUsedTag("N/A");
        statistics.setSystemDesignsByTag(new ArrayList<>());
        statistics.setTotalCategories(0L);
        statistics.setSystemDesignsByCategory(new ArrayList<>());
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkTitleAvailability(String title, String sourceId) {
        log.info("Checking title availability: '{}' for source: {}", title, sourceId);
        
        if (sourceId == null || sourceId.trim().isEmpty()) {
            return true; // If no source specified, consider it available
        }
        
        return !systemDesignRepository.existsByTitleAndSourceId(title, sourceId);
    }

    @Override
    public void addLabelToSystemDesign(SystemDesignLabelCreateDto createDto) {
        log.info("Adding label {} to system design {}", createDto.getLabelId(), createDto.getSystemDesignId());
        
        // Validate system design exists
        SystemDesign systemDesign = systemDesignRepository.findByIdAndNotDeleted(createDto.getSystemDesignId())
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with ID: " + createDto.getSystemDesignId()));
        
        // Validate label exists
        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + createDto.getLabelId()));
        
        // Check if association already exists
        if (systemDesignLabelRepository.existsBySystemDesignIdAndLabelId(createDto.getSystemDesignId(), createDto.getLabelId())) {
            throw new DuplicateResourceException("Label is already associated with this system design");
        }
        
        // Create the association
        SystemDesignLabel systemDesignLabel = new SystemDesignLabel();
        systemDesignLabel.setSystemDesign(systemDesign);
        systemDesignLabel.setLabel(label);
        systemDesignLabel.setIsPrimary(createDto.getIsPrimary() != null ? createDto.getIsPrimary() : false);
        
        // Set display order if not provided
        if (createDto.getDisplayOrder() == null) {
            Integer maxOrder = systemDesignLabelRepository.getMaxDisplayOrderBySystemDesignId(createDto.getSystemDesignId());
            systemDesignLabel.setDisplayOrder(maxOrder + 1);
        } else {
            systemDesignLabel.setDisplayOrder(createDto.getDisplayOrder());
        }
        
        systemDesignLabelRepository.save(systemDesignLabel);
        log.info("Successfully added label {} to system design {}", createDto.getLabelId(), createDto.getSystemDesignId());
    }

    @Override
    public void addLabelsToSystemDesign(SystemDesignLabelBulkCreateDto bulkCreateDto) {
        log.info("Adding {} labels to system designs in bulk", bulkCreateDto.getSystemDesignLabels().size());
        
        for (SystemDesignLabelCreateDto createDto : bulkCreateDto.getSystemDesignLabels()) {
            try {
                addLabelToSystemDesign(createDto);
            } catch (Exception e) {
                log.warn("Failed to add label {} to system design {}: {}", 
                        createDto.getLabelId(), createDto.getSystemDesignId(), e.getMessage());
                // Continue with other labels
            }
        }
        
        log.info("Completed bulk addition of labels to system designs");
    }

    @Override
    public void removeLabelFromSystemDesign(String systemDesignId, String labelId) {
        log.info("Removing label {} from system design {}", labelId, systemDesignId);
        
        SystemDesignLabel systemDesignLabel = systemDesignLabelRepository.findBySystemDesignIdAndLabelId(systemDesignId, labelId)
                .orElseThrow(() -> new ResourceNotFoundException("System design-label association not found"));
        
        systemDesignLabel.setDeleted(true);
        systemDesignLabelRepository.save(systemDesignLabel);
        
        log.info("Successfully removed label {} from system design {}", labelId, systemDesignId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getSystemDesignContent(String id) throws Exception {
        log.info("Retrieving content for system design with ID: {}", id);
        
        SystemDesign systemDesign = systemDesignRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with ID: " + id));
        
        return readFileContent(systemDesign.getFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public String getSystemDesignContentByUrlSlug(String urlSlug) throws Exception {
        log.info("Retrieving content for system design with URL slug: {}", urlSlug);
        
        SystemDesign systemDesign = systemDesignRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("System design not found with URL slug: " + urlSlug));
        
        return readFileContent(systemDesign.getFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignLabelReferenceDto> getSystemDesignLabels(String systemDesignId) {
        log.info("Retrieving labels for system design: {}", systemDesignId);
        
        List<SystemDesignLabel> systemDesignLabels = systemDesignLabelRepository.findBySystemDesignId(systemDesignId);
        return systemDesignLabels.stream()
                .map(this::convertToLabelReferenceDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemDesignLabelReferenceDto> getPrimarySystemDesignLabels(String systemDesignId) {
        log.info("Retrieving primary labels for system design: {}", systemDesignId);
        
        List<SystemDesignLabel> primaryLabels = systemDesignLabelRepository.findPrimaryBySystemDesignId(systemDesignId);
        return primaryLabels.stream()
                .map(this::convertToLabelReferenceDto)
                .collect(Collectors.toList());
    }

    // Private helper methods

    private void updateSystemDesignFields(SystemDesign systemDesign, SystemDesignUpdateDto updateDto) {
        if (updateDto.getTitle() != null && !updateDto.getTitle().trim().isEmpty()) {
            systemDesign.setTitle(updateDto.getTitle());
            
            // Regenerate URL slug if title changed
            String sourceCode = systemDesign.getSource() != null ? systemDesign.getSource().getCode() : "";
            String baseSlug = UrlSlugGenerator.generateArticleSlug(updateDto.getTitle(), sourceCode);
            String uniqueSlug = generateUniqueSlug(baseSlug, systemDesign.getId());
            systemDesign.setUrlSlug(uniqueSlug);
        }
        
        if (updateDto.getShortDescription() != null) {
            systemDesign.setShortDescription(updateDto.getShortDescription());
        }
        
        if (updateDto.getFilePath() != null && !updateDto.getFilePath().trim().isEmpty()) {
            systemDesign.setFilePath(updateDto.getFilePath());
        }
        
        if (updateDto.getOriginalUrl() != null) {
            systemDesign.setOriginalUrl(updateDto.getOriginalUrl());
        }
        
        if (updateDto.getStatus() != null && !updateDto.getStatus().trim().isEmpty()) {
            systemDesign.setStatus(SystemDesign.SystemDesignStatus.valueOf(updateDto.getStatus()));
        }
        
        if (updateDto.getSourceId() != null) {
            if (updateDto.getSourceId().trim().isEmpty()) {
                systemDesign.setSource(null);
            } else {
                Source source = sourceRepository.findByIdAndNotDeleted(updateDto.getSourceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + updateDto.getSourceId()));
                systemDesign.setSource(source);
            }
        }
    }

    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }

    private String generateUniqueSlug(String baseSlug, String excludeId) {
        String candidateSlug = baseSlug;
        int counter = 1;
        
        while (isSlugTaken(candidateSlug, excludeId)) {
            candidateSlug = baseSlug + "-" + counter;
            counter++;
        }
        
        return candidateSlug;
    }

    private boolean isSlugTaken(String slug, String excludeId) {
        if (excludeId == null) {
            return systemDesignRepository.existsByUrlSlug(slug);
        } else {
            return systemDesignRepository.existsByUrlSlugAndIdNot(slug, excludeId);
        }
    }

    private String readFileContent(String filePath) throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            if (!resource.exists()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }
            
            try (InputStream inputStream = resource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Error reading file content from path: {}", filePath, e);
            throw new Exception("Failed to read file content: " + e.getMessage(), e);
        }
    }

    // DTO conversion methods

    private SystemDesignDto convertToDto(SystemDesign systemDesign) {
        SystemDesignDto dto = new SystemDesignDto();
        dto.setId(systemDesign.getId());
        dto.setTitle(systemDesign.getTitle());
        dto.setShortDescription(systemDesign.getShortDescription());
        dto.setOriginalUrl(systemDesign.getOriginalUrl());
        dto.setStatus(systemDesign.getStatus().name());
        dto.setUri("/api/v1/system-designs/" + systemDesign.getUrlSlug());
        dto.setContentUrl("/api/v1/system-designs/" + systemDesign.getUrlSlug() + "/content");
        dto.setVersion(systemDesign.getVersion());
        dto.setCreatedAt(systemDesign.getCreatedAt());
        dto.setUpdatedAt(systemDesign.getUpdatedAt());
        
        // Convert source
        if (systemDesign.getSource() != null) {
            SourceSummaryDto sourceDto = new SourceSummaryDto();
            sourceDto.setName(systemDesign.getSource().getName());
            sourceDto.setQuestionUri("/api/v1/sources/" + systemDesign.getSource().getUrlSlug());
            dto.setSource(sourceDto);
        }
        
        // Convert user
        if (systemDesign.getCreatedByUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(systemDesign.getCreatedByUser().getId());
            userDto.setUsername(systemDesign.getCreatedByUser().getUsername());
            userDto.setEmail(systemDesign.getCreatedByUser().getEmail());
            dto.setCreatedByUser(userDto);
        }
        
        // Convert labels (primary only for summary)
        if (systemDesign.getSystemDesignLabels() != null) {
            List<LabelSummaryDto> tags = systemDesign.getSystemDesignLabels().stream()
                    .filter(sdl -> !sdl.getDeleted() && sdl.getIsPrimary())
                    .sorted(Comparator.comparing(SystemDesignLabel::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(sdl -> convertLabelToSummaryDto(sdl.getLabel()))
                    .collect(Collectors.toList());
            dto.setTags(tags);
        }
        
        return dto;
    }

    private SystemDesignSummaryDto convertToSummaryDto(SystemDesign systemDesign) {
        SystemDesignSummaryDto dto = new SystemDesignSummaryDto();
        dto.setId(systemDesign.getId());
        dto.setTitle(systemDesign.getTitle());
        dto.setShortDescription(systemDesign.getShortDescription());
        dto.setSourceName(systemDesign.getSource() != null ? systemDesign.getSource().getName() : null);
        dto.setStatus(systemDesign.getStatus().name());
        dto.setUri("/api/v1/system-designs/" + systemDesign.getUrlSlug());
        dto.setContentUrl("/api/v1/system-designs/" + systemDesign.getUrlSlug() + "/content");
        dto.setCreatedAt(systemDesign.getCreatedAt());
        
        // Convert primary labels
        if (systemDesign.getSystemDesignLabels() != null) {
            List<LabelSummaryDto> primaryTags = systemDesign.getSystemDesignLabels().stream()
                    .filter(sdl -> !sdl.getDeleted() && sdl.getIsPrimary())
                    .sorted(Comparator.comparing(SystemDesignLabel::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                    .limit(5) // Top 5 primary tags
                    .map(sdl -> convertLabelToSummaryDto(sdl.getLabel()))
                    .collect(Collectors.toList());
            dto.setPrimaryTags(primaryTags);
        }
        
        return dto;
    }

    private SystemDesignLabelReferenceDto convertToLabelReferenceDto(SystemDesignLabel systemDesignLabel) {
        SystemDesignLabelReferenceDto dto = new SystemDesignLabelReferenceDto();
        dto.setId(systemDesignLabel.getId());
        dto.setSystemDesignId(systemDesignLabel.getSystemDesign().getId());
        dto.setIsPrimary(systemDesignLabel.getIsPrimary());
        dto.setDisplayOrder(systemDesignLabel.getDisplayOrder());
        dto.setCreatedAt(systemDesignLabel.getCreatedAt());
        
        // Convert label
        if (systemDesignLabel.getLabel() != null) {
            dto.setLabel(convertLabelToSummaryDto(systemDesignLabel.getLabel()));
        }
        
        return dto;
    }

    private LabelSummaryDto convertLabelToSummaryDto(Label label) {
        LabelSummaryDto dto = new LabelSummaryDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setUrlSlug(label.getUrlSlug());
        dto.setUri("/api/v1/labels/" + label.getUrlSlug());
        dto.setQuestionUri("/api/v1/system-designs/label/" + label.getUrlSlug());
        
        if (label.getCategory() != null) {
            dto.setCategoryCode(label.getCategory().getCode());
        }
        
        return dto;
    }
}
