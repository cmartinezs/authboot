INSERT INTO users (username, password, email, created_at, updated_at, enabled_at)
VALUES ('sysadmin', '$2a$12$VjwwQAGKoDFSHelEGnFrhuN2eqSjmHJ8dq84kgE65YNHk4M78Un3i', 'sysadmin@authboot.io', NOW(),
        NOW(), NOW());