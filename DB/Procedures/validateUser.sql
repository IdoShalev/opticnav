/* *********************************************************************
**	Description:	Will check if the provided account name and password
**                  match a row in the database.
**  Parameter:      p_accountName - The username
**                  p_password - The password
**  Returns:        0 if no match is found
**                  The account ID if a match is found
********************************************************************* */

DROP FUNCTION IF EXISTS validateUser;

DELIMITER //

CREATE FUNCTION validateUser 
(p_accountName VARCHAR(255), p_password VARCHAR(255))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE id INT;
    SET id = 0;

    SELECT web_account_id INTO id
        FROM WEB_ACCOUNT
        WHERE username = p_accountName
        AND pass = UNHEX(SHA1(CONCAT(p_password,(SELECT web_account_id
                                            FROM WEB_ACCOUNT
                                            WHERE p_accountName = username))));

    RETURN id;
END//

DELIMITER ;
