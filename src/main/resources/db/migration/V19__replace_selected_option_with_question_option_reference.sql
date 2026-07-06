ALTER TABLE test_answers
DROP COLUMN selected_option;

ALTER TABLE test_answers
    ADD COLUMN selected_option_id BIGINT;

ALTER TABLE test_answers
    ADD CONSTRAINT fk_test_answers_selected_option
        FOREIGN KEY (selected_option_id)
            REFERENCES question_options(id);

ALTER TABLE test_answers
    ALTER COLUMN is_correct DROP NOT NULL;

ALTER TABLE test_answers
    ALTER COLUMN marks_awarded DROP NOT NULL;