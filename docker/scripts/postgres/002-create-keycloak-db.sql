-- Create admin database user for the it-employee database
CREATE USER keycloak_adm WITH ENCRYPTED PASSWORD 'keycloak_adm_jukilo90';

-- Create database for it-employee.
CREATE DATABASE keycloak OWNER keycloak_adm ENCODING 'UTF8';

-- Give all privileges to admin database user
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak_adm;
