/* *********************************************************************
**	File:  		    checkAccountName.sql
**	Author:  	    Kay Bernhardt
**	Created:	    Feburary 1, 2014
**	Description:	will check if the provided account name exists.
**  Returns:        a BOOLEAN. TRUE if the name exsits FALSE is it does not.
**	Update History:
********************************************************************* */

DROP FUNCTION IF EXISTS checkAccountName;

DELIMITER //

CREATE FUNCTION checkAccountName 
(p_accountName VARCHAR(25))
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE flag BOOLEAN;
    DECLARE id INT;
    SET id = 0;
    SET flag = true;

    SELECT account_id INTO id
    FROM ACCOUNT
    WHERE user = p_accountName;

    IF (id = 0) THEN
        SET flag = false;
    END IF;    

    RETURN flag;
END//

DELIMITER ;
