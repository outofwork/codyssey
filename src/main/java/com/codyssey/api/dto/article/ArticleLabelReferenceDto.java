package com.codyssey.api.dto.article;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for ArticleLabel reference information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelReferenceDto {

    private String id;
    
    private String articleId;
    
    private LabelSummaryDto label;
    
    private Integer relevanceScore;
    
    private Boolean isPrimary;
    
    private String notes;
}