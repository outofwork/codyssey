package com.codyssey.api.model;

import com.codyssey.api.util.AlphanumericIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base Entity class for all JPA entities
 * <p>
 * This abstract class provides common fields and functionality
 * that all entities in the application should have, such as
 * ID, creation timestamp, and last modified timestamp.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * Primary key for the entity - 15-character alphanumeric ID
     */
    @Id
    @GeneratedValue(generator = "alphanumeric-id")
    @GenericGenerator(name = "alphanumeric-id", strategy = "com.codyssey.api.util.AlphanumericIdGenerator")
    @Column(name = "id", length = 15)
    private String id;

    /**
     * Timestamp when the entity was created
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last modified
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Version field for optimistic locking
     */
    @Version
    private Long version;

    /**
     * Soft delete flag
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}