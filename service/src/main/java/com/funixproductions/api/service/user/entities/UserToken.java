package com.funixproductions.api.service.user.entities;

import com.funixproductions.api.service.core.encryption.EncryptionString;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity(name = "api_users_tokens")
public class UserToken extends ApiEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, updatable = false, length = 2000)
    @Convert(converter = EncryptionString.class)
    private String token;

    @Column(name = "expiration_date", updatable = false)
    private Date expirationDate;

    public Instant getExpirationDate() {
        if (expirationDate != null) {
            return expirationDate.toInstant();
        } else {
            return null;
        }
    }
}
