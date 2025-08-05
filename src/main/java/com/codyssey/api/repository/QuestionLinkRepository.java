package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.QuestionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionLink entity
 * <p>
 * Provides data access methods for QuestionLink entities for managing
 * relationships between coding questions.
 */
@Repository
public interface QuestionLinkRepository extends JpaRepository<QuestionLink, String> {

    /**
     * Find all outgoing links from a source question ordered by sequence
     *
     * @param sourceQuestionId the source question ID
     * @return List of outgoing links ordered by sequence
     */
    @Query("SELECT ql FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId ORDER BY ql.sequence")
    List<QuestionLink> findBySourceQuestionIdOrderBySequence(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Find all incoming links to a target question
     *
     * @param targetQuestionId the target question ID
     * @return List of incoming links
     */
    @Query("SELECT ql FROM QuestionLink ql WHERE ql.targetQuestion.id = :targetQuestionId")
    List<QuestionLink> findByTargetQuestionId(@Param("targetQuestionId") String targetQuestionId);

    /**
     * Find links by relationship type from a source question
     *
     * @param sourceQuestionId the source question ID
     * @param relationshipType the relationship type
     * @return List of links with the specified relationship type
     */
    @Query("SELECT ql FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND ql.relationshipType = :relationshipType ORDER BY ql.sequence")
    List<QuestionLink> findBySourceQuestionIdAndRelationshipType(@Param("sourceQuestionId") String sourceQuestionId, 
                                                                 @Param("relationshipType") QuestionLink.RelationshipType relationshipType);

    /**
     * Find link by source question, target question, and relationship type
     *
     * @param sourceQuestion the source question
     * @param targetQuestion the target question
     * @param relationshipType the relationship type
     * @return Optional containing the link if found
     */
    Optional<QuestionLink> findBySourceQuestionAndTargetQuestionAndRelationshipType(
        CodingQuestion sourceQuestion, CodingQuestion targetQuestion, QuestionLink.RelationshipType relationshipType);

    /**
     * Check if a link exists between two questions with a specific relationship type
     *
     * @param sourceQuestionId the source question ID
     * @param targetQuestionId the target question ID
     * @param relationshipType the relationship type
     * @return true if the link exists
     */
    @Query("SELECT COUNT(ql) > 0 FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND " +
           "ql.targetQuestion.id = :targetQuestionId AND ql.relationshipType = :relationshipType")
    boolean existsBySourceQuestionIdAndTargetQuestionIdAndRelationshipType(
        @Param("sourceQuestionId") String sourceQuestionId, 
        @Param("targetQuestionId") String targetQuestionId, 
        @Param("relationshipType") QuestionLink.RelationshipType relationshipType);

    /**
     * Check if a link exists at a specific sequence for a source question
     *
     * @param sourceQuestionId the source question ID
     * @param sequence the sequence number
     * @return true if a link exists at the sequence
     */
    @Query("SELECT COUNT(ql) > 0 FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND ql.sequence = :sequence")
    boolean existsBySourceQuestionIdAndSequence(@Param("sourceQuestionId") String sourceQuestionId, @Param("sequence") Integer sequence);

    /**
     * Check if a link exists at a specific sequence for a source question (excluding a specific link ID for updates)
     *
     * @param sourceQuestionId the source question ID
     * @param sequence the sequence number
     * @param excludeId the link ID to exclude from the check
     * @return true if a link exists at the sequence (excluding the specified ID)
     */
    @Query("SELECT COUNT(ql) > 0 FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND " +
           "ql.sequence = :sequence AND ql.id != :excludeId")
    boolean existsBySourceQuestionIdAndSequenceExcludingId(@Param("sourceQuestionId") String sourceQuestionId, 
                                                           @Param("sequence") Integer sequence, 
                                                           @Param("excludeId") String excludeId);

    /**
     * Delete all links for a question (both as source and target)
     *
     * @param questionId the question ID
     */
    @Query("DELETE FROM QuestionLink ql WHERE ql.sourceQuestion.id = :questionId OR ql.targetQuestion.id = :questionId")
    void deleteByQuestionId(@Param("questionId") String questionId);

    /**
     * Get follow-up questions for a source question
     *
     * @param sourceQuestionId the source question ID
     * @return List of follow-up questions ordered by sequence
     */
    @Query("SELECT ql.targetQuestion FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND " +
           "ql.relationshipType = 'FOLLOW_UP' AND ql.targetQuestion.deleted = false ORDER BY ql.sequence")
    List<CodingQuestion> findFollowUpQuestions(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Get similar questions for a source question
     *
     * @param sourceQuestionId the source question ID
     * @return List of similar questions ordered by sequence
     */
    @Query("SELECT ql.targetQuestion FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND " +
           "ql.relationshipType = 'SIMILAR' AND ql.targetQuestion.deleted = false ORDER BY ql.sequence")
    List<CodingQuestion> findSimilarQuestions(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Get prerequisite questions for a source question
     *
     * @param sourceQuestionId the source question ID
     * @return List of prerequisite questions ordered by sequence
     */
    @Query("SELECT ql.targetQuestion FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId AND " +
           "ql.relationshipType = 'PREREQUISITE' AND ql.targetQuestion.deleted = false ORDER BY ql.sequence")
    List<CodingQuestion> findPrerequisiteQuestions(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Get the maximum sequence number for links from a source question
     *
     * @param sourceQuestionId the source question ID
     * @return the maximum sequence number, or 0 if no links exist
     */
    @Query("SELECT COALESCE(MAX(ql.sequence), 0) FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId")
    Integer findMaxSequenceBySourceQuestionId(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Count outgoing links from a question
     *
     * @param sourceQuestionId the source question ID
     * @return count of outgoing links
     */
    @Query("SELECT COUNT(ql) FROM QuestionLink ql WHERE ql.sourceQuestion.id = :sourceQuestionId")
    Long countBySourceQuestionId(@Param("sourceQuestionId") String sourceQuestionId);

    /**
     * Count incoming links to a question
     *
     * @param targetQuestionId the target question ID
     * @return count of incoming links
     */
    @Query("SELECT COUNT(ql) FROM QuestionLink ql WHERE ql.targetQuestion.id = :targetQuestionId")
    Long countByTargetQuestionId(@Param("targetQuestionId") String targetQuestionId);
}