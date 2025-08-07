package com.codyssey.api.dto.label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Label entity
 * <p>
 * Used for transferring label data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {

    private String id;
    private String name;
    private String description;
    private Boolean active;
    private String urlSlug;
    private String uri;
    private LabelCategorySummaryDto category;
    private LabelSummaryDto parent;
    private List<LabelSummaryDto> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}