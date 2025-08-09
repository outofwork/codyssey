package com.codyssey.api.repository;

import com.codyssey.api.model.MCQQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MCQQuestion entity
 */
@Repository
public interface MCQQuestionRepository extends JpaRepository<MCQQuestion, String> {

    /**
     * Find MCQ questions by status
     */
    List<MCQQuestion> findByStatus(MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by status with pagination
     */
    Page<MCQQuestion> findByStatus(MCQQuestion.MCQStatus status, Pageable pageable);

    /**
     * Find MCQ questions by difficulty label
     */
    @Query("SELECT mcq FROM MCQQuestion mcq WHERE mcq.difficultyLabel.id = :difficultyLabelId AND mcq.status = :status")
    List<MCQQuestion> findByDifficultyLabelAndStatus(@Param("difficultyLabelId") String difficultyLabelId, 
                                                     @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by label (through MCQLabel relationship)
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE ml.label.id = :labelId AND mcq.status = :status")
    List<MCQQuestion> findByLabelAndStatus(@Param("labelId") String labelId, 
                                          @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by label with pagination
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE ml.label.id = :labelId AND mcq.status = :status")
    Page<MCQQuestion> findByLabelAndStatus(@Param("labelId") String labelId, 
                                          @Param("status") MCQQuestion.MCQStatus status,
                                          Pageable pageable);

    /**
     * Find MCQ questions by label hierarchy (including parent and child labels)
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE (ml.label.id = :labelId OR ml.label.parent.id = :labelId OR ml.label.id IN " +
           "(SELECT child.id FROM Label child WHERE child.parent.id = :labelId)) " +
           "AND mcq.status = :status")
    List<MCQQuestion> findByLabelHierarchyAndStatus(@Param("labelId") String labelId, 
                                                    @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by label hierarchy with pagination
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE (ml.label.id = :labelId OR ml.label.parent.id = :labelId OR ml.label.id IN " +
           "(SELECT child.id FROM Label child WHERE child.parent.id = :labelId)) " +
           "AND mcq.status = :status")
    Page<MCQQuestion> findByLabelHierarchyAndStatus(@Param("labelId") String labelId, 
                                                    @Param("status") MCQQuestion.MCQStatus status,
                                                    Pageable pageable);

    /**
     * Find random MCQ questions by label
     */
    @Query(value = "SELECT DISTINCT mcq.* FROM mcq_questions mcq " +
                   "JOIN mcq_labels ml ON mcq.id = ml.mcq_question_id " +
                   "WHERE ml.label_id = :labelId AND mcq.status = :status " +
                   "ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<MCQQuestion> findRandomByLabel(@Param("labelId") String labelId, 
                                       @Param("status") String status, 
                                       @Param("limit") int limit);

    /**
     * Find random MCQ questions by label hierarchy
     */
    @Query(value = "SELECT DISTINCT mcq.* FROM mcq_questions mcq " +
                   "JOIN mcq_labels ml ON mcq.id = ml.mcq_question_id " +
                   "JOIN labels l ON ml.label_id = l.id " +
                   "WHERE (l.id = :labelId OR l.parent_id = :labelId OR " +
                   "l.id IN (SELECT id FROM labels WHERE parent_id = :labelId)) " +
                   "AND mcq.status = :status " +
                   "ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<MCQQuestion> findRandomByLabelHierarchy(@Param("labelId") String labelId, 
                                                @Param("status") String status, 
                                                @Param("limit") int limit);

    /**
     * Find by URL slug
     */
    Optional<MCQQuestion> findByUrlSlug(String urlSlug);

    /**
     * Check if URL slug exists
     */
    boolean existsByUrlSlug(String urlSlug);

    /**
     * Count MCQ questions by label
     */
    @Query("SELECT COUNT(DISTINCT mcq) FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE ml.label.id = :labelId AND mcq.status = :status")
    long countByLabelAndStatus(@Param("labelId") String labelId, @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Count MCQ questions by label hierarchy
     */
    @Query("SELECT COUNT(DISTINCT mcq) FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE (ml.label.id = :labelId OR ml.label.parent.id = :labelId OR ml.label.id IN " +
           "(SELECT child.id FROM Label child WHERE child.parent.id = :labelId)) " +
           "AND mcq.status = :status")
    long countByLabelHierarchyAndStatus(@Param("labelId") String labelId, @Param("status") MCQQuestion.MCQStatus status);
}
