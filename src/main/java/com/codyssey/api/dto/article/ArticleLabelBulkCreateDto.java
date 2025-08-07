package com.codyssey.api.dto.article;

import com.codyssey.api.validation.ValidId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for creating multiple Article-Label relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelBulkCreateDto {

    @NotBlank(message = "Article ID is required")
    @ValidId(message = "Invalid article ID format")
    private String articleId;

    @NotEmpty(message = "At least one label ID is required")
    private List<String> labelIds;
}