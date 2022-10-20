INSERT INTO assignments (user_id, role_id, created_at, updated_at, enabled_at) VALUES
   ((SELECT id FROM users WHERE username = 'sysadmin'), (SELECT id FROM roles WHERE code = 'SYS_ADMIN'), NOW(),NOW(), NOW()),
   ((SELECT id FROM users WHERE username = 'sysadmin'), (SELECT id FROM roles WHERE code = 'SYS_USER'), NOW(),NOW(), NOW());