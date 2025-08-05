package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.Label;
import com.codyssey.api.model.QuestionLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionLabel entity
 * <p>
 * Provides data access methods for QuestionLabel entities (junction table).
 */
@Repository
public interface QuestionLabelRepository extends JpaRepository<QuestionLabel, String> {

    /**
     * Find all question-label associations by question ID
     *
     * @param questionId the question ID
     * @return List of question-label associations
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.question.id = :questionId")
    List<QuestionLabel> findByQuestionId(@Param("questionId") String questionId);

    /**
     * Find all question-label associations by label ID
     *
     * @param labelId the label ID
     * @return List of question-label associations
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.label.id = :labelId")
    List<QuestionLabel> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find question-label association by question and label
     *
     * @param question the question
     * @param label the label
     * @return Optional containing the association if found
     */
    Optional<QuestionLabel> findByQuestionAndLabel(CodingQuestion question, Label label);

    /**
     * Find question-label association by question ID and label ID
     *
     * @param questionId the question ID
     * @param labelId the label ID
     * @return Optional containing the association if found
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.label.id = :labelId")
    Optional<QuestionLabel> findByQuestionIdAndLabelId(@Param("questionId") String questionId, @Param("labelId") String labelId);

    /**
     * Check if a question-label association exists
     *
     * @param questionId the question ID
     * @param labelId the label ID
     * @return true if the association exists
     */
    @Query("SELECT COUNT(ql) > 0 FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.label.id = :labelId")
    boolean existsByQuestionIdAndLabelId(@Param("questionId") String questionId, @Param("labelId") String labelId);

    /**
     * Delete question-label association by question ID and label ID
     *
     * @param questionId the question ID
     * @param labelId the label ID
     */
    @Query("DELETE FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.label.id = :labelId")
    void deleteByQuestionIdAndLabelId(@Param("questionId") String questionId, @Param("labelId") String labelId);

    /**
     * Delete all question-label associations for a question
     *
     * @param questionId the question ID
     */
    @Query("DELETE FROM QuestionLabel ql WHERE ql.question.id = :questionId")
    void deleteByQuestionId(@Param("questionId") String questionId);

    /**
     * Get labels for a question
     *
     * @param questionId the question ID
     * @return List of labels for the question
     */
    @Query("SELECT ql.label FROM QuestionLabel ql WHERE ql.question.id = :questionId")
    List<Label> findLabelsByQuestionId(@Param("questionId") String questionId);

    /**
     * Get questions for a label
     *
     * @param labelId the label ID
     * @return List of questions with the label
     */
    @Query("SELECT ql.question FROM QuestionLabel ql WHERE ql.label.id = :labelId AND ql.question.deleted = false")
    List<CodingQuestion> findQuestionsByLabelId(@Param("labelId") String labelId);

    /**
     * Count labels for a question
     *
     * @param questionId the question ID
     * @return count of labels for the question
     */
    @Query("SELECT COUNT(ql) FROM QuestionLabel ql WHERE ql.question.id = :questionId")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count questions for a label
     *
     * @param labelId the label ID
     * @return count of questions with the label
     */
    @Query("SELECT COUNT(ql) FROM QuestionLabel ql WHERE ql.label.id = :labelId AND ql.question.deleted = false")
    Long countByLabelId(@Param("labelId") String labelId);
}