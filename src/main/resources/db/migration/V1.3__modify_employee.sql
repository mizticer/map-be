ALTER TABLE app.employee
    ADD COLUMN squad VARCHAR(100);

UPDATE app.employee
SET department = 'IT',
    squad      = '404_squad'
WHERE id = '550e8400-e29b-41d4-a716-446655440002';

UPDATE app.employee
SET department = 'HR',
    squad      = 'YOLO'
WHERE id = '550e8400-e29b-41d4-a716-446655440003';

UPDATE app.employee
SET department = 'Marketing',
    squad      = '404_squad'
WHERE id = '550e8400-e29b-41d4-a716-446655440004';