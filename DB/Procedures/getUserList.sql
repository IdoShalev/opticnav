/* *********************************************************************
**	Description:	Gets a list of all users and the ard_ids bound to them
**  Returns:        A result set containing usernames and the ard_ids bound to them
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
