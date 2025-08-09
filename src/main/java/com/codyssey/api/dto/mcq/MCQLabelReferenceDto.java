package com.codyssey.api.dto.mcq;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for MCQ Label Reference
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQLabelReferenceDto {

    private String id;
    private LabelSummaryDto label;
    private Integer relevanceScore;
    private Boolean isPrimary;
    private String notes;
    private String uri;
}
