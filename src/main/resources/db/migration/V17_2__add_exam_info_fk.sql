ALTER TABLE questions
    ADD CONSTRAINT fk_questions_exam_info
        FOREIGN KEY (exam_info_id)
            REFERENCES exam_info (id);