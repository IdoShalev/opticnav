/* *********************************************************************
**	Description:	Will add a new row to the ACCOUNT table conating
**                  the provided account name, the provided password 
**                  in a hashed binary form, and an auto incementing
**                  account id.
********************************************************************* */

DROP FUNCTION IF EXISTS registerAccount;

DELIMITER //

CREATE FUNCTION registerAccount 
(p_accountName VARCHAR(25), p_password VARCHAR(20))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE duplicate_user CONDITION FOR SQLSTATE '23000';
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        RETURN false;
    END;
    INSERT INTO WEB_ACCOUNT
    (user, pass)
    VALUES (p_accountName, UNHEX(SHA1(p_password)));
    
    RETURN true;
END//

DELIMITER ;
