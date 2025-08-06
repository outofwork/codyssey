package com.codyssey.api.repository;

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
 * Provides data access methods for QuestionCompany entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface QuestionCompanyRepository extends JpaRepository<QuestionCompany, String> {

    /**
     * Find all question companies that are not soft deleted
     *
     * @return List of all non-deleted question companies
     */
    List<QuestionCompany> findByDeletedFalse();

    /**
     * Find question company by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the question company if found and not deleted
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.id = :id AND qc.deleted = false")
    Optional<QuestionCompany> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find all companies for a specific question
     *
     * @param questionId the question ID
     * @return List of question companies for the question
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.deleted = false")
    List<QuestionCompany> findByQuestionId(@Param("questionId") String questionId);

    /**
     * Find all questions asked by a specific company
     *
     * @param companyLabelId the company label ID
     * @return List of question companies for the company
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId AND qc.deleted = false")
    List<QuestionCompany> findByCompanyLabelId(@Param("companyLabelId") String companyLabelId);

    /**
     * Find companies for a question ordered by frequency score
     *
     * @param questionId the question ID
     * @return List of question companies ordered by frequency score (descending)
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.deleted = false " +
           "ORDER BY qc.frequencyScore DESC, qc.isVerified DESC")
    List<QuestionCompany> findByQuestionIdOrderByFrequency(@Param("questionId") String questionId);

    /**
     * Find top companies that ask a specific question
     *
     * @param questionId the question ID
     * @param limit maximum number of companies to return
     * @return List of question companies with highest frequency scores
     */
    @Query("SELECT qc FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.deleted = false " +
           "ORDER BY qc.frequencyScore DESC, qc.isVerified DESC")
    List<QuestionCompany> findTopCompaniesByQuestionId(@Param("questionId") String questionId, 
                                                       @Param("limit") int limit);

    /**
     * Find questions by company and interview round
     *
     * @param companyLabelId the company label ID
     * @param interviewRound the interview round
     * @return List of question companies for the company and round
     */
    @Query("SELECT qc FROM QuestionCompany qc " +
           "WHERE qc.companyLabel.id = :companyLabelId AND qc.interviewRound = :interviewRound AND qc.deleted = false")
    List<QuestionCompany> findByCompanyAndRound(@Param("companyLabelId") String companyLabelId, 
                                               @Param("interviewRound") QuestionCompany.InterviewRound interviewRound);

    /**
     * Find most frequently asked questions by a company
     *
     * @param companyLabelId the company label ID
     * @param minFrequencyScore minimum frequency score
     * @return List of question companies with high frequency scores
     */
    @Query("SELECT qc FROM QuestionCompany qc " +
           "WHERE qc.companyLabel.id = :companyLabelId AND qc.frequencyScore >= :minFrequencyScore AND qc.deleted = false " +
           "ORDER BY qc.frequencyScore DESC")
    List<QuestionCompany> findFrequentQuestionsByCompany(@Param("companyLabelId") String companyLabelId, 
                                                        @Param("minFrequencyScore") int minFrequencyScore);

    /**
     * Find question-company relationship by question and company label
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @return Optional containing the question company if found
     */
    @Query("SELECT qc FROM QuestionCompany qc " +
           "WHERE qc.question.id = :questionId AND qc.companyLabel.id = :companyLabelId AND qc.deleted = false")
    Optional<QuestionCompany> findByQuestionIdAndCompanyLabelId(@Param("questionId") String questionId, 
                                                               @Param("companyLabelId") String companyLabelId);

    /**
     * Check if a question-company relationship exists
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @return true if relationship exists
     */
    @Query("SELECT COUNT(qc) > 0 FROM QuestionCompany qc " +
           "WHERE qc.question.id = :questionId AND qc.companyLabel.id = :companyLabelId AND qc.deleted = false")
    boolean existsByQuestionIdAndCompanyLabelId(@Param("questionId") String questionId, 
                                               @Param("companyLabelId") String companyLabelId);

    /**
     * Count companies for a specific question
     *
     * @param questionId the question ID
     * @return count of companies for the question
     */
    @Query("SELECT COUNT(qc) FROM QuestionCompany qc WHERE qc.question.id = :questionId AND qc.deleted = false")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count questions for a specific company
     *
     * @param companyLabelId the company label ID
     * @return count of questions for the company
     */
    @Query("SELECT COUNT(qc) FROM QuestionCompany qc WHERE qc.companyLabel.id = :companyLabelId AND qc.deleted = false")
    Long countByCompanyLabelId(@Param("companyLabelId") String companyLabelId);

    /**
     * Find the most active companies (companies with most questions)
     *
     * @param limit maximum number of companies to return
     * @return List of company data ordered by question count
     */
    @Query("SELECT qc.companyLabel.id, qc.companyLabel.name, COUNT(qc) as questionCount " +
           "FROM QuestionCompany qc WHERE qc.deleted = false " +
           "GROUP BY qc.companyLabel.id, qc.companyLabel.name " +
           "ORDER BY questionCount DESC")
    List<Object[]> findMostActiveCompanies(@Param("limit") int limit);

    /**
     * Find verified question-company relationships
     *
     * @param questionId the question ID
     * @return List of verified question companies
     */
    @Query("SELECT qc FROM QuestionCompany qc " +
           "WHERE qc.question.id = :questionId AND qc.isVerified = true AND qc.deleted = false")
    List<QuestionCompany> findVerifiedByQuestionId(@Param("questionId") String questionId);

    /**
     * Find recent questions by company (within last N years)
     *
     * @param companyLabelId the company label ID
     * @param sinceYear the minimum year to consider
     * @return List of recent question companies
     */
    @Query("SELECT qc FROM QuestionCompany qc " +
           "WHERE qc.companyLabel.id = :companyLabelId AND qc.lastAskedYear >= :sinceYear AND qc.deleted = false " +
           "ORDER BY qc.lastAskedYear DESC, qc.frequencyScore DESC")
    List<QuestionCompany> findRecentQuestionsByCompany(@Param("companyLabelId") String companyLabelId, 
                                                      @Param("sinceYear") int sinceYear);

    /**
     * Delete all companies for a specific question
     *
     * @param questionId the question ID
     */
    @Query("UPDATE QuestionCompany qc SET qc.deleted = true WHERE qc.question.id = :questionId")
    void deleteByQuestionId(@Param("questionId") String questionId);
}