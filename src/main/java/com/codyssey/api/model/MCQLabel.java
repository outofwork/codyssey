package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * MCQLabel Entity
 * <p>
 * Represents the many-to-many relationship between MCQ questions and labels.
 * Each MCQ question can have multiple labels (topics, categories, etc.) 
 * and each label can be associated with multiple MCQ questions.
 */
@Entity
@Table(name = "mcq_labels",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"mcq_question_id", "label_id"})
        },
        indexes = {
                @Index(name = "idx_mcq_label_question", columnList = "mcq_question_id"),
                @Index(name = "idx_mcq_label_label", columnList = "label_id"),
                @Index(name = "idx_mcq_label_primary", columnList = "is_primary"),
                @Index(name = "idx_mcq_label_relevance", columnList = "relevance_score")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQLabel extends BaseEntity {

    /**
     * Primary key for the entity - MCQLabel ID (MLB-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "mcq-label-id")
    @GenericGenerator(name = "mcq-label-id", strategy = "com.codyssey.api.util.MCQLabelIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Reference to the MCQ question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcq_question_id", nullable = false)
    private MCQQuestion mcqQuestion;

    /**
     * Reference to the label (Data Structure, Algorithm, Topic, etc.)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    /**
     * Relevance score for this label to the question (1-10)
     * Higher score indicates more relevance
     */
    @Column(name = "relevance_score")
    private Integer relevanceScore = 5;

    /**
     * Whether this is a primary/main tag for the question
     */
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    /**
     * Notes about why this label applies to the question
     */
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Constructor with required fields
     */
    public MCQLabel(MCQQuestion mcqQuestion, Label label) {
        this.mcqQuestion = mcqQuestion;
        this.label = label;
        this.relevanceScore = 5;
        this.isPrimary = false;
    }

    /**
     * Constructor with relevance score
     */
    public MCQLabel(MCQQuestion mcqQuestion, Label label, Integer relevanceScore, Boolean isPrimary) {
        this.mcqQuestion = mcqQuestion;
        this.label = label;
        this.relevanceScore = relevanceScore;
        this.isPrimary = isPrimary;
    }
}
