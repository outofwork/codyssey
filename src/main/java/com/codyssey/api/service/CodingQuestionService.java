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
     * @param createDto question creation data
     * @return created question DTO
     */
    CodingQuestionDto createQuestion(CodingQuestionCreateDto createDto);

    /**
     * Get all coding questions (non-deleted)
     *
     * @return list of all questions
     */
    List<CodingQuestionSummaryDto> getAllQuestions();

    /**
     * Get all coding questions with pagination
     *
     * @param pageable pagination information
     * @return page of question summaries
     */
    Page<CodingQuestionSummaryDto> getAllQuestions(Pageable pageable);

    /**
     * Get coding question by ID
     *
     * @param id question ID
     * @return question DTO if found
     */
    Optional<CodingQuestionDto> getQuestionById(String id);

    /**
     * Update coding question
     *
     * @param id        question ID
     * @param updateDto updated question data
     * @return updated question DTO
     */
    CodingQuestionDto updateQuestion(String id, CodingQuestionUpdateDto updateDto);

    /**
     * Soft delete coding question
     *
     * @param id question ID
     */
    void deleteQuestion(String id);

    /**
     * Get questions by difficulty level
     *
     * @param difficultyLabelId the difficulty label ID
     * @return list of questions with the specified difficulty
     */
    List<CodingQuestionSummaryDto> getQuestionsByDifficulty(String difficultyLabelId);

    /**
     * Get questions by platform source
     *
     * @param platformSource the platform source
     * @return list of questions from the specified platform
     */
    List<CodingQuestionSummaryDto> getQuestionsByPlatform(String platformSource);

    /**
     * Search questions by title or description
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of matching questions
     */
    Page<CodingQuestionSummaryDto> searchQuestions(String searchTerm, Pageable pageable);

    /**
     * Get questions by label/tag
     *
     * @param labelId the label ID
     * @return list of questions tagged with the specified label
     */
    List<CodingQuestionSummaryDto> getQuestionsByLabel(String labelId);

    /**
     * Get questions asked by a specific company
     *
     * @param companyLabelId the company label ID
     * @return list of questions asked by the specified company
     */
    List<CodingQuestionSummaryDto> getQuestionsByCompany(String companyLabelId);

    /**
     * Get questions created by a specific user
     *
     * @param userId the user ID
     * @return list of questions created by the user
     */
    List<CodingQuestionSummaryDto> getQuestionsByUser(String userId);

    /**
     * Get active questions only
     *
     * @return list of active questions
     */
    List<CodingQuestionSummaryDto> getActiveQuestions();

    /**
     * Get questions by status with pagination
     *
     * @param status the question status
     * @param pageable pagination information
     * @return page of questions with the specified status
     */
    Page<CodingQuestionSummaryDto> getQuestionsByStatus(String status, Pageable pageable);

    /**
     * Check if a question title exists for a platform
     *
     * @param title the title to check
     * @param platformSource the platform source
     * @return true if title exists for the platform
     */
    boolean checkTitleAvailability(String title, String platformSource);

    /**
     * Get question statistics
     *
     * @param questionId the question ID
     * @return question statistics
     */
    com.codyssey.api.dto.question.QuestionStatisticsDto getQuestionStatistics(String questionId);
}