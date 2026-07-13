CREATE UNIQUE INDEX uq_test_attempts_active_user_test
    ON test_attempts (test_id, user_id)
    WHERE completed = false AND is_deleted = false;