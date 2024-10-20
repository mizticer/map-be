CREATE SCHEMA app;

CREATE USER dbuser WITH PASSWORD 'pass';
ALTER ROLE dbuser SET search_path = app;
GRANT ALL ON SCHEMA app TO dbuser;
