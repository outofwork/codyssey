package com.codyssey.api.dto.article;

import com.codyssey.api.validation.ValidId;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating Article-Label relationship
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelCreateDto {

    @NotBlank(message = "Article ID is required")
    @ValidId(message = "Invalid article ID format")
    private String articleId;

    @NotBlank(message = "Label ID is required")
    @ValidId(message = "Invalid label ID format")
    private String labelId;
    
    private Integer displayOrder;
    
    private Boolean isPrimary;
}