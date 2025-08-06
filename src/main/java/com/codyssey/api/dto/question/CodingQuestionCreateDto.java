package com.codyssey.api.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new CodingQuestion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingQuestionCreateDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @NotBlank(message = "File path is required")
    @Size(max = 500, message = "File path must not exceed 500 characters")
    private String filePath;

    private String difficultyLabelId;

    private String sourceId;

    @Size(max = 100, message = "Platform question ID must not exceed 100 characters")
    private String platformQuestionId;

    @Size(max = 500, message = "Original URL must not exceed 500 characters")
    private String originalUrl;

    private String status = "ACTIVE";

    private String createdByUserId;
}