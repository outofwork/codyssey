package com.codyssey.api.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a QuestionLabel association
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLabelCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Label ID is required")
    private String labelId;
}