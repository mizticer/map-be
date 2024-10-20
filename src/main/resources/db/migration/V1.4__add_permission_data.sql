ALTER TABLE app.employee
ADD COLUMN permission VARCHAR(20);

UPDATE app.employee
SET permission = 'ADMIN'
WHERE id = '550e8400-e29b-41d4-a716-446655440002';

UPDATE app.employee
SET permission = 'ADMIN'
WHERE id = '550e8400-e29b-41d4-a716-446655440003';

UPDATE app.employee
SET permission = 'REGULAR'
WHERE id = '550e8400-e29b-41d4-a716-446655440004';