package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

/**
 * QuestionCompany Entity
 * <p>
 * Represents the many-to-many relationship between CodingQuestion and Company Labels.
 * Tracks which companies have asked specific questions in interviews.
 */
@Entity
@Table(name = "question_companies",
        indexes = {
                @Index(name = "idx_question_company_question", columnList = "question_id"),
                @Index(name = "idx_question_company_label", columnList = "company_label_id"),
                @Index(name = "idx_question_company_combo", columnList = "question_id, company_label_id", unique = true),
                @Index(name = "idx_question_company_frequency", columnList = "frequency_score")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCompany extends BaseEntity {

    /**
     * Primary key for the entity - QuestionCompany ID (QCP-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-company-id")
    @GenericGenerator(name = "question-company-id", strategy = "com.codyssey.api.util.QuestionCompanyIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Reference to the coding question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

    /**
     * Reference to the company label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_label_id", nullable = false)
    private Label companyLabel;

    /**
     * How frequently this company asks this question (1-10)
     * 10 = Very frequently asked, 1 = Rarely asked
     */
    @Column(name = "frequency_score")
    private Integer frequencyScore = 5;

    /**
     * Interview round where this question is typically asked
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "interview_round")
    private InterviewRound interviewRound;

    /**
     * Year when this question was last reported from this company
     */
    @Column(name = "last_asked_year")
    private Integer lastAskedYear;

    /**
     * Month when this question was last reported from this company
     */
    @Column(name = "last_asked_date")
    private LocalDate lastAskedDate;

    /**
     * Additional notes about this company's use of the question
     */
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Whether this is confirmed information or rumored
     */
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    /**
     * Constructor with required fields
     */
    public QuestionCompany(CodingQuestion question, Label companyLabel) {
        this.question = question;
        this.companyLabel = companyLabel;
        this.frequencyScore = 5;
        this.isVerified = false;
    }

    /**
     * Constructor with frequency and round
     */
    public QuestionCompany(CodingQuestion question, Label companyLabel, 
                          Integer frequencyScore, InterviewRound interviewRound) {
        this.question = question;
        this.companyLabel = companyLabel;
        this.frequencyScore = frequencyScore;
        this.interviewRound = interviewRound;
        this.isVerified = false;
    }

    /**
     * Interview round enumeration
     */
    public enum InterviewRound {
        ONLINE_ASSESSMENT,
        PHONE_SCREEN,
        TECHNICAL_INTERVIEW,
        ONSITE_INTERVIEW,
        FINAL_ROUND,
        SYSTEM_DESIGN,
        BEHAVIORAL,
        CODING_CHALLENGE
    }
}