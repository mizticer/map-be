CREATE TABLE app.address
(
    id       uuid primary key not null,
    city     varchar(100)     not null,
    street   varchar(100)     not null,
    country  varchar(100)     not null,
    state    varchar(100)     not null,
    postcode varchar(10)      not null
);
CREATE TABLE app.employee
(
    id         uuid primary key not null,
    first_name varchar(50)      not null,
    last_name  varchar(50)      not null,
    photo      BYTEA            not null,
    role       varchar(50)      not null,
    department varchar(100)     not null,
    username   varchar(50)      not null,
    password   varchar(100)     not null,
    address    uuid REFERENCES app.address (id)
);

