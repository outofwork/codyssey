package com.codyssey.api.service;

import com.codyssey.api.dto.testcase.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for QuestionTestCase operations
 * <p>
 * Defines the contract for test case business logic operations.
 */
public interface QuestionTestCaseService {

    /**
     * Create a new test case
     *
     * @param createDto test case creation data
     * @return created test case DTO
     */
    QuestionTestCaseDto createTestCase(QuestionTestCaseCreateDto createDto);

    /**
     * Create multiple test cases in bulk
     *
     * @param bulkCreateDto bulk test case creation data
     * @return list of created test case DTOs
     */
    List<QuestionTestCaseDto> createTestCasesBulk(QuestionTestCaseBulkCreateDto bulkCreateDto);

    /**
     * Get all test cases (non-deleted)
     *
     * @return list of all test cases
     */
    List<QuestionTestCaseDto> getAllTestCases();

    /**
     * Get test case by ID
     *
     * @param id test case ID
     * @return test case DTO if found
     */
    Optional<QuestionTestCaseDto> getTestCaseById(String id);

    /**
     * Update test case
     *
     * @param id test case ID
     * @param updateDto updated test case data
     * @return updated test case DTO
     */
    QuestionTestCaseDto updateTestCase(String id, QuestionTestCaseUpdateDto updateDto);

    /**
     * Soft delete test case
     *
     * @param id test case ID
     */
    void deleteTestCase(String id);

    /**
     * Get test cases by question ID
     *
     * @param questionId the question ID
     * @return list of test cases ordered by sequence
     */
    List<QuestionTestCaseDto> getTestCasesByQuestionId(String questionId);

    /**
     * Get sample test cases by question ID
     *
     * @param questionId the question ID
     * @return list of sample test cases
     */
    List<QuestionTestCaseDto> getSampleTestCasesByQuestionId(String questionId);

    /**
     * Get hidden test cases by question ID
     *
     * @param questionId the question ID
     * @return list of hidden test cases
     */
    List<QuestionTestCaseDto> getHiddenTestCasesByQuestionId(String questionId);

    /**
     * Check if sequence is available
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if available
     */
    boolean checkSequenceAvailability(String questionId, Integer sequence);

    /**
     * Get next available sequence number
     *
     * @param questionId the question ID
     * @return next sequence number
     */
    Integer getNextSequenceNumber(String questionId);

    /**
     * Reorder test cases for a question
     *
     * @param questionId the question ID
     * @param testCaseSequences list of test case sequences
     */
    void reorderTestCases(String questionId, List<TestCaseSequenceDto> testCaseSequences);

    /**
     * Toggle sample status of a test case
     *
     * @param id test case ID
     * @param isSample whether the test case should be a sample
     * @return updated test case DTO
     */
    QuestionTestCaseDto toggleSampleStatus(String id, Boolean isSample);

    /**
     * Delete all test cases for a question
     *
     * @param questionId the question ID
     */
    void deleteAllTestCasesForQuestion(String questionId);

    /**
     * Get test cases count by question ID
     *
     * @param questionId the question ID
     * @return count of test cases
     */
    Long getTestCasesCountByQuestionId(String questionId);

    /**
     * Get sample test cases count by question ID
     *
     * @param questionId the question ID
     * @return count of sample test cases
     */
    Long getSampleTestCasesCountByQuestionId(String questionId);

    /**
     * Get hidden test cases count by question ID
     *
     * @param questionId the question ID
     * @return count of hidden test cases
     */
    Long getHiddenTestCasesCountByQuestionId(String questionId);

    /**
     * Search test cases by input data
     *
     * @param searchTerm the search term
     * @return list of matching test cases
     */
    List<QuestionTestCaseDto> searchTestCasesByInputData(String searchTerm);

    /**
     * Search test cases by expected output
     *
     * @param searchTerm the search term
     * @return list of matching test cases
     */
    List<QuestionTestCaseDto> searchTestCasesByExpectedOutput(String searchTerm);

    /**
     * Clone test cases from one question to another
     *
     * @param sourceQuestionId the source question ID
     * @param targetQuestionId the target question ID
     * @return list of cloned test cases
     */
    List<QuestionTestCaseDto> cloneTestCases(String sourceQuestionId, String targetQuestionId);
}