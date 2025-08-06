package com.codyssey.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Article model representing a markdown-based learning article
 * Articles are stored as files and indexed for quick retrieval
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    
    private String id;                    // Unique identifier (e.g., "arrays", "binary-search")
    private String title;                 // Display title
    private String description;           // Short description/summary
    private String category;              // Category (data-structures, algorithms, etc.)
    private String subcategory;           // Optional subcategory
    private String filePath;              // Path to the .md file
    private String slug;                  // URL-friendly identifier
    private List<String> tags;            // Tags for filtering/search
    private String difficulty;            // Difficulty level (Beginner, Intermediate, Advanced)
    private Integer readTime;             // Estimated read time in minutes
    private String author;                // Author name
    private LocalDateTime lastModified;   // Last modification timestamp
    private Boolean published;            // Publication status
    private String metaDescription;       // SEO meta description
    private List<String> prerequisites;   // List of prerequisite topics
    private List<String> relatedTopics;   // Related article IDs
    
    // Content will be loaded dynamically from the markdown file
    private transient String content;     // The actual markdown content
    private transient String htmlContent; // Converted HTML content (if needed)
}