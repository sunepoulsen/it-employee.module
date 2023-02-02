-- Create admin database user for the it-employee database
CREATE USER it_employee_adm WITH ENCRYPTED PASSWORD 'it_employee_jukilo90';

-- Create database for it-employee.
CREATE DATABASE it_employee OWNER it_employee_adm ENCODING 'UTF8';

-- Give all privileges to admin database user
GRANT ALL PRIVILEGES ON DATABASE it_employee TO it_employee_adm;
