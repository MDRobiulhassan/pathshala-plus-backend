ALTER TABLE test_answers
    ADD CONSTRAINT uq_test_answers_attempt_question
        UNIQUE (attempt_id, question_id);