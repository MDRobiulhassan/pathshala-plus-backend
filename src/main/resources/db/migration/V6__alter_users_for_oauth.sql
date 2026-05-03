ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL,
    ALTER COLUMN preferred_language_id DROP NOT NULL;