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

    @NotBlank(message = "Description is required")
    private String description;

    private String difficultyLabelId;

    @Size(max = 50, message = "Platform source must not exceed 50 characters")
    private String platformSource;

    @Size(max = 100, message = "Platform question ID must not exceed 100 characters")
    private String platformQuestionId;

    private String inputFormat;

    private String outputFormat;

    private String constraintsText;

    @Size(max = 100, message = "Time complexity hint must not exceed 100 characters")
    private String timeComplexityHint;

    @Size(max = 100, message = "Space complexity hint must not exceed 100 characters")
    private String spaceComplexityHint;

    private String status = "ACTIVE";

    private String createdByUserId;
}