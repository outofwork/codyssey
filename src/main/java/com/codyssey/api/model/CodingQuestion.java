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
 * with all associated metadata.
 */
@Entity
@Table(name = "coding_questions",
        indexes = {
                @Index(name = "idx_question_difficulty", columnList = "difficulty_label_id"),
                @Index(name = "idx_question_source", columnList = "source_id"),
                @Index(name = "idx_question_status", columnList = "status"),
                @Index(name = "idx_question_created_by", columnList = "created_by_user_id"),
                @Index(name = "idx_question_platform_id", columnList = "platform_question_id")
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
     * Path to the Markdown file containing the full question content
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Difficulty level (Easy, Medium, Hard) - links to Label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_label_id")
    private Label difficultyLabel;

    /**
     * Platform source (LeetCode, HackerRank, etc.) - links to Source entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    /**
     * Original platform question ID/number
     */
    @Size(max = 100)
    @Column(name = "platform_question_id")
    private String platformQuestionId;

    /**
     * URL to the original question on the platform
     */
    @Size(max = 500)
    @Column(name = "original_url")
    private String originalUrl;

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
     * Associated labels/tags (Data Structures, Algorithms, Topics, etc.)
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionLabel> questionLabels;

    /**
     * Associated companies that ask this question
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionCompany> questionCompanies;

    /**
     * SEO-friendly URL slug for this question
     */
    @Size(max = 300)
    @Column(name = "url_slug", unique = true)
    private String urlSlug;

    /**
     * Constructor with required fields
     */
    public CodingQuestion(String title, String shortDescription, String filePath, Source source, User createdByUser) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.filePath = filePath;
        this.source = source;
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