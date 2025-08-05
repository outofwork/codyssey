package com.codyssey.api.dto.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a QuestionMedia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMediaCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotNull(message = "Sequence is required")
    private Integer sequence;

    @NotBlank(message = "Media type is required")
    private String mediaType; // IMAGE, DIAGRAM, VIDEO

    @NotBlank(message = "File path is required")
    @Size(max = 500, message = "File path must not exceed 500 characters")
    private String filePath;

    @NotBlank(message = "File name is required")
    @Size(max = 200, message = "File name must not exceed 200 characters")
    private String fileName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}