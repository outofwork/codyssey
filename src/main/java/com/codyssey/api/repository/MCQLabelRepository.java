package com.codyssey.api.repository;

import com.codyssey.api.model.MCQLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MCQLabel entity
 */
@Repository
public interface MCQLabelRepository extends JpaRepository<MCQLabel, String> {

    /**
     * Find MCQ labels by MCQ question ID
     */
    List<MCQLabel> findByMcqQuestionId(String mcqQuestionId);

    /**
     * Find MCQ labels by label ID
     */
    List<MCQLabel> findByLabelId(String labelId);

    /**
     * Find MCQ labels by MCQ question ID and label ID
     */
    Optional<MCQLabel> findByMcqQuestionIdAndLabelId(String mcqQuestionId, String labelId);

    /**
     * Find primary MCQ labels for a question
     */
    List<MCQLabel> findByMcqQuestionIdAndIsPrimary(String mcqQuestionId, Boolean isPrimary);

    /**
     * Find MCQ labels by relevance score
     */
    @Query("SELECT ml FROM MCQLabel ml WHERE ml.mcqQuestion.id = :mcqQuestionId AND ml.relevanceScore >= :minScore ORDER BY ml.relevanceScore DESC")
    List<MCQLabel> findByMcqQuestionIdAndRelevanceScoreGreaterThanEqual(@Param("mcqQuestionId") String mcqQuestionId, 
                                                                        @Param("minScore") Integer minScore);

    /**
     * Delete MCQ labels by MCQ question ID
     */
    void deleteByMcqQuestionId(String mcqQuestionId);

    /**
     * Delete MCQ labels by label ID
     */
    void deleteByLabelId(String labelId);

    /**
     * Check if MCQ label exists
     */
    boolean existsByMcqQuestionIdAndLabelId(String mcqQuestionId, String labelId);

    /**
     * Count MCQ labels by MCQ question ID
     */
    long countByMcqQuestionId(String mcqQuestionId);

    /**
     * Count MCQ labels by label ID
     */
    long countByLabelId(String labelId);
}
