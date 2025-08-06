package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * QuestionLabel Entity
 * <p>
 * Represents the many-to-many relationship between CodingQuestion and Label.
 * This includes all types of labels: Data Structures, Algorithms, Topics, etc.
 */
@Entity
@Table(name = "question_labels",
        indexes = {
                @Index(name = "idx_question_label_question", columnList = "question_id"),
                @Index(name = "idx_question_label_label", columnList = "label_id"),
                @Index(name = "idx_question_label_combo", columnList = "question_id, label_id", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLabel extends BaseEntity {

    /**
     * Primary key for the entity - QuestionLabel ID (QLB-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-label-id")
    @GenericGenerator(name = "question-label-id", strategy = "com.codyssey.api.util.QuestionLabelIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Reference to the coding question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

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
    public QuestionLabel(CodingQuestion question, Label label) {
        this.question = question;
        this.label = label;
        this.relevanceScore = 5;
        this.isPrimary = false;
    }

    /**
     * Constructor with relevance score
     */
    public QuestionLabel(CodingQuestion question, Label label, Integer relevanceScore, Boolean isPrimary) {
        this.question = question;
        this.label = label;
        this.relevanceScore = relevanceScore;
        this.isPrimary = isPrimary;
    }
}