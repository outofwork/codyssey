package com.codyssey.api.dto.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Source with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {
    
    private String id;
    
    private String code;
    
    private String name;
    
    private String baseUrl;
    
    private String description;
    
    private Boolean isActive;
    
    private String colorCode;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
    
    // Related statistics
    private Long questionsCount;
}