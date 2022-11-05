INSERT INTO roles (code, name, description, created_at, updated_at, enabled_at)
VALUES ('SYS_ADMIN', 'System administrator', 'System administrator', NOW(), NOW(), NOW()),
       ('SYS_USER', 'System user', 'System user', NOW(), NOW(), NOW()),
       ('APP_USER', 'Application user', 'Application user', NOW(), NOW(), NOW());