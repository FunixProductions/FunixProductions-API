package com.funixproductions.api.user.service.entities;

import com.funixproductions.api.user.client.dtos.requests.UserCreationDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.api.user.service.security.EncryptionBase64;
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
    @Convert(converter = EncryptionBase64.class)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(columnDefinition = "boolean default false")
    private Boolean banned = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean valid = false;

    @Column(nullable = false, name = "country_name", columnDefinition = "VARCHAR(255) DEFAULT 'France'")
    private String countryName;

    @Column(nullable = false, name = "country_code", columnDefinition = "INT DEFAULT 250")
    private Integer countryCode;

    @Column(nullable = false, name = "country_code_2_chars", columnDefinition = "VARCHAR(2) DEFAULT 'FR'")
    private String countryCode2Chars;

    @Column(nullable = false, name = "country_code_3_chars", columnDefinition = "VARCHAR(3) DEFAULT 'FRA'")
    private String countryCode3Chars;

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
