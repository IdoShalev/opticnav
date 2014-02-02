/* *********************************************************************
**	File:  		    checkAccountName.sql
**	Author:  	    Kay Bernhardt
**	Created:	    Feburary 1, 2014
**	Description:	will add a new row to the ACCOUNT table conating
**                  the provided account name, the provided password 
**                  in a hashed binary form, and an auto incementing
**                  account id.
**	Update History:
********************************************************************* */

DROP PROCEDURE IF EXISTS registerAccount;

DELIMITER //

CREATE PROCEDURE registerAccount 
(p_accountName VARCHAR(25), p_password VARCHAR(20))
BEGIN
    INSERT INTO ACCOUNT
    (user, pass)
    VALUES (p_accountName, UNHEX(SHA1(p_password)));
END//

DELIMITER ;
