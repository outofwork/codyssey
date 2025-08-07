package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for comprehensive Article statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatisticsDto {

    // Overall Statistics
    private Long totalArticles;
    private Long activeArticles;
    private Long draftArticles;
    private Long deprecatedArticles;
    
    // Source Statistics
    private Long totalSources;
    private Long articlesWithSource;
    private Long articlesWithoutSource;
    private List<SourceStatistic> articlesBySource;
    
    // Tag/Label Statistics
    private Long totalTags;
    private Long totalUniqueTagsUsed;
    private String mostUsedTag;
    private List<TagStatistic> articlesByTag;
    
    // Category Statistics
    private Long totalCategories;
    private List<CategoryStatistic> articlesByCategory;
    
    // Temporal Statistics
    private List<MonthlyStatistic> articlesByMonth;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceStatistic {
        private String sourceId;
        private String sourceName;
        private String sourceCode;
        private Long articleCount;
        private Double percentage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatistic {
        private String labelId;
        private String labelName;
        private String categoryCode;
        private Long articleCount;
        private Long primaryCount;
        private Long secondaryCount;
        private Double averageRelevanceScore;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStatistic {
        private String categoryId;
        private String categoryName;
        private String categoryCode;
        private Long articleCount;
        private Long uniqueLabelsUsed;
        private List<String> topLabels;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatistic {
        private String month; // Format: "2024-01"
        private Long articleCount;
    }
}