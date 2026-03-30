CREATE TABLE IF NOT EXISTS passages (
    id BIGSERIAL PRIMARY KEY,
    passage_text TEXT NOT NULL,
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by BIGINT,
    updated_by BIGINT,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    deleted_by BIGINT
);

ALTER TABLE questions ADD COLUMN passage_id BIGINT REFERENCES passages(id);