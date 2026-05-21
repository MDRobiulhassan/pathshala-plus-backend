ALTER TABLE education_levels
    ADD COLUMN department VARCHAR(10);

CREATE TABLE institutes
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(150) NOT NULL,
    unit               VARCHAR(5),
    shift              VARCHAR(10),
    description        VARCHAR(50),
    education_level_id BIGINT REFERENCES education_levels (id),
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP,
    created_by         BIGINT,
    updated_by         BIGINT,
    is_deleted         BOOLEAN DEFAULT FALSE,
    deleted_at         TIMESTAMP,
    deleted_by         BIGINT
);

ALTER TABLE questions
    ADD COLUMN institute_id BIGINT REFERENCES institutes (id);