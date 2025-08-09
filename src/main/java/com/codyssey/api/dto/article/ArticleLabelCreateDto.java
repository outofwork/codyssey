package com.codyssey.api.dto.article;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new ArticleLabel relationship
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelCreateDto {

    @NotBlank(message = "Article ID is required")
    private String articleId;

    @NotBlank(message = "Label ID is required")
    private String labelId;

    @Min(value = 1, message = "Relevance score must be between 1 and 10")
    @Max(value = 10, message = "Relevance score must be between 1 and 10")
    private Integer relevanceScore = 5;

    private Boolean isPrimary = false;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}