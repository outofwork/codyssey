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
     * Find coding question by URL slug
     *
     * @param urlSlug the URL slug
     * @return Optional containing the question if found
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.urlSlug = :urlSlug AND q.deleted = false")
    Optional<CodingQuestion> findByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find coding question by URL slug with associated labels (eager fetch)
     *
     * @param urlSlug the URL slug
     * @return Optional containing the question with labels if found and not deleted
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q " +
           "LEFT JOIN FETCH q.questionLabels ql " +
           "LEFT JOIN FETCH ql.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE q.urlSlug = :urlSlug AND q.deleted = false")
    Optional<CodingQuestion> findByUrlSlugWithLabels(@Param("urlSlug") String urlSlug);

    /**
     * Find coding question by URL slug with associated companies (eager fetch)
     *
     * @param urlSlug the URL slug
     * @return Optional containing the question with companies if found and not deleted
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q " +
           "LEFT JOIN FETCH q.questionCompanies qc " +
           "LEFT JOIN FETCH qc.companyLabel cl " +
           "LEFT JOIN FETCH cl.category " +
           "WHERE q.urlSlug = :urlSlug AND q.deleted = false")
    Optional<CodingQuestion> findByUrlSlugWithCompanies(@Param("urlSlug") String urlSlug);

    /**
     * Check if URL slug exists (excluding specific ID)
     *
     * @param urlSlug the URL slug to check
     * @param excludeId the ID to exclude from the check
     * @return true if URL slug exists for a different entity
     */
    @Query("SELECT COUNT(q) > 0 FROM CodingQuestion q WHERE q.urlSlug = :urlSlug AND q.id != :excludeId AND q.deleted = false")
    boolean existsByUrlSlugAndIdNot(@Param("urlSlug") String urlSlug, @Param("excludeId") String excludeId);

    /**
     * Check if URL slug exists
     *
     * @param urlSlug the URL slug to check
     * @return true if URL slug exists
     */
    @Query("SELECT COUNT(q) > 0 FROM CodingQuestion q WHERE q.urlSlug = :urlSlug AND q.deleted = false")
    boolean existsByUrlSlug(@Param("urlSlug") String urlSlug);

    /**
     * Find all questions that are not soft deleted with source eagerly fetched
     *
     * @return List of all non-deleted questions with sources
     */
    @Query("SELECT q FROM CodingQuestion q LEFT JOIN FETCH q.source WHERE q.deleted = false")
    List<CodingQuestion> findByDeletedFalseWithSource();

    /**
     * Find coding question by ID with associated labels (eager fetch)
     *
     * @param id the ID to search for
     * @return Optional containing the question with labels if found and not deleted
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q " +
           "LEFT JOIN FETCH q.questionLabels ql " +
           "LEFT JOIN FETCH ql.label l " +
           "LEFT JOIN FETCH l.category " +
           "WHERE q.id = :id AND q.deleted = false")
    Optional<CodingQuestion> findByIdWithLabels(@Param("id") String id);

    /**
     * Find coding question by ID with associated companies (eager fetch)
     *
     * @param id the ID to search for
     * @return Optional containing the question with companies if found and not deleted
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q " +
           "LEFT JOIN FETCH q.questionCompanies qc " +
           "LEFT JOIN FETCH qc.companyLabel cl " +
           "LEFT JOIN FETCH cl.category " +
           "WHERE q.id = :id AND q.deleted = false")
    Optional<CodingQuestion> findByIdWithCompanies(@Param("id") String id);

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
     * Find coding questions by source
     *
     * @param sourceId the source ID
     * @return List of questions from the specified source
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.source.id = :sourceId AND q.deleted = false")
    List<CodingQuestion> findBySourceId(@Param("sourceId") String sourceId);

    /**
     * Find coding questions by source code
     *
     * @param sourceCode the source code (LEETCODE, HACKERRANK, etc.)
     * @return List of questions from the specified source
     */
    @Query("SELECT q FROM CodingQuestion q WHERE UPPER(q.source.code) = UPPER(:sourceCode) AND q.deleted = false")
    List<CodingQuestion> findBySourceCode(@Param("sourceCode") String sourceCode);

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
           "LOWER(q.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND q.deleted = false")
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
     * Check if a question title exists within a source (for uniqueness validation)
     *
     * @param title the title to check
     * @param sourceId the source ID
     * @return true if title exists for the source
     */
    @Query("SELECT COUNT(q) > 0 FROM CodingQuestion q WHERE LOWER(q.title) = LOWER(:title) AND " +
           "q.source.id = :sourceId AND q.deleted = false")
    boolean existsByTitleAndSourceId(@Param("title") String title, @Param("sourceId") String sourceId);

    /**
     * Check if a platform question ID exists within a source (for uniqueness validation)
     *
     * @param platformQuestionId the platform question ID to check
     * @param sourceId the source ID
     * @return true if platform question ID exists for the source
     */
    @Query("SELECT COUNT(q) > 0 FROM CodingQuestion q WHERE q.platformQuestionId = :platformQuestionId AND " +
           "q.source.id = :sourceId AND q.deleted = false")
    boolean existsByPlatformQuestionIdAndSourceId(@Param("platformQuestionId") String platformQuestionId, @Param("sourceId") String sourceId);

    /**
     * Find questions with pagination
     *
     * @param pageable pagination information
     * @return Page of questions
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.deleted = false ORDER BY q.createdAt DESC")
    Page<CodingQuestion> findQuestionsWithPagination(Pageable pageable);

    /**
     * Find questions created by a specific user
     *
     * @param userId the user ID
     * @return List of questions created by the user
     */
    @Query("SELECT q FROM CodingQuestion q WHERE q.createdByUser.id = :userId AND q.deleted = false")
    List<CodingQuestion> findByCreatedByUserId(@Param("userId") String userId);

    /**
     * Advanced search with multiple filters including label and company joins
     *
     * @param sourceIds list of source IDs to filter by (can be null)
     * @param difficultyIds list of difficulty label IDs to filter by (can be null)
     * @param labelIds list of label IDs to filter by (can be null)
     * @param companyIds list of company label IDs to filter by (can be null)
     * @param searchTerm search term for title/description (can be null)
     * @return List of questions matching the filters
     */
    @Query("SELECT DISTINCT q FROM CodingQuestion q " +
           "LEFT JOIN q.questionLabels ql " +
           "LEFT JOIN q.questionCompanies qc " +
           "WHERE q.deleted = false " +
           "AND (:sourceIds IS NULL OR q.source.id IN :sourceIds) " +
           "AND (:difficultyIds IS NULL OR q.difficultyLabel.id IN :difficultyIds) " +
           "AND (:labelIds IS NULL OR ql.label.id IN :labelIds) " +
           "AND (:companyIds IS NULL OR qc.companyLabel.id IN :companyIds) " +
           "AND (:searchTerm IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "     OR LOWER(q.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY q.createdAt DESC")
    List<CodingQuestion> findWithFilters(@Param("sourceIds") List<String> sourceIds,
                                        @Param("difficultyIds") List<String> difficultyIds,
                                        @Param("labelIds") List<String> labelIds,
                                        @Param("companyIds") List<String> companyIds,
                                        @Param("searchTerm") String searchTerm);

    /**
     * Count questions by source
     *
     * @param sourceId the source ID
     * @return count of questions for the source
     */
    @Query("SELECT COUNT(q) FROM CodingQuestion q WHERE q.source.id = :sourceId AND q.deleted = false")
    Long countBySourceId(@Param("sourceId") String sourceId);

    /**
     * Count questions by difficulty
     *
     * @param difficultyId the difficulty label ID
     * @return count of questions for the difficulty
     */
    @Query("SELECT COUNT(q) FROM CodingQuestion q WHERE q.difficultyLabel.id = :difficultyId AND q.deleted = false")
    Long countByDifficultyId(@Param("difficultyId") String difficultyId);
}