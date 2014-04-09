/* *********************************************************************
**	Description:    Gets the ard_id for the specified row in the WEB_ACCOUNT table
**  Parameter:      p_acc_id - The account id
**  Returns:        The ARD ID
********************************************************************* */

DROP FUNCTION IF EXISTS getARD;

DELIMITER //

CREATE FUNCTION getARD 
(p_acc_id INT(4))
RETURNS INT
READS SQL DATA
BEGIN
    RETURN (SELECT ard_id
            FROM WEB_ACCOUNT
            WHERE p_acc_id = web_account_id);
END//

DELIMITER ;
