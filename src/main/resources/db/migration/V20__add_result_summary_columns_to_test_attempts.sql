ALTER TABLE test_attempts
    ADD COLUMN correct_answers INTEGER,

ADD COLUMN incorrect_answers INTEGER,

ADD COLUMN unanswered_questions INTEGER,

ADD COLUMN passed BOOLEAN;