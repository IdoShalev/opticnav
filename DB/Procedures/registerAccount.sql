/* *********************************************************************
**	Description:	Will add a new row to the ACCOUNT table conating
**                  the provided account name and the provided password 
**                  in binary form
**  Parameter:      p_accountName - the username
**                  p_password - the password
**  Returns:        True if the WEB_ACCOUNT was created without error
**                  False if the username or password where empty strings
********************************************************************* */

DROP FUNCTION IF EXISTS registerAccount;

DELIMITER //

CREATE FUNCTION registerAccount 
(p_accountName VARCHAR(255), p_password VARCHAR(255))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE id INT;
    DECLARE flag BOOLEAN;
    DECLARE duplicate_user CONDITION FOR SQLSTATE '23000';
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        RETURN false;
    END;
    
    SET flag = true;

    IF (p_password = '')
    THEN
        SET flag = false;
    ELSE
        INSERT INTO WEB_ACCOUNT
        (username, pass)
        VALUES (p_accountName, UNHEX(SHA1(CONCAT(p_password,(SELECT `AUTO_INCREMENT`
                                                        FROM  INFORMATION_SCHEMA.TABLES
                                                        WHERE TABLE_SCHEMA = (SELECT DATABASE() FROM DUAL)
                                                        AND   TABLE_NAME   = 'WEB_ACCOUNT')))));
    END IF;    
    
    RETURN flag;
END//

DELIMITER ;
