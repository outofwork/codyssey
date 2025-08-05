package com.codyssey.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating media files
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMediaUpdateDto {
    private Integer sequence;
    private String mediaType;
    private String filePath;
    private String fileName;
    private String description;
}