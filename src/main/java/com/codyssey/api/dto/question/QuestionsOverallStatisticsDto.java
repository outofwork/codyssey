package com.codyssey.api.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for comprehensive CodingQuestion statistics across all questions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsOverallStatisticsDto {

    // Overall Statistics
    private Long totalQuestions;
    private Long activeQuestions;
    private Long draftQuestions;
    private Long deprecatedQuestions;
    
    // Source Statistics
    private Long totalSources;
    private Long questionsWithSource;
    private Long questionsWithoutSource;
    private List<SourceStatistic> questionsBySource;
    
    // Difficulty Statistics
    private List<DifficultyStatistic> questionsByDifficulty;
    
    // Tag/Label Statistics
    private Long totalTags;
    private Long totalUniqueTagsUsed;
    private String mostUsedTag;
    private List<TagStatistic> questionsByTag;
    
    // Category Statistics
    private Long totalCategories;
    private List<CategoryStatistic> questionsByCategory;
    
    // Company Statistics
    private Long totalCompanies;
    private Long questionsWithCompanies;
    private Long questionsWithoutCompanies;
    private String mostAskingCompany;
    private List<CompanyStatistic> questionsByCompany;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceStatistic {
        private String sourceName;
        private Long questionCount;
        private String uri; // URI to access questions by this source
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DifficultyStatistic {
        private String difficultyName;
        private Long questionCount;
        private String uri; // URI to access questions by this difficulty
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatistic {
        private String labelName;
        private String categoryCode;
        private Long questionCount;
        private String uri; // URI to access questions by this tag
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStatistic {
        private String categoryId;
        private String categoryName;
        private String categoryCode;
        private Long questionCount;
        private Long uniqueLabelsUsed;
        private List<String> topLabels;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyStatistic {
        private String companyName;
        private Long questionCount;
        private String uri; // URI to access questions by this company
    }
}