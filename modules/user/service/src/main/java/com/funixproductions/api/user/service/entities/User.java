package com.funixproductions.api.user.service.entities;

import com.funixproductions.api.encryption.client.utils.EncryptionString;
import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "api_users")
@NoArgsConstructor
public class User extends ApiEntity implements UserDetails {

    public User(UserCreationDTO registerRequest) {
        this.username = registerRequest.getUsername();
        this.email = registerRequest.getEmail();
    }

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 2000)
    @Convert(converter = EncryptionString.class)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(columnDefinition = "boolean default false")
    private Boolean banned = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean valid = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserToken> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPasswordReset> passwordResets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserValidAccountToken> validTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (final String authority : role.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !banned;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !banned;
    }

    @Override
    public boolean isEnabled() {
        return !banned;
    }
}
