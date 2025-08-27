package com.codyssey.api.service.impl;

import com.codyssey.api.dto.label.*;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.Label;
import com.codyssey.api.model.LabelCategory;
import com.codyssey.api.repository.LabelCategoryRepository;
import com.codyssey.api.repository.LabelRepository;
import com.codyssey.api.service.LabelService;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of LabelService
 * <p>
 * Provides label management functionality including
 * creation, retrieval, updating, and deletion with hierarchy support.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelCategoryRepository labelCategoryRepository;

    @Override
    public LabelDto createLabel(LabelCreateDto createDto) {
        log.info("Creating new label with name: {}", createDto.getName());

        // Validate category exists
        LabelCategory category = labelCategoryRepository.findByIdAndNotDeleted(createDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with ID: " + createDto.getCategoryId()));

        // Validate parent if provided
        Label parent = null;
        if (createDto.getParentId() != null && !createDto.getParentId().trim().isEmpty()) {
            parent = labelRepository.findByIdAndNotDeleted(createDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent label not found with ID: " + createDto.getParentId()));

            // Ensure parent is in the same category
            if (!parent.getCategory().getId().equals(createDto.getCategoryId())) {
                throw new IllegalArgumentException("Parent label must be in the same category");
            }
        }

        // Check name uniqueness within category and parent context
        if (labelRepository.existsByNameAndCategoryAndParent(
                createDto.getName(),
                createDto.getCategoryId(),
                createDto.getParentId())) {
            throw new DuplicateResourceException("Label name already exists in this category/parent context");
        }

        Label label = new Label();
        label.setName(createDto.getName());
        label.setDescription(createDto.getDescription());
        label.setCategory(category);
        label.setParent(parent);
        label.setActive(createDto.getActive() != null ? createDto.getActive() : true);
        
        // Generate unique URL slug
        String baseSlug = UrlSlugGenerator.generateLabelSlug(createDto.getName(), category.getName());
        String uniqueSlug = generateUniqueSlug(baseSlug);
        label.setUrlSlug(uniqueSlug);

        Label savedLabel = labelRepository.save(label);
        log.info("Successfully created label with ID: {}", savedLabel.getId());

        return convertToDto(savedLabel);
    }

    @Override
    public List<LabelDto> createLabelsBulk(LabelBulkCreateDto bulkCreateDto) {
        log.info("Creating {} labels in bulk", bulkCreateDto.getLabels().size());

        List<LabelDto> createdLabels = new ArrayList<>();
        Map<String, String> tempIdToRealIdMap = new HashMap<>();

        // First pass: create labels without parent references
        for (LabelCreateDto createDto : bulkCreateDto.getLabels()) {
            // Skip labels that have parent references for now
            if (createDto.getParentId() == null || createDto.getParentId().trim().isEmpty()) {
                LabelDto createdLabel = createLabel(createDto);
                createdLabels.add(createdLabel);
                // Store mapping if this was a temporary ID scenario
                tempIdToRealIdMap.put(createDto.getName(), createdLabel.getId());
            }
        }

        // Second pass: create labels with parent references
        for (LabelCreateDto createDto : bulkCreateDto.getLabels()) {
            if (createDto.getParentId() != null && !createDto.getParentId().trim().isEmpty()) {
                // Check if parentId is a real ID or needs mapping
                String actualParentId = tempIdToRealIdMap.getOrDefault(createDto.getParentId(), createDto.getParentId());
                createDto.setParentId(actualParentId);

                LabelDto createdLabel = createLabel(createDto);
                createdLabels.add(createdLabel);
            }
        }

        log.info("Successfully created {} labels in bulk", createdLabels.size());
        return createdLabels;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getAllLabels() {
        log.info("Retrieving all labels");
        List<Label> labels = labelRepository.findByDeletedFalse();
        return labels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelHierarchyDto> getLabelsHierarchy() {
        log.info("Retrieving labels hierarchy");
        List<Label> rootLabels = labelRepository.findHierarchy();
        return rootLabels.stream()
                .map(this::convertToHierarchyDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabelDto> getLabelById(String id) {
        log.info("Retrieving label by ID: {}", id);
        return labelRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabelDto> getLabelByUrlSlug(String urlSlug) {
        log.info("Retrieving label by URL slug: {}", urlSlug);
        return labelRepository.findByUrlSlug(urlSlug)
                .map(this::convertToDto);
    }

    @Override
    public LabelDto updateLabel(String id, LabelUpdateDto updateDto) {
        log.info("Updating label with ID: {}", id);

        Label label = labelRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + id));

        // Update fields only if they are provided
        if (updateDto.getName() != null) {
            // Check name uniqueness if name is being changed
            if (!label.getName().equals(updateDto.getName())) {
                String parentId = label.getParent() != null ? label.getParent().getId() : null;
                if (labelRepository.existsByNameAndCategoryAndParentExcludingId(
                        updateDto.getName(),
                        label.getCategory().getId(),
                        parentId,
                        id)) {
                    throw new DuplicateResourceException("Label name already exists in this category/parent context");
                }
            }
            label.setName(updateDto.getName());
        }

        if (updateDto.getDescription() != null) {
            label.setDescription(updateDto.getDescription());
        }

        if (updateDto.getParentId() != null) {
            if (updateDto.getParentId().trim().isEmpty()) {
                // Remove parent (make it a root label)
                label.setParent(null);
            } else {
                // Set new parent
                Label newParent = labelRepository.findByIdAndNotDeleted(updateDto.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent label not found with ID: " + updateDto.getParentId()));

                // Prevent circular references
                if (isCircularReference(label, newParent)) {
                    throw new IllegalArgumentException("Cannot set parent: would create circular reference");
                }

                // Ensure parent is in the same category
                if (!newParent.getCategory().getId().equals(label.getCategory().getId())) {
                    throw new IllegalArgumentException("Parent label must be in the same category");
                }

                label.setParent(newParent);
            }
        }

        if (updateDto.getActive() != null) {
            label.setActive(updateDto.getActive());
        }

        Label updatedLabel = labelRepository.save(label);
        log.info("Successfully updated label with ID: {}", updatedLabel.getId());

        return convertToDto(updatedLabel);
    }

    @Override
    public void deleteLabel(String id) {
        log.info("Soft deleting label with ID: {}", id);

        Label label = labelRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + id));

        // Check if label has children
        List<Label> children = labelRepository.findByParent(label);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete label with child labels. Delete children first or reassign them.");
        }

        label.setDeleted(true);
        labelRepository.save(label);

        log.info("Successfully soft deleted label with ID: {}", id);
    }

    @Override
    public LabelDto updateLabelByUrlSlug(String urlSlug, LabelUpdateDto updateDto) {
        log.info("Updating label with URL slug: {}", urlSlug);

        Label label = labelRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with URL slug: " + urlSlug));

        // Update fields only if they are provided
        if (updateDto.getName() != null) {
            // Check name uniqueness if name is being changed
            if (!label.getName().equals(updateDto.getName())) {
                String parentId = label.getParent() != null ? label.getParent().getId() : null;
                if (labelRepository.existsByNameAndCategoryAndParentExcludingId(
                        updateDto.getName(),
                        label.getCategory().getId(),
                        parentId,
                        label.getId())) {
                    throw new DuplicateResourceException("Label name already exists in this category/parent context");
                }
            }
            label.setName(updateDto.getName());
            
            // Update URL slug if name changes
            String baseSlug = UrlSlugGenerator.generateLabelSlug(updateDto.getName(), label.getCategory().getName());
            String uniqueSlug = generateUniqueSlug(baseSlug, label.getId());
            label.setUrlSlug(uniqueSlug);
        }

        if (updateDto.getDescription() != null) {
            label.setDescription(updateDto.getDescription());
        }

        if (updateDto.getParentId() != null) {
            if (updateDto.getParentId().trim().isEmpty()) {
                // Remove parent (make it a root label)
                label.setParent(null);
            } else {
                // Set new parent
                Label newParent = labelRepository.findByIdAndNotDeleted(updateDto.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent label not found with ID: " + updateDto.getParentId()));

                // Prevent circular references
                if (isCircularReference(label, newParent)) {
                    throw new IllegalArgumentException("Cannot set parent: would create circular reference");
                }

                // Ensure parent is in the same category
                if (!newParent.getCategory().getId().equals(label.getCategory().getId())) {
                    throw new IllegalArgumentException("Parent label must be in the same category");
                }

                label.setParent(newParent);
            }
        }

        if (updateDto.getActive() != null) {
            label.setActive(updateDto.getActive());
        }

        Label updatedLabel = labelRepository.save(label);
        log.info("Successfully updated label with URL slug: {}", urlSlug);

        return convertToDto(updatedLabel);
    }

    @Override
    public void deleteLabelByUrlSlug(String urlSlug) {
        log.info("Soft deleting label with URL slug: {}", urlSlug);

        Label label = labelRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with URL slug: " + urlSlug));

        // Check if label has children
        List<Label> children = labelRepository.findByParent(label);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete label with child labels. Delete children first or reassign them.");
        }

        label.setDeleted(true);
        labelRepository.save(label);

        log.info("Successfully soft deleted label with URL slug: {}", urlSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getLabelsByCategory(String categoryCode) {
        log.info("Retrieving labels by category: {}", categoryCode);
        LabelCategory category = labelCategoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with code: " + categoryCode));

        List<Label> labels = labelRepository.findByCategory(category);
        return labels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getActiveLabelsByCategory(String categoryCode) {
        log.info("Retrieving active labels by category: {}", categoryCode);
        LabelCategory category = labelCategoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with code: " + categoryCode));

        List<Label> labels = labelRepository.findActiveLabelsByCategory(category);
        return labels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getRootLabelsByCategory(String categoryCode) {
        log.info("Retrieving root labels by category: {}", categoryCode);
        LabelCategory category = labelCategoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with code: " + categoryCode));

        List<Label> labels = labelRepository.findRootLabelsByCategory(category);
        return labels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getChildLabels(String labelId) {
        log.info("Retrieving child labels for label ID: {}", labelId);
        List<Label> children = labelRepository.findByParentId(labelId);
        return children.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> searchLabels(String searchTerm) {
        log.info("Searching labels by term: {}", searchTerm);
        List<Label> labels = labelRepository.findByNameContainingIgnoreCase(searchTerm);
        return labels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAvailableCategories() {
        log.info("Retrieving available categories");
        List<LabelCategory> categories = labelRepository.findDistinctCategories();
        return categories.stream()
                .map(LabelCategory::getCode)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkNameAvailability(String name, String categoryCode, String parentId) {
        log.info("Checking name availability: {} in category: {} with parent: {}", name, categoryCode, parentId);

        LabelCategory category = labelCategoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with code: " + categoryCode));

        boolean exists = labelRepository.existsByNameAndCategoryAndParent(name, category.getId(), parentId);
        return !exists; // Return true if available (doesn't exist)
    }

    @Override
    public LabelDto toggleActiveStatus(String id) {
        log.info("Toggling active status for label ID: {}", id);

        Label label = labelRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + id));

        label.setActive(!label.getActive());
        Label updatedLabel = labelRepository.save(label);

        log.info("Successfully toggled active status for label ID: {} to {}", id, updatedLabel.getActive());
        return convertToDto(updatedLabel);
    }

    /**
     * Check for circular reference in parent-child relationship
     */
    private boolean isCircularReference(Label child, Label potentialParent) {
        if (potentialParent == null) {
            return false;
        }

        if (child.getId().equals(potentialParent.getId())) {
            return true;
        }

        return isAncestor(child, potentialParent);
    }

    /**
     * Check if child is an ancestor of potentialParent
     */
    private boolean isAncestor(Label child, Label potentialParent) {
        List<Label> descendants = getDescendants(child);
        return descendants.stream().anyMatch(desc -> desc.getId().equals(potentialParent.getId()));
    }

    /**
     * Get all descendants of a label
     */
    private List<Label> getDescendants(Label label) {
        List<Label> descendants = new ArrayList<>();
        List<Label> children = labelRepository.findByParent(label);

        for (Label child : children) {
            descendants.add(child);
            descendants.addAll(getDescendants(child));
        }

        return descendants;
    }

    /**
     * Convert Label entity to DTO
     */
    private LabelDto convertToDto(Label label) {
        LabelDto dto = new LabelDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setActive(label.getActive());
        dto.setUri("/api/v1/labels/" + label.getUrlSlug());
        dto.setArticlesUri("/api/v1/articles/label/" + label.getUrlSlug());
        dto.setMcqsUri("/api/v1/mcq/labels/" + label.getUrlSlug() + "/questions");
        dto.setQuestionsUri("/api/v1/coding-questions/label/" + label.getUrlSlug());

        // Convert category
        if (label.getCategory() != null) {
            dto.setCategory(convertCategoryToSummaryDto(label.getCategory()));
        }

        // Convert parent (without children to avoid circular reference)
        if (label.getParent() != null) {
            LabelSummaryDto parentDto = new LabelSummaryDto();
            parentDto.setName(label.getParent().getName());
            parentDto.setDescription(label.getParent().getDescription());
            parentDto.setUri("/api/v1/labels/" + label.getParent().getUrlSlug());
            dto.setParent(parentDto);
        }

        // Convert children (without their children to avoid deep nesting)
        if (label.getChildren() != null && !label.getChildren().isEmpty()) {
            List<LabelSummaryDto> childrenDtos = label.getChildren().stream()
                    .filter(child -> !child.getDeleted())
                    .map(child -> {
                        LabelSummaryDto childDto = new LabelSummaryDto();
                        childDto.setName(child.getName());
                        childDto.setDescription(child.getDescription());
                        childDto.setUri("/api/v1/labels/" + child.getUrlSlug());
                        return childDto;
                    })
                    .collect(Collectors.toList());
            dto.setChildren(childrenDtos);
        }

        return dto;
    }

    /**
     * Convert Label entity to HierarchyDto
     */
    private LabelHierarchyDto convertToHierarchyDto(Label label) {
        LabelHierarchyDto dto = new LabelHierarchyDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setActive(label.getActive());
        dto.setCategoryId(label.getCategory().getId());
        dto.setCategoryName(label.getCategory().getName());
        dto.setParentId(label.getParent() != null ? label.getParent().getId() : null);
        dto.setCreatedAt(label.getCreatedAt());
        dto.setUpdatedAt(label.getUpdatedAt());

        // Convert children recursively
        if (label.getChildren() != null && !label.getChildren().isEmpty()) {
            List<LabelHierarchyDto> childrenDtos = label.getChildren().stream()
                    .filter(child -> !child.getDeleted())
                    .map(this::convertToHierarchyDto)
                    .collect(Collectors.toList());
            dto.setChildren(childrenDtos);
        }

        return dto;
    }

    /**
     * Convert LabelCategory to DTO
     */
    private com.codyssey.api.dto.labelcategory.LabelCategoryDto convertCategoryToDto(LabelCategory category) {
        com.codyssey.api.dto.labelcategory.LabelCategoryDto dto = new com.codyssey.api.dto.labelcategory.LabelCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setActive(category.getActive());
        dto.setUri("/api/v1/labelcategories/" + category.getUrlSlug());
        return dto;
    }

    /**
     * Convert LabelCategory to Summary DTO
     */
    private LabelCategorySummaryDto convertCategoryToSummaryDto(LabelCategory category) {
        LabelCategorySummaryDto dto = new LabelCategorySummaryDto();
        dto.setName(category.getName());
        dto.setUri("/api/v1/labels/category/" + category.getCode());
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
               labelRepository.existsByUrlSlugAndIdNot(slug, excludeId) : 
               labelRepository.existsByUrlSlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
}