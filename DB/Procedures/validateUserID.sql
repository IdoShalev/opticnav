/* *********************************************************************
**	Description:
**
**  Returns:
********************************************************************* */

DROP FUNCTION IF EXISTS validateUserID;

DELIMITER //

CREATE FUNCTION validateUserID 
(p_acc_id INT(4))
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE id INT;
    SET id = 0;
    
    SELECT web_account_id INTO id
    FROM WEB_ACCOUNT
    WHERE p_acc_id = web_account_id;

    IF p_acc_id = id THEN
        RETURN true;
    END IF;

    RETURN false;
END//

DELIMITER ;
