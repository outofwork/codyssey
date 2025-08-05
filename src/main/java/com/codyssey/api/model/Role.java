package com.codyssey.api.model;

import com.codyssey.api.util.RoleIdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
     * Primary key for the entity - Role ID (ROL-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "role-id")
    @GenericGenerator(name = "role-id", strategy = "com.codyssey.api.util.RoleIdGenerator")
    @Column(name = "id", length = 10)
    private String id;

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