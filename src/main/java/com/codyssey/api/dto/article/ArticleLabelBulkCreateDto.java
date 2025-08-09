package com.codyssey.api.dto.article;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for creating multiple ArticleLabel relationships in bulk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabelBulkCreateDto {

    @NotEmpty(message = "At least one article-label relationship must be provided")
    @Valid
    private List<ArticleLabelCreateDto> articleLabels;
}