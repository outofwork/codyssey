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
 * SystemDesign Entity
 * <p>
 * Represents educational system design articles about architecture patterns, scalability, 
 * distributed systems, and other system design concepts with all associated metadata.
 */
@Entity
@Table(name = "system_designs",
        indexes = {
                @Index(name = "idx_system_design_source", columnList = "source_id"),
                @Index(name = "idx_system_design_status", columnList = "status"),
                @Index(name = "idx_system_design_created_by", columnList = "created_by_user_id"),
                @Index(name = "idx_system_design_url_slug", columnList = "url_slug")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesign extends BaseEntity {

    /**
     * Primary key for the entity - SystemDesign ID (SYS-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "system-design-id")
    @GenericGenerator(name = "system-design-id", strategy = "com.codyssey.api.util.SystemDesignIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * System design article title
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Brief summary/description of the system design article
     */
    @Size(max = 500)
    @Column(name = "short_description")
    private String shortDescription;

    /**
     * Path to the Markdown file containing the full system design content
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Source/platform of the system design article (Internal, External Blog, etc.) - links to Source entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    /**
     * URL to the original system design article if it's from an external source
     */
    @Size(max = 500)
    @Column(name = "original_url")
    private String originalUrl;

    /**
     * System design article status (ACTIVE, DEPRECATED, DRAFT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SystemDesignStatus status = SystemDesignStatus.ACTIVE;

    /**
     * User who created/added this system design article
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Associated labels/tags (Scalability, Database Design, Microservices, etc.)
     */
    @OneToMany(mappedBy = "systemDesign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SystemDesignLabel> systemDesignLabels;

    /**
     * SEO-friendly URL slug for this system design article
     */
    @Size(max = 300)
    @Column(name = "url_slug", unique = true)
    private String urlSlug;

    /**
     * Constructor with required fields
     */
    public SystemDesign(String title, String shortDescription, String filePath, Source source, User createdByUser) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.filePath = filePath;
        this.source = source;
        this.createdByUser = createdByUser;
        this.status = SystemDesignStatus.ACTIVE;
    }

    /**
     * SystemDesign status enumeration
     */
    public enum SystemDesignStatus {
        ACTIVE, DEPRECATED, DRAFT
    }
}
