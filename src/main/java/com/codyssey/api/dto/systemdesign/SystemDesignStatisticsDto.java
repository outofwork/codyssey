package com.codyssey.api.dto.systemdesign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for comprehensive SystemDesign statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignStatisticsDto {

    // Overall Statistics
    private Long totalSystemDesigns;
    private Long activeSystemDesigns;
    private Long draftSystemDesigns;
    private Long deprecatedSystemDesigns;
    
    // Source Statistics
    private Long totalSources;
    private Long systemDesignsWithSource;
    private Long systemDesignsWithoutSource;
    private List<SourceStatistic> systemDesignsBySource;
    
    // Tag/Label Statistics
    private Long totalTags;
    private Long totalUniqueTagsUsed;
    private String mostUsedTag;
    private List<TagStatistic> systemDesignsByTag;
    
    // Category Statistics
    private Long totalCategories;
    private List<CategoryStatistic> systemDesignsByCategory;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceStatistic {
        private String sourceName;
        private Long systemDesignCount;
        private String uri; // URI to access system designs by this source
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatistic {
        private String labelName;
        private String categoryCode;
        private Long systemDesignCount;
        private String uri; // URI to access system designs by this tag
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStatistic {
        private String categoryId;
        private String categoryName;
        private String categoryCode;
        private Long systemDesignCount;
        private Long uniqueLabelsUsed;
        private List<String> topLabels;
    }
}
