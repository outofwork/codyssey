package com.codyssey.api.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for question statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionStatisticsDto {
    private String questionId;
    private Long solutionsCount;
    private Long testCasesCount;
    private Long mediaFilesCount;
    private Long labelsCount;
    private Long companiesCount;
    private Long outgoingLinksCount;
    private Long incomingLinksCount;
}