CREATE TYPE user_role AS ENUM ('ADMIN', 'STUDENT', 'TEACHER');

CREATE TABLE tbl_users
(
    id         BIGSERIAL PRIMARY KEY,
    uuid       UUID         NOT NULL UNIQUE,
    username   VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    role       user_role    NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);