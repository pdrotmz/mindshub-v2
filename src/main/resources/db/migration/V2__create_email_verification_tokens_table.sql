CREATE TABLE tbl_email_verification_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    UUID                             NOT NULL,
    token      VARCHAR(255)                     NOT NULL UNIQUE,
    expires_at TIMESTAMP                        NOT NULL,
    created_at TIMESTAMP                        NOT NULL,
    used_at   TIMESTAMP                                 ,

    CONSTRAINT fk_email_verification_token_user
        FOREIGN KEY (user_id)
        REFERENCES tbl_users(uuid)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);