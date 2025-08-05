package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.QuestionTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionTestCase entity
 * <p>
 * Provides data access methods for QuestionTestCase entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface QuestionTestCaseRepository extends JpaRepository<QuestionTestCase, String> {

    /**
     * Find all test cases that are not soft deleted
     *
     * @return List of all non-deleted test cases
     */
    List<QuestionTestCase> findByDeletedFalse();

    /**
     * Find test case by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the test case if found and not deleted
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE tc.id = :id AND tc.deleted = false")
    Optional<QuestionTestCase> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find test cases by question ID ordered by sequence
     *
     * @param questionId the question ID
     * @return List of test cases for the question ordered by sequence
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.deleted = false ORDER BY tc.sequence")
    List<QuestionTestCase> findByQuestionIdOrderBySequence(@Param("questionId") String questionId);

    /**
     * Find test cases by question ordered by sequence
     *
     * @param question the question
     * @return List of test cases for the question ordered by sequence
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE tc.question = :question AND tc.deleted = false ORDER BY tc.sequence")
    List<QuestionTestCase> findByQuestionOrderBySequence(@Param("question") CodingQuestion question);

    /**
     * Find sample test cases for a question
     *
     * @param questionId the question ID
     * @return List of sample test cases for the question
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.isSample = true AND tc.deleted = false ORDER BY tc.sequence")
    List<QuestionTestCase> findSampleTestCasesByQuestionId(@Param("questionId") String questionId);

    /**
     * Find non-sample (hidden) test cases for a question
     *
     * @param questionId the question ID
     * @return List of hidden test cases for the question
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.isSample = false AND tc.deleted = false ORDER BY tc.sequence")
    List<QuestionTestCase> findHiddenTestCasesByQuestionId(@Param("questionId") String questionId);

    /**
     * Check if a test case exists for a question at a specific sequence
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if a test case exists at the sequence
     */
    @Query("SELECT COUNT(tc) > 0 FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.sequence = :sequence AND tc.deleted = false")
    boolean existsByQuestionIdAndSequence(@Param("questionId") String questionId, @Param("sequence") Integer sequence);

    /**
     * Check if a test case exists for a question at a specific sequence (excluding a specific test case ID for updates)
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @param excludeId the test case ID to exclude from the check
     * @return true if a test case exists at the sequence (excluding the specified ID)
     */
    @Query("SELECT COUNT(tc) > 0 FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.sequence = :sequence AND tc.deleted = false AND tc.id != :excludeId")
    boolean existsByQuestionIdAndSequenceExcludingId(@Param("questionId") String questionId, @Param("sequence") Integer sequence, @Param("excludeId") String excludeId);

    /**
     * Get the maximum sequence number for test cases of a question
     *
     * @param questionId the question ID
     * @return the maximum sequence number, or 0 if no test cases exist
     */
    @Query("SELECT COALESCE(MAX(tc.sequence), 0) FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.deleted = false")
    Integer findMaxSequenceByQuestionId(@Param("questionId") String questionId);

    /**
     * Count test cases by question ID
     *
     * @param questionId the question ID
     * @return count of test cases for the question
     */
    @Query("SELECT COUNT(tc) FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.deleted = false")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count sample test cases by question ID
     *
     * @param questionId the question ID
     * @return count of sample test cases for the question
     */
    @Query("SELECT COUNT(tc) FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.isSample = true AND tc.deleted = false")
    Long countSampleTestCasesByQuestionId(@Param("questionId") String questionId);

    /**
     * Count hidden test cases by question ID
     *
     * @param questionId the question ID
     * @return count of hidden test cases for the question
     */
    @Query("SELECT COUNT(tc) FROM QuestionTestCase tc WHERE tc.question.id = :questionId AND tc.isSample = false AND tc.deleted = false")
    Long countHiddenTestCasesByQuestionId(@Param("questionId") String questionId);

    /**
     * Delete all test cases for a question (soft delete)
     *
     * @param questionId the question ID
     */
    @Query("UPDATE QuestionTestCase tc SET tc.deleted = true WHERE tc.question.id = :questionId")
    void softDeleteByQuestionId(@Param("questionId") String questionId);

    /**
     * Find test cases with input data containing specific text
     *
     * @param searchTerm the search term
     * @return List of test cases with matching input data
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE LOWER(tc.inputData) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND tc.deleted = false")
    List<QuestionTestCase> findByInputDataContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Find test cases with expected output containing specific text
     *
     * @param searchTerm the search term
     * @return List of test cases with matching expected output
     */
    @Query("SELECT tc FROM QuestionTestCase tc WHERE LOWER(tc.expectedOutput) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND tc.deleted = false")
    List<QuestionTestCase> findByExpectedOutputContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}