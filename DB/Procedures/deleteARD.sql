/* *********************************************************************
**	Description:	Sets the ard_id of the specified WEB_ACCOUNT row to NULL
**  Parameter:      p_acc_id - The account ID of the ard_id to be set to NULL
**  Returns:        True if the ard_id was set to NULL, false otherwise
********************************************************************* */


DROP FUNCTION IF EXISTS deleteARD;

DELIMITER //

CREATE FUNCTION deleteARD 
(p_acc_id INT(4))
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE flag BOOLEAN;
    SET flag = false;
    IF (SELECT ard_id 
        FROM WEB_ACCOUNT
        WHERE p_acc_id = web_account_id) IS NOT NULL THEN
            UPDATE WEB_ACCOUNT
            SET ard_id = NULL
            WHERE p_acc_id = web_account_id;
            SET flag = true;
    END IF;
    RETURN flag;
END//

DELIMITER ;
