package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * QuestionSolution Entity
 * <p>
 * Represents a solution to a coding question in a specific programming language.
 */
@Entity
@Table(name = "question_solutions",
        indexes = {
                @Index(name = "idx_solution_question", columnList = "question_id"),
                @Index(name = "idx_solution_language", columnList = "language"),
                @Index(name = "idx_solution_sequence", columnList = "question_id, sequence"),
                @Index(name = "idx_solution_created_by", columnList = "created_by_user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolution extends BaseEntity {

    /**
     * Primary key for the entity - Solution ID (SOL-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "solution-id")
    @GenericGenerator(name = "solution-id", strategy = "com.codyssey.api.util.SolutionIdGenerator")
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
     * Order/priority of solution
     */
    @Column(name = "sequence", nullable = false)
    @NotNull
    private Integer sequence;

    /**
     * Programming language (Java, Python, C++, etc.)
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "language", nullable = false)
    private String language;

    /**
     * The actual code solution
     */
    @NotBlank
    @Column(name = "solution_code", nullable = false, columnDefinition = "TEXT")
    private String solutionCode;

    /**
     * Solution explanation
     */
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    /**
     * Actual time complexity
     */
    @Size(max = 50)
    @Column(name = "time_complexity")
    private String timeComplexity;

    /**
     * Actual space complexity
     */
    @Size(max = 50)
    @Column(name = "space_complexity")
    private String spaceComplexity;

    /**
     * Is this the optimal solution
     */
    @Column(name = "is_optimal", nullable = false)
    private Boolean isOptimal = false;

    /**
     * User who created this solution
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Constructor with required fields
     */
    public QuestionSolution(CodingQuestion question, Integer sequence, String language, 
                           String solutionCode, User createdByUser) {
        this.question = question;
        this.sequence = sequence;
        this.language = language;
        this.solutionCode = solutionCode;
        this.createdByUser = createdByUser;
        this.isOptimal = false;
    }
}