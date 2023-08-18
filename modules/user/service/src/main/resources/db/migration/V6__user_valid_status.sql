ALTER TABLE api_users ADD COLUMN valid BOOLEAN DEFAULT false;

CREATE TABLE user_valid_account_token
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY primary key,
    uuid             VARCHAR(255)  NOT NULL CONSTRAINT uc_user_valid_account_token_uuid UNIQUE,
    created_at       TIMESTAMP     NOT NULL,
    updated_at       TIMESTAMP,
    user_id          BIGINT        NOT NULL CONSTRAINT FK_USER_VALID_ACCOUNT_TOKEN_ON_USER REFERENCES api_users,
    validation_token VARCHAR(2000) NOT NULL CONSTRAINT uc_user_valid_account_token_validation_token UNIQUE
);