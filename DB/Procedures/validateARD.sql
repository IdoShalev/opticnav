/* *********************************************************************
**	Description:	Will check if the provided password
**                  matches a row in the database.
**  Returns:        0 if no match is found
**                  The ARD_id if a match is found
********************************************************************* */

DROP FUNCTION IF EXISTS validateARD;

DELIMITER //

CREATE FUNCTION validateARD 
(p_password VARCHAR(20))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE id INT;
    SET id = 0;

    SELECT ARD_id INTO id
        FROM ARD
        WHERE passcode = UNHEX(SHA1(p_password));

    RETURN id;
END//

DELIMITER ;
