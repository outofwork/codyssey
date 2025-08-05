package com.codyssey.api.model;

import com.codyssey.api.util.CategoryIdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * LabelCategory Entity
 * <p>
 * Represents a label category in the system for organizing
 * and categorizing labels with code and description.
 */
@Entity
@Table(name = "label_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategory extends BaseEntity {

    /**
     * Primary key for the entity - Category ID (CAT-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "category-id")
    @GenericGenerator(name = "category-id", strategy = "com.codyssey.api.util.CategoryIdGenerator")
    @Column(name = "id", length = 10)
    private String id;

    /**
     * Name of the label category
     */
    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Unique code for the label category
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Description of the label category
     */
    @Size(max = 500)
    @Column(name = "description")
    private String description;

    /**
     * Whether the label category is active
     */
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    /**
     * Constructor with required fields
     */
    public LabelCategory(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.active = true;
    }
}