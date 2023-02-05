-- Create admin database user for the it-employee database
CREATE USER itemployee_adm WITH ENCRYPTED PASSWORD 'itemployee_adm_jukilo90';

-- Create database for it-employee.
CREATE DATABASE itemployee OWNER itemployee_adm ENCODING 'UTF8';

-- Give all privileges to admin database user
GRANT ALL PRIVILEGES ON DATABASE itemployee TO itemployee_adm;
