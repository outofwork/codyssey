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
 * QuestionMedia Entity
 * <p>
 * Represents media files (images, diagrams, videos) associated with coding questions.
 */
@Entity
@Table(name = "question_media",
        indexes = {
                @Index(name = "idx_media_question", columnList = "question_id"),
                @Index(name = "idx_media_sequence", columnList = "question_id, sequence"),
                @Index(name = "idx_media_type", columnList = "media_type")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMedia extends BaseEntity {

    /**
     * Primary key for the entity - Media ID (MED-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "media-id")
    @GenericGenerator(name = "media-id", strategy = "com.codyssey.api.util.MediaIdGenerator")
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
     * Display order of media
     */
    @Column(name = "sequence", nullable = false)
    @NotNull
    private Integer sequence;

    /**
     * Type of media file
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    @NotNull
    private MediaType mediaType;

    /**
     * Path to media file
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Original file name
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * Media description
     */
    @Size(max = 500)
    @Column(name = "description")
    private String description;

    /**
     * Constructor with required fields
     */
    public QuestionMedia(CodingQuestion question, Integer sequence, MediaType mediaType, 
                        String filePath, String fileName) {
        this.question = question;
        this.sequence = sequence;
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    /**
     * Constructor with description
     */
    public QuestionMedia(CodingQuestion question, Integer sequence, MediaType mediaType, 
                        String filePath, String fileName, String description) {
        this.question = question;
        this.sequence = sequence;
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.description = description;
    }

    /**
     * Media type enumeration
     */
    public enum MediaType {
        IMAGE,      // PNG, JPG, etc.
        DIAGRAM,    // Flowcharts, tree diagrams, etc.
        VIDEO       // MP4, etc.
    }
}