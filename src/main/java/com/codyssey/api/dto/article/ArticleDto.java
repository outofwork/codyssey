package com.codyssey.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Article responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    
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
    private List<String> prerequisites;
    private List<String> relatedTopics;
    
    // Full content included when requested
    private String content;
    private String htmlContent;
}