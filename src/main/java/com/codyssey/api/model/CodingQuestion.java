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
 * CodingQuestion Entity
 * <p>
 * Represents a coding question from platforms like LeetCode, HackerRank, etc.
 * with all associated metadata, solutions, test cases, and media.
 */
@Entity
@Table(name = "coding_questions",
        indexes = {
                @Index(name = "idx_question_difficulty", columnList = "difficulty_label_id"),
                @Index(name = "idx_question_platform", columnList = "platform_source"),
                @Index(name = "idx_question_status", columnList = "status"),
                @Index(name = "idx_question_created_by", columnList = "created_by_user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodingQuestion extends BaseEntity {

    /**
     * Primary key for the entity - Question ID (QST-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-id")
    @GenericGenerator(name = "question-id", strategy = "com.codyssey.api.util.QuestionIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Question title
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Brief summary/teaser of the question
     */
    @Size(max = 500)
    @Column(name = "short_description")
    private String shortDescription;

    /**
     * Full problem statement
     */
    @NotBlank
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Difficulty level (Easy, Medium, Hard) - links to Label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_label_id")
    private Label difficultyLabel;

    /**
     * Platform source (LeetCode, HackerRank, etc.)
     */
    @Size(max = 50)
    @Column(name = "platform_source")
    private String platformSource;

    /**
     * Original platform question ID
     */
    @Size(max = 100)
    @Column(name = "platform_question_id")
    private String platformQuestionId;

    /**
     * Input format description
     */
    @Column(name = "input_format", columnDefinition = "TEXT")
    private String inputFormat;

    /**
     * Output format description
     */
    @Column(name = "output_format", columnDefinition = "TEXT")
    private String outputFormat;

    /**
     * Problem constraints
     */
    @Column(name = "constraints_text", columnDefinition = "TEXT")
    private String constraintsText;

    /**
     * Expected time complexity hint
     */
    @Size(max = 100)
    @Column(name = "time_complexity_hint")
    private String timeComplexityHint;

    /**
     * Expected space complexity hint
     */
    @Size(max = 100)
    @Column(name = "space_complexity_hint")
    private String spaceComplexityHint;

    /**
     * Question status (ACTIVE, DEPRECATED, DRAFT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuestionStatus status = QuestionStatus.ACTIVE;

    /**
     * User who created/added this question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Associated solutions
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionSolution> solutions;

    /**
     * Associated labels/tags
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionLabel> questionLabels;

    /**
     * Associated companies
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionCompany> questionCompanies;

    /**
     * Related questions (as source)
     */
    @OneToMany(mappedBy = "sourceQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionLink> outgoingLinks;

    /**
     * Related questions (as target)
     */
    @OneToMany(mappedBy = "targetQuestion", fetch = FetchType.LAZY)
    private List<QuestionLink> incomingLinks;

    /**
     * Test cases
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionTestCase> testCases;

    /**
     * Media files
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionMedia> mediaFiles;

    /**
     * Constructor with required fields
     */
    public CodingQuestion(String title, String description, User createdByUser) {
        this.title = title;
        this.description = description;
        this.createdByUser = createdByUser;
        this.status = QuestionStatus.ACTIVE;
    }

    /**
     * Question status enumeration
     */
    public enum QuestionStatus {
        ACTIVE, DEPRECATED, DRAFT
    }
}