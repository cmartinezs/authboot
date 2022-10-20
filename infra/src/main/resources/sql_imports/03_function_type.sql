INSERT INTO function_types (code, name, description, created_at, updated_at, enabled_at)
VALUES ('C', 'Create', 'Allows you to create data for the function', NOW(), NOW(), NOW()),
       ('R', 'Retrieve', 'Allows you to retrieve data for the function', NOW(), NOW(), NOW()),
       ('U', 'Update', 'Allows you to update data for the function', NOW(), NOW(), NOW()),
       ('D', 'Delete', 'Allows you to delete data for the function', NOW(), NOW(), NOW()),
       ('E', 'Execute', 'Allows you to execute function on the data', NOW(), NOW(), NOW());