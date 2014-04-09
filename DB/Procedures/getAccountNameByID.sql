/* *********************************************************************
**	Description:    Gets the username associated with the specified account_id
**  Parameter:      p_acc_id - The account_id
**  Returns:        The username
********************************************************************* */

DROP FUNCTION IF EXISTS getAccountNameByID;

DELIMITER //

CREATE FUNCTION getAccountNameByID 
(p_acc_id INT(4))
RETURNS VARCHAR(Web__MaxLength__USERNAME)
READS SQL DATA
BEGIN    
    RETURN (SELECT username
            FROM WEB_ACCOUNT
            WHERE p_acc_id = web_account_id);
END//

DELIMITER ;
