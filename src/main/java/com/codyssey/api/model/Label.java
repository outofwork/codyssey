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
 * Label Entity
 * <p>
 * Represents a label in the system with hierarchical parent-child relationships
 * and association with a label category.
 */
@Entity
@Table(name = "labels",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "category_id", "parent_id"})
        },
        indexes = {
                @Index(name = "idx_label_category", columnList = "category_id"),
                @Index(name = "idx_label_parent", columnList = "parent_id"),
                @Index(name = "idx_label_active", columnList = "active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Label extends BaseEntity {

    /**
     * Primary key for the entity - Label ID (LBL-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "label-id")
    @GenericGenerator(name = "label-id", strategy = "com.codyssey.api.util.LabelIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Name of the label
     */
    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Description of the label
     */
    @Size(max = 500)
    @Column(name = "description")
    private String description;

    /**
     * Whether the label is active
     */
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    /**
     * Associated label category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private LabelCategory category;

    /**
     * Parent label for hierarchical structure
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Label parent;

    /**
     * Child labels
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Label> children;

    /**
     * Constructor with required fields
     */
    public Label(String name, String description, LabelCategory category, Label parent) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.parent = parent;
        this.active = true;
    }

    /**
     * Constructor for root label (no parent)
     */
    public Label(String name, String description, LabelCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.parent = null;
        this.active = true;
    }

    /**
     * Check if this label is a root label (no parent)
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Check if this label has children
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}