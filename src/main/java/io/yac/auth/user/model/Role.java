package io.yac.auth.user.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by geoffroy on 09/02/2016.
 */
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    private Long id;
    private RoleName name;
    private Set<User> users = new HashSet<>();

    @Override
    @Transient
    public String getAuthority() {
        return name.name();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public enum RoleName {
        USER, ADMIN
    }
}
