package com.codyssey.api.dto.label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Label hierarchy representation
 * <p>
 * Simplified version without circular references for hierarchy display
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelHierarchyDto {

    private String id;
    private String name;
    private String description;
    private Boolean active;
    private String categoryId;
    private String categoryName;
    private String parentId;
    private List<LabelHierarchyDto> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}