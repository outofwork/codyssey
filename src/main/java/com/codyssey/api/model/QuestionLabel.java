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
 * QuestionLabel Entity (Junction Table)
 * <p>
 * Represents the many-to-many relationship between coding questions and labels/tags.
 */
@Entity
@Table(name = "question_labels",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"question_id", "label_id"})
        },
        indexes = {
                @Index(name = "idx_question_label_question", columnList = "question_id"),
                @Index(name = "idx_question_label_label", columnList = "label_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLabel {

    /**
     * Primary key for the entity - Question Label ID (QLB-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-label-id")
    @GenericGenerator(name = "question-label-id", strategy = "com.codyssey.api.util.QuestionLabelIdGenerator")
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
     * Associated label
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    @NotNull
    private Label label;

    /**
     * When the tag was added
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Constructor with required fields
     */
    public QuestionLabel(CodingQuestion question, Label label) {
        this.question = question;
        this.label = label;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}