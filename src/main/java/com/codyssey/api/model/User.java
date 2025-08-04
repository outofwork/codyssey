package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * User Entity
 * <p>
 * Represents a user in the system with authentication
 * and profile information.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    /**
     * Username for authentication
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * User's email address
     */
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Encrypted password
     */
    @NotBlank
    @Size(max = 120)
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User's first name
     */
    @Size(max = 50)
    @Column(name = "first_name")
    private String firstName;

    /**
     * User's last name
     */
    @Size(max = 50)
    @Column(name = "last_name")
    private String lastName;

    /**
     * Whether the user account is enabled
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * User roles for authorization
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor with basic fields
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}