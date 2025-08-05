package com.codyssey.api.service;

import com.codyssey.api.dto.solution.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for QuestionSolution operations
 * <p>
 * Defines the contract for question solution-related business logic operations.
 */
public interface QuestionSolutionService {

    /**
     * Create a new solution
     *
     * @param createDto solution creation data
     * @return created solution DTO
     */
    QuestionSolutionDto createSolution(QuestionSolutionCreateDto createDto);

    /**
     * Create multiple solutions in bulk
     *
     * @param bulkCreateDto bulk solution creation data
     * @return list of created solution DTOs
     */
    List<QuestionSolutionDto> createSolutionsBulk(QuestionSolutionBulkCreateDto bulkCreateDto);

    /**
     * Get all solutions (non-deleted)
     *
     * @return list of all solutions
     */
    List<QuestionSolutionDto> getAllSolutions();

    /**
     * Get solution by ID
     *
     * @param id solution ID
     * @return solution DTO if found
     */
    Optional<QuestionSolutionDto> getSolutionById(String id);

    /**
     * Update solution
     *
     * @param id        solution ID
     * @param updateDto updated solution data
     * @return updated solution DTO
     */
    QuestionSolutionDto updateSolution(String id, QuestionSolutionUpdateDto updateDto);

    /**
     * Soft delete solution
     *
     * @param id solution ID
     */
    void deleteSolution(String id);

    /**
     * Get solutions by question ID ordered by sequence
     *
     * @param questionId the question ID
     * @return list of solutions for the question
     */
    List<QuestionSolutionDto> getSolutionsByQuestionId(String questionId);

    /**
     * Get solutions by programming language
     *
     * @param language the programming language
     * @return list of solutions in the specified language
     */
    List<QuestionSolutionDto> getSolutionsByLanguage(String language);

    /**
     * Get optimal solutions for a question
     *
     * @param questionId the question ID
     * @return list of optimal solutions for the question
     */
    List<QuestionSolutionDto> getOptimalSolutionsByQuestionId(String questionId);

    /**
     * Get solutions by question and language
     *
     * @param questionId the question ID
     * @param language the programming language
     * @return list of solutions for the question in the specified language
     */
    List<QuestionSolutionDto> getSolutionsByQuestionIdAndLanguage(String questionId, String language);

    /**
     * Get solutions created by a specific user
     *
     * @param userId the user ID
     * @return list of solutions created by the user
     */
    List<QuestionSolutionDto> getSolutionsByUser(String userId);

    /**
     * Check if a sequence number is available for a question
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if sequence is available
     */
    boolean checkSequenceAvailability(String questionId, Integer sequence);

    /**
     * Get next available sequence number for a question
     *
     * @param questionId the question ID
     * @return next available sequence number
     */
    Integer getNextSequenceNumber(String questionId);

    /**
     * Reorder solutions for a question
     *
     * @param questionId the question ID
     * @param solutionSequences list of solution IDs with their new sequences
     */
    void reorderSolutions(String questionId, List<com.codyssey.api.dto.solution.SolutionSequenceDto> solutionSequences);

    /**
     * Mark solution as optimal/non-optimal
     *
     * @param id solution ID
     * @param isOptimal whether the solution is optimal
     * @return updated solution DTO
     */
    QuestionSolutionDto markSolutionOptimal(String id, Boolean isOptimal);
}