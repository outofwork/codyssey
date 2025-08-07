package com.codyssey.api.service.impl;

import com.codyssey.api.dto.labelcategory.LabelCategoryCreateDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryDto;
import com.codyssey.api.dto.labelcategory.LabelCategoryUpdateDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.LabelCategory;
import com.codyssey.api.repository.LabelCategoryRepository;
import com.codyssey.api.service.LabelCategoryService;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of LabelCategoryService
 * <p>
 * Provides label category management functionality including
 * creation, retrieval, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabelCategoryServiceImpl implements LabelCategoryService {

    private final LabelCategoryRepository labelCategoryRepository;

    @Override
    public LabelCategoryDto createCategory(LabelCategoryCreateDto createDto) {
        log.info("Creating new label category with code: {}", createDto.getCode());

        // Check if code already exists
        if (labelCategoryRepository.existsByCode(createDto.getCode())) {
            throw new DuplicateResourceException("Code is already taken!");
        }

        LabelCategory labelCategory = new LabelCategory();
        labelCategory.setName(createDto.getName());
        labelCategory.setCode(createDto.getCode());
        labelCategory.setDescription(createDto.getDescription());
        labelCategory.setActive(createDto.getActive() != null ? createDto.getActive() : true);
        
        // Generate unique URL slug
        String baseSlug = UrlSlugGenerator.generateCategorySlug(createDto.getName());
        String uniqueSlug = generateUniqueSlug(baseSlug);
        labelCategory.setUrlSlug(uniqueSlug);

        LabelCategory savedCategory = labelCategoryRepository.save(labelCategory);
        log.info("Successfully created label category with ID: {}", savedCategory.getId());

        return convertToDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelCategoryDto> getAllCategories() {
        log.info("Retrieving all label categories");
        List<LabelCategory> categories = labelCategoryRepository.findByDeletedFalse();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelCategoryDto> getActiveCategories() {
        log.info("Retrieving active label categories");
        List<LabelCategory> categories = labelCategoryRepository.findActiveCategories();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabelCategoryDto> getCategoryById(String id) {
        log.info("Retrieving label category by ID: {}", id);
        return labelCategoryRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabelCategoryDto> getCategoryByUrlSlug(String urlSlug) {
        log.info("Retrieving label category by URL slug: {}", urlSlug);
        return labelCategoryRepository.findByUrlSlug(urlSlug)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabelCategoryDto> getCategoryByCode(String code) {
        log.info("Retrieving label category by code: {}", code);
        return labelCategoryRepository.findByCode(code)
                .filter(category -> !category.getDeleted())
                .map(this::convertToDto);
    }

    @Override
    public LabelCategoryDto updateCategory(String id, LabelCategoryUpdateDto updateDto) {
        log.info("Updating label category with ID: {}", id);

        LabelCategory category = labelCategoryRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with ID: " + id));

        // Update fields only if they are provided
        if (updateDto.getName() != null) {
            category.setName(updateDto.getName());
        }
        if (updateDto.getDescription() != null) {
            category.setDescription(updateDto.getDescription());
        }
        if (updateDto.getActive() != null) {
            category.setActive(updateDto.getActive());
        }

        LabelCategory updatedCategory = labelCategoryRepository.save(category);
        log.info("Successfully updated label category with ID: {}", updatedCategory.getId());

        return convertToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(String id) {
        log.info("Soft deleting label category with ID: {}", id);

        LabelCategory category = labelCategoryRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with ID: " + id));

        category.setDeleted(true);
        labelCategoryRepository.save(category);

        log.info("Successfully soft deleted label category with ID: {}", id);
    }

    @Override
    public LabelCategoryDto updateCategoryByUrlSlug(String urlSlug, LabelCategoryUpdateDto updateDto) {
        log.info("Updating label category with URL slug: {}", urlSlug);

        LabelCategory category = labelCategoryRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with URL slug: " + urlSlug));

        // Update fields only if they are provided
        if (updateDto.getName() != null) {
            category.setName(updateDto.getName());
            
            // Update URL slug if name changes
            String baseSlug = UrlSlugGenerator.generateCategorySlug(updateDto.getName());
            String uniqueSlug = generateUniqueSlug(baseSlug, category.getId());
            category.setUrlSlug(uniqueSlug);
        }
        if (updateDto.getDescription() != null) {
            category.setDescription(updateDto.getDescription());
        }
        if (updateDto.getActive() != null) {
            category.setActive(updateDto.getActive());
        }

        LabelCategory updatedCategory = labelCategoryRepository.save(category);
        log.info("Successfully updated label category with URL slug: {}", urlSlug);

        return convertToDto(updatedCategory);
    }

    @Override
    public void deleteCategoryByUrlSlug(String urlSlug) {
        log.info("Soft deleting label category with URL slug: {}", urlSlug);

        LabelCategory category = labelCategoryRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label category not found with URL slug: " + urlSlug));

        category.setDeleted(true);
        labelCategoryRepository.save(category);

        log.info("Successfully soft deleted label category with URL slug: {}", urlSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelCategoryDto> searchCategoriesByName(String name) {
        log.info("Searching label categories by name: {}", name);
        List<LabelCategory> categories = labelCategoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        log.info("Checking if code exists: {}", code);
        return labelCategoryRepository.existsByCode(code);
    }

    /**
     * Convert LabelCategory entity to DTO
     *
     * @param category the entity to convert
     * @return converted DTO
     */
    private LabelCategoryDto convertToDto(LabelCategory category) {
        LabelCategoryDto dto = new LabelCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setActive(category.getActive());
        dto.setUri("/api/v1/labelcategories/" + category.getUrlSlug());
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
               labelCategoryRepository.existsByUrlSlugAndIdNot(slug, excludeId) : 
               labelCategoryRepository.existsByUrlSlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
}