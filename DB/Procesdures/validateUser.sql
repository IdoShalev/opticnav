/* *********************************************************************
**	Description:	Will check if the provided account name and password
**                  match a row in the database.
**  Returns:        0 if no match is found
**                  The account id if a match is found
********************************************************************* */

DROP FUNCTION IF EXISTS validateUser;

DELIMITER //

CREATE FUNCTION validateUser 
(p_accountName VARCHAR(25), p_password VARCHAR(20))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE id INT;
    SET id = 0;

    SELECT account_id INTO id
        FROM ACCOUNT
        WHERE pass = UNHEX(SHA1(p_password))
        AND user = p_accountName;

    RETURN id;
END//

DELIMITER ;
