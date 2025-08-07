package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for Article statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsDto {

    private long totalArticles;
    
    private Map<String, Long> articlesByType;
    
    private Map<String, Long> articlesByDifficulty;
    
    private Map<String, Long> articlesByStatus;
    
    private int totalTags;
    
    private double averageReadingTime;
}