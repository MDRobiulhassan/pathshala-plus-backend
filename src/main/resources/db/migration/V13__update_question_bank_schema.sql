ALTER TABLE questions DROP COLUMN exam_type;
ALTER TABLE questions
    ALTER COLUMN question_text DROP NOT NULL;
ALTER TABLE passages
    ALTER COLUMN passage_text DROP NOT NULL;
ALTER TABLE question_options
    ALTER COLUMN option_text DROP NOT NULL;

CREATE TABLE exam_info
(
    id                  BIGSERIAL PRIMARY KEY,
    exam_session_id     BIGINT      NOT NULL REFERENCES exam_sessions (id),
    unit_id             BIGINT REFERENCES units (id),
    shift_id            BIGINT REFERENCES shifts (id),
    exam_type           VARCHAR(10) NOT NULL,
    total_marks         INTEGER     NOT NULL,
    duration_in_minutes INTEGER     NOT NULL,
    created_at          TIMESTAMP   NOT NULL,
    updated_at          TIMESTAMP   NOT NULL,
    created_by          BIGINT REFERENCES users (id),
    updated_by          BIGINT REFERENCES users (id),
    is_deleted          BOOLEAN     NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMP,
    deleted_by          BIGINT
);