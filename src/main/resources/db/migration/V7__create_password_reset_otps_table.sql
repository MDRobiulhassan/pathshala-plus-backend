CREATE TABLE password_reset_otps
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id                BIGINT      NOT NULL REFERENCES users (id),
    otp                    VARCHAR(255),
    otp_expires_at         TIMESTAMP,
    reset_token            VARCHAR(255),
    reset_token_expires_at TIMESTAMP,
    is_used                BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at             TIMESTAMP   NOT NULL,
    updated_at             TIMESTAMP   NOT NULL,
    created_by             BIGINT,
    updated_by             BIGINT,
    is_deleted             BOOLEAN     NOT NULL DEFAULT FALSE,
    deleted_at             TIMESTAMP,
    deleted_by             BIGINT
);