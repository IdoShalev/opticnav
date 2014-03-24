/* *********************************************************************
**	Description:	
********************************************************************* */


DROP PROCEDURE IF EXISTS getUserList;

DELIMITER //

CREATE PROCEDURE getUserList 
()
BEGIN
    SELECT username, ard_id
    FROM WEB_ACCOUNT;
END//

DELIMITER ;
