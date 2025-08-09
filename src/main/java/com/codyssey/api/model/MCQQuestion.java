package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

/**
 * MCQQuestion Entity
 * <p>
 * Represents a Multiple Choice Question (MCQ) in the system with associated labels
 * for categorization and difficulty levels.
 */
@Entity
@Table(name = "mcq_questions",
        indexes = {
                @Index(name = "idx_mcq_difficulty", columnList = "difficulty_label_id"),
                @Index(name = "idx_mcq_status", columnList = "status"),
                @Index(name = "idx_mcq_created_by", columnList = "created_by_user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQQuestion extends BaseEntity {

    /**
     * Primary key for the entity - MCQ ID (MCQ-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "mcq-id")
    @GenericGenerator(name = "mcq-id", strategy = "com.codyssey.api.util.MCQIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Question text
     */
    @NotBlank
    @Size(max = 1000)
    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    /**
     * Option A
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "option_a", nullable = false)
    private String optionA;

    /**
     * Option B
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "option_b", nullable = false)
    private String optionB;

    /**
     * Option C
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "option_c", nullable = false)
    private String optionC;

    /**
     * Option D
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "option_d", nullable = false)
    private String optionD;

    /**
     * Correct answer (A, B, C, or D)
     */
    @NotBlank
    @Size(max = 1)
    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    /**
     * Explanation for the correct answer
     */
    @Size(max = 1000)
    @Column(name = "explanation", length = 1000)
    private String explanation;

    /**
     * Difficulty level (Easy, Medium, Hard) - links to Label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_label_id")
    private Label difficultyLabel;

    /**
     * Question status (ACTIVE, DEPRECATED, DRAFT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MCQStatus status = MCQStatus.ACTIVE;

    /**
     * User who created/added this question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Associated categories (Data Structures, Algorithms, etc.)
     * Multiple categories can be associated with different relevance scores
     */
    @OneToMany(mappedBy = "mcqQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MCQCategory> mcqCategories;

    /**
     * Associated labels/tags (Binary Tree, Recursion, Search, etc.)
     * Multiple labels can be associated with different relevance scores
     */
    @OneToMany(mappedBy = "mcqQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MCQLabel> mcqLabels;

    /**
     * SEO-friendly URL slug for this question
     */
    @Size(max = 300)
    @Column(name = "url_slug", unique = true)
    private String urlSlug;

    /**
     * Constructor with required fields
     */
    public MCQQuestion(String questionText, String optionA, String optionB, String optionC, 
                      String optionD, String correctAnswer, String explanation, User createdByUser) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.createdByUser = createdByUser;
        this.status = MCQStatus.ACTIVE;
    }

    /**
     * MCQ status enumeration
     */
    public enum MCQStatus {
        ACTIVE, DEPRECATED, DRAFT
    }
}
