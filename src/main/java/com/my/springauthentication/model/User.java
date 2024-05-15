package com.my.springauthentication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "BINARY(16)")
    protected UUID id;
    @Column(length = 150)
    private String firstname;
    @Column(length = 150)
    private String lastname;
    @Column(length = 250)
    private String email;
    @Column(length = 250)
    private String password;
    @Column(length = 50)
    private Role role;
    @Column(length = 10)
    private String language;
    @Column(length = 50)
    private String theme;
    @Column(length = 5)
    private Boolean validated;
    @Column(length = 10)
    private Integer verification;
    @Column(length = 50, updatable = false)
    @CreationTimestamp
    private Date created;
    @Column(length = 50)
    private Date logged;
    @Column(length = 50)
    @UpdateTimestamp
    private Date updated;

//    public User() {
//        this.uuid = UUID.randomUUID();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
