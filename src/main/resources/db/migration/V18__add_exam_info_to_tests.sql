ALTER TABLE tests
    ADD COLUMN exam_info_id BIGINT;

ALTER TABLE tests
    ADD CONSTRAINT fk_tests_exam_info
        FOREIGN KEY (exam_info_id)
            REFERENCES exam_info (id);