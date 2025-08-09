package com.codyssey.api.dto.mcq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object for creating MCQ Question with multiple categories and labels
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQBulkCreateDto {

    @NotBlank(message = "Question text is required")
    @Size(max = 1000, message = "Question text must not exceed 1000 characters")
    private String questionText;

    @NotBlank(message = "Option A is required")
    @Size(max = 500, message = "Option A must not exceed 500 characters")
    private String optionA;

    @NotBlank(message = "Option B is required")
    @Size(max = 500, message = "Option B must not exceed 500 characters")
    private String optionB;

    @NotBlank(message = "Option C is required")
    @Size(max = 500, message = "Option C must not exceed 500 characters")
    private String optionC;

    @NotBlank(message = "Option D is required")
    @Size(max = 500, message = "Option D must not exceed 500 characters")
    private String optionD;

    @NotBlank(message = "Correct answer is required")
    @Pattern(regexp = "^[ABCD]$", message = "Correct answer must be A, B, C, or D")
    private String correctAnswer;

    @Size(max = 1000, message = "Explanation must not exceed 1000 characters")
    private String explanation;

    @NotEmpty(message = "At least one category is required")
    private List<String> categoryIds;

    private List<String> labelIds;

    private String difficultyLabelId;
}
