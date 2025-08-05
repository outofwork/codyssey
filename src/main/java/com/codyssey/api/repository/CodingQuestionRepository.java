package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CodingQuestion entity
 * <p>
 * Provides data access methods for CodingQuestion entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, String> {

    /**
     * Find all coding questions that are not soft deleted
     *
     * @return List of all non-deleted coding questions
     */
    List<CodingQuestion> findByDeletedFalse();

    /**
     * Find coding question by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the question if found and not deleted
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.id = :id AND q.deleted = false")
    Optional<CodingQuestion> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all active coding questions that are not soft deleted
     *
     * @return List of active, non-deleted coding questions
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.status = 'ACTIVE' AND q.deleted = false")
    List<CodingQuestion> findActiveQuestions();

    /**
     * Find coding questions by difficulty label
     *
     * @param difficultyLabel the difficulty label
     * @return List of questions with the specified difficulty
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.difficultyLabel = :difficultyLabel AND q.deleted = false")
    List<CodingQuestion> findByDifficultyLabel(@Param("difficultyLabel") Label difficultyLabel);

    /**
     * Find coding questions by platform source
     *
     * @param platformSource the platform source
     * @return List of questions from the specified platform
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.platformSource = :platformSource AND q.deleted = false")
    List<CodingQuestion> findByPlatformSource(@Param("platformSource") String platformSource);

    /**
     * Search coding questions by title containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of matching questions
     */
    @Query("SELECT q FROM CodingQuestion q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND q.deleted = false")
    List<CodingQuestion> findByTitleContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Search coding questions by title or description containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return Page of matching questions
     */
    @Query("SELECT q FROM CodingQuestion q WHERE (LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND q.deleted = false")
    Page<CodingQuestion> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find questions by status with pagination
     *
     * @param status the question status
     * @param pageable pagination information
     * @return Page of questions with the specified status
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.status = :status AND q.deleted = false")
    Page<CodingQuestion> findByStatus(@Param("status") CodingQuestion.QuestionStatus status, Pageable pageable);

    /**
     * Find questions that have a specific label/tag
     *
     * @param labelId the label ID
     * @return List of questions tagged with the specified label
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q JOIN q.questionLabels ql WHERE ql.label.id = :labelId AND q.deleted = false")
    List<CodingQuestion> findByLabelId(@Param("labelId") String labelId);

    /**
     * Find questions asked by a specific company
     *
     * @param companyLabelId the company label ID
     * @return List of questions asked by the specified company
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q JOIN q.questionCompanies qc WHERE qc.companyLabel.id = :companyLabelId AND q.deleted = false")
    List<CodingQuestion> findByCompanyLabelId(@Param("companyLabelId") String companyLabelId);

    /**
     * Check if a question title exists within a platform (for uniqueness validation)
     *
     * @param title the title to check
     * @param platformSource the platform source
     * @return true if title exists for the platform
     */
    @Query("SELECT COUNT(q) > 0 FROM CodingQuestion q WHERE LOWER(q.title) = LOWER(:title) AND " +
           "q.platformSource = :platformSource AND q.deleted = false")
    boolean existsByTitleAndPlatformSource(@Param("title") String title, @Param("platformSource") String platformSource);

    /**
     * Find questions with their solution counts
     *
     * @param pageable pagination information
     * @return Page of questions with solution counts
     */
    @Query("SELECT q FROM CodingQuestion q LEFT JOIN q.solutions s WHERE q.deleted = false GROUP BY q")
    Page<CodingQuestion> findQuestionsWithSolutionCounts(Pageable pageable);

    /**
     * Find questions created by a specific user
     *
     * @param userId the user ID
     * @return List of questions created by the user
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.createdByUser.id = :userId AND q.deleted = false")
    List<CodingQuestion> findByCreatedByUserId(@Param("userId") String userId);
}