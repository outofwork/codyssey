package com.codyssey.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for QuestionMedia with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMediaDto {

    private String id;
    
    private String questionId;
    
    private Integer sequence;
    
    private String mediaType;
    
    private String filePath;
    
    private String fileName;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
}