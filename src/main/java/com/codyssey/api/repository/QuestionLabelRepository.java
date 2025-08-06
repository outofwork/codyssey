package com.codyssey.api.repository;

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
 * Provides data access methods for QuestionLabel entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface QuestionLabelRepository extends JpaRepository<QuestionLabel, String> {

    /**
     * Find all question labels that are not soft deleted
     *
     * @return List of all non-deleted question labels
     */
    List<QuestionLabel> findByDeletedFalse();

    /**
     * Find question label by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the question label if found and not deleted
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.id = :id AND ql.deleted = false")
    Optional<QuestionLabel> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all labels for a specific question
     *
     * @param questionId the question ID
     * @return List of question labels for the question
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.deleted = false")
    List<QuestionLabel> findByQuestionId(@Param("questionId") String questionId);

    /**
     * Find primary labels for a specific question
     *
     * @param questionId the question ID
     * @return List of primary question labels for the question
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.isPrimary = true AND ql.deleted = false")
    List<QuestionLabel> findPrimaryLabelsByQuestionId(@Param("questionId") String questionId);

    /**
     * Find all questions that have a specific label
     *
     * @param labelId the label ID
     * @return List of question labels for the label
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.label.id = :labelId AND ql.deleted = false")
    List<QuestionLabel> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find labels by category for a specific question
     *
     * @param questionId the question ID
     * @param categoryCode the label category code
     * @return List of question labels in the specified category
     */
    @Query("SELECT ql FROM QuestionLabel ql JOIN ql.label l JOIN l.category c " +
           "WHERE ql.question.id = :questionId AND c.code = :categoryCode AND ql.deleted = false")
    List<QuestionLabel> findByQuestionIdAndCategory(@Param("questionId") String questionId, 
                                                    @Param("categoryCode") String categoryCode);

    /**
     * Find top labels for a question by relevance score
     *
     * @param questionId the question ID
     * @param limit maximum number of labels to return
     * @return List of question labels ordered by relevance score
     */
    @Query("SELECT ql FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.deleted = false " +
           "ORDER BY ql.relevanceScore DESC, ql.isPrimary DESC")
    List<QuestionLabel> findTopLabelsByQuestionId(@Param("questionId") String questionId, 
                                                  @Param("limit") int limit);

    /**
     * Find question-label relationship by question and label
     *
     * @param questionId the question ID
     * @param labelId the label ID
     * @return Optional containing the question label if found
     */
    @Query("SELECT ql FROM QuestionLabel ql " +
           "WHERE ql.question.id = :questionId AND ql.label.id = :labelId AND ql.deleted = false")
    Optional<QuestionLabel> findByQuestionIdAndLabelId(@Param("questionId") String questionId, @Param("labelId") String labelId);

    /**
     * Check if a question-label relationship exists
     *
     * @param questionId the question ID
     * @param labelId the label ID
     * @return true if relationship exists
     */
    @Query("SELECT COUNT(ql) > 0 FROM QuestionLabel ql " +
           "WHERE ql.question.id = :questionId AND ql.label.id = :labelId AND ql.deleted = false")
    boolean existsByQuestionIdAndLabelId(@Param("questionId") String questionId, @Param("labelId") String labelId);

    /**
     * Count labels for a specific question
     *
     * @param questionId the question ID
     * @return count of labels for the question
     */
    @Query("SELECT COUNT(ql) FROM QuestionLabel ql WHERE ql.question.id = :questionId AND ql.deleted = false")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count questions for a specific label
     *
     * @param labelId the label ID
     * @return count of questions for the label
     */
    @Query("SELECT COUNT(ql) FROM QuestionLabel ql WHERE ql.label.id = :labelId AND ql.deleted = false")
    Long countByLabelId(@Param("labelId") String labelId);

    /**
     * Find the most popular labels (labels used by most questions)
     *
     * @param limit maximum number of labels to return
     * @return List of question labels ordered by usage count
     */
    @Query("SELECT ql.label.id, ql.label.name, COUNT(ql) as usageCount " +
           "FROM QuestionLabel ql WHERE ql.deleted = false " +
           "GROUP BY ql.label.id, ql.label.name " +
           "ORDER BY usageCount DESC")
    List<Object[]> findMostPopularLabels(@Param("limit") int limit);

    /**
     * Find questions that have multiple specific labels (intersection)
     *
     * @param labelIds list of label IDs
     * @param minMatches minimum number of labels that must match
     * @return List of question IDs that have at least minMatches of the specified labels
     */
    @Query("SELECT ql.question.id FROM QuestionLabel ql " +
           "WHERE ql.label.id IN :labelIds AND ql.deleted = false " +
           "GROUP BY ql.question.id " +
           "HAVING COUNT(DISTINCT ql.label.id) >= :minMatches")
    List<String> findQuestionIdsByLabelIntersection(@Param("labelIds") List<String> labelIds, 
                                                    @Param("minMatches") long minMatches);

    /**
     * Delete all labels for a specific question
     *
     * @param questionId the question ID
     */
    @Query("UPDATE QuestionLabel ql SET ql.deleted = true WHERE ql.question.id = :questionId")
    void deleteByQuestionId(@Param("questionId") String questionId);
}