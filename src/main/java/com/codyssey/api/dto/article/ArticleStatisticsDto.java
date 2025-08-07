package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Article statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsDto {

    private Long totalArticles;
    
    private Long activeArticles;
    
    private Long draftArticles;
    
    private Long deprecatedArticles;
    
    private Long totalTags;
    
    private String mostUsedTag;
    
    private Long articlesWithSource;
    
    private Long articlesWithoutSource;
}