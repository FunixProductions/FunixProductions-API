create table google_auth_link_user
(
    id             bigserial primary key,
    created_at     timestamp(6) not null,
    updated_at     timestamp(6),
    uuid           varchar(255) not null constraint uk_google_auth_link_user_public_id unique,
    google_user_id varchar(255) not null constraint uk_google_user_id_external_id unique,
    user_uuid      varchar(255) not null constraint uk_link_user_uuid_to_google unique
);
