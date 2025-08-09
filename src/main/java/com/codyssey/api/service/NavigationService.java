package com.codyssey.api.service;

import com.codyssey.api.dto.navigation.CategoryNavigationDto;
import com.codyssey.api.dto.navigation.LabelNavigationDto;

import java.util.List;

/**
 * Service interface for Navigation operations
 */
public interface NavigationService {

    /**
     * Get all label categories for navigation
     */
    List<CategoryNavigationDto> getAllLabelCategories();

    /**
     * Get labels by category for navigation (using category ID)
     */
    List<LabelNavigationDto> getLabelsByCategory(String categoryId);

    /**
     * Get labels by category for navigation (using category slug)
     */
    List<LabelNavigationDto> getLabelsByCategorySlug(String categorySlug);

    /**
     * Get label navigation details including children, MCQ counts, and navigation links (using label ID)
     */
    LabelNavigationDto getLabelNavigation(String labelId);

    /**
     * Get label navigation details including children, MCQ counts, and navigation links (using label slug)
     */
    LabelNavigationDto getLabelNavigationBySlug(String labelSlug);

    /**
     * Get root labels (labels without parent) by category (using category ID)
     */
    List<LabelNavigationDto> getRootLabelsByCategory(String categoryId);

    /**
     * Get root labels (labels without parent) by category (using category slug)
     */
    List<LabelNavigationDto> getRootLabelsByCategorySlug(String categorySlug);

    /**
     * Get child labels for navigation (using parent label ID)
     */
    List<LabelNavigationDto> getChildLabels(String parentLabelId);

    /**
     * Get child labels for navigation (using parent label slug)
     */
    List<LabelNavigationDto> getChildLabelsBySlug(String parentLabelSlug);
}
