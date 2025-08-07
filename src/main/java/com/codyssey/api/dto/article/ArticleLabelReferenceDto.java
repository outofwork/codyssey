package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simplified label reference for Article responses
 * Contains only the label name and single URL for that label
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelReferenceDto {
    
    private String id;
    private String name;
    private String urlSlug;
    private String articleUrls;
}