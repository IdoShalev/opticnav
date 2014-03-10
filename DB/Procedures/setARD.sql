/* *********************************************************************
**	Description:	Sets an ARD ID to te specified WEB_ACCOUNT
********************************************************************* */


DROP PROCEDURE IF EXISTS setARD;

DELIMITER //

CREATE PROCEDURE setARD 
(p_acc_id VARCHAR(25), p_ard_id INT(4))
BEGIN
    UPDATE WEB_ACCOUNT
    SET ard_id = p_ard_id
    WHERE web_account_id = p_acc_id;
END//

DELIMITER ;
