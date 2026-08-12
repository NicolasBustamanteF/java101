-- Setup script for PostgreSQL database used by the project
-- Run this as a superuser (postgres) or a user with CREATEDB/CREATEROLE privileges

-- Crear la base de datos
CREATE DATABASE db_depositos;

-- Crear el usuario 'develop' con contraseña
CREATE USER develop WITH PASSWORD 'develop123';

-- Otorgar todos los privilegios en la base de datos al usuario develop
GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop;

-- Otorgar uso del esquema público al usuario develop (debe ejecutarse sobre la BD)
\connect db_depositos
GRANT USAGE ON SCHEMA public TO develop;

-- Volver a la conexión por defecto (opcional)
\connect postgres

-- Cambiar la contraseña del usuario postgres (opcional)
ALTER USER postgres WITH PASSWORD 'postgres';

-- Fin del script

