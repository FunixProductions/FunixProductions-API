package com.funixproductions.api.google.auth.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "google_auth_link_user")
public class GoogleAuthLinkUser extends ApiEntity {

    @Column(name = "user_uuid", nullable = false, unique = true)
    private String userUuid;

    @Column(name = "google_user_id", unique = true, nullable = false)
    private String googleUserId;

}
