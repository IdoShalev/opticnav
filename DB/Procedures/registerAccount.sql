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
        VALUES (p_accountName, UNHEX(SHA1('temppass')));

        SELECT web_account_id INTO id
            FROM WEB_ACCOUNT
            WHERE p_accountName = username;

        UPDATE WEB_ACCOUNT
            SET pass = UNHEX(SHA1(p_password + SHA1(id)))
            WHERE p_accountName = username;
    END IF;    
    
    RETURN flag;
END//

DELIMITER ;
