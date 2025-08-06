package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lightweight DTO for article listings (without full content)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummaryDto {
    
    private String id;
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private String slug;
    private List<String> tags;
    private String difficulty;
    private Integer readTime;
    private String author;
    private LocalDateTime lastModified;
    private String metaDescription;
}