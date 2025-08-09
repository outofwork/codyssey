package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * SystemDesignLabel Entity - Association between SystemDesign and Label
 * <p>
 * Represents the many-to-many relationship between system design articles and labels/tags
 * with additional metadata about the relationship.
 */
@Entity
@Table(name = "system_design_labels",
        indexes = {
                @Index(name = "idx_system_design_label_system_design", columnList = "system_design_id"),
                @Index(name = "idx_system_design_label_label", columnList = "label_id"),
                @Index(name = "idx_system_design_label_composite", columnList = "system_design_id,label_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_system_design_label", columnNames = {"system_design_id", "label_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignLabel extends BaseEntity {

    /**
     * Primary key for the entity - SystemDesignLabel ID (SDL-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "system-design-label-id")
    @GenericGenerator(name = "system-design-label-id", strategy = "com.codyssey.api.util.SystemDesignLabelIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Reference to the SystemDesign entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_design_id", nullable = false)
    private SystemDesign systemDesign;

    /**
     * Reference to the Label entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    /**
     * Indicates if this is a primary tag for the system design article (for display purposes)
     */
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    /**
     * Order/priority of this label for the system design article (for sorting)
     */
    @Column(name = "display_order")
    private Integer displayOrder;

    /**
     * Constructor with required fields
     */
    public SystemDesignLabel(SystemDesign systemDesign, Label label, Boolean isPrimary, Integer displayOrder) {
        this.systemDesign = systemDesign;
        this.label = label;
        this.isPrimary = isPrimary != null ? isPrimary : false;
        this.displayOrder = displayOrder;
    }
}
