/* *********************************************************************
**	Description:	Removes an ARD_ID from the specified WEB_ACCOUNT
********************************************************************* */


DROP PROCEDURE IF EXISTS removeARD;

DELIMITER //

CREATE PROCEDURE removeARD 
(p_acc_id VARCHAR(25))
BEGIN
    UPDATE WEB_ACCOUNT
    SET ard_id = NULL
    WHERE p_acc_id = web_account_id;
END//

DELIMITER ;
