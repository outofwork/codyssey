package com.codyssey.api.dto.mcq;

import com.codyssey.api.model.MCQQuestion;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for updating MCQ Question
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQQuestionUpdateDto {

    @Size(max = 1000, message = "Question text must not exceed 1000 characters")
    private String questionText;

    @Size(max = 500, message = "Option A must not exceed 500 characters")
    private String optionA;

    @Size(max = 500, message = "Option B must not exceed 500 characters")
    private String optionB;

    @Size(max = 500, message = "Option C must not exceed 500 characters")
    private String optionC;

    @Size(max = 500, message = "Option D must not exceed 500 characters")
    private String optionD;

    @Pattern(regexp = "^[ABCD]$", message = "Correct answer must be A, B, C, or D")
    private String correctAnswer;

    @Size(max = 1000, message = "Explanation must not exceed 1000 characters")
    private String explanation;

    private String difficultyLabelId;

    private MCQQuestion.MCQStatus status;
}
