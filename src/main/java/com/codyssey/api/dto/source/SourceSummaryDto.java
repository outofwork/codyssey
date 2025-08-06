package com.codyssey.api.dto.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Source summary information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceSummaryDto {
    
    private String id;
    
    private String code;
    
    private String name;
    
    private String baseUrl;
    
    private String colorCode;
}