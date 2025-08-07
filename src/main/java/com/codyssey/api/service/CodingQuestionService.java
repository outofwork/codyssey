package com.codyssey.api.service;

import com.codyssey.api.dto.question.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CodingQuestion operations
 * <p>
 * Defines the contract for coding question-related business logic operations.
 */
public interface CodingQuestionService {

    /**
     * Create a new coding question
     *
     * @param createDto coding question creation data
     * @return created coding question DTO
     */
    CodingQuestionDto createQuestion(CodingQuestionCreateDto createDto);

    /**
     * Get all coding questions (non-deleted)
     *
     * @return list of all coding questions
     */
    List<CodingQuestionSummaryDto> getAllQuestions();

    /**
     * Get coding question by ID
     *
     * @param id coding question ID
     * @return coding question DTO if found
     */
    Optional<CodingQuestionDto> getQuestionById(String id);

    /**
     * Get coding question by URL slug
     *
     * @param urlSlug coding question URL slug
     * @return coding question DTO if found
     */
    Optional<CodingQuestionDto> getQuestionByUrlSlug(String urlSlug);

    /**
     * Update coding question
     *
     * @param id coding question ID
     * @param updateDto updated coding question data
     * @return updated coding question DTO
     */
    CodingQuestionDto updateQuestion(String id, CodingQuestionUpdateDto updateDto);

    /**
     * Update coding question by URL slug
     *
     * @param urlSlug coding question URL slug
     * @param updateDto updated coding question data
     * @return updated coding question DTO
     */
    CodingQuestionDto updateQuestionByUrlSlug(String urlSlug, CodingQuestionUpdateDto updateDto);

    /**
     * Soft delete coding question
     *
     * @param id coding question ID
     */
    void deleteQuestion(String id);

    /**
     * Soft delete coding question by URL slug
     *
     * @param urlSlug coding question URL slug
     */
    void deleteQuestionByUrlSlug(String urlSlug);

    /**
     * Get coding questions with pagination
     *
     * @param pageable pagination information
     * @return paginated coding questions
     */
    Page<CodingQuestionSummaryDto> getQuestionsWithPagination(Pageable pageable);

    /**
     * Search coding questions by title or description
     *
     * @param searchTerm search term
     * @param pageable pagination information
     * @return paginated search results
     */
    Page<CodingQuestionSummaryDto> searchQuestions(String searchTerm, Pageable pageable);

    /**
     * Get coding questions by source
     *
     * @param sourceId source ID
     * @return list of questions from the source
     */
    List<CodingQuestionSummaryDto> getQuestionsBySource(String sourceId);

    /**
     * Get coding questions by difficulty label
     *
     * @param difficultyLabelId difficulty label ID
     * @return list of questions with the specified difficulty
     */
    List<CodingQuestionSummaryDto> getQuestionsByDifficulty(String difficultyLabelId);

    /**
     * Get coding questions by label/tag
     *
     * @param labelId label ID
     * @return list of questions tagged with the specified label
     */
    List<CodingQuestionSummaryDto> getQuestionsByLabel(String labelId);

    /**
     * Get coding questions by company
     *
     * @param companyLabelId company label ID
     * @return list of questions asked by the specified company
     */
    List<CodingQuestionSummaryDto> getQuestionsByCompany(String companyLabelId);

    /**
     * Advanced search with multiple filters
     *
     * @param sourceIds list of source IDs to filter by
     * @param difficultyIds list of difficulty label IDs to filter by
     * @param labelIds list of label IDs to filter by
     * @param companyIds list of company label IDs to filter by
     * @param searchTerm search term for title/description
     * @return list of questions matching the filters
     */
    List<CodingQuestionSummaryDto> searchWithFilters(List<String> sourceIds,
                                                     List<String> difficultyIds,
                                                     List<String> labelIds,
                                                     List<String> companyIds,
                                                     String searchTerm);

    /**
     * Get question statistics
     *
     * @param questionId question ID
     * @return question statistics
     */
    QuestionStatisticsDto getQuestionStatistics(String questionId);

    /**
     * Check if a question title is available within a source
     *
     * @param title question title
     * @param sourceId source ID
     * @return true if title is available
     */
    boolean checkTitleAvailability(String title, String sourceId);

    /**
     * Add a label to a question
     *
     * @param createDto question-label relationship data
     */
    void addLabelToQuestion(QuestionLabelCreateDto createDto);

    /**
     * Add multiple labels to a question
     *
     * @param bulkCreateDto bulk question-label relationship data
     */
    void addLabelsToQuestion(QuestionLabelBulkCreateDto bulkCreateDto);

    /**
     * Remove a label from a question
     *
     * @param questionId question ID
     * @param labelId label ID
     */
    void removeLabelFromQuestion(String questionId, String labelId);

    /**
     * Add a company to a question
     *
     * @param createDto question-company relationship data
     */
    void addCompanyToQuestion(QuestionCompanyCreateDto createDto);

    /**
     * Remove a company from a question
     *
     * @param questionId question ID
     * @param companyLabelId company label ID
     */
    void removeCompanyFromQuestion(String questionId, String companyLabelId);

    /**
     * Get the markdown content of a coding question
     *
     * @param id coding question ID
     * @return markdown content of the question
     * @throws Exception if file cannot be read
     */
    String getQuestionContent(String id) throws Exception;
}