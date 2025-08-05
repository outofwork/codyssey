package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.QuestionSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionSolution entity
 * <p>
 * Provides data access methods for QuestionSolution entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface QuestionSolutionRepository extends JpaRepository<QuestionSolution, String> {

    /**
     * Find all solutions that are not soft deleted
     *
     * @return List of all non-deleted solutions
     */
    List<QuestionSolution> findByDeletedFalse();

    /**
     * Find solution by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the solution if found and not deleted
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.id = :id AND s.deleted = false")
    Optional<QuestionSolution> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find solutions by question ID ordered by sequence
     *
     * @param questionId the question ID
     * @return List of solutions for the question ordered by sequence
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.question.id = :questionId AND s.deleted = false ORDER BY s.sequence")
    List<QuestionSolution> findByQuestionIdOrderBySequence(@Param("questionId") String questionId);

    /**
     * Find solutions by question ordered by sequence
     *
     * @param question the question
     * @return List of solutions for the question ordered by sequence
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.question = :question AND s.deleted = false ORDER BY s.sequence")
    List<QuestionSolution> findByQuestionOrderBySequence(@Param("question") CodingQuestion question);

    /**
     * Find solutions by programming language
     *
     * @param language the programming language
     * @return List of solutions in the specified language
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.language = :language AND s.deleted = false")
    List<QuestionSolution> findByLanguage(@Param("language") String language);

    /**
     * Find optimal solutions for a question
     *
     * @param questionId the question ID
     * @return List of optimal solutions for the question
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.question.id = :questionId AND s.isOptimal = true AND s.deleted = false ORDER BY s.sequence")
    List<QuestionSolution> findOptimalSolutionsByQuestionId(@Param("questionId") String questionId);

    /**
     * Find solutions by question and language
     *
     * @param questionId the question ID
     * @param language the programming language
     * @return List of solutions for the question in the specified language
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.question.id = :questionId AND s.language = :language AND s.deleted = false ORDER BY s.sequence")
    List<QuestionSolution> findByQuestionIdAndLanguage(@Param("questionId") String questionId, @Param("language") String language);

    /**
     * Check if a solution exists for a question at a specific sequence
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if a solution exists at the sequence
     */
    @Query("SELECT COUNT(s) > 0 FROM QuestionSolution s WHERE s.question.id = :questionId AND s.sequence = :sequence AND s.deleted = false")
    boolean existsByQuestionIdAndSequence(@Param("questionId") String questionId, @Param("sequence") Integer sequence);

    /**
     * Check if a solution exists for a question at a specific sequence (excluding a specific solution ID for updates)
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @param excludeId the solution ID to exclude from the check
     * @return true if a solution exists at the sequence (excluding the specified ID)
     */
    @Query("SELECT COUNT(s) > 0 FROM QuestionSolution s WHERE s.question.id = :questionId AND s.sequence = :sequence AND s.deleted = false AND s.id != :excludeId")
    boolean existsByQuestionIdAndSequenceExcludingId(@Param("questionId") String questionId, @Param("sequence") Integer sequence, @Param("excludeId") String excludeId);

    /**
     * Find solutions created by a specific user
     *
     * @param userId the user ID
     * @return List of solutions created by the user
     */
    @Query("SELECT s FROM QuestionSolution s WHERE s.createdByUser.id = :userId AND s.deleted = false")
    List<QuestionSolution> findByCreatedByUserId(@Param("userId") String userId);

    /**
     * Get the maximum sequence number for a question
     *
     * @param questionId the question ID
     * @return the maximum sequence number, or 0 if no solutions exist
     */
    @Query("SELECT COALESCE(MAX(s.sequence), 0) FROM QuestionSolution s WHERE s.question.id = :questionId AND s.deleted = false")
    Integer findMaxSequenceByQuestionId(@Param("questionId") String questionId);

    /**
     * Count solutions by question ID
     *
     * @param questionId the question ID
     * @return count of solutions for the question
     */
    @Query("SELECT COUNT(s) FROM QuestionSolution s WHERE s.question.id = :questionId AND s.deleted = false")
    Long countByQuestionId(@Param("questionId") String questionId);
}