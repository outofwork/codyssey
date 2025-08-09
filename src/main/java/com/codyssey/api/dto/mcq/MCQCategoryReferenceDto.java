package com.codyssey.api.dto.mcq;

import com.codyssey.api.dto.labelcategory.LabelCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object for MCQ Category reference
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQCategoryReferenceDto {

    private String id;
    private LabelCategoryDto category;
    private BigDecimal relevanceScore;
    private Boolean isPrimary;
    private String notes;
}
