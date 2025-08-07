package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Article with simplified details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private String id;
    
    private String title;
    
    private String shortDescription;
    
    private String articleType;
    
    private List<ArticleLabelReferenceDto> categoryLabels;
    
    private ArticleLabelReferenceDto difficultyLabel;
    
    private String status;
    
    private Integer readingTimeMinutes;
    
    private String contentUrl;
    
    private Long version;
}