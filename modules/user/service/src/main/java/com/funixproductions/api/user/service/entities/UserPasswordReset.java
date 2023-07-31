package com.funixproductions.api.user.service.entities;

import com.funixproductions.api.core.enums.FrontOrigins;
import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "user_password_reset")
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordReset extends ApiEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, updatable = false, length = 2000, name = "reset_token")
    private String resetToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrontOrigins origin;

}
