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

    /**
     * Find MCQ questions by category (through MCQCategory relationship)
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqCategories mc " +
           "WHERE mc.category.id = :categoryId AND mcq.status = :status")
    List<MCQQuestion> findByCategoryAndStatus(@Param("categoryId") String categoryId, 
                                             @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by category with pagination (through MCQCategory relationship)
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqCategories mc " +
           "WHERE mc.category.id = :categoryId AND mcq.status = :status")
    Page<MCQQuestion> findByCategoryAndStatus(@Param("categoryId") String categoryId, 
                                             @Param("status") MCQQuestion.MCQStatus status,
                                             Pageable pageable);

    /**
     * Count MCQ questions by category (through MCQCategory relationship)
     */
    @Query("SELECT COUNT(DISTINCT mcq) FROM MCQQuestion mcq " +
           "JOIN mcq.mcqCategories mc " +
           "WHERE mc.category.id = :categoryId AND mcq.status = :status")
    long countByCategoryAndStatus(@Param("categoryId") String categoryId, @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find random MCQ questions by category (through MCQCategory relationship)
     */
    @Query(value = "SELECT DISTINCT mcq.* FROM mcq_questions mcq " +
                   "JOIN mcq_categories mc ON mcq.id = mc.mcq_question_id " +
                   "WHERE mc.category_id = :categoryId AND mcq.status = :status " +
                   "ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<MCQQuestion> findRandomByCategory(@Param("categoryId") String categoryId, 
                                          @Param("status") String status, 
                                          @Param("limit") int limit);

    /**
     * Find MCQ questions accessible through label hierarchy (including category and all parent labels)
     * This query returns MCQs that should be available when querying a specific label,
     * including MCQs from the label's category and parent labels
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "LEFT JOIN mcq.mcqLabels ml " +
           "LEFT JOIN mcq.mcqCategories mc " +
           "LEFT JOIN ml.label l " +
           "WHERE (mc.category.id = :categoryId OR " +
           "       ml.label.id = :labelId OR " +
           "       ml.label.parent.id = :labelId OR " +
           "       ml.label.id IN (SELECT child.id FROM Label child WHERE child.parent.id = :labelId)) " +
           "AND mcq.status = :status")
    List<MCQQuestion> findByLabelWithCategoryFallback(@Param("labelId") String labelId,
                                                     @Param("categoryId") String categoryId,
                                                     @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Count MCQ questions accessible through label hierarchy (including category fallback)
     */
    @Query("SELECT COUNT(DISTINCT mcq) FROM MCQQuestion mcq " +
           "LEFT JOIN mcq.mcqLabels ml " +
           "LEFT JOIN mcq.mcqCategories mc " +
           "LEFT JOIN ml.label l " +
           "WHERE (mc.category.id = :categoryId OR " +
           "       ml.label.id = :labelId OR " +
           "       ml.label.parent.id = :labelId OR " +
           "       ml.label.id IN (SELECT child.id FROM Label child WHERE child.parent.id = :labelId)) " +
           "AND mcq.status = :status")
    long countByLabelWithCategoryFallback(@Param("labelId") String labelId,
                                         @Param("categoryId") String categoryId,
                                         @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by multiple categories
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqCategories mc " +
           "WHERE mc.category.id IN :categoryIds AND mcq.status = :status")
    List<MCQQuestion> findByMultipleCategoriesAndStatus(@Param("categoryIds") List<String> categoryIds, 
                                                       @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by multiple labels
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE ml.label.id IN :labelIds AND mcq.status = :status")
    List<MCQQuestion> findByMultipleLabelsAndStatus(@Param("labelIds") List<String> labelIds, 
                                                   @Param("status") MCQQuestion.MCQStatus status);

    /**
     * Find MCQ questions by multiple categories and labels (intersection)
     */
    @Query("SELECT DISTINCT mcq FROM MCQQuestion mcq " +
           "JOIN mcq.mcqCategories mc " +
           "JOIN mcq.mcqLabels ml " +
           "WHERE mc.category.id IN :categoryIds AND ml.label.id IN :labelIds AND mcq.status = :status")
    List<MCQQuestion> findByMultipleCategoriesAndLabelsAndStatus(@Param("categoryIds") List<String> categoryIds,
                                                               @Param("labelIds") List<String> labelIds,
                                                               @Param("status") MCQQuestion.MCQStatus status);
}
