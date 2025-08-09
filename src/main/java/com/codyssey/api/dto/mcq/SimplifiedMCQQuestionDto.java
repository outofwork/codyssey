package com.codyssey.api.dto.mcq;

import com.codyssey.api.model.MCQQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simplified Data Transfer Object for MCQ Question with clean response format
 * Contains only essential fields with simplified nested objects
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedMCQQuestionDto {

    private String id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String explanation;
    private SimplifiedLabelDto difficultyLabel;
    private MCQQuestion.MCQStatus status;
    private List<SimplifiedCategoryDto> mcqCategories;
    private List<SimplifiedLabelDto> mcqLabels;
    private LocalDateTime createdAt;
}
