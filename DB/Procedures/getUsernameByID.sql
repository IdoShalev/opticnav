/* *********************************************************************
**	Description:    Gets the username of the account specified
**  Parameter:      p_acc_id - The account ID
**  Returns:        The username
********************************************************************* */

DROP FUNCTION IF EXISTS getUsernameByID;

DELIMITER //

CREATE FUNCTION getUsernameByID 
(p_acc_id INT(4))
RETURNS VARCHAR(255)
READS SQL DATA
BEGIN    
    RETURN (SELECT username
            FROM WEB_ACCOUNT
            WHERE p_acc_id = web_account_id);
END//

DELIMITER ;
