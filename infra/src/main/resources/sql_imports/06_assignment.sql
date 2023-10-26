INSERT INTO assignments (user_id, role_id, created_at, updated_at, enabled_at)
VALUES ((SELECT id FROM users WHERE username = 'sysadmin'), (SELECT id FROM roles WHERE code = 'SYS_ADMIN'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'sysadmin'), (SELECT id FROM roles WHERE code = 'SYS_USER'), NOW(), NOW(), NOW());

INSERT INTO assignments (user_id, role_id, created_at, updated_at, enabled_at)
VALUES ((SELECT id FROM users WHERE username = 'fgaray'), (SELECT id FROM roles WHERE code = 'RAC_SELLER'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'rulloa'), (SELECT id FROM roles WHERE code = 'RAC_CLIENT'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'ctroncoso'), (SELECT id FROM roles WHERE code = 'RAC_SELLER'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'gcampos'), (SELECT id FROM roles WHERE code = 'RAC_CLIENT'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'rentadmin'), (SELECT id FROM roles WHERE code = 'RAC_ADMIN'), NOW(), NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'rentventa'), (SELECT id FROM roles WHERE code = 'RAC_SELLER'), NOW(), NOW(), NOW());