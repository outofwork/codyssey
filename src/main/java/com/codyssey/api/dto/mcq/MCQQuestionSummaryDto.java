package com.codyssey.api.dto.mcq;

import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.model.MCQQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Summary Data Transfer Object for MCQ Question (without sensitive data like correct answer)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQQuestionSummaryDto {

    private String id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private LabelSummaryDto difficultyLabel;
    private MCQQuestion.MCQStatus status;
    private List<MCQLabelReferenceDto> mcqLabels;
    private String urlSlug;
    private LocalDateTime createdAt;
}
