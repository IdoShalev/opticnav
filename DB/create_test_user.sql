/* Drop the user and create a new user
 * This user will have all privileges for now */

DROP USER 'test'@localhost;

CREATE USER 'test'@localhost IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON OpticNavDB TO 'test'@localhost;

GRANT EXECUTE ON FUNCTION OpticNavDB.registerAccount TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION OpticNavDB.validateUser TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION OpticNavDB.findAccount TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION OpticNavDB.getARD TO 'test'@localhost;

GRANT EXECUTE ON PROCEDURE OpticNavDB.setARD TO 'test'@localhost;
GRANT EXECUTE ON PROCEDURE OpticNavDB.removeARD TO 'test'@localhost;

GRANT SELECT ON `mysql`.`proc` TO 'test'@localhost;
