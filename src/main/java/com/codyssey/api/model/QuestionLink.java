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
 * QuestionLink Entity
 * <p>
 * Represents relationships between coding questions (follow-ups, similar problems, prerequisites).
 */
@Entity
@Table(name = "question_links",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"source_question_id", "target_question_id", "relationship_type"})
        },
        indexes = {
                @Index(name = "idx_question_link_source", columnList = "source_question_id"),
                @Index(name = "idx_question_link_target", columnList = "target_question_id"),
                @Index(name = "idx_question_link_type", columnList = "relationship_type"),
                @Index(name = "idx_question_link_sequence", columnList = "source_question_id, sequence")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLink {

    /**
     * Primary key for the entity - Question Link ID (QLK-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "question-link-id")
    @GenericGenerator(name = "question-link-id", strategy = "com.codyssey.api.util.QuestionLinkIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Source/primary question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_question_id", nullable = false)
    @NotNull
    private CodingQuestion sourceQuestion;

    /**
     * Target/related question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_question_id", nullable = false)
    @NotNull
    private CodingQuestion targetQuestion;

    /**
     * Order of related questions
     */
    @Column(name = "sequence", nullable = false)
    @NotNull
    private Integer sequence;

    /**
     * Type of relationship
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    @NotNull
    private RelationshipType relationshipType;

    /**
     * When this link was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Constructor with required fields
     */
    public QuestionLink(CodingQuestion sourceQuestion, CodingQuestion targetQuestion, 
                       Integer sequence, RelationshipType relationshipType) {
        this.sourceQuestion = sourceQuestion;
        this.targetQuestion = targetQuestion;
        this.sequence = sequence;
        this.relationshipType = relationshipType;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    /**
     * Relationship type enumeration
     */
    public enum RelationshipType {
        FOLLOW_UP,      // This question is a follow-up to the source
        SIMILAR,        // This question is similar to the source
        PREREQUISITE    // This question is a prerequisite for the source
    }
}