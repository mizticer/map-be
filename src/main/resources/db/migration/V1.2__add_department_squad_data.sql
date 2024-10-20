CREATE TABLE app.squad
(
    id         uuid primary key not null,
    squad_name varchar(100)     not null
);
CREATE TABLE app.department
(
    id              uuid primary key not null,
    department_name varchar(100)     not null
);

INSERT INTO app.squad (id, squad_name)
VALUES ('550e8400-e29b-41d4-a716-446655440000', '404_squad');

INSERT INTO app.squad (id, squad_name)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'Dream team');

INSERT INTO app.squad (id, squad_name)
VALUES ('550e8400-e29b-41d4-a716-446655440002', 'The best team');

INSERT INTO app.squad (id, squad_name)
VALUES ('550e8400-e29b-41d4-a716-446655440003', 'YOLO');

INSERT INTO app.department (id, department_name)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'IT');

INSERT INTO app.department (id, department_name)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'HR');

INSERT INTO app.department (id, department_name)
VALUES ('550e8400-e29b-41d4-a716-446655440002', 'Finance');

INSERT INTO app.department (id, department_name)
VALUES ('550e8400-e29b-41d4-a716-446655440003', 'Marketing');