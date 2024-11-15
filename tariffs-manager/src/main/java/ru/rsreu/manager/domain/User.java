package ru.rsreu.manager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "{valid.user.login.empty}")
    @Size(max = 15, message = "{valid.user.login.size}")
    private String login;

    @NotBlank(message = "{valid.user.password.empty}")
    @Size(max = 15, message = "{valid.user.password.size}")
    private String password;

    @NotBlank(message = "{valid.user.name.empty}")
    @Size(max = 255, message = "{valid.user.name.size}")
    private String name;

    @JoinColumn(name = "role_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Role role;

    private boolean isAuthorized;

    public User() {
    }

}
