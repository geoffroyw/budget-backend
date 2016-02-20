package io.yac.auth.user.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Entity
@Table(name = "application_user")
public class User {

    private Long id;

    private String email;

    private String login;

    private String password;

    private Date lastLoginDate;

    private Set<Role> roles = new HashSet<>();

    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    private User(Long id, String login, String email, String password,
                 Set<Role> roles, Date lastLoginDate) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.lastLoginDate = lastLoginDate;
    }

    public User(User user) {
        super();
        this.id = user.id;
        this.login = user.login;
        this.password = user.password;
        this.email = user.email;
        this.lastLoginDate = user.lastLoginDate;
    }

    public static Builder builder() {
        return new Builder();
    }


    @PrePersist
    public void setInsertionAudit() {
        final Date now = new Date();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void setUpdateAudit() {
        final Date now = new Date();
        this.setUpdatedAt(now);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty
    @Column(unique = true, nullable = false)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @NotEmpty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")},
               inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @NotEmpty
    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "last_login_date")
    @Temporal(TemporalType.DATE)
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Column(name = "created_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, password, roles);
    }

    public static class Builder {
        private Long id;

        private String login;

        private String password;

        private Set<Role> roles = new HashSet<>();
        private String email;
        private Date lastLoginDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }


        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = new BCryptPasswordEncoder().encode(password);
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder lastLoginDate(Date date) {
            this.lastLoginDate = date;
            return this;
        }

        public User build() {
            return new User(id, login, email, password, roles, lastLoginDate);
        }

    }
}
