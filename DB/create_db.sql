/* Drop the database if it exists, and create a new database
 * All existing tables/procedures/etc are dropped, and a new clean database
 * is created in its place. */
DROP DATABASE IF EXISTS OpticNavDB;
CREATE DATABASE OpticNavDB;
use OpticNavDB;
