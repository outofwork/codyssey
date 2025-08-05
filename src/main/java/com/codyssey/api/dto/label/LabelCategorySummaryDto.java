package com.codyssey.api.dto.label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simplified Data Transfer Object for LabelCategory in Label responses
 * <p>
 * Contains only essential category information to keep Label API responses clean.
 * For full category details, use the LabelCategory API endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategorySummaryDto {

    private String id;
    private String name;
    private String code;
}