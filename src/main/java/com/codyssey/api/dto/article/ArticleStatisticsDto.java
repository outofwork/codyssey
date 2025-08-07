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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceStatistic {
        private String sourceName;
        private Long articleCount;
        private String uri; // URI to access articles by this source
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatistic {
        private String labelName;
        private String categoryCode;
        private Long articleCount;
        private String uri; // URI to access articles by this tag
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
}