package com.codyssey.api.service;

import com.codyssey.api.dto.question.QuestionLabelCreateDto;
import com.codyssey.api.dto.question.QuestionLabelBulkCreateDto;
import com.codyssey.api.dto.question.LabelUsageDto;
import com.codyssey.api.dto.label.LabelSummaryDto;

import java.util.List;

/**
 * Service interface for QuestionLabel operations
 * <p>
 * Defines the contract for question-label association business logic operations.
 */
public interface QuestionLabelService {

    /**
     * Create a new question-label association
     *
     * @param createDto association creation data
     * @return success message
     */
    String createQuestionLabel(QuestionLabelCreateDto createDto);

    /**
     * Create multiple question-label associations in bulk
     *
     * @param bulkCreateDto bulk association creation data
     * @return list of success/error messages
     */
    List<String> createQuestionLabelsBulk(QuestionLabelBulkCreateDto bulkCreateDto);

    /**
     * Get all labels for a question
     *
     * @param questionId the question ID
     * @return list of labels associated with the question
     */
    List<LabelSummaryDto> getLabelsByQuestionId(String questionId);

    /**
     * Get all question IDs for a label
     *
     * @param labelId the label ID
     * @return list of question IDs
     */
    List<String> getQuestionIdsByLabelId(String labelId);

    /**
     * Remove a label from a question
     *
     * @param questionId the question ID
     * @param labelId the label ID
     */
    void removeQuestionLabel(String questionId, String labelId);

    /**
     * Remove all labels from a question
     *
     * @param questionId the question ID
     */
    void removeAllLabelsFromQuestion(String questionId);

    /**
     * Check if a question-label association exists
     *
     * @param questionId the question ID
     * @param labelId the label ID
     * @return true if association exists
     */
    boolean isQuestionLabelExists(String questionId, String labelId);

    /**
     * Replace all labels for a question
     *
     * @param questionId the question ID
     * @param labelIds list of new label IDs
     * @return list of new labels
     */
    List<LabelSummaryDto> replaceQuestionLabels(String questionId, List<String> labelIds);

    /**
     * Add labels to a question
     *
     * @param questionId the question ID
     * @param labelIds list of label IDs to add
     * @return updated list of labels
     */
    List<LabelSummaryDto> addLabelsToQuestion(String questionId, List<String> labelIds);

    /**
     * Remove labels from a question
     *
     * @param questionId the question ID
     * @param labelIds list of label IDs to remove
     * @return updated list of labels
     */
    List<LabelSummaryDto> removeLabelsFromQuestion(String questionId, List<String> labelIds);

    /**
     * Get count of labels for a question
     *
     * @param questionId the question ID
     * @return number of labels
     */
    Long getLabelsCountByQuestionId(String questionId);

    /**
     * Get count of questions for a label
     *
     * @param labelId the label ID
     * @return number of questions
     */
    Long getQuestionsCountByLabelId(String labelId);

    /**
     * Get most used labels
     *
     * @param limit maximum number of labels to return
     * @return list of label usage statistics
     */
    List<LabelUsageDto> getMostUsedLabels(int limit);
}