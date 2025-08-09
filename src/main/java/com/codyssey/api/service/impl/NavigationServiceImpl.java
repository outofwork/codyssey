package com.codyssey.api.service.impl;

import com.codyssey.api.dto.navigation.CategoryNavigationDto;
import com.codyssey.api.dto.navigation.LabelNavigationDto;
import com.codyssey.api.dto.navigation.NavigationLinksDto;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.Label;
import com.codyssey.api.model.LabelCategory;
import com.codyssey.api.repository.LabelCategoryRepository;
import com.codyssey.api.repository.LabelRepository;
import com.codyssey.api.repository.MCQQuestionRepository;
import com.codyssey.api.service.NavigationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of NavigationService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NavigationServiceImpl implements NavigationService {

    private final LabelCategoryRepository labelCategoryRepository;
    private final LabelRepository labelRepository;
    private final MCQQuestionRepository mcqQuestionRepository;

    @Override
    public List<CategoryNavigationDto> getAllLabelCategories() {
        log.info("Getting all label categories for navigation");

        List<LabelCategory> categories = labelCategoryRepository.findByActiveOrderByName(true);
        return categories.stream()
                .map(this::convertToCategoryNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelNavigationDto> getLabelsByCategory(String categoryId) {
        log.info("Getting labels by category ID: {}", categoryId);

        // Validate category exists
        labelCategoryRepository.findByIdAndNotDeleted(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with ID: " + categoryId));

        List<Label> labels = labelRepository.findByCategoryIdAndActiveOrderByName(categoryId, true);
        return labels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelNavigationDto getLabelNavigation(String labelId) {
        log.info("Getting label navigation for ID: {}", labelId);

        Label label = labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        return convertToLabelNavigationDto(label);
    }

    @Override
    public List<LabelNavigationDto> getRootLabelsByCategory(String categoryId) {
        log.info("Getting root labels by category ID: {}", categoryId);

        // Validate category exists
        labelCategoryRepository.findByIdAndNotDeleted(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with ID: " + categoryId));

        List<Label> rootLabels = labelRepository.findByCategoryIdAndParentIdAndActiveOrderByName(categoryId, null, true);
        return rootLabels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelNavigationDto> getChildLabels(String parentLabelId) {
        log.info("Getting child labels for parent ID: {}", parentLabelId);

        Label parentLabel = labelRepository.findByIdAndNotDeleted(parentLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + parentLabelId));

        List<Label> childLabels = labelRepository.findByParentIdAndActiveOrderByName(parentLabelId, true);
        return childLabels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelNavigationDto> getLabelsByCategorySlug(String categorySlug) {
        log.info("Getting labels by category slug: {}", categorySlug);

        // Find category by slug
        LabelCategory category = labelCategoryRepository.findByUrlSlug(categorySlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with slug: " + categorySlug));

        List<Label> labels = labelRepository.findByCategoryIdAndActiveOrderByName(category.getId(), true);
        return labels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelNavigationDto getLabelNavigationBySlug(String labelSlug) {
        log.info("Getting label navigation for slug: {}", labelSlug);

        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        return convertToLabelNavigationDto(label);
    }

    @Override
    public List<LabelNavigationDto> getRootLabelsByCategorySlug(String categorySlug) {
        log.info("Getting root labels by category slug: {}", categorySlug);

        // Find category by slug
        LabelCategory category = labelCategoryRepository.findByUrlSlug(categorySlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with slug: " + categorySlug));

        List<Label> rootLabels = labelRepository.findByCategoryIdAndParentIdAndActiveOrderByName(category.getId(), null, true);
        return rootLabels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelNavigationDto> getChildLabelsBySlug(String parentLabelSlug) {
        log.info("Getting child labels for parent slug: {}", parentLabelSlug);

        Label parentLabel = labelRepository.findByUrlSlug(parentLabelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + parentLabelSlug));

        List<Label> childLabels = labelRepository.findByParentIdAndActiveOrderByName(parentLabel.getId(), true);
        return childLabels.stream()
                .map(this::convertToLabelNavigationDto)
                .collect(Collectors.toList());
    }

    private LabelNavigationDto convertToLabelNavigationDto(Label label) {
        LabelNavigationDto dto = new LabelNavigationDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setUrlSlug(label.getUrlSlug());
        dto.setCategoryId(label.getCategory().getId());
        dto.setCategoryName(label.getCategory().getName());
        dto.setHasChildren(label.hasChildren());
        dto.setIsRoot(label.isRoot());

        // Set parent information
        if (label.getParent() != null) {
            dto.setParent(convertToLabelSummaryDto(label.getParent()));
        }

        // Set children information
        if (label.getChildren() != null && !label.getChildren().isEmpty()) {
            dto.setChildren(label.getChildren().stream()
                    .filter(child -> child.getActive())
                    .map(this::convertToLabelSummaryDto)
                    .collect(Collectors.toList()));
        }

        // Set MCQ counts
        dto.setMcqCount(mcqQuestionRepository.countByLabelAndStatus(label.getId(), 
                com.codyssey.api.model.MCQQuestion.MCQStatus.ACTIVE));
        dto.setTotalMcqCountInHierarchy(mcqQuestionRepository.countByLabelHierarchyAndStatus(label.getId(), 
                com.codyssey.api.model.MCQQuestion.MCQStatus.ACTIVE));

        // Set navigation links
        dto.setNavigationLinks(createNavigationLinks(label));

        return dto;
    }

    private NavigationLinksDto createNavigationLinks(Label label) {
        NavigationLinksDto links = new NavigationLinksDto();
        
        String baseUrl = "/api/v1";
        String labelSlug = label.getUrlSlug();
        
        // MCQ-related URLs using slugs
        links.setMcqsUrl(baseUrl + "/mcq/labels/" + labelSlug + "/questions");
        links.setRandomMcqsUrl(baseUrl + "/mcq/labels/" + labelSlug + "/questions/random");
        
        // Label navigation URLs using slugs
        links.setLabelsUrl(baseUrl + "/navigation/labels/" + labelSlug);
        
        if (label.getParent() != null && label.getParent().getUrlSlug() != null) {
            links.setParentUrl(baseUrl + "/navigation/labels/" + label.getParent().getUrlSlug());
        }
        
        if (label.hasChildren()) {
            links.setChildrenUrl(baseUrl + "/navigation/labels/" + labelSlug + "/children");
        }
        
        return links;
    }

    private CategoryNavigationDto convertToCategoryNavigationDto(LabelCategory category) {
        CategoryNavigationDto dto = new CategoryNavigationDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setUrlSlug(category.getUrlSlug());
        
        // Count labels in this category
        long labelCount = labelRepository.countByCategoryIdAndActive(category.getId(), true);
        dto.setLabelCount(labelCount);
        
        return dto;
    }

    private com.codyssey.api.dto.label.LabelSummaryDto convertToLabelSummaryDto(Label label) {
        com.codyssey.api.dto.label.LabelSummaryDto dto = new com.codyssey.api.dto.label.LabelSummaryDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setUrlSlug(label.getUrlSlug());
        return dto;
    }
}
