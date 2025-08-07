package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Source Entity
 * <p>
 * Represents coding question platforms/sources like LeetCode, HackerRank, GeeksForGeeks.
 */
@Entity
@Table(name = "sources",
        indexes = {
                @Index(name = "idx_source_code", columnList = "code", unique = true),
                @Index(name = "idx_source_name", columnList = "name")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source extends BaseEntity {

    /**
     * Primary key for the entity - Source ID (SRC-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "source-id")
    @GenericGenerator(name = "source-id", strategy = "com.codyssey.api.util.SourceIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Source code (LEETCODE, HACKERRANK, GEEKSFORGEEKS, etc.)
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Display name of the source
     */
    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Base URL of the platform
     */
    @Size(max = 200)
    @Column(name = "base_url")
    private String baseUrl;

    /**
     * Description of the platform
     */
    @Size(max = 500)
    @Column(name = "description")
    private String description;

    /**
     * Whether this source is active
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * Color code for UI representation
     */
    @Size(max = 7)
    @Column(name = "color_code")
    private String colorCode;

    /**
     * SEO-friendly URL slug for this source
     */
    @Size(max = 200)
    @Column(name = "url_slug", unique = true)
    private String urlSlug;

    /**
     * Constructor with required fields
     */
    public Source(String code, String name, String baseUrl) {
        this.code = code;
        this.name = name;
        this.baseUrl = baseUrl;
        this.isActive = true;
    }
}