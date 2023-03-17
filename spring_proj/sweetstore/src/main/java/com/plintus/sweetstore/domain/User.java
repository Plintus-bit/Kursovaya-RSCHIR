package com.plintus.sweetstore.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
//    private String role;

    // Getters
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public boolean isActive() {
        return active;
    }
//    public String getRole() {
//        return role;
//    }
    public Set<Role> getRoles() {
        return roles;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
//    public void setRole(String role){
//        this.role = role;
//    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    // Override


}
