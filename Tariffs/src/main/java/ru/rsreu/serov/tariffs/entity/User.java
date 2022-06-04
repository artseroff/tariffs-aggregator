package ru.rsreu.serov.tariffs.entity;


import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "{valid.user.login.empty}")
    @Size(max=15, message = "{valid.user.login.size}")
    private String login;

    @NotBlank(message = "{valid.user.password.empty}")
    @Size(max=15, message = "{valid.user.password.size}")
    //@Pattern(regexp = "[A-Za-z0-9]*", message = "{valid.user.password.pattern}")
    private String password;

    @NotBlank(message = "{valid.user.name.empty}")
    @Size(max=255, message = "{valid.user.name.size}")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private boolean isAuthorized;

    public User() {
    }

    public User(String login, String password, String name, Role role, boolean isAuthorized) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.isAuthorized = isAuthorized;
    }

    public User(String login, String password, String name, Role role) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public User(Long id, String login, String password, String name, Role role, boolean isAuthorized) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.isAuthorized = isAuthorized;
    }

    public User(Long id, String login, String password, String name, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String username) {
        this.login = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", isAuthorized=" + isAuthorized +
                '}';
    }
}
