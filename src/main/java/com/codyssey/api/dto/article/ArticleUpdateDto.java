package com.codyssey.api.dto.article;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an Article
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateDto {

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    private String articleType; // DATA_STRUCTURE, ALGORITHM, SYSTEM_DESIGN

    private String categoryLabelId;

    private String difficultyLabelId;

    private String status; // ACTIVE, DRAFT, DEPRECATED

    private Integer readingTimeMinutes;
    
    private String content;
    
    private String contentUrl;
    
    private String metaTitle;
    
    private String metaDescription;
    
    private String metaKeywords;
}