
INSERT INTO app.address (id, city, street, state, country, postcode)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'City1', 'Street1', 'State1', 'Country1', '12345');

INSERT INTO app.address (id, city, street, state, country, postcode)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'City2', 'Street2', 'State2', 'Country2', '54321');

INSERT INTO app.address (id, city, street, state, country, postcode)
VALUES ('550e8400-e29b-41d4-a716-446655440005', 'City3', 'Street3', 'State3', 'Country3', '54323');


INSERT INTO app.employee (id, first_name, last_name, role, department, photo, username, password, address)
VALUES ('550e8400-e29b-41d4-a716-446655440002', 'John', 'Doe', 'Role1', 'Department1', E'\\x01020304',
        'John@gmail.com', '$2a$12$ZRY3liqpjsULnOj89GzXCOFL07JEmCar56vnDkF2wL7nKMBgNbZxe',
        '550e8400-e29b-41d4-a716-446655440000');

INSERT INTO app.employee (id, first_name, last_name, role, department, photo, username, password, address)
VALUES ('550e8400-e29b-41d4-a716-446655440003', 'Jane', 'Smith', 'Role2', 'Department2', E'\\x01020305',
        'Jane@gmail.com',
        '$2a$12$ZRY3liqpjsULnOj89GzXCOFL07JEmCar56vnDkF2wL7nKMBgNbZxe', '550e8400-e29b-41d4-a716-446655440001');

INSERT INTO app.employee (id, first_name, last_name, role, department, photo,username, password, address )
VALUES ('550e8400-e29b-41d4-a716-446655440004', 'Helena', 'Smith', 'Role3', 'Department3', E'\\x01020306',
        'Helena@gmail.com',
        '$2a$12$ZRY3liqpjsULnOj89GzXCOFL07JEmCar56vnDkF2wL7nKMBgNbZxe', '550e8400-e29b-41d4-a716-446655440005');

