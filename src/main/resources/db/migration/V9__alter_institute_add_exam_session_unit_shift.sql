ALTER TABLE questions
DROP
COLUMN institute_id;

DROP TABLE institutes;

CREATE TABLE institutes
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(150)             NOT NULL,
    short_name         VARCHAR(50),
    description        VARCHAR(50),
    education_level_id BIGINT REFERENCES education_levels (id),
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE,
    created_by         BIGINT,
    updated_by         BIGINT,
    is_deleted         BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at         TIMESTAMP WITH TIME ZONE,
    deleted_by         BIGINT
);

CREATE TABLE exam_sessions
(
    id            BIGSERIAL PRIMARY KEY,
    institute_id  BIGINT                   NOT NULL REFERENCES institutes (id),
    session_label VARCHAR(20)              NOT NULL,
    start_year    INT                      NOT NULL,
    end_year      INT,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE,
    created_by    BIGINT,
    updated_by    BIGINT,
    is_deleted    BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at    TIMESTAMP WITH TIME ZONE,
    deleted_by    BIGINT
);

CREATE TABLE units
(
    id           BIGSERIAL PRIMARY KEY,
    institute_id BIGINT                   NOT NULL REFERENCES institutes (id),
    name         VARCHAR(5)               NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    created_by   BIGINT,
    updated_by   BIGINT,
    is_deleted   BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at   TIMESTAMP WITH TIME ZONE,
    deleted_by   BIGINT
);

CREATE TABLE shifts
(
    id              BIGSERIAL PRIMARY KEY,
    exam_session_id BIGINT                   NOT NULL REFERENCES exam_sessions (id),
    unit_id         BIGINT                   NOT NULL REFERENCES units (id),
    shift_number    INT                      NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    created_by      BIGINT,
    updated_by      BIGINT,
    is_deleted      BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMP WITH TIME ZONE,
    deleted_by      BIGINT
);

ALTER TABLE questions
    ADD COLUMN exam_session_id BIGINT NOT NULL REFERENCES exam_sessions (id),
    ADD COLUMN unit_id         BIGINT REFERENCES units(id),
    ADD COLUMN shift_id        BIGINT REFERENCES shifts(id);