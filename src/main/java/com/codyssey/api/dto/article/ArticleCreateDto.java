package com.codyssey.api.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Article
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @NotNull(message = "Article type is required")
    private String articleType; // DATA_STRUCTURE, ALGORITHM, SYSTEM_DESIGN

    private String categoryLabelId;

    private String difficultyLabelId;

    private String status = "ACTIVE";

    private String createdByUserId;

    private Integer readingTimeMinutes;
    
    private String content;
    
    private String contentUrl;
    
    private String metaTitle;
    
    private String metaDescription;
    
    private String metaKeywords;
}