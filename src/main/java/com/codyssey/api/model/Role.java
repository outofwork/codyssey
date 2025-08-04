package com.codyssey.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Role Entity
 * <p>
 * Represents user roles for authorization purposes.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    /**
     * Role name (e.g., ROLE_USER, ROLE_ADMIN)
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Role description
     */
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    /**
     * Constructor with role name
     */
    public Role(String name) {
        this.name = name;
    }
}