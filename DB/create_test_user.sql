/* Drop the user and create a new user
 * This user will have all privileges for now */

DROP USER 'test'@localhost;

CREATE USER 'test'@localhost IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON OpticNavDB TO 'test'@localhost;
GRANT ALL PRIVILEGES ON testDB TO 'test'@localhost;

GRANT EXECUTE ON FUNCTION testDB.registerAccount TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION testDB.validateUser TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION testDB.findAccount TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION testDB.getARD TO 'test'@localhost;
GRANT EXECUTE ON FUNCTION testDB.validateUserID TO 'test'@localhost;

GRANT EXECUTE ON PROCEDURE testDB.setARD TO 'test'@localhost;
GRANT EXECUTE ON PROCEDURE testDB.removeARD TO 'test'@localhost;

GRANT SELECT ON `mysql`.`proc` TO 'test'@localhost;
