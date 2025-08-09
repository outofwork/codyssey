package com.codyssey.api.dto.mcq;

import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.model.MCQQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for MCQ Question
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQQuestionDto {

    private String id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String explanation;
    private LabelSummaryDto difficultyLabel;
    private MCQQuestion.MCQStatus status;
    private UserDto createdByUser;
    private List<MCQLabelReferenceDto> mcqLabels;
    private String urlSlug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
