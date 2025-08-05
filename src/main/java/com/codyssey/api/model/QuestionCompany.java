package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

/**
 * QuestionCompany Entity (Junction Table)
 * <p>
 * Represents the many-to-many relationship between coding questions and companies
 * that have asked these questions in interviews.
 */
@Entity
@Table(name = "question_companies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"question_id", "company_label_id"})
        },
        indexes = {
                @Index(name = "idx_question_company_question", columnList = "question_id"),
                @Index(name = "idx_question_company_label", columnList = "company_label_id"),
                @Index(name = "idx_question_company_frequency", columnList = "frequency"),
                @Index(name = "idx_question_company_year", columnList = "last_asked_year")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCompany {

    /**
     * Primary key for the entity - Question Company ID (QCP-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-company-id")
    @GenericGenerator(name = "question-company-id", strategy = "com.codyssey.api.util.QuestionCompanyIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Associated coding question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @NotNull
    private CodingQuestion question;

    /**
     * Associated company label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_label_id", nullable = false)
    @NotNull
    private Label companyLabel;

    /**
     * How often this question is asked by this company
     */
    @Column(name = "frequency", nullable = false)
    private Integer frequency = 1;

    /**
     * Year when this question was last asked by this company
     */
    @Column(name = "last_asked_year")
    private Integer lastAskedYear;

    /**
     * When this association was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Constructor with required fields
     */
    public QuestionCompany(CodingQuestion question, Label companyLabel) {
        this.question = question;
        this.companyLabel = companyLabel;
        this.frequency = 1;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor with frequency
     */
    public QuestionCompany(CodingQuestion question, Label companyLabel, Integer frequency, Integer lastAskedYear) {
        this.question = question;
        this.companyLabel = companyLabel;
        this.frequency = frequency != null ? frequency : 1;
        this.lastAskedYear = lastAskedYear;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (frequency == null) {
            frequency = 1;
        }
    }
}