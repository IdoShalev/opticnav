/* Drop the user and create a new user
 * This user will have all privileges for now */

DROP USER 'test'@localhost;

CREATE USER 'test'@localhost IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON OpticNavDB TO 'test'@localhost;
