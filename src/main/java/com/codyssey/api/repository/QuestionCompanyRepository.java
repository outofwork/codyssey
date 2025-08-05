package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.Label;
import com.codyssey.api.model.QuestionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionCompany entity
 * <p>
 * Provides data access methods for QuestionCompany entities (junction table).
 */
@Repository
public interface QuestionCompanyRepository extends JpaRepository<QuestionCompany, String> {

    /**
     * Find all question-company associations by question ID
     *
     * @param questionId the question ID
     * @return List of question-company associations
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.question.id = :questionId")
    List<QuestionCompany> findByQuestionId(@Param("questionId") String questionId);

    /**
     * Find all question-company associations by company label ID
     *
     * @param companyLabelId the company label ID
     * @return List of question-company associations
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId")
    List<QuestionCompany> findByCompanyLabelId(@Param("companyLabelId") String companyLabelId);

    /**
     * Find question-company association by question and company label
     *
     * @param question the question
     * @param companyLabel the company label
     * @return Optional containing the association if found
     */
    Optional<QuestionCompany> findByQuestionAndCompanyLabel(CodingQuestion question, Label companyLabel);

    /**
     * Find question-company association by question ID and company label ID
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @return Optional containing the association if found
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.companyLabel.id = :companyLabelId")
    Optional<QuestionCompany> findByQuestionIdAndCompanyLabelId(@Param("questionId") String questionId, @Param("companyLabelId") String companyLabelId);

    /**
     * Check if a question-company association exists
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @return true if the association exists
     */
    @Query("SELECT COUNT(qc) > 0 FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.companyLabel.id = :companyLabelId")
    boolean existsByQuestionIdAndCompanyLabelId(@Param("questionId") String questionId, @Param("companyLabelId") String companyLabelId);

    /**
     * Delete question-company association by question ID and company label ID
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     */
    @Query("DELETE FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.companyLabel.id = :companyLabelId")
    void deleteByQuestionIdAndCompanyLabelId(@Param("questionId") String questionId, @Param("companyLabelId") String companyLabelId);

    /**
     * Delete all question-company associations for a question
     *
     * @param questionId the question ID
     */
    @Query("DELETE FROM QuestionCompany qc WHERE qc.question.id = :questionId")
    void deleteByQuestionId(@Param("questionId") String questionId);

    /**
     * Get company labels for a question
     *
     * @param questionId the question ID
     * @return List of company labels for the question
     */
    @Query("SELECT qc.companyLabel FROM QuestionCompany qc WHERE qc.question.id = :questionId")
    List<Label> findCompanyLabelsByQuestionId(@Param("questionId") String questionId);

    /**
     * Get questions for a company label
     *
     * @param companyLabelId the company label ID
     * @return List of questions asked by the company
     */
    @Query("SELECT qc.question FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId AND qc.question.deleted = false")
    List<CodingQuestion> findQuestionsByCompanyLabelId(@Param("companyLabelId") String companyLabelId);

    /**
     * Find top companies that ask questions most frequently
     *
     * @param limit the maximum number of companies to return
     * @return List of company labels ordered by frequency
     */
    @Query("SELECT qc.companyLabel FROM QuestionCompany qc GROUP BY qc.companyLabel ORDER BY SUM(qc.frequency) DESC LIMIT :limit")
    List<Label> findTopCompaniesByFrequency(@Param("limit") int limit);

    /**
     * Find questions asked by a company in a specific year
     *
     * @param companyLabelId the company label ID
     * @param year the year
     * @return List of questions asked by the company in the year
     */
    @Query("SELECT qc.question FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId AND qc.lastAskedYear = :year AND qc.question.deleted = false")
    List<CodingQuestion> findQuestionsByCompanyAndYear(@Param("companyLabelId") String companyLabelId, @Param("year") Integer year);

    /**
     * Count companies for a question
     *
     * @param questionId the question ID
     * @return count of companies that ask the question
     */
    @Query("SELECT COUNT(qc) FROM QuestionCompany qc WHERE qc.question.id = :questionId")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count questions for a company
     *
     * @param companyLabelId the company label ID
     * @return count of questions asked by the company
     */
    @Query("SELECT COUNT(qc) FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId AND qc.question.deleted = false")
    Long countByCompanyLabelId(@Param("companyLabelId") String companyLabelId);
}