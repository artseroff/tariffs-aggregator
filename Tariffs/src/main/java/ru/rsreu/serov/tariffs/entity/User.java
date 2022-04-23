package ru.rsreu.serov.tariffs.entity;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    private String password;

    private String name;

    @OneToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isAuthorized;

    public boolean getIsAuthorized() {
        return isAuthorized;
    }


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
                ", username='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
