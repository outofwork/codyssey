package com.codyssey.api.dto.source;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing Source
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceUpdateDto {
    
    @Size(max = 100, message = "Source name must not exceed 100 characters")
    private String name;
    
    @Size(max = 200, message = "Base URL must not exceed 200 characters")
    private String baseUrl;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    private Boolean isActive;
    
    @Size(max = 7, message = "Color code must not exceed 7 characters")
    private String colorCode;
}