package com.codyssey.api.dto.source;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Source
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceCreateDto {
    
    @NotBlank(message = "Source code is required")
    @Size(max = 50, message = "Source code must not exceed 50 characters")
    private String code;
    
    @NotBlank(message = "Source name is required")
    @Size(max = 100, message = "Source name must not exceed 100 characters")
    private String name;
    
    @Size(max = 200, message = "Base URL must not exceed 200 characters")
    private String baseUrl;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    private Boolean isActive = true;
    
    @Size(max = 7, message = "Color code must not exceed 7 characters")
    private String colorCode;
}