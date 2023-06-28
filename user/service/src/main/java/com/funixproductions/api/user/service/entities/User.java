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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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

    @Column(nullable = false)
    private Boolean banned = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserToken> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals(UserRole.ADMIN)) {
            final Set<SimpleGrantedAuthority> roles = new HashSet<>();

            for (final UserRole roleGet : UserRole.values()) {
                roles.add(new SimpleGrantedAuthority(roleGet.getRole()));
            }
            return roles;
        } else if (this.role.equals(UserRole.MODERATOR)) {
            return Set.of(
                    new SimpleGrantedAuthority(UserRole.USER.getRole()),
                    new SimpleGrantedAuthority(UserRole.MODERATOR.getRole())
            );
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority(UserRole.USER.getRole()));
        }
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
